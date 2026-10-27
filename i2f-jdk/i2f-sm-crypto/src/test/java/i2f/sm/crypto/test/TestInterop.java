package i2f.sm.crypto.test;

import i2f.sm.crypto.sm2.KeyPair;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm2.Sm2Cipher;
import i2f.sm.crypto.sm2.Utils;
import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;
import i2f.sm.crypto.sm4.Sm4Cipher;
import i2f.sm.crypto.util.CipUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigInteger;

/**
 * 与原始 sm-crypto js（直接跑 jar 内置的 js）的互操作测试
 */
public class TestInterop {
    public static String text = "Hello你好 World世界!";
    public static ScriptEngine engine;
    public static String key = "0123456789abcdeffedcba9876543210";
    public static String iv = "000102030405060708090a0b0c0d0e0f";

    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
        // sm-crypto jar 内置的 js（相对于 jar 根目录）
        engine.eval(new java.io.InputStreamReader(TestInterop.class.getResourceAsStream("/sm2.js"), "UTF-8"));
        engine.eval(new java.io.InputStreamReader(TestInterop.class.getResourceAsStream("/sm3.js"), "UTF-8"));
        engine.eval(new java.io.InputStreamReader(TestInterop.class.getResourceAsStream("/sm4.js"), "UTF-8"));

