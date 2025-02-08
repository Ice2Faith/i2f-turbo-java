package i2f.pool.segment;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/12 20:30
 * @desc 分段锁
 * 通过给定Object获取对应分段的锁对象
 * 使用时，需要指定锁的构造器
 */
public class SegmentLock<T extends Lock> extends SegmentObjectProvider<T> {
    public SegmentLock(Supplier<T> supplier) {
        super(supplier);
    }

    public SegmentLock(int segmentSize, Supplier<T> supplier) {
        super(segmentSize, supplier);
    }

    public T getLock(Object object) {
        return getSegmentObject(object);
    }

    public void lock(Object object) {
        Lock lock = getSegmentObject(object);
        lock.lock();
    }

    public void unlock(Object object) {
        Lock lock = getSegmentObject(object);
        lock.unlock();
    }
}
