//! UniFFI 胶水层：把 sculk core 的 Rust 接口暴露给 Java（Project Panama FFM）。
//!
//! `build.rs` 调用 `uniffi::generate_scaffolding("src/sculk_ffi.udl")` 生成绑定脚手架；
//! Java 侧用 IronCoreLabs 的 `uniffi-bindgen-java` 0.5.2 从 DLL 生成调用代码。
//!
//! 注意：uniffi 0.32 的 UDL 模式下，脚手架里的占位类型会被 `udl_derive` / `export_for_udl`
//! 宏整体替换为转换实现，所以 **UDL 里声明的类型必须在这里手写定义**，且形状（变体、字段名与
//! 类型）必须与 UDL 完全一致。core 的类型全部是 `#[non_exhaustive]`，这里只能通过公开构造器
//! 与字段读取访问。

use std::fmt;
use std::net::SocketAddr;
use std::num::{NonZeroU16, NonZeroUsize};
use std::sync::{Arc, Mutex as StdMutex};
use std::time::Duration;

use sculk::ErrorCategory as CoreErrorCategory;
use sculk::minecraft::lan::{LanBroadcaster as CoreLanBroadcaster, LanScanner as CoreLanScanner};
use sculk::minecraft::probe_server as core_probe_server;
use sculk::tunnel::{
    AccessToken as CoreAccessToken, ConnectionSnapshot as CoreConnectionSnapshot,
    HostConfig as CoreHostConfig, HostOptions as CoreHostOptions, JoinConfig as CoreJoinConfig,
    JoinOptions as CoreJoinOptions, JoinUri as CoreJoinUri, LocalPort as CoreLocalPort,
    RelayUrl as CoreRelayUrl, SecretKey as CoreSecretKey,
    ServiceId as CoreServiceId, TunnelEvent as CoreTunnelEvent, TunnelMode as CoreTunnelMode,
    TunnelPhase as CoreTunnelPhase, TunnelService as CoreTunnelService,
    TunnelServiceError as CoreTunnelServiceError, TunnelState as CoreTunnelState,
    TunnelStatus as CoreTunnelStatus, TunnelSubscription as CoreTunnelSubscription,
    TunnelUpdate as CoreTunnelUpdate,
};

uniffi::include_scaffolding!("sculk_ffi");

// ---------------------------------------------------------------------------
// UDL 类型定义（uniffi 0.32 UDL 模式要求用户侧自行定义，形状与 UDL 一一对应）
// ---------------------------------------------------------------------------

/// 隧道生命周期阶段。
pub enum TunnelPhase {
    Idle {},
    Starting {},
    Active {},
    Stopping {},
}

/// 隧道运行角色。
pub enum TunnelMode {
    Host {},
    Join {},
}

/// 隧道运行时事件种类。
pub enum EventKind {
    PlayerJoined {},
    PlayerLeft {},
    Connected {},
    Disconnected {},
    PathChanged {},
    Reconnecting {},
    Reconnected {},
    TokenRotated {},
    TokenRotationFailed {},
    AuthFailed {},
    PlayerRejected {},
    Error {},
}

/// 稳定错误分类。
pub enum ErrorCategory {
    InvalidJoinUri {},
    InvalidEndpoint {},
    AuthorizationDenied {},
    HostUnreachable {},
    TargetUnavailable {},
    LocalPortUnavailable {},
    IdentityUnavailable {},
    OperationConflict {},
    ResourceLimit {},
    InvalidConfiguration {},
    Internal {},
}

/// Host 端行为配置。
pub struct HostConfig {
    /// PathChanged 事件节流（毫秒）；0 = 仅变化时发送。
    pub event_delay_ms: u64,
    /// 最大玩家数；None = 不限制。
    pub max_players: Option<u32>,
}

/// Join 端行为配置。
pub struct JoinConfig {
    /// PathChanged 事件节流（毫秒）；0 = 仅变化时发送。
    pub event_delay_ms: u64,
    /// 断线重连最长等待（毫秒）；None = 无限等待，Some(0) = 关闭重连。
    pub reconnect_timeout_ms: Option<u64>,
    /// 本地 TCP 转发会话上限；0 = 使用默认值 64。
    pub local_sessions_max: u64,
}