        testSm3();
        testSm4();
        testHexUtils();
        testSm2Key();
        testSm2Encrypt();
        testSm2Sign();
        testNoKReuse();
    }

    /**
     * HexUtils 的边界行为需要与原来委托 i2f-codec 时保持一致
     */
    public static void testHexUtils() {
        System.out.println("\n=== Hex 编解码（CipUtils -> HexUtils） ===");
        cmp("hexToArray empty", java.util.Arrays.toString(CipUtils.hexToArray("")), "[]");
        cmp("hexToArray odd(丢弃末位)", java.util.Arrays.toString(CipUtils.hexToArray("abc")), "[-85]");
        cmp("hexToArray upper", java.util.Arrays.toString(CipUtils.hexToArray("AB")), "[-85]");
        cmp("hexToArray bad->0", java.util.Arrays.toString(CipUtils.hexToArray("zz")), "[0]");
        cmp("hexToArray space->0", java.util.Arrays.toString(CipUtils.hexToArray(" 12")), "[0]");
        cmp("hexToArray sign", java.util.Arrays.toString(CipUtils.hexToArray("-1")), "[-1]");
        cmp("hexToArray roundtrip", CipUtils.arrayToHex(CipUtils.hexToArray(text2Hex())), text2Hex());
        cmp("arrayToHex ff000f", CipUtils.arrayToHex(new byte[]{-1, 0, 15}), "ff000f");
        cmp("arrayToHex empty", CipUtils.arrayToHex(new byte[0]), "");
        boolean npe = false;
        try {
            CipUtils.hexToArray(null);
        } catch (NullPointerException e) {
            npe = true;
        }
        cmp("hexToArray null -> NPE", npe, true);
    }

    public static String text2Hex() {
        return "0123456789abcdeffedcba987654321000112233445566778899aabbccddeeff";
    }

    public static Object js(String script) throws Exception {
        return engine.eval(script);
    }

    public static void testSm3() throws Exception {
        System.out.println("\n=== SM3 ===");
        cmp("sm3 text", Sm3.sm3(text), js("sm3('" + text + "')"));
        cmp("sm3 ascii", Sm3.sm3("abc"), js("sm3('abc')"));
        String longText = "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd";
        cmp("sm3 512bit", Sm3.sm3(longText), js("sm3('" + longText + "')"));
    }

    public static void testSm4() throws Exception {
        System.out.println("\n=== SM4 ===");
        String je = Sm4.encrypt(text, key);
        String se = (String) js("sm4.encrypt('" + text + "', '" + key + "')");
        cmp("sm4 ecb enc", je, se);
        cmp("sm4 ecb dec(js enc)", Sm4.decrypt(se, key), js("sm4.decrypt('" + se + "', '" + key + "')"));
        cmp("sm4 ecb dec(java enc)", Sm4.decrypt(je, key), text);

        String jce = Sm4.encrypt(text, key, null, Sm4Cipher.Mode.CBC, iv);
        String sce = (String) js("sm4.encrypt('" + text + "', '" + key + "', {mode:'cbc', iv:'" + iv + "'})");
        cmp("sm4 cbc enc", jce, sce);
        cmp("sm4 cbc dec(js enc)", Sm4.decrypt(sce, key, null, Sm4Cipher.Mode.CBC, iv), text);

        String jp7 = Sm4.encrypt(text, key, Sm4Cipher.Padding.PKCS_7, Sm4Cipher.Mode.ECB, null);
        String sp5 = (String) js("sm4.encrypt('" + text + "', '" + key + "', {padding:'pkcs#5'})");
        cmp("sm4 pkcs7 vs js pkcs5", jp7, sp5);
    }

    public static void testSm2Key() throws Exception {
        System.out.println("\n=== SM2 KEY ===");
        KeyPair pair = Utils.generateKeyPairHex();
        cmp("generateKeyPairHex 公钥可由私钥推导", Sm2.getPublicKeyFromPrivateKey(pair.getPrivateKey()), pair.getPublicKey());
        cmp("verifyPublicKey", String.valueOf(Sm2.verifyPublicKey(pair.getPublicKey())), String.valueOf(true));
    }

    public static void testSm2Encrypt() throws Exception {
        System.out.println("\n=== SM2 ENCRYPT ===");
        KeyPair pair = Utils.generateKeyPairHex();
        String pub = pair.getPublicKey();
        String pri = pair.getPrivateKey();

        String j1 = Sm2.doEncrypt(text, pub);
        cmp("java C1C3C2 -> js decrypt", js("doDecrypt('" + j1 + "', '" + pri + "')"), text);
        String j2 = Sm2.doEncrypt(text, pub, Sm2Cipher.CipherMode.C1C2C3);
        cmp("java C1C2C3 -> js decrypt", js("doDecrypt('" + j2 + "', '" + pri + "', 0)"), text);

        String s1 = (String) js("doEncrypt('" + text + "', '" + pub + "')");
        cmp("js C1C3C2 -> java decrypt", Sm2.doDecrypt(s1, pri), text);
        String s2 = (String) js("doEncrypt('" + text + "', '" + pub + "', 0)");
        cmp("js C1C2C3 -> java decrypt", Sm2.doDecrypt(s2, pri, Sm2Cipher.CipherMode.C1C2C3), text);

        // 空消息与中文、长消息
        String longText = buildLongText();
        String j3 = Sm2.doEncrypt(longText, pub);
        cmp("java long -> js decrypt", js("doDecrypt('" + j3 + "', '" + pri + "')"), longText);
        String s3 = (String) js("doEncrypt('" + longText + "', '" + pub + "')");
        cmp("js long -> java decrypt", Sm2.doDecrypt(s3, pri), longText);
    }

    public static void testSm2Sign() throws Exception {
        System.out.println("\n=== SM2 SIGN ===");
        KeyPair pair = Utils.generateKeyPairHex();
        String pub = pair.getPublicKey();
        String pri = pair.getPrivateKey();

        // raw
        String jr = Sm2.doSignature(text, pri);
        cmp("js verify java raw sign", js("doVerifySignature('" + text + "', '" + jr + "', '" + pub + "')"), Boolean.TRUE);
        String sr = (String) js("doSignature('" + text + "', '" + pri + "')");
        cmp("java verify js raw sign", Sm2.doVerifySignature(text, sr, pub), true);

        // der
        String jd = Sm2.doSignature(text, pri, null, true, false, pub, null);
        cmp("js verify java der sign", js("doVerifySignature('" + text + "', '" + jd + "', '" + pub + "', {der:true})"), Boolean.TRUE);
        String sd = (String) js("doSignature('" + text + "', '" + pri + "', {der:true})");
        cmp("java verify js der sign", Sm2.doVerifySignature(text, sd, pub, true, false, null), true);

        // hash（Z 值）
        String jh = Sm2.doSignature(text, pri, null, false, true, pub, null);
        cmp("js verify java hash sign", js("doVerifySignature('" + text + "', '" + jh + "', '" + pub + "', {hash:true})"), Boolean.TRUE);
        String sh = (String) js("doSignature('" + text + "', '" + pri + "', {hash:true})");
        cmp("java verify js hash sign", Sm2.doVerifySignature(text, sh, pub, false, true, null), true);
        cmp("js verify js hash sign(sanity)", js("doVerifySignature('" + text + "', '" + sh + "', '" + pub + "', {hash:true})"), Boolean.TRUE);

        // 空消息 hash
        String jh2 = Sm2.doSignature("", pri, null, false, true, pub, null);
        cmp("js verify java hash sign(empty)", js("doVerifySignature('', '" + jh2 + "', '" + pub + "', {hash:true})"), Boolean.TRUE);

        // 自定义 userId
        String jh3 = Sm2.doSignature(text, pri, null, false, true, pub, "test-user");
        cmp("js verify java hash sign(userId)", js("doVerifySignature('" + text + "', '" + jh3 + "', '" + pub + "', {hash:true, userId:'test-user'})"), Boolean.TRUE);
        String sh3 = (String) js("doSignature('" + text + "', '" + pri + "', {hash:true, userId:'test-user'})");
        cmp("java verify js hash sign(userId)", Sm2.doVerifySignature(text, sh3, pub, false, true, "test-user"), true);

        // 多字节 userId
        String jh4 = Sm2.doSignature(text, pri, null, false, true, pub, "中文用户");
        cmp("js verify java hash sign(cn userId)", js("doVerifySignature('" + text + "', '" + jh4 + "', '" + pub + "', {hash:true, userId:'中文用户'})"), Boolean.TRUE);

        // 空消息 raw
        try {
            String jr0 = Sm2.doSignature("", pri);
            cmp("js verify java raw sign(empty)", js("doVerifySignature('', '" + jr0 + "', '" + pub + "')"), Boolean.TRUE);
            String sr0 = (String) js("doSignature('', '" + pri + "')");
            cmp("java verify js raw sign(empty)", Sm2.doVerifySignature("", sr0, pub), true);
        } catch (Exception e) {
            System.out.println("[FAIL] java raw sign(empty) 抛异常: " + e);
        }

        // 字节数组入参不应被修改
        byte[] plain = text.getBytes("UTF-8");
        byte[] copy = plain.clone();
        Sm2.doEncrypt(plain, pub);
        cmp("doEncrypt(byte[]) 不修改入参", java.util.Arrays.equals(plain, copy), true);
    }

    public static void testNoKReuse() throws Exception {
        System.out.println("\n=== K 唯一性（与 js 每次 generateKeyPairHex 行为一致） ===");
        KeyPair pair = Utils.generateKeyPairHex();
        String pub = pair.getPublicKey();
        String pri = pair.getPrivateKey();

        String e1 = Sm2.doEncrypt("a", pub);
        String e2 = Sm2.doEncrypt("a", pub);
        cmp("两次加密 C1 不同", e1.substring(0, 128).equals(e2.substring(0, 128)), false);
        cmp("两次加密密文不同", e1.equals(e2), false);

        String s1 = Sm2.doSignature("a", pri);
        String s2 = Sm2.doSignature("b", pri);
        cmp("两次签名 r 不同", s1.substring(0, 64).equals(s2.substring(0, 64)), false);

        // k 复用时可以用两条签名反推私钥，这里必须推不出来
        BigInteger n = Utils.EC_PARAM.getN();
        BigInteger r1 = new BigInteger(s1.substring(0, 64), 16);
        BigInteger t1 = new BigInteger(s1.substring(64), 16);
        BigInteger r2 = new BigInteger(s2.substring(0, 64), 16);
        BigInteger t2 = new BigInteger(s2.substring(64), 16);
        boolean leaked;
        try {
            BigInteger recovered = t1.subtract(t2)
                    .multiply(r2.subtract(r1).subtract(t1).add(t2).modInverse(n))
                    .mod(n);
            leaked = recovered.equals(new BigInteger(pri, 16));
        } catch (ArithmeticException ex) {
            leaked = false;
        }
        cmp("两条签名无法反推私钥", leaked, false);

        // 并发下同样正确（不能共用同一个 k）
        final KeyPair fp = pair;
        final java.util.List<String> errs = java.util.Collections.synchronizedList(new java.util.ArrayList<String>());
        Thread[] threads = new Thread[4];
        for (int t = 0; t < threads.length; t++) {
            final int idx = t;
            threads[t] = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    String msg = "thread-" + idx + "-" + i;
                    try {
                        String sign = Sm2.doSignature(msg, fp.getPrivateKey());
                        if (!Sm2.doVerifySignature(msg, sign, fp.getPublicKey())) {
                            errs.add("verify fail: " + msg);
                        }
                        String enc = Sm2.doEncrypt(msg, fp.getPublicKey());
                        if (!msg.equals(Sm2.doDecrypt(enc, fp.getPrivateKey()))) {
                            errs.add("roundtrip fail: " + msg);
                        }
                    } catch (Throwable ex) {
                        errs.add(String.valueOf(ex));
                    }
                }
            });
            threads[t].start();
        }
        for (Thread th : threads) {
            th.join();
        }
        cmp("并发签名/加解密", errs.isEmpty(), true);
        if (!errs.isEmpty()) {
            System.out.println("       " + errs.get(0));
        }
    }

    public static String buildLongText() throws Exception {
        // 构造 4 字节以上、跨 64 字节分组的消息
        String s = "";
        for (int i = 0; i < 1000; i++) {
            s += (char) ('a' + (i % 26));
        }
        return s;
    }

    public static void cmp(String name, Object javaRet, Object jsRet) {
        boolean same = javaRet == null ? jsRet == null : javaRet.equals(jsRet);
        System.out.println((same ? "[OK  ] " : "[FAIL] ") + name);
        if (!same) {
            System.out.println("       java: " + javaRet);
            System.out.println("       js  : " + jsRet);
        }
    }
}
