package i2f.extension.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import i2f.extension.easyexcel.core.ObjectAnalysisEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelImportUtil {
    public static <T> List<T> read(File file, Class<T> beanClass) throws IOException {
        ObjectAnalysisEventListener<T> listener = new ObjectAnalysisEventListener<T>();
        try (InputStream is = new FileInputStream(file)) {
            read(is, beanClass, listener);
        }
        return listener.getLegalData();
    }

    public static <T> List<T> read(File file, Class<T> beanClass, ObjectAnalysisEventListener<T> listener) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            read(is, beanClass, listener);
        }
        return listener.getLegalData();
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass) {
        return read(is, beanClass, 0, new ObjectAnalysisEventListener<T>());
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass, ObjectAnalysisEventListener<T> listener) {
        return read(is, beanClass, 0, listener);
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass, int sheetNo, ObjectAnalysisEventListener<T> listener) {
        ExcelReader excelReader = EasyExcel.read(is, beanClass, listener).build();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return listener.getLegalData();
    }
}
