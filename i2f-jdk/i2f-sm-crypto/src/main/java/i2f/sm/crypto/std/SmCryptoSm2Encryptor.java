package i2f.sm.crypto.std;

import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.crypto.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.crypto.std.encrypt.asymmetric.key.BytesPrivateKey;
import i2f.crypto.std.encrypt.asymmetric.key.BytesPublicKey;
import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm2.Sm2Cipher;
import lombok.Data;

import javax.script.ScriptException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2025/8/14 15:13
 */
@Data
public class SmCryptoSm2Encryptor implements IAsymmetricEncryptor {
    protected Sm2Cipher.CipherMode cipherMode= Sm2Cipher.CipherMode.C1C2C3;
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
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    public static KeyPair genKeyPair() {
        try {
            i2f.sm.crypto.sm2.KeyPair keypair = Sm2.generateKeyPairHex();

            return new KeyPair(new BytesPublicKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(keypair.getPublicKey())),
                    new BytesPrivateKey("sm2", "hex", HexStringByteCodec.INSTANCE.decode(keypair.getPrivateKey())));
        } catch (SmException e) {
            throw new IllegalStateException(e.getMessage(),e);
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

    }

    @Override
    public PublicKey getPublicKey() {
        return null;
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {

    }

    @Override
    public PrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public void setPublicKeyBytes(byte[] publicKeyBytes) {

    }

    @Override
    public byte[] getPublicKeyBytes() {
        return new byte[0];
    }

    @Override
    public void setPrivateKeyBytes(byte[] privateKeyBytes) {

    }

    @Override
    public byte[] getPrivateKeyBytes() {
        return new byte[0];
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {

    }

    @Override
    public KeyPair getKeyPair() {
        return null;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return new byte[0];
    }
}
