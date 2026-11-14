//! UniFFI 胶水层：把 sculk core 的 Rust 接口暴露给 Java（Project Panama FFM）。
//!
//! `build.rs` 调用 `uniffi::generate_scaffolding("src/sculk_ffi.udl")` 生成绑定脚手架；
//! Java 侧用 IronCoreLabs 的 `uniffi-bindgen-java` 0.5.2 从 UDL 生成调用代码。
//!
//! 注意：uniffi 0.32 的 UDL 模式下，脚手架里的占位类型会被 `udl_derive` / `export_for_udl`
//! 宏整体替换为转换实现，所以 **UDL 里声明的类型必须在这里手写定义**，且形状（变体、字段名与
//! 类型）必须与 UDL 完全一致。core 的类型全部是 `#[non_exhaustive]`，这里只能通过公开构造器
//! 与字段读取访问。

use std::fmt;
use std::net::SocketAddr;
use std::num::{NonZeroU16, NonZeroUsize};
use std::path::Path;
use std::sync::{Arc, Mutex as StdMutex};
use std::time::{Duration, SystemTime, UNIX_EPOCH};

use async_compat::Compat;

use sculk::ErrorCategory as CoreErrorCategory;
use sculk::minecraft::lan::{LanBroadcaster as CoreLanBroadcaster, LanScanner as CoreLanScanner};
use sculk::minecraft::probe_server as core_probe_server;
use sculk::persist::{
    HostProfile as CoreHostProfile, HostState as CoreHostState, JoinProfile as CoreJoinProfile,
    Profile as CoreProfile, RelayProfile as CoreRelayProfile,
    TokenRefreshSetting as CoreTokenRefreshSetting, data_dir as core_data_dir,
    default_host_state_path as core_default_host_state_path,
    default_key_path as core_default_key_path, generate_new_key as core_generate_new_key,
    load_host_state as core_load_host_state, load_or_generate_key as core_load_or_generate_key,
    save_host_state as core_save_host_state,
};
use sculk::tunnel::{
    AccessToken as CoreAccessToken, ConnectionSnapshot as CoreConnectionSnapshot,
    HostConfig as CoreHostConfig, HostOptions as CoreHostOptions,
    HostedServiceHandle as CoreHostedServiceHandle, HostedServiceOptions as CoreHostedServiceOptions,
    HostedServicePhase as CoreHostedServicePhase, HostedServiceStatus as CoreHostedServiceStatus,
    HostedServiceStatusSubscription as CoreHostedServiceStatusSubscription,
    JoinConfig as CoreJoinConfig, JoinOptions as CoreJoinOptions, JoinUri as CoreJoinUri,
    LocalPort as CoreLocalPort, NodeOptions as CoreNodeOptions, RelayUrl as CoreRelayUrl,
    SculkNode as CoreSculkNode, SculkNodeError as CoreSculkNodeError,
    SculkNodeStatus as CoreSculkNodeStatus, SecretKey as CoreSecretKey, ServiceId as CoreServiceId,
    TokenRefreshPolicy as CoreTokenRefreshPolicy, TokenState as CoreTokenState,
    TunnelEvent as CoreTunnelEvent, TunnelMode as CoreTunnelMode, TunnelPhase as CoreTunnelPhase,
    TunnelService as CoreTunnelService, TunnelServiceError as CoreTunnelServiceError,
    TunnelState as CoreTunnelState, TunnelStatus as CoreTunnelStatus,
    TunnelSubscription as CoreTunnelSubscription, TunnelUpdate as CoreTunnelUpdate,
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

/// 多服务节点的创建参数。
pub struct NodeOptions {
    /// 稳定节点密钥（32 字节）；None = 生成临时身份。
    pub secret_key: Option<Vec<u8>>,
    /// Node 级自定义中继 URL；None = 使用 iroh 默认中继。
    pub relay_url: Option<String>,
    /// 同时等待认证的连接上限；0 = 使用默认值 128。
    pub unauthenticated_connections_max: u64,
}

/// 需要由实例管理层安全持久化的访问令牌状态。
pub struct TokenState {
    /// 访问令牌（32 字节）。
    pub token: Vec<u8>,
    /// 令牌创建时刻（Unix 毫秒）。
    pub created_at_ms: u64,
}

/// 令牌轮换策略种类。
pub enum TokenRefreshKind {
    Always {},
    Never {},
    After {},
}

/// 跨服务停止和重启生效的令牌刷新策略。
pub struct TokenRefreshPolicy {
    /// 策略种类。
    pub kind: TokenRefreshKind,
    /// `After` 的轮换周期（毫秒）；其他种类忽略。
    pub period_ms: u64,
}

/// 发布单个 Host 服务的参数。
pub struct HostedServiceOptions {
    /// 稳定的服务路由标识（16 字节）。
    pub service_id: Vec<u8>,
    /// 仅允许本机 loopback 的目标 Minecraft 地址，如 `127.0.0.1:25565`。
    pub target_addr: String,
    /// 上次发布保存的令牌状态；首次发布时为 None。
    pub token_state: Option<TokenState>,
    /// 令牌刷新策略。
    pub token_refresh: TokenRefreshPolicy,
    /// 服务级连接策略。
    pub config: HostConfig,
}

/// Node 的稳定状态快照。
pub struct SculkNodeStatus {
    /// Node 的稳定 EndpointId。
    pub endpoint_id: String,
    /// 底层 Endpoint 是否仍在运行。
    pub online: bool,
    /// Node 级自定义中继。
    pub relay_url: Option<String>,
    /// 当前已发布服务数。
    pub service_count: u64,
    /// 当前等待认证的连接数。
    pub unauthenticated_connection_count: u64,
    /// 认证并发上限。
    pub unauthenticated_connections_max: u64,
}

/// 已发布服务的生命周期阶段。
pub enum HostedServicePhase {
    Active {},
    Stopping {},
    Stopped {},
}

/// 单个已发布服务的稳定状态快照。
pub struct HostedServiceStatus {
    /// 服务标识（16 字节）。
    pub service_id: Vec<u8>,
    /// 当前生命周期阶段。
    pub phase: HostedServicePhase,
    /// 当前分享 URI 的代数。
    pub uri_generation: u64,
    /// 当前令牌创建时刻（Unix 毫秒）。
    pub token_created_at_ms: u64,
    /// 下一次自动轮换时刻（Unix 毫秒）；None = 不自动轮换。
    pub next_rotation_at_ms: Option<u64>,
    /// 当前已认证且仍存活的连接数。
    pub connection_count: u64,
    /// 当前活跃的 TCP-to-QUIC 桥接数。
    pub bridge_count: u64,
    /// 最近一次影响此服务的结构化错误。
    pub last_error: Option<ErrorCategory>,
}

/// 跨 Host 发布保存的稳定服务标识和令牌状态。
pub struct HostState {
    /// 服务标识（16 字节）。
    pub service_id: Vec<u8>,
    /// 令牌状态。
    pub token_state: TokenState,
}

/// 可持久化的分享链接刷新策略。
pub enum TokenRefreshSetting {
    Always {},
    Never {},
    OneHour {},
    ThreeHours {},
    SixHours {},
    TwelveHours {},
    TwentyFourHours {},
}

/// host 端偏好配置。
pub struct HostProfile {
    /// 本地 Minecraft 服务端监听端口。
    pub port: u16,
    /// 跨发布生效的分享链接刷新策略。
    pub token_refresh: TokenRefreshSetting,
}

/// join 端偏好配置。
pub struct JoinProfile {
    /// 本地入站监听端口。
    pub port: u16,
}

/// relay 偏好配置。
pub struct RelayProfile {
    /// true 启用自建中继，false 使用 iroh 内置中继。
    pub custom: bool,
    /// 自建中继地址，仅 custom = true 时生效。
    pub url: Option<String>,
}

/// 用户偏好配置根结构。
pub struct Profile {
    pub host: HostProfile,
    pub join: JoinProfile,
    pub relay: RelayProfile,
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
    TargetNotLoopback {},
    InvalidTargetPort {},
    DuplicateService {},
    ServiceNotFound {},
    RotationInProgress {},
    InvalidRefreshPeriod {},
    SessionGenerationExhausted {},
    BindEndpoint {},
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
            Self::TargetNotLoopback {} => f.write_str("target address must be a loopback address"),
            Self::InvalidTargetPort {} => f.write_str("target port must be non-zero"),
            Self::DuplicateService {} => {
                f.write_str("a service with this service id is already published")
            }
            Self::ServiceNotFound {} => f.write_str("service is not published"),
            Self::RotationInProgress {} => {
                f.write_str("service token rotation is already in progress")
            }
            Self::InvalidRefreshPeriod {} => f.write_str("token refresh period must be greater than zero"),
            Self::SessionGenerationExhausted {} => {
                f.write_str("service session generation space exhausted")
            }
            Self::BindEndpoint {} => f.write_str("failed to bind node endpoint"),
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

/// core 的 Node 错误 -> FFI 错误。
fn map_node_error(error: CoreSculkNodeError) -> ScuLkError {
    match error {
        CoreSculkNodeError::TargetNotLoopback => ScuLkError::TargetNotLoopback {},
        CoreSculkNodeError::InvalidTargetPort => ScuLkError::InvalidTargetPort {},
        CoreSculkNodeError::DuplicateService => ScuLkError::DuplicateService {},
        CoreSculkNodeError::ServiceNotFound => ScuLkError::ServiceNotFound {},
        CoreSculkNodeError::RotationInProgress => ScuLkError::RotationInProgress {},
        CoreSculkNodeError::InvalidRefreshPeriod => ScuLkError::InvalidRefreshPeriod {},
        CoreSculkNodeError::SessionGenerationExhausted => {
            ScuLkError::SessionGenerationExhausted {}
        }
        CoreSculkNodeError::JoinUri(_) => ScuLkError::InvalidJoinUri {},
        CoreSculkNodeError::BindEndpoint(_) => ScuLkError::BindEndpoint {},
        _ => ScuLkError::Internal {},
    }
}

// ---------------------------------------------------------------------------
// 通用小工具
// ---------------------------------------------------------------------------

fn system_time_to_ms(time: SystemTime) -> u64 {
    time.duration_since(UNIX_EPOCH)
        .unwrap_or(Duration::ZERO)
        .as_millis() as u64
}

fn ms_to_system_time(ms: u64) -> SystemTime {
    UNIX_EPOCH + Duration::from_millis(ms)
}

fn parse_bytes16(bytes: &[u8]) -> Result<[u8; 16], ScuLkError> {
    bytes
        .try_into()
        .map_err(|_| ScuLkError::InvalidConfiguration {})
}

fn parse_bytes32(bytes: &[u8]) -> Result<[u8; 32], ScuLkError> {
    bytes
        .try_into()
        .map_err(|_| ScuLkError::InvalidConfiguration {})
}

fn parse_service_id(bytes: &[u8]) -> Result<CoreServiceId, ScuLkError> {
    Ok(CoreServiceId::from_bytes(parse_bytes16(bytes)?))
}

fn parse_access_token(bytes: &[u8]) -> Result<CoreAccessToken, ScuLkError> {
    Ok(CoreAccessToken::from_bytes(parse_bytes32(bytes)?))
}

fn to_ffi_token_state(state: CoreTokenState) -> TokenState {
    TokenState {
        token: state.token().to_bytes().to_vec(),
        created_at_ms: system_time_to_ms(state.created_at()),
    }
}

fn to_core_token_state(state: TokenState) -> Result<CoreTokenState, ScuLkError> {
    Ok(CoreTokenState::new(
        parse_access_token(&state.token)?,
        ms_to_system_time(state.created_at_ms),
    ))
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

/// 生成新的 32 字节节点密钥。
pub fn secret_key_generate() -> Arc<SecretKey> {
    let bytes: [u8; 32] = rand::random();
    Arc::new(SecretKey {
        inner: CoreSecretKey::from_bytes(&bytes),
    })
}

/// 从 32 字节数组创建节点密钥。
pub fn secret_key_from_bytes(bytes: Vec<u8>) -> Result<Arc<SecretKey>, ScuLkError> {
    let key: [u8; 32] = parse_bytes32(&bytes)?;
    Ok(Arc::new(SecretKey {
        inner: CoreSecretKey::from_bytes(&key),
    }))
}

/// 生成新的 16 字节服务标识。
pub fn service_id_generate() -> Arc<ServiceId> {
    Arc::new(ServiceId {
        inner: CoreServiceId::generate(),
    })
}

/// 从 16 字节数组创建服务标识。
pub fn service_id_from_bytes(bytes: Vec<u8>) -> Result<Arc<ServiceId>, ScuLkError> {
    Ok(Arc::new(ServiceId {
        inner: parse_service_id(&bytes)?,
    }))
}

/// 生成新的 32 字节访问令牌。
pub fn access_token_generate() -> Arc<AccessToken> {
    Arc::new(AccessToken {
        inner: CoreAccessToken::generate(),
    })
}

/// 从 32 字节数组创建访问令牌。
pub fn access_token_from_bytes(bytes: Vec<u8>) -> Result<Arc<AccessToken>, ScuLkError> {
    Ok(Arc::new(AccessToken {
        inner: parse_access_token(&bytes)?,
    }))
}

/// 解析 `sculk://join/v1/` 分享 URI。
pub fn join_uri_parse(uri: String) -> Result<Arc<JoinUri>, ScuLkError> {
    let parsed: CoreJoinUri = uri.parse().map_err(|_| ScuLkError::InvalidJoinUri {})?;
    Ok(Arc::new(JoinUri { inner: parsed }))
}

/// 创建多服务 Host 节点。
pub async fn sculpt_node_bind(options: NodeOptions) -> Result<Arc<SculkNode>, ScuLkError> {
    let options = to_core_node_options(options)?;
    Compat::new(async move {
        let node = CoreSculkNode::bind(options).await.map_err(map_node_error)?;
        Ok(Arc::new(SculkNode { inner: node }))
    })
    .await
}

// ---------------------------------------------------------------------------
// 持久化函数
// ---------------------------------------------------------------------------

/// 应用数据目录（`{data_dir}/sculk/`）。
pub fn persist_data_dir() -> Result<String, ScuLkError> {
    core_data_dir()
        .map(|path| path.display().to_string())
        .map_err(|_| ScuLkError::Io {})
}

/// 默认节点密钥文件路径。
pub fn persist_default_key_path() -> Result<String, ScuLkError> {
    core_default_key_path()
        .map(|path| path.display().to_string())
        .map_err(|_| ScuLkError::Io {})
}

/// 默认 host 状态文件路径。
pub fn persist_default_host_state_path() -> Result<String, ScuLkError> {
    core_default_host_state_path()
        .map(|path| path.display().to_string())
        .map_err(|_| ScuLkError::Io {})
}

/// 加载稳定节点密钥；不存在时生成并写入。
pub fn persist_load_or_generate_key(path: String) -> Result<Arc<SecretKey>, ScuLkError> {
    let key = core_load_or_generate_key(Path::new(&path)).map_err(|_| ScuLkError::Io {})?;
    Ok(Arc::new(SecretKey { inner: key }))
}

/// 生成并写入新的节点密钥（覆盖已有文件）。
pub fn persist_generate_new_key(path: String) -> Result<Arc<SecretKey>, ScuLkError> {
    let key = core_generate_new_key(Path::new(&path)).map_err(|_| ScuLkError::Io {})?;
    Ok(Arc::new(SecretKey { inner: key }))
}

/// 加载 host 状态；文件不存在时返回 None。
pub fn persist_load_host_state(path: String) -> Result<Option<HostState>, ScuLkError> {
    let state = core_load_host_state(Path::new(&path)).map_err(|_| ScuLkError::Io {})?;
    Ok(state.map(to_ffi_host_state))
}

/// 原子保存 host 状态。
pub fn persist_save_host_state(path: String, state: HostState) -> Result<(), ScuLkError> {
    let core_state = to_core_host_state(state)?;
    core_save_host_state(Path::new(&path), &core_state).map_err(|_| ScuLkError::Io {})
}

/// 加载默认位置的用户偏好 profile。
pub fn persist_load_profile() -> Result<Profile, ScuLkError> {
    CoreProfile::load()
        .map(to_ffi_profile)
        .map_err(|_| ScuLkError::Io {})
}

/// 保存用户偏好 profile 到默认位置。
pub fn persist_save_profile(profile: Profile) -> Result<(), ScuLkError> {
    to_core_profile(profile)
        .save()
        .map_err(|_| ScuLkError::Io {})
}

/// 从指定路径加载 profile。
pub fn persist_load_profile_from(path: String) -> Result<Profile, ScuLkError> {
    CoreProfile::load_from(Path::new(&path))
        .map(to_ffi_profile)
        .map_err(|_| ScuLkError::Io {})
}

/// 保存 profile 到指定路径。
pub fn persist_save_profile_to(path: String, profile: Profile) -> Result<(), ScuLkError> {
    to_core_profile(profile)
        .save_to(Path::new(&path))
        .map_err(|_| ScuLkError::Io {})
}

// ---------------------------------------------------------------------------
// 密钥 / 标识 / 令牌对象
// ---------------------------------------------------------------------------

/// 节点密钥。
pub struct SecretKey {
    inner: CoreSecretKey,
}

impl SecretKey {
    pub fn to_bytes(&self) -> Vec<u8> {
        self.inner.to_bytes().to_vec()
    }

    pub fn public_key(&self) -> String {
        self.inner.public().to_string()
    }
}

/// 服务的稳定逻辑标识。
pub struct ServiceId {
    inner: CoreServiceId,
}

impl ServiceId {
    pub fn to_bytes(&self) -> Vec<u8> {
        self.inner.to_bytes().to_vec()
    }
}

/// 仅在当前 Host 会话中有效的访问令牌。
pub struct AccessToken {
    inner: CoreAccessToken,
}

impl AccessToken {
    pub fn to_bytes(&self) -> Vec<u8> {
        self.inner.to_bytes().to_vec()
    }

    pub fn matches(&self, other: Vec<u8>) -> bool {
        match parse_bytes32(&other) {
            Ok(bytes) => self.inner.matches(&CoreAccessToken::from_bytes(bytes)),
            Err(_) => false,
        }
    }
}

// ---------------------------------------------------------------------------
// 分享 URI
// ---------------------------------------------------------------------------

/// `sculk://join/v1/` 分享 URI。
pub struct JoinUri {
    inner: CoreJoinUri,
}

impl JoinUri {
    pub fn expose_secret_uri(&self) -> Result<String, ScuLkError> {
        self.inner
            .expose_secret_uri()
            .map_err(|_| ScuLkError::InvalidJoinUri {})
    }

    pub fn relay_url(&self) -> Option<String> {
        self.inner.relay_url().map(|url| url.to_string())
    }

    pub fn redacted(&self) -> String {
        format!("{:?}", self.inner)
    }
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
        Compat::new(async move { inner.start_host(options).await.map_err(map_service_error) }).await
    }

    pub async fn start_join(&self, options: JoinOptions) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        let options = to_core_join_options(options)?;
        Compat::new(async move { inner.start_join(options).await.map_err(map_service_error) }).await
    }

    pub async fn stop(&self) -> Result<TunnelStatus, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            inner
                .stop()
                .await
                .map(to_ffi_status)
                .map_err(map_service_error)
        })
        .await
    }

    pub async fn shutdown(&self) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move { inner.shutdown().await.map_err(map_service_error) }).await
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
        Compat::new(async {
            let mut subscription = self.inner.lock().await;
            Ok(subscription.recv().await.map(to_ffi_update))
        })
        .await
    }
}

