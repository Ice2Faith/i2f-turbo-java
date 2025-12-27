package i2f.extension.jce.sm.antherd.encrypt.symmetric;

import com.antherd.smcrypto.NashornProvider;
import com.antherd.smcrypto.sm4.Sm4;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.crypto.impl.jdk.encrypt.Encryptor;
import i2f.crypto.impl.jdk.supports.SecureRandomAlgorithm;
import i2f.crypto.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.crypto.std.encrypt.symmetric.key.BytesKey;
import i2f.extension.jce.sm.antherd.SmAntherdProvider;

import javax.script.ScriptException;
import java.security.Key;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:34
 * @desc
 */
public class Sm4Encryptor implements ISymmetricEncryptor {
    static {
        NashornProvider.printNonNashorn();
        SmAntherdProvider.printNonDependency();
    }

    private String keyHex;

    public Sm4Encryptor() {
    }

    public Sm4Encryptor(String key) {
        this.keyHex = key;
    }

    public static final int[] SECRET_BYTE_LEN = {128};

    public static int[] secretBytesLen() {
        return SECRET_BYTE_LEN;
    }

    public static String keyOf(byte[] codes) {
        return HexStringByteCodec.INSTANCE.encode(codes);
    }

    public static byte[] keyTo(String key) {
        return HexStringByteCodec.INSTANCE.decode(key);
    }

    public static String genKey() throws Exception {
        return genKey(null, SecureRandomAlgorithm.SHA1PRNG.text());
    }

    public static String genKey(byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        byte[] keyBytes = Encryptor.genKeyBytes(vectorBytes,
                Sm4Encryptor.secretBytesLen()[0],
                secureRandomAlgorithmName);
        return HexStringByteCodec.INSTANCE.encode(keyBytes);
    }

    @Override
    public void setKey(Key key) {
        this.keyHex = HexStringByteCodec.INSTANCE.encode(key.getEncoded());
    }

    @Override
    public Key getKey() {
        return new BytesKey("sm4", "hex", HexStringByteCodec.INSTANCE.decode(this.keyHex));
    }

    @Override
    public void setKeyBytes(byte[] keyBytes) {
        this.keyHex = HexStringByteCodec.INSTANCE.encode(keyBytes);
    }

    @Override
    public byte[] getKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(this.keyHex);
    }

    @Override
    public void setKeyString(String str) {
        this.keyHex = str;
    }

    @Override
    public String getKeyString() {
        return this.keyHex;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str = new String(data, "UTF-8");
        return encrypt(str).getBytes("UTF-8");
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String str = new String(data, "UTF-8");
        return decrypt(str).getBytes("UTF-8");
    }


    public String encrypt(String data) {
        try {
            return Sm4.encrypt(data, keyHex);
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String decrypt(String data) {
        try {
            return Sm4.decrypt(data, keyHex);
        } catch (ScriptException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public byte[] keyTo() {
        return HexStringByteCodec.INSTANCE.decode(keyHex);
    }

    public void ofKey(byte[] codes) {
        this.keyHex = HexStringByteCodec.INSTANCE.encode(codes);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sm4Encryptor that = (Sm4Encryptor) o;
        return Objects.equals(keyHex, that.keyHex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyHex);
    }

    @Override
    public String toString() {
        return "Sm4Encryptor{" +
                "key='" + keyHex + '\'' +
                '}';
    }
}
