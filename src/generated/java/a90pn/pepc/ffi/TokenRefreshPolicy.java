package a90pn.pepc.ffi;

/**
 * 跨服务停止和重启生效的令牌刷新策略。
 */
public class TokenRefreshPolicy {
    /**
     * 策略种类。
     */
    private TokenRefreshKind kind;
    /**
     * `After` 的轮换周期（毫秒）；其他种类忽略。
     */
    private long periodMs;

    public TokenRefreshPolicy(
        TokenRefreshKind kind, 
        long periodMs
    ) {
        
        this.kind = kind;
        
        this.periodMs = periodMs;
    }
    
    public TokenRefreshKind kind() {
        return this.kind;
    }
    
    public long periodMs() {
        return this.periodMs;
    }
    public void setKind(TokenRefreshKind kind) {
        this.kind = kind;
    }
    public void setPeriodMs(long periodMs) {
        this.periodMs = periodMs;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TokenRefreshPolicy) {
            TokenRefreshPolicy _uniffiThat = (TokenRefreshPolicy) _uniffiOther;
            return (
              java.util.Objects.equals(kind, _uniffiThat.kind) && 
              
              periodMs == _uniffiThat.periodMs
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(kind);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(periodMs);
        return _uniffiHash;
    }

    
    
    

}


