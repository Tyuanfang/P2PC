package a90pn.p2pc.ffi;

/**
 * LAN 公告广播器。
 */
public interface LanBroadcasterInterface {
    public boolean isFinished();
    public void stop() throws ScuLkException;
    
}

