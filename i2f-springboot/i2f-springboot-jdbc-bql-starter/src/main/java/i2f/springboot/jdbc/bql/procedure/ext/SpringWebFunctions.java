package i2f.springboot.jdbc.bql.procedure.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ice2Faith
 * @date 2025/9/17 8:40
 */
public class SpringWebFunctions {
    public static volatile ObjectMapper objectMapper = new ObjectMapper();

    public static volatile RestTemplate restTemplate = new RestTemplate();

    public static String to_json(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }


    public static Object parse_json(Object json, Object type) throws JsonProcessingException {
        if (json == null) {
            return null;
        }
        Class<?> objClass = SpringContextFunctions.class_of(type);
        return objectMapper.readValue(String.valueOf(json), objClass);
    }

    public static Object http_get_object(Object url, Object type) {
        return restTemplate.getForObject(String.valueOf(url), SpringContextFunctions.class_of(type));
    }

    public static String http_get_text(Object url) {
        String text = restTemplate.getForObject(String.valueOf(url), String.class);
        return text;
    }

}
