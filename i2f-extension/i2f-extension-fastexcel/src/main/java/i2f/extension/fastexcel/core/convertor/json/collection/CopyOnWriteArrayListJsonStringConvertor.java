package i2f.extension.fastexcel.core.convertor.json.collection;

import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.fastexcel.core.convertor.json.AbsObjectJsonStringConvertor;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/7/24 14:04
 */
public class CopyOnWriteArrayListJsonStringConvertor extends AbsObjectJsonStringConvertor<CopyOnWriteArrayList<?>> {
    protected ObjectMapper objectMapper = new ObjectMapper();

    public CopyOnWriteArrayListJsonStringConvertor() {
    }

    public CopyOnWriteArrayListJsonStringConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getSupportType() {
        return CopyOnWriteArrayList.class;
    }

    @Override
    public String toJson(CopyOnWriteArrayList<?> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public CopyOnWriteArrayList<?> parseJson(String text, ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return objectMapper.readValue(text, new TypeReference<CopyOnWriteArrayList<?>>() {
        });
    }
}
