package a90pn.pepc.ffi;


public enum FfiConverterTypeTunnelUpdate implements FfiConverterRustBuffer<TunnelUpdate> {
  INSTANCE;

  @Override
  public TunnelUpdate read(java.nio.ByteBuffer buf) {
    return new TunnelUpdate(
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterOptionalTypeTunnelStatus.INSTANCE.read(buf),
      FfiConverterOptionalTypeTunnelEvent.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TunnelUpdate value) {
      return (
            FfiConverterBoolean.INSTANCE.allocationSize(value.isEvent()) +
            FfiConverterOptionalTypeTunnelStatus.INSTANCE.allocationSize(value.status()) +
            FfiConverterOptionalTypeTunnelEvent.INSTANCE.allocationSize(value.event())
      );
  }

  @Override
  public void write(TunnelUpdate value, java.nio.ByteBuffer buf) {
      FfiConverterBoolean.INSTANCE.write(value.isEvent(), buf);
      FfiConverterOptionalTypeTunnelStatus.INSTANCE.write(value.status(), buf);
      FfiConverterOptionalTypeTunnelEvent.INSTANCE.write(value.event(), buf);
  }
}
