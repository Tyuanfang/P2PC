package a90pn.pepc.ffi;


public interface UniffiRustCallStatusErrorHandler<E extends java.lang.Exception> {
    E lift(java.lang.foreign.MemorySegment errorBuf);
}

