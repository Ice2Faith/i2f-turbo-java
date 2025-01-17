package i2f.extension.easyexcel.core;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.enums.WriteDirectionEnum;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.fill.FillConfig;
import i2f.extension.easyexcel.annotation.ExcelTag;
import i2f.extension.easyexcel.style.AnnotationExcelStyleCellWriteHandler;
import i2f.io.stream.StreamUtil;
import i2f.reflect.ReflectResolver;
import i2f.resources.ResourceUtil;
import i2f.typeof.TypeOf;
import lombok.Data;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2024/1/29 10:38
 * @desc
 */
@Data
public class ExcelExportTask implements Runnable {
    private int pageSize = 65530; // 旧版本的excel,单个sheet仅支持 65535 ，0x7fff
    private IDataProvider provider;
    private File file;
    private File tmpFile;
    private String sheetName;
    private File templateFile;
    private Set<String> excludeColumnTags;
    private Set<String> excludeColumnNames;
    private boolean enableCellStyleAnnotation;
    private CellWriteHandler writeHandler;
    private CountDownLatch latch;

    public ExcelExportTask(IDataProvider provider, File file) {
        this.provider = provider;
        this.file = file;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName, File templateFile) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
        this.templateFile = templateFile;
    }

    public ExcelExportTask(IDataProvider provider, File file, String sheetName, File templateFile, File tmpFile) {
        this.provider = provider;
        this.file = file;
        this.sheetName = sheetName;
        this.templateFile = templateFile;
        this.tmpFile = tmpFile;
    }

    public ExcelExportTask setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ExcelExportTask setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
        return this;
    }

    public ExcelExportTask setExcludeColumnNames(Set<String> excludeColumnNames) {
        this.excludeColumnNames = excludeColumnNames;
        return this;
    }

    public static class EmptyCellWriteHandler implements CellWriteHandler {

    }

    public static final EmptyCellWriteHandler EMPTY_CELL_WRITE_HANDLER = new EmptyCellWriteHandler();

    private void updateWriteHandler(List<?> list, Class<?> clazz,
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
            tmpFile = new File(file.getParentFile(), UUID.randomUUID().toString().replaceAll("-", "") + tmpSuffix + ".data");
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
        boolean createdMapTemplateFile = false;

        Map<String, Object> workbookContext = new HashMap<>();
        Map<Integer, Map<String, Object>> sheetContext = new HashMap<>();

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


        boolean isSupportPage = provider.supportPage();
        if (!isSupportPage) {
            Class<?> clazz = provider.getDataClass();
            List<?> data = provider.getData(null);
            if (TypeOf.typeOf(clazz, Map.class)) {
                if (!data.isEmpty()) {
                    templateFile = createTemplateFileByMap(file, (Map<?, ?>) data.get(0));
                    useTemplate = true;
                    createdMapTemplateFile = true;
                }
            }
            int size = data.size();
            if (size <= pageSize) {
                if (useTemplate) {
                    ExcelWriter excelWriter = FastExcel.write(tmpFile)
                            .withTemplate(templateFile)
                            .registerWriteHandler(writeHandler)
                            .excludeColumnFieldNames(excludeColumnNames)
                            .build();
                    WriteSheet writeSheet = FastExcel.write()
                            .sheet(0, sheetName)
                            .build();
                    updateWriteHandler(data, clazz, sheetContext, 0, workbookContext);
                    excelWriter.fill(data, writeSheet);
                    excelWriter.finish();
                } else {
                    updateWriteHandler(data, clazz, sheetContext, 0, workbookContext);
                    FastExcel.write(tmpFile, clazz)
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
                                templateFile = createTemplateFileByMap(file, (Map<?, ?>) item);
                                useTemplate = true;
                                createdMapTemplateFile = true;
                            }
                        }
                        // 处理使用模板的情况
                        if (useTemplate) {
                            excelWriter = FastExcel.write(tmpFile)
                                    .withTemplate(templateFile)
                                    .registerWriteHandler(writeHandler)
                                    .excludeColumnFieldNames(excludeColumnNames)
                                    .build();
                        } else {
                            excelWriter = FastExcel.write(tmpFile, clazz)
                                    .registerWriteHandler(writeHandler)
                                    .excludeColumnFieldNames(excludeColumnNames)
                                    .build();
                        }
                    }
                    if (count == pageSize) {
                        WriteSheet writeSheet = FastExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
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
                    WriteSheet writeSheet = FastExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
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
                            templateFile = createTemplateFileByMap(file, (Map<?, ?>) data.get(0));
                            useTemplate = true;
                            createdMapTemplateFile = true;
                        }
                    }
                    if (useTemplate) {
                        excelWriter = FastExcel.write(tmpFile)
                                .withTemplate(templateFile)
                                .registerWriteHandler(writeHandler)
                                .excludeColumnFieldNames(excludeColumnNames)
                                .build();
                    } else {
                        excelWriter = FastExcel.write(tmpFile, clazz)
                                .registerWriteHandler(writeHandler)
                                .excludeColumnFieldNames(excludeColumnNames)
                                .build();
                    }
                }

                int dsize = data.size();

                WriteSheet writeSheet = FastExcel.writerSheet(curPageIndex, ((curPageIndex == 0) ? sheetName : (sheetName + "-" + curPageIndex)))
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
                excelWriter = FastExcel.write(tmpFile, clazz)
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

        if (createdMapTemplateFile) {
            templateFile.delete();
        }

        if (latch != null) {
            latch.countDown();
        }
    }

    public static File createTemplateFileByMap(File file, Map<?, ?> map) {
        String tmpSuffix = "";
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            tmpSuffix = name.substring(idx);
        }

        File templateFile = new File(file.getParentFile(), UUID.randomUUID().toString().replaceAll("-", "") + tmpSuffix + ".data");
        File ret = new File(file.getParentFile(), UUID.randomUUID().toString().replaceAll("-", "") + tmpSuffix + ".data");
        try {
            InputStream is = ResourceUtil.getClasspathResourceAsStream("assets/excel/map-export-template.xlsx");
            StreamUtil.writeBytes(is, templateFile);

            List<Map<String, Object>> data = new ArrayList<>();
            for (Object item : map.keySet()) {
                String prop = String.valueOf(item);
                Map<String, Object> val = new HashMap<>();
                val.put("label", prop);
                val.put("prop", "{." + prop + "}");
                data.add(val);
            }

            ExcelWriter excelWriter = FastExcel.write(ret)
                    .withTemplate(templateFile)
                    .build();
            WriteSheet writeSheet = FastExcel.write()
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

}
