package i2f.extension.easyexcel.core.convertor.atomics;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:31
 */
public class AtomicLongNumberConvertor implements Converter<AtomicLong> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return AtomicInteger.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public AtomicLong convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        BigDecimal num = cellData.getNumberValue();
        if (num == null) {
            return null;
        }
        return new AtomicLong(num.longValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(AtomicLong value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return NumberUtils.formatToCellData(value, contentProperty);
    }
}
