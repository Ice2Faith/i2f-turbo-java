package i2f.springboot.secure.crypto;


import i2f.codec.CodecUtil;
import i2f.jce.std.digest.IMessageDigester;

/**
 * @author Ice2Faith
 * @date 2022/7/1 13:54
 * @desc
 */
public class SignatureUtil {
    public static String sign(String text) {
        try {
            IMessageDigester digester = SecureProvider.messageDigesterSupplier.get();
            return digester.digestAsHex(CodecUtil.toUtf8(CodecUtil.toBase64(CodecUtil.toUtf8(text))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
