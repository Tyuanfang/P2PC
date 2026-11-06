package a90pn.p2pc.ffi;


public enum FfiConverterTypeHostedServiceStatus implements FfiConverterRustBuffer<HostedServiceStatus> {
  INSTANCE;

  @Override
  public HostedServiceStatus read(java.nio.ByteBuffer buf) {
    return new HostedServiceStatus(
      FfiConverterSequenceByte.INSTANCE.read(buf),
      FfiConverterTypeHostedServicePhase.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterOptionalLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterOptionalTypeErrorCategory.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostedServiceStatus value) {
      return (
            FfiConverterSequenceByte.INSTANCE.allocationSize(value.serviceId()) +
            FfiConverterTypeHostedServicePhase.INSTANCE.allocationSize(value.phase()) +
            FfiConverterLong.INSTANCE.allocationSize(value.uriGeneration()) +
            FfiConverterLong.INSTANCE.allocationSize(value.tokenCreatedAtMs()) +
            FfiConverterOptionalLong.INSTANCE.allocationSize(value.nextRotationAtMs()) +
            FfiConverterLong.INSTANCE.allocationSize(value.connectionCount()) +
            FfiConverterLong.INSTANCE.allocationSize(value.bridgeCount()) +
            FfiConverterOptionalTypeErrorCategory.INSTANCE.allocationSize(value.lastError())
      );
  }

  @Override
  public void write(HostedServiceStatus value, java.nio.ByteBuffer buf) {
      FfiConverterSequenceByte.INSTANCE.write(value.serviceId(), buf);
      FfiConverterTypeHostedServicePhase.INSTANCE.write(value.phase(), buf);
      FfiConverterLong.INSTANCE.write(value.uriGeneration(), buf);
      FfiConverterLong.INSTANCE.write(value.tokenCreatedAtMs(), buf);
      FfiConverterOptionalLong.INSTANCE.write(value.nextRotationAtMs(), buf);
      FfiConverterLong.INSTANCE.write(value.connectionCount(), buf);
      FfiConverterLong.INSTANCE.write(value.bridgeCount(), buf);
      FfiConverterOptionalTypeErrorCategory.INSTANCE.write(value.lastError(), buf);
  }
}
