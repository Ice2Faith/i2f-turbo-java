package i2f.extension.fastexcel.core.convertor.atomics;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.util.NumberUtils;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:31
 */
public class AtomicIntegerNumberConvertor implements Converter<AtomicInteger> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return AtomicInteger.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public AtomicInteger convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        BigDecimal num = cellData.getNumberValue();
        if(num==null){
            return null;
        }
        return new AtomicInteger(num.intValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(AtomicInteger value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return NumberUtils.formatToCellData(value, contentProperty);
    }
}
