package a90pn.p2pc.ffi;

/**
 * Join 启动参数。
 */
public class JoinOptions {
    /**
     * sculk://join/v1/ 分享 URI。
     */
    private java.lang.String joinUri;
    /**
     * 本地 Minecraft 客户端连接端口；0 = 自动分配。
     */
    private short localPort;
    /**
     * Join 行为配置。
     */
    private JoinConfig config;

    public JoinOptions(
        java.lang.String joinUri, 
        short localPort, 
        JoinConfig config
    ) {
        
        this.joinUri = joinUri;
        
        this.localPort = localPort;
        
        this.config = config;
    }
    
    public java.lang.String joinUri() {
        return this.joinUri;
    }
    
    public short localPort() {
        return this.localPort;
    }
    
    public JoinConfig config() {
        return this.config;
    }
    public void setJoinUri(java.lang.String joinUri) {
        this.joinUri = joinUri;
    }
    public void setLocalPort(short localPort) {
        this.localPort = localPort;
    }
    public void setConfig(JoinConfig config) {
        this.config = config;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof JoinOptions) {
            JoinOptions _uniffiThat = (JoinOptions) _uniffiOther;
            return (
              java.util.Objects.equals(joinUri, _uniffiThat.joinUri) && 
              
              localPort == _uniffiThat.localPort && 
              
              java.util.Objects.equals(config, _uniffiThat.config)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(joinUri);
        _uniffiHash = 31 * _uniffiHash + java.lang.Short.hashCode(localPort);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(config);
        return _uniffiHash;
    }

    
    
    

}