// ---------------------------------------------------------------------------
// 多服务 Host 节点
// ---------------------------------------------------------------------------

/// 多服务 Host 节点。
pub struct SculkNode {
    inner: CoreSculkNode,
}

impl SculkNode {
    pub async fn start_service(
        &self,
        options: HostedServiceOptions,
    ) -> Result<Arc<HostedServiceHandle>, ScuLkError> {
        let inner = self.inner.clone();
        let options = to_core_hosted_service_options(options)?;
        Compat::new(async move {
            let handle = inner.start_service(options).await.map_err(map_node_error)?;
            Ok(Arc::new(HostedServiceHandle { inner: handle }))
        })
        .await
    }

    pub async fn stop_service(&self, service_id: Vec<u8>) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        let service_id = parse_service_id(&service_id)?;
        Compat::new(async move {
            inner
                .stop_service(service_id)
                .await
                .map_err(map_node_error)
        })
        .await
    }

    pub async fn service_count(&self) -> u64 {
        let inner = self.inner.clone();
        Compat::new(async move { inner.service_count().await as u64 }).await
    }

    pub async fn status(&self) -> SculkNodeStatus {
        let inner = self.inner.clone();
        Compat::new(async move { to_ffi_node_status(inner.status().await) }).await
    }

    pub async fn rotate_token(&self, service_id: Vec<u8>) -> Result<Arc<JoinUri>, ScuLkError> {
        let inner = self.inner.clone();
        let service_id = parse_service_id(&service_id)?;
        Compat::new(async move {
            let uri = inner.rotate_token(service_id).await.map_err(map_node_error)?;
            Ok(Arc::new(JoinUri { inner: uri }))
        })
        .await
    }

    pub fn endpoint_id(&self) -> String {
        self.inner.endpoint_id().to_string()
    }

    pub async fn shutdown(&self) {
        let inner = self.inner.clone();
        Compat::new(async move { inner.close().await }).await;
    }
}

