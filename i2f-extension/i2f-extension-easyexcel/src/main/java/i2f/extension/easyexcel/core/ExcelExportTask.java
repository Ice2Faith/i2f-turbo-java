package i2f.extension.easyexcel.core;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import i2f.extension.easyexcel.annotation.ExcelTag;
import i2f.extension.easyexcel.style.AnnotationExcelStyleCellWriteHandler;
import i2f.io.stream.StreamUtil;
import i2f.reflect.ReflectResolver;
import i2f.resources.ResourceUtil;
import i2f.typeof.TypeOf;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2024/1/29 10:38
 * @desc
 */
@Data
public class ExcelExportTask implements Runnable, Callable<File> {
    private int pageSize = 65530; // 旧版本的excel,单个sheet仅支持 65535 ，0x7fff
    private IDataProvider provider;
    private File file;
    private File tmpFile;
    private String sheetName;
    private URL templateUrl;
    private Set<String> excludeColumnTags;
    private Set<String> excludeColumnNames;
    private boolean enableCellStyleAnnotation;
    private CellWriteHandler writeHandler;
    private CountDownLatch latch;
    private List<ExcelExportColumn> exportColumns;
    private Consumer<ExcelExportTask> beforeConsumer;
    private Consumer<ExcelExportTask> afterConsumer;

    public ExcelExportTask(IDataProvider provider) {
        this.provider = provider;
    }

