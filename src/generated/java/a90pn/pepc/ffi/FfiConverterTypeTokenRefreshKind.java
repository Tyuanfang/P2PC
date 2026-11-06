package a90pn.pepc.ffi;


public enum FfiConverterTypeTokenRefreshKind implements FfiConverterRustBuffer<TokenRefreshKind> {
    INSTANCE;

    @Override
    public TokenRefreshKind read(java.nio.ByteBuffer buf) {
        try {
            return TokenRefreshKind.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(TokenRefreshKind value) {
        return 4L;
    }

    @Override
    public void write(TokenRefreshKind value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


