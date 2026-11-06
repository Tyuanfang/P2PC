package a90pn.pepc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

import a90pn.pepc.ffi.HostConfig;
import a90pn.pepc.ffi.HostOptions;
import a90pn.pepc.ffi.HostProfile;
import a90pn.pepc.ffi.HostState;
import a90pn.pepc.ffi.HostedServiceHandle;
import a90pn.pepc.ffi.HostedServiceOptions;
import a90pn.pepc.ffi.HostedServiceStatus;
import a90pn.pepc.ffi.JoinConfig;
import a90pn.pepc.ffi.JoinOptions;
import a90pn.pepc.ffi.JoinProfile;
import a90pn.pepc.ffi.JoinUri;
import a90pn.pepc.ffi.LanBroadcaster;
import a90pn.pepc.ffi.LanScanner;
import a90pn.pepc.ffi.NodeOptions;
import a90pn.pepc.ffi.Profile;
import a90pn.pepc.ffi.RelayProfile;
import a90pn.pepc.ffi.ScuLkException;
import a90pn.pepc.ffi.SculkFfi;
import a90pn.pepc.ffi.SculkNode;
import a90pn.pepc.ffi.SculkNodeStatus;
import a90pn.pepc.ffi.Subscription;
import a90pn.pepc.ffi.TokenRefreshKind;
import a90pn.pepc.ffi.TokenRefreshPolicy;
import a90pn.pepc.ffi.TokenRefreshSetting;
import a90pn.pepc.ffi.TokenState;
import a90pn.pepc.ffi.TunnelService;
import a90pn.pepc.ffi.TunnelStatus;
import a90pn.pepc.ffi.TunnelUpdate;
import a90pn.pepc.ffi.UniffiInitializer;

/**
 * sculk 联机内核的 Java 入口
 *
 * <p>底层是 uniffi-bindgen-java 从 {@code ffi/src/sculk_ffi.udl} 生成的 Panama FFM 绑定
 * （包 {@code a90pn.pepc.ffi}）；本类把常用入口、参数构造、事件泵和多平台原生库加载
 * 收拢在一个门面里。
 *
 * <p>原生库：jar 内按平台放在 {@code /natives/<平台标识>/}（windows-x86_64、
 * linux-x86_64、linux-aarch64、macos-x86_64、macos-aarch64）；首次调用时解包到临时
 * 目录并通过 {@code uniffi.component.sculk_ffi.libraryOverride} 交给生成代码加载。
 *
 * <p>要求 Java 22+，运行时加 {@code --enable-native-access=ALL-UNNAMED}
 * （22/23 必需，24+ 只是消警告）。
 */
public final class SculkClient {
    /** 生成代码读取的库路径覆盖属性（namespace = sculk_ffi）。 */
    public static final String LIBRARY_PROPERTY = "uniffi.component.sculk_ffi.libraryOverride";

    private static volatile boolean loaded = false;

    private SculkClient() {
    }

    // ------------------------------------------------------------------ 原生库

