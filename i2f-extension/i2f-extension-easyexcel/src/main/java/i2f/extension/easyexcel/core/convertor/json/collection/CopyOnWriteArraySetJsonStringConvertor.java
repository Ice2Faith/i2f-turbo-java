package i2f.extension.easyexcel.core.convertor.json.collection;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.easyexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class CopyOnWriteArraySetJsonStringConvertor extends AbsObjectJsonStringConvertor<CopyOnWriteArraySet<?>> {
    protected ObjectMapper objectMapper = new ObjectMapper();

    public CopyOnWriteArraySetJsonStringConvertor() {
    }

    public CopyOnWriteArraySetJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return CopyOnWriteArraySet.class;
    }

    @Override
    public String toJson(CopyOnWriteArraySet<?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public CopyOnWriteArraySet<?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<CopyOnWriteArraySet<?>>() {
        });
    }
}
