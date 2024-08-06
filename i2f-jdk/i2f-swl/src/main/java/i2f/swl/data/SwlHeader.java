package i2f.swl.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:41
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlHeader {
    /**
     * 时间戳
     */
    public String timestamp;
    /**
     * 一次性消息
     */
    public String nonce;
    /**
     * 随机秘钥
     */
    public String randomKey;
    /**
     * 签名
     */
    public String sign;
    /**
     * 数字签名
     */
    public String digital;
    /**
     * 服务端非对称签名
     */
    public String remoteAsymSign;

    /**
     * 客户端非对称签名
     */
    public String localAsymSign;

    public static SwlHeader copy(SwlHeader src,SwlHeader dst) {
        dst.setTimestamp(src.getTimestamp());
        dst.setNonce(src.getNonce());
        dst.setRandomKey(src.getRandomKey());
        dst.setSign(src.getSign());
        dst.setDigital(src.getDigital());
        dst.setLocalAsymSign(src.getLocalAsymSign());
        dst.setRemoteAsymSign(src.getRemoteAsymSign());
        return dst;
    }
}
