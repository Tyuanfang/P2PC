package a90pn.p2pc.ffi;

/**
 * 单个已发布服务的稳定状态快照。
 */
public class HostedServiceStatus {
    /**
     * 服务标识（16 字节）。
     */
    private java.util.List<java.lang.Byte> serviceId;
    /**
     * 当前生命周期阶段。
     */
    private HostedServicePhase phase;
    /**
     * 当前分享 URI 的代数。
     */
    private long uriGeneration;
    /**
     * 当前令牌创建时刻（Unix 毫秒）。
     */
    private long tokenCreatedAtMs;
    /**
     * 下一次自动轮换时刻（Unix 毫秒）；null = 不自动轮换。
     */
    private java.lang.Long nextRotationAtMs;
    /**
     * 当前已认证且仍存活的连接数。
     */
    private long connectionCount;
    /**
     * 当前活跃的 TCP-to-QUIC 桥接数。
     */
    private long bridgeCount;
    /**
     * 最近一次影响此服务的结构化错误。
     */
    private ErrorCategory lastError;

    public HostedServiceStatus(
        java.util.List<java.lang.Byte> serviceId, 
        HostedServicePhase phase, 
        long uriGeneration, 
        long tokenCreatedAtMs, 
        java.lang.Long nextRotationAtMs, 
        long connectionCount, 
        long bridgeCount, 
        ErrorCategory lastError
    ) {
        
        this.serviceId = serviceId;
        
        this.phase = phase;
        
        this.uriGeneration = uriGeneration;
        
        this.tokenCreatedAtMs = tokenCreatedAtMs;
        
        this.nextRotationAtMs = nextRotationAtMs;
        
        this.connectionCount = connectionCount;
        
        this.bridgeCount = bridgeCount;
        
        this.lastError = lastError;
    }
    
    public java.util.List<java.lang.Byte> serviceId() {
        return this.serviceId;
    }
    
    public HostedServicePhase phase() {
        return this.phase;
    }
    
    public long uriGeneration() {
        return this.uriGeneration;
    }
    
    public long tokenCreatedAtMs() {
        return this.tokenCreatedAtMs;
    }
    
    public java.lang.Long nextRotationAtMs() {
        return this.nextRotationAtMs;
    }
    
    public long connectionCount() {
        return this.connectionCount;
    }
    
    public long bridgeCount() {
        return this.bridgeCount;
    }
    
    public ErrorCategory lastError() {
        return this.lastError;
    }
    public void setServiceId(java.util.List<java.lang.Byte> serviceId) {
        this.serviceId = serviceId;
    }
    public void setPhase(HostedServicePhase phase) {
        this.phase = phase;
    }
    public void setUriGeneration(long uriGeneration) {
        this.uriGeneration = uriGeneration;
    }
    public void setTokenCreatedAtMs(long tokenCreatedAtMs) {
        this.tokenCreatedAtMs = tokenCreatedAtMs;
    }
    public void setNextRotationAtMs(java.lang.Long nextRotationAtMs) {
        this.nextRotationAtMs = nextRotationAtMs;
    }
    public void setConnectionCount(long connectionCount) {
        this.connectionCount = connectionCount;
    }
    public void setBridgeCount(long bridgeCount) {
        this.bridgeCount = bridgeCount;
    }
    public void setLastError(ErrorCategory lastError) {
        this.lastError = lastError;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostedServiceStatus) {
            HostedServiceStatus _uniffiThat = (HostedServiceStatus) _uniffiOther;
            return (
              java.util.Objects.equals(serviceId, _uniffiThat.serviceId) && 
              
              java.util.Objects.equals(phase, _uniffiThat.phase) && 
              
              uriGeneration == _uniffiThat.uriGeneration && 
              
              tokenCreatedAtMs == _uniffiThat.tokenCreatedAtMs && 
              
              java.util.Objects.equals(nextRotationAtMs, _uniffiThat.nextRotationAtMs) && 
              
              connectionCount == _uniffiThat.connectionCount && 
              
              bridgeCount == _uniffiThat.bridgeCount && 
              
              java.util.Objects.equals(lastError, _uniffiThat.lastError)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(serviceId);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(phase);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(uriGeneration);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(tokenCreatedAtMs);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(nextRotationAtMs);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(connectionCount);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(bridgeCount);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(lastError);
        return _uniffiHash;
    }

    
    
    

}


