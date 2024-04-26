package i2f.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/1/28 8:44
 * @desc 提供异步处理能力
 * ----------------------------------------
 * 一般用于将多线程中的任务合并到此队列中进行单线程串行队列化处理
 * 使用示例：
 * 使用instance方法实例化一个queue
 * <p>
 * 使用handle方法注册队列元素的消费者
 * <p>
 * 使用commit方法提交元素给队列
 */
public class AsyncQueue<E, Q extends BlockingQueue<E>> {
    private AtomicInteger handlerCount = new AtomicInteger(0);
    private AtomicInteger consumerCount = new AtomicInteger(0);

    private volatile Q queue;

    public AsyncQueue(Q queue) {
        this.queue = queue;
    }


    public static <T> AsyncQueue<T, LinkedBlockingQueue<T>> instance() {
        return new AsyncQueue<>(new LinkedBlockingQueue<>());
    }

    public int consumerCount() {
        return consumerCount.get();
    }

    public int handlerCount() {
        return handlerCount.get();
    }

    public synchronized void handle(String name, Consumer<E> consumer) {
        String threadName = "async-queue-handler";
        if (name != null) {
            threadName = name + "-" + "aqh";
        }
        threadName = threadName + "-" + handlerCount.incrementAndGet();
        consumerCount.incrementAndGet();
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    double sleepTs = 1;
                    try {
                        E elem = queue.poll();
                        if (elem != null) {
                            consumer.accept(elem);
                            sleepTs = 1;
                        } else {
                            sleepTs *= 1.1;
                            sleepTs = sleepTs > 300 ? 300 : sleepTs;
                            sleepTs = sleepTs < 1 ? 1 : sleepTs;
                            Thread.sleep((int) sleepTs);
                        }
                    } catch (Throwable e) {
                        System.err.println("异步处理队里处理异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } finally {
                consumerCount.decrementAndGet();
            }
        }, threadName);
        thread.start();
    }

    public void submit(E elem) {
        try {
            queue.put(elem);
        } catch (Exception e) {
            System.err.println("异步处理队列提交异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

}
