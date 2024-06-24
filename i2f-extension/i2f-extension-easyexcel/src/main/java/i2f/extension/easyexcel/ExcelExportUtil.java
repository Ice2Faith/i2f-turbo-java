package i2f.extension.easyexcel;

import i2f.extension.easyexcel.core.ExcelExportTask;
import i2f.extension.easyexcel.core.IDataProvider;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2024/1/29 9:43
 * @desc
 */
public class ExcelExportUtil {
    public static File write(IDataProvider provider, File file) {
        ExcelExportTask task = new ExcelExportTask(provider, file, null, null, null);
        task.run();
        return task.getFile();
    }

    public static File write(IDataProvider provider, File file, String sheetName) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, null, null);
        task.run();
        return task.getFile();
    }

    public static File write(IDataProvider provider, File file, String sheetName, File templateFile) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, templateFile, null);
        task.run();
        return task.getFile();
    }

    public static File write(IDataProvider provider, File file, String sheetName, File templateFile, File tmpFile) {
        ExcelExportTask task = new ExcelExportTask(provider, file, sheetName, templateFile, tmpFile);
        task.run();
        return task.getFile();
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
