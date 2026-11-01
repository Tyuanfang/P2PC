package a90pn.pepc.ffi;

/**
 * Host 启动参数。
 */
public class HostOptions {
    /**
     * 本地 Minecraft 服务端端口。
     */
    private short mcPort;
    /**
     * 稳定节点密钥（32 字节）；null = 生成临时密钥。
     */
    private java.util.List<java.lang.Byte> secretKey;
    /**
     * 自定义中继 URL；null = 使用 iroh 默认中继。
     */
    private java.lang.String relayUrl;
    /**
     * 服务标识（16 字节）；null = 自动生成。
     */
    private java.util.List<java.lang.Byte> serviceId;
    /**
     * 会话访问令牌（32 字节）；null = 自动生成。
     */
    private java.util.List<java.lang.Byte> token;
    /**
     * Host 行为配置。
     */
    private HostConfig config;

    public HostOptions(
        short mcPort, 
        java.util.List<java.lang.Byte> secretKey, 
        java.lang.String relayUrl, 
        java.util.List<java.lang.Byte> serviceId, 
        java.util.List<java.lang.Byte> token, 
        HostConfig config
    ) {
        
        this.mcPort = mcPort;
        
        this.secretKey = secretKey;
        
        this.relayUrl = relayUrl;
        
        this.serviceId = serviceId;
        
        this.token = token;
        
        this.config = config;
    }
    
    public short mcPort() {
        return this.mcPort;
    }
    
    public java.util.List<java.lang.Byte> secretKey() {
        return this.secretKey;
    }
    
    public java.lang.String relayUrl() {
        return this.relayUrl;
    }
    
    public java.util.List<java.lang.Byte> serviceId() {
        return this.serviceId;
    }
    
    public java.util.List<java.lang.Byte> token() {
        return this.token;
    }
    
    public HostConfig config() {
        return this.config;
    }
    public void setMcPort(short mcPort) {
        this.mcPort = mcPort;
    }
    public void setSecretKey(java.util.List<java.lang.Byte> secretKey) {
        this.secretKey = secretKey;
    }
    public void setRelayUrl(java.lang.String relayUrl) {
        this.relayUrl = relayUrl;
    }
    public void setServiceId(java.util.List<java.lang.Byte> serviceId) {
        this.serviceId = serviceId;
    }
    public void setToken(java.util.List<java.lang.Byte> token) {
        this.token = token;
    }
    public void setConfig(HostConfig config) {
        this.config = config;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof HostOptions) {
            HostOptions _uniffiThat = (HostOptions) _uniffiOther;
            return (
              mcPort == _uniffiThat.mcPort && 
              
              java.util.Objects.equals(secretKey, _uniffiThat.secretKey) && 
              
              java.util.Objects.equals(relayUrl, _uniffiThat.relayUrl) && 
              
              java.util.Objects.equals(serviceId, _uniffiThat.serviceId) && 
              
              java.util.Objects.equals(token, _uniffiThat.token) && 
              
              java.util.Objects.equals(config, _uniffiThat.config)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Short.hashCode(mcPort);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(secretKey);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(relayUrl);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(serviceId);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(token);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(config);
        return _uniffiHash;
    }

    
    
    

}


