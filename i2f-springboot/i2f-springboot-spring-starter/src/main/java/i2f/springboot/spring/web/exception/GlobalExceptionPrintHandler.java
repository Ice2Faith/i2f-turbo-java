package i2f.springboot.spring.web.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Ice2Faith
 * @date 2024/10/23 18:58
 * @desc
 */
@Data
@NoArgsConstructor
@Slf4j
public class GlobalExceptionPrintHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String str = String.format("[%5s] [%20s] %s exception : %s",
                request.getMethod(),
                request.getContentType(),
                request.getRequestURL(),
                e.getMessage());
        log.error(str, e);
        return null;
    }
}