    public ExcelExportTask(IDataProvider provider, File file) {
        this.provider = provider;
        this.file = file;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName, URL templateUrl) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
        this.templateUrl = templateUrl;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName, URL templateUrl, File tmpFile) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
        this.templateUrl = templateUrl;
        this.tmpFile = tmpFile;
    }

    public ExcelExportTask setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ExcelExportTask setTemplateUrl(URL templateUrl) {
        this.templateUrl = templateUrl;
        return this;
    }

    public ExcelExportTask setExcludeColumnNames(Set<String> excludeColumnNames) {
        this.excludeColumnNames = excludeColumnNames;
        return this;
    }

    public ExcelExportTask updateProperties(Consumer<ExcelExportTask> consumer) {
        if (consumer != null) {
            consumer.accept(this);
        }
        return this;
    }

    public static class EmptyCellWriteHandler implements CellWriteHandler {

    }

    public static final EmptyCellWriteHandler EMPTY_CELL_WRITE_HANDLER = new EmptyCellWriteHandler();

    protected void updateWriteHandler(List<?> list, Class<?> clazz,
                                      Map<Integer, Map<String, Object>> sheetContext,
                                      int sheetIndex,
                                      Map<String, Object> workbookContext) {
        if (writeHandler == null) {
            return;
        }
        if (!(writeHandler instanceof IDataHoldCellWriteHandler)) {
            return;
        }
        IDataHoldCellWriteHandler handler = (IDataHoldCellWriteHandler) writeHandler;
        if (list != null) {
            handler.setList(list);
        }
        if (clazz != null) {
            handler.setClazz(clazz);
        }
        handler.setWorkbookContext(workbookContext);
        if (!sheetContext.containsKey(sheetIndex)) {
            sheetContext.put(sheetIndex, new HashMap<>());
        }
        handler.setSheetContext(sheetContext.get(sheetIndex));
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public File call() throws Exception {
        boolean createdMapTemplateFile = false;
        boolean isTempTemplateFile = false;
        File templateFile = null;

        try {
            if (file == null) {
                file = getTempFile();
            }

            File parentFile = file.getParentFile();
            if (parentFile == null) {
                parentFile = new File(file.getAbsolutePath()).getParentFile();
            }
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (tmpFile == null) {
                String tmpSuffix = "";
                String name = file.getName();
                int idx = name.lastIndexOf(".");
                if (idx >= 0) {
                    tmpSuffix = name.substring(idx);
                }
                tmpFile = new File(file.getParentFile(), getRandomUuid() + tmpSuffix + ".data");
            }

            if (sheetName == null) {
                sheetName = "sheet";
            }

            if (excludeColumnNames == null) {
                excludeColumnNames = new HashSet<>();
            }

            if (tmpFile.exists()) {
                tmpFile.delete();
            }

            if (writeHandler == null) {
                if (enableCellStyleAnnotation) {
                    writeHandler = new AnnotationExcelStyleCellWriteHandler();
                } else {
                    writeHandler = EMPTY_CELL_WRITE_HANDLER;
                }
            }

            Class<?> dataClass = provider.getDataClass();

            Map<String, Object> workbookContext = new HashMap<>();
            Map<Integer, Map<String, Object>> sheetContext = new HashMap<>();

            if (templateFile == null) {
                if (exportColumns != null && !exportColumns.isEmpty()) {
                    templateFile = createTemplateFileByColumns(file, exportColumns);
                    createdMapTemplateFile = true;
                }
            }

            if (templateFile == null) {
                if (templateUrl != null) {
                    try {
                        String protocol = templateUrl.getProtocol();
                        if ("file".equals(protocol)) {
                            String templatePath = templateUrl.getFile();
                            templateFile = new File(templatePath);
                        } else {
                            InputStream is = templateUrl.openStream();
                            templateFile = getTempFile(".xlsx");
                            StreamUtil.streamCopy(is, new FileOutputStream(templateFile));
                            isTempTemplateFile = true;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            boolean useTemplate = (templateFile != null && templateFile.exists() && templateFile.isFile());

            File tmpDir = tmpFile.getParentFile();
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }

            if (excludeColumnTags == null) {
                excludeColumnTags = new HashSet<>();
            }

            if (dataClass != null && !excludeColumnTags.isEmpty()) {
                Map<Field, Class<?>> fields = ReflectResolver.getFields(dataClass, (field) -> {
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                        return false;
                    }
                    ExcelTag ann = ReflectResolver.getAnnotation(field, ExcelTag.class);
                    if (ann == null) {
                        return false;
                    }
                    String[] tags = ann.value();
                    if (tags == null) {
                        return false;
                    }
                    for (String tag : tags) {
                        if (excludeColumnTags.contains(tag)) {
                            return true;
                        }
                    }
                    return false;
                });

                Set<String> names = fields.keySet().stream().map(Field::getName).collect(Collectors.toSet());
                excludeColumnNames.addAll(names);
            }

            if (beforeConsumer != null) {
                beforeConsumer.accept(this);
            }

            boolean isSupportPage = provider.supportPage();
            if (!isSupportPage) {
                Class<?> clazz = provider.getDataClass();
                List<?> data = provider.getData(null);
                if (TypeOf.typeOf(clazz, Map.class)) {
                    if (!data.isEmpty()) {
                        if (!useTemplate) {
                            if (isTempTemplateFile) {
                                if (templateFile != null) {
                                    templateFile.delete();
                                }
                            }
                            templateFile = createTemplateFileByMap(file, (Map<?, ?>) data.get(0));
                            useTemplate = true;
                            createdMapTemplateFile = true;
                        }
                    }
                }
                int size = data.size();
                if (size <= pageSize) {
                    if (useTemplate) {
                        ExcelWriter excelWriter = EasyExcel.write(tmpFile)
                                .withTemplate(templateFile)
                                .registerWriteHandler(writeHandler)
                                .excludeColumnFieldNames(excludeColumnNames)
                                .build();
                        WriteSheet writeSheet = EasyExcel.write()
                                .sheet(0, sheetName)
                                .build();
                        updateWriteHandler(data, clazz, sheetContext, 0, workbookContext);
                        excelWriter.fill(data, writeSheet);
                        excelWriter.finish();
                    } else {
                        updateWriteHandler(data, clazz, sheetContext, 0, workbookContext);
                        EasyExcel.write(tmpFile, clazz)
                                .sheet(0, sheetName)
                                .registerWriteHandler(writeHandler)
                                .excludeColumnFieldNames(excludeColumnNames)
                                .doWrite(data);
                    }
                } else {
                    ExcelWriter excelWriter = null;
                    int curPageIndex = 0;
                    int count = 0;
                    List<Object> curData = new ArrayList<>(pageSize);
                    for (Object item : data) {
                        if (excelWriter == null) {
                            if (TypeOf.typeOf(clazz, Map.class)) {
                                if (!data.isEmpty()) {
                                    if (!useTemplate) {
                                        if (isTempTemplateFile) {
                                            if (templateFile != null) {
                                                templateFile.delete();
                                            }
                                        }
                                        templateFile = createTemplateFileByMap(file, (Map<?, ?>) item);
                                        useTemplate = true;
                                        createdMapTemplateFile = true;
                                    }
                                }
                            }
                            // 处理使用模板的情况
                            if (useTemplate) {
                                excelWriter = EasyExcel.write(tmpFile)
                                        .withTemplate(templateFile)
                                        .registerWriteHandler(writeHandler)
                                        .excludeColumnFieldNames(excludeColumnNames)
                                        .build();
                            } else {
                                excelWriter = EasyExcel.write(tmpFile, clazz)
                                        .registerWriteHandler(writeHandler)
                                        .excludeColumnFieldNames(excludeColumnNames)
                                        .build();
                            }
                        }
                        if (count == pageSize) {
                            WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
                                    .excludeColumnFieldNames(excludeColumnNames)
                                    .build();
                            updateWriteHandler(curData, clazz, sheetContext, curPageIndex, workbookContext);
                            if (useTemplate) {
                                excelWriter.fill(curData, writeSheet);
                            } else {
                                excelWriter.write(curData, writeSheet);
                            }

                            count = 0;
                            curPageIndex++;
                            curData.clear();
                        }
                        curData.add(item);
                        count++;
                    }
                    if (count > 0) {
                        WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
                                .excludeColumnFieldNames(excludeColumnNames)
                                .build();
                        updateWriteHandler(curData, clazz, sheetContext, curPageIndex, workbookContext);
                        if (useTemplate) {
                            excelWriter.fill(curData, writeSheet);
                        } else {
                            excelWriter.write(curData, writeSheet);
                        }
                    }

                    excelWriter.finish();
                }
            } else {
                int curPageIndex = 0;

                Class<?> clazz = provider.getDataClass();
                ExcelWriter excelWriter = null;

                while (true) {
                    ExcelExportPage page = new ExcelExportPage(curPageIndex, pageSize);
                    List<?> data = provider.getData(page);
                    if (data == null || data.isEmpty()) {
                        break;
                    }

                    if (excelWriter == null) {
                        if (TypeOf.typeOf(clazz, Map.class)) {
                            if (!data.isEmpty()) {
                                if (!useTemplate) {
                                    if (isTempTemplateFile) {
                                        if (templateFile != null) {
                                            templateFile.delete();
                                        }
                                    }
                                    templateFile = createTemplateFileByMap(file, (Map<?, ?>) data.get(0));
                                    useTemplate = true;
                                    createdMapTemplateFile = true;
                                }
                            }
                        }
                        if (useTemplate) {
                            excelWriter = EasyExcel.write(tmpFile)
                                    .withTemplate(templateFile)
                                    .registerWriteHandler(writeHandler)
                                    .excludeColumnFieldNames(excludeColumnNames)
                                    .build();
                        } else {
                            excelWriter = EasyExcel.write(tmpFile, clazz)
                                    .registerWriteHandler(writeHandler)
                                    .excludeColumnFieldNames(excludeColumnNames)
                                    .build();
                        }
                    }

                    int dsize = data.size();

                    WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
                            .excludeColumnFieldNames(excludeColumnNames)
                            .build();
                    updateWriteHandler(data, clazz, sheetContext, curPageIndex, workbookContext);
                    if (useTemplate) {
                        excelWriter.fill(data, writeSheet);
                    } else {
                        excelWriter.write(data, writeSheet);
                    }

                    curPageIndex++;

                    if (dsize < pageSize) {
                        break;
                    }
                }

                if (excelWriter == null) {
                    excelWriter = EasyExcel.write(tmpFile, clazz)
                            .registerWriteHandler(writeHandler)
                            .excludeColumnFieldNames(excludeColumnNames)
                            .build();
                }
                excelWriter.finish();
            }
            if (file.exists()) {
                file.delete();
            }

            File fileDir = file.getParentFile();
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            tmpFile.renameTo(file);

            return file;
        } finally {
            if (afterConsumer != null) {
                afterConsumer.accept(this);
            }
            if (tmpFile != null) {
                tmpFile.delete();
            }

            if (createdMapTemplateFile || isTempTemplateFile) {
                if (templateFile != null) {
                    templateFile.delete();
                }
            }

            if (latch != null) {
                latch.countDown();
            }
        }
    }

    public static File getTempFile() {
        return getTempFile(null);
    }

    public static File getTempFile(String suffix) {
        String nameOnly = "tmp_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
        if (suffix == null) {
            suffix = ".data";
        }
        try {
            return File.createTempFile(nameOnly, suffix);
        } catch (IOException e) {
            return new File(nameOnly + suffix);
        }
    }

    private static String getRandomUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static File createTemplateFileByColumns(File file, List<ExcelExportColumn> columns) {
        String tmpSuffix = "";
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            tmpSuffix = name.substring(idx);
        }

        File templateFile = new File(file.getParentFile(), getRandomUuid() + tmpSuffix + ".data");
        File ret = new File(file.getParentFile(), getRandomUuid() + tmpSuffix + ".data");
        try {
            InputStream is = ResourceUtil.getClasspathResourceAsStream("assets/excel/map-export-template.xlsx");
            StreamUtil.writeBytes(is, templateFile);

            List<Map<String, Object>> data = new ArrayList<>();
            for (ExcelExportColumn column : columns) {
                if (column == null) {
                    continue;
                }
                String label = column.getTitle();
                String propExpr = column.getProp();
                if (propExpr == null) {
                    propExpr = "";
                }
                propExpr = propExpr.trim();
                if (!propExpr.startsWith("{")) {
                    propExpr = "{." + propExpr + "}";
                }
                Map<String, Object> val = new HashMap<>();
                val.put("label", label);
                val.put("prop", propExpr);
                data.add(val);
            }

            ExcelWriter excelWriter = EasyExcel.write(ret)
                    .withTemplate(templateFile)
                    .build();
            WriteSheet writeSheet = EasyExcel.write()
                    .sheet(0, "sheet")
                    .build();
            FillConfig fillConfig = FillConfig.builder()
                    .direction(WriteDirectionEnum.HORIZONTAL)
                    .build();
            excelWriter.fill(data, fillConfig, writeSheet);
            excelWriter.finish();
            excelWriter.close();

            return ret;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            templateFile.delete();
        }
    }

    public static File createTemplateFileByMap(File file, Map<?, ?> map) {
        List<ExcelExportColumn> columns = new ArrayList<>();
        for (Object item : map.keySet()) {
            String prop = String.valueOf(item);
            ExcelExportColumn column = new ExcelExportColumn();
            column.setTitle(prop);
            column.setProp(prop);
            columns.add(column);
        }
        return createTemplateFileByColumns(file, columns);
    }

}
