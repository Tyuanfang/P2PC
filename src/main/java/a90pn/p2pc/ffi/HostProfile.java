package a90pn.p2pc.ffi;

/**
 * host 端偏好配置。
 */
public class HostProfile {
    /**
     * 本地 Minecraft 服务端监听端口。
     */
    private short port;
    /**
     * 跨发布生效的分享链接刷新策略。
     */
    private TokenRefreshSetting tokenRefresh;

    public HostProfile(
        short port, 
        TokenRefreshSetting tokenRefresh
    ) {
        
        this.port = port;
        
        this.tokenRefresh = tokenRefresh;
    }
    
    public short port() {
        return this.port;
    }
    
    public TokenRefreshSetting tokenRefresh() {
        return this.tokenRefresh;
    }
    public void setPort(short port) {
        this.port = port;
    }
    public void setTokenRefresh(TokenRefreshSetting tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostProfile) {
            HostProfile _uniffiThat = (HostProfile) _uniffiOther;
            return (
              port == _uniffiThat.port && 
              
              java.util.Objects.equals(tokenRefresh, _uniffiThat.tokenRefresh)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Short.hashCode(port);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(tokenRefresh);
        return _uniffiHash;
    }

    
    
    

}


