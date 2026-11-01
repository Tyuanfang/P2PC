package a90pn.pepc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import a90pn.pepc.ffi.HostConfig;
import a90pn.pepc.ffi.HostOptions;
import a90pn.pepc.ffi.JoinConfig;
import a90pn.pepc.ffi.JoinOptions;
import a90pn.pepc.ffi.LanBroadcaster;
import a90pn.pepc.ffi.LanScanner;
import a90pn.pepc.ffi.ScuLkException;
import a90pn.pepc.ffi.SculkFfi;
import a90pn.pepc.ffi.Subscription;
import a90pn.pepc.ffi.TunnelService;
import a90pn.pepc.ffi.TunnelStatus;
import a90pn.pepc.ffi.TunnelUpdate;
import a90pn.pepc.ffi.UniffiInitializer;

/**
 * sculk 联机内核的 Java 入口（模组里只需要碰这一个类）。
 *
 * <p>底层由 uniffi-bindgen-java 从 {@code sculk_ffi.dll} 生成的 Panama FFM 绑定
 * （包 {@code a90pn.pepc.ffi}）组成；这里做三件事：
 * <ul>
 *   <li>把模组 jar 里的原生库解包到临时目录，并设置
 *       {@code uniffi.component.sculk_ffi.libraryOverride} 让生成代码用绝对路径加载；</li>
 *   <li>把生成绑定里对 Java 不友好的类型（{@code List<Byte>} 密钥、{@code short} 端口）
 *       收成普通 {@code byte[]}/{@code int}；</li>
 *   <li>把 12 种隧道事件 + 状态快照转成一个可直接消费的事件流。</li>
 * </ul>
 *
 * <p>要求 Java 22+（Panama FFM 定稿 API），运行时建议加
 * {@code --enable-native-access=ALL-UNNAMED}（22/23 必需，24+ 只是消警告）。
 */
public final class SculkClient {
    /** 生成代码读取的库路径覆盖属性（namespace = sculk_ffi）。 */
    public static final String LIBRARY_PROPERTY = "uniffi.component.sculk_ffi.libraryOverride";

    /** 模组内 DLL 资源路径（见 build.gradle.kts 的 jar 打包配置）。 */
    private static final String BUNDLED_DLL = "/natives/windows-x86_64/sculk_ffi.dll";

    private static volatile boolean loaded = false;

    private SculkClient() {
    }

    // ------------------------------------------------------------------ 原生库

    /**
     * 加载原生库（幂等）。必须在首次触碰任何生成绑定之前调用；模组初始化时调一次即可。
     */
    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }

        String override = System.getProperty(LIBRARY_PROPERTY);
        if (override == null || override.isBlank()) {
            override = extractBundledDll().toString();
            System.setProperty(LIBRARY_PROPERTY, override);
        }

        // 触发生成代码的静态初始化：System.load + UniFFI contract/checksum 校验。
        UniffiInitializer.ensureInitialized();
        loaded = true;
    }

    private static Path extractBundledDll() {
        try (InputStream in = SculkClient.class.getResourceAsStream(BUNDLED_DLL)) {
            if (in == null) {
                throw new IllegalStateException(
                        "sculk_ffi.dll 不在模组内（" + BUNDLED_DLL
                                + "），先构建 Rust 侧（cargo build -p sculk-ffi --release）再重新打包");
            }
            Path dir = Files.createTempDirectory("p2pc-natives");
            Path dll = dir.resolve("sculk_ffi.dll");
            Files.copy(in, dll, StandardCopyOption.REPLACE_EXISTING);
            dll.toFile().deleteOnExit();
            dir.toFile().deleteOnExit();
            return dll;
        } catch (IOException error) {
            throw new IllegalStateException("解包 sculk_ffi.dll 失败", error);
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

    // ------------------------------------------------------------------ 参数构造

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

    // ------------------------------------------------------------------ 隧道

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
}
