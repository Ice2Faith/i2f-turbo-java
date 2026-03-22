package i2f.springboot.swl.spring;


import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.annotation.SecureParams;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2022/6/30 11:34
 * @desc
 */
@AutoConfigureAfter(SwlSpringAutoConfiguration.class)
@ConditionalOnBean(SwlTransfer.class)
@ConditionalOnExpression("${i2f.swl.api.enable:true}")
@RestController
@RequestMapping("swl")
public class SwlSpringController {

    @Autowired
    private SwlTransfer swlTransfer;

    /**
     * 客户端发起公钥交换请求
     * 明文传递公钥，并且使用私钥进行签名
     * 密文是一个随机字符串，仅用于进行签名使用
     *
     * @return
     */
    public SwlData client() {
        AsymKeyPair swapKeyPair = swlTransfer.getSelfSwapKey();

        AsymKeyPair customKeyPair = swlTransfer.generateKeyPair();

        String payload = UUID.randomUUID().toString().replace("-", "");
        SwlData req = swlTransfer.sendByRaw("swap",
                swapKeyPair.getPublicKey(),
                customKeyPair.getPrivateKey(),
                new ArrayList<>(Collections.singletonList(payload)),
                new ArrayList<>(Collections.singletonList(customKeyPair.getPublicKey()))
        );
        req.setContext(null);
        return req;
    }


    @SecureParams(in = false, out = false)
    @PostMapping("swapKey")
    public SwlData swapKey(@RequestBody SwlData req) throws Exception {
        String clientPublicKey = req.getAttaches().get(0);

        AsymKeyPair swapKeyPair = swlTransfer.getSelfSwapKey();

        swlTransfer.receiveByRaw("swap", req,
                clientPublicKey,
                swapKeyPair.getPrivateKey());

        String certId = swlTransfer.acceptOtherSwapKey(clientPublicKey);
        String selfPublicKey = swlTransfer.getSelfPublicKey(certId);

        SwlData resp = swlTransfer.sendByRaw(certId,
                clientPublicKey,
                swapKeyPair.getPrivateKey(),
                new ArrayList<>(Collections.singletonList(selfPublicKey))
        );
        resp.setContext(null);

        return resp;
    }
}
