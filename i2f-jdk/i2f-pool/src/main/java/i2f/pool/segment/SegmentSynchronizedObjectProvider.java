package i2f.pool.segment;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/12 20:07
 * @desc 分段锁同步对象提供器
 * 使用JDK对String类型的不可变型intern方法
 * 针对字面值相同的字符串，返回的引用对象地址都相同，是同一个对象
 * 原理是JVM层面对String进行了全局的缓存，只要字面值相同，则缓存对象相同
 * 但是，如果不正确的使用intern方法，导致大量的String驻留在全局缓存中
 * 则会导致OOM等严重的问题
 * 因此，这里使用hash的思想，利用intern的特性
 * 对hash码进行intern
 * 则对任意对象计算hashCode之后，映射为一个固定的hash码intern对象
 * 则，相同对象获取到的锁对象就是相同的
 * 则就可以使用 synchronized 关键字对这个锁对象进行同步
 * 则实现了基于hash的分段锁
 */
public class SegmentSynchronizedObjectProvider {
    public static final SegmentSynchronizedObjectProvider INSTANCE = new SegmentSynchronizedObjectProvider();
    private final int segmentSize;

    public SegmentSynchronizedObjectProvider() {
        this(1024);
    }

    public SegmentSynchronizedObjectProvider(int segmentSize) {
        assert segmentSize > 0;
        this.segmentSize = segmentSize;
    }

    public String getSynchronizedObject(Object key) {
        int code = Math.abs(Objects.hashCode(key)) % segmentSize;
        String hash = String.valueOf(code);
        return hash.intern();
    }

    public void synchronize(Object key, Runnable runnable) {
        synchronized (getSynchronizedObject(key)) {
            runnable.run();
        }
    }

    public <V> V synchronize(Object key, Supplier<V> callable) {
        synchronized (getSynchronizedObject(key)) {
            return callable.get();
        }
    }

    public <R, T> R synchronize(Object key, Function<T, R> function, T arg) {
        synchronized (getSynchronizedObject(key)) {
            return function.apply(arg);
        }
    }

}
