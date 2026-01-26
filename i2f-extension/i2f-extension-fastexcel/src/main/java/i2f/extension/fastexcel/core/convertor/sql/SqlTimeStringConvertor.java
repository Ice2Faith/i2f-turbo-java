package i2f.extension.fastexcel.core.convertor.sql;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.util.DateUtils;

import java.util.Date;


/**
 * @author Ice2Faith
 * @date 2026/1/7 15:08
 * @desc
 */
public class SqlTimeStringConvertor implements Converter<java.sql.Time> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return java.sql.Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public java.sql.Time convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Date date= contentProperty != null && contentProperty.getDateTimeFormatProperty() != null ?
                DateUtils.parseDate(cellData.getStringValue(), contentProperty.getDateTimeFormatProperty().getFormat()) :
                DateUtils.parseDate(cellData.getStringValue(), (String)null);
        return new java.sql.Time(date.getTime());
    }

    @Override
    public WriteCellData<?> convertToExcelData(java.sql.Time value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return contentProperty != null && contentProperty.getDateTimeFormatProperty() != null ?
                new WriteCellData(DateUtils.format(value, contentProperty.getDateTimeFormatProperty().getFormat())) :
                new WriteCellData(DateUtils.format(value, (String)null));
    }
}
