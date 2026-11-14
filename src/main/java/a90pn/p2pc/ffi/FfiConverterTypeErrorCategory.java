package a90pn.p2pc.ffi;


public enum FfiConverterTypeErrorCategory implements FfiConverterRustBuffer<ErrorCategory> {
    INSTANCE;

    @Override
    public ErrorCategory read(java.nio.ByteBuffer buf) {
        try {
            return ErrorCategory.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(ErrorCategory value) {
        return 4L;
    }

    @Override
    public void write(ErrorCategory value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


