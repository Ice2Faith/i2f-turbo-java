package i2f.jce.jdk.encrypt.symmetric;

import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.std.encrypt.symmetric.ISymmetricEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:53
 * @desc
 */
public class SymmetricEncryptor implements ISymmetricEncryptor {

    public static final Function<Key, SymmetricEncryptor> AES_DEFAULT = (key) -> new SymmetricEncryptor(AesType.DEFAULT, key);
    public static final Supplier<SymmetricEncryptor> NEW_AES_DEFAULT = () -> {
        try {
            return SymmetricEncryptor.genKeyEncryptor(AesType.DEFAULT);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    protected String algorithmName;
    protected String providerName;
    protected boolean noPadding;
    protected boolean requireVector;
    protected Key key;
    protected AlgorithmParameterSpec spec;

    public SymmetricEncryptor() {
    }

    public SymmetricEncryptor(String algorithmName, String providerName) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
    }

    public SymmetricEncryptor(String algorithmName, String providerName, boolean noPadding, boolean requireVector, Key key, AlgorithmParameterSpec spec) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
        this.key = key;
        this.spec = spec;
    }

    public SymmetricEncryptor(SymmetricType algorithm, Key key, AlgorithmParameterSpec spec) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.requireVector = algorithm.requireVector();
        this.key = key;
        this.spec = spec;
    }

    public SymmetricEncryptor(SymmetricType algorithm, Key key) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.requireVector = algorithm.requireVector();
        this.key = key;
    }

    public static SecretKey keyOf(SymmetricType algorithm, byte[] codes) {
        return Encryptor.keyOf(codes, algorithm);
    }

    public static AlgorithmParameterSpec specOf(byte[] codes) {
        return Encryptor.specOf(codes);
    }

    public static byte[] keyTo(Key key) {
        return Encryptor.keyTo(key);
    }

    public static byte[] specTo(IvParameterSpec spec) {
        return Encryptor.specTo(spec);
    }

    public static SecretKey genKey(SymmetricType algorithm) throws Exception {
        return genKey(algorithm, null, null);
    }

    public static SecretKey genKey(SymmetricType algorithm, byte[] keyBytes) throws Exception {
        return genKey(algorithm, keyBytes, null);
    }

    public static SecretKey genKey(SymmetricType algorithm, byte[] keyBytes, String secureRandomAlgorithmName) throws Exception {
        return Encryptor.genSecretKey(algorithm,
                keyBytes,
                secureRandomAlgorithmName);
    }

    public static AlgorithmParameterSpec genSpec(SymmetricType algorithm) throws Exception {
        return genSpec(algorithm, null, null);
    }

    public static AlgorithmParameterSpec genSpec(SymmetricType algorithm, byte[] vectorBytes) throws Exception {
        return genSpec(algorithm, vectorBytes, null);
    }

    public static AlgorithmParameterSpec genSpec(SymmetricType algorithm, byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        return Encryptor.genParameterSpec(algorithm,
                vectorBytes,
                secureRandomAlgorithmName);
    }

    public static SymmetricEncryptor genKeyEncryptor(SymmetricType algorithm) throws Exception {
        return genKeyEncryptor(algorithm, null, null, null);
    }

    public static SymmetricEncryptor genKeyEncryptor(SymmetricType algorithm, byte[] keyBytes) throws Exception {
        return genKeyEncryptor(algorithm, keyBytes, null, null);
    }

    public static SymmetricEncryptor genKeyEncryptor(SymmetricType algorithm, byte[] keyBytes, byte[] vectorBytes) throws Exception {
        return genKeyEncryptor(algorithm, keyBytes, vectorBytes, null);
    }

    public static SymmetricEncryptor genKeyEncryptor(SymmetricType algorithm, byte[] keyBytes, byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        if (algorithm.requireVector()) {
            return new SymmetricEncryptor(algorithm,
                    genKey(algorithm,
                            keyBytes,
                            secureRandomAlgorithmName),
                    Encryptor.genParameterSpec(algorithm,
                            vectorBytes,
                            secureRandomAlgorithmName));
        } else {
            return new SymmetricEncryptor(algorithm,
                    genKey(algorithm,
                            keyBytes,
                            secureRandomAlgorithmName));
        }
    }

    public static SymmetricEncryptor genPbeKeyEncryptor(PbeType algorithm, int iterationCount) throws Exception {
        return genPbeKeyEncryptor(algorithm, null, null, iterationCount, null);
    }

    public static SymmetricEncryptor genPbeKeyEncryptor(PbeType algorithm, byte[] keyBytes, int iterationCount) throws Exception {
        return genPbeKeyEncryptor(algorithm, keyBytes, null, iterationCount, null);
    }

    public static SymmetricEncryptor genPbeKeyEncryptor(PbeType algorithm, byte[] keyBytes, byte[] vectorBytes, int iterationCount) throws Exception {
        return genPbeKeyEncryptor(algorithm, keyBytes, vectorBytes, iterationCount, null);
    }

    public static SymmetricEncryptor genPbeKeyEncryptor(PbeType algorithm, byte[] keyBytes, byte[] vectorBytes, int iterationCount, String secureRandomAlgorithmName) throws Exception {
        if (algorithm.requireVector()) {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genPbeSecretKey(algorithm.algorithmName(),
                            null,
                            keyBytes
                    ),
                    Encryptor.genPbeParameterSpec(
                            vectorBytes,
                            algorithm.vectorBytesLen()[0],
                            secureRandomAlgorithmName,
                            iterationCount));
        } else {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genPbeSecretKey(algorithm.algorithmName(),
                            null,
                            keyBytes
                    ));
        }
    }

    @Override
    public void setKeyBytes(byte[] keyBytes) {
        this.key = Encryptor.keyOf(keyBytes, algorithmName);
    }

    @Override
    public byte[] getKeyBytes() {
        return key.getEncoded();
    }

    public Cipher getCipher(boolean encrypt) throws Exception {
        return Encryptor.getCipher(algorithmName, providerName, encrypt, key, requireVector ? spec : null);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
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

    public boolean isRequireVector() {
        return requireVector;
    }

    public void setRequireVector(boolean requireVector) {
        this.requireVector = requireVector;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public AlgorithmParameterSpec getSpec() {
        return spec;
    }

    public void setSpec(AlgorithmParameterSpec spec) {
        this.spec = spec;
    }

    public byte[] keyTo() {
        return keyTo(key);
    }

    public void keyOf(byte[] codes) {
        this.key = Encryptor.keyOf(codes, algorithmName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SymmetricEncryptor that = (SymmetricEncryptor) o;
        return noPadding == that.noPadding &&
                requireVector == that.requireVector &&
                Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(key, that.key) &&
                Objects.equals(spec, that.spec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, providerName, noPadding, requireVector, key, spec);
    }

    @Override
    public String toString() {
        return "SymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", requireVector=" + requireVector +
                ", key=" + key +
                ", spec=" + spec +
                '}';
    }
}
