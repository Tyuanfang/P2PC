package a90pn.pepc.ffi;

public enum FfiConverterOptionalTypeTunnelStatus implements FfiConverterRustBuffer<TunnelStatus> {
  INSTANCE;

  @Override
  public TunnelStatus read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeTunnelStatus.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(TunnelStatus value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeTunnelStatus.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(TunnelStatus value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeTunnelStatus.INSTANCE.write(value, buf);
    }
  }
}