/// Host 启动参数。
pub struct HostOptions {
    /// 本地 Minecraft 服务端端口。
    pub mc_port: u16,
    /// 稳定节点密钥（32 字节）；None = 生成临时密钥。
    pub secret_key: Option<Vec<u8>>,
    /// 自定义中继 URL；None = 使用 iroh 默认中继。
    pub relay_url: Option<String>,
    /// 服务标识（16 字节）；None = 自动生成。
    pub service_id: Option<Vec<u8>>,
    /// 会话访问令牌（32 字节）；None = 自动生成。
    pub token: Option<Vec<u8>>,
    /// Host 行为配置。
    pub config: HostConfig,
}

/// Join 启动参数。
pub struct JoinOptions {
    /// `sculk://join/v1/` 分享 URI。
    pub join_uri: String,
    /// 本地 Minecraft 客户端连接端口；0 = 自动分配。
    pub local_port: u16,
    /// Join 行为配置。
    pub config: JoinConfig,
}

/// 轻量生命周期快照。
pub struct TunnelState {
    pub phase: TunnelPhase,
    pub mode: Option<TunnelMode>,
    /// 分享用的 join URI（仅 host 态有值）。
    pub join_uri: Option<String>,
    pub local_addr: Option<String>,
}

/// 单条连接瞬时快照。
pub struct ConnectionSnapshot {
    pub remote_id: String,
    pub is_relay: bool,
    pub rtt_ms: u64,
    pub tx_bytes: u64,
    pub rx_bytes: u64,
    pub alive: bool,
    pub elapsed_ms: u64,
}

/// 完整隧道快照。
pub struct TunnelStatus {
    pub state: TunnelState,
    pub connections: Vec<ConnectionSnapshot>,
    pub last_error: Option<ErrorCategory>,
}

/// 拍平的隧道事件记录：`kind` 决定哪些字段有效。
pub struct TunnelEvent {
    pub kind: EventKind,
    pub player_id: Option<String>,
    pub reason: Option<String>,
    pub remote_id: Option<String>,
    pub is_relay: bool,
    pub rtt_ms: u64,
    pub attempt: u32,
    pub retry_in_ms: Option<u64>,
    pub category: Option<ErrorCategory>,
    pub message: Option<String>,
}

/// 统一更新：状态快照或过程事件。
pub struct TunnelUpdate {
    pub is_event: bool,
    pub status: Option<TunnelStatus>,
    pub event: Option<TunnelEvent>,
}

// ---------------------------------------------------------------------------
// 统一错误（flat error：变体本身不带数据，Display 文案随异常一起传到 Java）
// ---------------------------------------------------------------------------

#[derive(Debug)]
pub enum ScuLkError {
    Busy {},
    NotRunning {},
    InvalidPort {},
    InvalidMaxPlayers {},
    Io {},
    InvalidJoinUri {},
    InvalidEndpoint {},
    AuthorizationDenied {},
    HostUnreachable {},
    TargetUnavailable {},
    LocalPortUnavailable {},
    IdentityUnavailable {},
    OperationConflict {},
    ResourceLimit {},
    InvalidConfiguration {},
    Internal {},
}

impl fmt::Display for ScuLkError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::Busy {} => f.write_str("a tunnel is already starting, active, or stopping"),
            Self::NotRunning {} => f.write_str("no tunnel is running"),
            Self::InvalidPort {} => f.write_str("port must be non-zero"),
            Self::InvalidMaxPlayers {} => f.write_str("max players must be greater than zero"),
            Self::Io {} => f.write_str("I/O error"),
            Self::InvalidJoinUri {} => f.write_str("invalid join uri"),
            Self::InvalidEndpoint {} => f.write_str("invalid endpoint or relay url"),
            Self::AuthorizationDenied {} => f.write_str("authorization denied"),
            Self::HostUnreachable {} => f.write_str("host unreachable"),
            Self::TargetUnavailable {} => f.write_str("target unavailable"),
            Self::LocalPortUnavailable {} => f.write_str("local port unavailable"),
            Self::IdentityUnavailable {} => f.write_str("identity unavailable"),
            Self::OperationConflict {} => f.write_str("operation conflict"),
            Self::ResourceLimit {} => f.write_str("resource limit reached"),
            Self::InvalidConfiguration {} => f.write_str("invalid configuration"),
            Self::Internal {} => f.write_str("internal error"),
        }
    }
}

impl std::error::Error for ScuLkError {}

