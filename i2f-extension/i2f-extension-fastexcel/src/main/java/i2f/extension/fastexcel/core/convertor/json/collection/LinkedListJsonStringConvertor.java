package i2f.extension.fastexcel.core.convertor.json.collection;

import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.fastexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.LinkedList;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class LinkedListJsonStringConvertor extends AbsObjectJsonStringConvertor<LinkedList<?>> {
    protected ObjectMapper objectMapper=new ObjectMapper();

    public LinkedListJsonStringConvertor() {
    }

    public LinkedListJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return LinkedList.class;
    }

    @Override
    public String toJson(LinkedList<?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public LinkedList<?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<LinkedList<?>>() {});
    }
}
