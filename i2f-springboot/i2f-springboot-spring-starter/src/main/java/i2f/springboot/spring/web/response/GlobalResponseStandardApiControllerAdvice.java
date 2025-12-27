package i2f.springboot.spring.web.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.spring.web.api.StandardApiResponseConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * @author Ice2Faith
 * @date 2024/10/23 19:43
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class GlobalResponseStandardApiControllerAdvice implements ResponseBodyAdvice<Object> {
    String FLAG_ENABLE = "true";

    String FLAG_DISABLE = "false";

    // 是否是String返回值类型标记
    String STRING_RETURN_HEADER = "SECURE_RETURN_STRING";

    protected StandardApiResponseConverter<?> standardApiResponseConverter;
    protected StandardApiNotFoundResponseConvertor<?> standardApiNotFoundResponseConvertor;
    protected ObjectMapper objectMapper = new ObjectMapper();

    public GlobalResponseStandardApiControllerAdvice(StandardApiResponseConverter<?> standardApiResponseConverter, StandardApiNotFoundResponseConvertor<?> standardApiNotFoundResponseConvertor) {
        this.standardApiResponseConverter = standardApiResponseConverter;
        this.standardApiNotFoundResponseConvertor = standardApiNotFoundResponseConvertor;
    }

    public GlobalResponseStandardApiControllerAdvice(StandardApiResponseConverter<?> standardApiResponseConverter, StandardApiNotFoundResponseConvertor<?> standardApiNotFoundResponseConvertor, ObjectMapper objectMapper) {
        this.standardApiResponseConverter = standardApiResponseConverter;
        this.standardApiNotFoundResponseConvertor = standardApiNotFoundResponseConvertor;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        if (method == null) {
            return false;
        }
        StandardResp ann = method.getDeclaredAnnotation(StandardResp.class);
        if (ann == null) {
            ann = method.getAnnotation(StandardResp.class);
        }
        if (ann == null) {
            ann = method.getDeclaringClass().getDeclaredAnnotation(StandardResp.class);
        }
        if (ann == null) {
            ann = method.getDeclaringClass().getAnnotation(StandardResp.class);
        }
        return ann == null || ann.value();
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (object == null) {
            return null;
        }

        boolean isStringReturn = (object instanceof String);
        //如果返回值是LinkedHashMap，那么有可能是发生了错误，类型为LinkedHashMap<String,Object>
        //其中最常见的就是404页面找不到，在这里是能够截获的
        if (object instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) object;
            Object status = map.get("status");
            if (status != null && status instanceof Integer && ((Integer) status) == 404) {
                object = standardApiNotFoundResponseConvertor.convert(map);
            }
        }

        // 对不是标准返回类型的进行包装
        object = standardApiResponseConverter.convert(object);


        // 如果封装之后，不是String类型，则表示不再需要对String类型特殊处理
        // 后续如果使用过滤器，则不再需要针对String类型做特殊处理
        if (isStringReturn) {
            ServletServerHttpResponse sresp = (ServletServerHttpResponse) serverHttpResponse;
            HttpServletResponse resp = sresp.getServletResponse();
            resp.setHeader(STRING_RETURN_HEADER, FLAG_DISABLE);
        }
        if (isStringReturn) {
            try {
                object = objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage(), e);
            }
        }
        return object;
    }
}
