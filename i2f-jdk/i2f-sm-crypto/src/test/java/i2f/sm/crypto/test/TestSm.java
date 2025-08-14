package i2f.sm.crypto.test;

import i2f.sm.crypto.sm2.KeyPair;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm2.Utils;
import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/8/13 21:04
 * @desc
 */
public class TestSm {
    public static void main(String[] args) throws Exception {

        testSm3();

        testSm4();

        testSm2();
    }

    public static void testSm3() throws Exception {
        System.out.println("\n\nTesting Sm3 ==============");
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);
        String s1 = Sm3.sm3(text);
        System.out.println("s1: " + s1);
        String s2 = Sm3.sm3(text);
        System.out.println("s2: " + s2);
        assert s1.equals(s2);

    }

    public static void testSm4() throws Exception {
        System.out.println("\n\nTesting Sm4 ==============");
        String key = Sm4.generateHexKey();
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);
        String enc = Sm4.encrypt(text, key);
        System.out.println("enc: " + enc);
        String dec = Sm4.decrypt(enc, key);
        System.out.println("dec: " + dec);
        assert text.equals(dec);
    }

    public static void testSm2() throws Exception {
        System.out.println("\n\nTesting Sm2 ==============");
        KeyPair key = Utils.generateKeyPairHex();
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);
        System.out.println("========sign/verify");
        String sign = Sm2.doSignature(text, key.getPrivateKey());
        System.out.println("sign: " + sign);
        boolean ok = Sm2.doVerifySignature(text, sign, key.getPublicKey());
        System.out.println("ok: " + ok);


        System.out.println("\n\n========encrypt/decrypt");
        String enc = Sm2.doEncrypt(text, key.getPublicKey());
        System.out.println("enc: " + enc);
        String dec = Sm2.doDecrypt(enc, key.getPrivateKey());
        System.out.println("dec: " + dec);
        assert text.equals(dec);
        assert Arrays.equals(text.getBytes("UTF-8"), dec.getBytes("UTF-8"));

    }
}
