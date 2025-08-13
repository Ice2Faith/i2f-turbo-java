package i2f.sm.crypto.sm2;

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
        BigInteger g = curve.decodePointHex("04" + gxHex + gyHex);

        BigInteger n = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);

        EcParam ret = new EcParam();
        ret.setCurve(curve);
        ret.setG(g);
        ret.setN(n);
        return ret;
    }
}
