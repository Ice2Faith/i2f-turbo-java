package i2f.extension.fastexcel.core.convertor.json.collection;

import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.fastexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class CollectionJsonStringConvertor extends AbsObjectJsonStringConvertor<Collection<?>> {
    protected ObjectMapper objectMapper = new ObjectMapper();

    public CollectionJsonStringConvertor() {
    }

    public CollectionJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return Collection.class;
    }

    @Override
    public String toJson(Collection<?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public Collection<?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<ArrayList<?>>() {
        });
    }
}
