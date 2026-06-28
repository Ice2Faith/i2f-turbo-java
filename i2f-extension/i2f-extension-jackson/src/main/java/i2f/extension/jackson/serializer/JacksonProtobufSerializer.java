package i2f.extension.jackson.serializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.protobuf.ProtobufMapper;

public class JacksonProtobufSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new ProtobufMapper();

    public JacksonProtobufSerializer() {
    }

    public JacksonProtobufSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonProtobufSerializer(ProtobufMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
