package i2f.codec.str.obfuscate;

import i2f.codec.str.IStringStringCodec;

/**
 * @author Ice2Faith
 * @date 2024/7/9 10:27
 * @desc
 */
public class Base64ObfuscatorCodec implements IStringStringCodec {
    public static final Base64ObfuscatorCodec INSTANCE = new Base64ObfuscatorCodec();

    @Override
    public String encode(String data) {
        return Base64Obfuscator.encode(data, true);
    }

    @Override
    public String decode(String enc) {
        return Base64Obfuscator.decode(enc);
    }
}
