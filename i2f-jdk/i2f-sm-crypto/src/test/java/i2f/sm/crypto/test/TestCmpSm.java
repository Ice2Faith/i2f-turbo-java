package i2f.sm.crypto.test;

import com.antherd.smcrypto.sm2.Keypair;
import i2f.sm.crypto.sm2.KeyPair;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/8/14 21:06
 * @desc
 */
public class TestCmpSm {
    public static final SecureRandom random = new SecureRandom();
    public static final boolean perf = true;
    public static String text = "Hello你好 World世界!";
    public static int perfCount = 1000;

    public static void main(String[] args) throws Exception {

        perfPreHot();

        int enCharRange = 0x7f - 0x20;
        int cnCharRange = 0x9fff - 0x4e00;
        StringBuilder builder = new StringBuilder();
        int loopCount = perf ? 1 : 300;
        for (int i = 0; i < loopCount; i++) {
            if (i > 0) {
                builder.setLength(0);
                int len = random.nextInt(128) + 4;
                for (int j = 0; j < len; j++) {
                    char ch = (char) (random.nextInt(enCharRange) + 0x20);
                    if (random.nextBoolean()) {
                        ch = (char) (random.nextInt(cnCharRange) + 0x4e00);
                    }
                    builder.append(ch);
                }
                text = builder.toString();
            }
            testSm3();

            testSm4();

            testSm2();
        }

    }

    public static void perfPreHot() throws Exception {
        if (!perf) {
            return;
        }
        System.out.println("perf pre hot ...");
        // 预热
        for (int i = 0; i < 50; i++) {
            String bs = Sm3.sm3(text);
            com.antherd.smcrypto.sm3.Sm3.sm3(text);

            String key = Sm4.generateHexKey();
            String enc = Sm4.encrypt(text, key);
            String dec = Sm4.decrypt(enc, key);

            enc = com.antherd.smcrypto.sm4.Sm4.encrypt(text, key);
            dec = com.antherd.smcrypto.sm4.Sm4.decrypt(enc, key);

            KeyPair keyPair = Sm2.generateKeyPairHex();
            com.antherd.smcrypto.sm2.Sm2.generateKeyPairHex();

            enc = Sm2.doEncrypt(text, keyPair.getPublicKey());
            dec = Sm2.doDecrypt(enc, keyPair.getPrivateKey());

            enc = com.antherd.smcrypto.sm2.Sm2.doEncrypt(text, keyPair.getPublicKey());
            dec = com.antherd.smcrypto.sm2.Sm2.doDecrypt(enc, keyPair.getPrivateKey());

            String sign = Sm2.doSignature(text, keyPair.getPrivateKey());
            Sm2.doVerifySignature(text, sign, keyPair.getPublicKey());

            sign = com.antherd.smcrypto.sm2.Sm2.doSignature(text, keyPair.getPrivateKey());
            com.antherd.smcrypto.sm2.Sm2.doVerifySignature(text, sign, keyPair.getPublicKey());
        }

        System.out.println("perf pre hot finished.");
    }

    public static void testSm3() throws Exception {
        System.out.println("\n\nTesting Sm3 ==============");
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
            for (int i = 0; i < perfCount; i++) {
                String bs = Sm3.sm3(text);
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms, " + (uts / perfCount) + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                String bs = com.antherd.smcrypto.sm3.Sm3.sm3(text);
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms, " + (cmpUts / perfCount) + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }
    }

    public static void testSm4() throws Exception {
        System.out.println("\n\nTesting Sm4 ==============");
        String key = Sm4.generateHexKey();
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
            for (int i = 0; i < perfCount; i++) {
                String e = Sm4.encrypt(text, key);
                String d = Sm4.decrypt(e, key);
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms, " + (uts / perfCount) + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                String e = com.antherd.smcrypto.sm4.Sm4.encrypt(text, key);
                String d = com.antherd.smcrypto.sm4.Sm4.decrypt(e, key);
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms, " + (cmpUts / perfCount) + "ms");

            System.out.println("speed rate:" + (cmpUts * 1.0 / uts));
        }

        String cmp2Dec = com.antherd.smcrypto.sm4.Sm4.decrypt(enc, key);
        System.out.println("cmp2Dec: " + cmp2Dec);
        assert text.equals(cmp2Dec);

        String cmp3Dec = Sm4.decrypt(cmpEnc, key);
        System.out.println("cmp3Dec: " + cmp3Dec);
        assert text.equals(cmp3Dec);


    }

    public static void testSm2() throws Exception {
        System.out.println("\n\nTesting Sm2 ==============");
        KeyPair key = Sm2.generateKeyPairHex();
        System.out.println("text: " + text);

        if (perf) {
            long bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                KeyPair k = Sm2.generateKeyPairHex();
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms, " + (uts / perfCount) + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                Keypair k = com.antherd.smcrypto.sm2.Sm2.generateKeyPairHex();
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms, " + (cmpUts / perfCount) + "ms");

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
            for (int i = 0; i < perfCount; i++) {
                String s = Sm2.doSignature(text, key.getPrivateKey());
                boolean o = Sm2.doVerifySignature(text, sign, key.getPublicKey());
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms, " + (uts / perfCount) + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                String s = com.antherd.smcrypto.sm2.Sm2.doSignature(text, key.getPrivateKey());
                boolean o = com.antherd.smcrypto.sm2.Sm2.doVerifySignature(text, s, key.getPublicKey());
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms, " + (cmpUts / perfCount) + "ms");

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
            for (int i = 0; i < perfCount; i++) {
                String e = Sm2.doEncrypt(text, key.getPublicKey());
                String d = Sm2.doDecrypt(e, key.getPrivateKey());
            }
            long ets = System.currentTimeMillis();
            long uts = ets - bts;
            System.out.println("use time: " + uts + "ms, " + (uts / perfCount) + "ms");

            bts = System.currentTimeMillis();
            for (int i = 0; i < perfCount; i++) {
                String e = com.antherd.smcrypto.sm2.Sm2.doEncrypt(text, key.getPublicKey());
                String d = com.antherd.smcrypto.sm2.Sm2.doDecrypt(e, key.getPrivateKey());
            }
            ets = System.currentTimeMillis();
            long cmpUts = ets - bts;
            System.out.println("use time: " + cmpUts + "ms, " + (cmpUts / perfCount) + "ms");

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
