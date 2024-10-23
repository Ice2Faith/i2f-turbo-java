package i2f.springboot.spring.web.response;

import java.util.LinkedHashMap;

/**
 * @author Ice2Faith
 * @date 2024/10/23 20:03
 * @desc
 */
public interface StandardApiNotFoundResponseConvertor<R> {
    R convert(LinkedHashMap<String, Object> response);
}
