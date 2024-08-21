package i2f.thread;

import i2f.thread.local.ThreadLocalUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/8/20 20:43
 * @desc
 */
@Data
@NoArgsConstructor
public class DynamicSharedThreadPool implements ExecutorService {
    protected static final AtomicInteger POOL_COUNT = new AtomicInteger(0);

    protected LinkedBlockingQueue<ThreadTaskInfo<?>> taskQueue = new LinkedBlockingQueue<>();
    protected ConcurrentHashMap<String, ThreadWorkInfo> threadMap = new ConcurrentHashMap<>();

    protected String threadName = "dynamic-shared";
    protected AtomicInteger threadId = new AtomicInteger(0);

    protected volatile int processorCount = Runtime.getRuntime().availableProcessors();
    protected AtomicInteger currThreadCount = new AtomicInteger(0);
    protected AtomicInteger maxThreadCount = new AtomicInteger(1024 * processorCount);

    protected AtomicInteger workThreadIdleMillSeconds = new AtomicInteger(1);
    protected AtomicInteger workThreadAliveWaitMillSeconds = new AtomicInteger(30 * 1000);

    protected AtomicLong taskMaxWaitMillSeconds = new AtomicLong(300);

    protected AtomicBoolean enableClearThreadLocals = new AtomicBoolean(false);
    protected AtomicBoolean enableClearInheritableThreadLocals = new AtomicBoolean(false);

