package a90pn.pepc.ffi;

/**
 * 多服务 Host 节点。
 */
public class SculkNode implements AutoCloseable, SculkNodeInterface {
  protected long handle;
  protected UniffiCleaner.Cleanable cleanable;

  private java.util.concurrent.atomic.AtomicBoolean wasDestroyed = new java.util.concurrent.atomic.AtomicBoolean(false);
  private java.util.concurrent.atomic.AtomicLong callCounter = new java.util.concurrent.atomic.AtomicLong(1);

  /**
   * Internal constructor to wrap a raw handle from FFI.
   * The UniffiWithHandle marker disambiguates this from other constructors.
   */
  public SculkNode(UniffiWithHandle phantom, long handle) {
    this.handle = handle;
    this.cleanable = UniffiLib.CLEANER.register(this, new UniffiCleanAction(handle));
  }

  /**
   * This constructor can be used to instantiate a fake object. Only used for tests. Any
   * attempt to actually use an object constructed this way will fail as there is no
   * connected Rust object.
   */
  public SculkNode(NoHandle noHandle) {
    this.handle = 0L;
    this.cleanable = null;
  }

  

  @Override
  public synchronized void close() {
    // Only allow a single call to this method.
    // TODO(uniffi): maybe we should log a warning if called more than once?
    if (this.wasDestroyed.compareAndSet(false, true)) {
      // This decrement always matches the initial count of 1 given at creation time.
      if (this.callCounter.decrementAndGet() == 0L) {
        if (cleanable != null) {
          cleanable.clean();
        }
      }
    }
  }

  public <R> R callWithHandle(java.util.function.Function<java.lang.Long, R> block) {
    // Check and increment the call counter, to keep the object alive.
    // This needs a compare-and-set retry loop in case of concurrent updates.
    long c;
    do {
      c = this.callCounter.get();
      if (c == 0L) {
        throw new java.lang.IllegalStateException("SculkNode object has already been destroyed");
      }
      if (c == java.lang.Long.MAX_VALUE) {
        throw new java.lang.IllegalStateException("SculkNode call counter would overflow");
      }
    } while (! this.callCounter.compareAndSet(c, c + 1L));
    // Now we can safely do the method call without the handle being freed concurrently.
    try {
      return block.apply(this.uniffiCloneHandle());
    } finally {
      // This decrement always matches the increment we performed above.
      if (this.callCounter.decrementAndGet() == 0L) {
        if (cleanable != null) {
          cleanable.clean();
        }
      }
    }
  }

  public void callWithHandle(java.util.function.Consumer<java.lang.Long> block) {
    callWithHandle((java.lang.Long uniffiHandle) -> {
      block.accept(uniffiHandle);
      return (java.lang.Void)null;
    });
  }

  private class UniffiCleanAction implements Runnable {
    private final long handle;

    public UniffiCleanAction(long handle) {
      this.handle = handle;
    }

    @Override
    public void run() {
      // If the handle is 0 this is a fake object created with `NoHandle`, don't try to free.
      if (handle != 0L) {
        UniffiHelpers.uniffiRustCall((_allocator, status) -> {
          UniffiLib.uniffi_sculk_ffi_fn_free_sculknode(handle, status);
          return null;
        });
      }
    }
  }

  long uniffiCloneHandle() {
    return UniffiHelpers.uniffiRustCall((_allocator, status) -> {
      if (handle == 0L) {
        throw new java.lang.NullPointerException();
      }
      return UniffiLib.uniffi_sculk_ffi_fn_clone_sculknode(handle, status);
    });
  }

  
    /**
     * Node 的稳定 EndpointId。
     */
    @Override
    public java.lang.String endpointId()  {
            try {
                return FfiConverterString.INSTANCE.lift(
    callWithHandle(_uniffiHandle -> {
        try {
    
            return
    UniffiHelpers.uniffiRustCall( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_endpoint_id(_allocator, _uniffiHandle,
            _status);
    });
    } catch (java.lang.RuntimeException _uniffi_ex) {
            throw _uniffi_ex;
        } catch (java.lang.Exception _uniffi_ex) {
            throw new java.lang.RuntimeException(_uniffi_ex);
        }
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
     * 为指定服务生成新令牌，并返回对应的新 Join URI。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<JoinUri> rotateToken(java.util.List<java.lang.Byte> serviceId){
        return rotateToken(serviceId, java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 为指定服务生成新令牌，并返回对应的新 Join URI。
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
    public java.util.concurrent.CompletableFuture<JoinUri> rotateToken(java.util.List<java.lang.Byte> serviceId, java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_rotate_token(
                _uniffiHandle,
                FfiConverterSequenceByte.INSTANCE.lower(serviceId)
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeJoinUri.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * 当前已发布的服务数量。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<java.lang.Long> serviceCount(){
        return serviceCount(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 当前已发布的服务数量。
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
    public java.util.concurrent.CompletableFuture<java.lang.Long> serviceCount(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_service_count(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterLong.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new UniffiNullRustCallStatusErrorHandler()
    );
    }

  
        /**
     * 停止所有服务并关闭 Node Endpoint。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<java.lang.Void> shutdown(){
        return shutdown(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 停止所有服务并关闭 Node Endpoint。
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
    public java.util.concurrent.CompletableFuture<java.lang.Void> shutdown(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_shutdown(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_void(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_void(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_void(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_void(_uniffiFuture),
        // lift function
        () -> {},
        // Error FFI converter
        new UniffiNullRustCallStatusErrorHandler()
    );
    }

  
        /**
     * 发布服务并返回其操作句柄。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<HostedServiceHandle> startService(HostedServiceOptions options){
        return startService(options, java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 发布服务并返回其操作句柄。
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
    public java.util.concurrent.CompletableFuture<HostedServiceHandle> startService(HostedServiceOptions options, java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_start_service(
                _uniffiHandle,
                FfiConverterTypeHostedServiceOptions.INSTANCE.lower(options)
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeHostedServiceHandle.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * Node 的稳定状态快照。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<SculkNodeStatus> status(){
        return status(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * Node 的稳定状态快照。
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
    public java.util.concurrent.CompletableFuture<SculkNodeStatus> status(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_status(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_rust_buffer(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_rust_buffer(_uniffiAllocator, _uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_rust_buffer(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_rust_buffer(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeSculkNodeStatus.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new UniffiNullRustCallStatusErrorHandler()
    );
    }

  
        /**
     * 停止一个服务；不会关闭 Node 或影响其他服务。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<java.lang.Void> stopService(java.util.List<java.lang.Byte> serviceId){
        return stopService(serviceId, java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 停止一个服务；不会关闭 Node 或影响其他服务。
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
    public java.util.concurrent.CompletableFuture<java.lang.Void> stopService(java.util.List<java.lang.Byte> serviceId, java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_sculknode_stop_service(
                _uniffiHandle,
                FfiConverterSequenceByte.INSTANCE.lower(serviceId)
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_void(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_void(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_void(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_void(_uniffiFuture),
        // lift function
        () -> {},
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  

  


  
}

