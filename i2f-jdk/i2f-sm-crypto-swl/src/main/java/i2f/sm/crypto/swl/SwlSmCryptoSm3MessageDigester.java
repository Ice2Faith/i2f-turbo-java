package i2f.sm.crypto.swl;

import i2f.sm.crypto.std.SmCryptoSm3Digester;
import i2f.swl.std.ISwlMessageDigester;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:02
 * @desc
 */
public class SwlSmCryptoSm3MessageDigester implements ISwlMessageDigester {
    private SmCryptoSm3Digester digester = SmCryptoSm3Digester.INSTANCE;

    @Override
    public String digest(String data) {
        return digester.digest(data);
    }

    @Override
    public boolean verify(String digest, String data) {
        return digester.verify(digest, data);
    }
}
