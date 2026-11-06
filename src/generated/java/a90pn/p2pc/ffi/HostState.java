package a90pn.p2pc.ffi;

/**
 * 跨 Host 发布保存的稳定服务标识和令牌状态。
 */
public class HostState {
    /**
     * 服务标识（16 字节）。
     */
    private java.util.List<java.lang.Byte> serviceId;
    /**
     * 令牌状态。
     */
    private TokenState tokenState;

    public HostState(
        java.util.List<java.lang.Byte> serviceId, 
        TokenState tokenState
    ) {
        
        this.serviceId = serviceId;
        
        this.tokenState = tokenState;
    }
    
    public java.util.List<java.lang.Byte> serviceId() {
        return this.serviceId;
    }
    
    public TokenState tokenState() {
        return this.tokenState;
    }
    public void setServiceId(java.util.List<java.lang.Byte> serviceId) {
        this.serviceId = serviceId;
    }
    public void setTokenState(TokenState tokenState) {
        this.tokenState = tokenState;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostState) {
            HostState _uniffiThat = (HostState) _uniffiOther;
            return (
              java.util.Objects.equals(serviceId, _uniffiThat.serviceId) && 
              
              java.util.Objects.equals(tokenState, _uniffiThat.tokenState)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(serviceId);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(tokenState);
        return _uniffiHash;
    }

    
    
    

}