    /**
     * 加载原生库（幂等）。必须在首次触碰任何生成绑定之前调用；模组初始化时调一次即可。
     *
     * <p>也可以事先设置 {@code -Duniffi.component.sculk_ffi.libraryOverride=<绝对路径>}
     * 让它直接加载指定文件（调试用）。
     */
    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }

        String override = System.getProperty(LIBRARY_PROPERTY);
        if (override == null || override.isBlank()) {
            override = extractBundledLibrary().toString();
            System.setProperty(LIBRARY_PROPERTY, override);
        }

        // 触发生成代码的静态初始化：System.load + UniFFI contract/checksum 校验。
        UniffiInitializer.ensureInitialized();
        loaded = true;
    }

    /** 当前系统对应的平台标识；不支持时返回 null。 */
    public static String platformToken() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
        boolean arm64 = arch.contains("aarch64") || arch.contains("arm64");
        boolean x64 = arch.contains("amd64") || arch.contains("x86_64") || arch.equals("x64");

        if (os.contains("win")) {
            return x64 ? "windows-x86_64" : null;
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return arm64 ? "macos-aarch64" : (x64 ? "macos-x86_64" : null);
        }
        if (os.contains("linux")) {
            return arm64 ? "linux-aarch64" : (x64 ? "linux-x86_64" : null);
        }
        return null;
    }

    /** 平台上动态库的文件名。 */
    public static String libraryFileName(String platformToken) {
        if (platformToken.startsWith("windows")) {
            return "sculk_ffi.dll";
        }
        if (platformToken.startsWith("macos")) {
            return "libsculk_ffi.dylib";
        }
        return "libsculk_ffi.so";
    }

    private static Path extractBundledLibrary() {
        String platform = platformToken();
        if (platform == null) {
            throw new IllegalStateException("p2pc 的 sculk 原生库不支持当前系统: "
                    + System.getProperty("os.name") + " / " + System.getProperty("os.arch"));
        }

        String fileName = libraryFileName(platform);
        String resource = "/natives/" + platform + "/" + fileName;
        try (InputStream in = SculkClient.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("模组里没有 " + resource
                        + "：这个平台的 sculk 原生库还没被打包（需要为 " + platform
                        + " 构建 sculk-ffi 后重新打包）");
            }
            Path dir = Files.createTempDirectory("p2pc-natives-");
            Path library = dir.resolve(fileName);
            Files.copy(in, library, StandardCopyOption.REPLACE_EXISTING);
            library.toFile().deleteOnExit();
            dir.toFile().deleteOnExit();
            return library;
        } catch (IOException error) {
            throw new IllegalStateException("解包 sculk 原生库失败（" + resource + "）", error);
        }
    }

    // ------------------------------------------------------------------ 常量 / 探测

    /** Minecraft 服务端标准端口（Rust 侧 DEFAULT_MC_PORT）。 */
    public static int defaultMcPort() {
        ensureLoaded();
        return SculkFfi.defaultMcPort() & 0xFFFF;
    }

    /** join 端本地入站监听端口默认值（Rust 侧 DEFAULT_INLET_PORT）。 */
    public static int defaultInletPort() {
        ensureLoaded();
        return SculkFfi.defaultInletPort() & 0xFFFF;
    }

    /**
     * 用旧版服务器列表探测（0xFE/0xFF）确认目标端口是 Minecraft Java 版服务。
     *
     * @param addr      {@code host:port}
     * @param timeoutMs 探测超时（毫秒）
     */
    public static void probeServer(String addr, long timeoutMs) throws ScuLkException {
        ensureLoaded();
        SculkFfi.probeServer(addr, timeoutMs);
    }

    // ------------------------------------------------------------------ 密钥 / 标识 / 令牌

    /** 生成新的 32 字节节点密钥。 */
    public static byte[] generateSecretKey() {
        ensureLoaded();
        return toByteArray(SculkFfi.secretKeyGenerate().toBytes());
    }

    /** 由 32 字节节点密钥导出 EndpointId 字符串。 */
    public static String secretKeyPublicKey(byte[] secretKey) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.secretKeyFromBytes(toByteList(secretKey)).publicKey();
    }

    /** 生成新的 16 字节服务标识。 */
    public static byte[] generateServiceId() {
        ensureLoaded();
        return toByteArray(SculkFfi.serviceIdGenerate().toBytes());
    }

    /** 生成新的 32 字节访问令牌。 */
    public static byte[] generateAccessToken() {
        ensureLoaded();
        return toByteArray(SculkFfi.accessTokenGenerate().toBytes());
    }

    /** 以常量时间比较两个 32 字节访问令牌。 */
    public static boolean accessTokenMatches(byte[] token, byte[] other) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.accessTokenFromBytes(toByteList(token)).matches(toByteList(other));
    }

    // ------------------------------------------------------------------ 分享 URI

    /** 解析 {@code sculk://join/v1/...} 分享串。 */
    public static JoinUri parseJoinUri(String uri) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.joinUriParse(uri);
    }

    /** 从分享 URI 取回可分享的完整串（含访问令牌）。 */
    public static String joinUriSecret(JoinUri uri) throws ScuLkException {
        return uri.exposeSecretUri();
    }

    /** 脱敏展示形式（不含访问令牌，可安全写日志）。 */
    public static String joinUriRedacted(JoinUri uri) {
        return uri.redacted();
    }

    /** URI 里携带的自定义中继（null = iroh 默认）。 */
    public static String joinUriRelayUrl(JoinUri uri) {
        return uri.relayUrl();
    }

    // ------------------------------------------------------------------ 单隧道（TunnelService）

    /** Host 端行为配置。 */
    public static HostConfig hostConfig(long eventDelayMs, Integer maxPlayers) {
        return new HostConfig(eventDelayMs, maxPlayers);
    }

    /**
     * Host 启动参数。
     *
     * @param mcPort    本地 Minecraft 服务端端口
     * @param relayUrl  自定义中继 URL，null = iroh 默认
     * @param secretKey 稳定节点密钥 32 字节，null = 临时
     * @param serviceId 服务标识 16 字节，null = 自动生成
     * @param token     访问令牌 32 字节，null = 自动生成
     */
    public static HostOptions hostOptions(int mcPort, String relayUrl, byte[] secretKey,
                                         byte[] serviceId, byte[] token, HostConfig config) {
        return new HostOptions((short) mcPort, toByteList(secretKey), relayUrl,
                toByteList(serviceId), toByteList(token), config);
    }

    /** Join 端行为配置。reconnectTimeoutMs 传 null = 无限等待，0 = 关闭重连。 */
    public static JoinConfig joinConfig(long eventDelayMs, Long reconnectTimeoutMs, long localSessionsMax) {
        return new JoinConfig(eventDelayMs, reconnectTimeoutMs, localSessionsMax);
    }

    /**
     * Join 启动参数。
     *
     * @param joinUri   {@code sculk://join/v1/...} 分享串
     * @param localPort 本地客户端连接端口，0 = 自动分配
     */
    public static JoinOptions joinOptions(String joinUri, int localPort, JoinConfig config) {
        return new JoinOptions(joinUri, (short) localPort, config);
    }

    /** 新建一个隧道服务实例（进程内一个就够）。 */
    public static TunnelService newService() {
        ensureLoaded();
        return new TunnelService();
    }

    /** 作为房主：把本地 Minecraft 服务端暴露到 P2P 网络。 */
    public static CompletableFuture<Void> startHost(TunnelService service, HostOptions options) {
        ensureLoaded();
        return service.startHost(options);
    }

    /** 作为加入方：把远端世界映射到本地端口，然后用 Minecraft 客户端连 127.0.0.1。 */
    public static CompletableFuture<Void> startJoin(TunnelService service, JoinOptions options) {
        ensureLoaded();
        return service.startJoin(options);
    }

    /** 当前状态快照（同步）。 */
    public static TunnelStatus status(TunnelService service) {
        ensureLoaded();
        return service.status();
    }

    /** 停止隧道，返回停止后的状态。 */
    public static CompletableFuture<TunnelStatus> stop(TunnelService service) {
        ensureLoaded();
        return service.stop();
    }

    /**
     * Host 侧可分享的 {@code sculk://join/v1/...} 分享串；还没监听时返回 null。
     * 该串包含访问令牌，发给朋友即授权。
     */
    public static String shareUri(TunnelStatus status) {
        if (status == null) {
            return null;
        }
        return status.state().joinUri();
    }

    // ------------------------------------------------------------------ 事件流

    /**
     * 订阅隧道更新：首条是当前状态快照，之后状态/事件交错。
     * 回调在内部守护线程上执行，别在里面做重活。
     */
    public static Watcher watch(TunnelService service, Consumer<TunnelUpdate> listener) {
        ensureLoaded();
        WireSubscription subscription = new WireSubscription(service.subscribe());
        Thread pump = new Thread(subscription::runPump, "p2pc-tunnel-events");
        pump.setDaemon(true);
        subscription.bind(pump, listener);
        pump.start();
        return subscription;
    }

    /** 事件流句柄：关掉它会结束内部线程并释放 Rust 侧订阅。 */
    public interface Watcher extends AutoCloseable {
        @Override
        void close();
    }

    private static final class WireSubscription implements Watcher {
        private final Subscription subscription;
        private volatile Thread thread;
        private Consumer<TunnelUpdate> listener;
        private volatile boolean closed = false;

        WireSubscription(Subscription subscription) {
            this.subscription = subscription;
        }

        void bind(Thread thread, Consumer<TunnelUpdate> listener) {
            this.thread = thread;
            this.listener = listener;
        }

        /** 循环拉取更新；recv 返回 null 表示订阅端已关闭。 */
        void runPump() {
            try {
                while (!closed) {
                    TunnelUpdate update = subscription.recv().join();
                    if (update == null) {
                        return;
                    }
                    Consumer<TunnelUpdate> current = listener;
                    if (current != null) {
                        current.accept(update);
                    }
                }
            } catch (RuntimeException error) {
                if (!closed) {
                    throw error;
                }
            } finally {
                subscription.close();
            }
        }

        @Override
        public void close() {
            closed = true;
            Thread current = thread;
            if (current != null) {
                current.interrupt();
            }
        }
    }

    // ------------------------------------------------------------------ 多服务 Host 节点

    /** 多服务节点的创建参数。 */
    public static NodeOptions nodeOptions(byte[] secretKey, String relayUrl, long unauthenticatedConnectionsMax) {
        return new NodeOptions(toByteList(secretKey), relayUrl, unauthenticatedConnectionsMax);
    }

    /** 令牌状态（token 32 字节 + 创建时刻 Unix 毫秒）。 */
    public static TokenState tokenState(byte[] token, long createdAtMs) {
        return new TokenState(toByteList(token), createdAtMs);
    }

    /** 令牌刷新策略。 */
    public static TokenRefreshPolicy tokenRefresh(TokenRefreshPolicyKind kind, long periodMs) {
        return new TokenRefreshPolicy(kind.value(), periodMs);
    }

    /** 便捷的令牌刷新策略枚举（与生成代码的 TokenRefreshKind 一一对应）。 */
    public enum TokenRefreshPolicyKind {
        ALWAYS(TokenRefreshKind.ALWAYS),
        NEVER(TokenRefreshKind.NEVER),
        AFTER(TokenRefreshKind.AFTER);

        private final TokenRefreshKind value;

        TokenRefreshPolicyKind(TokenRefreshKind value) {
            this.value = value;
        }

        TokenRefreshKind value() {
            return value;
        }
    }

    /** 发布单个 Host 服务的参数。 */
    public static HostedServiceOptions hostedServiceOptions(byte[] serviceId, String targetAddr,
                                                           TokenState tokenState,
                                                           TokenRefreshPolicy tokenRefresh,
                                                           HostConfig config) {
        return new HostedServiceOptions(toByteList(serviceId), targetAddr, tokenState, tokenRefresh, config);
    }

    /** 创建多服务 Host 节点（单个 iroh Endpoint 上发布多个隔离服务）。 */
    public static CompletableFuture<SculkNode> bindNode(NodeOptions options) {
        ensureLoaded();
        return SculkFfi.sculptNodeBind(options);
    }

    /** 发布服务并返回其操作句柄。 */
    public static CompletableFuture<HostedServiceHandle> startService(SculkNode node, HostedServiceOptions options) {
        ensureLoaded();
        return node.startService(options);
    }

    /** 停止服务（不关闭 Node）。 */
    public static CompletableFuture<Void> stopService(SculkNode node, byte[] serviceId) {
        ensureLoaded();
        return node.stopService(toByteList(serviceId));
    }

    /** 节点稳定状态快照。 */
    public static CompletableFuture<SculkNodeStatus> nodeStatus(SculkNode node) {
        ensureLoaded();
        return node.status();
    }

    /** 节点稳定 EndpointId。 */
    public static String nodeEndpointId(SculkNode node) {
        ensureLoaded();
        return node.endpointId();
    }

    /** 为指定服务轮换令牌并返回新的分享 URI。 */
    public static CompletableFuture<JoinUri> rotateServiceToken(SculkNode node, byte[] serviceId) {
        ensureLoaded();
        return node.rotateToken(toByteList(serviceId));
    }

    /** 停止所有服务并关闭 Node。 */
    public static CompletableFuture<Void> closeNode(SculkNode node) {
        ensureLoaded();
        return node.shutdown();
    }

    /** 服务句柄：当前分享 URI（含令牌）。 */
    public static String handleSecretUri(HostedServiceHandle handle) throws ScuLkException {
        return await(handle.joinUri()).exposeSecretUri();
    }

    /** 服务句柄：当前令牌状态。 */
    public static TokenState handleTokenState(HostedServiceHandle handle) throws ScuLkException {
        return await(handle.tokenState());
    }

    /** 服务句柄：稳定状态快照。 */
    public static HostedServiceStatus handleStatus(HostedServiceHandle handle) throws ScuLkException {
        return await(handle.status());
    }

    /** 服务句柄：立即轮换令牌并返回新的分享 URI。 */
    public static JoinUri handleRotateToken(HostedServiceHandle handle) throws ScuLkException {
        return await(handle.rotateToken());
    }

    /** 服务句柄：独立停止此服务。 */
    public static void handleStop(HostedServiceHandle handle) throws ScuLkException {
        await(handle.stop());
    }

    /** 服务句柄：服务标识（16 字节）。 */
    public static byte[] handleServiceId(HostedServiceHandle handle) {
        return toByteArray(handle.serviceId());
    }

    // ------------------------------------------------------------------ 持久化

    /** 应用数据目录（{@code {data_dir}/sculk/}）。 */
    public static String dataDir() throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistDataDir();
    }

    /** 默认节点密钥文件路径。 */
    public static String defaultKeyPath() throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistDefaultKeyPath();
    }

    /** 默认 host 状态文件路径。 */
    public static String defaultHostStatePath() throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistDefaultHostStatePath();
    }

    /** 加载稳定节点密钥；不存在时生成并写入。 */
    public static byte[] loadOrGenerateKey(String path) throws ScuLkException {
        ensureLoaded();
        return toByteArray(SculkFfi.persistLoadOrGenerateKey(path).toBytes());
    }

    /** 生成并写入新的节点密钥（覆盖已有文件）。 */
    public static byte[] generateNewKey(String path) throws ScuLkException {
        ensureLoaded();
        return toByteArray(SculkFfi.persistGenerateNewKey(path).toBytes());
    }

    /** 加载 host 状态；文件不存在时返回 null。 */
    public static HostState loadHostState(String path) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistLoadHostState(path);
    }

    /** 原子保存 host 状态。 */
    public static void saveHostState(String path, HostState state) throws ScuLkException {
        ensureLoaded();
        SculkFfi.persistSaveHostState(path, state);
    }

    /** 加载默认位置的用户偏好 profile（不存在时返回默认值）。 */
    public static Profile loadProfile() throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistLoadProfile();
    }

    /** 保存用户偏好 profile 到默认位置。 */
    public static void saveProfile(Profile profile) throws ScuLkException {
        ensureLoaded();
        SculkFfi.persistSaveProfile(profile);
    }

    /** 从指定路径加载 profile。 */
    public static Profile loadProfileFrom(String path) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.persistLoadProfileFrom(path);
    }

    /** 保存 profile 到指定路径。 */
    public static void saveProfileTo(String path, Profile profile) throws ScuLkException {
        ensureLoaded();
        SculkFfi.persistSaveProfileTo(path, profile);
    }

    /** host 端偏好配置。 */
    public static HostProfile hostProfile(int port, TokenRefreshSetting tokenRefresh) {
        return new HostProfile((short) port, tokenRefresh);
    }

    /** join 端偏好配置。 */
    public static JoinProfile joinProfile(int port) {
        return new JoinProfile((short) port);
    }

    /** relay 偏好配置。 */
    public static RelayProfile relayProfile(boolean custom, String url) {
        return new RelayProfile(custom, url);
    }

    /** 用户偏好配置根结构。 */
    public static Profile profile(HostProfile host, JoinProfile join, RelayProfile relay) {
        return new Profile(host, join, relay);
    }

    // ------------------------------------------------------------------ LAN

    /** 开始监听 Minecraft LAN 公告（224.0.2.60:4445），发现可连接的本机服务后结束。 */
    public static LanScanner lanScannerStart() throws ScuLkException {
        ensureLoaded();
        return SculkFfi.lanScannerStart();
    }

    /** 向本机 Minecraft 客户端周期性发布 LAN 公告（在 multiplayer 列表里出现）。 */
    public static LanBroadcaster lanBroadcasterStart(String name, int port) throws ScuLkException {
        ensureLoaded();
        return SculkFfi.lanBroadcasterStart(name, (short) port);
    }

    // ------------------------------------------------------------------ 小工具

    private static byte[] toByteArray(List<Byte> raw) {
        if (raw == null) {
            return null;
        }
        byte[] out = new byte[raw.size()];
        for (int i = 0; i < out.length; i++) {
            Byte value = raw.get(i);
            out[i] = value == null ? 0 : value;
        }
        return out;
    }

    private static List<Byte> toByteList(byte[] raw) {
        if (raw == null) {
            return null;
        }
        if (raw.length != 0 && raw.length != 16 && raw.length != 32) {
            throw new IllegalArgumentException("长度必须是 16 或 32 字节，实际 " + raw.length);
        }
        List<Byte> out = new ArrayList<>(raw.length);
        for (byte b : raw) {
            out.add(b);
        }
        return out;
    }

    private static <T> T await(CompletableFuture<T> future) throws ScuLkException {
        try {
            return future.join();
        } catch (CompletionException error) {
            Throwable cause = error.getCause();
            if (cause instanceof ScuLkException sculk) {
                throw sculk;
            }
            if (cause instanceof RuntimeException runtime) {
                throw runtime;
            }
            throw new IllegalStateException(cause);
        }
    }
}
