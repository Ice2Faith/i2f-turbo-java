package i2f.extension.fastexcel.core.impl;


import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.IDataProvider;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2021/10/19
 */
public class ListDataProvider implements IDataProvider {
    private List<?> data;
    private Class<?> clazz;


    public ListDataProvider(List<Map<String, Object>> data) {
        this(data, Map.class);
    }

    public ListDataProvider(List<?> data, Class<?> clazz) {
        this.data = data;
        this.clazz = clazz;
    }

    @Override
    public boolean supportPage() {
        return false;
    }


    @Override
    public List<?> getData(ExcelExportPage page) {
        return data;
    }

    @Override
    public Class<?> getDataClass() {
        return clazz;
    }
}
