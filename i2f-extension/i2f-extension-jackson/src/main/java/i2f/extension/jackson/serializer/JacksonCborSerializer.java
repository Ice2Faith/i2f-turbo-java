package i2f.extension.jackson.serializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.cbor.CBORMapper;

public class JacksonCborSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new CBORMapper();

    public JacksonCborSerializer() {
    }

    public JacksonCborSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonCborSerializer(CBORMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
