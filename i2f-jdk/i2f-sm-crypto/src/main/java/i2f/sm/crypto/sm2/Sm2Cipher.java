package i2f.sm.crypto.sm2;

import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm2.ec.EcCurveFp;
import i2f.sm.crypto.sm2.ec.EcParam;
import i2f.sm.crypto.sm2.ec.EcPointFp;
import i2f.sm.crypto.util.CipUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:39
 */
public class Sm2Cipher {
    public static enum CipherMode {
        C1C3C2, // default
        C1C2C3,
    }

    public static final EcParam EC_PARAM = Utils.generateEcparam();

    public static String doEncrypt(String msg, String publicKey, CipherMode cipherMode) {
        byte[] msgArr = Utils.hexToArray(Utils.utf8ToHex(msg));
        byte[] retArr = doEncrypt(msgArr, publicKey, cipherMode);
        return CipUtils.ArrayToHex(retArr);
    }

    /**
     * 加密
     */
    public static byte[] doEncrypt(byte[] msg, String publicKeyHex, CipherMode cipherMode) {
        if (cipherMode == null) {
            cipherMode = CipherMode.C1C3C2;
        }
        EcPointFp publicKey = Utils.getGlobalCurve().decodePointHex(publicKeyHex); // 先将公钥转成点

        KeyPair keypair = Utils.generateKeyPairHex();
        BigInteger k = new BigInteger(keypair.privateKey, 16); // 随机数 k

        // c1 = k * G
        String c1 = keypair.publicKey;
        if (c1.length() > 128) {
            c1 = c1.substring(c1.length() - 128);
        }

        // (x2, y2) = k * publicKey
        EcPointFp p = publicKey.multiply(k);
        byte[] x2 = Utils.hexToArray(CipUtils.leftPad(p.getX().toBigInteger().toString(16), 64));
        byte[] y2 = Utils.hexToArray(CipUtils.leftPad(p.getY().toBigInteger().toString(16), 64));

        // c3 = hash(x2 || msg || y2)
        //[].concat(x2, msg, y2)
        byte[] tmp = new byte[x2.length + msg.length + y2.length];
        System.arraycopy(x2, 0, tmp, 0, x2.length);
        System.arraycopy(msg, 0, tmp, x2.length, msg.length);
        System.arraycopy(y2, 0, tmp, x2.length + msg.length, y2.length);
        byte[] c3 = Sm3Inner.sm3(tmp);

        AtomicInteger ct = new AtomicInteger(1);
        AtomicInteger offset = new AtomicInteger(0);
        AtomicReference<byte[]> t = new AtomicReference<>(new byte[256 / 8]); // 256 位
        // const z = [].concat(x2, y2);
        byte[] z = new byte[x2.length + y2.length];
        System.arraycopy(x2, 0, z, 0, x2.length);
        System.arraycopy(y2, 0, z, x2.length, y2.length);
        Runnable nextT = () -> {
            // (1) Hai = hash(z || ct)
            // (2) ct++
            // [...z, ct >> 24 & 0x00ff, ct >> 16 & 0x00ff, ct >> 8 & 0x00ff, ct & 0x00ff]
            byte[] buff = new byte[z.length + 4];
            System.arraycopy(z, 0, buff, 0, z.length);
            buff[z.length + 0] = (byte) (ct.get() >> 24 & 0x00ff);
            buff[z.length + 1] = (byte) (ct.get() >> 16 & 0x00ff);
            buff[z.length + 2] = (byte) (ct.get() >> 8 & 0x00ff);
            buff[z.length + 3] = (byte) (ct.get() & 0x00ff);
            t.set(Sm3Inner.sm3(buff));
            ct.incrementAndGet();
            offset.set(0);
        };
        nextT.run(); // 先生成 Ha1

        for (int i = 0, len = msg.length; i < len; i++) {
            // t = Ha1 || Ha2 || Ha3 || Ha4
            if (offset.get() == t.get().length) {
                nextT.run();
            }

            // c2 = msg ^ t
            msg[i] = (byte) ((msg[i] ^ t.get()[offset.getAndIncrement()]) & 0x0ff);
        }
        byte[] c2 = msg;
        byte[] bc1 = CipUtils.hexToArray(c1);

        byte[] ret = new byte[bc1.length + c2.length + c3.length];
        System.arraycopy(bc1, 0, ret, 0, bc1.length);
        if (cipherMode == CipherMode.C1C2C3) {
            System.arraycopy(c2, 0, ret, bc1.length, c2.length);
            System.arraycopy(c3, 0, ret, bc1.length + c2.length, c3.length);
        } else {
            System.arraycopy(c3, 0, ret, bc1.length, c3.length);
            System.arraycopy(c2, 0, ret, bc1.length + c3.length, c2.length);
        }

        return ret;
    }

