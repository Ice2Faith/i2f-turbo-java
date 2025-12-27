package i2f.springboot.nginx.rtmp.auth.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/1/14 20:47
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("/api/rtmp")
public class NginxRtmpAuthController {

    @Autowired
    private NginxRtmpAuthTokenValidator validator;

    /**
     * nginxData字段介绍
     * call=play。
     * addr - 客户端 IP 地址。
     * app - application 名。
     * flashVer - 客户端 flash 版本。
     * swfUrl - 客户端 swf url。
     * tcUrl - tcUrl。
     * pageUrl - 客户端页面 url。
     * name - 流名。
     * <p>
     * nginx 回调通过判断HTTP Status进行判断是否通过
     * 因此，需要设置response.status
     * 当为2xx时，表示回调成功，否则表示回调失败
     *
     * @param nginxData
     * @param token
     * @return
     */
    @PostMapping(value = "/auth")
    public Object auth(@RequestParam Map<String, Object> nginxData,
                       @RequestParam(value = "token", required = false) String token,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        log.info("nginxData:" + nginxData);
        log.info("token:" + token);
        try {
            if (!validator.validate(request, token, nginxData)) {
                log.error("auth failure!");
                response.setStatus(500);
                return "{\"code\":500,\"detail\":\"ERROR\"}";
            }
        } catch (Exception e) {
            log.error("auth failure!");
            response.setStatus(500);
            return "{\"code\":500,\"detail\":\"ERROR\"}";
        }

        log.info("auth success!");
        return "{\"code\":200,\"detail\":\"SUCCESS\"}";
    }


}