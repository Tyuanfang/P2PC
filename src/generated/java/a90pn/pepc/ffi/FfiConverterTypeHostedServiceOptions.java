package a90pn.pepc.ffi;


public enum FfiConverterTypeHostedServiceOptions implements FfiConverterRustBuffer<HostedServiceOptions> {
  INSTANCE;

  @Override
  public HostedServiceOptions read(java.nio.ByteBuffer buf) {
    return new HostedServiceOptions(
      FfiConverterSequenceByte.INSTANCE.read(buf),
      FfiConverterString.INSTANCE.read(buf),
      FfiConverterOptionalTypeTokenState.INSTANCE.read(buf),
      FfiConverterTypeTokenRefreshPolicy.INSTANCE.read(buf),
      FfiConverterTypeHostConfig.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostedServiceOptions value) {
      return (
            FfiConverterSequenceByte.INSTANCE.allocationSize(value.serviceId()) +
            FfiConverterString.INSTANCE.allocationSize(value.targetAddr()) +
            FfiConverterOptionalTypeTokenState.INSTANCE.allocationSize(value.tokenState()) +
            FfiConverterTypeTokenRefreshPolicy.INSTANCE.allocationSize(value.tokenRefresh()) +
            FfiConverterTypeHostConfig.INSTANCE.allocationSize(value.config())
      );
  }

  @Override
  public void write(HostedServiceOptions value, java.nio.ByteBuffer buf) {
      FfiConverterSequenceByte.INSTANCE.write(value.serviceId(), buf);
      FfiConverterString.INSTANCE.write(value.targetAddr(), buf);
      FfiConverterOptionalTypeTokenState.INSTANCE.write(value.tokenState(), buf);
      FfiConverterTypeTokenRefreshPolicy.INSTANCE.write(value.tokenRefresh(), buf);
      FfiConverterTypeHostConfig.INSTANCE.write(value.config(), buf);
  }
}
