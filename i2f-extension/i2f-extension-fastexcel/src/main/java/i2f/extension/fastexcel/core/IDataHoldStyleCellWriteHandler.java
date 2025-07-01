package i2f.extension.fastexcel.core;

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.context.CellWriteHandlerContext;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/2/2 8:49
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class IDataHoldStyleCellWriteHandler extends IDataHoldCellWriteHandler {
    @Override
    public void apply(CellWriteHandlerContext context, Field field, Object rawData, Object rawObj, Cell cell, List<WriteCellData<?>> cellDataList, Integer rowIndex, Integer columnIndex, Head head, Boolean isHead) {
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle writeCellStyle = cellData.getWriteCellStyle();
        if (writeCellStyle == null) {
            writeCellStyle = new WriteCellStyle();
        }
        boolean ok = style(context, writeCellStyle, field, rawData, rawObj, cell, cellDataList, rowIndex, columnIndex, head, isHead);
        if (!ok) {
            return;
        }
        context.getFirstCellData().setWriteCellStyle(writeCellStyle);
        CellStyle originCellStyle = cellData.getOriginCellStyle();
        if (writeCellStyle != null || originCellStyle != null) {
            WriteWorkbookHolder writeWorkbookHolder = context.getWriteWorkbookHolder();
            context.getCell().setCellStyle(writeWorkbookHolder.createCellStyle(writeCellStyle, originCellStyle, CellDataTypeEnum.EMPTY));
        }
    }

    public abstract boolean style(CellWriteHandlerContext context, WriteCellStyle writeCellStyle, Field field, Object rawData, Object rawObj, Cell cell, List<WriteCellData<?>> cellDataList, Integer rowIndex, Integer columnIndex, Head head, Boolean isHead);
}
