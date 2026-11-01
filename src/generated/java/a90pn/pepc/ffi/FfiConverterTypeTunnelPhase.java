package a90pn.pepc.ffi;


public enum FfiConverterTypeTunnelPhase implements FfiConverterRustBuffer<TunnelPhase> {
    INSTANCE;

    @Override
    public TunnelPhase read(java.nio.ByteBuffer buf) {
        try {
            return TunnelPhase.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(TunnelPhase value) {
        return 4L;
    }

    @Override
    public void write(TunnelPhase value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


