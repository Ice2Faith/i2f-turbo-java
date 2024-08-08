package i2f.swl.cert;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.str.code.UrlCodeStringCodec;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.data.SwlCertPair;

/**
 * @author Ice2Faith
 * @date 2024/8/8 15:14
 * @desc
 */
public class SwlCertUtil {

    public static String serialize(SwlCert cert) {
        StringBuilder builder = new StringBuilder();
        builder.append("certId=").append(UrlCodeStringCodec.INSTANCE.encode(cert.getCertId())).append("\n");
        builder.append("publicKey=").append(UrlCodeStringCodec.INSTANCE.encode(cert.getPublicKey())).append("\n");
        builder.append("privateKey=").append(UrlCodeStringCodec.INSTANCE.encode(cert.getPrivateKey())).append("\n");
        builder.append("remotePublicKey=").append(UrlCodeStringCodec.INSTANCE.encode(cert.getRemotePublicKey())).append("\n");
        String prop = builder.toString();
        return Base64StringByteCodec.INSTANCE.encode(CharsetStringByteCodec.UTF8.decode(prop));
    }

    public static SwlCert deserialize(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        SwlCert ret = new SwlCert();
        String prop = CharsetStringByteCodec.UTF8.encode(Base64StringByteCodec.INSTANCE.decode(str));
        String[] lines = prop.split("\n");
        for (String line : lines) {
            String[] pair = line.split("=", 2);
            if (pair.length < 2) {
                continue;
            }
            String name = pair[0];
            String value = UrlCodeStringCodec.INSTANCE.decode(pair[1]);
            if ("certId".equals(name)) {
                ret.setCertId(value);
            } else if ("publicKey".equals(name)) {
                ret.setPublicKey(value);
            } else if ("privateKey".equals(name)) {
                ret.setPrivateKey(value);
            } else if ("remotePublicKey".equals(name)) {
                ret.setRemotePublicKey(value);
            }
        }

        return ret;
    }

    public static SwlCertPair make(String certId, AsymKeyPair serverKeyPair, AsymKeyPair clientKeyPair) {
        SwlCert server = new SwlCert(certId,
                serverKeyPair.getPublicKey(),
                serverKeyPair.getPrivateKey(),
                clientKeyPair.getPublicKey());
        SwlCert client = new SwlCert(certId,
                clientKeyPair.getPublicKey(),
                clientKeyPair.getPrivateKey(),
                serverKeyPair.getPublicKey());
        return new SwlCertPair(server, client);
    }

}
