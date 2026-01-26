package i2f.extension.easyexcel.core.convertor.sql;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.util.Date;


/**
 * @author Ice2Faith
 * @date 2026/1/7 15:08
 * @desc
 */
public class SqlDateStringConvertor implements Converter<java.sql.Date> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return java.sql.Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public java.sql.Date convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Date date= contentProperty != null && contentProperty.getDateTimeFormatProperty() != null ?
                DateUtils.parseDate(cellData.getStringValue(), contentProperty.getDateTimeFormatProperty().getFormat()) :
                DateUtils.parseDate(cellData.getStringValue(), (String)null);
        return new java.sql.Date(date.getTime());
    }

    @Override
    public WriteCellData<?> convertToExcelData(java.sql.Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return contentProperty != null && contentProperty.getDateTimeFormatProperty() != null ?
                new WriteCellData(DateUtils.format(value, contentProperty.getDateTimeFormatProperty().getFormat())) :
                new WriteCellData(DateUtils.format(value, (String)null));
    }
}
