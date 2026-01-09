package i2f.lru;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/1/8 14:46
 * @desc 适用于在递归调用或者使用统一上下文参数的场景中，避免重复对参数进行准备/检验/转换等
 * 采用L1+L2两级缓存，对参数的转换操作结果进行缓存
 * 特别是在递归调用或者ABAB等交叉调用时，每个函数都需要校验参数的合理性，进行初始化，检验，预处理等耗时的操作
 * 比如：校验参数对象是否完整，检查缺失的参数进行补充
 * 这时候，使用本类进行代理之后，能够最大程度的避免对相同的参数进行初始化，检验，预处理等操作，而是直接从缓存获取结果
 * 请注意，使用此类的前提是转换具有幂等性，对于输入参数 T 经过多次转换之后，总能够得到功能一致的结果 R，则可以使用本类进行包装
 * 否则，不能使用此类进行包装
 * 原理是：
 * 对输入的参数进行同对象的线程比较，如果线程比较为通对象，且有缓存结果，则直接返回
 * 泛型参数说明：
 * T 缓存对比的对象，也就是输入参数
 * U 在构建或者初始化T对象中使用到的辅助性工具，注意是工具，也就是不要把这些参数作为T的组成部分
 * R 构建的产物，也就是转换的结果
 */
public class WeakStackRetrieveCacheProvider<T, U, R> implements BiFunction<T, U, R> {
    protected final AtomicBoolean l1Cache = new AtomicBoolean(true);
    protected final AtomicBoolean l2Cache = new AtomicBoolean(true);
    protected final AtomicInteger l2CacheSize = new AtomicInteger(30);
    protected final ThreadLocal<WeakEntry<T, R>> localCacheL1 = new ThreadLocal<>();
    protected final ThreadLocal<LinkedList<WeakEntry<T, R>>> localCacheL2 = new ThreadLocal<>();
    protected final ThreadLocalRandom random = ThreadLocalRandom.current();
    protected BiFunction<T, U, R> wrapper;

    public WeakStackRetrieveCacheProvider(BiFunction<T, U, R> wrapper) {
        this.wrapper = wrapper;
    }

    public static <K, U, V> WeakStackRetrieveCacheProvider<K, U, V> of(BiFunction<K, U, V> wrapper) {
        return new WeakStackRetrieveCacheProvider<>(wrapper);
    }

    public static <K, V> Function<K, V> of(Function<K, V> wrapper) {
        return of(wrapper, null);
    }

    public static <K, V> Function<K, V> of(Function<K, V> wrapper,
                                           Consumer<WeakStackRetrieveCacheProvider<K, Object, V>> initializer) {
        WeakStackRetrieveCacheProvider<K, Object, V> provider = new WeakStackRetrieveCacheProvider<>((k, u) -> {
            return wrapper.apply(k);
        });
        if (initializer != null) {
            initializer.accept(provider);
        }
        return (k) -> {
            return provider.apply(k, null);
        };
    }

    public static <V> Consumer<V> of(Consumer<V> consumer) {
        return of(consumer, null);
    }

    public static <V> Consumer<V> of(Consumer<V> consumer,
                                     Consumer<WeakStackRetrieveCacheProvider<V, Object, Boolean>> initializer) {
        WeakStackRetrieveCacheProvider<V, Object, Boolean> provider = new WeakStackRetrieveCacheProvider<>((k, u) -> {
            consumer.accept(k);
            return true;
        });
        if (initializer != null) {
            initializer.accept(provider);
        }
        return k -> {
            Boolean ok = provider.apply(k, null);
        };
    }

    public static <V, U> BiConsumer<V, U> of(BiConsumer<V, U> consumer) {
        return of(consumer, null);
    }

    public static <V, U> BiConsumer<V, U> of(BiConsumer<V, U> consumer,
                                             Consumer<WeakStackRetrieveCacheProvider<V, U, Boolean>> initializer) {
        WeakStackRetrieveCacheProvider<V, U, Boolean> provider = new WeakStackRetrieveCacheProvider<>((k, u) -> {
            consumer.accept(k, u);
            return true;
        });
        if (initializer != null) {
            initializer.accept(provider);
        }
        return (v, u) -> {
            Boolean ok = provider.apply(v, u);
        };
    }

    public WeakStackRetrieveCacheProvider<T, U, R> withL1Cache(boolean enable) {
        this.l1Cache.set(enable);
        return this;
    }

    public WeakStackRetrieveCacheProvider<T, U, R> withL2Cache(boolean enable) {
        this.l2Cache.set(enable);
        return this;
    }

    public WeakStackRetrieveCacheProvider<T, U, R> withL2CacheSize(int l2CacheSize) {
        this.l2CacheSize.set(l2CacheSize);
        return this;
    }

    @Override
    public R apply(T params, U args) {
        if (l1Cache.get()) {
            WeakEntry<T, R> ref = localCacheL1.get();
            if (ref != null) {
                T refMap = ref.getKey();
                if (refMap != null) {
                    if (refMap == params) {
                        // 这里必须使用 == 判断是否是同一个对象，不能使用equals
                        return ref.getValue();
                    }
                }
            }
        }
        if (l2Cache.get()) {
            LinkedList<WeakEntry<T, R>> localList = localCacheL2.get();
            if (localList != null) {
                Iterator<WeakEntry<T, R>> iterator = localList.iterator();
                while (iterator.hasNext()) {
                    WeakEntry<T, R> ref = iterator.next();
                    if (ref == null) {
                        iterator.remove();
                        continue;
                    }
                    // 判断是否已经prepared，如果已经prepared,则不需要再进行prepared浪费性能
                    T refMap = ref.getKey();
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

        R ret = wrapper.apply(params, args);
        if (ret == params) {
            throw new IllegalArgumentException("wrapper cannot return the input argument! because of will cause strong reference by key util jvm oom!");
        }

        WeakEntry<T, R> refCache = new WeakEntry<>(params, ret);
        if (l1Cache.get()) {
            localCacheL1.set(refCache);
        }

        if (l2Cache.get()) {
            LinkedList<WeakEntry<T, R>> localList = localCacheL2.get();
            if (localList == null) {
                localList = new LinkedList<>();
            }
            Iterator<WeakEntry<T, R>> localIterator = localList.iterator();
            while (localIterator.hasNext()) {
                WeakEntry<T, R> ref = localIterator.next();
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
