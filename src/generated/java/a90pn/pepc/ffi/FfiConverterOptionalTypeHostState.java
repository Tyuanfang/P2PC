package a90pn.pepc.ffi;

public enum FfiConverterOptionalTypeHostState implements FfiConverterRustBuffer<HostState> {
  INSTANCE;

  @Override
  public HostState read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeHostState.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(HostState value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeHostState.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(HostState value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeHostState.INSTANCE.write(value, buf);
    }
  }
}
