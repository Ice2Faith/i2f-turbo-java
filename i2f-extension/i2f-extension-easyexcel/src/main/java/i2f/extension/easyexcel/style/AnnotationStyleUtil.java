package i2f.extension.easyexcel.style;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import i2f.extension.easyexcel.annotation.ExcelCellStyle;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/2/2 11:14
 * @desc
 */
public class AnnotationStyleUtil {

    public static boolean parse(boolean head, WriteCellStyle style,
                                Field field, Object rawData, Object rawObj,
                                int rowIndex, int colIndex,
                                Cell cell, CellWriteHandlerContext context,
                                Map<String, Object> sheetContext, Map<String, Object> workbookContext) {
        ExcelStyleCallbackMeta meta = parseStyleCallbackMeta(field, rawData, rawObj, rowIndex, colIndex);

        ExcelCellStyle[] styles = meta.style;
        List<ExcelCellStyle> list = new ArrayList<>();
        if (styles != null) {
            for (ExcelCellStyle item : styles) {
                if (item == null) {
                    continue;
                }
                if (item.head() == head) {
                    list.add(item);
                }
            }
        }

        ExcelCellStyle[] arr = list.toArray(new ExcelCellStyle[0]);
        meta.style = arr;

        return applyFieldStyleMeta(style, meta, cell, context, sheetContext);
    }


    public static boolean applyFieldStyleMeta(WriteCellStyle style,
                                              ExcelStyleCallbackMeta meta,
                                              Cell cell, CellWriteHandlerContext context,
                                              Map<String, Object> sheetContext) {
        Map<String, Boolean> spelMap = new HashedMap<>();
        boolean ret = false;

        int colWidth = -1;

        boolean hasUrlLink = false;
        HyperlinkType linkType = HyperlinkType.URL;

        String formula = "";

        boolean commentSpel = false;
        String comment = "";

        String[] selectionList = null;
        String selectionSpel = "";

        if (meta.style != null) {
            for (ExcelCellStyle item : meta.style) {
                if (item == null) {
                    continue;
                }
                String spel = item.spel();
                if (StringUtils.isEmpty(spel)) {
                    continue;
                }
                boolean ok = false;
                if (spelMap.containsKey(spel)) {
                    ok = spelMap.get(spel);
                } else {
                    ok = resolveSpelExpression(spel, meta);
                }
                spelMap.put(spel, ok);
                ret = ret || ok;
                if (ok) {
                    createCellStyleByAnnotation(item, style);

                    colWidth = Math.max(colWidth, item.colWidth());

                    hasUrlLink = hasUrlLink || item.hyperLink();
                    if (item.linkType() != HyperlinkType.NONE) {
                        linkType = item.linkType();
                    }

                    if (!StringUtils.isEmpty(item.formula())) {
                        formula = item.formula();
                    }

                    if (!StringUtils.isEmpty(item.comment())) {
                        comment = item.comment();
                        commentSpel = item.commentSpel();
                    }

                    if (item.selection().length > 0) {
                        selectionList = item.selection();
                    }

                    if (!StringUtils.isEmpty(item.selectionSpel())) {
                        selectionSpel = item.selectionSpel();
                    }
                }
            }
        }

        if (ret) {
            WriteWorkbookHolder workbookHolder = context.getWriteWorkbookHolder();
            Workbook workbook = workbookHolder.getWorkbook();
            WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
            Sheet sheet = writeSheetHolder.getSheet();
            CreationHelper creationHelper = workbook.getCreationHelper();

            if (colWidth > 0) {
                String colWidthKey = "col-width:" + meta.field;
                if (!sheetContext.containsKey(colWidthKey)) {
                    sheetContext.put(colWidthKey, true);
                    sheet.setColumnWidth(meta.col, colWidth * 256);
                }
            }


            if (hasUrlLink) {
                if (linkType == null || linkType == HyperlinkType.NONE) {
                    linkType = HyperlinkType.URL;
                }
                Hyperlink link = creationHelper.createHyperlink(linkType);
                link.setAddress(meta.val + "");
                cell.setHyperlink(link);
            }

            if (!StringUtils.isEmpty(comment)) {

                if (commentSpel) {
                    try {
                        Object obj = StandaloneSpelExpressionResolver.getValue(comment, Object.class, meta);
                        if (obj != null) {
                            comment = String.valueOf(obj);
                        }
                    } catch (Exception e) {

                    }
                }

                Comment cellComment = cell.getCellComment();
                if (cellComment == null) {
                    Drawing<?> drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = creationHelper.createClientAnchor();

                    // fixed exception: Multiple cell comments in one cell are not allowed, cell: A1
                    anchor.setRow1(meta.row);
                    anchor.setCol1(meta.col);
                    anchor.setRow2(anchor.getRow1() + 2);
                    anchor.setCol2(anchor.getCol1() + 2);

                    cellComment = drawing.createCellComment(anchor);
                }

                RichTextString richTextString = creationHelper.createRichTextString(comment);
                cellComment.setString(richTextString);
                cellComment.setAuthor("Apache POI");
                cell.setCellComment(cellComment);
            }

            if (!StringUtils.isEmpty(formula)) {
                cell.setCellFormula(formula);
            }

            String fieldSelectionKey = "selection:" + meta.field;
            if (!sheetContext.containsKey(fieldSelectionKey)) {
                sheetContext.put(fieldSelectionKey, true);

                Set<String> listSet = new LinkedHashSet<>();
                if (!StringUtils.isEmpty(selectionSpel)) {
                    Object obj = StandaloneSpelExpressionResolver.getValue(selectionSpel, Object.class, meta);
                    if (obj instanceof Collection) {
                        Collection<?> col = (Collection<?>) obj;
                        for (Object item : col) {
                            if (item == null) {
                                continue;
                            }
                            String str = String.valueOf(item);
                            if ("".equals(str)) {
                                continue;
                            }
                            listSet.add(str);
                        }
                    }
                } else if (selectionList != null && selectionList.length > 0) {
                    for (String item : selectionList) {
                        String str = String.valueOf(item);
                        if ("".equals(str)) {
                            continue;
                        }
                        listSet.add(str);
                    }
                }

                if (!listSet.isEmpty()) {
                    DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
                    String[] items = listSet.toArray(new String[0]);
                    DataValidationConstraint listConstraint = dataValidationHelper.createExplicitListConstraint(items);

                    CellRangeAddressList cellAddresses = new CellRangeAddressList(0, 65535, meta.col, meta.col);
                    DataValidation validation = dataValidationHelper.createValidation(listConstraint, cellAddresses);
                    sheet.addValidationData(validation);
                }

            }

        }

        return ret;
    }

