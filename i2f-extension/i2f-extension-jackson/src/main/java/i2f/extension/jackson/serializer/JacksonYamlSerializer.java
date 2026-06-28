package i2f.extension.jackson.serializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;

public class JacksonYamlSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new YAMLMapper();

    public JacksonYamlSerializer() {
    }

    public JacksonYamlSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonYamlSerializer(YAMLMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
