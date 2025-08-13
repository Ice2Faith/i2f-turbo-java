package i2f.sm.crypto.sm2;

import i2f.bytes.ByteUtil;
import i2f.sm.crypto.util.CipUtils;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:39
 */
public class Sm3InnerCipher {


    /**
     * 二进制异或运算
     */
    public static byte[] xor(byte[] x, byte[] y) {
        byte[] result = new byte[x.length];
        for (int i = x.length - 1; i >= 0; i--) {
            result[i] = (byte) ((x[i] ^ y[i]) & 0x0ff);
        }
        return result;
    }

    /**
     * 压缩函数中的置换函数 P0(X) = X xor (X <<< 9) xor (X <<< 17)
     */
    public static int P0(int X) {
        return (X ^ CipUtils.rotl(X, 9)) ^ CipUtils.rotl(X, 17);
    }

    /**
     * 消息扩展中的置换函数 P1(X) = X xor (X <<< 15) xor (X <<< 23)
     */
    public static int P1(int X) {
        return (X ^ CipUtils.rotl(X, 15)) ^ CipUtils.rotl(X, 23);
    }

    /**
     * sm3 本体
     */
    public static byte[] sm3(byte[] array) {
        int len = array.length * 8;

        // k 是满足 len + 1 + k = 448mod512 的最小的非负整数
        int k = len % 512;
        // 如果 448 <= (512 % len) < 512，需要多补充 (len % 448) 比特'0'以满足总比特长度为512的倍数
        k = k >= 448 ? 512 - (k % 448) - 1 : 448 - k - 1;

        // 填充
        int[] kArr = new int[(k - 7) / 8];
        int[] lenArr = new int[8];
        for (int i = 0, l = kArr.length; i < l; i++) {
            kArr[i] = 0;
        }
        for (int i = 0, l = lenArr.length; i < l; i++) {
            lenArr[i] = 0;
        }
        String sLen = Integer.toString(len, 2);
        for (int i = 7; i >= 0; i--) {
            if (sLen.length() > 8) {
                int start = sLen.length() - 8;
                lenArr[i] = Integer.parseInt(sLen.substring(start), 2);
                sLen = sLen.substring(0, start);
            } else if (sLen.length() > 0) {
                lenArr[i] = Integer.parseInt(sLen, 2);
                sLen = "";
            }
        }

        // const m = new Uint8Array([...array, 0x80, ...kArr, ...lenArr]);
        byte[] m = new byte[array.length + 1 + kArr.length + lenArr.length];
        if (m.length > 0) {
            int j = 0;
            for (int i = 0; i < array.length; i++) {
                m[j++] = array[i];
            }
            m[j++] = (byte) 0x80;
            for (int i = 0; i < kArr.length; i++) {
                m[j++] = (byte) kArr[i];
            }
            for (int i = 0; i < lenArr.length; i++) {
                m[j++] = (byte) lenArr[i];
            }
        }

        byte[] dataView = m;

        // 消息扩展
        int[] W = new int[68];
        int[] M = new int[64]; // W'

        // 迭代压缩
        int n = m.length / 64;
        int[] V = {0x7380166f, 0x4914b2b9, 0x172442d7, 0xda8a0600, 0xa96f30bc, 0x163138aa, 0xe38dee4d, 0xb0fb0e4e};
        for (int i = 0; i < n; i++) {

            // 将消息分组B划分为 16 个字 W0， W1，……，W15
            int start = 16 * i;
            for (int j = 0; j < 16; j++) {
                W[j] = (int) ByteUtil.ofBigEndian(dataView, (start + j) * 4, 4);
            }

            // W16 ～ W67：W[j] <- P1(W[j−16] xor W[j−9] xor (W[j−3] <<< 15)) xor (W[j−13] <<< 7) xor W[j−6]
            for (int j = 16; j < 68; j++) {
                W[j] = (P1((W[j - 16] ^ W[j - 9]) ^ CipUtils.rotl(W[j - 3], 15)) ^ CipUtils.rotl(W[j - 13], 7)) ^ W[j - 6];
            }

            // W′0 ～ W′63：W′[j] = W[j] xor W[j+4]
            for (int j = 0; j < 64; j++) {
                M[j] = W[j] ^ W[j + 4];
            }

            // 压缩
            int T1 = 0x79cc4519;
            int T2 = 0x7a879d8a;
            // 字寄存器
            int A = V[0];
            int B = V[1];
            int C = V[2];
            int D = V[3];
            int E = V[4];
            int F = V[5];
            int G = V[6];
            int H = V[7];
            // 中间变量
            int SS1 = 0;
            int SS2 = 0;
            int TT1 = 0;
            int TT2 = 0;
            int T = 0;
            for (int j = 0; j < 64; j++) {
                T = j >= 0 && j <= 15 ? T1 : T2;
                SS1 = CipUtils.rotl(CipUtils.rotl(A, 12) + E + CipUtils.rotl(T, j), 7);
                SS2 = SS1 ^ CipUtils.rotl(A, 12);

                TT1 = (j >= 0 && j <= 15 ? ((A ^ B) ^ C) : (((A & B) | (A & C)) | (B & C))) + D + SS2 + M[j];
                TT2 = (j >= 0 && j <= 15 ? ((E ^ F) ^ G) : ((E & F) | ((~E) & G))) + H + SS1 + W[j];

                D = C;
                C = CipUtils.rotl(B, 9);
                B = A;
                A = TT1;
                H = G;
                G = CipUtils.rotl(F, 19);
                F = E;
                E = P0(TT2);
            }

            V[0] ^= A;
            V[1] ^= B;
            V[2] ^= C;
            V[3] ^= D;
            V[4] ^= E;
            V[5] ^= F;
            V[6] ^= G;
            V[7] ^= H;
        }

        // 转回 uint8
        byte[] result = new byte[V.length * 4];
        if (result.length > 0) {
            int j = 0;
            for (int i = 0; i < V.length; i++) {
                int word = V[i];
                result[j++] = (byte) ((word >>> 24) & 0x0ff);
                result[j++] = (byte) ((word >>> 16) & 0x0ff);
                result[j++] = (byte) ((word >>> 8) & 0x0ff);
                result[j++] = (byte) (word & 0x0ff);
            }
        }

        return result;
    }

    public static final int blockLen = 64;
    protected static final byte[] iPad = new byte[blockLen];
    protected static final byte[] oPad = new byte[blockLen];

    static {
        for (int i = 0; i < blockLen; i++) {
            iPad[i] = 0x36;
            oPad[i] = 0x5c;
        }
    }

    /**
     * hmac 实现
     */
    public static byte[] hmac(byte[] input, byte[] key) {
// 密钥填充
        if (key.length > blockLen) {
            key = sm3(key);
        }
        // while (key.length < blockLen) key.push(0)
        int diffLen = blockLen - key.length;
        if (diffLen > 0) {
            byte[] buff = new byte[key.length + diffLen];
            System.arraycopy(key, 0, buff, 0, key.length);
            key = buff;
        }

        byte[] iPadKey = xor(key, iPad);
        byte[] oPadKey = xor(key, oPad);

        byte[] arr = new byte[iPadKey.length + input.length];
        System.arraycopy(iPadKey, 0, arr, 0, iPadKey.length);
        System.arraycopy(input, 0, arr, iPadKey.length, input.length);
        byte[] hash = sm3(arr);

        arr = new byte[oPadKey.length + hash.length];
        System.arraycopy(oPadKey, 0, arr, 0, oPadKey.length);
        System.arraycopy(hash, 0, arr, oPadKey.length, hash.length);
        return sm3(arr);
    }
}
