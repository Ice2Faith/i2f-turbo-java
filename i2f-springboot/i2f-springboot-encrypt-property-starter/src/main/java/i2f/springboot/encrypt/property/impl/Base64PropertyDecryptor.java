package i2f.springboot.encrypt.property.impl;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.springboot.encrypt.property.core.ITextEncryptor;
import i2f.springboot.encrypt.property.core.PrefixPropertyDecryptor;

/**
 * @author Ice2Faith
 * @date 2022/6/7 9:59
 * @desc
 */
public class Base64PropertyDecryptor extends PrefixPropertyDecryptor implements ITextEncryptor {
    public static final String BASE64_PREFIX = "bs64.";

    public Base64PropertyDecryptor() {
        super(BASE64_PREFIX);
    }

    @Override
    public String decryptText(String text) {
        try {
            byte[] data = Base64StringByteCodec.INSTANCE.decode(text);
            return CharsetStringByteCodec.UTF8.encode(data);
        } catch (Exception e) {
            return text;
        }
    }

    @Override
    public String encrypt(String text) {
        try {
            byte[] enc = CharsetStringByteCodec.UTF8.decode(text);
            return BASE64_PREFIX + Base64StringByteCodec.INSTANCE.encode(enc);
        } catch (Exception e) {
            return BASE64_PREFIX + text;
        }
    }
}