/// 已发布服务的轻量操作句柄。
pub struct HostedServiceHandle {
    inner: CoreHostedServiceHandle,
}

impl HostedServiceHandle {
    pub fn service_id(&self) -> Vec<u8> {
        self.inner.service_id().to_bytes().to_vec()
    }

    pub async fn join_uri(&self) -> Result<Arc<JoinUri>, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let uri = inner.join_uri().await.map_err(map_node_error)?;
            Ok(Arc::new(JoinUri { inner: uri }))
        })
        .await
    }

    pub async fn token_state(&self) -> Result<TokenState, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let state = inner.token_state().await.map_err(map_node_error)?;
            Ok(to_ffi_token_state(state))
        })
        .await
    }

    pub async fn status(&self) -> Result<HostedServiceStatus, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let status = inner.status().await.map_err(map_node_error)?;
            Ok(to_ffi_hosted_status(status))
        })
        .await
    }

    pub async fn stop(&self) -> Result<(), ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move { inner.stop().await.map_err(map_node_error) }).await
    }

    pub async fn rotate_token(&self) -> Result<Arc<JoinUri>, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let uri = inner.rotate_token().await.map_err(map_node_error)?;
            Ok(Arc::new(JoinUri { inner: uri }))
        })
        .await
    }

    pub async fn subscribe(&self) -> Result<Arc<HostedServiceEventReader>, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let receiver = inner.subscribe().await.map_err(map_node_error)?;
            Ok(Arc::new(HostedServiceEventReader {
                inner: tokio::sync::Mutex::new(receiver),
            }))
        })
        .await
    }

    pub async fn subscribe_status(
        &self,
    ) -> Result<Arc<HostedServiceStatusSubscription>, ScuLkError> {
        let inner = self.inner.clone();
        Compat::new(async move {
            let subscription = inner.subscribe_status().await.map_err(map_node_error)?;
            Ok(Arc::new(HostedServiceStatusSubscription {
                inner: tokio::sync::Mutex::new(subscription),
            }))
        })
        .await
    }
}

