package i2f.event;

import i2f.clock.SystemClock;
import lombok.Data;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/8/9 21:04
 * @desc
 */
public class EventDispatcher<T> implements IEventPublisher<T> {
    protected static final AtomicInteger dispatcherThreadCount = new AtomicInteger(0);

    protected final LinkedBlockingQueue<Map.Entry<T, Long>> queue = new LinkedBlockingQueue<>();
    protected final CopyOnWriteArrayList<IEventSubscriber<T>> subscribers = new CopyOnWriteArrayList<>();
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    protected ExecutorService pool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() * 2);
    protected final AtomicLong threadIdleMillSeconds = new AtomicLong(300);
    protected final AtomicBoolean waitSubscriber = new AtomicBoolean(false);
    protected final AtomicLong dropOverMillSeconds = new AtomicLong(0);
    protected BiConsumer<Thread, EventDispatcher<T>> dispatcherThreadConsumer;

    public EventDispatcher() {
    }

    public EventDispatcher(ExecutorService pool) {
        this.pool = pool;
    }

    public ExecutorService getPool() {
        return pool;
    }

    public void setPool(ExecutorService pool) {
        this.pool = pool;
    }

    public long getThreadIdleMillSeconds() {
        return threadIdleMillSeconds.get();
    }

    public void setThreadIdleMillSeconds(long millSeconds) {
        threadIdleMillSeconds.set(millSeconds >= 0 ? millSeconds : 0);
    }

    public boolean getWaitSubscriber() {
        return waitSubscriber.get();
    }

    public void setWaitSubscriber(boolean enable) {
        waitSubscriber.set(enable);
    }

    public long getDropOverMillSeconds() {
        return dropOverMillSeconds.get();
    }

    public void setDropOverMillSeconds(long millSeconds) {
        dropOverMillSeconds.set(millSeconds);
    }

    public BiConsumer<Thread, EventDispatcher<T>> getDispatcherThreadConsumer() {
        return dispatcherThreadConsumer;
    }

    public void setDispatcherThreadConsumer(BiConsumer<Thread, EventDispatcher<T>> dispatcherThreadConsumer) {
        this.dispatcherThreadConsumer = dispatcherThreadConsumer;
    }

    @Override
    public void publish(T event) {
        this.queue.add(new AbstractMap.SimpleEntry<>(event, SystemClock.currentTimeMillis()));
        runDispatcher();
    }

    @Override
    public void subscribe(IEventSubscriber<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    public void runDispatcher() {
        if (initialized.getAndSet(true)) {
            return;
        }
        String threadName = "event-dispatcher-" + dispatcherThreadCount.incrementAndGet();
        Thread thread = new Thread(() -> {
            doDispatchLoop();
        }, threadName);
        thread.setDaemon(true);
        if (dispatcherThreadConsumer != null) {
            dispatcherThreadConsumer.accept(thread, this);
        }
        thread.start();
    }

    protected void doDispatchLoop() {
        try {
            List<T> list = new ArrayList<>(64);
            int count = 0;
            boolean isFirst = true;
            while (true) {
                boolean hasElement = false;
                while (true) {
                    Map.Entry<T, Long> entry = queue.poll();
                    if (entry == null) {
                        break;
                    }
                    if (isFirst) {
                        if (waitSubscriber.get()) {
                            while (subscribers.isEmpty()) {
                                try {
                                    Thread.sleep(threadIdleMillSeconds.get());
                                } catch (Exception e) {

                                }
                            }
                        }
                        isFirst = false;
                    }

                    T item = entry.getKey();
                    long dropOverMillSeconds = this.dropOverMillSeconds.get();
                    if (dropOverMillSeconds > 0) {
                        Long ts = entry.getValue();
                        if (SystemClock.currentTimeMillis() - ts > dropOverMillSeconds) {
                            continue;
                        }
                    }

                    list.add(item);
                    hasElement = true;
                    count++;
                    if (count >= 64) {
                        break;
                    }
                }

                if (count > 0) {
                    for (T item : list) {
                        for (IEventSubscriber<T> subscriber : subscribers) {
                            if (subscriber.test(item)) {
                                pool.submit(new EventRunnable<T>(subscriber, item));
                            }
                        }
                    }
                    list.clear();
                    count = 0;
                }

                if (!hasElement) {
                    try {
                        Thread.sleep(threadIdleMillSeconds.get());
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            System.err.println("EventDispatcher dispatch event error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Data
    public static class EventRunnable<T> implements Runnable {
        protected IEventSubscriber<T> subscriber;
        protected T event;
        protected Throwable exception;
        protected CountDownLatch latch;

        public EventRunnable(IEventSubscriber<T> subscriber, T event) {
            this.subscriber = subscriber;
            this.event = event;
        }

        public EventRunnable(IEventSubscriber<T> subscriber, T event, CountDownLatch latch) {
            this.subscriber = subscriber;
            this.event = event;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                this.subscriber.handle(this.event);
            } catch (Exception e) {
                this.exception = exception;
            } finally {
                if (this.latch != null) {
                    this.latch.countDown();
                }
            }
        }
    }
}
