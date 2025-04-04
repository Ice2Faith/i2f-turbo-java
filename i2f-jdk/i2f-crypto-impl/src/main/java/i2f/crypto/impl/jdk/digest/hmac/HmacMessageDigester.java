package i2f.crypto.impl.jdk.digest.hmac;

import i2f.crypto.impl.jdk.supports.MacAlgorithm;
import i2f.crypto.std.digest.IMessageDigester;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:00
 * @desc
 */
public class HmacMessageDigester implements IMessageDigester {

    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_MD2 = (key, providerName) -> getInstance("MD2", key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_MD5 = (key, providerName) -> getInstance(MacAlgorithm.HmacMD5.text(), key, providerName);

    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_SHA_1 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA1.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_SHA_224 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA224.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_SHA_256 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA256.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_SHA_384 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA384.text(), key, providerName);
    public static final BiFunction<byte[], String, HmacMessageDigester> HMAC_SHA_512 = (key, providerName) -> getInstance(MacAlgorithm.HmacSHA512.text(), key, providerName);


    protected Mac provider;

    public HmacMessageDigester() {
    }

    public HmacMessageDigester(Mac provider) {
        this.provider = provider;
    }

    @Override
    public byte[] digest(InputStream is) throws Exception {
        return getHmacs(is, provider);
    }

    public static HmacMessageDigester getInstance(String mdType, byte[] key, String providerName) {
        return new HmacMessageDigester(hmacInstance(mdType, key, providerName));
    }

    public static String hmacName(String mdType) {
        String hmacName = mdType;
        if (!hmacName.startsWith("Hmac")) {
            hmacName = "Hmac" + mdType.replaceAll("-", "");
        }
        return hmacName;
    }

    public static Mac hmacInstance(String mdType, byte[] key, String providerName) {
        try {
            String hmacName = hmacName(mdType);
            SecretKey skey = new SecretKeySpec(key, hmacName);
            Mac mac = macOf(hmacName, providerName);
            mac.init(skey);
            return mac;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Mac macOf(String type, String providerName) {
        try {
            if ("".equals(providerName)) {
                providerName = null;
            }
            if (providerName != null) {
                Provider provider = Security.getProvider(providerName);
                if (provider == null) {
                    providerName = null;
                }
            }
            Mac mac = null;
            if (providerName != null) {
                mac = Mac.getInstance(type, providerName);
            } else {
                mac = Mac.getInstance(type);
            }
            return mac;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static byte[] getHmacs(InputStream is, Mac mac) throws IOException {
        byte[] buf = new byte[16];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            mac.update(buf, 0, len);
        }
        is.close();
        return mac.doFinal();
    }


    public Mac getProvider() {
        return provider;
    }

    public void setProvider(Mac provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmacMessageDigester that = (HmacMessageDigester) o;
        return Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider);
    }

    @Override
    public String toString() {
        return "HmacMessageDigester{" +
                "provider=" + provider +
                '}';
    }
}
