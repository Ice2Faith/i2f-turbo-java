package i2f.text;

import i2f.iterator.iterator.Iterators;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2022/5/7 16:43
 * @desc
 */
public class Appender<T extends Appendable> {
    protected volatile T appender;

    public static final Predicate<?> DEFAULT_FILTER = (val) -> {
        if (val == null) {
            return false;
        }
        if (val instanceof String) {
            return !((String) val).isEmpty();
        }
        if (val instanceof Collection) {
            return !((Collection) val).isEmpty();
        }
        if (val instanceof Map) {
            return !((Map) val).isEmpty();
        }
        Class<?> clazz = val.getClass();
        if (clazz.isArray()) {
            return Array.getLength(val) > 0;
        }
        return val != null;
    };

    public Appender(T appender) {
        this.appender = appender;
    }

    public static Appender<StringBuffer> buffer() {
        return new Appender<>(new StringBuffer());
    }

    public static Appender<StringBuilder> builder() {
        return new Appender<>(new StringBuilder());
    }

    public static String str(Object... objs) {
        return builder().adds(objs).get();
    }

    public static String sepStr(Object separator, Object... objs) {
        return builder().addsSep(separator, objs).get();
    }

    public static Appender<StringBuffer> buffer(Appender<?> val) {
        return buffer().add(val.get());
    }

    public static Appender<StringBuilder> builder(Appender<?> val) {
        return builder().add(val.get());
    }

    public static Appender<StringBuffer> buffer(Supplier<Appender<?>> caller) {
        return buffer().add(caller.get());
    }

    public static Appender<StringBuilder> builder(Supplier<Appender<?>> caller) {
        return builder().add(caller.get());
    }

    public Appender<T> add(Supplier<Appender<?>> val) {
        if (val == null) {
            return this;
        }
        Appender<?> appender = val.get();
        return add(appender);
    }

    public Appender<T> add(Appender<?> val) {
        if (val == null) {
            return this;
        }
        String str = val.get();
        return add(str);
    }

    ////////////////////////////////////////////////////
    public Appender<T> set(Object obj) {
        clear();
        return add(obj);
    }

    public String build() {
        return appender.toString();
    }

    public String get() {
        return build();
    }

    @Override
    public String toString() {
        return get();
    }

    public Appender<T> clear() {
        return trunc(0);
    }

    public static <T> Predicate<T> defaultFilter() {
        return (Predicate<T>) DEFAULT_FILTER;
    }

    public static <T> List<T> $arr2list(T[] arr) {
        List<T> ret = new ArrayList<>();
        if (arr == null) {
            return ret;
        }
        for (T item : arr) {
            ret.add(item);
        }
        return ret;
    }

    public static <T> List<T> $one2list(T obj) {
        List<T> ret = new ArrayList<>();
        if (obj == null) {
            return ret;
        }
        ret.add(obj);
        return ret;
    }


    public <E> Appender<T> $if(E val, Predicate<E> filter, Function<E, Appender<?>> caller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        return add(caller.apply(val));
    }

    public <E> Appender<T> $if(E val, Function<E, Appender<?>> caller) {
        return $if(val, defaultFilter(), caller);
    }


