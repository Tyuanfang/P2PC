package a90pn.pepc.ffi;

/**
 * 已发布服务的轻量操作句柄。
 */
public interface HostedServiceHandleInterface {
    
    /**
     * 当前可分享 Join URI。
     */public java.util.concurrent.CompletableFuture<JoinUri> joinUri() ;
    
    /**
     * 立即轮换访问令牌，并返回新的 Join URI。
     */public java.util.concurrent.CompletableFuture<JoinUri> rotateToken() ;
    
    /**
     * 服务标识（16 字节）。
     */public java.util.List<java.lang.Byte> serviceId();
    
    /**
     * 服务的稳定状态快照。
     */public java.util.concurrent.CompletableFuture<HostedServiceStatus> status() ;
    
    /**
     * 独立停止此服务。
     */public java.util.concurrent.CompletableFuture<java.lang.Void> stop() ;
    
    /**
     * 订阅此服务的过程事件。
     */public java.util.concurrent.CompletableFuture<HostedServiceEventReader> subscribe() ;
    
    /**
     * 订阅此服务可恢复的状态快照。
     */public java.util.concurrent.CompletableFuture<HostedServiceStatusSubscription> subscribeStatus() ;
    
    /**
     * 当前令牌及其创建时刻。
     */public java.util.concurrent.CompletableFuture<TokenState> tokenState() ;
    
}

