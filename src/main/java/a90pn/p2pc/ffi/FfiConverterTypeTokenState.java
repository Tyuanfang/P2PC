package a90pn.p2pc.ffi;


public enum FfiConverterTypeTokenState implements FfiConverterRustBuffer<TokenState> {
  INSTANCE;

  @Override
  public TokenState read(java.nio.ByteBuffer buf) {
    return new TokenState(
      FfiConverterSequenceByte.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TokenState value) {
      return (
            FfiConverterSequenceByte.INSTANCE.allocationSize(value.token()) +
            FfiConverterLong.INSTANCE.allocationSize(value.createdAtMs())
      );
  }

  @Override
  public void write(TokenState value, java.nio.ByteBuffer buf) {
      FfiConverterSequenceByte.INSTANCE.write(value.token(), buf);
      FfiConverterLong.INSTANCE.write(value.createdAtMs(), buf);
  }
}
