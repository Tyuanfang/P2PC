package a90pn.pepc.ffi;


public enum FfiConverterSequenceTypeConnectionSnapshot implements FfiConverterRustBuffer<java.util.List<ConnectionSnapshot>> {
  INSTANCE;

  @Override
  public java.util.List<ConnectionSnapshot> read(java.nio.ByteBuffer buf) {
    int len = buf.getInt();
    return java.util.stream.IntStream.range(0, len).mapToObj(_i -> FfiConverterTypeConnectionSnapshot.INSTANCE.read(buf)).toList();
  }

  @Override
  public long allocationSize(java.util.List<ConnectionSnapshot> value) {
    long sizeForLength = 4L;
    long sizeForItems = value.stream().mapToLong(inner -> FfiConverterTypeConnectionSnapshot.INSTANCE.allocationSize(inner)).sum();
    return sizeForLength + sizeForItems;
  }

  @Override
  public void write(java.util.List<ConnectionSnapshot> value, java.nio.ByteBuffer buf) {
    buf.putInt(value.size());
    value.forEach(inner -> FfiConverterTypeConnectionSnapshot.INSTANCE.write(inner, buf));
  }
}


