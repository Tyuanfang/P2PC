package a90pn.pepc.ffi;

/**
 * 多服务 Host 节点。
 */
public interface SculkNodeInterface {
    
    /**
     * Node 的稳定 EndpointId。
     */public java.lang.String endpointId();
    
    /**
     * 为指定服务生成新令牌，并返回对应的新 Join URI。
     */public java.util.concurrent.CompletableFuture<JoinUri> rotateToken(java.util.List<java.lang.Byte> serviceId) ;
    
    /**
     * 当前已发布的服务数量。
     */public java.util.concurrent.CompletableFuture<java.lang.Long> serviceCount();
    
    /**
     * 停止所有服务并关闭 Node Endpoint。
     */public java.util.concurrent.CompletableFuture<java.lang.Void> shutdown();
    
    /**
     * 发布服务并返回其操作句柄。
     */public java.util.concurrent.CompletableFuture<HostedServiceHandle> startService(HostedServiceOptions options) ;
    
    /**
     * Node 的稳定状态快照。
     */public java.util.concurrent.CompletableFuture<SculkNodeStatus> status();
    
    /**
     * 停止一个服务；不会关闭 Node 或影响其他服务。
     */public java.util.concurrent.CompletableFuture<java.lang.Void> stopService(java.util.List<java.lang.Byte> serviceId) ;
    
}

