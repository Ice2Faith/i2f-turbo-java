package i2f.trace.mdc.thread;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:30
 * @desc
 */
public class MdcExecutorService implements ExecutorService {
    protected final ExecutorService delegate;

    public MdcExecutorService(ExecutorService delegate) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        this.delegate = delegate;
    }

    public static MdcExecutorService of(ExecutorService pool) {
        if (pool instanceof MdcExecutorService) {
            return (MdcExecutorService) pool;
        }
        return new MdcExecutorService(pool);
    }

    @Override
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.delegate.submit(MdcCallable.of(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.delegate.submit(MdcRunnable.of(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return this.delegate.submit(MdcRunnable.of(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.delegate.invokeAll(tasks.stream().map(MdcCallable::of).collect(Collectors.toList()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.invokeAll(tasks.stream().map(MdcCallable::of).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.delegate.invokeAny(tasks.stream().map(MdcCallable::of).collect(Collectors.toList()));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.invokeAny(tasks.stream().map(MdcCallable::of).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        this.delegate.execute(MdcRunnable.of(command));
    }

}
