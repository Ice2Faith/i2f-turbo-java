package i2f.sm.crypto.sm2;

import i2f.sm.crypto.exception.SmException;
import i2f.sm.crypto.sm2.ec.EcParam;
import i2f.sm.crypto.sm2.ec.EcPointFp;
import i2f.sm.crypto.util.CipUtils;

import java.math.BigInteger;
import java.util.Arrays;
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
}