/// core 的隧道服务错误 -> FFI 错误。
fn map_service_error(error: CoreTunnelServiceError) -> ScuLkError {
    match error {
        CoreTunnelServiceError::Busy => ScuLkError::Busy {},
        CoreTunnelServiceError::NotRunning => ScuLkError::NotRunning {},
        CoreTunnelServiceError::InvalidPort => ScuLkError::InvalidPort {},
        CoreTunnelServiceError::InvalidMaxPlayers => ScuLkError::InvalidMaxPlayers {},
        CoreTunnelServiceError::Tunnel(error) => map_category(error.category()),
        _ => ScuLkError::Internal {},
    }
}

/// core 的错误分类 -> FFI 错误。
fn map_category(category: CoreErrorCategory) -> ScuLkError {
    match category {
        CoreErrorCategory::InvalidJoinUri => ScuLkError::InvalidJoinUri {},
        CoreErrorCategory::InvalidEndpoint => ScuLkError::InvalidEndpoint {},
        CoreErrorCategory::AuthorizationDenied => ScuLkError::AuthorizationDenied {},
        CoreErrorCategory::HostUnreachable => ScuLkError::HostUnreachable {},
        CoreErrorCategory::TargetUnavailable => ScuLkError::TargetUnavailable {},
        CoreErrorCategory::LocalPortUnavailable => ScuLkError::LocalPortUnavailable {},
        CoreErrorCategory::IdentityUnavailable => ScuLkError::IdentityUnavailable {},
        CoreErrorCategory::OperationConflict => ScuLkError::OperationConflict {},
        CoreErrorCategory::ResourceLimit => ScuLkError::ResourceLimit {},
        CoreErrorCategory::InvalidConfiguration => ScuLkError::InvalidConfiguration {},
        CoreErrorCategory::Internal => ScuLkError::Internal {},
        _ => ScuLkError::Internal {},
    }
}

// ---------------------------------------------------------------------------
// 常量与自由函数
// ---------------------------------------------------------------------------

/// Minecraft 服务端标准端口。
pub fn default_mc_port() -> u16 {
    sculk::DEFAULT_MC_PORT
}

/// join 端本地入站监听端口默认值。
pub fn default_inlet_port() -> u16 {
    sculk::DEFAULT_INLET_PORT
}

/// 用旧版服务器列表探测（0xFE/0xFF）确认目标端口是 Minecraft Java 版服务。
pub fn probe_server(addr: String, timeout_ms: u64) -> Result<(), ScuLkError> {
    let socket_addr: SocketAddr = addr.parse().map_err(|_| ScuLkError::Io {})?;
    core_probe_server(socket_addr, Duration::from_millis(timeout_ms)).map_err(|_| ScuLkError::Io {})
}

/// 开始监听 Minecraft LAN 公告（224.0.2.60:4445）。
pub fn lan_scanner_start() -> Result<Arc<LanScanner>, ScuLkError> {
    let scanner = CoreLanScanner::start().map_err(|_| ScuLkError::Io {})?;
    Ok(Arc::new(LanScanner {
        inner: StdMutex::new(Some(scanner)),
    }))
}

/// 向本机 Minecraft 客户端周期性发布 LAN 公告。
pub fn lan_broadcaster_start(name: String, port: u16) -> Result<Arc<LanBroadcaster>, ScuLkError> {
    let port = NonZeroU16::new(port).ok_or(ScuLkError::InvalidPort {})?;
    let broadcaster = CoreLanBroadcaster::start(&name, port).map_err(|_| ScuLkError::Io {})?;
    Ok(Arc::new(LanBroadcaster {
        inner: StdMutex::new(Some(broadcaster)),
    }))
}

// ---------------------------------------------------------------------------
// TunnelService
// ---------------------------------------------------------------------------

/// 单实例托管隧道服务。
pub struct TunnelService {
    inner: CoreTunnelService,
}

impl TunnelService {
    /// 注意：UDL 模式下脚手架会用 `Arc::new(...)` 包住构造器返回值，所以这里返回 `Self`。
    pub fn new() -> Self {
        Self {
            inner: CoreTunnelService::new(),
        }
    }

