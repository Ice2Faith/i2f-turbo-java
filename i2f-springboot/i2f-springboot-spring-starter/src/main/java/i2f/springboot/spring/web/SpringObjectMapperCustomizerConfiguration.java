package i2f.springboot.spring.web;

import i2f.extension.jackson.datetime.deserializer.JacksonDateDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalDateDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalDateTimeDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalTimeDeserializer;
import i2f.extension.jackson.datetime.serializer.JacksonTemporalSerializer;
import i2f.extension.jackson.types.JacksonLong2StringSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:00
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.jackson.enable:true}")
@Data
@ConfigurationProperties(prefix = "i2f.spring.jackson")
public class SpringObjectMapperCustomizerConfiguration {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss SSS}")
    private String dateFormat;

    private boolean enableLongToString = true;

    private boolean enableGlobalLong2String = true;

    private String localDateFormat;

    private String localTimeFormat;

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return new JsonMapperBuilderCustomizer() {
            @Override
            public void customize(JsonMapper.Builder builder) {
                SimpleModule simpleModule = new SimpleModule();

                if (enableLongToString) {
                    if (enableGlobalLong2String) {
                        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
                    } else {
                        simpleModule.addSerializer(Long.class, new JacksonLong2StringSerializer());
                        simpleModule.addSerializer(Long.TYPE, new JacksonLong2StringSerializer());
                        simpleModule.addSerializer(long.class, new JacksonLong2StringSerializer());
                    }
                }
                if (!StringUtils.isEmpty(dateFormat)) {
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                    simpleModule.addDeserializer(Date.class, new JacksonDateDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(dateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                    simpleModule.addSerializer(LocalDateTime.class, new JacksonTemporalSerializer<LocalDateTime>(formatter));
                    simpleModule.addDeserializer(LocalDateTime.class, new JacksonLocalDateTimeDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localDateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localDateFormat);
                    simpleModule.addSerializer(LocalDate.class, new JacksonTemporalSerializer<LocalDate>(formatter));
                    simpleModule.addDeserializer(LocalDate.class, new JacksonLocalDateDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localTimeFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localTimeFormat);
                    simpleModule.addSerializer(LocalTime.class, new JacksonTemporalSerializer<LocalTime>(formatter));
                    simpleModule.addDeserializer(LocalTime.class, new JacksonLocalTimeDeserializer(formatter));
                }

                builder.addModule(simpleModule);
            }
        };
    }
}
