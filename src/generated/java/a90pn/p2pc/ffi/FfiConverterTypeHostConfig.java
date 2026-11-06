package a90pn.p2pc.ffi;


public enum FfiConverterTypeHostConfig implements FfiConverterRustBuffer<HostConfig> {
  INSTANCE;

  @Override
  public HostConfig read(java.nio.ByteBuffer buf) {
    return new HostConfig(
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterOptionalInteger.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostConfig value) {
      return (
            FfiConverterLong.INSTANCE.allocationSize(value.eventDelayMs()) +
            FfiConverterOptionalInteger.INSTANCE.allocationSize(value.maxPlayers())
      );
  }

  @Override
  public void write(HostConfig value, java.nio.ByteBuffer buf) {
      FfiConverterLong.INSTANCE.write(value.eventDelayMs(), buf);
      FfiConverterOptionalInteger.INSTANCE.write(value.maxPlayers(), buf);
  }
}
