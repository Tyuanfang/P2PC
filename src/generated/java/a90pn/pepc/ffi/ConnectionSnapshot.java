package a90pn.pepc.ffi;

/**
 * 单条连接瞬时快照。
 */
public class ConnectionSnapshot {
    private java.lang.String remoteId;
    private boolean isRelay;
    private long rttMs;
    private long txBytes;
    private long rxBytes;
    private boolean alive;
    private long elapsedMs;

    public ConnectionSnapshot(
        java.lang.String remoteId, 
        boolean isRelay, 
        long rttMs, 
        long txBytes, 
        long rxBytes, 
        boolean alive, 
        long elapsedMs
    ) {
        
        this.remoteId = remoteId;
        
        this.isRelay = isRelay;
        
        this.rttMs = rttMs;
        
        this.txBytes = txBytes;
        
        this.rxBytes = rxBytes;
        
        this.alive = alive;
        
        this.elapsedMs = elapsedMs;
    }
    
    public java.lang.String remoteId() {
        return this.remoteId;
    }
    
    public boolean isRelay() {
        return this.isRelay;
    }
    
    public long rttMs() {
        return this.rttMs;
    }
    
    public long txBytes() {
        return this.txBytes;
    }
    
    public long rxBytes() {
        return this.rxBytes;
    }
    
    public boolean alive() {
        return this.alive;
    }
    
    public long elapsedMs() {
        return this.elapsedMs;
    }
    public void setRemoteId(java.lang.String remoteId) {
        this.remoteId = remoteId;
    }
    public void setIsRelay(boolean isRelay) {
        this.isRelay = isRelay;
    }
    public void setRttMs(long rttMs) {
        this.rttMs = rttMs;
    }
    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }
    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof ConnectionSnapshot) {
            ConnectionSnapshot _uniffiThat = (ConnectionSnapshot) _uniffiOther;
            return (
              java.util.Objects.equals(remoteId, _uniffiThat.remoteId) && 
              
              isRelay == _uniffiThat.isRelay && 
              
              rttMs == _uniffiThat.rttMs && 
              
              txBytes == _uniffiThat.txBytes && 
              
              rxBytes == _uniffiThat.rxBytes && 
              
              alive == _uniffiThat.alive && 
              
              elapsedMs == _uniffiThat.elapsedMs
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(remoteId);
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(isRelay);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(rttMs);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(txBytes);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(rxBytes);
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(alive);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(elapsedMs);
        return _uniffiHash;
    }

    
    
    

}


