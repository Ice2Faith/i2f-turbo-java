package i2f.extension.fastexcel.core.convertor;

import cn.idev.excel.converters.Converter;
import i2f.extension.fastexcel.core.convertor.atomics.AtomicBooleanBooleanConvertor;
import i2f.extension.fastexcel.core.convertor.atomics.AtomicBooleanNumberConvertor;
import i2f.extension.fastexcel.core.convertor.atomics.AtomicIntegerNumberConvertor;
import i2f.extension.fastexcel.core.convertor.atomics.AtomicLongNumberConvertor;
import i2f.extension.fastexcel.core.convertor.json.collection.*;
import i2f.extension.fastexcel.core.convertor.json.map.*;
import i2f.extension.fastexcel.core.convertor.sql.SqlDateStringConvertor;
import i2f.extension.fastexcel.core.convertor.sql.SqlTimeStringConvertor;
import i2f.extension.fastexcel.core.convertor.sql.SqlTimestampStringConvertor;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:39
 */
public class Convertors {
    public static final CopyOnWriteArrayList<Converter<?>> CONVERTERS = new CopyOnWriteArrayList<>();

    static {
        CONVERTERS.add(new SqlTimestampStringConvertor());
        CONVERTERS.add(new SqlDateStringConvertor());
        CONVERTERS.add(new SqlTimeStringConvertor());
        CONVERTERS.add(new AtomicBooleanBooleanConvertor());
        CONVERTERS.add(new AtomicBooleanNumberConvertor());
        CONVERTERS.add(new AtomicIntegerNumberConvertor());
        CONVERTERS.add(new AtomicLongNumberConvertor());
        CONVERTERS.add(new ArrayListJsonStringConvertor());
        CONVERTERS.add(new CollectionJsonStringConvertor());
        CONVERTERS.add(new CopyOnWriteArrayListJsonStringConvertor());
        CONVERTERS.add(new CopyOnWriteArraySetJsonStringConvertor());
        CONVERTERS.add(new HashSetJsonStringConvertor());
        CONVERTERS.add(new LinkedHashSetJsonStringConvertor());
        CONVERTERS.add(new LinkedListJsonStringConvertor());
        CONVERTERS.add(new ListJsonStringConvertor());
        CONVERTERS.add(new SetJsonStringConvertor());
        CONVERTERS.add(new TreeSetJsonStringConvertor());
        CONVERTERS.add(new ConcurrentHashMapJsonStringConvertor());
        CONVERTERS.add(new HashMapJsonStringConvertor());
        CONVERTERS.add(new HashTableJsonStringConvertor());
        CONVERTERS.add(new LinkedHashMapJsonStringConvertor());
        CONVERTERS.add(new MapJsonStringConvertor());
        CONVERTERS.add(new TreeMapJsonStringConvertor());

        ServiceLoader<Converter> list = ServiceLoader.load(Converter.class);
        for (Converter item : list) {
            CONVERTERS.add(item);
        }
    }

    public static List<Converter<?>> getConvertors() {
        List<Converter<?>> ret = new ArrayList<>();
        ret.addAll(CONVERTERS);
        return ret;
    }
}
