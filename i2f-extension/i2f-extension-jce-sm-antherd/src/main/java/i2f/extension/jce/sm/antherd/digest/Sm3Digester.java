package i2f.extension.jce.sm.antherd.digest;

import com.antherd.smcrypto.NashornProvider;
import com.antherd.smcrypto.sm3.Sm3;
import i2f.crypto.std.digest.IMessageDigester;
import i2f.extension.jce.sm.antherd.SmAntherdProvider;

import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:36
 * @desc
 */
public class Sm3Digester implements IMessageDigester {
    static {
        NashornProvider.printNonNashorn();
        SmAntherdProvider.printNonDependency();
    }

    public static final Sm3Digester INSTANCE = new Sm3Digester();

    public Sm3Digester() {
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
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean verify(String sign, String str) {
        try {
            String hash = Sm3.sm3(str);
            return hash.equalsIgnoreCase(sign);
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
