package a90pn.pepc.ffi;


public enum FfiConverterTypeJoinConfig implements FfiConverterRustBuffer<JoinConfig> {
  INSTANCE;

  @Override
  public JoinConfig read(java.nio.ByteBuffer buf) {
    return new JoinConfig(
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterOptionalLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(JoinConfig value) {
      return (
            FfiConverterLong.INSTANCE.allocationSize(value.eventDelayMs()) +
            FfiConverterOptionalLong.INSTANCE.allocationSize(value.reconnectTimeoutMs()) +
            FfiConverterLong.INSTANCE.allocationSize(value.localSessionsMax())
      );
  }

  @Override
  public void write(JoinConfig value, java.nio.ByteBuffer buf) {
      FfiConverterLong.INSTANCE.write(value.eventDelayMs(), buf);
      FfiConverterOptionalLong.INSTANCE.write(value.reconnectTimeoutMs(), buf);
      FfiConverterLong.INSTANCE.write(value.localSessionsMax(), buf);
  }
}
