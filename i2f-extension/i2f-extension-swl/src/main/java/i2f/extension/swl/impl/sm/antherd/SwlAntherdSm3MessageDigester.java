package i2f.extension.swl.impl.sm.antherd;

import i2f.extension.jce.sm.antherd.digest.Sm3Digester;
import i2f.swl.std.ISwlMessageDigester;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:02
 * @desc
 */
public class SwlAntherdSm3MessageDigester implements ISwlMessageDigester {
    private Sm3Digester digester = Sm3Digester.INSTANCE;

    @Override
    public String digest(String data) {
        return digester.digest(data);
    }

    @Override
    public boolean verify(String digest, String data) {
        return digester.verify(digest, data);
    }
}
