package a90pn.pepc.ffi;

/**
 * join 端偏好配置。
 */
public class JoinProfile {
    /**
     * 本地入站监听端口。
     */
    private short port;

    public JoinProfile(
        short port
    ) {
        
        this.port = port;
    }
    
    public short port() {
        return this.port;
    }
    public void setPort(short port) {
        this.port = port;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof JoinProfile) {
            JoinProfile _uniffiThat = (JoinProfile) _uniffiOther;
            return (
              port == _uniffiThat.port
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Short.hashCode(port);
        return _uniffiHash;
    }

    
    
    

}