    pub async fn start_host(&self, options: HostOptions) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        let options = to_core_host_options(options)?;
        inner.start_host(options).await.map_err(map_service_error)
    }

    pub async fn start_join(&self, options: JoinOptions) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        let options = to_core_join_options(options)?;
        inner.start_join(options).await.map_err(map_service_error)
    }

    pub async fn stop(&self) -> Result<TunnelStatus, ScuLkError> {
        let inner = self.inner.clone();
        inner
            .stop()
            .await
            .map(to_ffi_status)
            .map_err(map_service_error)
    }

    pub async fn shutdown(&self) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        inner.shutdown().await.map_err(map_service_error)
    }

    pub fn status(&self) -> TunnelStatus {
        to_ffi_status(self.inner.status())
    }

    pub fn subscribe(&self) -> Arc<Subscription> {
        Arc::new(Subscription {
            inner: tokio::sync::Mutex::new(self.inner.subscribe()),
        })
    }
}

// ---------------------------------------------------------------------------
// Subscription
// ---------------------------------------------------------------------------

/// 统一订阅端：首次 recv 返回当前状态，之后状态/事件交错。
pub struct Subscription {
    inner: tokio::sync::Mutex<CoreTunnelSubscription>,
}

impl Subscription {
    pub async fn recv(&self) -> Result<Option<TunnelUpdate>, ScuLkError> {
        let mut subscription = self.inner.lock().await;
        Ok(subscription.recv().await.map(to_ffi_update))
    }
}

// ---------------------------------------------------------------------------
// LAN 扫描与广播（core 的 stop 消耗 self，这里用 Mutex<Option> 包装）
// ---------------------------------------------------------------------------

/// LAN 公告扫描器。
pub struct LanScanner {
    inner: StdMutex<Option<CoreLanScanner>>,
}

impl LanScanner {
    pub fn try_recv(&self) -> Option<u16> {
        let guard = self.inner.lock().ok()?;
        let scanner = guard.as_ref()?;
        scanner.try_recv().ok().map(NonZeroU16::get)
    }

    pub fn is_finished(&self) -> bool {
        match self.inner.lock() {
            Ok(guard) => guard
                .as_ref()
                .map(CoreLanScanner::is_finished)
                .unwrap_or(true),
            Err(_) => true,
        }
    }

    pub fn stop(&self) -> Result<(), ScuLkError> {
        let mut guard = self.inner.lock().map_err(|_| ScuLkError::Io {})?;
        if let Some(scanner) = guard.take() {
            scanner.stop().map_err(|_| ScuLkError::Io {})?;
        }
        Ok(())
    }
}

/// LAN 公告广播器。
pub struct LanBroadcaster {
    inner: StdMutex<Option<CoreLanBroadcaster>>,
}

impl LanBroadcaster {
    pub fn is_finished(&self) -> bool {
        match self.inner.lock() {
            Ok(guard) => guard
                .as_ref()
                .map(CoreLanBroadcaster::is_finished)
                .unwrap_or(true),
            Err(_) => true,
        }
    }

    pub fn stop(&self) -> Result<(), ScuLkError> {
        let mut guard = self.inner.lock().map_err(|_| ScuLkError::Io {})?;
        if let Some(broadcaster) = guard.take() {
            broadcaster.stop().map_err(|_| ScuLkError::Io {})?;
        }
        Ok(())
    }
}

// ---------------------------------------------------------------------------
// FFI -> core 配置转换
// ---------------------------------------------------------------------------

fn to_core_host_config(config: HostConfig) -> CoreHostConfig {
    CoreHostConfig::new()
        .event_delay(Duration::from_millis(config.event_delay_ms))
        .max_players(config.max_players)
}

fn to_core_join_config(config: JoinConfig) -> CoreJoinConfig {
    CoreJoinConfig::new()
        .event_delay(Duration::from_millis(config.event_delay_ms))
        .reconnect_timeout(config.reconnect_timeout_ms.map(Duration::from_millis))
        .local_sessions_max(parse_sessions_max(config.local_sessions_max))
}

/// `0` 表示使用 core 默认值 64。
fn parse_sessions_max(value: u64) -> NonZeroUsize {
    let value = if value == 0 { 64 } else { value };
    usize::try_from(value)
        .ok()
        .and_then(NonZeroUsize::new)
        .unwrap_or(NonZeroUsize::MIN)
}

