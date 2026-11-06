package a90pn.pepc.ffi;

/**
 * 已发布服务的轻量操作句柄。
 */
public class HostedServiceHandle implements AutoCloseable, HostedServiceHandleInterface {
  protected long handle;
  protected UniffiCleaner.Cleanable cleanable;

  private java.util.concurrent.atomic.AtomicBoolean wasDestroyed = new java.util.concurrent.atomic.AtomicBoolean(false);
  private java.util.concurrent.atomic.AtomicLong callCounter = new java.util.concurrent.atomic.AtomicLong(1);

  /**
   * Internal constructor to wrap a raw handle from FFI.
   * The UniffiWithHandle marker disambiguates this from other constructors.
   */
  public HostedServiceHandle(UniffiWithHandle phantom, long handle) {
    this.handle = handle;
    this.cleanable = UniffiLib.CLEANER.register(this, new UniffiCleanAction(handle));
  }

  /**
   * This constructor can be used to instantiate a fake object. Only used for tests. Any
   * attempt to actually use an object constructed this way will fail as there is no
   * connected Rust object.
   */
  public HostedServiceHandle(NoHandle noHandle) {
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
        throw new java.lang.IllegalStateException("HostedServiceHandle object has already been destroyed");
      }
      if (c == java.lang.Long.MAX_VALUE) {
        throw new java.lang.IllegalStateException("HostedServiceHandle call counter would overflow");
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
          UniffiLib.uniffi_sculk_ffi_fn_free_hostedservicehandle(handle, status);
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
      return UniffiLib.uniffi_sculk_ffi_fn_clone_hostedservicehandle(handle, status);
    });
  }

  
        /**
     * 当前可分享 Join URI。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<JoinUri> joinUri(){
        return joinUri(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 当前可分享 Join URI。
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
    public java.util.concurrent.CompletableFuture<JoinUri> joinUri(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_join_uri(
                _uniffiHandle
                
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
     * 立即轮换访问令牌，并返回新的 Join URI。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<JoinUri> rotateToken(){
        return rotateToken(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 立即轮换访问令牌，并返回新的 Join URI。
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
    public java.util.concurrent.CompletableFuture<JoinUri> rotateToken(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_rotate_token(
                _uniffiHandle
                
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
     * 服务标识（16 字节）。
     */
    @Override
    public java.util.List<java.lang.Byte> serviceId()  {
            try {
                return FfiConverterSequenceByte.INSTANCE.lift(
    callWithHandle(_uniffiHandle -> {
        try {
    
            return
    UniffiHelpers.uniffiRustCall( (_allocator, _status) -> {
        return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_service_id(_allocator, _uniffiHandle,
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
     * 服务的稳定状态快照。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<HostedServiceStatus> status(){
        return status(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 服务的稳定状态快照。
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
    public java.util.concurrent.CompletableFuture<HostedServiceStatus> status(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_status(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_rust_buffer(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_rust_buffer(_uniffiAllocator, _uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_rust_buffer(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_rust_buffer(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeHostedServiceStatus.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * 独立停止此服务。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<java.lang.Void> stop(){
        return stop(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 独立停止此服务。
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
    public java.util.concurrent.CompletableFuture<java.lang.Void> stop(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_stop(
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
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * 订阅此服务的过程事件。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<HostedServiceEventReader> subscribe(){
        return subscribe(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 订阅此服务的过程事件。
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
    public java.util.concurrent.CompletableFuture<HostedServiceEventReader> subscribe(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeHostedServiceEventReader.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * 订阅此服务可恢复的状态快照。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<HostedServiceStatusSubscription> subscribeStatus(){
        return subscribeStatus(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 订阅此服务可恢复的状态快照。
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
    public java.util.concurrent.CompletableFuture<HostedServiceStatusSubscription> subscribeStatus(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_subscribe_status(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_u64(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_u64(_uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_u64(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_u64(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeHostedServiceStatusSubscription.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  
        /**
     * 当前令牌及其创建时刻。
     *
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<TokenState> tokenState(){
        return tokenState(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
     * 当前令牌及其创建时刻。
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
    public java.util.concurrent.CompletableFuture<TokenState> tokenState(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_hostedservicehandle_token_state(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_rust_buffer(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_rust_buffer(_uniffiAllocator, _uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_rust_buffer(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_rust_buffer(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterTypeTokenState.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  

  


  
}

