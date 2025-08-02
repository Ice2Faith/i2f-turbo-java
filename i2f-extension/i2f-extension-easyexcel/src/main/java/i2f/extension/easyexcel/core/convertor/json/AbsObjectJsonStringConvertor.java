package i2f.extension.easyexcel.core.convertor.json;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author Ice2Faith
 * @date 2025/7/24 13:46
 */
public abstract class AbsObjectJsonStringConvertor<T> implements Converter<T> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return getSupportType();
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public T convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String text = cellData.getStringValue();
        if (text == null) {
            return null;
        }
        text = text.trim();
        return parseJson(text, cellData, contentProperty, globalConfiguration);
    }

    @Override
    public WriteCellData<?> convertToExcelData(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            return new WriteCellData<>("");
        }
        String text = toJson(value, contentProperty, globalConfiguration);
        return new WriteCellData<>(text);
    }

    public abstract Class<?> getSupportType();

    public abstract String toJson(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception;

    public abstract T parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception;

}
