package i2f.springboot.swl.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/6/13 10:11
 * @desc
 */
@Data
@NoArgsConstructor
public class SecureHeader {
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
}
