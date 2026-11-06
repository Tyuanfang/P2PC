package a90pn.p2pc.ffi;

/**
 * 单实例托管隧道服务（Clone 语义由 Rust 侧对象共享）。
 */
public interface TunnelServiceInterface {
    public java.util.concurrent.CompletableFuture<java.lang.Void> shutdown() ;
    public java.util.concurrent.CompletableFuture<java.lang.Void> startHost(HostOptions options) ;
    public java.util.concurrent.CompletableFuture<java.lang.Void> startJoin(JoinOptions options) ;
    public TunnelStatus status();
    public java.util.concurrent.CompletableFuture<TunnelStatus> stop() ;
    public Subscription subscribe();
    
}

