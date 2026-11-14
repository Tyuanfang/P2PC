package a90pn.p2pc.ffi;

/**
 * 完整隧道快照。
 */
public class TunnelStatus {
    private TunnelState state;
    private java.util.List<ConnectionSnapshot> connections;
    private ErrorCategory lastError;

    public TunnelStatus(
        TunnelState state, 
        java.util.List<ConnectionSnapshot> connections, 
        ErrorCategory lastError
    ) {
        
        this.state = state;
        
        this.connections = connections;
        
        this.lastError = lastError;
    }
    
    public TunnelState state() {
        return this.state;
    }
    
    public java.util.List<ConnectionSnapshot> connections() {
        return this.connections;
    }
    
    public ErrorCategory lastError() {
        return this.lastError;
    }
    public void setState(TunnelState state) {
        this.state = state;
    }
    public void setConnections(java.util.List<ConnectionSnapshot> connections) {
        this.connections = connections;
    }
    public void setLastError(ErrorCategory lastError) {
        this.lastError = lastError;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TunnelStatus) {
            TunnelStatus _uniffiThat = (TunnelStatus) _uniffiOther;
            return (
              java.util.Objects.equals(state, _uniffiThat.state) && 
              
              java.util.Objects.equals(connections, _uniffiThat.connections) && 
              
              java.util.Objects.equals(lastError, _uniffiThat.lastError)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(state);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(connections);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(lastError);
        return _uniffiHash;
    }

    
    
    

}


