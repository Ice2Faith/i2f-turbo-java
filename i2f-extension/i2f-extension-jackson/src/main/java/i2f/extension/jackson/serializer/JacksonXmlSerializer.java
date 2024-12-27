package i2f.extension.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import i2f.serialize.std.str.xml.IXmlSerializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Supplier;

public class JacksonXmlSerializer extends AbsJacksonSerializer implements IXmlSerializer {
    public static String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss SSS";
    public static JacksonXmlSerializer INSTANCE = new Supplier<JacksonXmlSerializer>() {
        @Override
        public JacksonXmlSerializer get() {
            XmlMapper mapper = new XmlMapper();
            mapper.setLocale(Locale.getDefault());
            mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FMT));
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            return new JacksonXmlSerializer(mapper);
        }
    }.get();

    private ObjectMapper mapper = new XmlMapper();

    public JacksonXmlSerializer() {
    }

    public JacksonXmlSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonXmlSerializer(XmlMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
