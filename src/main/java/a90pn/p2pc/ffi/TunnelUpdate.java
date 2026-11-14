package a90pn.p2pc.ffi;

/**
 * 统一更新：状态快照或过程事件。
 */
public class TunnelUpdate {
    private boolean isEvent;
    private TunnelStatus status;
    private TunnelEvent event;

    public TunnelUpdate(
        boolean isEvent, 
        TunnelStatus status, 
        TunnelEvent event
    ) {
        
        this.isEvent = isEvent;
        
        this.status = status;
        
        this.event = event;
    }
    
    public boolean isEvent() {
        return this.isEvent;
    }
    
    public TunnelStatus status() {
        return this.status;
    }
    
    public TunnelEvent event() {
        return this.event;
    }
    public void setIsEvent(boolean isEvent) {
        this.isEvent = isEvent;
    }
    public void setStatus(TunnelStatus status) {
        this.status = status;
    }
    public void setEvent(TunnelEvent event) {
        this.event = event;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TunnelUpdate) {
            TunnelUpdate _uniffiThat = (TunnelUpdate) _uniffiOther;
            return (
              isEvent == _uniffiThat.isEvent && 
              
              java.util.Objects.equals(status, _uniffiThat.status) && 
              
              java.util.Objects.equals(event, _uniffiThat.event)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(isEvent);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(status);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(event);
        return _uniffiHash;
    }

    
    
    

}


