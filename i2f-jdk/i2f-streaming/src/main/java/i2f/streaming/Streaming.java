package i2f.streaming;

import i2f.streaming.impl.*;
import i2f.streaming.index.DecimalIndex;
import i2f.streaming.patten.StreamingPatten;
import i2f.streaming.timed.TimedStreaming;
import i2f.streaming.type.str.StringStreaming;
import i2f.streaming.window.ConditionWindowInfo;
import i2f.streaming.window.SlideWindowInfo;
import i2f.streaming.window.ViewWindowInfo;
import i2f.streaming.window.WindowInfo;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:30
 * @desc
 */
public interface Streaming<E> {

    static Streaming<Integer> ofInt(int begin, int step, int end) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            AtomicInteger curr = new AtomicInteger(begin);
            return new SupplierIterator<>(() -> {
                int ret = curr.getAndAdd(step);
                if (step > 0) {
                    if (ret > end) {
                        return Reference.finish();
                    }
                } else {
                    if (ret < end) {
                        return Reference.finish();
                    }
                }
                return Reference.of(ret);
            });
        }));
    }

    static Streaming<Integer> ofRandomInt(int count, int bound) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            AtomicInteger curr = new AtomicInteger(0);
            SecureRandom rand = new SecureRandom();
            return new SupplierIterator<>(() -> {
                int cnt = curr.getAndIncrement();
                if (cnt >= count) {
                    return Reference.finish();
                }
                return Reference.of(rand.nextInt(bound));
            });
        }));
    }

    static Streaming<Double> ofRandom(int count) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            AtomicInteger curr = new AtomicInteger(0);
            SecureRandom rand = new SecureRandom();
            return new SupplierIterator<>(() -> {
                int cnt = curr.getAndIncrement();
                if (cnt >= count) {
                    return Reference.finish();
                }
                return Reference.of(rand.nextDouble());
            });
        }));
    }

    static Streaming<File> ofDir(File dir) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            File[] arr = dir.listFiles();
            AtomicInteger idx = new AtomicInteger(0);
            return new SupplierIterator<>(() -> {
                if (idx.get() >= arr.length) {
                    return Reference.finish();
                }
                int i = idx.getAndIncrement();
                return Reference.of(arr[i]);
            });
        }));
    }

    static Streaming<Field> ofField(Class<?> clazz) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            Set<Field> fields = new LinkedHashSet<>();
            for (Field field : clazz.getFields()) {
                fields.add(field);
            }
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            return fields.iterator();
        }));
    }

    static Streaming<Method> ofMethod(Class<?> clazz) {
        return new StreamingImpl<>(new LazyIterator<>(() -> {
            Set<Method> methods = new LinkedHashSet<>();
            for (Method method : clazz.getMethods()) {
                methods.add(method);
            }
            for (Method method : clazz.getDeclaredMethods()) {
                methods.add(method);
            }
            return methods.iterator();
        }));
    }

    static <T> Streaming<T> ofGenerator(Consumer<Consumer<Reference<T>>> collector) {
        return new StreamingImpl<>(new GeneratorIterator<>(collector));
    }

    static <T> Streaming<T> of(Supplier<Reference<T>> supplier) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            return new SupplierIterator<>(supplier);
        }));
    }

    static <T> Streaming<T> of(Iterable<T> iterable) {
        return new StreamingImpl<>(new LazyIterator<T>(iterable::iterator));
    }

    static <K, V> Streaming<Map.Entry<K, V>> of(Map<K, V> map) {
        return new StreamingImpl<>(new LazyIterator<Map.Entry<K, V>>(map.entrySet().iterator()));
    }

    static <T> Streaming<T> of(Iterator<T> iterator) {
        return new StreamingImpl<>(iterator);
    }

    static <T> Streaming<T> of(Enumeration<T> enumeration) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            return new SupplierIterator<>(() -> {
                if (!enumeration.hasMoreElements()) {
                    return Reference.finish();
                }
                return Reference.of(enumeration.nextElement());
            });
        }));
    }

    static <T> Streaming<T> of(T... arr) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            AtomicInteger idx = new AtomicInteger(0);
            return new SupplierIterator<>(() -> {
                if (idx.get() >= arr.length) {
                    return Reference.finish();
                }
                int i = idx.getAndIncrement();
                return Reference.of(arr[i]);
            });
        }));
    }

    static <T> Streaming<T> of(Stream<T> stream) {
        return new StreamingImpl<>(new LazyIterator<T>(() -> {
            LinkedList<T> col = stream.collect(Collectors.toCollection(LinkedList::new));
            return col.iterator();
        }));
    }

    static Streaming<String> of(File file, String charset) {
        try {
            return of(new FileInputStream(file), charset);
        } catch (Exception e) {
            throw new IllegalStateException("open io exception", e);
        }
    }

    static Streaming<String> of(InputStream is, String charset) {
        try {
            return of(new InputStreamReader(is, charset));
        } catch (Exception e) {
            throw new IllegalStateException("convert io exception", e);
        }
    }

    static Streaming<String> of(Reader reader) {
        return new StreamingImpl<>(new ResourcesIterator<>(reader, (res, iter) -> {
            try {
                BufferedReader buffer = new BufferedReader(res);
                return new SupplierIterator<>(() -> {
                    String line = null;
                    try {
                        line = buffer.readLine();
                    } catch (Exception e) {
                        throw new IllegalStateException("read io exception", e);
                    }
                    if (line == null) {
                        return Reference.finish();
                    }
                    return Reference.of(line);
                });
            } catch (Exception e) {
                throw new IllegalStateException("initial io exception", e);
            }
        }, (res, iter) -> {
            try {
                res.close();
            } catch (Exception e) {
                throw new IllegalStateException("close io exception", e);
            }
        }));
    }

    static Streaming<Map<String, Object>> of(PreparedStatement stat) {
        Map.Entry<PreparedStatement, ResultSet> resource = new AbstractMap.SimpleEntry<>(stat, null);
        return new StreamingImpl<>(new ResourcesIterator<>(resource, (res, iter) -> {
            try {
                if (res.getValue() == null) {
                    res.setValue(res.getKey().executeQuery());
                }
                ResultSet rs = res.getValue();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                List<String> columnNames = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    String name = metaData.getColumnLabel(i + 1);
                    if (name == null) {
                        name = metaData.getColumnName(i + 1);
                    }
                    columnNames.add(name);
                }
                return new SupplierIterator<>(() -> {
                    Map<String, Object> row = null;
                    try {
                        if (rs.next()) {
                            row = new LinkedHashMap<>();
                            for (int i = 0; i < columnCount; i++) {
                                Object val = rs.getObject(i + 1);
                                row.put(columnNames.get(i), val);
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("read rs exception", e);
                    }
                    if (row == null) {
                        return Reference.finish();
                    }
                    return Reference.of(row);
                });
            } catch (Exception e) {
                throw new IllegalStateException("initial rs exception", e);
            }
        }, (res, iter) -> {
            try {
                if (res.getValue() != null) {
                    res.getValue().close();
                }
                res.getKey().close();
            } catch (Exception e) {
                throw new IllegalStateException("close rs exception", e);
            }
        }));
    }

    static Streaming<Map<String, Object>> of(ResultSet rs) {
        return new StreamingImpl<>(new ResourcesIterator<>(rs, (res, iter) -> {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                List<String> columnNames = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    String name = metaData.getColumnLabel(i + 1);
                    if (name == null) {
                        name = metaData.getColumnName(i + 1);
                    }
                    columnNames.add(name);
                }
                return new SupplierIterator<>(() -> {
                    Map<String, Object> row = null;
                    try {
                        if (rs.next()) {
                            row = new LinkedHashMap<>();
                            for (int i = 0; i < columnCount; i++) {
                                Object val = rs.getObject(i + 1);
                                row.put(columnNames.get(i), val);
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("read rs exception", e);
                    }
                    if (row == null) {
                        return Reference.finish();
                    }
                    return Reference.of(row);
                });
            } catch (Exception e) {
                throw new IllegalStateException("initial rs exception", e);
            }
        }, (res, iter) -> {
            try {
                res.close();
            } catch (Exception e) {
                throw new IllegalStateException("close rs exception", e);
            }
        }));
    }

    static <T> int defaultComparator(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1 instanceof Comparable) {
            Comparable c1 = (Comparable) o1;
            Comparable c2 = (Comparable) o2;
            return c1.compareTo(c2);
        }
        return Long.compare(o1.hashCode(), o2.hashCode());
    }

    Streaming<E> parallel();

    Streaming<E> sequence();

    Streaming<E> pool(ExecutorService pool);

    Streaming<E> defaultPool();

    Streaming<E> pool(int size);

    Streaming<E> parallelism(int count);

    <R> Streaming<R> process(BiConsumer<E, Consumer<R>> mapper);

    <R> Streaming<R> process(Function<Iterator<E>, Iterator<R>> process);

    Streaming<Map.Entry<E, Long>> indexed();

    Streaming<E> filter(Predicate<E> filter);

    default Streaming<E> notNull() {
        return filter(Objects::nonNull);
    }

    Streaming<E> topN(int size, BiPredicate<E, E> replacer);

    default Streaming<E> maxN(int size, Comparator<E> comparator) {
        return topN(size, (val, old) -> {
            return comparator.compare(val, old) >= 0;
        });
    }

    default Streaming<E> maxN(int size) {
        return maxN(size, Streaming::defaultComparator);
    }

    default Streaming<E> minN(int size, Comparator<E> comparator) {
        return topN(size, (val, old) -> {
            return comparator.compare(val, old) < 0;
        });
    }

    default Streaming<E> minN(int size) {
        return minN(size, Streaming::defaultComparator);
    }

    Streaming<E> afterAll(Predicate<E> filter);

    Streaming<E> beforeAll(Predicate<E> filter);

    Streaming<E> afterN(int size, Predicate<E> filter, int limit);

    default Streaming<E> afterN(int size, Predicate<E> filter) {
        return afterN(size, filter, 1);
    }

    Streaming<E> beforeN(int size, Predicate<E> filter, int limit);

    default Streaming<E> beforeN(int size, Predicate<E> filter) {
        return beforeN(size, filter, 1);
    }

    default Streaming<E> rangeAll(Predicate<E> beginFilter, Predicate<E> endFilter) {
        return rangeAll(beginFilter, endFilter, true, false);
    }

    Streaming<E> rangeAll(Predicate<E> beginFilter, Predicate<E> endFilter, boolean includeBegin, boolean includeEnd);

    default Streaming<E> dropRange(Predicate<E> beginFilter, Predicate<E> endFilter) {
        return dropRange(beginFilter, endFilter, true, false);
    }

    Streaming<E> dropRange(Predicate<E> beginFilter, Predicate<E> endFilter, boolean includeBegin, boolean includeEnd);

    <R> Streaming<R> map(Function<E, R> mapper);

    default <R> Streaming<R> mapNonNull(Function<E, R> mapper) {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return mapper.apply(e);
        });
    }

    <R> Streaming<R> flatMap(BiConsumer<E, Consumer<R>> collector);

    default <R> Streaming<R> flatMapArray(Function<E, R[]> mapper) {
        return flatMap((e, collector) -> {
            R[] arr = mapper.apply(e);
            if (arr != null) {
                for (R item : arr) {
                    collector.accept(item);
                }
            }
        });
    }

    default Streaming<Integer> flatMapIntArray(Function<E, int[]> mapper) {
        return flatMap((e, collector) -> {
            int[] arr = mapper.apply(e);
            if (arr != null) {
                for (int item : arr) {
                    collector.accept(item);
                }
            }
        });
    }

    default Streaming<Long> flatMapLongArray(Function<E, long[]> mapper) {
        return flatMap((e, collector) -> {
            long[] arr = mapper.apply(e);
            if (arr != null) {
                for (long item : arr) {
                    collector.accept(item);
                }
            }
        });
    }

    default Streaming<Character> flatMapCharArray(Function<E, char[]> mapper) {
        return flatMap((e, collector) -> {
            char[] arr = mapper.apply(e);
            if (arr != null) {
                for (char item : arr) {
                    collector.accept(item);
                }
            }
        });
    }

    default <R> Streaming<R> flatMapIterable(Function<E, Iterable<R>> mapper) {
        return flatMap((e, collector) -> {
            Iterable<R> arr = mapper.apply(e);
            if (arr != null) {
                for (R item : arr) {
                    collector.accept(item);
                }
            }
        });
    }

    Streaming<E> recursive(BiConsumer<E, Consumer<E>> collector);

    <R> Streaming<R> recursiveMap(BiConsumer<E, Consumer<R>> initCollector, BiConsumer<R, Consumer<R>> recursiveCollector);

    Streaming<E> skip(long count);

    Streaming<E> limit(long count);

    Streaming<E> tail(int count);

    default Streaming<E> sort() {
        return sort(true);
    }

    default Streaming<E> sort(boolean asc) {
        Comparator<E> comparator = Streaming::defaultComparator;
        if (!asc) {
            comparator = comparator.reversed();
        }
        return sort(comparator);
    }

    Streaming<E> sort(Comparator<E> comparator);

    Streaming<E> reverse();

    Streaming<E> shuffle();

    Streaming<E> distinct();

    Streaming<E> sample(double rate);

    Streaming<E> sampleCount(int count);

    Streaming<E> peek(Consumer<E> consumer);

    default Streaming<E> print() {
        return print(null);
    }

    Streaming<E> print(String prefix);

    Streaming<E> merge(Streaming<E> streaming);

    <T> Streaming<E> merge(Streaming<T> streaming, Function<T, E> mapper);

    Streaming<E> mixed(Streaming<E> streaming);

    Streaming<E> include(Streaming<E> streaming);

    Streaming<E> exclude(Streaming<E> streaming);

    <K> Streaming<Map.Entry<K, Streaming<E>>> keyBy(Function<E, K> keySupplier);

    <K, R> Streaming<Map.Entry<K, R>> keyBy(Function<E, K> keySupplier, Function<Streaming<E>, R> finishMapper);

    <K, V extends Collection<E>> Streaming<Map.Entry<K, V>> keyBy(Supplier<V> containerSupplier,
                                                                  Function<E, K> keySupplier);

    Streaming<Map.Entry<E, Long>> countBy();

    <K> Streaming<Map.Entry<K, Long>> countBy(Function<E, K> keySupplier);

    Streaming<Map.Entry<List<E>, ViewWindowInfo>> viewWindow(int beforeCount, int afterCount);

    Streaming<Map.Entry<List<E>, SlideWindowInfo>> slideWindow(int windowSize, int slideCount);

    default Streaming<Map.Entry<List<E>, SlideWindowInfo>> countWindow(int windowSize) {
        return slideWindow(windowSize, windowSize);
    }

    <R> Streaming<Map.Entry<List<E>, ConditionWindowInfo<R>>> conditionWindow(Supplier<R> initConditionSupplier,
                                                                              Function<E, R> currentConditionMapper,
                                                                              BiPredicate<R, R> conditionChangePredicater);

    Streaming<Map.Entry<List<E>, WindowInfo>> pattenWindow(StreamingPatten<E> patten);

    default Streaming<Map.Entry<List<E>, WindowInfo>> pattenWindow(Function<StreamingPatten<E>, StreamingPatten<E>> consumer) {
        StreamingPatten<E> patten = StreamingPatten.begin();
        patten = consumer.apply(patten);
        return pattenWindow(patten.end());
    }

    default Streaming<Map.Entry<E, BigDecimal>> latelyAverage(int latelyCount,
                                                              int scale,
                                                              Function<E, BigDecimal> valueMapper) {
        return latelyAggregate(latelyCount,
                () -> new SimpleEntry<BigDecimal, BigDecimal>(BigDecimal.ZERO, BigDecimal.ZERO),
                (t, e) -> {
                    t.setKey(t.getKey().add(valueMapper.apply(e)));
                    t.setValue(t.getValue().add(BigDecimal.ONE));
                    return t;
                }, (e) -> e.getKey().divide(e.getValue(), scale, RoundingMode.HALF_UP));
    }

    default Streaming<Map.Entry<E, DecimalIndex>> latelyIndex(int latelyCount,
                                                              int scale,
                                                              Function<E, BigDecimal> valueMapper) {
        return latelyAggregate(latelyCount,
                () -> {
                    DecimalIndex index = new DecimalIndex();
                    index.setAvg(BigDecimal.ZERO);
                    index.setCount(BigDecimal.ZERO);
                    index.setSum(BigDecimal.ZERO);
                    return index;
                },
                (t, e) -> {
                    BigDecimal val = valueMapper.apply(e);
                    t.setSum(t.getSum().add(val));
                    t.setCount(t.getCount().add(BigDecimal.ONE));
                    if (t.getMin() == null) {
                        t.setMin(val);
                    } else {
                        t.setMin(t.getMin().min(val));
                    }
                    if (t.getMax() == null) {
                        t.setMax(val);
                    } else {
                        t.setMax(t.getMax().max(val));
                    }
                    return t;
                }, (e) -> {
                    e.setAvg(e.getSum().divide(e.getCount(), scale, RoundingMode.HALF_UP));
                    return e;
                });
    }

    <T, R> Streaming<Map.Entry<E, R>> latelyAggregate(int latelyCount,
                                                      Supplier<T> firstSupplier,
                                                      BiFunction<T, E, T> accumulator,
                                                      Function<T, R> finisher);

    <T> Streaming<Map.Entry<E, T>> connect(Streaming<T> other);

    <T> Streaming<Map.Entry<E, T>> join(Streaming<T> other, BiPredicate<E, T> conditional);

    <K, T> Streaming<Map.Entry<E, T>> join(Streaming<T> other, Function<E, K> leftKeySupplier, Function<T, K> rightKeySupplier);

    <R> R collect(Function<Iterator<E>, R> mapper);

    Iterator<E> iterator();

    <R extends Collection<E>> R toCollection(R collection);

    default <R extends Collection<E>> R toCollection(Supplier<R> collectionSupplier) {
        return toCollection(collectionSupplier.get());
    }

    <K, V, R extends Map<K, V>> R toMap(R map, Function<E, K> keySupplier,
                                        Function<E, V> valueSupplier,
                                        BiFunction<V, V, V> valueSelector);

    default <K, V, R extends Map<K, V>> R toMap(Supplier<R> mapSupplier,
                                                Function<E, K> keySupplier,
                                                Function<E, V> valueSupplier,
                                                BiFunction<V, V, V> valueSelector) {
        return toMap(mapSupplier.get(), keySupplier, valueSupplier, valueSelector);
    }

    <K, V extends Collection<E>, R extends Map<K, V>> Map.Entry<V, R> toGroup(R map,
                                                                              Supplier<V> containerSupplier,
                                                                              Function<E, K> keySupplier);

    default <K, V extends Collection<E>, R extends Map<K, V>> Map.Entry<V, R> toGroup(Supplier<R> mapSupplier,
                                                                                      Supplier<V> containerSupplier,
                                                                                      Function<E, K> keySupplier) {
        return toGroup(mapSupplier.get(), containerSupplier, keySupplier);
    }

    Reference<E> first();

    Reference<E> last();

    Reference<E> first(Predicate<E> filter);

    Reference<E> last(Predicate<E> filter);

    Streaming<E> firstN(int size, Predicate<E> filter);

    Streaming<E> lastN(int size, Predicate<E> filter);

    boolean anyMatch(Predicate<E> filter);

    boolean allMatch(Predicate<E> filter);

    Reference<E> min(Comparator<E> comparator);

    default Reference<E> min() {
        return min(Streaming::defaultComparator);
    }

    Reference<E> max(Comparator<E> comparator);

    default Reference<E> max() {
        return max(Streaming::defaultComparator);
    }

    Reference<E> most();

    <K> Reference<Map.Entry<K, E>> most(Function<E, K> keySupplier);

    Reference<E> least();

    <K> Reference<Map.Entry<K, E>> least(Function<E, K> keySupplier);

    Reference<E> reduce(Supplier<E> firstSupplier,
                        BiFunction<E, E, E> accumulator);

    <T, R> Reference<R> aggregate(Supplier<T> firstSupplier,
                                  BiFunction<T, E, T> accumulator,
                                  Function<T, R> finisher);

    long count();

    void forEach(Consumer<E> consumer);

    void forEach(BiConsumer<E, Long> consumer);

    default void sysout() {
        sysout(null);
    }

    void sysout(String prefix);

    default void broadcast(BiConsumer<E, Long>... consumers) {
        broadcast(Arrays.asList(consumers));
    }

    void broadcast(Collection<BiConsumer<E, Long>> consumers);

    default void ring(BiConsumer<E, Long>... consumers) {
        ring(Arrays.asList(consumers));
    }

    void ring(Collection<BiConsumer<E, Long>> consumers);

    default void random(BiConsumer<E, Long>... consumers) {
        random(Arrays.asList(consumers));
    }

    void random(Collection<BiConsumer<E, Long>> consumers);

    <C extends Collection<E>> void batch(int batchSize, Supplier<C> containerSupplier, Consumer<C> consumer);

    default void batch(int batchSize, Consumer<List<E>> consumer) {
        batch(batchSize, () -> new ArrayList<>(batchSize), consumer);
    }

    default String stringify(String separator) {
        return stringify(null, separator, null);
    }

    String stringify(String prefix, String separator, String suffix);

    default void toFile(File file, String charset) throws IOException {
        toFile(file, charset, null, "\n", null);
    }

    default void toFile(File file, String charset, String prefix, String separator, String suffix) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        toStream(fos, charset, prefix, separator, suffix);
        fos.flush();
        fos.close();
    }

    default void toStream(OutputStream os, String charset) throws IOException {
        toStream(os, charset, null, "\n", null);
    }

    default void toStream(OutputStream os, String charset, String prefix, String separator, String suffix) throws IOException {
        OutputStreamWriter ow = new OutputStreamWriter(os, charset);
        toWriter(ow, prefix, separator, suffix);
        ow.flush();
        ow.close();
    }

    default void toWriter(Writer writer) throws IOException {
        toWriter(writer, null, "\n", null);
    }

    void toWriter(Writer writer, String prefix, String separator, String suffix) throws IOException;

    TimedStreaming<E> timed(Function<E, Long> timestampMapper);

    StringStreaming string(Function<E, String> stringifier);

    default StringStreaming string() {
        return string(e -> {
            if (e == null) {
                return null;
            }
            if (e instanceof String) {
                return (String) e;
            }
            return String.valueOf(e);
        });
    }
}
