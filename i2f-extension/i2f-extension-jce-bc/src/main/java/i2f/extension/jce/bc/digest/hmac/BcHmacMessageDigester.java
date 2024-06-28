package i2f.extension.jce.bc.digest.hmac;

import i2f.extension.jce.bc.BcProvider;
import i2f.extension.jce.bc.supports.MacAlgorithm;
import i2f.jce.jdk.digest.hmac.HmacMessageDigester;

import javax.crypto.Mac;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/5/23 20:32
 * @desc
 */
public class BcHmacMessageDigester extends HmacMessageDigester {
    static {
        BcProvider.registryProvider();
    }

    public static final Function<byte[], HmacMessageDigester> HmacMD5 = (key) -> getInstance(MacAlgorithm.HmacMD5.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA1 = (key) -> getInstance(MacAlgorithm.HmacSHA1.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA224 = (key) -> getInstance(MacAlgorithm.HmacSHA224.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA256 = (key) -> getInstance(MacAlgorithm.HmacSHA256.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA384 = (key) -> getInstance(MacAlgorithm.HmacSHA384.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA512 = (key) -> getInstance(MacAlgorithm.HmacSHA512.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacRIPEMD128 = (key) -> getInstance(MacAlgorithm.HmacRIPEMD128.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacRIPEMD160 = (key) -> getInstance(MacAlgorithm.HmacRIPEMD160.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacRIPEMD256 = (key) -> getInstance(MacAlgorithm.HmacRIPEMD256.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacRIPEMD320 = (key) -> getInstance(MacAlgorithm.HmacRIPEMD320.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA512_224 = (key) -> getInstance(MacAlgorithm.HmacSHA512_224.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSHA512_256 = (key) -> getInstance(MacAlgorithm.HmacSHA512_256.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacSM3 = (key) -> getInstance(MacAlgorithm.HmacSM3.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacTiger = (key) -> getInstance(MacAlgorithm.HmacTiger.text(), key, BcProvider.PROVIDER_NAME);
    public static final Function<byte[], HmacMessageDigester> HmacWhirlpool = (key) -> getInstance(MacAlgorithm.HmacWhirlpool.text(), key, BcProvider.PROVIDER_NAME);

    public BcHmacMessageDigester() {
    }

    public BcHmacMessageDigester(Mac provider) {
        super(provider);
    }
}
