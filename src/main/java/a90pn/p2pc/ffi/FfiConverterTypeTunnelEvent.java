package a90pn.p2pc.ffi;


public enum FfiConverterTypeTunnelEvent implements FfiConverterRustBuffer<TunnelEvent> {
  INSTANCE;

  @Override
  public TunnelEvent read(java.nio.ByteBuffer buf) {
    return new TunnelEvent(
      FfiConverterTypeEventKind.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterInteger.INSTANCE.read(buf),
      FfiConverterOptionalLong.INSTANCE.read(buf),
      FfiConverterOptionalTypeErrorCategory.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TunnelEvent value) {
      return (
            FfiConverterTypeEventKind.INSTANCE.allocationSize(value.kind()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.playerId()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.reason()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.remoteId()) +
            FfiConverterBoolean.INSTANCE.allocationSize(value.isRelay()) +
            FfiConverterLong.INSTANCE.allocationSize(value.rttMs()) +
            FfiConverterInteger.INSTANCE.allocationSize(value.attempt()) +
            FfiConverterOptionalLong.INSTANCE.allocationSize(value.retryInMs()) +
            FfiConverterOptionalTypeErrorCategory.INSTANCE.allocationSize(value.category()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.message())
      );
  }

  @Override
  public void write(TunnelEvent value, java.nio.ByteBuffer buf) {
      FfiConverterTypeEventKind.INSTANCE.write(value.kind(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.playerId(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.reason(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.remoteId(), buf);
      FfiConverterBoolean.INSTANCE.write(value.isRelay(), buf);
      FfiConverterLong.INSTANCE.write(value.rttMs(), buf);
      FfiConverterInteger.INSTANCE.write(value.attempt(), buf);
      FfiConverterOptionalLong.INSTANCE.write(value.retryInMs(), buf);
      FfiConverterOptionalTypeErrorCategory.INSTANCE.write(value.category(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.message(), buf);
  }
}
