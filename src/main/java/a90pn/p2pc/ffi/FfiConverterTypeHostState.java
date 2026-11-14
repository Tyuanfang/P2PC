package a90pn.p2pc.ffi;


public enum FfiConverterTypeHostState implements FfiConverterRustBuffer<HostState> {
  INSTANCE;

  @Override
  public HostState read(java.nio.ByteBuffer buf) {
    return new HostState(
      FfiConverterSequenceByte.INSTANCE.read(buf),
      FfiConverterTypeTokenState.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(HostState value) {
      return (
            FfiConverterSequenceByte.INSTANCE.allocationSize(value.serviceId()) +
            FfiConverterTypeTokenState.INSTANCE.allocationSize(value.tokenState())
      );
  }

  @Override
  public void write(HostState value, java.nio.ByteBuffer buf) {
      FfiConverterSequenceByte.INSTANCE.write(value.serviceId(), buf);
      FfiConverterTypeTokenState.INSTANCE.write(value.tokenState(), buf);
  }
}
