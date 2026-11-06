package a90pn.p2pc.ffi;

public enum FfiConverterOptionalTypeTunnelEvent implements FfiConverterRustBuffer<TunnelEvent> {
  INSTANCE;

  @Override
  public TunnelEvent read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeTunnelEvent.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(TunnelEvent value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeTunnelEvent.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(TunnelEvent value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeTunnelEvent.INSTANCE.write(value, buf);
    }
  }
}
