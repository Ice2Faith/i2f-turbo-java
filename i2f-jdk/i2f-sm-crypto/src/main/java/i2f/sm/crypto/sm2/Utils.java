package i2f.sm.crypto.sm2;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.sm.crypto.util.CipUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:41
 */
public class Utils {
    public static final SecureRandom rng = new SecureRandom();
    public static final EcParam EC_PARAM = generateEcparam();

    /**
     * 获取公共椭圆曲线
     */
    public static EcCurveFp getGlobalCurve() {
        return EC_PARAM.getCurve();
    }

    /**
     * 生成ecparam
     */
    public static EcParam generateEcparam() {
        // 椭圆曲线
        BigInteger p = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
        BigInteger a = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
        BigInteger b = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
        EcCurveFp curve = new EcCurveFp(p, a, b);

        // 基点
        String gxHex = "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7";
        String gyHex = "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0";
        EcPointFp g = curve.decodePointHex("04" + gxHex + gyHex);

        BigInteger n = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);

        return new EcParam(curve, g, n);
    }

    public static KeyPair generateKeyPairHex() {
        return generateKeyPairHex(null, 10, 1);
    }
    /**
     * 生成密钥对：publicKey = privateKey * G
     */
    public static KeyPair generateKeyPairHex(String a, int b, int c) {
        if (b <= 0) {
            b = 10;
        }
        c = c >= 0 ? 1 : -1;

        BigInteger random = a != null ? new BigInteger(a, b).multiply(BigInteger.valueOf(c)) : new BigInteger(EC_PARAM.getN().bitLength(), rng);
        BigInteger d = random.mod(EC_PARAM.getN().subtract(BigInteger.ONE)).add(BigInteger.ONE); // 随机数
        String privateKey = CipUtils.leftPad(d.toString(16), 64);

        BigInteger P = EC_PARAM.getG().multiply(d); // P = dG，p 为公钥，d 为私钥
        String Px = CipUtils.leftPad(P.getX().toBigInteger().toString(16), 64);
        String Py = CipUtils.leftPad(P.getY().toBigInteger().toString(16), 64);
        String publicKey = "04" + Px + Py;

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 生成压缩公钥
     */
    public static String compressPublicKeyHex(String s) {
        if (s.length() != 130) {
            throw new Error("Invalid public key to compress");
        }

        int len = (s.length() - 2) / 2;
        String xHex = s.substring(2, len);
        BigInteger y = new BigInteger(s.substring(len + 2), 16);

        String prefix = "03";
        if (y.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {
            prefix = "02";
        }

        return prefix + xHex;
    }

    /**
     * utf8串转16进制串
     */
    public static String utf8ToHex(String input) {
        byte[] bytes = CharsetStringByteCodec.UTF8.decode(input);
        return HexStringByteCodec.INSTANCE.encode(bytes);
    }

    /**
     * 验证公钥是否为椭圆曲线上的点
     */
    public static boolean verifyPublicKey(String publicKey) {
        EcCurveFp curve = EC_PARAM.getCurve();
        EcPointFp point = curve.decodePointHex(publicKey)
        if (point == null) {
            return false;
        }

        EcFieldElementFp x = point.getX();
        EcFieldElementFp y = point.getY();

        // 验证 y^2 是否等于 x^3 + ax + b
        return y.square().equals(x.multiply(x.square()).add(x.multiply(curve.a)).add(curve.b));
    }

    /**
     * 验证公钥是否等价，等价返回true
     */
    public static boolean comparePublicKeyHex(String publicKey1, String publicKey2) {
        EcCurveFp curve = EC_PARAM.getCurve();
        EcPointFp point1 = curve.decodePointHex(publicKey1);
        if (point1 == null) {
            return false;
        }

        EcPointFp point2 = curve.decodePointHex(publicKey2);
        if (point2 == null) {
            return false;
        }

        return point1.equals(point2);
    }
}
