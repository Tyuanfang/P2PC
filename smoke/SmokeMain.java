import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import a90pn.pepc.SculkClient;
import a90pn.pepc.ffi.HostState;
import a90pn.pepc.ffi.Profile;
import a90pn.pepc.ffi.ScuLkException;
import a90pn.pepc.ffi.SculkNode;
import a90pn.pepc.ffi.TokenRefreshSetting;
import a90pn.pepc.ffi.TunnelService;
import a90pn.pepc.ffi.TunnelStatus;

/** 不依赖 Minecraft 的冒烟测试：原生库加载、密钥/URI/持久化、节点绑定。 */
public final class SmokeMain {
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            System.setProperty(SculkClient.LIBRARY_PROPERTY, args[0]);
        }

        SculkClient.ensureLoaded();
        System.out.println("loaded = true");
        System.out.println("platform = " + SculkClient.platformToken());
        System.out.println("defaultMcPort = " + SculkClient.defaultMcPort());
        System.out.println("defaultInletPort = " + SculkClient.defaultInletPort());

        byte[] key = SculkClient.generateSecretKey();
        System.out.println("secretKey bytes = " + key.length);
        System.out.println("secretKey public = " + SculkClient.secretKeyPublicKey(key));
        byte[] serviceId = SculkClient.generateServiceId();
        byte[] token = SculkClient.generateAccessToken();
        System.out.println("serviceId = " + serviceId.length + "B, token = " + token.length + "B");
        System.out.println("token matches self = " + SculkClient.accessTokenMatches(token, token));

        try {
            SculkClient.parseJoinUri("not-a-uri");
            System.out.println("parse invalid: UNEXPECTED OK");
        } catch (ScuLkException error) {
            System.out.println("parse invalid -> " + error.getClass().getSimpleName() + ": " + error.getMessage());
        }

        System.out.println("dataDir = " + SculkClient.dataDir());
        System.out.println("defaultKeyPath = " + SculkClient.defaultKeyPath());

        Path tmp = Files.createTempDirectory("sculk-smoke");
        Path keyPath = tmp.resolve("identity.key");
        byte[] first = SculkClient.loadOrGenerateKey(keyPath.toString());
        byte[] again = SculkClient.loadOrGenerateKey(keyPath.toString());
        System.out.println("persist key stable = " + Arrays.equals(first, again));

        Path hostStatePath = tmp.resolve("host.toml");
        HostState state = new HostState(toList(serviceId), SculkClient.tokenState(token, 1_700_000_000_000L));
        SculkClient.saveHostState(hostStatePath.toString(), state);
        HostState restored = SculkClient.loadHostState(hostStatePath.toString());
        System.out.println("host state roundtrip = "
                + (restored != null
                && Arrays.equals(toArray(restored.serviceId()), serviceId)
                && Arrays.equals(toArray(restored.tokenState().token()), token)
                && restored.tokenState().createdAtMs() == 1_700_000_000_000L));

        Path profilePath = tmp.resolve("profile.toml");
        Profile profile = SculkClient.profile(
                SculkClient.hostProfile(25566, TokenRefreshSetting.THREE_HOURS),
                SculkClient.joinProfile(31000),
                SculkClient.relayProfile(true, "https://relay.example.com"));
        SculkClient.saveProfileTo(profilePath.toString(), profile);
        Profile back = SculkClient.loadProfileFrom(profilePath.toString());
        System.out.println("profile roundtrip = "
                + (back.host().port() == 25566
                && back.host().tokenRefresh() == TokenRefreshSetting.THREE_HOURS
                && back.join().port() == 31000
                && back.relay().custom()
                && "https://relay.example.com".equals(back.relay().url())));

        TunnelService service = SculkClient.newService();
        TunnelStatus status = SculkClient.status(service);
        System.out.println("status.phase = " + status.state().phase());
        System.out.println("status.mode = " + status.state().mode());
        System.out.println("status.connections = " + status.connections().size());
        try {
            SculkClient.probeServer("127.0.0.1:1", 300);
            System.out.println("probe closed port: UNEXPECTED OK");
        } catch (ScuLkException error) {
            System.out.println("probe closed port -> " + error.getClass().getSimpleName());
        }
        service.close();

        // 节点绑定需要联网（发现中继）；超时就算跳过，不阻塞冒烟。
        Thread nodeProbe = new Thread(() -> {
            try {
                SculkNode node = SculkClient.bindNode(
                        SculkClient.nodeOptions(key, null, 0)).join();
                System.out.println("node endpoint = " + SculkClient.nodeEndpointId(node));
                System.out.println("node online = " + SculkClient.nodeStatus(node).join().online());
                SculkClient.closeNode(node).join();
                System.out.println("node closed");
            } catch (Throwable error) {
                System.out.println("node bind failed -> " + error);
            }
        }, "sculk-smoke-node");
        nodeProbe.setDaemon(true);
        nodeProbe.start();
        nodeProbe.join(25_000L);
        if (nodeProbe.isAlive()) {
            System.out.println("node bind: TIMEOUT (skipped)");
        }

        System.out.println("SMOKE OK");
    }

    private static List<Byte> toList(byte[] raw) {
        List<Byte> out = new ArrayList<>(raw.length);
        for (byte value : raw) {
            out.add(value);
        }
        return out;
    }

    private static byte[] toArray(List<Byte> raw) {
        byte[] out = new byte[raw.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = raw.get(i);
        }
        return out;
    }
}
