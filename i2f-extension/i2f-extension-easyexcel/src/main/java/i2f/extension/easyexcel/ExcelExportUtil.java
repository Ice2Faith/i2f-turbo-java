package i2f.extension.easyexcel;


import i2f.extension.easyexcel.core.ExcelExportPage;
import i2f.extension.easyexcel.core.ExcelExportTask;
import i2f.extension.easyexcel.core.IDataProvider;
import i2f.extension.easyexcel.core.impl.DefaultDataProvider;
import i2f.extension.easyexcel.core.impl.ListDataProvider;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/1/29 9:43
 * @desc
 */
public class ExcelExportUtil {
    public static File write(List<Map<String, Object>> data, File file) {
        return write(new ListDataProvider(data), file);
    }

    public static File write(List<?> data, Class<?> elemType, File file) {
        return write(new ListDataProvider(data, elemType), file);
    }

    public static <E> File write(Function<ExcelExportPage, List<E>> dataExtractor, Class<E> elemType, File file) {
        return write(new DefaultDataProvider<>(dataExtractor, elemType), file);
    }

    public static File write(IDataProvider provider, File file) {
        ExcelExportTask task = new ExcelExportTask(provider, file, null, null, null);
        task.run();
        return task.getFile();
    }

    public static File write(List<Map<String, Object>> data, File file, String sheetName) {
        return write(new ListDataProvider(data), file, sheetName);
    }

    public static File write(List<?> data, Class<?> elemType, File file, String sheetName) {
        return write(new ListDataProvider(data, elemType), file, sheetName);
    }

    public static File write(IDataProvider provider, File file, String sheetName) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, null, null);
        task.run();
        return task.getFile();
    }

    public static File write(List<Map<String, Object>> data, File file, String sheetName, File templateFile) {
        return write(new ListDataProvider(data), file, sheetName, templateFile);
    }

    public static File write(List<?> data, Class<?> elemType, File file, String sheetName, File templateFile) {
        return write(new ListDataProvider(data, elemType), file, sheetName, templateFile);
    }

    public static File write(IDataProvider provider, File file, String sheetName, File templateFile) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, templateFile, null);
        task.run();
        return task.getFile();
    }

    public static File write(List<Map<String, Object>> data, File file, String sheetName, File templateFile, File tmpFile) {
        return write(new ListDataProvider(data), file, sheetName, templateFile, tmpFile);
    }

    public static File write(List<?> data, Class<?> elemType, File file, String sheetName, File templateFile, File tmpFile) {
        return write(new ListDataProvider(data, elemType), file, sheetName, templateFile, tmpFile);
    }

    public static File write(IDataProvider provider, File file, String sheetName, File templateFile, File tmpFile) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, templateFile, tmpFile);
        task.run();
        return task.getFile();
    }

    public static File write(List<Map<String, Object>> data, File file, String sheetName, File templateFile, File tmpFile, Consumer<ExcelExportTask> taskConsumer) {
        return write(new ListDataProvider(data), file, sheetName, templateFile, tmpFile, taskConsumer);
    }

    public static File write(List<?> data, Class<?> elemType, File file, String sheetName, File templateFile, File tmpFile, Consumer<ExcelExportTask> taskConsumer) {
        return write(new ListDataProvider(data, elemType), file, sheetName, templateFile, tmpFile, taskConsumer);
    }

    public static File write(IDataProvider provider, File file, String sheetName, File templateFile, File tmpFile, Consumer<ExcelExportTask> taskConsumer) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, templateFile, tmpFile);
        if (taskConsumer != null) {
            taskConsumer.accept(task);
        }
        task.run();
        return task.getFile();
    }
}
