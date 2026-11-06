package a90pn.p2pc.ffi;

/**
 * 服务过程事件读取端；recv 返回 null 表示订阅已关闭。
 */
public interface HostedServiceEventReaderInterface {
    public java.util.concurrent.CompletableFuture<TunnelEvent> recv();
    
}

