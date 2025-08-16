package i2f.sm.crypto.test;

import com.antherd.smcrypto.sm2.Keypair;
import i2f.sm.crypto.sm2.KeyPair;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/8/14 21:06
 * @desc
 */
public class TestCmpSm {
    public static void main(String[] args) throws Exception {

        boolean perf = false;

        if(perf){
            // 预热
            for (int i = 0; i < 5000; i++) {
                String bs = Sm3.sm3("123456");
            }
        }

        testSm3(perf);

        testSm4(perf);

        testSm2(perf);
    }

    public static void testSm3(boolean perf) throws Exception {
        System.out.println("\n\nTesting Sm3 ==============");
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);
        String s1 = Sm3.sm3(text);
        System.out.println("s1: " + s1);
        String s2 = Sm3.sm3(text);
        System.out.println("s2: " + s2);
        assert s1.equals(s2);

        System.out.println("*********************");

        String cmpS1 = com.antherd.smcrypto.sm3.Sm3.sm3(text);
        System.out.println("cmpS1: " + cmpS1);
        String cmpS2 = com.antherd.smcrypto.sm3.Sm3.sm3(text);
        System.out.println("cmpS2: " + cmpS2);
        assert cmpS1.equals(cmpS2);

        assert s1.equals(cmpS1);

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String bs = Sm3.sm3(text);
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String bs = com.antherd.smcrypto.sm3.Sm3.sm3(text);
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }
    }

    public static void testSm4(boolean perf) throws Exception {
        System.out.println("\n\nTesting Sm4 ==============");
        String key = Sm4.generateHexKey();
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);
        String enc = Sm4.encrypt(text, key);
        System.out.println("enc: " + enc);
        String dec = Sm4.decrypt(enc, key);
        System.out.println("dec: " + dec);
        assert text.equals(dec);

        System.out.println("*********************");

        String cmpEnc = com.antherd.smcrypto.sm4.Sm4.encrypt(text, key);
        System.out.println("cmpEnc: " + cmpEnc);
        String cmpDec = com.antherd.smcrypto.sm4.Sm4.decrypt(cmpEnc, key);
        System.out.println("cmpDec: " + cmpDec);
        assert text.equals(cmpDec);

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String e = Sm4.encrypt(text, key);
                String d = Sm4.decrypt(e, key);
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String e = com.antherd.smcrypto.sm4.Sm4.encrypt(text, key);
                String d = com.antherd.smcrypto.sm4.Sm4.decrypt(e, key);
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }

        String cmp2Dec = com.antherd.smcrypto.sm4.Sm4.decrypt(enc, key);
        System.out.println("cmp2Dec: " + cmp2Dec);
        assert text.equals(cmp2Dec);

        String cmp3Dec = Sm4.decrypt(cmpEnc, key);
        System.out.println("cmp3Dec: " + cmp3Dec);
        assert text.equals(cmp3Dec);


    }

    public static void testSm2(boolean perf) throws Exception {
        System.out.println("\n\nTesting Sm2 ==============");
        KeyPair key = Sm2.generateKeyPairHex();
        String text = "Hello你好 World世界!";
        System.out.println("text: " + text);

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                KeyPair k = Sm2.generateKeyPairHex();
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                Keypair k = com.antherd.smcrypto.sm2.Sm2.generateKeyPairHex();
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }

        System.out.println("========sign/verify");
        String sign = Sm2.doSignature(text, key.getPrivateKey());
        System.out.println("sign: " + sign);
        boolean ok = Sm2.doVerifySignature(text, sign, key.getPublicKey());
        System.out.println("ok: " + ok);

        System.out.println("*********************");
        String cmpSign = com.antherd.smcrypto.sm2.Sm2.doSignature(text, key.getPrivateKey());
        System.out.println("cmpSign: " + cmpSign);
        boolean cmpOk = com.antherd.smcrypto.sm2.Sm2.doVerifySignature(text, cmpSign, key.getPublicKey());
        System.out.println(cmpOk);

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String s = Sm2.doSignature(text, key.getPrivateKey());
                boolean o = Sm2.doVerifySignature(text, sign, key.getPublicKey());
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String s = com.antherd.smcrypto.sm2.Sm2.doSignature(text, key.getPrivateKey());
                boolean o = com.antherd.smcrypto.sm2.Sm2.doVerifySignature(text, s, key.getPublicKey());
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }

        boolean cmp2Ok = Sm2.doVerifySignature(text, cmpSign, key.getPublicKey());
        System.out.println("cmp2Ok: " + cmp2Ok);

        boolean cmp3Ok = com.antherd.smcrypto.sm2.Sm2.doVerifySignature(text, sign, key.getPublicKey());
        System.out.println("cmp3Ok: " + cmp3Ok);


        System.out.println("\n\n========encrypt/decrypt");
        String enc = Sm2.doEncrypt(text, key.getPublicKey());
        System.out.println("enc: " + enc);
        String dec = Sm2.doDecrypt(enc, key.getPrivateKey());
        System.out.println("dec: " + dec);
        assert text.equals(dec);
        assert Arrays.equals(text.getBytes("UTF-8"), dec.getBytes("UTF-8"));

        System.out.println("*********************");

        String cmpEnc = com.antherd.smcrypto.sm2.Sm2.doEncrypt(text, key.getPublicKey());
        System.out.println("cmpEnc: " + cmpEnc);
        String cmpDec = com.antherd.smcrypto.sm2.Sm2.doDecrypt(cmpEnc, key.getPrivateKey());
        System.out.println("cmpDec: " + cmpDec);
        assert text.equals(cmpDec);
        assert Arrays.equals(text.getBytes("UTF-8"), cmpDec.getBytes("UTF-8"));

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String e = Sm2.doEncrypt(text, key.getPublicKey());
                String d = Sm2.doDecrypt(e, key.getPrivateKey());
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String e = com.antherd.smcrypto.sm2.Sm2.doEncrypt(text, key.getPublicKey());
                String d = com.antherd.smcrypto.sm2.Sm2.doDecrypt(e, key.getPrivateKey());
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }

        String cmp3Dec = com.antherd.smcrypto.sm2.Sm2.doDecrypt(enc, key.getPrivateKey());
        System.out.println("cmp3Dec: " + cmp3Dec);
        assert text.equals(cmp3Dec);
        assert Arrays.equals(text.getBytes("UTF-8"), cmp3Dec.getBytes("UTF-8"));

        String cmp2Dec = Sm2.doDecrypt(cmpEnc, key.getPrivateKey());
        System.out.println("cmp2Dec: " + cmp2Dec);
        assert text.equals(cmp2Dec);
        assert Arrays.equals(text.getBytes("UTF-8"), cmp2Dec.getBytes("UTF-8"));


    }
}
