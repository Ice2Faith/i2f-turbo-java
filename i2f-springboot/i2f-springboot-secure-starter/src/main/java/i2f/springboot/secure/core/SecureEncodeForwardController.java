package i2f.springboot.secure.core;


import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.util.SecureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ice2Faith
 * @date 2021/10/8
 */
@ConditionalOnBean(SecureConfig.class)
@ConditionalOnExpression("${i2f.springboot.config.secure.enc-url-forward.enable:true}")
@Slf4j
@Controller
public class SecureEncodeForwardController implements InitializingBean {
    @RequestMapping("/enc/{encodeUrl}")
    public void encodeUrlForward(@PathVariable("encodeUrl") String encodeUrl,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String trueUrl = SecureUtils.decodeEncTrueUrl(encodeUrl);
        if (!trueUrl.startsWith("/")) {
            trueUrl = "/" + trueUrl;
        }
        log.info("forward:\n\tsrc:" + encodeUrl + "\n\t" + "dst:" + trueUrl);

        request.getRequestDispatcher(trueUrl).forward(request, response);

        log.info("forward:\n\tsrc:" + encodeUrl + "\n\t" + "dst:" + trueUrl);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("EncodeRequestForwardController config done.");
    }
}
