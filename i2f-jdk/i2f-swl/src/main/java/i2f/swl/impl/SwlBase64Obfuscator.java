package i2f.swl.impl;

import i2f.codec.str.obfuscate.Base64Obfuscator;
import i2f.swl.std.ISwlObfuscator;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:35
 * @desc
 */
public class SwlBase64Obfuscator implements ISwlObfuscator {
    @Override
    public String encode(String data) {
        return Base64Obfuscator.encode(data, true);
    }

    @Override
    public String decode(String data) {
        return Base64Obfuscator.decode(data);
    }
}
