package a90pn.p2pc.ffi;

/**
 * Node 的稳定状态快照。
 */
public class SculkNodeStatus {
    /**
     * Node 的稳定 EndpointId。
     */
    private java.lang.String endpointId;
    /**
     * 底层 Endpoint 是否仍在运行。
     */
    private boolean online;
    /**
     * Node 级自定义中继。
     */
    private java.lang.String relayUrl;
    /**
     * 当前已发布服务数。
     */
    private long serviceCount;
    /**
     * 当前等待认证的连接数。
     */
    private long unauthenticatedConnectionCount;
    /**
     * 认证并发上限。
     */
    private long unauthenticatedConnectionsMax;

    public SculkNodeStatus(
        java.lang.String endpointId, 
        boolean online, 
        java.lang.String relayUrl, 
        long serviceCount, 
        long unauthenticatedConnectionCount, 
        long unauthenticatedConnectionsMax
    ) {
        
        this.endpointId = endpointId;
        
        this.online = online;
        
        this.relayUrl = relayUrl;
        
        this.serviceCount = serviceCount;
        
        this.unauthenticatedConnectionCount = unauthenticatedConnectionCount;
        
        this.unauthenticatedConnectionsMax = unauthenticatedConnectionsMax;
    }
    
    public java.lang.String endpointId() {
        return this.endpointId;
    }
    
    public boolean online() {
        return this.online;
    }
    
    public java.lang.String relayUrl() {
        return this.relayUrl;
    }
    
    public long serviceCount() {
        return this.serviceCount;
    }
    
    public long unauthenticatedConnectionCount() {
        return this.unauthenticatedConnectionCount;
    }
    
    public long unauthenticatedConnectionsMax() {
        return this.unauthenticatedConnectionsMax;
    }
    public void setEndpointId(java.lang.String endpointId) {
        this.endpointId = endpointId;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public void setRelayUrl(java.lang.String relayUrl) {
        this.relayUrl = relayUrl;
    }
    public void setServiceCount(long serviceCount) {
        this.serviceCount = serviceCount;
    }
    public void setUnauthenticatedConnectionCount(long unauthenticatedConnectionCount) {
        this.unauthenticatedConnectionCount = unauthenticatedConnectionCount;
    }
    public void setUnauthenticatedConnectionsMax(long unauthenticatedConnectionsMax) {
        this.unauthenticatedConnectionsMax = unauthenticatedConnectionsMax;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof SculkNodeStatus) {
            SculkNodeStatus _uniffiThat = (SculkNodeStatus) _uniffiOther;
            return (
              java.util.Objects.equals(endpointId, _uniffiThat.endpointId) && 
              
              online == _uniffiThat.online && 
              
              java.util.Objects.equals(relayUrl, _uniffiThat.relayUrl) && 
              
              serviceCount == _uniffiThat.serviceCount && 
              
              unauthenticatedConnectionCount == _uniffiThat.unauthenticatedConnectionCount && 
              
              unauthenticatedConnectionsMax == _uniffiThat.unauthenticatedConnectionsMax
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(endpointId);
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(online);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(relayUrl);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(serviceCount);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(unauthenticatedConnectionCount);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(unauthenticatedConnectionsMax);
        return _uniffiHash;
    }

    
    
    

}


