package i2f.pool.segment;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/12 20:30
 * @desc 分段互斥锁
 */
public class SegmentReentrantLock extends SegmentLock<ReentrantLock> {
    public static final Supplier<ReentrantLock> LOCK_SUPPLIER = ReentrantLock::new;

    public SegmentReentrantLock() {
        super(LOCK_SUPPLIER);
    }

    public SegmentReentrantLock(int segmentSize) {
        super(segmentSize, LOCK_SUPPLIER);
    }
}
