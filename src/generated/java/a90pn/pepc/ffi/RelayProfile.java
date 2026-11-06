package a90pn.pepc.ffi;

/**
 * relay 偏好配置。
 */
public class RelayProfile {
    /**
     * true 启用自建中继，false 使用 iroh 内置中继。
     */
    private boolean custom;
    /**
     * 自建中继地址，仅 custom = true 时生效。
     */
    private java.lang.String url;

    public RelayProfile(
        boolean custom, 
        java.lang.String url
    ) {
        
        this.custom = custom;
        
        this.url = url;
    }
    
    public boolean custom() {
        return this.custom;
    }
    
    public java.lang.String url() {
        return this.url;
    }
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    

    
    @Override
    public boolean equals(java.lang.Object _uniffiOther) {
        if (_uniffiOther instanceof RelayProfile) {
            RelayProfile _uniffiThat = (RelayProfile) _uniffiOther;
            return (
              custom == _uniffiThat.custom && 
              
              java.util.Objects.equals(url, _uniffiThat.url)
              
            );
        };
        return false;
    }
    @Override
    public int hashCode() {
        int _uniffiHash = 17;
        _uniffiHash = 31 * _uniffiHash + java.lang.Boolean.hashCode(custom);
        _uniffiHash = 31 * _uniffiHash + java.util.Objects.hashCode(url);
        return _uniffiHash;
    }

    
    
    

}


