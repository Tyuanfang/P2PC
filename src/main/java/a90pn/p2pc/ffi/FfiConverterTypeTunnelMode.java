package a90pn.p2pc.ffi;


public enum FfiConverterTypeTunnelMode implements FfiConverterRustBuffer<TunnelMode> {
    INSTANCE;

    @Override
    public TunnelMode read(java.nio.ByteBuffer buf) {
        try {
            return TunnelMode.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(TunnelMode value) {
        return 4L;
    }

    @Override
    public void write(TunnelMode value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


