package a90pn.p2pc.ffi;


public enum FfiConverterTypeHostOptions implements FfiConverterRustBuffer<HostOptions> {
  INSTANCE;

  @Override
  public HostOptions read(java.nio.ByteBuffer buf) {
    return new HostOptions(
      FfiConverterShort.INSTANCE.read(buf),
      FfiConverterOptionalSequenceByte.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterOptionalSequenceByte.INSTANCE.read(buf),
      FfiConverterOptionalSequenceByte.INSTANCE.read(buf),
      FfiConverterTypeHostConfig.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostOptions value) {
      return (
            FfiConverterShort.INSTANCE.allocationSize(value.mcPort()) +
            FfiConverterOptionalSequenceByte.INSTANCE.allocationSize(value.secretKey()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.relayUrl()) +
            FfiConverterOptionalSequenceByte.INSTANCE.allocationSize(value.serviceId()) +
            FfiConverterOptionalSequenceByte.INSTANCE.allocationSize(value.token()) +
            FfiConverterTypeHostConfig.INSTANCE.allocationSize(value.config())
      );
  }

  @Override
  public void write(HostOptions value, java.nio.ByteBuffer buf) {
      FfiConverterShort.INSTANCE.write(value.mcPort(), buf);
      FfiConverterOptionalSequenceByte.INSTANCE.write(value.secretKey(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.relayUrl(), buf);
      FfiConverterOptionalSequenceByte.INSTANCE.write(value.serviceId(), buf);
      FfiConverterOptionalSequenceByte.INSTANCE.write(value.token(), buf);
      FfiConverterTypeHostConfig.INSTANCE.write(value.config(), buf);
  }
}
