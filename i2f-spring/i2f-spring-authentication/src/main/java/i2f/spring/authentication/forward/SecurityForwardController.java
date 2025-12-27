package i2f.spring.authentication.forward;


import i2f.resp.ApiResp;
import i2f.web.servlet.ServletContextUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Ice2Faith
 * @date 2022/4/7 17:07
 * @desc
 */
@RestController
@RequestMapping("forward")
public class SecurityForwardController {

    @RequestMapping("response")
    public ApiResp<?> response(HttpServletRequest request) {
        Object obj = ServletContextUtil.getForwardData(request);
        if (obj == null) {
            Throwable ex = ServletContextUtil.getForwardException(request);
            if (ex != null) {
                ex.printStackTrace();
                return ApiResp.error("internal error!");
            }
        }
        if (obj instanceof ApiResp) {
            return (ApiResp<?>) obj;
        }
        return ApiResp.success(obj);
    }
}
