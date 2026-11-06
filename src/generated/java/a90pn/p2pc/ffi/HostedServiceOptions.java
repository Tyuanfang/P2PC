package a90pn.p2pc.ffi;

/**
 * 发布单个 Host 服务的参数。
 */
public class HostedServiceOptions {
    /**
     * 稳定的服务路由标识（16 字节）。
     */
    private java.util.List<java.lang.Byte> serviceId;
    /**
     * 仅允许本机 loopback 的目标 Minecraft 地址，如 `127.0.0.1:25565`。
     */
    private java.lang.String targetAddr;
    /**
     * 上次发布保存的令牌状态；首次发布时为 null。
     */
    private TokenState tokenState;
    /**
     * 令牌刷新策略。
     */
    private TokenRefreshPolicy tokenRefresh;
    /**
     * 服务级连接策略。
     */
    private HostConfig config;

    public HostedServiceOptions(
        java.util.List<java.lang.Byte> serviceId, 
        java.lang.String targetAddr, 
        TokenState tokenState, 
        TokenRefreshPolicy tokenRefresh, 
        HostConfig config
    ) {
        
        this.serviceId = serviceId;
        
        this.targetAddr = targetAddr;
        
        this.tokenState = tokenState;
        
        this.tokenRefresh = tokenRefresh;
        
        this.config = config;
    }
    
    public java.util.List<java.lang.Byte> serviceId() {
        return this.serviceId;
    }
    
    public java.lang.String targetAddr() {
        return this.targetAddr;
    }
    
    public TokenState tokenState() {
        return this.tokenState;
    }
    
    public TokenRefreshPolicy tokenRefresh() {
        return this.tokenRefresh;
    }
    
    public HostConfig config() {
        return this.config;
    }
    public void setServiceId(java.util.List<java.lang.Byte> serviceId) {
        this.serviceId = serviceId;
    }
    public void setTargetAddr(java.lang.String targetAddr) {
        this.targetAddr = targetAddr;
    }
    public void setTokenState(TokenState tokenState) {
        this.tokenState = tokenState;
    }
    public void setTokenRefresh(TokenRefreshPolicy tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }
    public void setConfig(HostConfig config) {
        this.config = config;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostedServiceOptions) {
            HostedServiceOptions _uniffiThat = (HostedServiceOptions) _uniffiOther;
            return (
              java.util.Objects.equals(serviceId, _uniffiThat.serviceId) && 
              
              java.util.Objects.equals(targetAddr, _uniffiThat.targetAddr) && 
              
              java.util.Objects.equals(tokenState, _uniffiThat.tokenState) && 
              
              java.util.Objects.equals(tokenRefresh, _uniffiThat.tokenRefresh) && 
              
              java.util.Objects.equals(config, _uniffiThat.config)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(serviceId);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(targetAddr);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(tokenState);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(tokenRefresh);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(config);
        return _uniffiHash;
    }

    
    
    

}


