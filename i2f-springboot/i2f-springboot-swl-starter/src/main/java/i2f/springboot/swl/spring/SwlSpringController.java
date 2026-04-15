package i2f.springboot.swl.spring;


import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.swl.annotation.SwlCtrl;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlDto;
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

    @Autowired
    private IJsonSerializer jsonSerializer;

    @ConditionalOnExpression("${i2f.swl.web.enable:true}")
    @SwlCtrl(in = false, out = false)
    @PostMapping("swapKey")
    public SwlDto swapKey(@RequestBody SwlDto dto) throws Exception {
        String reqPayload = dto.getPayload();
        String reqJson = new String(Base64StringByteCodec.INSTANCE.decode(reqPayload),"UTF-8");
        SwlData reqHandleShake = (SwlData)jsonSerializer.deserialize(reqJson, SwlData.class);

        AsymKeyPair swapKeyPair = swlTransfer.getSelfSwapKey();

        // ************************服务端接收握手并响应*******************************
        String obfuscateClientPublicKey = reqHandleShake.getAttaches().get(0);
        String clientPublicKey = swlTransfer.obfuscateDecode(obfuscateClientPublicKey);

        SwlData recvReqHandleShake = swlTransfer.receiveByRaw("swap", reqHandleShake,
                clientPublicKey,
                swapKeyPair.getPrivateKey());

        String serverCertId = swlTransfer.acceptOtherSwapKey(obfuscateClientPublicKey);
        String selfPublicKey = swlTransfer.getSelfPublicKey(serverCertId);

        SwlData respHandleShake = swlTransfer.sendByRaw(serverCertId,
                clientPublicKey,
                swapKeyPair.getPrivateKey(),
                new ArrayList<>(Collections.singletonList(selfPublicKey))
        );
        respHandleShake.setContext(null);

        SwlDto ret=new SwlDto();
        String retJson=jsonSerializer.serialize(respHandleShake);
        String retPayload=Base64StringByteCodec.INSTANCE.encode(retJson.getBytes("UTF-8"));
        ret.setPayload(retPayload);
        return ret;
    }
}
