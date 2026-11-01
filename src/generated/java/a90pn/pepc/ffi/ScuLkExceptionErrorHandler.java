package a90pn.pepc.ffi;


public class ScuLkExceptionErrorHandler implements UniffiRustCallStatusErrorHandler<ScuLkException> {
  @Override
  public ScuLkException lift(java.lang.foreign.MemorySegment errorBuf){
     return FfiConverterTypeScuLkError.INSTANCE.lift(errorBuf);
  }
}

