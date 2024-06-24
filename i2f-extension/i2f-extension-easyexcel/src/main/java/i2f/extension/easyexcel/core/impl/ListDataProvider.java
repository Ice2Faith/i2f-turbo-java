package i2f.extension.easyexcel.core.impl;


import i2f.extension.easyexcel.core.ExcelExportPage;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2021/10/19
 */
public class ListDataProvider extends AbsDataProviderAdapter {
    private List<?> data;
    private Class<?> clazz;

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
