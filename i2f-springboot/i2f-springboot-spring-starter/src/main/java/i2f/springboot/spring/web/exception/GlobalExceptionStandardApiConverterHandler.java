package i2f.springboot.spring.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.spring.web.api.StandardApiExceptionConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2024/10/23 18:58
 * @desc
 */
@Data
@NoArgsConstructor
@Slf4j
public class GlobalExceptionStandardApiConverterHandler<R> implements HandlerExceptionResolver {
    protected StandardApiExceptionConverter<R> exceptionConverter;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected int httpStatus = 200;

    public GlobalExceptionStandardApiConverterHandler(StandardApiExceptionConverter<R> exceptionConverter) {
        this.exceptionConverter = exceptionConverter;
    }

    public GlobalExceptionStandardApiConverterHandler(StandardApiExceptionConverter<R> exceptionConverter, ObjectMapper objectMapper) {
        this.exceptionConverter = exceptionConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        ModelAndView mv = new ModelAndView();
        try {
            R obj = exceptionConverter.convert(e);
            String json = objectMapper.writeValueAsString(obj);
            response.setStatus(httpStatus);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            ServletOutputStream os = response.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException ex) {
            log.warn(ex.getMessage(), e);
        }
        return mv;
    }
}
