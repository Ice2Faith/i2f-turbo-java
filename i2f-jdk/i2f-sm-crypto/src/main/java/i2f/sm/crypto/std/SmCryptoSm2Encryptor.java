package i2f.sm.crypto.std;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.crypto.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.crypto.std.encrypt.asymmetric.key.BytesPrivateKey;
import i2f.crypto.std.encrypt.asymmetric.key.BytesPublicKey;
import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm2.Sm2Cipher;
import lombok.Data;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2025/8/14 15:13
 */
@Data
public class SmCryptoSm2Encryptor implements IAsymmetricEncryptor {
    protected Sm2Cipher.CipherMode cipherMode = Sm2Cipher.CipherMode.C1C2C3;
    protected String pubKey;
    protected String priKey;

    public SmCryptoSm2Encryptor() {
    }

    public SmCryptoSm2Encryptor(String publicKey, String privateKey) {
        this.pubKey = publicKey;
        this.priKey = privateKey;
    }

    public SmCryptoSm2Encryptor(i2f.sm.crypto.sm2.KeyPair keypair) {
        this.pubKey = keypair.getPublicKey();
        this.priKey = keypair.getPrivateKey();
    }

    public SmCryptoSm2Encryptor(KeyPair keyPair) {
        if (keyPair.getPublic() != null) {
            this.pubKey = HexStringByteCodec.INSTANCE.encode(keyPair.getPublic().getEncoded());
        }
        if (keyPair.getPrivate() != null) {
            this.priKey = HexStringByteCodec.INSTANCE.encode(keyPair.getPrivate().getEncoded());
        }
    }

    public static i2f.sm.crypto.sm2.KeyPair genKey() {
        try {
            return Sm2.generateKeyPairHex();
        } catch (SmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static KeyPair genKeyPair() {
        try {
            i2f.sm.crypto.sm2.KeyPair keypair = Sm2.generateKeyPairHex();

            return new KeyPair(new BytesPublicKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(keypair.getPublicKey())),
                    new BytesPrivateKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(keypair.getPrivateKey())));
        } catch (SmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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
        this.pubKey = HexStringByteCodec.INSTANCE.encode(publicKey.getEncoded());
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKeyOf(HexStringByteCodec.INSTANCE.decode(this.pubKey));
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {
        this.priKey = HexStringByteCodec.INSTANCE.encode(privateKey.getEncoded());
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKeyOf(HexStringByteCodec.INSTANCE.decode(this.priKey));
    }

    @Override
    public void setPublicKeyBytes(byte[] publicKeyBytes) {
        this.pubKey = HexStringByteCodec.INSTANCE.encode(publicKeyBytes);
    }

    @Override
    public byte[] getPublicKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(pubKey);
    }

    @Override
    public void setPrivateKeyBytes(byte[] privateKeyBytes) {
        this.priKey = HexStringByteCodec.INSTANCE.encode(privateKeyBytes);
    }

    @Override
    public byte[] getPrivateKeyBytes() {
        return HexStringByteCodec.INSTANCE.decode(this.priKey);
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        if (pub != null) {
            this.pubKey = HexStringByteCodec.INSTANCE.encode(pub.getEncoded());
        }
        PrivateKey pri = keyPair.getPrivate();
        if (pri != null) {
            this.priKey = HexStringByteCodec.INSTANCE.encode(pri.getEncoded());
        }
    }

    @Override
    public KeyPair getKeyPair() {
        PublicKey pub = null;
        PrivateKey pri = null;
        if (this.pubKey != null && !this.pubKey.isEmpty()) {
            pub = publicKeyOf(HexStringByteCodec.INSTANCE.decode(this.pubKey));
        }
        if (this.priKey != null && !this.priKey.isEmpty()) {
            pri = privateKeyOf(HexStringByteCodec.INSTANCE.decode(this.priKey));
        }
        return new KeyPair(pub, pri);
    }

    @Override
    public void setPublicKeyString(String str) {
        this.pubKey = str;
    }

    @Override
    public String getPublicKeyString() {
        return this.pubKey;
    }

    @Override
    public void setPrivateKeyString(String str) {
        this.priKey = str;
    }

    @Override
    public String getPrivateKeyString() {
        return this.priKey;
    }

    @Override
    public void setAsymKeyPair(AsymKeyPair keyPair) {
        this.pubKey = keyPair.getPublicKey();
        this.priKey = keyPair.getPrivateKey();
    }

    @Override
    public AsymKeyPair getAsymKeyPair() {
        return new AsymKeyPair(this.pubKey, this.priKey);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str = CharsetStringByteCodec.UTF8.encode(data);
        String hex = encrypt(str);
        return HexStringByteCodec.INSTANCE.decode(hex);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String hex = HexStringByteCodec.INSTANCE.encode(data);
        String enc = decrypt(hex);
        return CharsetStringByteCodec.UTF8.decode(enc);
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        String str = CharsetStringByteCodec.UTF8.encode(data);
        String sign = sign(str);
        return HexStringByteCodec.INSTANCE.decode(sign);
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        String signHex = HexStringByteCodec.INSTANCE.encode(data);
        String str = CharsetStringByteCodec.UTF8.encode(data);
        return verify(signHex, str);
    }

    public String encrypt(String data) throws Exception {
        return Sm2.doEncrypt(data, pubKey);
    }

    public String decrypt(String data) throws Exception {
        return Sm2.doDecrypt(data, priKey);
    }

    public String sign(String data) throws Exception {
        return Sm2.doSignature(data, priKey);
    }

    public boolean verify(String sign, String data) throws Exception {
        return Sm2.doVerifySignature(data, sign, pubKey);
    }
}
