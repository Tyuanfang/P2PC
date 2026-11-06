package a90pn.p2pc.ffi;

/**
 * Join 端行为配置。
 */
public class JoinConfig {
    /**
     * PathChanged 事件节流（毫秒）；0 = 仅变化时发送。
     */
    private long eventDelayMs;
    /**
     * 断线重连最长等待（毫秒）；null = 无限等待，0 = 关闭重连。
     */
    private java.lang.Long reconnectTimeoutMs;
    /**
     * 本地 TCP 转发会话上限；0 = 使用默认值 64。
     */
    private long localSessionsMax;

    public JoinConfig(
        long eventDelayMs, 
        java.lang.Long reconnectTimeoutMs, 
        long localSessionsMax
    ) {
        
        this.eventDelayMs = eventDelayMs;
        
        this.reconnectTimeoutMs = reconnectTimeoutMs;
        
        this.localSessionsMax = localSessionsMax;
    }
    
    public long eventDelayMs() {
        return this.eventDelayMs;
    }
    
    public java.lang.Long reconnectTimeoutMs() {
        return this.reconnectTimeoutMs;
    }
    
    public long localSessionsMax() {
        return this.localSessionsMax;
    }
    public void setEventDelayMs(long eventDelayMs) {
        this.eventDelayMs = eventDelayMs;
    }
    public void setReconnectTimeoutMs(java.lang.Long reconnectTimeoutMs) {
        this.reconnectTimeoutMs = reconnectTimeoutMs;
    }
    public void setLocalSessionsMax(long localSessionsMax) {
        this.localSessionsMax = localSessionsMax;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof JoinConfig) {
            JoinConfig _uniffiThat = (JoinConfig) _uniffiOther;
            return (
              eventDelayMs == _uniffiThat.eventDelayMs && 
              
              java.util.Objects.equals(reconnectTimeoutMs, _uniffiThat.reconnectTimeoutMs) && 
              
              localSessionsMax == _uniffiThat.localSessionsMax
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(eventDelayMs);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(reconnectTimeoutMs);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(localSessionsMax);
        return _uniffiHash;
    }

    
    
    

}


