package a90pn.p2pc.ffi;


public enum FfiConverterTypeConnectionSnapshot implements FfiConverterRustBuffer<ConnectionSnapshot> {
  INSTANCE;

  @Override
  public ConnectionSnapshot read(java.nio.ByteBuffer buf) {
    return new ConnectionSnapshot(
      FfiConverterString.INSTANCE.read(buf),
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf),
      FfiConverterBoolean.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(ConnectionSnapshot value) {
      return (
            FfiConverterString.INSTANCE.allocationSize(value.remoteId()) +
            FfiConverterBoolean.INSTANCE.allocationSize(value.isRelay()) +
            FfiConverterLong.INSTANCE.allocationSize(value.rttMs()) +
            FfiConverterLong.INSTANCE.allocationSize(value.txBytes()) +
            FfiConverterLong.INSTANCE.allocationSize(value.rxBytes()) +
            FfiConverterBoolean.INSTANCE.allocationSize(value.alive()) +
            FfiConverterLong.INSTANCE.allocationSize(value.elapsedMs())
      );
  }

  @Override
  public void write(ConnectionSnapshot value, java.nio.ByteBuffer buf) {
      FfiConverterString.INSTANCE.write(value.remoteId(), buf);
      FfiConverterBoolean.INSTANCE.write(value.isRelay(), buf);
      FfiConverterLong.INSTANCE.write(value.rttMs(), buf);
      FfiConverterLong.INSTANCE.write(value.txBytes(), buf);
      FfiConverterLong.INSTANCE.write(value.rxBytes(), buf);
      FfiConverterBoolean.INSTANCE.write(value.alive(), buf);
      FfiConverterLong.INSTANCE.write(value.elapsedMs(), buf);
  }
}
