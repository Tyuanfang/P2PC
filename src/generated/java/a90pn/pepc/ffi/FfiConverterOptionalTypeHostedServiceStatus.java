package a90pn.pepc.ffi;

public enum FfiConverterOptionalTypeHostedServiceStatus implements FfiConverterRustBuffer<HostedServiceStatus> {
  INSTANCE;

  @Override
  public HostedServiceStatus read(java.nio.ByteBuffer buf) {
    if (buf.get() == (byte)0) {
      return null;
    }
    return FfiConverterTypeHostedServiceStatus.INSTANCE.read(buf);
  }

  @Override
  public long allocationSize(HostedServiceStatus value) {
    if (value == null) {
      return 1L;
    } else {
      return 1L + FfiConverterTypeHostedServiceStatus.INSTANCE.allocationSize(value);
    }
  }

  @Override
  public void write(HostedServiceStatus value, java.nio.ByteBuffer buf) {
    if (value == null) {
      buf.put((byte)0);
    } else {
      buf.put((byte)1);
      FfiConverterTypeHostedServiceStatus.INSTANCE.write(value, buf);
    }
  }
}
