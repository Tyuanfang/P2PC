package a90pn.pepc.ffi;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
public class SculkFfi {
  
    /**
     * join 端本地入站监听端口默认值。
     */public static short defaultInletPort()  {
            try {
                return 
    UniffiHelpers.uniffiRustCallShort( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_default_inlet_port(
            _status);
    })
    ;
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
    /**
     * Minecraft 服务端标准端口。
     */public static short defaultMcPort()  {
            try {
                return 
    UniffiHelpers.uniffiRustCallShort( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_default_mc_port(
            _status);
    })
    ;
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
    /**
     * 向本机 Minecraft 客户端周期性发布 LAN 公告。
     */public static LanBroadcaster lanBroadcasterStart(java.lang.String name, short port) throws ScuLkException {
            try {
                return FfiConverterTypeLanBroadcaster.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_lan_broadcaster_start(
            FfiConverterString.INSTANCE.lower(name), port, _status);
    })
    );
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                if (ScuLkException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (ScuLkException)_uniffi_ex.getCause();
                }
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
    /**
     * 开始监听 Minecraft LAN 公告（224.0.2.60:4445），发现可连接的本机服务后结束。
     */public static LanScanner lanScannerStart() throws ScuLkException {
            try {
                return FfiConverterTypeLanScanner.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_lan_scanner_start(
            _status);
    })
    );
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                if (ScuLkException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (ScuLkException)_uniffi_ex.getCause();
                }
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
    /**
     * 用旧版服务器列表探测（0xFE/0xFF）确认目标端口是 Minecraft Java 版服务。
     */public static void probeServer(java.lang.String addr, long timeoutMs) throws ScuLkException {
            try {
                
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        UniffiLib.uniffi_sculk_ffi_fn_func_probe_server(
            FfiConverterString.INSTANCE.lower(addr), timeoutMs, _status);
    })
    ;
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                if (ScuLkException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (ScuLkException)_uniffi_ex.getCause();
                }
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

}

