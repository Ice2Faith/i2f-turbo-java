package i2f.extension.easyexcel.core.convertor.atomics;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:31
 */
public class AtomicBooleanNumberConvertor implements Converter<AtomicBoolean> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return AtomicBoolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public AtomicBoolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        BigDecimal num = cellData.getNumberValue();
        if (num == null) {
            return null;
        }
        return new AtomicBoolean(num.longValue() != 0);
    }

    @Override
    public WriteCellData<?> convertToExcelData(AtomicBoolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        boolean ok = value.get();
        return NumberUtils.formatToCellData(ok ? 1 : 0, contentProperty);
    }
}
