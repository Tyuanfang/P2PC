package a90pn.pepc.ffi;


// FFM-based library binding. Each FFI function gets a MethodHandle and a wrapper method.
final class UniffiLib {
    private static final java.lang.foreign.Linker LINKER = java.lang.foreign.Linker.nativeLinker();
    private static final java.lang.foreign.SymbolLookup SYMBOLS;

    
    // The Cleaner for the whole library
    static UniffiCleaner CLEANER;

    static {
        SYMBOLS = NamespaceLibrary.loadLibrary();
    }

    private static java.lang.invoke.MethodHandle findDowncallHandle(String name, java.lang.foreign.FunctionDescriptor descriptor) {
        return SYMBOLS.find(name)
            .map(s -> LINKER.downcallHandle(s, descriptor))
            .orElseThrow(() -> new RuntimeException("Missing FFI symbol: " + name));
    }

    // uniffi_sculk_ffi_fn_clone_accesstoken
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_accesstoken = findDowncallHandle("uniffi_sculk_ffi_fn_clone_accesstoken", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_accesstoken(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_accesstoken.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_accesstoken
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_accesstoken = findDowncallHandle("uniffi_sculk_ffi_fn_free_accesstoken", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_accesstoken(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_accesstoken.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_accesstoken_matches
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_accesstoken_matches = findDowncallHandle("uniffi_sculk_ffi_fn_method_accesstoken_matches", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_BYTE, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static byte uniffi_sculk_ffi_fn_method_accesstoken_matches(long ptr, java.lang.foreign.MemorySegment other, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (byte) MH_uniffi_sculk_ffi_fn_method_accesstoken_matches.invokeExact(ptr, other, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_accesstoken_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_accesstoken_to_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_method_accesstoken_to_bytes", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_accesstoken_to_bytes(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_accesstoken_to_bytes.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_hostedserviceeventreader
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_hostedserviceeventreader = findDowncallHandle("uniffi_sculk_ffi_fn_clone_hostedserviceeventreader", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_hostedserviceeventreader(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_hostedserviceeventreader.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_hostedserviceeventreader
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_hostedserviceeventreader = findDowncallHandle("uniffi_sculk_ffi_fn_free_hostedserviceeventreader", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_hostedserviceeventreader(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_hostedserviceeventreader.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedserviceeventreader_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedserviceeventreader_recv = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedserviceeventreader_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedserviceeventreader_recv(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedserviceeventreader_recv.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_hostedservicehandle
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_hostedservicehandle = findDowncallHandle("uniffi_sculk_ffi_fn_clone_hostedservicehandle", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_hostedservicehandle(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_hostedservicehandle.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_hostedservicehandle
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_hostedservicehandle = findDowncallHandle("uniffi_sculk_ffi_fn_free_hostedservicehandle", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_hostedservicehandle(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_hostedservicehandle.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_status = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_status(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_status.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_stop = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_stop(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_stop.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_hostedservicestatussubscription
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_hostedservicestatussubscription = findDowncallHandle("uniffi_sculk_ffi_fn_clone_hostedservicestatussubscription", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_hostedservicestatussubscription(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_hostedservicestatussubscription.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_hostedservicestatussubscription
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_hostedservicestatussubscription = findDowncallHandle("uniffi_sculk_ffi_fn_free_hostedservicestatussubscription", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_hostedservicestatussubscription(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_hostedservicestatussubscription.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_hostedservicestatussubscription_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_hostedservicestatussubscription_recv = findDowncallHandle("uniffi_sculk_ffi_fn_method_hostedservicestatussubscription_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_hostedservicestatussubscription_recv(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_hostedservicestatussubscription_recv.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_joinuri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_joinuri = findDowncallHandle("uniffi_sculk_ffi_fn_clone_joinuri", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_joinuri(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_joinuri.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_joinuri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_joinuri = findDowncallHandle("uniffi_sculk_ffi_fn_free_joinuri", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_joinuri(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_joinuri.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_joinuri_expose_secret_uri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_joinuri_expose_secret_uri = findDowncallHandle("uniffi_sculk_ffi_fn_method_joinuri_expose_secret_uri", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_joinuri_expose_secret_uri(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_joinuri_expose_secret_uri.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_joinuri_redacted
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_joinuri_redacted = findDowncallHandle("uniffi_sculk_ffi_fn_method_joinuri_redacted", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_joinuri_redacted(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_joinuri_redacted.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_joinuri_relay_url
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_joinuri_relay_url = findDowncallHandle("uniffi_sculk_ffi_fn_method_joinuri_relay_url", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_joinuri_relay_url(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_joinuri_relay_url.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_lanbroadcaster
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_lanbroadcaster = findDowncallHandle("uniffi_sculk_ffi_fn_clone_lanbroadcaster", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_lanbroadcaster(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_lanbroadcaster.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_lanbroadcaster
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_lanbroadcaster = findDowncallHandle("uniffi_sculk_ffi_fn_free_lanbroadcaster", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_lanbroadcaster(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_lanbroadcaster.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_lanbroadcaster_is_finished
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_lanbroadcaster_is_finished = findDowncallHandle("uniffi_sculk_ffi_fn_method_lanbroadcaster_is_finished", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_BYTE, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static byte uniffi_sculk_ffi_fn_method_lanbroadcaster_is_finished(long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (byte) MH_uniffi_sculk_ffi_fn_method_lanbroadcaster_is_finished.invokeExact(ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_lanbroadcaster_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_lanbroadcaster_stop = findDowncallHandle("uniffi_sculk_ffi_fn_method_lanbroadcaster_stop", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_method_lanbroadcaster_stop(long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_method_lanbroadcaster_stop.invokeExact(ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_lanscanner
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_lanscanner = findDowncallHandle("uniffi_sculk_ffi_fn_clone_lanscanner", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_lanscanner(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_lanscanner.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_lanscanner
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_lanscanner = findDowncallHandle("uniffi_sculk_ffi_fn_free_lanscanner", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_lanscanner(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_lanscanner.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_lanscanner_is_finished
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_lanscanner_is_finished = findDowncallHandle("uniffi_sculk_ffi_fn_method_lanscanner_is_finished", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_BYTE, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static byte uniffi_sculk_ffi_fn_method_lanscanner_is_finished(long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (byte) MH_uniffi_sculk_ffi_fn_method_lanscanner_is_finished.invokeExact(ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_lanscanner_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_lanscanner_stop = findDowncallHandle("uniffi_sculk_ffi_fn_method_lanscanner_stop", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_method_lanscanner_stop(long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_method_lanscanner_stop.invokeExact(ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_lanscanner_try_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_lanscanner_try_recv = findDowncallHandle("uniffi_sculk_ffi_fn_method_lanscanner_try_recv", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_lanscanner_try_recv(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_lanscanner_try_recv.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_sculknode
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_sculknode = findDowncallHandle("uniffi_sculk_ffi_fn_clone_sculknode", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_sculknode(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_sculknode.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_sculknode
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_sculknode = findDowncallHandle("uniffi_sculk_ffi_fn_free_sculknode", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_sculknode(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_sculknode.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_endpoint_id
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_endpoint_id = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_endpoint_id", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_sculknode_endpoint_id(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_sculknode_endpoint_id.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_rotate_token
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_rotate_token = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_rotate_token", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_method_sculknode_rotate_token(long ptr, java.lang.foreign.MemorySegment serviceId) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_rotate_token.invokeExact(ptr, serviceId);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_service_count
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_service_count = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_service_count", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_sculknode_service_count(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_service_count.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_shutdown
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_shutdown = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_shutdown", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_sculknode_shutdown(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_shutdown.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_start_service
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_start_service = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_start_service", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_method_sculknode_start_service(long ptr, java.lang.foreign.MemorySegment options) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_start_service.invokeExact(ptr, options);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_status = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_sculknode_status(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_status.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_sculknode_stop_service
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_sculknode_stop_service = findDowncallHandle("uniffi_sculk_ffi_fn_method_sculknode_stop_service", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_method_sculknode_stop_service(long ptr, java.lang.foreign.MemorySegment serviceId) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_sculknode_stop_service.invokeExact(ptr, serviceId);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_secretkey
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_secretkey = findDowncallHandle("uniffi_sculk_ffi_fn_clone_secretkey", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_secretkey(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_secretkey.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_secretkey
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_secretkey = findDowncallHandle("uniffi_sculk_ffi_fn_free_secretkey", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_secretkey(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_secretkey.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_secretkey_public_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_secretkey_public_key = findDowncallHandle("uniffi_sculk_ffi_fn_method_secretkey_public_key", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_secretkey_public_key(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_secretkey_public_key.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_secretkey_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_secretkey_to_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_method_secretkey_to_bytes", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_secretkey_to_bytes(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_secretkey_to_bytes.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_serviceid
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_serviceid = findDowncallHandle("uniffi_sculk_ffi_fn_clone_serviceid", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_serviceid(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_serviceid.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_serviceid
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_serviceid = findDowncallHandle("uniffi_sculk_ffi_fn_free_serviceid", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_serviceid(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_serviceid.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_serviceid_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_serviceid_to_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_method_serviceid_to_bytes", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_serviceid_to_bytes(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_serviceid_to_bytes.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_subscription
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_subscription = findDowncallHandle("uniffi_sculk_ffi_fn_clone_subscription", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_subscription(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_subscription.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_subscription
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_subscription = findDowncallHandle("uniffi_sculk_ffi_fn_free_subscription", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_subscription(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_subscription.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_subscription_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_subscription_recv = findDowncallHandle("uniffi_sculk_ffi_fn_method_subscription_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_subscription_recv(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_subscription_recv.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_clone_tunnelservice
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_clone_tunnelservice = findDowncallHandle("uniffi_sculk_ffi_fn_clone_tunnelservice", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_clone_tunnelservice(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_clone_tunnelservice.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_free_tunnelservice
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_free_tunnelservice = findDowncallHandle("uniffi_sculk_ffi_fn_free_tunnelservice", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_free_tunnelservice(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_free_tunnelservice.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_constructor_tunnelservice_new
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_constructor_tunnelservice_new = findDowncallHandle("uniffi_sculk_ffi_fn_constructor_tunnelservice_new", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_constructor_tunnelservice_new(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_constructor_tunnelservice_new.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_shutdown
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_shutdown = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_shutdown", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_tunnelservice_shutdown(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_tunnelservice_shutdown.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_start_host
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_start_host = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_start_host", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_method_tunnelservice_start_host(long ptr, java.lang.foreign.MemorySegment options) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_tunnelservice_start_host.invokeExact(ptr, options);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_start_join
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_start_join = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_start_join", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_method_tunnelservice_start_join(long ptr, java.lang.foreign.MemorySegment options) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_tunnelservice_start_join.invokeExact(ptr, options);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_status = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_status", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_method_tunnelservice_status(java.lang.foreign.SegmentAllocator _allocator, long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_method_tunnelservice_status.invokeExact(_allocator, ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_stop = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG));

    static long uniffi_sculk_ffi_fn_method_tunnelservice_stop(long ptr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_tunnelservice_stop.invokeExact(ptr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_method_tunnelservice_subscribe
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_method_tunnelservice_subscribe = findDowncallHandle("uniffi_sculk_ffi_fn_method_tunnelservice_subscribe", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_method_tunnelservice_subscribe(long ptr, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_method_tunnelservice_subscribe.invokeExact(ptr, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_access_token_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_access_token_from_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_func_access_token_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_access_token_from_bytes(java.lang.foreign.MemorySegment bytes, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_access_token_from_bytes.invokeExact(bytes, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_access_token_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_access_token_generate = findDowncallHandle("uniffi_sculk_ffi_fn_func_access_token_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_access_token_generate(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_access_token_generate.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_default_inlet_port
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_default_inlet_port = findDowncallHandle("uniffi_sculk_ffi_fn_func_default_inlet_port", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT, java.lang.foreign.ValueLayout.ADDRESS));

    static short uniffi_sculk_ffi_fn_func_default_inlet_port(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (short) MH_uniffi_sculk_ffi_fn_func_default_inlet_port.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_default_mc_port
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_default_mc_port = findDowncallHandle("uniffi_sculk_ffi_fn_func_default_mc_port", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT, java.lang.foreign.ValueLayout.ADDRESS));

    static short uniffi_sculk_ffi_fn_func_default_mc_port(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (short) MH_uniffi_sculk_ffi_fn_func_default_mc_port.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_join_uri_parse
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_join_uri_parse = findDowncallHandle("uniffi_sculk_ffi_fn_func_join_uri_parse", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_join_uri_parse(java.lang.foreign.MemorySegment uri, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_join_uri_parse.invokeExact(uri, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_lan_broadcaster_start
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_lan_broadcaster_start = findDowncallHandle("uniffi_sculk_ffi_fn_func_lan_broadcaster_start", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_SHORT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_lan_broadcaster_start(java.lang.foreign.MemorySegment name, short port, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_lan_broadcaster_start.invokeExact(name, port, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_lan_scanner_start
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_lan_scanner_start = findDowncallHandle("uniffi_sculk_ffi_fn_func_lan_scanner_start", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_lan_scanner_start(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_lan_scanner_start.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_data_dir
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_data_dir = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_data_dir", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_data_dir(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_data_dir.invokeExact(_allocator, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_default_host_state_path
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_default_host_state_path = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_default_host_state_path", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_default_host_state_path(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_default_host_state_path.invokeExact(_allocator, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_default_key_path
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_default_key_path = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_default_key_path", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_default_key_path(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_default_key_path.invokeExact(_allocator, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_generate_new_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_generate_new_key = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_generate_new_key", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_persist_generate_new_key(java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_persist_generate_new_key.invokeExact(path, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_load_host_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_load_host_state = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_load_host_state", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_load_host_state(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_load_host_state.invokeExact(_allocator, path, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_load_or_generate_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_load_or_generate_key = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_load_or_generate_key", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_persist_load_or_generate_key(java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_persist_load_or_generate_key.invokeExact(path, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_load_profile
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_load_profile = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_load_profile", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_load_profile(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_load_profile.invokeExact(_allocator, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_load_profile_from
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_load_profile_from = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_load_profile_from", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment uniffi_sculk_ffi_fn_func_persist_load_profile_from(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_uniffi_sculk_ffi_fn_func_persist_load_profile_from.invokeExact(_allocator, path, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_save_host_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_save_host_state = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_save_host_state", java.lang.foreign.FunctionDescriptor.ofVoid(RustBuffer.LAYOUT, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_func_persist_save_host_state(java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment state, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_func_persist_save_host_state.invokeExact(path, state, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_save_profile
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_save_profile = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_save_profile", java.lang.foreign.FunctionDescriptor.ofVoid(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_func_persist_save_profile(java.lang.foreign.MemorySegment profile, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_func_persist_save_profile.invokeExact(profile, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_persist_save_profile_to
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_persist_save_profile_to = findDowncallHandle("uniffi_sculk_ffi_fn_func_persist_save_profile_to", java.lang.foreign.FunctionDescriptor.ofVoid(RustBuffer.LAYOUT, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_func_persist_save_profile_to(java.lang.foreign.MemorySegment path, java.lang.foreign.MemorySegment profile, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_func_persist_save_profile_to.invokeExact(path, profile, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_probe_server
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_probe_server = findDowncallHandle("uniffi_sculk_ffi_fn_func_probe_server", java.lang.foreign.FunctionDescriptor.ofVoid(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void uniffi_sculk_ffi_fn_func_probe_server(java.lang.foreign.MemorySegment addr, long timeoutMs, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_uniffi_sculk_ffi_fn_func_probe_server.invokeExact(addr, timeoutMs, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_sculpt_node_bind
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_sculpt_node_bind = findDowncallHandle("uniffi_sculk_ffi_fn_func_sculpt_node_bind", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT));

    static long uniffi_sculk_ffi_fn_func_sculpt_node_bind(java.lang.foreign.MemorySegment options) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_sculpt_node_bind.invokeExact(options);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_secret_key_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_secret_key_from_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_func_secret_key_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_secret_key_from_bytes(java.lang.foreign.MemorySegment bytes, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_secret_key_from_bytes.invokeExact(bytes, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_secret_key_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_secret_key_generate = findDowncallHandle("uniffi_sculk_ffi_fn_func_secret_key_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_secret_key_generate(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_secret_key_generate.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_service_id_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_service_id_from_bytes = findDowncallHandle("uniffi_sculk_ffi_fn_func_service_id_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_service_id_from_bytes(java.lang.foreign.MemorySegment bytes, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_service_id_from_bytes.invokeExact(bytes, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_fn_func_service_id_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_fn_func_service_id_generate = findDowncallHandle("uniffi_sculk_ffi_fn_func_service_id_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long uniffi_sculk_ffi_fn_func_service_id_generate(java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_uniffi_sculk_ffi_fn_func_service_id_generate.invokeExact(_uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rustbuffer_alloc
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rustbuffer_alloc = findDowncallHandle("ffi_sculk_ffi_rustbuffer_alloc", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment ffi_sculk_ffi_rustbuffer_alloc(java.lang.foreign.SegmentAllocator _allocator, long size, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_ffi_sculk_ffi_rustbuffer_alloc.invokeExact(_allocator, size, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rustbuffer_from_bytes
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rustbuffer_from_bytes = findDowncallHandle("ffi_sculk_ffi_rustbuffer_from_bytes", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, ForeignBytes.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment ffi_sculk_ffi_rustbuffer_from_bytes(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment bytes, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_ffi_sculk_ffi_rustbuffer_from_bytes.invokeExact(_allocator, bytes, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rustbuffer_free
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rustbuffer_free = findDowncallHandle("ffi_sculk_ffi_rustbuffer_free", java.lang.foreign.FunctionDescriptor.ofVoid(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.ADDRESS));

    static void ffi_sculk_ffi_rustbuffer_free(java.lang.foreign.MemorySegment buf, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_ffi_sculk_ffi_rustbuffer_free.invokeExact(buf, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rustbuffer_reserve
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rustbuffer_reserve = findDowncallHandle("ffi_sculk_ffi_rustbuffer_reserve", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment ffi_sculk_ffi_rustbuffer_reserve(java.lang.foreign.SegmentAllocator _allocator, java.lang.foreign.MemorySegment buf, long additional, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_ffi_sculk_ffi_rustbuffer_reserve.invokeExact(_allocator, buf, additional, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_u8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_u8 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_u8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_u8(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_u8.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_u8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_u8 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_u8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_u8(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_u8.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_u8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_u8 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_u8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_u8(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_u8.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_u8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_u8 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_u8", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_BYTE, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static byte ffi_sculk_ffi_rust_future_complete_u8(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (byte) MH_ffi_sculk_ffi_rust_future_complete_u8.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_i8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_i8 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_i8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_i8(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_i8.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_i8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_i8 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_i8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_i8(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_i8.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_i8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_i8 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_i8", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_i8(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_i8.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_i8
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_i8 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_i8", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_BYTE, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static byte ffi_sculk_ffi_rust_future_complete_i8(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (byte) MH_ffi_sculk_ffi_rust_future_complete_i8.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_u16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_u16 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_u16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_u16(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_u16.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_u16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_u16 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_u16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_u16(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_u16.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_u16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_u16 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_u16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_u16(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_u16.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_u16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_u16 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_u16", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static short ffi_sculk_ffi_rust_future_complete_u16(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (short) MH_ffi_sculk_ffi_rust_future_complete_u16.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_i16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_i16 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_i16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_i16(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_i16.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_i16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_i16 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_i16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_i16(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_i16.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_i16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_i16 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_i16", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_i16(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_i16.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_i16
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_i16 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_i16", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static short ffi_sculk_ffi_rust_future_complete_i16(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (short) MH_ffi_sculk_ffi_rust_future_complete_i16.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_u32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_u32 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_u32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_u32(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_u32.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_u32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_u32 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_u32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_u32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_u32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_u32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_u32 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_u32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_u32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_u32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_u32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_u32 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_u32", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_INT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static int ffi_sculk_ffi_rust_future_complete_u32(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (int) MH_ffi_sculk_ffi_rust_future_complete_u32.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_i32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_i32 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_i32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_i32(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_i32.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_i32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_i32 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_i32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_i32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_i32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_i32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_i32 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_i32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_i32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_i32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_i32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_i32 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_i32", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_INT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static int ffi_sculk_ffi_rust_future_complete_i32(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (int) MH_ffi_sculk_ffi_rust_future_complete_i32.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_u64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_u64 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_u64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_u64(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_u64.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_u64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_u64 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_u64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_u64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_u64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_u64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_u64 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_u64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_u64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_u64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_u64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_u64 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_u64", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long ffi_sculk_ffi_rust_future_complete_u64(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_ffi_sculk_ffi_rust_future_complete_u64.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_i64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_i64 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_i64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_i64(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_i64.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_i64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_i64 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_i64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_i64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_i64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_i64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_i64 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_i64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_i64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_i64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_i64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_i64 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_i64", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static long ffi_sculk_ffi_rust_future_complete_i64(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (long) MH_ffi_sculk_ffi_rust_future_complete_i64.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_f32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_f32 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_f32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_f32(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_f32.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_f32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_f32 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_f32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_f32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_f32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_f32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_f32 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_f32", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_f32(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_f32.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_f32
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_f32 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_f32", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_FLOAT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static float ffi_sculk_ffi_rust_future_complete_f32(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (float) MH_ffi_sculk_ffi_rust_future_complete_f32.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_f64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_f64 = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_f64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_f64(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_f64.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_f64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_f64 = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_f64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_f64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_f64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_f64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_f64 = findDowncallHandle("ffi_sculk_ffi_rust_future_free_f64", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_f64(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_f64.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_f64
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_f64 = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_f64", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_DOUBLE, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static double ffi_sculk_ffi_rust_future_complete_f64(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (double) MH_ffi_sculk_ffi_rust_future_complete_f64.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_rust_buffer
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_rust_buffer = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_rust_buffer", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_rust_buffer(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_rust_buffer.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_rust_buffer
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_rust_buffer = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_rust_buffer", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_rust_buffer(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_rust_buffer.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_rust_buffer
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_rust_buffer = findDowncallHandle("ffi_sculk_ffi_rust_future_free_rust_buffer", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_rust_buffer(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_rust_buffer.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_rust_buffer
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_rust_buffer = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_rust_buffer", java.lang.foreign.FunctionDescriptor.of(RustBuffer.LAYOUT, java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static java.lang.foreign.MemorySegment ffi_sculk_ffi_rust_future_complete_rust_buffer(java.lang.foreign.SegmentAllocator _allocator, long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            return (java.lang.foreign.MemorySegment) MH_ffi_sculk_ffi_rust_future_complete_rust_buffer.invokeExact(_allocator, handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_poll_void
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_poll_void = findDowncallHandle("ffi_sculk_ffi_rust_future_poll_void", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS, java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_poll_void(long handle, java.lang.foreign.MemorySegment callback, long callbackData) {
        try {
            MH_ffi_sculk_ffi_rust_future_poll_void.invokeExact(handle, callback, callbackData);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_cancel_void
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_cancel_void = findDowncallHandle("ffi_sculk_ffi_rust_future_cancel_void", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_cancel_void(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_cancel_void.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_free_void
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_free_void = findDowncallHandle("ffi_sculk_ffi_rust_future_free_void", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG));

    static void ffi_sculk_ffi_rust_future_free_void(long handle) {
        try {
            MH_ffi_sculk_ffi_rust_future_free_void.invokeExact(handle);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_rust_future_complete_void
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_rust_future_complete_void = findDowncallHandle("ffi_sculk_ffi_rust_future_complete_void", java.lang.foreign.FunctionDescriptor.ofVoid(java.lang.foreign.ValueLayout.JAVA_LONG, java.lang.foreign.ValueLayout.ADDRESS));

    static void ffi_sculk_ffi_rust_future_complete_void(long handle, java.lang.foreign.MemorySegment _uniffiOutErr) {
        try {
            MH_ffi_sculk_ffi_rust_future_complete_void.invokeExact(handle, _uniffiOutErr);
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_access_token_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_access_token_from_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_func_access_token_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_access_token_from_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_access_token_from_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_access_token_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_access_token_generate = findDowncallHandle("uniffi_sculk_ffi_checksum_func_access_token_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_access_token_generate() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_access_token_generate.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_default_inlet_port
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_default_inlet_port = findDowncallHandle("uniffi_sculk_ffi_checksum_func_default_inlet_port", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_default_inlet_port() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_default_inlet_port.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_default_mc_port
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_default_mc_port = findDowncallHandle("uniffi_sculk_ffi_checksum_func_default_mc_port", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_default_mc_port() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_default_mc_port.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_join_uri_parse
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_join_uri_parse = findDowncallHandle("uniffi_sculk_ffi_checksum_func_join_uri_parse", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_join_uri_parse() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_join_uri_parse.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_lan_broadcaster_start
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_lan_broadcaster_start = findDowncallHandle("uniffi_sculk_ffi_checksum_func_lan_broadcaster_start", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_lan_broadcaster_start() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_lan_broadcaster_start.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_lan_scanner_start
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_lan_scanner_start = findDowncallHandle("uniffi_sculk_ffi_checksum_func_lan_scanner_start", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_lan_scanner_start() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_lan_scanner_start.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_data_dir
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_data_dir = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_data_dir", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_data_dir() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_data_dir.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_default_host_state_path
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_default_host_state_path = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_default_host_state_path", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_default_host_state_path() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_default_host_state_path.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_default_key_path
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_default_key_path = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_default_key_path", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_default_key_path() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_default_key_path.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_generate_new_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_generate_new_key = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_generate_new_key", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_generate_new_key() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_generate_new_key.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_load_host_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_load_host_state = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_load_host_state", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_load_host_state() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_load_host_state.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_load_profile
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_load_profile = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_load_profile", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_load_profile() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_load_profile.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_load_profile_from
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_load_profile_from = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_load_profile_from", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_load_profile_from() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_load_profile_from.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_save_host_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_save_host_state = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_save_host_state", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_save_host_state() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_save_host_state.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_save_profile
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_save_profile = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_save_profile", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_save_profile() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_save_profile.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_persist_save_profile_to
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_persist_save_profile_to = findDowncallHandle("uniffi_sculk_ffi_checksum_func_persist_save_profile_to", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_persist_save_profile_to() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_persist_save_profile_to.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_probe_server
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_probe_server = findDowncallHandle("uniffi_sculk_ffi_checksum_func_probe_server", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_probe_server() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_probe_server.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_sculpt_node_bind
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_sculpt_node_bind = findDowncallHandle("uniffi_sculk_ffi_checksum_func_sculpt_node_bind", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_sculpt_node_bind() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_sculpt_node_bind.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_secret_key_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_secret_key_from_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_func_secret_key_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_secret_key_from_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_secret_key_from_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_secret_key_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_secret_key_generate = findDowncallHandle("uniffi_sculk_ffi_checksum_func_secret_key_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_secret_key_generate() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_secret_key_generate.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_service_id_from_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_service_id_from_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_func_service_id_from_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_service_id_from_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_service_id_from_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_func_service_id_generate
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_func_service_id_generate = findDowncallHandle("uniffi_sculk_ffi_checksum_func_service_id_generate", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_func_service_id_generate() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_func_service_id_generate.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_accesstoken_matches
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_accesstoken_matches = findDowncallHandle("uniffi_sculk_ffi_checksum_method_accesstoken_matches", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_accesstoken_matches() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_accesstoken_matches.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_status = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_status() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_status.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv = findDowncallHandle("uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri = findDowncallHandle("uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_joinuri_redacted
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_joinuri_redacted = findDowncallHandle("uniffi_sculk_ffi_checksum_method_joinuri_redacted", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_joinuri_redacted() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_joinuri_redacted.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_joinuri_relay_url
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_joinuri_relay_url = findDowncallHandle("uniffi_sculk_ffi_checksum_method_joinuri_relay_url", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_joinuri_relay_url() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_joinuri_relay_url.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished = findDowncallHandle("uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop = findDowncallHandle("uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_lanscanner_is_finished
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_lanscanner_is_finished = findDowncallHandle("uniffi_sculk_ffi_checksum_method_lanscanner_is_finished", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_lanscanner_is_finished() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_lanscanner_is_finished.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_lanscanner_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_lanscanner_stop = findDowncallHandle("uniffi_sculk_ffi_checksum_method_lanscanner_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_lanscanner_stop() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_lanscanner_stop.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_lanscanner_try_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_lanscanner_try_recv = findDowncallHandle("uniffi_sculk_ffi_checksum_method_lanscanner_try_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_lanscanner_try_recv() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_lanscanner_try_recv.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_rotate_token
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_rotate_token = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_rotate_token", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_rotate_token() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_rotate_token.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_service_count
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_service_count = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_service_count", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_service_count() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_service_count.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_shutdown
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_shutdown = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_shutdown", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_shutdown() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_shutdown.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_start_service
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_start_service = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_start_service", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_start_service() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_start_service.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_status = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_status() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_status.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_sculknode_stop_service
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_sculknode_stop_service = findDowncallHandle("uniffi_sculk_ffi_checksum_method_sculknode_stop_service", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_sculknode_stop_service() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_sculknode_stop_service.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_secretkey_public_key
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_secretkey_public_key = findDowncallHandle("uniffi_sculk_ffi_checksum_method_secretkey_public_key", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_secretkey_public_key() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_secretkey_public_key.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_secretkey_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_secretkey_to_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_method_secretkey_to_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_secretkey_to_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_secretkey_to_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_serviceid_to_bytes
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_serviceid_to_bytes = findDowncallHandle("uniffi_sculk_ffi_checksum_method_serviceid_to_bytes", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_serviceid_to_bytes() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_serviceid_to_bytes.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_subscription_recv
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_subscription_recv = findDowncallHandle("uniffi_sculk_ffi_checksum_method_subscription_recv", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_subscription_recv() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_subscription_recv.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_start_host
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_start_host = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_start_host", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_start_host() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_start_host.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_start_join
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_start_join = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_start_join", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_start_join() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_start_join.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_status
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_status = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_status", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_status() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_status.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_stop
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_stop = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_stop", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_stop() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_stop.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe = findDowncallHandle("uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // uniffi_sculk_ffi_checksum_constructor_tunnelservice_new
    private static final java.lang.invoke.MethodHandle MH_uniffi_sculk_ffi_checksum_constructor_tunnelservice_new = findDowncallHandle("uniffi_sculk_ffi_checksum_constructor_tunnelservice_new", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_SHORT));

    static short uniffi_sculk_ffi_checksum_constructor_tunnelservice_new() {
        try {
            return (short) MH_uniffi_sculk_ffi_checksum_constructor_tunnelservice_new.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    // ffi_sculk_ffi_uniffi_contract_version
    private static final java.lang.invoke.MethodHandle MH_ffi_sculk_ffi_uniffi_contract_version = findDowncallHandle("ffi_sculk_ffi_uniffi_contract_version", java.lang.foreign.FunctionDescriptor.of(java.lang.foreign.ValueLayout.JAVA_INT));

    static int ffi_sculk_ffi_uniffi_contract_version() {
        try {
            return (int) MH_ffi_sculk_ffi_uniffi_contract_version.invokeExact();
        } catch (Throwable _ex) { throw new AssertionError("invokeExact failed", _ex); }
    }

    

    // Integrity checks and initialization must happen after all MethodHandle fields
    // are initialized (static fields are initialized in textual order in Java).
    static {
        NamespaceLibrary.uniffiCheckContractApiVersion();
        NamespaceLibrary.uniffiCheckApiChecksums();
        
        CLEANER = UniffiCleaner.create();
        }
}

