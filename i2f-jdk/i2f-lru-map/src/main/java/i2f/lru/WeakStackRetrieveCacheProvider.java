package i2f.lru;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/1/8 14:46
 * @desc
 * 适用于在递归调用或者使用统一上下文参数的场景中，避免重复对参数进行准备/检验/转换等
 * 采用L1+L2两级缓存，对参数的转换操作结果进行缓存
 * 特别是在递归调用或者ABAB等交叉调用时，每个函数都需要校验参数的合理性，进行初始化，检验，预处理等耗时的操作
 * 比如：校验参数对象是否完整，检查缺失的参数进行补充
 * 这时候，使用本类进行代理之后，能够最大程度的避免对相同的参数进行初始化，检验，预处理等操作，而是直接从缓存获取结果
 * 原理是：
 * 对输入的参数进行同对象的线程比较，如果线程比较为通对象，且有缓存结果，则直接返回
 */
public class WeakStackRetrieveCacheProvider<K, V> implements Function<K,V> {
    protected final AtomicBoolean l1Cache = new AtomicBoolean(true);
    protected final AtomicBoolean l2Cache = new AtomicBoolean(true);
    protected final AtomicInteger l2CacheSize = new AtomicInteger(30);
    protected final ThreadLocal<WeakEntry<K, V>> localCacheL1 = new ThreadLocal<>();
    protected final ThreadLocal<LinkedList<WeakEntry<K, V>>> localCacheL2 = new ThreadLocal<>();
    protected final ThreadLocalRandom random = ThreadLocalRandom.current();
    protected Function<K, V> wrapper;

    protected final List<WeakEntry<K,V>> history=new LinkedList<>();

    public WeakStackRetrieveCacheProvider(Function<K, V> wrapper){
        this.wrapper=wrapper;
    }

    public static<K,V> WeakStackRetrieveCacheProvider<K,V> of(Function<K,V> wrapper){
        return new WeakStackRetrieveCacheProvider<>(wrapper);
    }

    public static<V> Consumer<V> of(Consumer<V> consumer){
        WeakStackRetrieveCacheProvider<V, Boolean> provider = new WeakStackRetrieveCacheProvider<>((k) -> {
            consumer.accept(k);
            return true;
        });
        return k->{
            Boolean ok = provider.apply(k);
        };
    }

    public WeakStackRetrieveCacheProvider<K,V> withL1Cache(boolean enable){
        this.l1Cache.set(enable);
        return this;
    }

    public WeakStackRetrieveCacheProvider<K,V> withL2Cache(boolean enable){
        this.l2Cache.set(enable);
        return this;
    }

    public WeakStackRetrieveCacheProvider<K,V> withL2CacheSize(int l2CacheSize){
        this.l2CacheSize.set(l2CacheSize);
        return this;
    }


    @Override
    public V apply(K params) {
        if (l1Cache.get()) {
            WeakEntry<K, V> ref = localCacheL1.get();
            if (ref != null) {
                K refMap = ref.getKey();
                if (refMap != null) {
                    if (refMap == params) {
                        // 这里必须使用 == 判断是否是同一个对象，不能使用equals
                        return ref.getValue();
                    }
                }
            }
        }
        if (l2Cache.get()) {
            LinkedList<WeakEntry<K, V>> localList = localCacheL2.get();
            if (localList != null) {
                Iterator<WeakEntry<K, V>> iterator = localList.iterator();
                while (iterator.hasNext()) {
                    WeakEntry<K, V> ref = iterator.next();
                    if (ref == null) {
                        iterator.remove();
                        continue;
                    }
                    // 判断是否已经prepared，如果已经prepared,则不需要再进行prepared浪费性能
                    K refMap = ref.getKey();
                    if (refMap == null) {
                        iterator.remove();
                        continue;
                    }
                    if (refMap == params) {
                        // 这里必须使用 == 判断是否是同一个对象，不能使用equals
                        if (random.nextDouble() < 0.3) {
                            iterator.remove();
                            localList.addFirst(ref);
                        }
                        return ref.getValue();
                    }

                }

            }
        }

        V ret = wrapper.apply(params);
        if(ret==params){
            throw new IllegalArgumentException("wrapper cannot return the input argument! because of will cause strong reference by key util jvm oom!");
        }

        WeakEntry<K, V> refCache = new WeakEntry<>(params, ret);
        history.add(refCache);
        if (l1Cache.get()) {
            localCacheL1.set(refCache);
        }

        if (l2Cache.get()) {
            LinkedList<WeakEntry<K, V>> localList = localCacheL2.get();
            if (localList == null) {
                localList = new LinkedList<>();
            }
            Iterator<WeakEntry<K, V>> localIterator = localList.iterator();
            while (localIterator.hasNext()) {
                WeakEntry<K, V> ref = localIterator.next();
                if (ref == null || ref.get() == null) {
                    localIterator.remove();
                }
            }
            while (localList.size() > l2CacheSize.get()) {
                localList.removeLast();
            }
            localList.addFirst(refCache);
            localCacheL2.set(localList);
        }

        return ret;
    }

}