    public <E> Appender<T> $trim(T val, Predicate<T> filter,
                                 String trimPrefixes, String trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<T, Appender<?>> caller) {
        return $trim(val, filter,
                $one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <E> Appender<T> $trim(T val, Predicate<T> filter,
                                 String[] trimPrefixes, String[] trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<T, Appender<?>> caller) {
        return $trim(val, filter,
                $arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }


    public <E> Appender<T> $trim(E val, Predicate<E> filter,
                                 Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<E, Appender<?>> caller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        String str = caller.apply(val).get();
        if (trimPrefixes != null) {
            for (String item : trimPrefixes) {
                if (item == null) {
                    continue;
                }
                str = str.trim();
                if (str.startsWith(item)) {
                    str = str.substring(item.length());
                }
            }
        }
        if (trimSuffixes != null) {
            for (String item : trimSuffixes) {
                if (item == null) {
                    continue;
                }
                str = str.trim();
                if (str.startsWith(item)) {
                    str = str.substring(0, str.length() - item.length());
                }
            }
        }
        if (appendPrefix != null) {
            str = str.trim();
            if (!str.isEmpty()) {
                str = appendPrefix + " " + str;
            }
        }
        if (appendSuffix != null) {
            str = str.trim();
            if (!str.isEmpty()) {
                str = str + " " + appendSuffix;
            }
        }
        return add(str);
    }

    public <E> Appender<T> $trim(E val,
                                 String trimPrefixes, String trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<E, Appender<?>> caller) {
        return $trim(val,
                $one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <E> Appender<T> $trim(E val,
                                 String[] trimPrefixes, String[] trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<E, Appender<?>> caller) {
        return $trim(val,
                $arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public <E> Appender<T> $trim(E val,
                                 Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                                 String appendPrefix, String appendSuffix,
                                 Function<E, Appender<?>> caller) {
        return $trim(val, defaultFilter(),
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                caller);
    }

    public Appender<T> $trim(String trimPrefixes, String trimSuffixes,
                             String appendPrefix, String appendSuffix,
                             Supplier<Appender<?>> caller) {
        return $trim($one2list(trimPrefixes), $one2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public Appender<T> $trim(String[] trimPrefixes, String[] trimSuffixes,
                             String appendPrefix, String appendSuffix,
                             Supplier<Appender<?>> caller) {
        return $trim($arr2list(trimPrefixes), $arr2list(trimSuffixes),
                appendPrefix, appendSuffix,
                caller);
    }

    public Appender<T> $trim(Collection<String> trimPrefixes, Collection<String> trimSuffixes,
                             String appendPrefix, String appendSuffix,
                             Supplier<Appender<?>> caller) {
        return $trim(null, null,
                trimPrefixes, trimSuffixes,
                appendPrefix, appendSuffix,
                v -> caller.get());
    }

    public <E, C extends Collection<E>> Appender<T> $for(C val, Predicate<C> filter,
                                                         String separator,
                                                         Predicate<E> itemFilter,
                                                         BiFunction<Integer, E, Appender<?>> itemCaller) {
        if (filter != null && !filter.test(val)) {
            return this;
        }
        Appender<?> next = Appender.buffer();
        int idx = 0;
        boolean isFirst = true;
        for (E item : val) {
            if (itemFilter != null && !itemFilter.test(item)) {
                idx++;
                continue;
            }
            if (!isFirst) {
                next.add(separator);
            }
            Appender<?> one = itemCaller.apply(idx, item);
            next.add(one);
            isFirst = false;
            idx++;
        }
        return add(next);
    }

    public <E, C extends Collection<E>> Appender<T> $for(C val,
                                                         String separator,
                                                         Predicate<E> itemFilter,
                                                         BiFunction<Integer, E, Appender<?>> itemCaller) {
        return $for(val, defaultFilter(), separator, itemFilter, itemCaller);
    }

    public Appender<T> $each(Supplier<Appender>... suppliers) {
        return $each($arr2list(suppliers));
    }

    public Appender<T> $each(Collection<Supplier<Appender>> suppliers) {
        for (Supplier<Appender> supplier : suppliers) {
            Appender<?> next = supplier.get();
            add(next);
        }
        return this;
    }


    public Appender<T> trunc(int len) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.setLength(len);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.setLength(len);
        }
        return this;
    }

    public Appender<T> add(Object obj) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.append(obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.append(obj);
        }
        return this;
    }

    public Appender<T> addLine(Object obj) {
        add(obj);
        return line();
    }

    public String substr(int start) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.substring(start);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.substring(start);
        }
        return null;
    }

    public String substr(int start, int end) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.substring(start, end);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.substring(start, end);
        }
        return null;
    }

    public Appender<T> addStart(Object obj) {
        return insert(0, obj);
    }

    public Appender<T> insert(int index, Object obj) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.insert(index, obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.insert(index, obj);
        }
        return this;
    }

    public Appender<T> del(int start, int end) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.delete(start, end);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.delete(start, end);
        }
        return this;
    }

    public int length() {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.length();
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.length();
        }
        return -1;
    }

    public Appender<T> addRepeat(Object obj, int count) {
        for (int i = 0; i < count; i++) {
            add(obj);
        }
        return this;
    }

    public Appender<T> addFormat(String format, Object... args) {
        String str = String.format(format, args);
        return add(str);
    }

    public Appender<T> addDateFormat(Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return add(fmt.format(date));
    }

    public Appender<T> addDateFormat(LocalDate date, DateTimeFormatter formatter) {
        return add(date.format(formatter));
    }

    public Appender<T> addDateFormat(LocalDateTime date, DateTimeFormatter formatter) {
        return add(date.format(formatter));
    }

    public Appender<T> addDateFormat(LocalTime date, DateTimeFormatter formatter) {
        return add(date.format(formatter));
    }

    public Appender<T> addDateFormat(LocalDate date, String format) {
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }

    public Appender<T> addDateFormat(LocalDateTime date, String format) {
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }

    public Appender<T> addDateFormat(LocalTime date, String format) {
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }

    public Appender<T> line() {
        return add("\n");
    }

    public Appender<T> tab() {
        return add("\t");
    }

    public Appender<T> blank() {
        return add(" ");
    }

    public Appender<T> line(int count) {
        for (int i = 0; i < count; i++) {
            line();
        }
        return this;
    }

    public Appender<T> tab(int count) {
        for (int i = 0; i < count; i++) {
            tab();
        }
        return this;
    }

    public Appender<T> blank(int count) {
        for (int i = 0; i < count; i++) {
            blank();
        }
        return this;
    }

    public Appender<T> addWhen(boolean condition, Object obj) {
        if (condition) {
            add(obj);
        }
        return this;
    }

    public Appender<T> addNotWhen(boolean condition, Object obj) {
        if (!condition) {
            add(obj);
        }
        return this;
    }

    public Appender<T> addsWhen(boolean condition, Object... arr) {
        if (condition) {
            adds(arr);
        }
        return this;
    }

    public Appender<T> addsNotWhen(boolean condition, Object... arr) {
        if (!condition) {
            adds(arr);
        }
        return this;
    }

    public Appender<T> addIteratorWhen(boolean condition, Iterator<?> iterator) {
        if (condition) {
            addIterator(iterator);
        }
        return this;
    }

    public Appender<T> addIteratorNotWhen(boolean condition, Iterator<?> iterator) {
        if (!condition) {
            addIterator(iterator);
        }
        return this;
    }

    public Appender<T> keepEnd(String str) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(clen - slen);
            if (!end.equals(str)) {
                add(str);
            }
        } else {
            add(str);
        }
        return this;
    }

    public Appender<T> trimEnd(String str) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(clen - slen);
            if (end.equals(str)) {
                del(clen - slen, clen);
            }
        }
        return this;
    }

    public Appender<T> trim() {
        String str = appender.toString().trim();
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.setLength(0);
            builder.append(str);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.setLength(0);
            buffer.append(str);
        }
        return this;
    }

    public Appender<T> toLowerCase() {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            int len = builder.length();
            for (int i = 0; i < len; i++) {
                builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));
            }
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            int len = buffer.length();
            for (int i = 0; i < len; i++) {
                buffer.setCharAt(i, Character.toLowerCase(buffer.charAt(i)));
            }
        }
        return this;
    }

    public Appender<T> toUpperCase() {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            int len = builder.length();
            for (int i = 0; i < len; i++) {
                builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
            }
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            int len = buffer.length();
            for (int i = 0; i < len; i++) {
                buffer.setCharAt(i, Character.toUpperCase(buffer.charAt(i)));
            }
        }
        return this;
    }

    public Appender<T> keepStart(String str) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(0, slen);
            if (!end.equals(str)) {
                addStart(str);
            }
        } else {
            addStart(str);
        }
        return this;
    }

    public Appender<T> trimStart(String str) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(0, slen);
            if (end.equals(str)) {
                del(0, slen);
            }
        }
        return this;
    }

    public Appender<T> addWhenEnd(String str, Object obj) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(clen - slen);
            if (end.equals(str)) {
                add(obj);
            }
        }
        return this;
    }

    public Appender<T> addNotWhenEnd(String str, Object obj) {
        str = str + "";
        int slen = str.length();
        int clen = length();
        if (clen >= slen) {
            String end = substr(clen - slen);
            if (!end.equals(str)) {
                add(obj);
            }
        }
        return this;
    }

    public Appender<T> addWhenTo(Object obj, boolean condition, Object replace) {
        if (condition) {
            add(replace);
        } else {
            add(obj);
        }
        return this;
    }

    public Appender<T> addNotWhenTo(Object obj, boolean condition, Object replace) {
        if (!condition) {
            add(replace);
        } else {
            add(obj);
        }
        return this;
    }

    public Appender<T> addNullTo(Object obj, Object replace) {
        return addWhenTo(obj, obj == null, replace);
    }

    public Appender<T> addEmptyTo(String str, Object replace) {
        return addWhenTo(str, str == null || str.isEmpty(), replace);
    }

    public <E> Appender<T> addIteratorElem(Iterator<E> iterator, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        if (open != null) {
            add(open);
        }
        boolean isFirst = true;
        while (iterator.hasNext()) {
            if (!isFirst) {
                if (separator != null) {
                    add(separator);
                }
            }
            E val = iterator.next();
            if (mapper != null) {
                mapper.accept(this, val);
            } else {
                add(val);
            }
            isFirst = false;
        }
        if (close != null) {
            add(close);
        }
        return this;
    }

    public <E> Appender<T> addIterableElem(Iterable<E> col, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        return addIteratorElem(Iterators.of(col), separator, open, close, mapper);
    }

    public <E> Appender<T> addCollectionElem(Collection<E> col, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        return addIteratorElem(Iterators.of(col), separator, open, close, mapper);
    }

    public <E> Appender<T> addEnumerationElem(Enumeration<E> enums, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        return addIteratorElem(Iterators.of(enums), separator, open, close, mapper);
    }

    public <E> Appender<T> addArrayElem(E[] arr, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        return addIteratorElem(Iterators.of(arr), separator, open, close, mapper);
    }

    public <E> Appender<T> addReflectArrayElem(Object arr, Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper) {
        return addIteratorElem(Iterators.ofArrayObject(arr), separator, open, close, mapper);
    }

    public <E> Appender<T> addsElem(Object separator, Object open, Object close, BiConsumer<Appender<T>, E> mapper, E... arr) {
        return addIteratorElem(Iterators.of(arr), separator, open, close, mapper);
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        if (open != null) {
            add(open);
        }
        boolean isFirst = true;
        while (iterator.hasNext()) {
            if (!isFirst) {
                if (separator != null) {
                    add(separator);
                }
            }
            Object val = iterator.next();
            if (mapper != null) {
                val = mapper.apply(val);
            }
            add(val);
            isFirst = false;
        }
        if (close != null) {
            add(close);
        }
        return this;
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator, Object open, Object close) {
        return addIterator(iterator, separator, open, close, null);
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator) {
        return addIterator(iterator, separator, null, null);
    }

    public Appender<T> addIterator(Iterator<?> iterator) {
        return addIterator(iterator, null, null, null);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        return addIterator(Iterators.of(col), separator, open, close, mapper);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(col), separator, open, close);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator) {
        return addIterator(Iterators.of(col), separator, null, null);
    }

    public Appender<T> addIterable(Iterable<?> col) {
        return addIterator(Iterators.of(col), null, null, null);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        return addIterator(Iterators.of(col), separator, open, close, mapper);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(col), separator, open, close);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator) {
        return addIterator(Iterators.of(col), separator, null, null);
    }

    public Appender<T> addCollection(Collection<?> col) {
        return addIterator(Iterators.of(col), null, null, null);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        return addIterator(Iterators.of(enu), separator, open, close, mapper);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(enu), separator, open, close);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator) {
        return addIterator(Iterators.of(enu), separator, null, null);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu) {
        return addIterator(Iterators.of(enu), null, null, null);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        return addIterator(Iterators.of(arr), separator, open, close, mapper);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator) {
        return addIterator(Iterators.of(arr), separator, null, null);
    }

    public <E> Appender<T> addArray(E[] arr) {
        return addIterator(Iterators.of(arr), null, null, null);
    }

    public Appender<T> addReflectArray(Object arr, Object separator, Object open, Object close, Function<Object, ?> mapper) {
        return addIterator(Iterators.ofArrayObject(arr), separator, open, close, mapper);
    }

    public Appender<T> addReflectArray(Object arr, Object separator, Object open, Object close) {
        return addIterator(Iterators.ofArrayObject(arr), separator, open, close);
    }

    public Appender<T> addReflectArray(Object arr, Object separator) {
        return addIterator(Iterators.ofArrayObject(arr), separator, null, null);
    }

    public Appender<T> addReflectArray(Object arr) {
        return addIterator(Iterators.ofArrayObject(arr), null, null, null);
    }

    public Appender<T> addsLine(Object... arr) {
        adds(arr);
        return line();
    }

    public Appender<T> adds(Object... arr) {
        return addIterator(Iterators.of(arr), null, null, null);
    }

    public Appender<T> addsSepLine(Object separator, Object... arr) {
        addsSep(separator, arr);
        return line();
    }

    public Appender<T> addsSep(Object separator, Object... arr) {
        return addIterator(Iterators.of(arr), separator, null, null);
    }

    public Appender<T> addsFull(Object separator, Object open, Object close, Object... arr) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public Appender<T> addsFullMap(Object separator, Object open, Object close, Function<Object, ?> mapper, Object... arr) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public <K, V> Appender<T> addMap(Map<K, V> map, Object kvSeparator, Object entrySeparator, Object open, Object close) {
        if (open != null) {
            add(open);
        }

        if (close != null) {
            add(close);
        }

        boolean isFirst = true;
        for (Map.Entry<K, V> item : map.entrySet()) {
            if (!isFirst) {
                if (entrySeparator != null) {
                    add(entrySeparator);
                }
            }
            add(item.getKey());
            if (kvSeparator != null) {
                add(kvSeparator);
            }
            add(item.getValue());
            isFirst = false;
        }

        return this;
    }

    public <K, V> Appender<T> addMap(Map<K, V> map, Object kvSeparator, Object entrySeparator) {
        return addMap(map, kvSeparator, entrySeparator, null, null);
    }

    public Appender<T> addStrBytes(byte[] bytes) {
        return add(new String(bytes));
    }

    public Appender<T> addStrBytes(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return add(new String(bytes, charset));
    }

    public Appender<T> addHexBytes(byte[] bytes) {
        return addHexBytes(bytes, "0x", ",");
    }

    public Appender<T> addHexBytes(byte[] bytes, Object prefix, Object separator) {
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                if (separator != null) {
                    add(separator);
                }
            }
            if (prefix != null) {
                add(prefix);
            }
            addFormat("%02X", bytes[i] & 0x0ff);
        }
        return this;
    }

    public Appender<T> addOtcBytes(byte[] bytes) {
        return addOtcBytes(bytes, "0", ",");
    }

    public Appender<T> addOtcBytes(byte[] bytes, Object prefix, Object separator) {
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                if (separator != null) {
                    add(separator);
                }
            }
            if (prefix != null) {
                add(prefix);
            }
            addFormat("%03o", bytes[i] & 0x0ff);
        }
        return this;
    }
}
