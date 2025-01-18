package i2f.extension.easyexcel.core;


import java.util.List;

/**
 * @author Ice2Faith
 * @date 2021/10/19
 */
public interface IDataProvider {
    void preProcess();

    boolean supportPage();

    List<?> getData(ExcelExportPage page);

    Class<?> getDataClass();
}
