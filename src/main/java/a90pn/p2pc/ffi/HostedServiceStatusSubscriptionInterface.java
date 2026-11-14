package a90pn.p2pc.ffi;

/**
 * 服务状态订阅端；recv 返回 null 表示订阅已关闭。
 */
public interface HostedServiceStatusSubscriptionInterface {
    public java.util.concurrent.CompletableFuture<HostedServiceStatus> recv();
    
}

