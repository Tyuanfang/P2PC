package a90pn.pepc.ffi;


public enum FfiConverterTypeTunnelService implements FfiConverter<TunnelService, java.lang.Long> {
    INSTANCE;

    @Override
    public java.lang.Long lower(TunnelService value) {
        return value.uniffiCloneHandle();
    }

    @Override
    public TunnelService lift(java.lang.Long value) {
        return new TunnelService(UniffiWithHandle.INSTANCE, value);
    }

    @Override
    public TunnelService read(java.nio.ByteBuffer buf) {
        // The Rust code always writes handles as 8 bytes, and will
        // fail to compile if they don't fit.
        return lift(buf.getLong());
    }

    @Override
    public long allocationSize(TunnelService value) {
        return 8L;
    }

    @Override
    public void write(TunnelService value, java.nio.ByteBuffer buf) {
        // The Rust code always expects handles written as 8 bytes,
        // and will fail to compile if they don't fit.
        buf.putLong(lower(value));
    }
}
