package i2f.sm.crypto.test;

import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;

/**
 * @author Ice2Faith
 * @date 2025/8/13 21:04
 * @desc
 */
public class TestSm {
    public static void main(String[] args) throws Exception {
        testSm3();

        testSm4();
    }

    public static void testSm3() throws Exception {
        System.out.println("Testing Sm3 ==============");
        String text = "Hello World!";
        System.out.println("text: " + text);
        String s1 = Sm3.sm3(text);
        System.out.println("s1: " + s1);
        String s2 = Sm3.sm3(text);
        System.out.println("s2: " + s2);
        assert s1.equals(s2);
    }

    public static void testSm4() throws Exception {
        System.out.println("Testing Sm4 ==============");
        String key = Sm4.generateHexKey();
        String text = "Hello World!";
        System.out.println("text: " + text);
        String enc = Sm4.encrypt(text, key);
        System.out.println("enc: " + enc);
        String dec = Sm4.decrypt(enc, key);
        System.out.println("dec: " + dec);
        assert text.equals(dec);
    }

    public static void testSm2() throws Exception {
        System.out.println("Testing Sm2 ==============");

    }
}
