package a90pn.p2pc.ffi;


public enum FfiConverterTypeTokenRefreshPolicy implements FfiConverterRustBuffer<TokenRefreshPolicy> {
  INSTANCE;

  @Override
  public TokenRefreshPolicy read(java.nio.ByteBuffer buf) {
    return new TokenRefreshPolicy(
      FfiConverterTypeTokenRefreshKind.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(TokenRefreshPolicy value) {
      return (
            FfiConverterTypeTokenRefreshKind.INSTANCE.allocationSize(value.kind()) +
            FfiConverterLong.INSTANCE.allocationSize(value.periodMs())
      );
  }

  @Override
  public void write(TokenRefreshPolicy value, java.nio.ByteBuffer buf) {
      FfiConverterTypeTokenRefreshKind.INSTANCE.write(value.kind(), buf);
      FfiConverterLong.INSTANCE.write(value.periodMs(), buf);
  }
}
