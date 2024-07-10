package i2f.springboot.swl.data;

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
     * 签名
     */
    public String sign;
    /**
     * 一次性消息
     */
    public String nonce;
    /**
     * 数字签名
     */
    public String digital;
    /**
     * 随机秘钥
     */
    public String randomKey;
    /**
     * 服务端非对称签名
     */
    public String serverAsymSign;

    /**
     * 客户端非对称签名
     */
    public String clientAsymSign;

    public static SwlHeader copy(SwlHeader header) {
        SwlHeader ret = new SwlHeader();
        ret.setSign(header.getSign());
        ret.setNonce(header.getNonce());
        ret.setDigital(header.getDigital());
        ret.setRandomKey(header.getRandomKey());
        ret.setClientAsymSign(header.getClientAsymSign());
        ret.setServerAsymSign(header.getServerAsymSign());

        return ret;
    }
}
