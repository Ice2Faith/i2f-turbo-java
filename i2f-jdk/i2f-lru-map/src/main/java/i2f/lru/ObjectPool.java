package i2f.lru;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/6 21:46
 * @desc
 */
public class ObjectPool<T> {
    private Supplier<T> supplier;
    private AtomicInteger maxCount = new AtomicInteger(300);

    private transient AtomicInteger count = new AtomicInteger(0);
    private transient ReadWriteLock lock = new ReentrantReadWriteLock();
    private transient LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public ObjectPool(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ObjectPool(Supplier<T> supplier,int maxCount) {
        this.supplier = supplier;
        this.maxCount.set(maxCount);
    }

    public void setMaxCount(int maxCount){
        this.maxCount.set(maxCount);
    }

    public void setSupplier(Supplier<T> supplier){
        lock.writeLock().lock();
        try{
            this.supplier=supplier;
            queue.clear();
            count.set(0);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public T require() {
        T ret = queue.poll();
        if (ret != null) {
            count.decrementAndGet();
            return ret;
        }
        if (count.get() > maxCount.get()) {
            return supplier.get();
        }
        lock.writeLock().lock();
        try {
            ret = supplier.get();
            queue.put(ret);
            count.incrementAndGet();
            return ret;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void release(T obj) {
        if (obj == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            queue.put(obj);
            count.incrementAndGet();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
