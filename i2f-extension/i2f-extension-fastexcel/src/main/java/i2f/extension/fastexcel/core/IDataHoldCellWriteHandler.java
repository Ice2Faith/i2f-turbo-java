package i2f.extension.fastexcel.core;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.handler.context.CellWriteHandlerContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/2/2 8:49
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class IDataHoldCellWriteHandler implements CellWriteHandler {
    protected List<?> list;
    protected int offsetIndex = 0;
    protected Class<?> clazz;
    protected Map<String, Object> sheetContext;
    protected Map<String, Object> workbookContext;

    public IDataHoldCellWriteHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    public IDataHoldCellWriteHandler(List<?> list, Class<?> clazz) {
        this.list = list;
        this.clazz = clazz;
    }

    public Object getRawData(int dataIndex) {
        if (list == null) {
            return null;
        }
        if (dataIndex < 0) {
            return null;
        }
        return list.get(dataIndex);
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        ExcelContentProperty property = context.getExcelContentProperty();
        Field field = property.getField();

        Head head = context.getHeadData();
        Boolean isHead = context.getHead();
        int headCount = head.getHeadNameList().size();

        List<WriteCellData<?>> cellDataList = context.getCellDataList();

        Cell cell = context.getCell();

        Integer rowIndex = context.getRowIndex();
        Integer columnIndex = context.getColumnIndex();

        int dataIndex = rowIndex - headCount - offsetIndex;

        Object rawObj = getRawData(dataIndex);
        Object rawData = null;
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (rawObj != null) {
                rawData = field.get(rawObj);
            }
        } catch (Exception e) {

        }

        apply(context, field, rawData, rawObj, cell, cellDataList, rowIndex, columnIndex, head, isHead);
    }

    public abstract void apply(CellWriteHandlerContext context,
                               Field field, Object rawData, Object rawObj,
                               Cell cell, List<WriteCellData<?>> cellDataList,
                               Integer rowIndex, Integer columnIndex,
                               Head head, Boolean isHead);


}
