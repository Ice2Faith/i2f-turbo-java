package i2f.extension.fastexcel.style;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.context.CellWriteHandlerContext;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import i2f.extension.fastexcel.core.IDataHoldStyleCellWriteHandler;
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
public class AnnotationExcelStyleCellWriteHandler extends IDataHoldStyleCellWriteHandler {

    @Override
    public boolean style(CellWriteHandlerContext context, WriteCellStyle writeCellStyle, Field field, Object rawData, Object rawObj, Cell cell, List<WriteCellData<?>> cellDataList, Integer rowIndex, Integer columnIndex, Head head, Boolean isHead) {
//        if (isHead) {
//            return false;
//        }
        Map<String, Object> workbookContext = getWorkbookContext();
        Map<String, Object> sheetContext = getSheetContext();
        boolean ok = AnnotationStyleUtil.parse(isHead == true,
                writeCellStyle,
                field, rawData, rawObj,
                rowIndex, columnIndex,
                cell, context,
                sheetContext, workbookContext);
        return ok;
    }
}