    public static String doDecrypt(String encryptData, String privateKey, CipherMode cipherMode) {
        byte[] encArr = CipUtils.hexToArray(encryptData);
        byte[] retArr = doDecrypt(encArr, privateKey, cipherMode);
        return Utils.arrayToUtf8(retArr);
    }

    /**
     * 解密
     */
    public static byte[] doDecrypt(byte[] encryptData, String privateKeyHex, CipherMode cipherMode) {
        if (cipherMode == null) {
            cipherMode = CipherMode.C1C2C3;
        }
        BigInteger privateKey = new BigInteger(privateKeyHex, 16);

        byte[] c3 = Arrays.copyOfRange(encryptData, 128 / 8, 128 / 8 + 64 / 8);
        byte[] c2 = Arrays.copyOfRange(encryptData, 128 / 8 + 64 / 8, encryptData.length);

        if (cipherMode == CipherMode.C1C2C3) {
            c3 = Arrays.copyOfRange(encryptData, encryptData.length - 64 / 8, encryptData.length);
            c2 = Arrays.copyOfRange(encryptData, 128 / 8, encryptData.length - 128 / 8 - 64 / 8);
        }

        byte[] msg = c2;

        EcPointFp c1 = Utils.getGlobalCurve().decodePointHex("04" + CipUtils.ArrayToHex(Arrays.copyOfRange(encryptData, 0 / 8, 128 / 8)));

        EcPointFp p = c1.multiply(privateKey);
        byte[] x2 = Utils.hexToArray(CipUtils.leftPad(p.getX().toBigInteger().toString(16), 64));
        byte[] y2 = Utils.hexToArray(CipUtils.leftPad(p.getY().toBigInteger().toString(16), 64));

        AtomicInteger ct = new AtomicInteger(1);
        AtomicInteger offset = new AtomicInteger(0);
        AtomicReference<byte[]> t = new AtomicReference<>(new byte[256 / 8]); // 256 位
        // const z = [].concat(x2, y2)
        byte[] z = new byte[x2.length + y2.length];
        System.arraycopy(x2, 0, z, 0, x2.length);
        System.arraycopy(y2, 0, z, x2.length, y2.length);
        Runnable nextT = () -> {
            // (1) Hai = hash(z || ct)
            // (2) ct++
            // [...z, ct >> 24 & 0x00ff, ct >> 16 & 0x00ff, ct >> 8 & 0x00ff, ct & 0x00ff]
            byte[] buff = new byte[z.length + 4];
            System.arraycopy(z, 0, buff, 0, z.length);
            buff[z.length + 0] = (byte) (ct.get() >> 24 & 0x00ff);
            buff[z.length + 1] = (byte) (ct.get() >> 16 & 0x00ff);
            buff[z.length + 2] = (byte) (ct.get() >> 8 & 0x00ff);
            buff[z.length + 3] = (byte) (ct.get() & 0x00ff);
            t.set(Sm3Inner.sm3(buff));
            ct.incrementAndGet();
            offset.set(0);
        };
        nextT.run(); // 先生成 Ha1

        for (int i = 0, len = msg.length; i < len; i++) {
            // t = Ha1 || Ha2 || Ha3 || Ha4
            if (offset.get() == t.get().length) {
                nextT.run();
            }

            // c2 = msg ^ t
            msg[i] = (byte) (msg[i] ^ t.get()[offset.getAndIncrement()] & 0x0ff);
        }

        // c3 = hash(x2 || msg || y2)
        // const checkC3 = _.arrayToHex(sm3([].concat(x2, msg, y2)))
        byte[] tmp = new byte[x2.length + msg.length + y2.length];
        System.arraycopy(x2, 0, tmp, 0, x2.length);
        System.arraycopy(msg, 0, tmp, x2.length, msg.length);
        System.arraycopy(y2, 0, tmp, x2.length + msg.length, y2.length);
        byte[] checkC3 = Sm3Inner.sm3(tmp);
        if (!Arrays.equals(checkC3, c3)) {
            throw new SmException("decrypt error");
        }

        return msg;
    }
    public static String doSignature(String msg, String privateKey
    ){
        return doSignature(msg,privateKey,null,false,false,null,null);
    }
    public static String doSignature(String msg, String privateKey,
                                     List<Point> pointPool, boolean der, boolean hash, String publicKey, String userId
    ) {
        String hex = Utils.utf8ToHex(msg);
        byte[] msgArr = Utils.hexToArray(hex);
        byte[] retArr = doSignature(msgArr, privateKey, pointPool, der, hash, publicKey, userId);
        return CipUtils.ArrayToHex(retArr);
    }

