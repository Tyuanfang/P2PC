package a90pn.pepc.ffi;


public enum FfiConverterTypeJoinOptions implements FfiConverterRustBuffer<JoinOptions> {
  INSTANCE;

  @Override
  public JoinOptions read(java.nio.ByteBuffer buf) {
    return new JoinOptions(
      FfiConverterString.INSTANCE.read(buf),
      FfiConverterShort.INSTANCE.read(buf),
      FfiConverterTypeJoinConfig.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(JoinOptions value) {
      return (
            FfiConverterString.INSTANCE.allocationSize(value.joinUri()) +
            FfiConverterShort.INSTANCE.allocationSize(value.localPort()) +
            FfiConverterTypeJoinConfig.INSTANCE.allocationSize(value.config())
      );
  }

  @Override
  public void write(JoinOptions value, java.nio.ByteBuffer buf) {
      FfiConverterString.INSTANCE.write(value.joinUri(), buf);
      FfiConverterShort.INSTANCE.write(value.localPort(), buf);
      FfiConverterTypeJoinConfig.INSTANCE.write(value.config(), buf);
  }
}
