package a90pn.p2pc.ffi;


public class ScuLkExceptionErrorHandler implements UniffiRustCallStatusErrorHandler<ScuLkException> {
  @Override
  public ScuLkException lift(java.lang.foreign.MemorySegment errorBuf){
     return FfiConverterTypeScuLkError.INSTANCE.lift(errorBuf);
  }
}

