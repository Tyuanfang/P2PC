package a90pn.pepc.ffi;

/**
 * 节点密钥。
 */
public interface SecretKeyInterface {
    
    /**
     * 对应公钥的 EndpointId。
     */public java.lang.String publicKey();
    
    /**
     * 导出 32 字节。
     */public java.util.List<java.lang.Byte> toBytes();
    
}

