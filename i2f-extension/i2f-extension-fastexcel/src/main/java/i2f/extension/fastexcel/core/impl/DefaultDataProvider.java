package i2f.extension.fastexcel.core.impl;

import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.IDataProvider;

import java.util.List;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/7/1 14:16
 */
public class DefaultDataProvider<R> implements IDataProvider {
    protected Function<ExcelExportPage, List<R>> dataExtractor;
    protected Class<R> returnType;

    public DefaultDataProvider(Function<ExcelExportPage, List<R>> dataExtractor, Class<R> returnType) {
        this.dataExtractor = dataExtractor;
        this.returnType = returnType;
    }

    @Override
    public List<R> getData(ExcelExportPage page) {
        return dataExtractor.apply(page);
    }

    @Override
    public Class<R> getDataClass() {
        return returnType;
    }
}
