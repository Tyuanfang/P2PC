package a90pn.p2pc.ffi;

/**
 * 统一订阅端：首次 recv 返回当前状态，之后状态/事件交错。
 */
public interface SubscriptionInterface {
    public java.util.concurrent.CompletableFuture<TunnelUpdate> recv() ;
    
}