/// 服务过程事件读取端。
pub struct HostedServiceEventReader {
    inner: tokio::sync::Mutex<tokio::sync::broadcast::Receiver<CoreTunnelEvent>>,
}

impl HostedServiceEventReader {
    pub async fn recv(&self) -> Option<TunnelEvent> {
        Compat::new(async {
            loop {
                let mut receiver = self.inner.lock().await;
                match receiver.recv().await {
                    Ok(event) => return Some(to_ffi_event(event)),
                    Err(tokio::sync::broadcast::error::RecvError::Lagged(_)) => continue,
                    Err(tokio::sync::broadcast::error::RecvError::Closed) => return None,
                }
            }
        })
        .await
    }
}

/// 服务状态订阅端。
pub struct HostedServiceStatusSubscription {
    inner: tokio::sync::Mutex<CoreHostedServiceStatusSubscription>,
}

impl HostedServiceStatusSubscription {
    pub async fn recv(&self) -> Option<HostedServiceStatus> {
        Compat::new(async {
            let mut subscription = self.inner.lock().await;
            subscription.recv().await.map(to_ffi_hosted_status)
        })
        .await
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
        let key: [u8; 32] = parse_bytes32(bytes)?;
        core = core.secret_key(Some(CoreSecretKey::from_bytes(&key)));
    }

