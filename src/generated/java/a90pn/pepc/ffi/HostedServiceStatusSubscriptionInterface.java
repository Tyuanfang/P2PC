package a90pn.pepc.ffi;

/**
 * 服务状态订阅端；recv 返回 null 表示订阅已关闭。
 */
public interface HostedServiceStatusSubscriptionInterface {
    public java.util.concurrent.CompletableFuture<HostedServiceStatus> recv();
    
}

