package i2f.extension.easyexcel.core.convertor.json.map;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.easyexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class ConcurrentHashMapJsonStringConvertor extends AbsObjectJsonStringConvertor<ConcurrentHashMap<?,?>> {
    protected ObjectMapper objectMapper=new ObjectMapper();

    public ConcurrentHashMapJsonStringConvertor() {
    }

    public ConcurrentHashMapJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return ConcurrentHashMap.class;
    }

    @Override
    public String toJson(ConcurrentHashMap<?, ?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public ConcurrentHashMap<?, ?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<ConcurrentHashMap<?, ?>>() {});
    }
}
