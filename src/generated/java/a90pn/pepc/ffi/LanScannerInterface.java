package a90pn.pepc.ffi;

/**
 * LAN 公告扫描器。
 */
public interface LanScannerInterface {
    public boolean isFinished();
    public void stop() throws ScuLkException;
    public java.lang.Short tryRecv();
    
}

