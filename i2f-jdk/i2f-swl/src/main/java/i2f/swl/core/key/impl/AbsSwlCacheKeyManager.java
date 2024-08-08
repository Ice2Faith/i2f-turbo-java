package i2f.swl.core.key.impl;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.std.ISwlObfuscator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/8/8 22:40
 * @desc
 */
@Data
@NoArgsConstructor
public abstract class AbsSwlCacheKeyManager {
    public static final String DEFAULT_KEY_PREFIX = "swl:key:";
    public static final String SELF_SUFFIX = "self:keys:";
    public static final String OTHER_SUFFIX = "client:keys:";
    public static final String DEFAULT_SELF_NAME = "self:default";
    public static final String DEFAULT_OTHER_NAME = "other:default";
    public static final String KEY_PAIR_SEPARATOR = "\n====\n";

    protected String cachePrefix = DEFAULT_KEY_PREFIX;


    protected ISwlObfuscator obfuscator = new SwlBase64Obfuscator();

    public String getSignName(String asymSign, boolean self) {
        if (self) {
            return SELF_SUFFIX + asymSign;
        }
        return OTHER_SUFFIX + asymSign;
    }

    public String cacheKey(String key) {
        if (cachePrefix == null || cachePrefix.isEmpty()) {
            return key;
        }
        return cachePrefix + ":" + key;
    }

    public String obfuscateEncode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.encode(data);
    }

    public String obfuscateDecode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.decode(data);
    }


    public String serializeKeyPair(AsymKeyPair keyPair) {
        StringBuilder builder = new StringBuilder();
        builder.append(keyPair.getPublicKey() == null ? "" : obfuscateEncode(keyPair.getPublicKey()));
        builder.append(KEY_PAIR_SEPARATOR);
        builder.append(keyPair.getPrivateKey() == null ? "" : obfuscateEncode(keyPair.getPrivateKey()));
        return builder.toString();
    }

    public AsymKeyPair deserializeKeyPair(String str) {
        String[] arr = str.split(KEY_PAIR_SEPARATOR, 2);
        String publicKey = arr[0];
        if (publicKey.isEmpty()) {
            publicKey = null;
        }
        String privateKey = null;
        if (arr.length > 1) {
            privateKey = arr[1];
        }
        if (privateKey != null && privateKey.isEmpty()) {
            privateKey = null;
        }
        return new AsymKeyPair(
                obfuscateDecode(publicKey),
                obfuscateDecode(privateKey)
        );
    }

}
