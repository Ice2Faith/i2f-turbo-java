package i2f.extension.jce.bc.digest.md;

import i2f.extension.jce.bc.BcProvider;
import i2f.extension.jce.bc.supports.MessageDigestAlgorithm;
import i2f.jce.jdk.digest.md.MessageDigester;

import java.security.MessageDigest;

/**
 * @author Ice2Faith
 * @date 2024/5/23 19:50
 * @desc
 */
public class BcMessageDigester extends MessageDigester {
    static {
        BcProvider.registryProvider();
    }

    public static final BcMessageDigester MD5 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.MD5.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA_1 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_1.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA224 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA224.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA256 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA256.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA384 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA384.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA512 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA512.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA512_224 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA512_224.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA512_256 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA512_256.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHAKE_128 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHAKE_128.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHAKE_256 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHAKE_256.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA3_224 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA3_224.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA3_256 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA3_256.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA3_384 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA3_384.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SHA3_512 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA3_512.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester SM3 = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.SM3.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester Tiger = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.Tiger.text(), BcProvider.PROVIDER_NAME));
    public static final BcMessageDigester Whirlpool = new BcMessageDigester(messageDigestOf(MessageDigestAlgorithm.Whirlpool.text(), BcProvider.PROVIDER_NAME));


    public BcMessageDigester() {
    }

    public BcMessageDigester(MessageDigest provider) {
        super(provider);
    }
}