    protected AtomicBoolean shutdown = new AtomicBoolean(false);

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public DynamicSharedThreadPool(String threadName) {
        if (threadName != null) {
            this.threadName = threadName;
        }
        this.threadName = this.threadName + "-" + POOL_COUNT.incrementAndGet();
        triggerThread();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (this.shutdown.get()) {
            throw new IllegalStateException("pool has shutdown!");
        }
        CompletableFuture<T> ret = new CompletableFuture<>();
        ThreadTaskInfo<T> info = (ThreadTaskInfo<T>) new ThreadTaskInfo<>(task, ret);
        taskQueue.add(info);
        System.out.println("taskQueue:size:" + taskQueue.size());
        triggerThread();
        return ret;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                task.run();
                return null;
            }
        });
    }

    @Override
    public void shutdown() {
        this.shutdown.set(true);
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> ret = new LinkedList<>();
        while (!taskQueue.isEmpty()) {
            ThreadTaskInfo<?> task = taskQueue.poll();
            if (task != null) {
                ret.add(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            task.getTask().call();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }
                });
            }
        }
        return ret;
    }

    @Override
    public boolean isShutdown() {
        return this.shutdown.get();
    }

    @Override
    public boolean isTerminated() {
        return this.shutdown.get() && this.taskQueue.isEmpty();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.submit(() -> {
            task.run();
            return result;
        });
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        int count = 0;
        List<Future<T>> ret = new ArrayList<>();
        for (Callable<T> task : tasks) {
            count++;
        }
        CountDownLatch latch = new CountDownLatch(count);
        for (Callable<T> task : tasks) {
            Future<T> future = this.submit(() -> {
                try {
                    return task.call();
                } finally {
                    latch.countDown();
                }
            });
            ret.add(future);
        }
        latch.await();
        return ret;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        int count = 0;
        List<Future<T>> ret = new ArrayList<>();
        for (Callable<T> task : tasks) {
            count++;
        }
        CountDownLatch latch = new CountDownLatch(count);
        for (Callable<T> task : tasks) {
            Future<T> future = this.submit(() -> {
                try {
                    return task.call();
                } finally {
                    latch.countDown();
                }
            });
            ret.add(future);
        }
        latch.await(timeout, unit);
        return ret;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        AtomicReference<T> ret = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        for (Callable<T> task : tasks) {
            Future<T> future = this.submit(() -> {
                try {
                    T obj = task.call();
                    ret.set(obj);
                    return obj;
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        return ret.get();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        AtomicReference<T> ret = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        for (Callable<T> task : tasks) {
            Future<T> future = this.submit(() -> {
                try {
                    T obj = task.call();
                    ret.set(obj);
                    return obj;
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(timeout, unit);
        return ret.get();
    }

    @Override
    public void execute(Runnable command) {
        try {
            this.submit(command).get();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void triggerThread() {
        lock.readLock().lock();
        try {
            if (currThreadCount.get() >= maxThreadCount.get()) {
                return;
            }
            if (currThreadCount.get() > taskQueue.size() / processorCount) {
                return;
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            int addCount = taskQueue.size() / (currThreadCount.get() + 1);
            if (addCount < 1) {
                addCount = 1;
            }
            System.out.println("addCount:" + addCount);
            for (int i = 0; i < addCount; i++) {
                addWorkThread();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void addWorkThread() {
        ThreadWorkInfo info = new ThreadWorkInfo();
        info.setThreadName(threadName + "-" + threadId.incrementAndGet());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    workTaskLoop(info);
                } catch (Exception e) {
                    info.setThrowable(e);
                } finally {
                    info.setEndTs(System.currentTimeMillis());
                    lock.writeLock().lock();
                    try{
                        currThreadCount.decrementAndGet();
                        threadMap.remove(info.getThreadName());
                    }finally {
                        lock.writeLock().unlock();
                    }

                    System.out.println("currThreadCount:destroy:" + currThreadCount.get());
                    System.out.println("threadMap:size:" + threadMap.size());
                }
            }
        }, info.getThreadName());
        thread.start();
        info.setThread(thread);
        info.setStartTs(System.currentTimeMillis());
        info.getTaskCount().set(0);
        lock.writeLock().lock();
        try{
            currThreadCount.incrementAndGet();
            threadMap.put(info.getThreadName(), info);
        }finally {
            lock.writeLock().unlock();
        }
        System.out.println("currThreadCount:create:" + currThreadCount.get());
        System.out.println("threadMap:size:" + threadMap.size());
    }

    protected void workTaskLoop(ThreadWorkInfo info) {
        long lastWorkTs = System.currentTimeMillis();

        info.setRecentSumTaskUseMillSeconds(0);
        info.setRecentTaskCount(0);
        info.setRecentTaskUseMillSecondsList(new LinkedList<>());
        info.setRecentRunTimestampMillSecondsList(new LinkedList<>());
        info.setRecentCpuTimestampMillSecondsList(new LinkedList<>());

        info.getRecentRunTimestampMillSecondsList().add(System.currentTimeMillis());

        Thread thread = Thread.currentThread();
        long id = thread.getId();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        info.getRecentCpuTimestampMillSecondsList().add(threadMXBean.getThreadCpuTime(id)/1000);

        int yieldCount = 0;
        while (true) {
            ThreadTaskInfo<?> task = null;
            for (int i = 0; i < 5; i++) {
                task = taskQueue.poll();
                if (task != null) {
                    yieldCount = 0;
                    break;
                }
                yieldCount++;
            }
            if (task == null) {
                long idleMillSeconds = System.currentTimeMillis() - lastWorkTs;
                if (idleMillSeconds > workThreadAliveWaitMillSeconds.get()) {
                    break;
                }
                try {
                    Thread.sleep(workThreadIdleMillSeconds.get() + yieldCount);
                } catch (Exception e) {

                }
                continue;
            }
            System.out.println("taskQueue:size:" + taskQueue.size());
            Callable<?> callable = task.getTask();
            CompletableFuture future = task.getFuture();

            long beginTs = System.currentTimeMillis();
            task.setTriggerTs(beginTs);

            long waitMillSeconds = beginTs - task.submitTs;
            if (waitMillSeconds > taskMaxWaitMillSeconds.get()) {
                triggerThread();
            }


            try {
                info.getTaskCount().incrementAndGet();
                lastWorkTs = beginTs;

                if (enableClearThreadLocals.get()) {
                    ThreadLocalUtil.clearAllThreadLocals(thread);
                }
                if (enableClearInheritableThreadLocals.get()) {
                    ThreadLocalUtil.clearAllInheritableThreadLocals(thread);
                }
                Object ret = callable.call();

                if (future != null) {
                    future.complete(ret);
                }
            } catch (Throwable e) {
                task.setThrowable(e);
                if (future != null) {
                    future.completeExceptionally(e);
                }
            } finally {
                long endTs = System.currentTimeMillis();
                long diffTs = endTs - beginTs;

                task.setFinishTs(endTs);

                info.getRecentTaskUseMillSecondsList().add(diffTs);
                info.setRecentSumTaskUseMillSeconds(info.getRecentSumTaskUseMillSeconds()+diffTs);
                info.setRecentTaskCount(info.getRecentTaskCount()+1);

                info.getRecentRunTimestampMillSecondsList().add(endTs);

                long cpuTime = threadMXBean.getThreadCpuTime(id) / 1000;
                info.getRecentCpuTimestampMillSecondsList().add(cpuTime);

                if(info.getRecentTaskCount()>30){
                    long diff = info.getRecentTaskUseMillSecondsList().removeFirst();
                    info.setRecentSumTaskUseMillSeconds(info.getRecentSumTaskUseMillSeconds()-diff);
                    info.setRecentTaskCount(info.getRecentTaskCount()-1);

                    info.getRecentRunTimestampMillSecondsList().removeFirst();
                    info.getRecentCpuTimestampMillSecondsList().removeFirst();
                }

                long avgUse=info.getRecentSumTaskUseMillSeconds()/info.getRecentTaskCount();

                long firstTs = info.getRecentRunTimestampMillSecondsList().getFirst();
                long sumTs=endTs-firstTs;
                long avgSum=sumTs/info.getRecentTaskCount();

                double useRate=avgUse*1.0/avgSum;
                System.out.println("useRate:"+String.format("%.04f",useRate));
                if(useRate<0.6){
                    triggerThread();
                }

                long avgCpu=cpuTime-info.getRecentCpuTimestampMillSecondsList().getFirst();
                avgCpu=avgCpu/info.getRecentTaskCount();

                double cpuRate=avgCpu*1.0/avgSum;
                System.out.println("cpuRate:"+String.format("%.04f",cpuRate));
                if(cpuRate<0.3){
                    triggerThread();
                }
            }

        }
    }
}
