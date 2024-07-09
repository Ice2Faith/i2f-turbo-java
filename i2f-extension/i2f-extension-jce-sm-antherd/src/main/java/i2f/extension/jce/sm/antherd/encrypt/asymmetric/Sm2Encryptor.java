package i2f.extension.jce.sm.antherd.encrypt.asymmetric;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import i2f.codec.CodecUtil;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.jce.std.encrypt.asymmetric.key.BytesPrivateKey;
import i2f.jce.std.encrypt.asymmetric.key.BytesPublicKey;
import lombok.Data;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:35
 * @desc
 */
@Data
public class Sm2Encryptor implements IAsymmetricEncryptor {
    public static final int MODE_C1C3C2 = 0;
    public static final int MODE_C1C2C3 = 1;

    protected int cipherMode = MODE_C1C2C3;
    protected String publicKey;
    protected String privateKey;

    public Sm2Encryptor() {
    }

    public Sm2Encryptor(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Sm2Encryptor(Keypair keypair) {
        this.publicKey = keypair.getPublicKey();
        this.privateKey = keypair.getPrivateKey();
    }

    public Sm2Encryptor(KeyPair keyPair) {
        if (keyPair.getPublic() != null) {
            this.publicKey = CodecUtil.toHexString(keyPair.getPublic().getEncoded());
        }
        if (keyPair.getPrivate() != null) {
            this.privateKey = CodecUtil.toHexString(keyPair.getPrivate().getEncoded());
        }
    }

    public static Keypair genKey() {
        return Sm2.generateKeyPairHex();
    }

    public static KeyPair genKeyPair() {
        Keypair keypair = Sm2.generateKeyPairHex();

        return new KeyPair(new BytesPublicKey("sm2", "hex", CodecUtil.ofHexString(keypair.getPublicKey())),
                new BytesPrivateKey("sm2", "hex", CodecUtil.ofHexString(keypair.getPrivateKey())));
    }

    public static KeyPair keyPairOf(byte[] publicKey, byte[] privateKey) throws Exception {
        return new KeyPair(new BytesPublicKey("sm2", "hex", publicKey),
                new BytesPrivateKey("sm2", "hex", privateKey));
    }

    public static PublicKey publicKeyOf(byte[] publicKey) {
        return new BytesPublicKey("sm2", "hex", publicKey);
    }

    public static PrivateKey privateKeyOf(byte[] publicKey) {
        return new BytesPrivateKey("sm2", "hex", publicKey);
    }

    @Override
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = HexStringByteCodec.INSTANCE.encode(publicKey.getEncoded());
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = HexStringByteCodec.INSTANCE.encode(privateKey.getEncoded());
    }

    @Override
    public void setPublicKeyBytes(byte[] publicKeyBytes) {
        this.publicKey = HexStringByteCodec.INSTANCE.encode(publicKeyBytes);
    }

    @Override
    public byte[] getPublicKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(publicKey);
    }

    @Override
    public void setPrivateKeyBytes(byte[] privateKeyBytes) {
        this.privateKey = HexStringByteCodec.INSTANCE.encode(privateKeyBytes);
    }

    @Override
    public byte[] getPrivateKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(this.privateKey);
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        if (pub != null) {
            this.publicKey = HexStringByteCodec.INSTANCE.encode(pub.getEncoded());
        }
        PrivateKey pri = keyPair.getPrivate();
        if (pri != null) {
            this.privateKey = HexStringByteCodec.INSTANCE.encode(pri.getEncoded());
        }
    }

    @Override
    public KeyPair getKeyPair() {
        PublicKey pub = null;
        PrivateKey pri = null;
        if (this.publicKey != null && !this.publicKey.isEmpty()) {
            pub = new BytesPublicKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(this.publicKey));
        }
        if (this.privateKey != null && !this.privateKey.isEmpty()) {
            pri = new BytesPrivateKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(this.privateKey));
        }
        return new KeyPair(pub, pri);
    }

    @Override
    public void setPublicKeyString(String str) {
        this.publicKey = str;
    }

    @Override
    public String getPublicKeyString() {
        return this.publicKey;
    }

    @Override
    public void setPrivateKeyString(String str) {
        this.privateKey = str;
    }

    @Override
    public String getPrivateKeyString() {
        return this.privateKey;
    }

    @Override
    public void setAsymKeyPair(AsymKeyPair keyPair) {
        this.publicKey = keyPair.getPublicKey();
        this.privateKey = keyPair.getPrivateKey();
    }

    @Override
    public AsymKeyPair getAsymKeyPair() {
        return new AsymKeyPair(this.publicKey, this.privateKey);
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

    @Override
    public byte[] sign(byte[] data) throws Exception {
        String str = new String(data, "UTF-8");
        return sign(str).getBytes("UTF-8");
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        String signHex = new String(sign, "UTF-8");
        String str = new String(data, "UTF-8");
        return verify(signHex, str);
    }

    public String encrypt(String data) throws Exception {
        return Sm2.doEncrypt(data, publicKey);
    }

    public String decrypt(String data) throws Exception {
        return Sm2.doDecrypt(data, privateKey);
    }

    public String sign(String data) throws Exception {
        return Sm2.doSignature(data, privateKey);
    }

    public boolean verify(String sign, String data) throws Exception {
        return Sm2.doVerifySignature(data, sign, publicKey);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] publicKeyTo() {
        return CodecUtil.ofHexString(publicKey);
    }

    public byte[] privateKeyTo() {
        return CodecUtil.ofHexString(privateKey);
    }

    public void ofPublicKey(byte[] codes) {
        this.publicKey = CodecUtil.toHexString(codes);
    }

    public void ofPrivateKey(byte[] codes) {
        this.privateKey = CodecUtil.toHexString(codes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sm2Encryptor that = (Sm2Encryptor) o;
        return Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "Sm2Encryptor{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
