package i2f.extension.jackson.serializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.csv.CsvMapper;

public class JacksonCsvSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new CsvMapper();

    public JacksonCsvSerializer() {
    }

    public JacksonCsvSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonCsvSerializer(CsvMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
