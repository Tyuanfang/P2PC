package a90pn.pepc.ffi;

/**
 * 仅在当前 Host 会话中有效的访问令牌。
 */
public interface AccessTokenInterface {
    
    /**
     * 以常量时间比较两个令牌。
     */public boolean matches(java.util.List<java.lang.Byte> other);
    
    /**
     * 导出 32 字节。
     */public java.util.List<java.lang.Byte> toBytes();
    
}

