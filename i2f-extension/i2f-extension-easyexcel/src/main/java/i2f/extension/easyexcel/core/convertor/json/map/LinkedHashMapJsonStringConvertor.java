package i2f.extension.easyexcel.core.convertor.json.map;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.easyexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.LinkedHashMap;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class LinkedHashMapJsonStringConvertor extends AbsObjectJsonStringConvertor<LinkedHashMap<?, ?>> {
    protected ObjectMapper objectMapper = new ObjectMapper();

    public LinkedHashMapJsonStringConvertor() {
    }

    public LinkedHashMapJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return LinkedHashMap.class;
    }

    @Override
    public String toJson(LinkedHashMap<?, ?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public LinkedHashMap<?, ?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<LinkedHashMap<?, ?>>() {
        });
    }
}