fn to_core_host_options(options: HostOptions) -> Result<CoreHostOptions, ScuLkError> {
    let mut core = CoreHostOptions::new(options.mc_port);

    if let Some(bytes) = &options.secret_key {
        let key: [u8; 32] = bytes
            .as_slice()
            .try_into()
            .map_err(|_| ScuLkError::InvalidConfiguration {})?;
        core = core.secret_key(Some(CoreSecretKey::from_bytes(&key)));
    }

    if let Some(url) = &options.relay_url {
        let relay = url
            .parse::<CoreRelayUrl>()
            .map_err(|_| ScuLkError::InvalidEndpoint {})?;
        core = core.relay_url(Some(relay));
    }

    let service_id = match &options.service_id {
        Some(bytes) => {
            let bytes: [u8; 16] = bytes
                .as_slice()
                .try_into()
                .map_err(|_| ScuLkError::InvalidConfiguration {})?;
            CoreServiceId::from_bytes(bytes)
        }
        None => CoreServiceId::generate(),
    };

    let token = match &options.token {
        Some(bytes) => {
            let bytes: [u8; 32] = bytes
                .as_slice()
                .try_into()
                .map_err(|_| ScuLkError::InvalidConfiguration {})?;
            CoreAccessToken::from_bytes(bytes)
        }
        None => CoreAccessToken::generate(),
    };

    Ok(core
        .service_id(service_id)
        .token(token)
        .config(to_core_host_config(options.config)))
}

fn to_core_join_options(options: JoinOptions) -> Result<CoreJoinOptions, ScuLkError> {
    let uri: CoreJoinUri = options
        .join_uri
        .parse()
        .map_err(|_| ScuLkError::InvalidJoinUri {})?;

    let mut core = CoreJoinOptions::new(uri).config(to_core_join_config(options.config));
    if options.local_port != 0 {
        let port = NonZeroU16::new(options.local_port).ok_or(ScuLkError::InvalidPort {})?;
        core = core.local_port(CoreLocalPort::Fixed(port));
    }
    Ok(core)
}

// ---------------------------------------------------------------------------
// core -> FFI 状态/事件转换
// ---------------------------------------------------------------------------

fn to_ffi_category(category: CoreErrorCategory) -> ErrorCategory {
    match category {
        CoreErrorCategory::InvalidJoinUri => ErrorCategory::InvalidJoinUri {},
        CoreErrorCategory::InvalidEndpoint => ErrorCategory::InvalidEndpoint {},
        CoreErrorCategory::AuthorizationDenied => ErrorCategory::AuthorizationDenied {},
        CoreErrorCategory::HostUnreachable => ErrorCategory::HostUnreachable {},
        CoreErrorCategory::TargetUnavailable => ErrorCategory::TargetUnavailable {},
        CoreErrorCategory::LocalPortUnavailable => ErrorCategory::LocalPortUnavailable {},
        CoreErrorCategory::IdentityUnavailable => ErrorCategory::IdentityUnavailable {},
        CoreErrorCategory::OperationConflict => ErrorCategory::OperationConflict {},
        CoreErrorCategory::ResourceLimit => ErrorCategory::ResourceLimit {},
        CoreErrorCategory::InvalidConfiguration => ErrorCategory::InvalidConfiguration {},
        CoreErrorCategory::Internal => ErrorCategory::Internal {},
        _ => ErrorCategory::Internal {},
    }
}

fn to_ffi_phase(phase: CoreTunnelPhase) -> TunnelPhase {
    match phase {
        CoreTunnelPhase::Idle => TunnelPhase::Idle {},
        CoreTunnelPhase::Starting => TunnelPhase::Starting {},
        CoreTunnelPhase::Active => TunnelPhase::Active {},
        CoreTunnelPhase::Stopping => TunnelPhase::Stopping {},
    }
}

fn to_ffi_mode(mode: CoreTunnelMode) -> TunnelMode {
    match mode {
        CoreTunnelMode::Host => TunnelMode::Host {},
        CoreTunnelMode::Join => TunnelMode::Join {},
    }
}

fn to_ffi_state(state: CoreTunnelState) -> TunnelState {
    TunnelState {
        phase: to_ffi_phase(state.phase),
        mode: state.mode.map(to_ffi_mode),
        // 分享 URI 是 UI 需要给用户复制/展示的字符串（token 本来就在 URI 里）。
        join_uri: state.join_uri.and_then(|uri| uri.expose_secret_uri().ok()),
        local_addr: state.local_addr.map(|addr| addr.to_string()),
    }
}

