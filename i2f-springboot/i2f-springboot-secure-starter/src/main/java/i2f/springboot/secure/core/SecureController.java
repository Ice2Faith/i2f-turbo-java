package i2f.springboot.secure.core;

import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.consts.SecureErrorCode;
import i2f.springboot.secure.exception.SecureException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ice2Faith
 * @date 2022/6/30 11:34
 * @desc
 */
@ConditionalOnBean(SecureConfig.class)
@ConditionalOnExpression("${i2f.springboot.config.secure.api.enable:true}")
@RestController
@RequestMapping("secure")
public class SecureController {

    @Autowired
    private SecureTransfer secureTransfer;

    @Autowired
    private SecureConfig secureConfig;

    @SecureParams(in = false, out = false)
    @PostMapping("key")
    public String key() {
        String pubKey = secureTransfer.getWebAsymPublicKey();
        return pubKey;
    }

    @SecureParams(in = false, out = false)
    @PostMapping("clientKey")
    public String clientKey(HttpServletRequest request) throws Exception {
        if (secureConfig.isEnableSwapAsymKey()) {
            throw new SecureException(SecureErrorCode.BAD_SECURE_REQUEST, "服务端不允许请求秘钥策略");
        }
        String priKey = secureTransfer.getWebClientAsymPrivateKey(request);
        return priKey;
    }

    @Data
    @NoArgsConstructor
    public static class SwapKey {
        private String key;
    }

    @SecureParams(in = false, out = false)
    @PostMapping("swapKey")
    public String swapKey(HttpServletRequest request, @RequestBody SwapKey pair) throws Exception {
        String pubKey = secureTransfer.getWebAsymPublicKeyAndSwap(request, pair.getKey());
        return pubKey;
    }
}
