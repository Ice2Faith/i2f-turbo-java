package i2f.sm.crypto.std;

import i2f.crypto.std.digest.IMessageDigester;
import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm3.Sm3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2025/8/14 17:47
 */
public class SmCryptoSm3Digester implements IMessageDigester {
    public static final SmCryptoSm3Digester INSTANCE = new SmCryptoSm3Digester();

    public SmCryptoSm3Digester() {
    }

    @Override
    public byte[] digest(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            bos.write(buff, 0, len);
        }
        is.close();
        String str = new String(bos.toByteArray(), "UTF-8");
        String digest = digest(str);
        return digest.getBytes("UTF-8");
    }

    @Override
    public byte[] digest(byte[] data) throws Exception {
        String str = new String(data, "UTF-8");
        String digest = digest(str);
        return digest.getBytes("UTF-8");
    }

    public String digest(String str) {
        try {
            return Sm3.sm3(str);
        } catch (SmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean verify(String sign, String str) {
        try {
            String hash = Sm3.sm3(str);
            return hash.equalsIgnoreCase(sign);
        } catch (SmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
