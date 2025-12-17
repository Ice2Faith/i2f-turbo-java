package i2f.springboot.ops.common;


import com.antherd.smcrypto.sm2.Sm2;
import com.antherd.smcrypto.sm3.Sm3;
import com.antherd.smcrypto.sm4.Sm4;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:15
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class OpsSecureTransfer {
    public static final SecureRandom RANDOM = new SecureRandom();
    @Autowired
    private OpsSecureKeyPair opsSecureKeyPair;
    @Autowired
    private ObjectMapper objectMapper;

    public OpsSecureReturn<OpsSecureDto> success(Object obj) throws Exception {
        return OpsSecureReturn.success(send(obj));
    }

    public OpsSecureReturn<OpsSecureDto> error(String msg) throws Exception {
        return OpsSecureReturn.error(msg);
    }

    public OpsSecureReturn<OpsSecureDto> error(String msg, Throwable e) throws Exception {
        return OpsSecureReturn.error(msg, e);
    }

    public OpsSecureReturn<OpsSecureDto> error(Throwable e) throws Exception {
        return OpsSecureReturn.error(e);
    }

    public OpsSecureDto send(Object obj) throws Exception {
        OpsSecureDto ret = new OpsSecureDto();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        ret.setTimestamp(timestamp);
        String nonce = String.valueOf(RANDOM.nextInt());
        ret.setNonce(nonce);
        String json = objectMapper.writeValueAsString(obj);
        String randomKey = i2f.sm.crypto.sm4.Sm4.generateHexKey();
        String key = Sm2.doEncrypt(randomKey, opsSecureKeyPair.getPublicKey());
        ret.setKey(key);
        String payload = Sm4.encrypt(json, randomKey);
        ret.setPayload(payload);
        String sign = Sm3.sm3(timestamp + nonce + key + payload);
        ret.setSign(sign);
        String digital = Sm2.doSignature(sign, opsSecureKeyPair.getPrivateKey());
        ret.setDigital(digital);
        return ret;
    }

    public String recvJson(OpsSecureDto dto) throws Exception {
        if (dto == null) {
            throw new OpsException("missing content!");
        }
        String timestamp = dto.getTimestamp();
        if (StringUtils.isEmpty(timestamp)) {
            throw new OpsException("missing timestamp!");
        }
        long ts = Long.parseLong(timestamp);
        long cts = System.currentTimeMillis() / 1000;
        if (Math.abs(cts - ts) > 12 * 60 * 60) {
            throw new OpsException("exceed timestamp!");
        }
        String nonce = dto.getNonce();
        if (StringUtils.isEmpty(nonce)) {
            throw new OpsException("missing nonce!");
        }
        String payload = dto.getPayload();
        if (StringUtils.isEmpty(payload)) {
            throw new OpsException("missing payload!");
        }
        String key = dto.getKey();
        if (StringUtils.isEmpty(key)) {
            throw new OpsException("missing key!");
        }
        String sign = dto.getSign();
        if (StringUtils.isEmpty(sign)) {
            throw new OpsException("missing sign!");
        }
        String calcSign = Sm3.sm3(timestamp + nonce + key + payload);
        if (!calcSign.equalsIgnoreCase(sign)) {
            throw new OpsException("invalid sign!");
        }
        String digital = dto.getDigital();
        if (StringUtils.isEmpty(digital)) {
            throw new OpsException("missing sign!");
        }
        boolean ok = Sm2.doVerifySignature(sign, digital, opsSecureKeyPair.getPublicKey());
        if (!ok) {
            throw new OpsException("verify digital failed!");
        }
        String randomKey = Sm2.doDecrypt(key, opsSecureKeyPair.getPrivateKey());
        if (StringUtils.isEmpty(randomKey)) {
            throw new OpsException("invalid key!");
        }
        String json = Sm4.decrypt(payload, randomKey);
        if (StringUtils.isEmpty(json)) {
            throw new OpsException("invalid payload!");
        }
        return json;
    }

    public <T> T recv(OpsSecureDto dto, Class<T> type) throws Exception {
        String json = recvJson(dto);
        return objectMapper.readValue(json, type);
    }

    public <T> T recv(OpsSecureDto dto, TypeReference<T> type) throws Exception {
        String json = recvJson(dto);
        return objectMapper.readValue(json, type);
    }
}
