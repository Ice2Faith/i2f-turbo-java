package i2f.sm.crypto.std;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.crypto.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.crypto.std.encrypt.symmetric.key.BytesKey;
import i2f.sm.crypto.sm4.Sm4;

import java.security.Key;

/**
 * @author Ice2Faith
 * @date 2025/8/14 17:38
 */
public class SmCryptoSm4Encryptor implements ISymmetricEncryptor {
    private String key;

    public SmCryptoSm4Encryptor() {
    }

    public SmCryptoSm4Encryptor(String key) {
        this.key = key;
    }

    public SmCryptoSm4Encryptor(Key key) {
        this.key = HexStringByteCodec.INSTANCE.encode(key.getEncoded());
    }

    public static String genKey() {
        return Sm4.generateHexKey();
    }

    public static Key keyOf(byte[] bytes) {
        return new BytesKey("sm4", "hex", bytes);
    }

    public static Key keyOf(String key) {
        return keyOf(HexStringByteCodec.INSTANCE.decode(key));
    }

    @Override
    public void setKey(Key key) {
        this.key = HexStringByteCodec.INSTANCE.encode(key.getEncoded());
    }

    @Override
    public Key getKey() {
        byte[] bytes = HexStringByteCodec.INSTANCE.decode(key);
        return keyOf(bytes);
    }

    @Override
    public void setKeyBytes(byte[] keyBytes) {
        this.key = HexStringByteCodec.INSTANCE.encode(keyBytes);
    }

    @Override
    public byte[] getKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(this.key);
    }

    @Override
    public void setKeyString(String str) {
        this.key = str;
    }

    @Override
    public String getKeyString() {
        return this.key;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str = CharsetStringByteCodec.UTF8.encode(data);
        String enc = Sm4.encrypt(str, this.key);
        return HexStringByteCodec.INSTANCE.decode(enc);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String enc = HexStringByteCodec.INSTANCE.encode(data);
        String str = Sm4.decrypt(enc, this.key);
        return CharsetStringByteCodec.UTF8.decode(str);
    }

    public String encrypt(String data) throws Exception {
        return Sm4.encrypt(data, key);
    }

    public String decrypt(String data) throws Exception {
        return Sm4.decrypt(data, key);
    }
}
