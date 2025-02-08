package i2f.pool.segment;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/8/12 20:25
 * @desc 分段对象提供器
 * 根据分段的思想，提供一种，每个分段公用一个对象的策略
 * 支持segmentSize个分段
 * 分段使用hashCode进行确定
 * 实现hashCode相同的对象，能够被划分到同一个分段
 * 从而使用同一个分段对象
 */
public class SegmentObjectProvider<T> {
    private final int segmentSize;
    private final Supplier<T> supplier;
    private final ConcurrentHashMap<String, T> objectMap = new ConcurrentHashMap<>();

    public SegmentObjectProvider(Supplier<T> supplier) {
        this(1024, supplier);
    }

    public SegmentObjectProvider(int segmentSize, Supplier<T> supplier) {
        assert segmentSize > 0;
        this.segmentSize = segmentSize;
        this.supplier = supplier;
    }

    /**
     * 获取指定对象在segmentSize上的hash值
     *
     * @param key
     * @return
     */
    public String getObjectHash(Object key) {
        int code = Math.abs(Objects.hashCode(key)) % segmentSize;
        String hash = String.valueOf(code);
        return hash;
    }

    public T getSegmentObject(Object object) {
        String key = getObjectHash(object);
        return objectMap.computeIfAbsent(key, k -> supplier.get());
    }
}
