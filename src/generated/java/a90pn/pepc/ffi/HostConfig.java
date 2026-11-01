package a90pn.pepc.ffi;

/**
 * Host 端行为配置。
 */
public class HostConfig {
    /**
     * PathChanged 事件节流（毫秒）；0 = 仅变化时发送。
     */
    private long eventDelayMs;
    /**
     * 最大玩家数；null = 不限制。
     */
    private java.lang.Integer maxPlayers;

    public HostConfig(
        long eventDelayMs, 
        java.lang.Integer maxPlayers
    ) {
        
        this.eventDelayMs = eventDelayMs;
        
        this.maxPlayers = maxPlayers;
    }
    
    public long eventDelayMs() {
        return this.eventDelayMs;
    }
    
    public java.lang.Integer maxPlayers() {
        return this.maxPlayers;
    }
    public void setEventDelayMs(long eventDelayMs) {
        this.eventDelayMs = eventDelayMs;
    }
    public void setMaxPlayers(java.lang.Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostConfig) {
            HostConfig _uniffiThat = (HostConfig) _uniffiOther;
            return (
              eventDelayMs == _uniffiThat.eventDelayMs && 
              
              java.util.Objects.equals(maxPlayers, _uniffiThat.maxPlayers)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(eventDelayMs);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(maxPlayers);
        return _uniffiHash;
    }

    
    
    

}


