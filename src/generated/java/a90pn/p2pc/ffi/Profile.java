package a90pn.p2pc.ffi;

/**
 * 用户偏好配置根结构。
 */
public class Profile {
    private HostProfile host;
    private JoinProfile join;
    private RelayProfile relay;

    public Profile(
        HostProfile host, 
        JoinProfile join, 
        RelayProfile relay
    ) {
        
        this.host = host;
        
        this.join = join;
        
        this.relay = relay;
    }
    
    public HostProfile host() {
        return this.host;
    }
    
    public JoinProfile join() {
        return this.join;
    }
    
    public RelayProfile relay() {
        return this.relay;
    }
    public void setHost(HostProfile host) {
        this.host = host;
    }
    public void setJoin(JoinProfile join) {
        this.join = join;
    }
    public void setRelay(RelayProfile relay) {
        this.relay = relay;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof Profile) {
            Profile _uniffiThat = (Profile) _uniffiOther;
            return (
              java.util.Objects.equals(host, _uniffiThat.host) && 
              
              java.util.Objects.equals(join, _uniffiThat.join) && 
              
              java.util.Objects.equals(relay, _uniffiThat.relay)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(host);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(join);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(relay);
        return _uniffiHash;
    }

    
    
    

}


