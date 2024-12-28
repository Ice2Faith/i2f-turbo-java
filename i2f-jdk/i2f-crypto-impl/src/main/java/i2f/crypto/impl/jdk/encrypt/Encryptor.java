package i2f.crypto.impl.jdk.encrypt;

import i2f.crypto.impl.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.crypto.impl.jdk.encrypt.symmetric.SymmetricType;
import i2f.crypto.impl.jdk.supports.CipherAlgorithm;
import i2f.crypto.impl.jdk.supports.CipherMode;
import i2f.crypto.impl.jdk.supports.CipherPadding;
import i2f.crypto.impl.jdk.supports.SecureRandomAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:55
 * @desc
 */
public class Encryptor {
    public static byte[] work(byte[] data, Cipher cipher) throws Exception {
        return cipher.doFinal(data);
    }

    public static byte[] work(byte[] data, int batchSize, Cipher cipher) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int i = 0;
        while ((i + batchSize) < data.length) {
            byte[] edata = cipher.update(data, i, batchSize);
            bos.write(edata);
            i += batchSize;
        }
        if (i < data.length) {
            byte[] edata = cipher.doFinal(data, i, data.length - i);
            bos.write(edata);
        }
        byte[] enc = bos.toByteArray();
        return enc;
    }

    public static void work(InputStream is, OutputStream os, int batchSize, Cipher cipher) throws Exception {
        byte[] buf = new byte[batchSize];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            byte[] edata = cipher.update(buf, 0, len);
            os.write(edata);
        }
        byte[] edata = cipher.doFinal();
        os.write(edata);
        os.flush();
    }


    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, String content) throws Exception {
        return handleNoPaddingEncryptFormat(cipher, content.getBytes("UTF-8"));
    }

    /**
     * <p>NoPadding加密模式, 加密内容必须是 8byte的倍数, 不足8位则末位补足0</p>
     * <p>加密算法不提供该补码方式, 需要代码完成该补码方式</p>
     *
     * @param cipher
     * @param data   ：加密内容
     * @return 符合加密的内容(byte[])
     */
    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, byte[] data) throws Exception {
        int blockSize = cipher.getBlockSize();
        int plaintextLength = data.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - plaintextLength % blockSize);
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(data, 0, plaintext, 0, data.length);
        return plaintext;
    }

    public static String algorithmNameOf(CipherAlgorithm algorithm, CipherMode mode, CipherPadding padding) {
        return algorithmNameOf(algorithm.text(), mode == null ? null : mode.text(), padding == null ? null : padding.text());
    }

    public static String algorithmNameOf(String algorithm, String mode, String padding) {
        String ret = algorithm;
        if (mode != null && !mode.isEmpty()) {
            ret += "/" + mode;
        }
        if (padding != null && !padding.isEmpty()) {
            ret += "/" + padding;
        }
        return ret;
    }

    public static String cipherAlgorithm(String algorithm) {
        if (algorithm == null) {
            return null;
        }
        int idx = algorithm.indexOf("/");
        if (idx < 0) {
            return algorithm;
        }
        return algorithm.substring(0, idx);
    }

    public static byte[] keyTo(Key key) {
        return key.getEncoded();
    }

    public static byte[] specTo(IvParameterSpec spec) {
        return spec.getIV();
    }

    public static SecretKey keyOf(byte[] keyBytes, String algorithmName) {
        algorithmName = cipherAlgorithm(algorithmName);
        return new SecretKeySpec(keyBytes, algorithmName);
    }

    public static SecretKey keyOf(byte[] keyBytes, SymmetricType algorithm) {
        return keyOf(keyBytes, algorithm.algorithmName());
    }

    public static IvParameterSpec specOf(byte[] vectorBytes) {
        return new IvParameterSpec(vectorBytes);
    }

    public static SecretKey genSecretKey(String algorithmName, byte[] secretBytes, int genSecretLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genSecretKey(algorithmName, secretBytes, genSecretLen, secureRandomAlgorithm.text());
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, byte[] secretBytes) throws Exception {
        return genSecretKey(algorithm.algorithmName(), secretBytes, algorithm.secretBytesLen()[0], (String) null);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        return genSecretKey(algorithm.algorithmName(), secretBytes, algorithm.secretBytesLen()[0], secureRandomAlgorithmName);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm) throws Exception {
        return genSecretKey(algorithm.algorithmName(), algorithm.provider(), null, algorithm.secretBytesLen()[0], (String) null);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, int genSecretLen) throws Exception {
        return genSecretKey(algorithm.algorithmName(), algorithm.provider(), null, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, byte[] secretBytes, int genSecretLen) throws Exception {
        return genSecretKey(algorithm.algorithmName(), algorithm.provider(), secretBytes, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, String providerName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genSecretKey(algorithm.algorithmName(), providerName, secretBytes, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(SymmetricType algorithm, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        return genSecretKey(algorithm.algorithmName(), providerName, secretBytes, genSecretLen, secureRandomAlgorithmName);
    }


    public static SecretKey genSecretKey(String algorithmName, String providerName, int genSecretLen) throws Exception {
        return genSecretKey(algorithmName, providerName, null, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genSecretKey(algorithmName, providerName, secretBytes, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(String algorithmName, int genSecretLen) throws Exception {
        return genSecretKey(algorithmName, null, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(String algorithmName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genSecretKey(algorithmName, secretBytes, genSecretLen, (String) null);
    }

    public static SecretKey genSecretKey(String algorithmName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        return genSecretKey(algorithmName, null, secretBytes, genSecretLen, secureRandomAlgorithmName);
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    public static SecretKey genSecretKey(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);

        KeyGenerator kgen = getKeyGenerator(algorithmName, providerName);
        SecureRandom random = getSecureRandom(secureRandomAlgorithmName);
        if (secretBytes != null) {
            random.setSeed(secretBytes);
        }
        kgen.init(genSecretLen, random);
        return kgen.generateKey();
    }

    public static SecretKey genPbeSecretKey(String algorithmName, byte[] secretBytes) throws Exception {
        return genPbeSecretKey(algorithmName, null, secretBytes);
    }

    public static SecretKey genPbeSecretKey(SymmetricType algorithm, byte[] secretBytes) throws Exception {
        return genPbeSecretKey(algorithm.algorithmName(), algorithm.provider(), secretBytes);
    }

    public static SecretKey genPbeSecretKey(SymmetricType algorithm, String providerName, byte[] secretBytes) throws Exception {
        return genPbeSecretKey(algorithm.algorithmName(), providerName, secretBytes);
    }

    public static SecretKey genPbeSecretKey(String algorithmName, String providerName, byte[] secretBytes) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        char[] secretChars = new char[secretBytes.length];
        for (int i = 0; i < secretBytes.length; i++) {
            secretChars[i] = (char) secretBytes[i];
        }
        PBEKeySpec keySpec = new PBEKeySpec(secretChars);
        SecretKeyFactory factory = getSecretKeyFactory(algorithmName, providerName);
        return factory.generateSecret(keySpec);
    }

    private static SecretKeyFactory getSecretKeyFactory(SymmetricType algorithm) throws Exception {
        return getSecretKeyFactory(algorithm.algorithmName(), algorithm.provider());
    }

    private static SecretKeyFactory getSecretKeyFactory(SymmetricType algorithm, String providerName) throws Exception {
        return getSecretKeyFactory(algorithm.algorithmName(), providerName);
    }

    private static SecretKeyFactory getSecretKeyFactory(String algorithmName) throws Exception {
        return getSecretKeyFactory(algorithmName, null);
    }

    private static SecretKeyFactory getSecretKeyFactory(String algorithmName, String providerName) throws Exception {
        providerName = checkProvider(providerName);
        SecretKeyFactory factory = null;
        if (providerName == null || providerName.isEmpty()) {
            factory = SecretKeyFactory.getInstance(algorithmName);
        } else {
            factory = SecretKeyFactory.getInstance(algorithmName, providerName);
        }
        return factory;
    }

    public static AlgorithmParameterSpec genParameterSpec(String algorithmName, byte[] vectorBytes, int genVectorLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genParameterSpec(algorithmName, vectorBytes, genVectorLen, secureRandomAlgorithm.text());
    }

    public static AlgorithmParameterSpec genParameterSpec(SymmetricType algorithm) throws Exception {
        return genParameterSpec(algorithm.algorithmName(), null, algorithm.vectorBytesLen()[0], (String) null);
    }

    public static AlgorithmParameterSpec genParameterSpec(SymmetricType algorithm, byte[] vectorBytes) throws Exception {
        return genParameterSpec(algorithm.algorithmName(), vectorBytes, algorithm.vectorBytesLen()[0], (String) null);
    }

    public static AlgorithmParameterSpec genParameterSpec(SymmetricType algorithm, byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        return genParameterSpec(algorithm.algorithmName(), vectorBytes, algorithm.vectorBytesLen()[0], secureRandomAlgorithmName);
    }

    public static byte[] genKeyBytes(int genVectorLen) throws Exception {
        return genKeyBytes(null, genVectorLen, null);
    }

    public static byte[] genKeyBytes(byte[] vectorBytes, int genVectorLen) throws Exception {
        return genKeyBytes(vectorBytes, genVectorLen, null);
    }

    public static byte[] genKeyBytes(byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName) throws Exception {
        SecureRandom random = getSecureRandom(secureRandomAlgorithmName);
        if (vectorBytes != null) {
            random.setSeed(vectorBytes);
        }
        int len = genVectorLen / 8;
        byte[] ret = new byte[len];
        random.nextBytes(ret);
        return ret;
    }

    public static KeyGenerator getKeyGenerator(SymmetricType algorithm) throws Exception {
        return getKeyGenerator(algorithm.algorithmName(), algorithm.provider());
    }

    public static KeyGenerator getKeyGenerator(SymmetricType algorithm, String providerName) throws Exception {
        return getKeyGenerator(algorithm.algorithmName(), providerName);
    }

    public static KeyGenerator getKeyGenerator(String algorithmName) throws Exception {
        return getKeyGenerator(algorithmName, null);
    }

    public static KeyGenerator getKeyGenerator(String algorithmName, String providerName) throws Exception {
        providerName = checkProvider(providerName);
        KeyGenerator keyGenerator = null;
        if (providerName != null) {
            keyGenerator = KeyGenerator.getInstance(algorithmName, providerName);
        } else {
            keyGenerator = KeyGenerator.getInstance(algorithmName);
        }
        return keyGenerator;
    }

    /**
     * 获取加密的向量
     */
    public static AlgorithmParameterSpec genParameterSpec(String algorithmName, byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName) throws Exception {
        KeyGenerator kgen = getKeyGenerator(algorithmName);
        SecureRandom random = getSecureRandom(secureRandomAlgorithmName);
        if (vectorBytes != null) {
            random.setSeed(vectorBytes);
        }
        kgen.init(genVectorLen, random);
        IvParameterSpec iv = new IvParameterSpec(kgen.generateKey().getEncoded());
        return iv;
    }

    public static AlgorithmParameterSpec genPbeParameterSpec(byte[] vectorBytes, int genVectorLen, String secureRandomAlgorithmName, int iterationCount) throws Exception {
        byte[] bytes = genKeyBytes(vectorBytes, genVectorLen, secureRandomAlgorithmName);
        AlgorithmParameterSpec spec = new PBEParameterSpec(bytes, iterationCount);
        return spec;
    }

    public static KeyPair genKeyPair(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen, SecureRandomAlgorithm secureRandomAlgorithm) throws Exception {
        return genKeyPair(algorithmName, providerName, secretBytes, genSecretLen, secureRandomAlgorithm.text());
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        return genKeyPair(algorithm.algorithmName(), algorithm.provider(), secretBytes, algorithm.secretBytesLen()[0], secureRandomAlgorithmName);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm) throws Exception {
        return genKeyPair(algorithm.algorithmName(), algorithm.provider(), null, algorithm.secretBytesLen()[0], (String) null);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] secretBytes) throws Exception {
        return genKeyPair(algorithm.algorithmName(), algorithm.provider(), secretBytes, algorithm.secretBytesLen()[0], (String) null);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, byte[] secretBytes, int genSecretLen) throws Exception {
        return genKeyPair(algorithm.algorithmName(), algorithm.provider(), secretBytes, genSecretLen, (String) null);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, String providerName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genKeyPair(algorithm.algorithmName(), providerName, secretBytes, genSecretLen, (String) null);
    }

    public static KeyPair genKeyPair(AsymmetricType algorithm, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        return genKeyPair(algorithm.algorithmName(), providerName, secretBytes, genSecretLen, secureRandomAlgorithmName);
    }

    public static KeyPair genKeyPair(String algorithmName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genKeyPair(algorithmName, null, secretBytes, genSecretLen, (String) null);
    }

    public static KeyPair genKeyPair(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen) throws Exception {
        return genKeyPair(algorithmName, providerName, secretBytes, genSecretLen, (String) null);
    }

    /**
     * 获取秘钥对
     */
    public static KeyPair genKeyPair(String algorithmName, String providerName, byte[] secretBytes, int genSecretLen, String secureRandomAlgorithmName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        providerName = checkProvider(providerName);
        KeyPairGenerator keyPairGenerator = getKeyPairGenerator(algorithmName, providerName);
        SecureRandom random = getSecureRandom(secureRandomAlgorithmName);
        if (secretBytes != null) {
            random.setSeed(secretBytes);
        }
        keyPairGenerator.initialize(genSecretLen, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        return getSecureRandom(null);
    }

    public static SecureRandom getSecureRandom(String secureRandomAlgorithmName) throws NoSuchAlgorithmException {
        if (secureRandomAlgorithmName == null || secureRandomAlgorithmName.isEmpty()) {
            secureRandomAlgorithmName = "SHA1PRNG";
        }
        SecureRandom random = SecureRandom.getInstance(secureRandomAlgorithmName);
        return random;
    }

    public static KeyPairGenerator getKeyPairGenerator(AsymmetricType algorithm) throws Exception {
        return getKeyPairGenerator(algorithm.algorithmName(), algorithm.provider());
    }

    public static KeyPairGenerator getKeyPairGenerator(AsymmetricType algorithm, String providerName) throws Exception {
        return getKeyPairGenerator(algorithm.algorithmName(), providerName);
    }

    public static KeyPairGenerator getKeyPairGenerator(String algorithmName) throws Exception {
        return getKeyPairGenerator(algorithmName, null);
    }

    public static KeyPairGenerator getKeyPairGenerator(String algorithmName, String providerName) throws Exception {
        providerName = checkProvider(providerName);
        KeyPairGenerator keyPairGenerator = null;
        if (providerName != null) {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithmName, providerName);
        } else {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithmName);
        }
        return keyPairGenerator;
    }

    public static KeyPair keyPairOf(AsymmetricType algorithm, byte[] publicKey, byte[] privateKey) throws Exception {
        return keyPairOf(algorithm.algorithmName(), algorithm.provider(), publicKey, privateKey);
    }

    public static KeyPair keyPairOf(AsymmetricType algorithm, String providerName, byte[] publicKey, byte[] privateKey) throws Exception {
        return keyPairOf(algorithm.algorithmName(), providerName, publicKey, privateKey);
    }

    public static KeyPair keyPairOf(String algorithmName, String providerName, byte[] publicKey, byte[] privateKey) throws Exception {
        PublicKey pub = null;
        if (publicKey != null) {
            pub = publicKeyOf(algorithmName, providerName, publicKey);
        }
        PrivateKey pri = null;
        if (privateKey != null) {
            pri = privateKeyOf(algorithmName, providerName, privateKey);
        }
        return new KeyPair(pub, pri);
    }

    public static Cipher getCipher(String type, String providerName,
                                   boolean encrypt,
                                   Key key, AlgorithmParameterSpec spec) throws Exception {
        int mode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = cipherOf(type, providerName);
        if (spec != null) {
            cipher.init(mode, key, spec);
        } else {
            cipher.init(mode, key);
        }
        return cipher;
    }

    public static Cipher cipherOf(String type, String providerName) throws Exception {
        providerName = checkProvider(providerName);
//        type=cipherAlgorithm(type);
        Cipher cipher = null;
        if (providerName == null) {
            cipher = Cipher.getInstance(type);
        } else {
            cipher = Cipher.getInstance(type, providerName);
        }
        return cipher;
    }

    public static String checkProvider(String providerName) {
        if ("".equals(providerName)) {
            providerName = null;
        }
        if (providerName != null) {
            Provider provider = Security.getProvider(providerName);
            if (provider == null) {
                providerName = null;
            }
        }
        return providerName;
    }

    public static Signature signatureOf(String type, String providerName) throws Exception {
        providerName = checkProvider(providerName);
        Signature signature = null;
        if (providerName == null) {
            signature = Signature.getInstance(type);
        } else {
            signature = Signature.getInstance(type, providerName);
        }
        return signature;
    }

    public static PublicKey publicKeyOf(AsymmetricType algorithm, String providerName, byte[] codes) throws Exception {
        return publicKeyOf(algorithm.algorithmName(), providerName, codes);
    }

    public static PrivateKey privateKeyOf(AsymmetricType algorithm, String providerName, byte[] codes) throws Exception {
        return privateKeyOf(algorithm.algorithmName(), providerName, codes);
    }

    public static KeyFactory getKeyFactory(AsymmetricType algorithm) throws Exception {
        return getKeyFactory(algorithm.algorithmName(), algorithm.provider());
    }

    public static KeyFactory getKeyFactory(String algorithmName) throws Exception {
        return getKeyFactory(algorithmName, null);
    }

    public static KeyFactory getKeyFactory(String algorithmName, String providerName) throws Exception {
        algorithmName = cipherAlgorithm(algorithmName);
        providerName = checkProvider(providerName);
        KeyFactory instance = null;
        if (providerName != null) {
            instance = KeyFactory.getInstance(algorithmName, providerName);
        } else {
            instance = KeyFactory.getInstance(algorithmName);
        }
        return instance;
    }

    public static PublicKey publicKeyOf(AsymmetricType algorithm, byte[] codes) throws Exception {
        return publicKeyOf(algorithm.algorithmName(), algorithm.provider(), codes);
    }

    public static PublicKey publicKeyOf(String algorithmName, byte[] codes) throws Exception {
        return publicKeyOf(algorithmName, null, codes);
    }

    public static PublicKey publicKeyOf(String algorithmName, String providerName, byte[] codes) throws Exception {
        KeyFactory instance = getKeyFactory(algorithmName, providerName);
        PublicKey pubKey = instance.generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public static PrivateKey privateKeyOf(AsymmetricType algorithm, byte[] codes) throws Exception {
        return privateKeyOf(algorithm.algorithmName(), algorithm.provider(), codes);
    }

    public static PrivateKey privateKeyOf(String algorithmName, byte[] codes) throws Exception {
        return privateKeyOf(algorithmName, null, codes);
    }


    public static PrivateKey privateKeyOf(String algorithmName, String providerName, byte[] codes) throws Exception {
        KeyFactory instance = getKeyFactory(algorithmName, providerName);
        PrivateKey priKey = instance.generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }
}
