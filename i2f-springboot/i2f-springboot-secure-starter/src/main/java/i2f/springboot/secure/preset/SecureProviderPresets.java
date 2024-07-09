package i2f.springboot.secure.preset;


import i2f.code.CodeUtil;
import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.BcProvider;
import i2f.extension.jce.sm.antherd.digest.Sm3Digester;
import i2f.extension.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.extension.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;
import i2f.jce.jdk.digest.md.MessageDigester;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.encrypt.asymmetric.RsaType;
import i2f.jce.jdk.encrypt.symmetric.AesType;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.supports.MessageDigestAlgorithm;
import i2f.jce.std.digest.IMessageDigester;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.jce.std.encrypt.symmetric.ISymmetricEncryptor;

import java.security.KeyPair;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/29 9:46
 * @desc
 */
public class SecureProviderPresets {
    public static Supplier<IAsymmetricEncryptor> asymmetricEncryptorSupplier_RSA = () -> new AsymmetricEncryptor(RsaType.NONE_PKCS1PADDING);

    public static Function<Integer, AsymKeyPair> asymmetricKeyPairSupplier_RSA = (len) -> {
        try {
            BcProvider.registryProvider();
            KeyPair keyPair = Encryptor.genKeyPair(RsaType.NONE_PKCS1PADDING, BcProvider.PROVIDER_NAME, null, len, null);

            IAsymmetricEncryptor encryptor = asymmetricEncryptorSupplier_RSA.get();
            encryptor.setKeyPair(keyPair);
            return encryptor.getAsymKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    public static Function<byte[], ISymmetricEncryptor> symmetricEncryptorSupplier_AES = (secretBytes) -> new SymmetricEncryptor(AesType.ECB_ISO10126Padding, SymmetricEncryptor.keyOf(AesType.ECB_ISO10126Padding, secretBytes));

    public static Function<Integer, byte[]> symmetricKeySupplier_AES = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static Supplier<IMessageDigester> messageDigesterSupplier_SHA256 = () -> new MessageDigester(MessageDigester.messageDigestOf(MessageDigestAlgorithm.SHA_256));

    public static Supplier<IAsymmetricEncryptor> asymmetricEncryptorSupplier_SM2 = () -> new Sm2Encryptor();

    public static Function<Integer, AsymKeyPair> asymmetricKeyPairSupplier_SM2 = (len) -> {
        try {
            KeyPair keyPair = Sm2Encryptor.genKeyPair();

            IAsymmetricEncryptor encryptor = asymmetricEncryptorSupplier_SM2.get();
            encryptor.setKeyPair(keyPair);
            return encryptor.getAsymKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    public static Function<byte[], ISymmetricEncryptor> symmetricEncryptorSupplier_SM4 = (secretBytes) -> new Sm4Encryptor(new String(secretBytes));

    public static Function<Integer, byte[]> symmetricKeySupplier_SM4 = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static Supplier<IMessageDigester> messageDigesterSupplier_SM3 = () -> new Sm3Digester();

}