    if let Some(url) = &options.relay_url {
        let relay = url
            .parse::<CoreRelayUrl>()
            .map_err(|_| ScuLkError::InvalidEndpoint {})?;
        core = core.relay_url(Some(relay));
    }

    let service_id = match &options.service_id {
        Some(bytes) => parse_service_id(bytes)?,
        None => CoreServiceId::generate(),
    };

    let token = match &options.token {
        Some(bytes) => parse_access_token(bytes)?,
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

fn to_core_node_options(options: NodeOptions) -> Result<CoreNodeOptions, ScuLkError> {
    let secret_key = match &options.secret_key {
        Some(bytes) => {
            let key: [u8; 32] = parse_bytes32(bytes)?;
            Some(CoreSecretKey::from_bytes(&key))
        }
        None => None,
    };
    let relay_url = match &options.relay_url {
        Some(url) => Some(url.parse::<CoreRelayUrl>().map_err(|_| ScuLkError::InvalidEndpoint {})?),
        None => None,
    };
    let max = if options.unauthenticated_connections_max == 0 {
        128
    } else {
        usize::try_from(options.unauthenticated_connections_max).unwrap_or(usize::MAX)
    };
    let maximum = NonZeroUsize::new(max).unwrap_or(NonZeroUsize::MIN);
    Ok(CoreNodeOptions {
        secret_key,
        relay_url,
        unauthenticated_connections_max: maximum,
    })
}

fn to_core_hosted_service_options(
    options: HostedServiceOptions,
) -> Result<CoreHostedServiceOptions, ScuLkError> {
    let service_id = parse_service_id(&options.service_id)?;
    let target_addr: SocketAddr = options
        .target_addr
        .parse()
        .map_err(|_| ScuLkError::InvalidConfiguration {})?;
    let token_state = match options.token_state {
        Some(state) => Some(to_core_token_state(state)?),
        None => None,
    };
    let token_refresh = match options.token_refresh.kind {
        TokenRefreshKind::Always {} => CoreTokenRefreshPolicy::Always,
        TokenRefreshKind::Never {} => CoreTokenRefreshPolicy::Never,
        TokenRefreshKind::After {} => {
            CoreTokenRefreshPolicy::After(Duration::from_millis(options.token_refresh.period_ms))
        }
    };
    Ok(CoreHostedServiceOptions {
        service_id,
        target_addr,
        token_state,
        token_refresh,
        config: to_core_host_config(options.config),
    })
}

fn to_core_token_refresh(setting: TokenRefreshSetting) -> CoreTokenRefreshSetting {
    match setting {
        TokenRefreshSetting::Always {} => CoreTokenRefreshSetting::Always,
        TokenRefreshSetting::Never {} => CoreTokenRefreshSetting::Never,
        TokenRefreshSetting::OneHour {} => CoreTokenRefreshSetting::OneHour,
        TokenRefreshSetting::ThreeHours {} => CoreTokenRefreshSetting::ThreeHours,
        TokenRefreshSetting::SixHours {} => CoreTokenRefreshSetting::SixHours,
        TokenRefreshSetting::TwelveHours {} => CoreTokenRefreshSetting::TwelveHours,
        TokenRefreshSetting::TwentyFourHours {} => CoreTokenRefreshSetting::TwentyFourHours,
    }
}

fn to_ffi_token_refresh(setting: CoreTokenRefreshSetting) -> TokenRefreshSetting {
    match setting {
        CoreTokenRefreshSetting::Always => TokenRefreshSetting::Always {},
        CoreTokenRefreshSetting::Never => TokenRefreshSetting::Never {},
        CoreTokenRefreshSetting::OneHour => TokenRefreshSetting::OneHour {},
        CoreTokenRefreshSetting::ThreeHours => TokenRefreshSetting::ThreeHours {},
        CoreTokenRefreshSetting::SixHours => TokenRefreshSetting::SixHours {},
        CoreTokenRefreshSetting::TwelveHours => TokenRefreshSetting::TwelveHours {},
        CoreTokenRefreshSetting::TwentyFourHours => TokenRefreshSetting::TwentyFourHours {},
    }
}

fn to_core_profile(profile: Profile) -> CoreProfile {
    CoreProfile {
        host: CoreHostProfile {
            port: profile.host.port,
            token_refresh: to_core_token_refresh(profile.host.token_refresh),
        },
        join: CoreJoinProfile {
            port: profile.join.port,
        },
        relay: CoreRelayProfile {
            custom: profile.relay.custom,
            url: profile.relay.url,
        },
    }
}

fn to_ffi_profile(profile: CoreProfile) -> Profile {
    Profile {
        host: HostProfile {
            port: profile.host.port,
            token_refresh: to_ffi_token_refresh(profile.host.token_refresh),
        },
        join: JoinProfile {
            port: profile.join.port,
        },
        relay: RelayProfile {
            custom: profile.relay.custom,
            url: profile.relay.url,
        },
    }
}

fn to_ffi_host_state(state: CoreHostState) -> HostState {
    HostState {
        service_id: state.service_id.to_bytes().to_vec(),
        token_state: to_ffi_token_state(state.token_state),
    }
}

fn to_core_host_state(state: HostState) -> Result<CoreHostState, ScuLkError> {
    Ok(CoreHostState {
        service_id: parse_service_id(&state.service_id)?,
        token_state: to_core_token_state(state.token_state)?,
    })
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

fn to_ffi_node_status(status: CoreSculkNodeStatus) -> SculkNodeStatus {
    SculkNodeStatus {
        endpoint_id: status.endpoint_id.to_string(),
        online: status.online,
        relay_url: status.relay_url.map(|url| url.to_string()),
        service_count: status.service_count as u64,
        unauthenticated_connection_count: status.unauthenticated_connection_count as u64,
        unauthenticated_connections_max: status.unauthenticated_connections_max as u64,
    }
}

fn to_ffi_hosted_phase(phase: CoreHostedServicePhase) -> HostedServicePhase {
    match phase {
        CoreHostedServicePhase::Active => HostedServicePhase::Active {},
        CoreHostedServicePhase::Stopping => HostedServicePhase::Stopping {},
        CoreHostedServicePhase::Stopped => HostedServicePhase::Stopped {},
    }
}

fn to_ffi_hosted_status(status: CoreHostedServiceStatus) -> HostedServiceStatus {
    HostedServiceStatus {
        service_id: status.service_id.to_bytes().to_vec(),
        phase: to_ffi_hosted_phase(status.phase),
        uri_generation: status.uri_generation,
        token_created_at_ms: system_time_to_ms(status.token_created_at),
        next_rotation_at_ms: status.next_rotation_at.map(system_time_to_ms),
        connection_count: status.connection_count as u64,
        bridge_count: status.bridge_count as u64,
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
