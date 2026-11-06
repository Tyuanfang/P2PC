package a90pn.p2pc.ffi;


public interface UniffiRustCallStatusErrorHandler<E extends java.lang.Exception> {
    E lift(java.lang.foreign.MemorySegment errorBuf);
}

