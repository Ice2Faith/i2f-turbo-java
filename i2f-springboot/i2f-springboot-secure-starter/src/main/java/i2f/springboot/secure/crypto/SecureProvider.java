package i2f.springboot.secure.crypto;


import i2f.code.CodeUtil;
import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.BcProvider;
import i2f.jce.jdk.digest.md.MessageDigester;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.encrypt.asymmetric.RsaType;
import i2f.jce.jdk.encrypt.symmetric.AesType;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.supports.MessageDigestAlgorithm;
import i2f.jce.std.digest.IMessageDigester;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;

import java.security.KeyPair;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/29 9:46
 * @desc
 */
public class SecureProvider {
    public static Supplier<AsymmetricEncryptor> asymmetricEncryptorSupplier = () -> new AsymmetricEncryptor(RsaType.NONE_PKCS1PADDING);

    public static Function<byte[], SymmetricEncryptor> symmetricEncryptorSupplier = (secretBytes) -> new SymmetricEncryptor(AesType.ECB_ISO10126Padding, SymmetricEncryptor.keyOf(AesType.ECB_ISO10126Padding, secretBytes));

    public static Supplier<IMessageDigester> messageDigesterSupplier = () -> new MessageDigester(MessageDigester.messageDigestOf(MessageDigestAlgorithm.SHA_256));

    public static Function<Integer, byte[]> symmetricKeySupplier = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static Function<Integer, AsymKeyPair> asymmetricKeyPairSupplier = (len) -> {
        try {
            BcProvider.registryProvider();
            KeyPair keyPair = Encryptor.genKeyPair(RsaType.NONE_PKCS1PADDING, BcProvider.PROVIDER_NAME, null, len, null);

            AsymmetricEncryptor encryptor = asymmetricEncryptorSupplier.get();
            encryptor.setKeyPair(keyPair);
            return encryptor.getAsymKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };
}
