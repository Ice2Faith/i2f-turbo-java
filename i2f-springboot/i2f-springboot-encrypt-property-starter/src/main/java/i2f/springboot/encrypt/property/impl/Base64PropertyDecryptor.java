package i2f.springboot.encrypt.property.impl;

import i2f.codec.CodecUtil;
import i2f.springboot.encrypt.property.core.ITextEncryptor;
import i2f.springboot.encrypt.property.core.PrefixPropertyDecryptor;

import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2022/6/7 9:59
 * @desc
 */
public class Base64PropertyDecryptor extends PrefixPropertyDecryptor implements ITextEncryptor {
    public static final String BASE64_PREFIX="bs64.";
    public Base64PropertyDecryptor() {
        super(BASE64_PREFIX);
    }

    @Override
    public String decryptText(String text) {
        try{
            byte[] data=CodecUtil.ofBase64(text);
            return CodecUtil.ofUtf8(data);
        }catch(Exception e){
            return text;
        }
    }

    @Override
    public String encrypt(String text) {
        try{
            byte[] enc = CodecUtil.toUtf8(text);
            return BASE64_PREFIX+CodecUtil.toBase16(enc);
        }catch(Exception e){
            return BASE64_PREFIX+text;
        }
    }
}
