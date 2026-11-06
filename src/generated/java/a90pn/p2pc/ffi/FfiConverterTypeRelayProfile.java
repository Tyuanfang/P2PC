package a90pn.p2pc.ffi;


public enum FfiConverterTypeRelayProfile implements FfiConverterRustBuffer<RelayProfile> {
  INSTANCE;

  @Override
  public RelayProfile read(java.nio.ByteBuffer buf) {
    return new RelayProfile(
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(RelayProfile value) {
      return (
            FfiConverterBoolean.INSTANCE.allocationSize(value.custom()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.url())
      );
  }

  @Override
  public void write(RelayProfile value, java.nio.ByteBuffer buf) {
      FfiConverterBoolean.INSTANCE.write(value.custom(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.url(), buf);
  }
}
