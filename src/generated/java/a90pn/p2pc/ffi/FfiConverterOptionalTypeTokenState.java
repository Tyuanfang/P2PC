package a90pn.p2pc.ffi;

public enum FfiConverterOptionalTypeTokenState implements FfiConverterRustBuffer<TokenState> {
  INSTANCE;

  @Override
  public TokenState read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeTokenState.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(TokenState value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeTokenState.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(TokenState value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeTokenState.INSTANCE.write(value, buf);
    }
  }
}
