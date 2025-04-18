package i2f.crypto.impl.jdk.encrypt.asymmetric;

import i2f.array.ArrayUtil;
import i2f.crypto.impl.jdk.encrypt.Encryptor;
import i2f.crypto.std.encrypt.asymmetric.IAsymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:53
 * @desc
 */
public class AsymmetricEncryptor implements IAsymmetricEncryptor {

    public static final Function<KeyPair, AsymmetricEncryptor> RSA_DEFAULT = (keyPair) -> new AsymmetricEncryptor(RsaType.DEFAULT, keyPair);
    public static final Function<KeyPair, AsymmetricEncryptor> RSA_ECB_PKCS1PADDING = (keyPair) -> new AsymmetricEncryptor(RsaType.ECB_PKCS1PADDING, keyPair);
    public static final Function<KeyPair, AsymmetricEncryptor> RSA_ECB_PKCS5PADDING = (keyPair) -> new AsymmetricEncryptor(RsaType.ECB_PKCS5PADDING, keyPair);

    public static final Supplier<AsymmetricEncryptor> NEW_RSA_DEFAULT = () -> {
        try {
            return AsymmetricEncryptor.genKeyEncryptor(RsaType.DEFAULT);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };
    public static final Supplier<AsymmetricEncryptor> NEW_RSA_ECB_PKCS1PADDING = () -> {
        try {
            return AsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_PKCS1PADDING);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };
    public static final Supplier<AsymmetricEncryptor> NEW_RSA_ECB_PKCS5PADDING = () -> {
        try {
            return AsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_PKCS5PADDING);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    protected String algorithmName;
    protected String providerName;
    protected boolean noPadding;
    protected boolean privateEncrypt;
    protected PrivateKey privateKey;
    protected PublicKey publicKey;

    public AsymmetricEncryptor() {
    }

    public AsymmetricEncryptor(String algorithmName, String providerName) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
    }

    public AsymmetricEncryptor(String algorithmName, String providerName, boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.noPadding = noPadding;
        this.privateEncrypt = privateEncrypt;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }


    public AsymmetricEncryptor(AsymmetricType algorithm) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
    }

    public AsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }


    public AsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        if (keyPair != null) {
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        }
    }

    public static KeyPair keyPairOf(AsymmetricType algorithm, byte[] publicKey, byte[] privateKey) throws Exception {
        return Encryptor.keyPairOf(algorithm, publicKey, privateKey);
    }

    public static PublicKey publicKeyOf(AsymmetricType algorithm, byte[] codes) throws Exception {
        return Encryptor.publicKeyOf(algorithm, codes);
    }

    public static PrivateKey privateKeyOf(AsymmetricType algorithm, byte[] codes) throws Exception {
        return Encryptor.privateKeyOf(algorithm, codes);
    }

    public static byte[] keyTo(Key key) {
        return Encryptor.keyTo(key);
    }

    public static byte[] keyTo(PublicKey key) {
        return Encryptor.keyTo(key);
    }

    public static byte[] keyTo(PrivateKey key) {
        return Encryptor.keyTo(key);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm) throws Exception {
        return Encryptor.genKeyPair(algorithm);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] keyBytes) throws Exception {
        return Encryptor.genKeyPair(algorithm, keyBytes);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] keyBytes, String secureRandomAlgorithmName) throws Exception {
        return Encryptor.genKeyPair(algorithm, keyBytes, secureRandomAlgorithmName);
    }

    public static AsymmetricEncryptor genKeyEncryptor(AsymmetricType algorithm) throws Exception {
        return genKeyEncryptor(algorithm, null, null);
    }

    public static AsymmetricEncryptor genKeyEncryptor(AsymmetricType algorithm, byte[] keyBytes) throws Exception {
        return genKeyEncryptor(algorithm, keyBytes, null);
    }

    public static AsymmetricEncryptor genKeyEncryptor(AsymmetricType algorithm, byte[] keyBytes, String secureRandomAlgorithmName) throws Exception {
        return new AsymmetricEncryptor(algorithm,
                genKeyPair(algorithm,
                        keyBytes,
                        secureRandomAlgorithmName)
        );

    }

    public Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception {
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = Encryptor.cipherOf(algorithmName, providerName);
        if (encryptMode) {
            if (isPrivate) {
                cipher.init(mode, privateKey);
            } else {
                cipher.init(mode, publicKey);
            }
        } else {
            if (isPrivate) {
                cipher.init(mode, privateKey);
            } else {
                cipher.init(mode, publicKey);
            }
        }
        return cipher;
    }

    @Override
    public void setPublicKeyBytes(byte[] publicKeyBytes) {
        try {
            this.publicKey = Encryptor.publicKeyOf(this.algorithmName, publicKeyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getPublicKeyBytes() {
        return publicKey.getEncoded();
    }


    @Override
    public void setPrivateKeyBytes(byte[] privateKeyBytes) {
        try {
            this.privateKey = Encryptor.privateKeyOf(algorithmName, privateKeyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getPrivateKeyBytes() {
        return this.privateKey.getEncoded();
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    @Override
    public KeyPair getKeyPair() {
        return new KeyPair(this.publicKey, this.privateKey);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true, false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false, true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] privateEncrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true, true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] publicDecrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false, false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        return privateEncrypt(data);
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        byte[] bytes = publicDecrypt(sign);
        return ArrayUtil.equal(bytes, data);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public boolean isNoPadding() {
        return noPadding;
    }

    public void setNoPadding(boolean noPadding) {
        this.noPadding = noPadding;
    }

    public boolean isPrivateEncrypt() {
        return privateEncrypt;
    }

    public void setPrivateEncrypt(boolean privateEncrypt) {
        this.privateEncrypt = privateEncrypt;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] publicKeyTo() {
        return keyTo(publicKey);
    }

    public byte[] privateKeyTo() {
        return keyTo(privateKey);
    }

    public void publicKeyOf(byte[] codes) throws Exception {
        this.publicKey = Encryptor.publicKeyOf(algorithmName, providerName, codes);
    }

    public void privateKeyOf(byte[] codes) throws Exception {
        this.privateKey = Encryptor.privateKeyOf(algorithmName, providerName, codes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AsymmetricEncryptor that = (AsymmetricEncryptor) o;
        return noPadding == that.noPadding &&
                privateEncrypt == that.privateEncrypt &&
                Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(publicKey, that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, providerName, noPadding, privateEncrypt, privateKey, publicKey);
    }

    @Override
    public String toString() {
        return "AsymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", privateEncrypt=" + privateEncrypt +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
