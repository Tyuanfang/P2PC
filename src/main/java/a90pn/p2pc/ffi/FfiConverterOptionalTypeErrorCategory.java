package a90pn.p2pc.ffi;

public enum FfiConverterOptionalTypeErrorCategory implements FfiConverterRustBuffer<ErrorCategory> {
  INSTANCE;

  @Override
  public ErrorCategory read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeErrorCategory.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(ErrorCategory value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeErrorCategory.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(ErrorCategory value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeErrorCategory.INSTANCE.write(value, buf);
    }
  }
}
