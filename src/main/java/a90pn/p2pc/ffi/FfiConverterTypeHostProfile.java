package a90pn.p2pc.ffi;


public enum FfiConverterTypeHostProfile implements FfiConverterRustBuffer<HostProfile> {
  INSTANCE;

  @Override
  public HostProfile read(java.nio.ByteBuffer buf) {
    return new HostProfile(
      FfiConverterShort.INSTANCE.read(buf),
      FfiConverterTypeTokenRefreshSetting.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostProfile value) {
      return (
            FfiConverterShort.INSTANCE.allocationSize(value.port()) +
            FfiConverterTypeTokenRefreshSetting.INSTANCE.allocationSize(value.tokenRefresh())
      );
  }

  @Override
  public void write(HostProfile value, java.nio.ByteBuffer buf) {
      FfiConverterShort.INSTANCE.write(value.port(), buf);
      FfiConverterTypeTokenRefreshSetting.INSTANCE.write(value.tokenRefresh(), buf);
  }
}
