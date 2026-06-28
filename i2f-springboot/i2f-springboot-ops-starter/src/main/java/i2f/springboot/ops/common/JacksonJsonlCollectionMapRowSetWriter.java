package i2f.springboot.ops.common;

import i2f.rowset.impl.json.AbsJsonlMapRowSetWriter;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/2 23:01
 * @desc
 */
@Data
@NoArgsConstructor
public class JacksonJsonlCollectionMapRowSetWriter<M extends Map<String, Object>> extends AbsJsonlMapRowSetWriter<M> {
    protected ObjectMapper mapper;

    public JacksonJsonlCollectionMapRowSetWriter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
