package i2f.extension.jce.bc.encrypt.asymmetric;

import i2f.crypto.impl.jdk.encrypt.Encryptor;
import i2f.crypto.impl.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.crypto.impl.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.extension.jce.bc.BcProvider;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:51
 * @desc
 */
public class BcAsymmetricEncryptor extends AsymmetricEncryptor {
    static {
        BcProvider.registryProvider();
    }

    public BcAsymmetricEncryptor() {
    }

    public BcAsymmetricEncryptor(String algorithmName) {
        super(algorithmName, BcProvider.PROVIDER_NAME);
    }

    public BcAsymmetricEncryptor(String algorithmName, boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithmName, BcProvider.PROVIDER_NAME, noPadding, privateEncrypt, privateKey, publicKey);
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithm, privateKey, publicKey);
        this.providerName = BcProvider.PROVIDER_NAME;
    }


    public BcAsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair) {
        super(algorithm, keyPair);
        this.providerName = BcProvider.PROVIDER_NAME;
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm) {
        super(algorithm);
        this.providerName = BcProvider.PROVIDER_NAME;
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

    @Override
    public String toString() {
        return "BcAsymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", privateEncrypt=" + privateEncrypt +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
