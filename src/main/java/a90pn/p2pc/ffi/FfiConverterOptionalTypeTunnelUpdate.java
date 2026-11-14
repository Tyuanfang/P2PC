package a90pn.p2pc.ffi;

public enum FfiConverterOptionalTypeTunnelUpdate implements FfiConverterRustBuffer<TunnelUpdate> {
  INSTANCE;

  @Override
  public TunnelUpdate read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeTunnelUpdate.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(TunnelUpdate value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeTunnelUpdate.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(TunnelUpdate value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeTunnelUpdate.INSTANCE.write(value, buf);
    }
  }
}
