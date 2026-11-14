package a90pn.p2pc.ffi;

/**
 * 轻量生命周期快照。
 */
public class TunnelState {
    private TunnelPhase phase;
    private TunnelMode mode;
    private java.lang.String joinUri;
    private java.lang.String localAddr;

    public TunnelState(
        TunnelPhase phase, 
        TunnelMode mode, 
        java.lang.String joinUri, 
        java.lang.String localAddr
    ) {
        
        this.phase = phase;
        
        this.mode = mode;
        
        this.joinUri = joinUri;
        
        this.localAddr = localAddr;
    }
    
    public TunnelPhase phase() {
        return this.phase;
    }
    
    public TunnelMode mode() {
        return this.mode;
    }
    
    public java.lang.String joinUri() {
        return this.joinUri;
    }
    
    public java.lang.String localAddr() {
        return this.localAddr;
    }
    public void setPhase(TunnelPhase phase) {
        this.phase = phase;
    }
    public void setMode(TunnelMode mode) {
        this.mode = mode;
    }
    public void setJoinUri(java.lang.String joinUri) {
        this.joinUri = joinUri;
    }
    public void setLocalAddr(java.lang.String localAddr) {
        this.localAddr = localAddr;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TunnelState) {
            TunnelState _uniffiThat = (TunnelState) _uniffiOther;
            return (
              java.util.Objects.equals(phase, _uniffiThat.phase) && 
              
              java.util.Objects.equals(mode, _uniffiThat.mode) && 
              
              java.util.Objects.equals(joinUri, _uniffiThat.joinUri) && 
              
              java.util.Objects.equals(localAddr, _uniffiThat.localAddr)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(phase);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(mode);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(joinUri);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(localAddr);
        return _uniffiHash;
    }

    
    
    

}


