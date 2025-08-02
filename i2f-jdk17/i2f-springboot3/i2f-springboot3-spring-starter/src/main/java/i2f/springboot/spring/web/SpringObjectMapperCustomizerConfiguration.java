package i2f.springboot.spring.web;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import i2f.extension.jackson.datetime.deserializer.JacksonDateDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalDateDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalDateTimeDeserializer;
import i2f.extension.jackson.datetime.deserializer.JacksonLocalTimeDeserializer;
import i2f.extension.jackson.datetime.serializer.JacksonTemporalSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

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

    private String localDateFormat;

    private String localTimeFormat;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                if (enableLongToString) {
                    builder.serializerByType(Long.class, ToStringSerializer.instance);
                    builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
                    builder.serializerByType(long.class, ToStringSerializer.instance);
                }
                if (StringUtils.isEmpty(dateFormat)) {
                    builder.deserializerByType(Date.class, new JacksonDateDeserializer());
                }
                if (!StringUtils.isEmpty(dateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                    builder.serializerByType(LocalDateTime.class, new JacksonTemporalSerializer<LocalDateTime>(formatter));
                    builder.deserializerByType(LocalDateTime.class, new JacksonLocalDateTimeDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localDateFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localDateFormat);
                    builder.serializerByType(LocalDate.class, new JacksonTemporalSerializer<LocalDate>(formatter));
                    builder.deserializerByType(LocalDate.class, new JacksonLocalDateDeserializer(formatter));
                }
                if (!StringUtils.isEmpty(localTimeFormat)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(localTimeFormat);
                    builder.serializerByType(LocalTime.class, new JacksonTemporalSerializer<LocalTime>(formatter));
                    builder.deserializerByType(LocalTime.class, new JacksonLocalTimeDeserializer(formatter));
                }
            }
        };
    }
}
