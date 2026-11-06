package a90pn.p2pc.ffi;

/**
 * 统一订阅端：首次 recv 返回当前状态，之后状态/事件交错。
 */
public class Subscription implements AutoCloseable, SubscriptionInterface {
  protected long handle;
  protected UniffiCleaner.Cleanable cleanable;

  private java.util.concurrent.atomic.AtomicBoolean wasDestroyed = new java.util.concurrent.atomic.AtomicBoolean(false);
  private java.util.concurrent.atomic.AtomicLong callCounter = new java.util.concurrent.atomic.AtomicLong(1);

  /**
   * Internal constructor to wrap a raw handle from FFI.
   * The UniffiWithHandle marker disambiguates this from other constructors.
   */
  public Subscription(UniffiWithHandle phantom, long handle) {
    this.handle = handle;
    this.cleanable = UniffiLib.CLEANER.register(this, new UniffiCleanAction(handle));
  }

  /**
   * This constructor can be used to instantiate a fake object. Only used for tests. Any
   * attempt to actually use an object constructed this way will fail as there is no
   * connected Rust object.
   */
  public Subscription(NoHandle noHandle) {
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
        throw new java.lang.IllegalStateException("Subscription object has already been destroyed");
      }
      if (c == java.lang.Long.MAX_VALUE) {
        throw new java.lang.IllegalStateException("Subscription call counter would overflow");
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
          UniffiLib.uniffi_sculk_ffi_fn_free_subscription(handle, status);
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
      return UniffiLib.uniffi_sculk_ffi_fn_clone_subscription(handle, status);
    });
  }

  
        /**
     * Re-polls and completes on {@link java.util.concurrent.ForkJoinPool#commonPool()}. See the overload
     * taking an {@link java.util.concurrent.Executor} for the contract an executor must meet.
     */
    @Override
    public java.util.concurrent.CompletableFuture<TunnelUpdate> recv(){
        return recv(java.util.concurrent.ForkJoinPool.commonPool());
    }    /**
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
    public java.util.concurrent.CompletableFuture<TunnelUpdate> recv(java.util.concurrent.Executor _uniffiExecutor
    ){
        return UniffiAsyncHelpers.uniffiRustCallAsync(
        _uniffiExecutor,
        callWithHandle(_uniffiHandle -> {
            return UniffiLib.uniffi_sculk_ffi_fn_method_subscription_recv(
                _uniffiHandle
                
            );
        }),
        (_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle) -> UniffiLib.ffi_sculk_ffi_rust_future_poll_rust_buffer(_uniffiFuture, _uniffiCallback, _uniffiContinuationHandle),
        (_uniffiAllocator, _uniffiFuture, _uniffiStatus) -> UniffiLib.ffi_sculk_ffi_rust_future_complete_rust_buffer(_uniffiAllocator, _uniffiFuture, _uniffiStatus),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_cancel_rust_buffer(_uniffiFuture),
        (_uniffiFuture) -> UniffiLib.ffi_sculk_ffi_rust_future_free_rust_buffer(_uniffiFuture),
        // lift function
        (_uniffiResult) -> FfiConverterOptionalTypeTunnelUpdate.INSTANCE.lift(_uniffiResult),
        // Error FFI converter
        new ScuLkExceptionErrorHandler()
    );
    }

  

  


  
}

