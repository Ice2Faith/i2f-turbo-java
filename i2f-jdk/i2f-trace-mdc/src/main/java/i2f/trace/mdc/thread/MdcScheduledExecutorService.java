package i2f.trace.mdc.thread;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/15 9:40
 * @desc
 */
public class MdcScheduledExecutorService implements ScheduledExecutorService {
    protected final ScheduledExecutorService delegate;

    public MdcScheduledExecutorService(ScheduledExecutorService delegate) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        this.delegate = delegate;
    }

    public static MdcScheduledExecutorService of(ScheduledExecutorService pool) {
        if (pool instanceof MdcScheduledExecutorService) {
            return (MdcScheduledExecutorService) pool;
        }
        return new MdcScheduledExecutorService(pool);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.delegate.schedule(MdcRunnable.of(command), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.delegate.schedule(MdcCallable.of(callable), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.delegate.scheduleAtFixedRate(MdcRunnable.ofNewTrace(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.delegate.scheduleWithFixedDelay(MdcRunnable.ofNewTrace(command), initialDelay, delay, unit);
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
