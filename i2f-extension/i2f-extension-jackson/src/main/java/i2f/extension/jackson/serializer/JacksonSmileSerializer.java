package i2f.extension.jackson.serializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.smile.SmileMapper;

public class JacksonSmileSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new SmileMapper();

    public JacksonSmileSerializer() {
    }

    public JacksonSmileSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonSmileSerializer(SmileMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
