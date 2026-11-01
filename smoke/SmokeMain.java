import a90pn.pepc.SculkClient;
import a90pn.pepc.ffi.ScuLkException;
import a90pn.pepc.ffi.Subscription;
import a90pn.pepc.ffi.TunnelService;
import a90pn.pepc.ffi.TunnelStatus;
import a90pn.pepc.ffi.TunnelUpdate;

public final class SmokeMain {
    public static void main(String[] args) throws Exception {
        SculkClient.ensureLoaded();
        System.out.println("loaded = true");
        System.out.println("defaultMcPort  = " + SculkClient.defaultMcPort());
        System.out.println("defaultInletPort = " + SculkClient.defaultInletPort());

        try (TunnelService service = SculkClient.newService()) {
            TunnelStatus status = SculkClient.status(service);
            System.out.println("status.phase = " + status.state().phase());
            System.out.println("status.mode  = " + status.state().mode());
            System.out.println("status.connections = " + status.connections().size());

            // 订阅后第一条应当是当前状态快照
            try (Subscription subscription = service.subscribe()) {
                TunnelUpdate first = subscription.recv().join();
                System.out.println("first update is_event = " + first.isEvent()
                        + ", phase = " + first.status().state().phase());
            }
        }

        try {
            SculkClient.probeServer("127.0.0.1:1", 300);
            System.out.println("probe: UNEXPECTED success");
        } catch (ScuLkException error) {
            System.out.println("probe closed port -> " + error.getClass().getSimpleName()
                    + ": " + error.getMessage());
        }

        System.out.println("SMOKE OK");
    }
}
