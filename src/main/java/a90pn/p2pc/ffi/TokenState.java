package a90pn.p2pc.ffi;

/**
 * 需要由实例管理层安全持久化的访问令牌状态。
 */
public class TokenState {
    /**
     * 访问令牌（32 字节）。
     */
    private java.util.List<java.lang.Byte> token;
    /**
     * 令牌创建时刻（Unix 毫秒）。
     */
    private long createdAtMs;

    public TokenState(
        java.util.List<java.lang.Byte> token, 
        long createdAtMs
    ) {
        
        this.token = token;
        
        this.createdAtMs = createdAtMs;
    }
    
    public java.util.List<java.lang.Byte> token() {
        return this.token;
    }
    
    public long createdAtMs() {
        return this.createdAtMs;
    }
    public void setToken(java.util.List<java.lang.Byte> token) {
        this.token = token;
    }
    public void setCreatedAtMs(long createdAtMs) {
        this.createdAtMs = createdAtMs;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TokenState) {
            TokenState _uniffiThat = (TokenState) _uniffiOther;
            return (
              java.util.Objects.equals(token, _uniffiThat.token) && 
              
              createdAtMs == _uniffiThat.createdAtMs
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(token);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(createdAtMs);
        return _uniffiHash;
    }

    
    
    

}


