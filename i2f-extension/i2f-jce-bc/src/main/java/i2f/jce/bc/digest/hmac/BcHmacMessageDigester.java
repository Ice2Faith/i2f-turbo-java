package i2f.jce.bc.digest.hmac;

import i2f.jce.bc.BcProvider;
import i2f.jce.bc.supports.MacAlgorithm;
import i2f.jce.jdk.digest.hmac.HmacMessageDigester;

import javax.crypto.Mac;
import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/5/23 20:32
 * @desc
 */
public class BcHmacMessageDigester extends HmacMessageDigester {
    static {
        BcProvider.registryProvider();
    }

    public static final BiFunction<byte[], String, HmacMessageDigester> HmacMD5 = (key, providerName) -> getInstance(MacAlgorithm.HmacMD5.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA1 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA1.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA224 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA224.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA256 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA256.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA384 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA384.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA512 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA512.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacRIPEMD128 = (key, providerName) -> getInstance(MacAlgorithm.HmacRIPEMD128.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacRIPEMD160 = (key, providerName) -> getInstance(MacAlgorithm.HmacRIPEMD160.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacRIPEMD256 = (key, providerName) -> getInstance(MacAlgorithm.HmacRIPEMD256.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacRIPEMD320 = (key, providerName) -> getInstance(MacAlgorithm.HmacRIPEMD320.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA512_224 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA512_224.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSHA512_256 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA512_256.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacSM3 = (key, providerName) -> getInstance(MacAlgorithm.HmacSM3.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacTiger = (key, providerName) -> getInstance(MacAlgorithm.HmacTiger.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HmacWhirlpool = (key, providerName) -> getInstance(MacAlgorithm.HmacWhirlpool.text(), key, providerName);

    public BcHmacMessageDigester() {
    }

    public BcHmacMessageDigester(Mac provider) {
        super(provider);
    }
}
