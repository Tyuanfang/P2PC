package a90pn.p2pc.ffi;

public enum FfiConverterOptionalTypeTunnelMode implements FfiConverterRustBuffer<TunnelMode> {
  INSTANCE;

  @Override
  public TunnelMode read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeTunnelMode.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(TunnelMode value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeTunnelMode.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(TunnelMode value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeTunnelMode.INSTANCE.write(value, buf);
    }
  }
}