    private static ConcurrentHashMap<Field, ExcelStyleCallbackMeta> cacheFieldMeta = new ConcurrentHashMap<>();

    private static ExcelStyleCallbackMeta getBasicStyleCallbackMeta(Field field) {

        if (field != null && cacheFieldMeta.containsKey(field)) {
            return cacheFieldMeta.get(field);
        }

        ExcelProperty propAnn = null;
        ExcelCellStyle[] cellStyleAnn = null;

        if (field != null) {
            Set<ExcelCellStyle> styles = new LinkedHashSet<>();
            propAnn = ReflectResolver.getAnnotation(field, ExcelProperty.class);
            Set<ExcelCellStyle> anns = ReflectResolver.getAnnotations(field, ExcelCellStyle.class);
            boolean hasData = false;
            boolean hasHead = false;
            if (anns != null) {
                for (ExcelCellStyle item : anns) {
                    if (item.head()) {
                        hasHead = true;
                    }
                    hasData = true;
                    styles.add(item);
                }
            }
            if (!hasData || !hasHead) {
                anns = ReflectResolver.getAnnotations(field, ExcelCellStyle.class);
                if (anns != null) {
                    for (ExcelCellStyle item : anns) {
                        if (item.head()) {
                            if (!hasHead) {
                                styles.add(item);
                            }
                        } else {
                            if (!hasData) {
                                styles.add(item);
                            }
                        }
                    }
                }
            }
            cellStyleAnn = styles.toArray(new ExcelCellStyle[0]);
        }

        int order = Integer.MAX_VALUE;
        int index = -1;
        if (propAnn != null) {
            order = propAnn.order();
            index = propAnn.index();
        }

        ExcelStyleCallbackMeta meta = new ExcelStyleCallbackMeta();
        meta.field = field;
        meta.index = index;
        meta.order = order;
        meta.property = propAnn;
        meta.style = cellStyleAnn;

        if (field != null) {
            cacheFieldMeta.put(field, meta);
        }
        return meta;
    }

    public static ExcelStyleCallbackMeta parseStyleCallbackMeta(Field field, Object rawData, Object rawObj, int rowIndex, int colIndex) {
        ExcelStyleCallbackMeta proto = getBasicStyleCallbackMeta(field);

        ExcelStyleCallbackMeta meta = new ExcelStyleCallbackMeta();
        meta.field = proto.field;
        meta.index = proto.index;
        meta.order = proto.order;
        meta.property = proto.property;
        meta.style = proto.style;

        meta.record = rawObj;
        meta.val = rawData;
        meta.row = rowIndex;
        meta.col = colIndex;

        return meta;
    }

    public static boolean resolveSpelExpression(String express, ExcelStyleCallbackMeta meta) {
        if (!StringUtils.isEmpty(express)) {
            return StandaloneSpelExpressionResolver.getBool(express, meta);
        }
        return false;
    }

    public static void createCellStyleByAnnotation(ExcelCellStyle ann, WriteCellStyle style) {
        if (ann == null) {
            return;
        }
        if (style == null) {
            return;
        }
        WriteFont cellWriteFont = style.getWriteFont();
        if (cellWriteFont == null) {
            cellWriteFont = new WriteFont();
        }
        if (!StringUtils.isEmpty(ann.fontName())) {
            cellWriteFont.setFontName(ann.fontName());
        }
        cellWriteFont.setBold(ann.fontBold().value());
        cellWriteFont.setItalic(ann.fontItalic().value());
        if (ann.fontUnderline()) {
            cellWriteFont.setUnderline((byte) 1);
        }
        cellWriteFont.setStrikeout(ann.fontStrikeout().value());
        if (ann.fontColor() != IndexedColors.AUTOMATIC) {
            cellWriteFont.setColor(ann.fontColor().getIndex());
        }

        style.setWriteFont(cellWriteFont);

        if (ann.backgroundColor() != IndexedColors.AUTOMATIC) {
            style.setFillForegroundColor(ann.backgroundColor().getIndex());
            style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        }

        style.setHorizontalAlignment(ann.alignHorizontal());
        style.setVerticalAlignment(ann.alignVertical());

        style.setWrapped(ann.textWrapped().value());
        style.setQuotePrefix(ann.quotePrefix().value());

    }
}
