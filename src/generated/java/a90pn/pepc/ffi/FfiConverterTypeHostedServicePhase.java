package a90pn.pepc.ffi;


public enum FfiConverterTypeHostedServicePhase implements FfiConverterRustBuffer<HostedServicePhase> {
    INSTANCE;

    @Override
    public HostedServicePhase read(java.nio.ByteBuffer buf) {
        try {
            return HostedServicePhase.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(HostedServicePhase value) {
        return 4L;
    }

    @Override
    public void write(HostedServicePhase value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


