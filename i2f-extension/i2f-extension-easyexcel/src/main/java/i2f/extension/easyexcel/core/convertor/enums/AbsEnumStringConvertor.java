package i2f.extension.easyexcel.core.convertor.enums;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:23
 */
public abstract class AbsEnumStringConvertor<T extends Enum> implements Converter<T> {
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
        if(text==null){
            return null;
        }
        text=text.trim();
        if(text.isEmpty()){
            return null;
        }
        return parseEnum(text,cellData,contentProperty,globalConfiguration);
    }

    @Override
    public WriteCellData<?> convertToExcelData(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(value==null){
            return new WriteCellData<>("");
        }
        String text=toText(value,contentProperty,globalConfiguration);
        return new WriteCellData<>(text);
    }

    public abstract Class<T> getSupportType() ;

    protected String toText(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception{
        return value.name();
    }

    public T parseEnum(String text,ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception{
        Class<T> enumType = getSupportType();
        T[] arr = enumType.getEnumConstants();
        for (T item : arr) {
            if(item.name().equalsIgnoreCase(text)){
                return item;
            }
        }
        return null;
    }

}
