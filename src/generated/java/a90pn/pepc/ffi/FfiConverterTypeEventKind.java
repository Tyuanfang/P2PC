package a90pn.pepc.ffi;


public enum FfiConverterTypeEventKind implements FfiConverterRustBuffer<EventKind> {
    INSTANCE;

    @Override
    public EventKind read(java.nio.ByteBuffer buf) {
        try {
            return EventKind.values()[buf.getInt() - 1];
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new java.lang.RuntimeException("invalid enum value, something is very wrong!!", e);
        }
    }

    @Override
    public long allocationSize(EventKind value) {
        return 4L;
    }

    @Override
    public void write(EventKind value, java.nio.ByteBuffer buf) {
        buf.putInt(value.ordinal() + 1);
    }
}


