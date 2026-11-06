package a90pn.pepc.ffi;


public enum FfiConverterTypeTokenRefreshSetting implements FfiConverterRustBuffer<TokenRefreshSetting> {
    INSTANCE;

    @Override
    public TokenRefreshSetting read(java.nio.ByteBuffer buf) {
        try {
            return TokenRefreshSetting.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(TokenRefreshSetting value) {
        return 4L;
    }

    @Override
    public void write(TokenRefreshSetting value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


