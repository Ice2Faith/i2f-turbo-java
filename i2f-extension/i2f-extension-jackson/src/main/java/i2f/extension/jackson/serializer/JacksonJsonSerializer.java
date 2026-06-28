package i2f.extension.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import i2f.serialize.std.str.json.IJsonSerializer;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class JacksonJsonSerializer extends AbsJacksonSerializer implements IJsonSerializer {
    public static String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss SSS";
    public static JacksonJsonSerializer INSTANCE = new Supplier<JacksonJsonSerializer>() {
        @Override
        public JacksonJsonSerializer get() {
            JsonMapper mapper = JsonMapper.builder()
                    .defaultLocale(Locale.getDefault())
                    .defaultDateFormat(new SimpleDateFormat(DEFAULT_DATE_FMT))
                    .withAllConfigOverrides(config -> {
                        config.setDefaultInclusion(JsonInclude.Value.ALL_ALWAYS);
                    })
                    .build();
            return new JacksonJsonSerializer(mapper);
        }
    }.get();

    public JacksonJsonSerializer() {
    }

    public JacksonJsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonSerializer(JsonMapper mapper) {
        this.mapper = mapper;
    }

    private ObjectMapper mapper = new JsonMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return deserialize(json, new TypeReference<Map<String, Object>>() {
        });
    }
}
