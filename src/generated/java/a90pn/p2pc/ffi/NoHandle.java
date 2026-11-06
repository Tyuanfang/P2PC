package a90pn.p2pc.ffi;


public class NoHandle {
    // Private constructor to prevent instantiation
    private NoHandle() {}

    // Static final instance of the class so it can be used in tests
    public static final NoHandle INSTANCE = new NoHandle();
}

