package a90pn.pepc.ffi;


public enum FfiConverterTypeTunnelStatus implements FfiConverterRustBuffer<TunnelStatus> {
  INSTANCE;

  @Override
  public TunnelStatus read(java.nio.ByteBuffer buf) {
    return new TunnelStatus(
      FfiConverterTypeTunnelState.INSTANCE.read(buf),
      FfiConverterSequenceTypeConnectionSnapshot.INSTANCE.read(buf),
      FfiConverterOptionalTypeErrorCategory.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TunnelStatus value) {
      return (
            FfiConverterTypeTunnelState.INSTANCE.allocationSize(value.state()) +
            FfiConverterSequenceTypeConnectionSnapshot.INSTANCE.allocationSize(value.connections()) +
            FfiConverterOptionalTypeErrorCategory.INSTANCE.allocationSize(value.lastError())
      );
  }

  @Override
  public void write(TunnelStatus value, java.nio.ByteBuffer buf) {
      FfiConverterTypeTunnelState.INSTANCE.write(value.state(), buf);
      FfiConverterSequenceTypeConnectionSnapshot.INSTANCE.write(value.connections(), buf);
      FfiConverterOptionalTypeErrorCategory.INSTANCE.write(value.lastError(), buf);
  }
}
