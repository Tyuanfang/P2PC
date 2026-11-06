package a90pn.pepc.ffi;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
public class SculkFfi {
  
    /**
     * 从 32 字节数组创建访问令牌。
     */public static AccessToken accessTokenFromBytes(java.util.List<java.lang.Byte> bytes) throws ScuLkException {
            try {
                return FfiConverterTypeAccessToken.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_access_token_from_bytes(
            FfiConverterSequenceByte.INSTANCE.lower(bytes), _status);
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
     * 生成新的 32 字节访问令牌。
     */public static AccessToken accessTokenGenerate()  {
            try {
                return FfiConverterTypeAccessToken.INSTANCE.lift(
    UniffiHelpers.uniffiRustCall( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_access_token_generate(
            _status);
    })
    );
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
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
     * 解析 `sculk://join/v1/` 分享 URI。
     */public static JoinUri joinUriParse(java.lang.String uri) throws ScuLkException {
            try {
                return FfiConverterTypeJoinUri.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_join_uri_parse(
            FfiConverterString.INSTANCE.lower(uri), _status);
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
     * 应用数据目录（`{data_dir}/sculk/`）。
     */public static java.lang.String persistDataDir() throws ScuLkException {
            try {
                return FfiConverterString.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_data_dir(_allocator, 
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
     * 默认 host 状态文件路径。
     */public static java.lang.String persistDefaultHostStatePath() throws ScuLkException {
            try {
                return FfiConverterString.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_default_host_state_path(_allocator, 
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
     * 默认节点密钥文件路径。
     */public static java.lang.String persistDefaultKeyPath() throws ScuLkException {
            try {
                return FfiConverterString.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_default_key_path(_allocator, 
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
     * 生成并写入新的节点密钥（覆盖已有文件）。
     */public static SecretKey persistGenerateNewKey(java.lang.String path) throws ScuLkException {
            try {
                return FfiConverterTypeSecretKey.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_generate_new_key(
            FfiConverterString.INSTANCE.lower(path), _status);
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
     * 加载 host 状态；文件不存在时返回 null。
     */public static HostState persistLoadHostState(java.lang.String path) throws ScuLkException {
            try {
                return FfiConverterOptionalTypeHostState.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_load_host_state(_allocator, 
            FfiConverterString.INSTANCE.lower(path), _status);
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
     * 加载稳定节点密钥；不存在时生成并写入。
     */public static SecretKey persistLoadOrGenerateKey(java.lang.String path) throws ScuLkException {
            try {
                return FfiConverterTypeSecretKey.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_load_or_generate_key(
            FfiConverterString.INSTANCE.lower(path), _status);
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
     * 加载默认位置的用户偏好 profile（不存在时返回默认值）。
     */public static Profile persistLoadProfile() throws ScuLkException {
            try {
                return FfiConverterTypeProfile.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_load_profile(_allocator, 
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
     * 从指定路径加载 profile。
     */public static Profile persistLoadProfileFrom(java.lang.String path) throws ScuLkException {
            try {
                return FfiConverterTypeProfile.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_persist_load_profile_from(_allocator, 
            FfiConverterString.INSTANCE.lower(path), _status);
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
     * 原子保存 host 状态。
     */public static void persistSaveHostState(java.lang.String path, HostState state) throws ScuLkException {
            try {
                
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        UniffiLib.uniffi_sculk_ffi_fn_func_persist_save_host_state(
            FfiConverterString.INSTANCE.lower(path), FfiConverterTypeHostState.INSTANCE.lower(state), _status);
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
    

  
    /**
     * 保存用户偏好 profile 到默认位置。
     */public static void persistSaveProfile(Profile profile) throws ScuLkException {
            try {
                
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        UniffiLib.uniffi_sculk_ffi_fn_func_persist_save_profile(
            FfiConverterTypeProfile.INSTANCE.lower(profile), _status);
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
    

  
    /**
     * 保存 profile 到指定路径。
     */public static void persistSaveProfileTo(java.lang.String path, Profile profile) throws ScuLkException {
            try {
                
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        UniffiLib.uniffi_sculk_ffi_fn_func_persist_save_profile_to(
            FfiConverterString.INSTANCE.lower(path), FfiConverterTypeProfile.INSTANCE.lower(profile), _status);
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
    

  
        /**
     * 创建多服务 Host 节点（单个 iroh Endpoint 上发布多个隔离服务）。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */public static java.util.concurrent.CompletableFuture<SculkNode> sculptNodeBind(NodeOptions options){
        return sculptNodeBind(options, java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 创建多服务 Host 节点（单个 iroh Endpoint 上发布多个隔离服务）。
     *
     * @param _uniffiExecutor runs every re-poll and the completion of the returned future. The first
     *     poll runs on the calling thread, before this method returns. Any executor that hands tasks to
     *     its own threads works: {@link java.util.concurrent.ForkJoinPool#commonPool()}, a fixed, cached
     *     or single-thread pool, or a virtual-thread executor. Rust invokes the continuation from inside
     *     {@code Waker::wake()}, so an executor that runs tasks on the submitting thread, such as
     *     {@code Runnable::run}, polls the future from within its own waker and deadlocks any future
     *     that holds a lock while waking. Cancelling the returned future signals Rust; the pipeline then
     *     frees the Rust future, so an executor that accepts a task and never runs it, such as one using
     *     {@link java.util.concurrent.ThreadPoolExecutor.DiscardPolicy} or one whose queue was drained
     *     by {@code shutdownNow()}, leaks the Rust future and everything it owns.
     */
    public static java.util.concurrent.CompletableFuture<SculkNode> sculptNodeBind(NodeOptions options, java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        UniffiLib.uniffi_sculk_ffi_fn_func_sculpt_node_bind(FfiConverterTypeNodeOptions.INSTANCE.lower(options)),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeSculkNode.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
    /**
     * 从 32 字节数组创建节点密钥。
     */public static SecretKey secretKeyFromBytes(java.util.List<java.lang.Byte> bytes) throws ScuLkException {
            try {
                return FfiConverterTypeSecretKey.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_secret_key_from_bytes(
            FfiConverterSequenceByte.INSTANCE.lower(bytes), _status);
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
     * 生成新的 32 字节节点密钥。
     */public static SecretKey secretKeyGenerate()  {
            try {
                return FfiConverterTypeSecretKey.INSTANCE.lift(
    UniffiHelpers.uniffiRustCall( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_secret_key_generate(
            _status);
    })
    );
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

  
    /**
     * 从 16 字节数组创建服务标识。
     */public static ServiceId serviceIdFromBytes(java.util.List<java.lang.Byte> bytes) throws ScuLkException {
            try {
                return FfiConverterTypeServiceId.INSTANCE.lift(
    UniffiHelpers.uniffiRustCallWithError(new ScuLkExceptionErrorHandler(), (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_service_id_from_bytes(
            FfiConverterSequenceByte.INSTANCE.lower(bytes), _status);
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
     * 生成新的 16 字节服务标识。
     */public static ServiceId serviceIdGenerate()  {
            try {
                return FfiConverterTypeServiceId.INSTANCE.lift(
    UniffiHelpers.uniffiRustCall( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_func_service_id_generate(
            _status);
    })
    );
            } catch (java.lang.RuntimeException _uniffi_ex) {
                
                
                if (InternalException.class.isInstance(_uniffi_ex.getCause())) {
                    throw (InternalException)_uniffi_ex.getCause();
                }
                throw _uniffi_ex;
            }
    }
    

}

