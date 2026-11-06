package a90pn.pepc.ffi;


public enum FfiConverterTypeSculkNodeStatus implements FfiConverterRustBuffer<SculkNodeStatus> {
  INSTANCE;

  @Override
  public SculkNodeStatus read(java.nio.ByteBuffer buf) {
    return new SculkNodeStatus(
      FfiConverterString.INSTANCE.read(buf),
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(SculkNodeStatus value) {
      return (
            FfiConverterString.INSTANCE.allocationSize(value.endpointId()) +
            FfiConverterBoolean.INSTANCE.allocationSize(value.online()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.relayUrl()) +
            FfiConverterLong.INSTANCE.allocationSize(value.serviceCount()) +
            FfiConverterLong.INSTANCE.allocationSize(value.unauthenticatedConnectionCount()) +
            FfiConverterLong.INSTANCE.allocationSize(value.unauthenticatedConnectionsMax())
      );
  }

  @Override
  public void write(SculkNodeStatus value, java.nio.ByteBuffer buf) {
      FfiConverterString.INSTANCE.write(value.endpointId(), buf);
      FfiConverterBoolean.INSTANCE.write(value.online(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.relayUrl(), buf);
      FfiConverterLong.INSTANCE.write(value.serviceCount(), buf);
      FfiConverterLong.INSTANCE.write(value.unauthenticatedConnectionCount(), buf);
      FfiConverterLong.INSTANCE.write(value.unauthenticatedConnectionsMax(), buf);
  }
}
