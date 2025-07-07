package i2f.extension.easyexcel.core;

import i2f.extension.easyexcel.core.impl.DefaultDataProvider;
import i2f.extension.easyexcel.core.impl.ListDataProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2021/10/19
 */
public interface IDataProvider {
    default void preProcess() {

    }

    default boolean supportPage() {
        return true;
    }

    List<?> getData(ExcelExportPage page);

    Class<?> getDataClass();

    static <R> IDataProvider of(Function<ExcelExportPage, List<R>> dataExtractor, Class<R> returnType) {
        return new DefaultDataProvider<>(dataExtractor, returnType);
    }

    static IDataProvider ofAny(Function<ExcelExportPage, List<?>> dataExtractor, Class<?> returnType) {
        return new DefaultDataProvider(dataExtractor, returnType);
    }

    static <R> IDataProvider of(List<R> list, Class<R> returnType) {
        return new ListDataProvider(list, returnType);
    }

    static IDataProvider of(List<Map<String, Object>> list) {
        return new ListDataProvider(list);
    }
}
