package i2f.thread;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/8/20 20:43
 * @desc
 */
@Data
@NoArgsConstructor
public class DynamicSharedThreadPool {
    protected static final AtomicInteger POOL_COUNT = new AtomicInteger(0);

    protected LinkedBlockingQueue<ThreadTaskInfo<?>> taskQueue = new LinkedBlockingQueue<>();
    protected ConcurrentHashMap<String, ThreadWorkInfo> threadMap = new ConcurrentHashMap<>();

    protected String threadName = "dynamic-shared";
    protected AtomicInteger threadId = new AtomicInteger(0);

    protected AtomicInteger currThreadCount = new AtomicInteger(0);
    protected AtomicInteger maxThreadCount = new AtomicInteger(300);

    protected AtomicInteger workThreadIdleMillSeconds = new AtomicInteger(1);
    protected AtomicInteger workThreadAliveWaitMillSeconds = new AtomicInteger(30 * 1000);

    protected AtomicLong taskMaxWaitMillSeconds = new AtomicLong(300);

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public DynamicSharedThreadPool(int maxThreadCount) {
        this(maxThreadCount, null);
    }

    public DynamicSharedThreadPool(int maxThreadCount, String threadName) {
        this.maxThreadCount.set(Math.max(1, maxThreadCount));
        if (threadName != null) {
            this.threadName = threadName;
        }
        this.threadName = this.threadName + "-" + POOL_COUNT.incrementAndGet();
        triggerThread();
    }

    public <T> Future<T> submit(Callable<T> task) {
        CompletableFuture<T> ret = new CompletableFuture<>();
        ThreadTaskInfo<T> info = (ThreadTaskInfo<T>) new ThreadTaskInfo<>(task, ret);
        taskQueue.add(info);
        triggerThread();
        return ret;
    }

    public void submit(Runnable task) {
        submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                task.run();
                return null;
            }
        });
    }

    protected void triggerThread() {
        if (currThreadCount.getAndIncrement() == 0) {
            addWorkThread();
            return;
        }
        lock.readLock().lock();
        try {
            if (currThreadCount.get() > maxThreadCount.get()) {
                return;
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            addWorkThread();
            currThreadCount.incrementAndGet();
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

                    System.out.println("currThreadCount:"+currThreadCount.get());
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
        System.out.println("currThreadCount:"+currThreadCount.get());
    }

    protected void workTaskLoop(ThreadWorkInfo info) {
        long lastWorkTs = System.currentTimeMillis();

        info.setTaskUseMillSeconds(0);
        info.setTaskUseCount(0);
        info.setTaskUseMillSecondsList(new LinkedList<>());
        info.setTaskWatermarkMillSecondsList(new LinkedList<>());
        info.setTaskCpuMillSecondsList(new LinkedList<>());

        info.getTaskWatermarkMillSecondsList().add(System.currentTimeMillis());

        long id = Thread.currentThread().getId();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        info.getTaskCpuMillSecondsList().add(threadMXBean.getThreadCpuTime(id)/1000);

        while (true) {
            ThreadTaskInfo<?> task = null;
            for (int i = 0; i < 5; i++) {
                task = taskQueue.poll();
                if (task != null) {
                    break;
                }
            }
            if (task == null) {
                long idleMillSeconds = System.currentTimeMillis() - lastWorkTs;
                if (idleMillSeconds > workThreadAliveWaitMillSeconds.get()) {
                    break;
                }
                try {
                    Thread.sleep(workThreadIdleMillSeconds.get());
                } catch (Exception e) {

                }
                continue;
            }

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

                info.getTaskUseMillSecondsList().add(diffTs);
                info.setTaskUseMillSeconds(info.getTaskUseMillSeconds()+diffTs);
                info.setTaskUseCount(info.getTaskUseCount()+1);

                info.getTaskWatermarkMillSecondsList().add(endTs);

                long cpuTime = threadMXBean.getThreadCpuTime(id) / 1000;
                info.getTaskCpuMillSecondsList().add(cpuTime);

                if(info.getTaskUseCount()>30){
                    long diff = info.getTaskUseMillSecondsList().removeFirst();
                    info.setTaskUseMillSeconds(info.getTaskUseMillSeconds()-diff);
                    info.setTaskUseCount(info.getTaskUseCount()-1);

                    info.getTaskWatermarkMillSecondsList().removeFirst();
                    info.getTaskCpuMillSecondsList().removeFirst();
                }

                long avgUse=info.getTaskUseMillSeconds()/info.getTaskUseCount();

                long firstTs = info.getTaskWatermarkMillSecondsList().getFirst();
                long sumTs=endTs-firstTs;
                long avgSum=sumTs/info.getTaskUseCount();

                double useRate=avgUse*1.0/avgSum;
                System.out.println("useRate:"+String.format("%.04f",useRate));
                if(useRate<0.6){
                    triggerThread();
                }

                long avgCpu=cpuTime-info.getTaskCpuMillSecondsList().getFirst();
                avgCpu=avgCpu/info.getTaskUseCount();

                double cpuRate=avgCpu*1.0/avgSum;
                System.out.println("cpuRate:"+String.format("%.04f",cpuRate));
                if(cpuRate<0.3){
                    triggerThread();
                }
            }

        }
    }
}
