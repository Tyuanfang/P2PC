package a90pn.pepc.ffi;


public enum FfiConverterTypeProfile implements FfiConverterRustBuffer<Profile> {
  INSTANCE;

  @Override
  public Profile read(java.nio.ByteBuffer buf) {
    return new Profile(
      FfiConverterTypeHostProfile.INSTANCE.read(buf),
      FfiConverterTypeJoinProfile.INSTANCE.read(buf),
      FfiConverterTypeRelayProfile.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(Profile value) {
      return (
            FfiConverterTypeHostProfile.INSTANCE.allocationSize(value.host()) +
            FfiConverterTypeJoinProfile.INSTANCE.allocationSize(value.join()) +
            FfiConverterTypeRelayProfile.INSTANCE.allocationSize(value.relay())
      );
  }

  @Override
  public void write(Profile value, java.nio.ByteBuffer buf) {
      FfiConverterTypeHostProfile.INSTANCE.write(value.host(), buf);
      FfiConverterTypeJoinProfile.INSTANCE.write(value.join(), buf);
      FfiConverterTypeRelayProfile.INSTANCE.write(value.relay(), buf);
  }
}
