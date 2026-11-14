package a90pn.p2pc.ffi;

/**
 * 多服务节点的创建参数。
 */
public class NodeOptions {
    /**
     * 稳定节点密钥（32 字节）；null = 生成临时身份。
     */
    private java.util.List<java.lang.Byte> secretKey;
    /**
     * Node 级自定义中继 URL；null = 使用 iroh 默认中继。
     */
    private java.lang.String relayUrl;
    /**
     * 同时等待认证的连接上限；0 = 使用默认值 128。
     */
    private long unauthenticatedConnectionsMax;

    public NodeOptions(
        java.util.List<java.lang.Byte> secretKey, 
        java.lang.String relayUrl, 
        long unauthenticatedConnectionsMax
    ) {
        
        this.secretKey = secretKey;
        
        this.relayUrl = relayUrl;
        
        this.unauthenticatedConnectionsMax = unauthenticatedConnectionsMax;
    }
    
    public java.util.List<java.lang.Byte> secretKey() {
        return this.secretKey;
    }
    
    public java.lang.String relayUrl() {
        return this.relayUrl;
    }
    
    public long unauthenticatedConnectionsMax() {
        return this.unauthenticatedConnectionsMax;
    }
    public void setSecretKey(java.util.List<java.lang.Byte> secretKey) {
        this.secretKey = secretKey;
    }
    public void setRelayUrl(java.lang.String relayUrl) {
        this.relayUrl = relayUrl;
    }
    public void setUnauthenticatedConnectionsMax(long unauthenticatedConnectionsMax) {
        this.unauthenticatedConnectionsMax = unauthenticatedConnectionsMax;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof NodeOptions) {
            NodeOptions _uniffiThat = (NodeOptions) _uniffiOther;
            return (
              java.util.Objects.equals(secretKey, _uniffiThat.secretKey) && 
              
              java.util.Objects.equals(relayUrl, _uniffiThat.relayUrl) && 
              
              unauthenticatedConnectionsMax == _uniffiThat.unauthenticatedConnectionsMax
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(secretKey);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(relayUrl);
        _uniffiHash = 31 * _uniffiHash + java.lang.Long.hashCode(unauthenticatedConnectionsMax);
        return _uniffiHash;
    }

    
    
    

}


