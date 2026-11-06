package a90pn.p2pc.ffi;


public enum FfiConverterTypeNodeOptions implements FfiConverterRustBuffer<NodeOptions> {
  INSTANCE;

  @Override
  public NodeOptions read(java.nio.ByteBuffer buf) {
    return new NodeOptions(
      FfiConverterOptionalSequenceByte.INSTANCE.read(buf),
      FfiConverterOptionalString.INSTANCE.read(buf),
      FfiConverterLong.INSTANCE.read(buf)
    );
  }

  @Override
  public long allocationSize(NodeOptions value) {
      return (
            FfiConverterOptionalSequenceByte.INSTANCE.allocationSize(value.secretKey()) +
            FfiConverterOptionalString.INSTANCE.allocationSize(value.relayUrl()) +
            FfiConverterLong.INSTANCE.allocationSize(value.unauthenticatedConnectionsMax())
      );
  }

  @Override
  public void write(NodeOptions value, java.nio.ByteBuffer buf) {
      FfiConverterOptionalSequenceByte.INSTANCE.write(value.secretKey(), buf);
      FfiConverterOptionalString.INSTANCE.write(value.relayUrl(), buf);
      FfiConverterLong.INSTANCE.write(value.unauthenticatedConnectionsMax(), buf);
  }
}
