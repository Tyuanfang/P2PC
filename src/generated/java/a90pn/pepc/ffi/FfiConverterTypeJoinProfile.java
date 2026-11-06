package a90pn.pepc.ffi;


public enum FfiConverterTypeJoinProfile implements FfiConverterRustBuffer<JoinProfile> {
  INSTANCE;

  @Override
  public JoinProfile read(java.nio.ByteBuffer buf) {
    return new JoinProfile(
      FfiConverterShort.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(JoinProfile value) {
      return (
            FfiConverterShort.INSTANCE.allocationSize(value.port())
      );
  }

  @Override
  public void write(JoinProfile value, java.nio.ByteBuffer buf) {
      FfiConverterShort.INSTANCE.write(value.port(), buf);
  }
}
