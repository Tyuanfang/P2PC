package a90pn.pepc.ffi;

/**
 * `sculk://join/v1/` 分享 URI。
 */
public interface JoinUriInterface {
    
    /**
     * 导出含访问令牌的完整 URI（仅用于分享，勿写入日志）。
     */public java.lang.String exposeSecretUri() throws ScuLkException;
    
    /**
     * 脱敏后的展示形式（不含访问令牌）。
     */public java.lang.String redacted();
    
    /**
     * 自定义中继地址。
     */public java.lang.String relayUrl();
    
}