fn to_ffi_snapshot(snapshot: CoreConnectionSnapshot) -> ConnectionSnapshot {
    ConnectionSnapshot {
        remote_id: snapshot.remote_id.to_string(),
        is_relay: snapshot.is_relay,
        rtt_ms: snapshot.rtt_ms,
        tx_bytes: snapshot.tx_bytes,
        rx_bytes: snapshot.rx_bytes,
        alive: snapshot.alive,
        elapsed_ms: snapshot.elapsed.as_millis() as u64,
    }
}

fn to_ffi_status(status: CoreTunnelStatus) -> TunnelStatus {
    TunnelStatus {
        state: to_ffi_state(status.state),
        connections: status
            .connections
            .into_iter()
            .map(to_ffi_snapshot)
            .collect(),
        last_error: status.last_error.map(to_ffi_category),
    }
}

fn empty_event() -> TunnelEvent {
    TunnelEvent {
        kind: EventKind::Connected {},
        player_id: None,
        reason: None,
        remote_id: None,
        is_relay: false,
        rtt_ms: 0,
        attempt: 0,
        retry_in_ms: None,
        category: None,
        message: None,
    }
}

fn to_ffi_event(event: CoreTunnelEvent) -> TunnelEvent {
    let empty = empty_event;

    match event {
        CoreTunnelEvent::PlayerJoined { id } => TunnelEvent {
            kind: EventKind::PlayerJoined {},
            player_id: Some(id.to_string()),
            ..empty()
        },
        CoreTunnelEvent::PlayerLeft { id, reason } => TunnelEvent {
            kind: EventKind::PlayerLeft {},
            player_id: Some(id.to_string()),
            reason: Some(reason),
            ..empty()
        },
        CoreTunnelEvent::Connected => TunnelEvent {
            kind: EventKind::Connected {},
            ..empty()
        },
        CoreTunnelEvent::Disconnected { reason } => TunnelEvent {
            kind: EventKind::Disconnected {},
            reason: Some(reason),
            ..empty()
        },
        CoreTunnelEvent::PathChanged {
            remote_id,
            is_relay,
            rtt_ms,
        } => TunnelEvent {
            kind: EventKind::PathChanged {},
            remote_id: Some(remote_id.to_string()),
            is_relay,
            rtt_ms,
            ..empty()
        },
        CoreTunnelEvent::Reconnecting { attempt } => TunnelEvent {
            kind: EventKind::Reconnecting {},
            attempt,
            ..empty()
        },
        CoreTunnelEvent::Reconnected => TunnelEvent {
            kind: EventKind::Reconnected {},
            ..empty()
        },
        CoreTunnelEvent::TokenRotated => TunnelEvent {
            kind: EventKind::TokenRotated {},
            ..empty()
        },
        CoreTunnelEvent::TokenRotationFailed { retry_in } => TunnelEvent {
            kind: EventKind::TokenRotationFailed {},
            retry_in_ms: Some(retry_in.as_millis() as u64),
            ..empty()
        },
        CoreTunnelEvent::AuthFailed { id } => TunnelEvent {
            kind: EventKind::AuthFailed {},
            player_id: Some(id.to_string()),
            ..empty()
        },
        CoreTunnelEvent::PlayerRejected { id, reason } => TunnelEvent {
            kind: EventKind::PlayerRejected {},
            player_id: Some(id.to_string()),
            reason: Some(reason),
            ..empty()
        },
        CoreTunnelEvent::Error {
            category,
            message,
        } => TunnelEvent {
            kind: EventKind::Error {},
            category: Some(to_ffi_category(category)),
            message: Some(message),
            ..empty()
        },
        _ => TunnelEvent {
            kind: EventKind::Error {},
            category: Some(ErrorCategory::Internal {}),
            message: Some("unknown tunnel event variant".to_owned()),
            ..empty()
        },
    }
}

fn to_ffi_update(update: CoreTunnelUpdate) -> TunnelUpdate {
    match update {
        CoreTunnelUpdate::Status(status) => TunnelUpdate {
            is_event: false,
            status: Some(to_ffi_status(status)),
            event: None,
        },
        CoreTunnelUpdate::Event(event) => TunnelUpdate {
            is_event: true,
            status: None,
            event: Some(to_ffi_event(event)),
        },
        _ => TunnelUpdate {
            is_event: true,
            status: None,
            event: Some(TunnelEvent {
                kind: EventKind::Error {},
                category: Some(ErrorCategory::Internal {}),
                message: Some("unknown tunnel update variant".to_owned()),
                ..empty_event()
            }),
        },
    }
}
