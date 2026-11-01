package a90pn.pepc.ffi;

/**
 * 拍平的隧道事件记录：`kind` 决定哪些字段有效。
 */
public class TunnelEvent {
    private EventKind kind;
    private java.lang.String playerId;
    private java.lang.String reason;
    private java.lang.String remoteId;
    private boolean isRelay;
    private long rttMs;
    private int attempt;
    private java.lang.Long retryInMs;
    private ErrorCategory category;
    private java.lang.String message;

    public TunnelEvent(
        EventKind kind, 
        java.lang.String playerId, 
        java.lang.String reason, 
        java.lang.String remoteId, 
        boolean isRelay, 
        long rttMs, 
        int attempt, 
        java.lang.Long retryInMs, 
        ErrorCategory category, 
        java.lang.String message
    ) {
        
        this.kind = kind;
        
        this.playerId = playerId;
        
        this.reason = reason;
        
        this.remoteId = remoteId;
        
        this.isRelay = isRelay;
        
        this.rttMs = rttMs;
        
        this.attempt = attempt;
        
        this.retryInMs = retryInMs;
        
        this.category = category;
        
        this.message = message;
    }
    
    public EventKind kind() {
        return this.kind;
    }
    
    public java.lang.String playerId() {
        return this.playerId;
    }
    
    public java.lang.String reason() {
        return this.reason;
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
    
    public int attempt() {
        return this.attempt;
    }
    
    public java.lang.Long retryInMs() {
        return this.retryInMs;
    }
    
    public ErrorCategory category() {
        return this.category;
    }
    
    public java.lang.String message() {
        return this.message;
    }
    public void setKind(EventKind kind) {
        this.kind = kind;
    }
    public void setPlayerId(java.lang.String playerId) {
        this.playerId = playerId;
    }
    public void setReason(java.lang.String reason) {
        this.reason = reason;
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
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
    public void setRetryInMs(java.lang.Long retryInMs) {
        this.retryInMs = retryInMs;
    }
    public void setCategory(ErrorCategory category) {
        this.category = category;
    }
    public void setMessage(java.lang.String message) {
        this.message = message;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof TunnelEvent) {
            TunnelEvent _uniffiThat = (TunnelEvent) _uniffiOther;
            return (
              java.util.Objects.equals(kind, _uniffiThat.kind) && 
              
              java.util.Objects.equals(playerId, _uniffiThat.playerId) && 
              
              java.util.Objects.equals(reason, _uniffiThat.reason) && 
              
              java.util.Objects.equals(remoteId, _uniffiThat.remoteId) && 
              
              isRelay == _uniffiThat.isRelay && 
              
              rttMs == _uniffiThat.rttMs && 
              
              attempt == _uniffiThat.attempt && 
              
              java.util.Objects.equals(retryInMs, _uniffiThat.retryInMs) && 
              
              java.util.Objects.equals(category, _uniffiThat.category) && 
              
              java.util.Objects.equals(message, _uniffiThat.message)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(kind);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(playerId);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(reason);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(remoteId);
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(isRelay);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(rttMs);
        _uniffiHash = 31 * _uniffiHash + java.lang.Integer.hashCode(attempt);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(retryInMs);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(category);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(message);
        return _uniffiHash;
    }

    
    
    

}


