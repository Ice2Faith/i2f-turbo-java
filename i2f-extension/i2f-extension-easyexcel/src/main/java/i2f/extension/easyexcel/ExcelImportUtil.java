package i2f.extension.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import i2f.extension.easyexcel.core.MapAnalysisEventListener;
import i2f.extension.easyexcel.core.ObjectAnalysisEventListener;
import i2f.extension.easyexcel.core.WrapAnalysisEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ExcelImportUtil {
    public static List<Map<String, Object>> read(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return read(is);
        }
    }

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

    public static List<Map<String, Object>> read(InputStream is) {
        return read(is, null, 0, new MapAnalysisEventListener());
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass) {
        return read(is, beanClass, 0, new ObjectAnalysisEventListener<T>());
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass, ObjectAnalysisEventListener<T> listener) {
        return read(is, beanClass, 0, listener);
    }

    public static <T> List<T> read(InputStream is, Class<T> beanClass, int sheetNo, ObjectAnalysisEventListener<T> listener) {
        return read(is, beanClass, sheetNo, (WrapAnalysisEventListener<T, T>) listener);
    }

    public static <T, R> List<R> read(InputStream is, Class<?> beanClass, int sheetNo, WrapAnalysisEventListener<T, R> listener) {
        ExcelReader excelReader = EasyExcel.read(is, beanClass, listener).build();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return listener.getLegalData();
    }
}