    public static byte[] doSignature(byte[] msg, String privateKey) {
        return doSignature(msg, privateKey, null, false, false, null, null);
    }

    /**
     * 签名
     */
    public static byte[] doSignature(byte[] msg, String privateKey,
                                     List<Point> pointPool, boolean der, boolean hash, String publicKey, String userId
    ) {
        String hashHex = CipUtils.ArrayToHex(msg);

        if (hash) {
            // sm3杂凑
            if (publicKey == null || publicKey.isEmpty()) {
                publicKey = getPublicKeyFromPrivateKey(privateKey);
            }
            hashHex = getHash(hashHex, publicKey, userId);
        }

        BigInteger dA = new BigInteger(privateKey, 16);
        BigInteger e = new BigInteger(hashHex, 16);

        // k
        BigInteger k = null;
        BigInteger r = null;
        BigInteger s = null;

        BigInteger n = EC_PARAM.getN();
        do {
            do {
                Point point = null;
                if (pointPool != null && !pointPool.isEmpty()) {
                    point = pointPool.get(0);
                } else {
                    point = getPoint();
                }
                k = point.k;

                // r = (e + x1) mod n
                r = e.add(point.x1).mod(n);
            } while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));

            // s = ((1 + dA)^-1 * (k - r * dA)) mod n
            s = dA.add(BigInteger.ONE).modInverse(n).multiply(k.subtract(r.multiply(dA))).mod(n);
        } while (s.equals(BigInteger.ZERO));

        if (der) {
            String retHex = Asn1.encodeDer(r, s); // asn.1 der 编码
            return Utils.hexToArray(retHex);
        }

        String retHex = CipUtils.leftPad(r.toString(16), 64) + CipUtils.leftPad(s.toString(16), 64);
        return Utils.hexToArray(retHex);
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey) {
        return doVerifySignature(msg, signHex, publicKey, false, false, null);
    }

    public static boolean doVerifySignature(String msg, String signHex, String publicKey,
                                            boolean der, boolean hash, String userId) {
        String hex = Utils.utf8ToHex(msg);
        byte[] msgArr = CipUtils.hexToArray(hex);
        return doVerifySignature(msgArr, CipUtils.hexToArray(hex), publicKey, der, hash, userId);
    }

    public static boolean doVerifySignature(byte[] msg, byte[] signHex, String publicKey) {
        return doVerifySignature(msg, signHex, publicKey, false, false, null);
    }

    /**
     * 验签
     */
    public static boolean doVerifySignature(byte[] msg, byte[] signHexByte, String publicKey,
                                            boolean der, boolean hash, String userId) {
        String signHex = CipUtils.ArrayToHex(signHexByte);
        String hashHex = CipUtils.ArrayToHex(msg);

        if (hash) {
            // sm3杂凑
            hashHex = getHash(hashHex, publicKey, userId);
        }

        BigInteger r = null;
        BigInteger s = null;
        if (der) {
            Asn1.Der decodeDerObj = Asn1.decodeDer(signHex); // asn.1 der 解码
            r = decodeDerObj.getR();
            s = decodeDerObj.getS();
        } else {
            r = new BigInteger(signHex.substring(0, 64), 16);
            s = new BigInteger(signHex.substring(64), 16);
        }

        EcCurveFp curve = EC_PARAM.getCurve();
        BigInteger n = EC_PARAM.getN();
        EcPointFp G = EC_PARAM.getG();

        EcPointFp PA = curve.decodePointHex(publicKey);
        BigInteger e = new BigInteger(hashHex, 16);

        // t = (r + s) mod n
        BigInteger t = r.add(s).mod(n);

        if (t.equals(BigInteger.ZERO)) {
            return false;
        }

        // x1y1 = s * G + t * PA
        EcPointFp x1y1 = G.multiply(s).add(PA.multiply(t));

        // R = (e + x1) mod n
        BigInteger R = e.add(x1y1.getX().toBigInteger()).mod(n);

        return r.equals(R);
    }

    /**
     * sm3杂凑算法
     */
    public static String getHash(String hashHex, String publicKey, String userId) {
        if (userId == null || userId.isEmpty()) {
            userId = "1234567812345678";
        }
        EcPointFp G = EC_PARAM.getG();
        // z = hash(entl || userId || a || b || gx || gy || px || py)
        userId = Utils.utf8ToHex(userId);
        String a = CipUtils.leftPad(G.getCurve().getA().toBigInteger().toString(16), 64);
        String b = CipUtils.leftPad(G.getCurve().getB().toBigInteger().toString(16), 64);
        String gx = CipUtils.leftPad(G.getX().toBigInteger().toString(16), 64);
        String gy = CipUtils.leftPad(G.getY().toBigInteger().toString(16), 64);
        String px = null;
        String py = null;
        if (publicKey.length() == 128) {
            px = publicKey.substring(0, 64);
            py = publicKey.substring(64, 64 + 64);
        } else {
            EcPointFp point = G.getCurve().decodePointHex(publicKey);
            px = CipUtils.leftPad(point.getX().toBigInteger().toString(16), 64);
            py = CipUtils.leftPad(point.getY().toBigInteger().toString(16), 64);
        }
        byte[] data = Utils.hexToArray(userId + a + b + gx + gy + px + py);

        int entl = userId.length() * 4;

        byte[] tmp = new byte[2 + data.length];
        tmp[0] = (byte) (entl & 0x00ff);
        tmp[1] = (byte) (entl >> 8 & 0x00ff);
        System.arraycopy(data, 0, tmp, 2, data.length);
        data = tmp;

        byte[] z = Sm3Inner.sm3(data);

        // e = hash(z || msg)
        byte[] hashBytes = Utils.hexToArray(hashHex);
        byte[] buff = new byte[z.length + hashBytes.length];
        System.arraycopy(z, 0, buff, 0, z.length);
        System.arraycopy(hashBytes, 0, buff, z.length, hashBytes.length);
        return CipUtils.ArrayToHex(Sm3Inner.sm3(buff));
    }

    /**
     * 计算公钥
     */
    public static String getPublicKeyFromPrivateKey(String privateKey) {
        EcPointFp G = EC_PARAM.getG();
        EcPointFp PA = G.multiply(new BigInteger(privateKey, 16));
        String x = CipUtils.leftPad(PA.getX().toBigInteger().toString(16), 64);
        String y = CipUtils.leftPad(PA.getY().toBigInteger().toString(16), 64);
        return "04" + x + y;
    }

    @Data
    @NoArgsConstructor
    public static class Point extends KeyPair {
        protected BigInteger k;
        protected BigInteger x1;

    }

    /**
     * 获取椭圆曲线点
     */
    public static Point getPoint() {
        EcCurveFp curve = EC_PARAM.getCurve();
        KeyPair keypair = Utils.generateKeyPairHex();
        EcPointFp PA = curve.decodePointHex(keypair.publicKey);

        BigInteger k = new BigInteger(keypair.privateKey, 16);
        BigInteger x1 = PA.getX().toBigInteger();

        Point ret = new Point();
        ret.setPublicKey(keypair.getPublicKey());
        ret.setPrivateKey(keypair.getPrivateKey());
        ret.setK(k);
        ret.setX1(x1);
        return ret;
    }
}
