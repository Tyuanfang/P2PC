package a90pn.pepc.ffi;


public enum FfiConverterTypeTunnelState implements FfiConverterRustBuffer<TunnelState> {
  INSTANCE;

  @Override
  public TunnelState read(java.nio.ByteBuffer buf) {
    return new TunnelState(
      FfiConverterTypeTunnelPhase.INSTANCE.read(buf),
      FfiConverterOptionalTypeTunnelMode.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TunnelState value) {
      return (
            FfiConverterTypeTunnelPhase.INSTANCE.allocationSize(value.phase()) +
            FfiConverterOptionalTypeTunnelMode.INSTANCE.allocationSize(value.mode()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.joinUri()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.localAddr())
      );
  }

  @Override
  public void write(TunnelState value, java.nio.ByteBuffer buf) {
      FfiConverterTypeTunnelPhase.INSTANCE.write(value.phase(), buf);
      FfiConverterOptionalTypeTunnelMode.INSTANCE.write(value.mode(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.joinUri(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.localAddr(), buf);
  }
}
