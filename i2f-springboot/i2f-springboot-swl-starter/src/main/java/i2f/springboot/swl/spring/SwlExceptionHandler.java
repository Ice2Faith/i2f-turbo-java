package i2f.springboot.swl.spring;

import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/7/12 11:18
 * @desc
 */
@Order(Integer.MAX_VALUE)
@ConditionalOnExpression("${i2f.swl.exception-handler.enable:true}")
@RestControllerAdvice
public class SwlExceptionHandler {

    @Autowired(required = false)
    private ISwlExceptionAdvideConverter advideConverter;

    @ExceptionHandler(SwlException.class)
    public Object handleSwlException(SwlException e) {
        e.printStackTrace();
        if (advideConverter != null) {
            return advideConverter.convert(e);
        }
        SwlCode swlCode = SwlCode.INTERNAL_EXCEPTION;
        int code = e.code();
        for (SwlCode value : SwlCode.values()) {
            if (value.code() == code) {
                swlCode = value;
                break;
            }
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("error", "swl error!");
        ret.put("code", swlCode.code());
        ret.put("message", swlCode.name());
        ret.put("status", 500);
        return ret;
    }
}
