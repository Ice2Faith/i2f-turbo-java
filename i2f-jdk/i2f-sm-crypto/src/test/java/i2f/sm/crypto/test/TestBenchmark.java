package i2f.sm.crypto.test;

import i2f.sm.crypto.sm2.KeyPair;
import i2f.sm.crypto.sm2.Sm2;
import i2f.sm.crypto.sm2.Sm2Cipher;
import i2f.sm.crypto.sm2.Utils;
import i2f.sm.crypto.sm3.Sm3;
import i2f.sm.crypto.sm4.Sm4;
import i2f.sm.crypto.util.CipUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 性能对比：纯 Java 实现  vs  原始 sm-crypto 的 Nashorn(JS) 实现
 * <p>
 * Nashorn 侧使用 com.antherd:sm-crypto 的 Java 包装，内部是静态缓存的 Invocable，
 * 与项目原先经由 nashorn 调用 js 的方式一致；SM3/SM4/SM2 各自一个 ScriptEngine，只在类初始化时 eval 一次脚本。
 * <p>
 * 计时方式：
 * 1. 先跑 {@link #WARMUP_ROUNDS} 轮预热，整轮丢弃，不参与比较（JIT 分层编译、内联、脚本引擎自身都要预热）；
 * 2. 再跑 {@link #MEASURE_ROUNDS} 轮，每轮在所有用例间轮转，每项取各轮结果的中位数，并给出波动范围；
 * 3. 单轮内部在固定时间窗口内累计完成次数，避免固定迭代次数受机器负载影响。
 * <p>
 * 注意：SM2 的加密/签名在长时间压测下会把点池取空（后台按 k*G 速度补池），
 * 所以这里反映的是"持续吞吐"（等价每次现算 k*G），点池命中时的收益见文末单独对比。
 * <p>
 * 运行前准备依赖：mvn -o dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=test
 * 运行：java -cp "target/classes;target/test-classes;target/lib/*" i2f.sm.crypto.test.TestBenchmark
 */
public class TestBenchmark {
    public static final String TEXT = "Hello你好 World世界!";
    public static final String KEY = "0123456789abcdeffedcba9876543210";
    public static final String IV = "000102030405060708090a0b0c0d0e0f";

    /**
     * 预热轮数，不计入结果
     */
    public static final int WARMUP_ROUNDS = 5;
    /**
     * 测量轮数，结果取中位数
     */
    public static final int MEASURE_ROUNDS = 10;
    /**
     * 单轮计时窗口下限（毫秒），慢操作按单次耗时自动放大
     */
    public static final long TARGET_MS = 300;
    /**
     * 轮与轮之间的停顿，让 JIT 编译线程与 GC 收敛
     */
    public static final long ROUND_GAP_MS = 200;

    /**
     * 避免结果被 JIT 当作无用代码消除
     */
    static long sink = 0;

    interface Task {
        Object run() throws Exception;
    }

    static class Case {
        String name;
        Task javaTask;
        Task jsTask;
        List<Double> javaSamples = new ArrayList<>();
        List<Double> jsSamples = new ArrayList<>();

        Case(String name, Task javaTask, Task jsTask) {
            this.name = name;
            this.javaTask = javaTask;
            this.jsTask = jsTask;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("java.version : " + System.getProperty("java.version"));
        System.out.println("os/arch      : " + System.getProperty("os.name") + "/" + System.getProperty("os.arch"));
        System.out.println("cpu cores    : " + Runtime.getRuntime().availableProcessors());
        System.out.println("nashorn      : " + new javax.script.ScriptEngineManager().getEngineByName("nashorn").getClass().getName());
        System.out.println("轮次          : 预热 " + WARMUP_ROUNDS + " 轮(丢弃) + 测量 " + MEASURE_ROUNDS + " 轮(取中位数)");

        // 双方使用同一对密钥，排除密钥差异
        final KeyPair pair = Utils.generateKeyPairHex();
        final String pub = pair.getPublicKey();
        final String pri = pair.getPrivateKey();

        final String msg22 = TEXT;
        final String msg200 = repeat("0123456789abcdef", 12) + "abcdefgh";
        final String msg1k = repeat("0123456789abcdef", 64);

        // 各自用自己的密文/签名做解密、验签，保证工作量一致
        final String jSm4Enc = Sm4.encrypt(msg22, KEY);
        final String jsSm4Enc = com.antherd.smcrypto.sm4.Sm4.encrypt(msg22, KEY);
        final String jSm4Enc1k = Sm4.encrypt(msg1k, KEY);
        final String jsSm4Enc1k = com.antherd.smcrypto.sm4.Sm4.encrypt(msg1k, KEY);

        final String jSm2Enc = Sm2.doEncrypt(msg22, pub);
        final String jsSm2Enc = com.antherd.smcrypto.sm2.Sm2.doEncrypt(msg22, pub);
        final String jSm2Enc200 = Sm2.doEncrypt(msg200, pub);
        final String jsSm2Enc200 = com.antherd.smcrypto.sm2.Sm2.doEncrypt(msg200, pub);

        final String jSign = Sm2.doSignature(msg22, pri);
        final String jsSign = com.antherd.smcrypto.sm2.Sm2.doSignature(msg22, pri);
        final String jSignHash = Sm2.doSignature(msg22, pri, null, false, true, pub, null);
        final String jsSignHash = com.antherd.smcrypto.sm2.Sm2.doSignature(msg22, pri,
                new com.antherd.smcrypto.sm2.SignatureOptions(null, false, true, pub, null));

        List<Case> compareCases = new ArrayList<>();
        compareCases.add(new Case("SM3 22B",
                () -> Sm3.sm3(msg22),
                () -> com.antherd.smcrypto.sm3.Sm3.sm3(msg22)));
        compareCases.add(new Case("SM3 1KB",
                () -> Sm3.sm3(msg1k),
                () -> com.antherd.smcrypto.sm3.Sm3.sm3(msg1k)));
        compareCases.add(new Case("SM4-ECB enc 22B",
                () -> Sm4.encrypt(msg22, KEY),
                () -> com.antherd.smcrypto.sm4.Sm4.encrypt(msg22, KEY)));
        compareCases.add(new Case("SM4-ECB dec 22B",
                () -> Sm4.decrypt(jSm4Enc, KEY),
                () -> com.antherd.smcrypto.sm4.Sm4.decrypt(jsSm4Enc, KEY)));
        compareCases.add(new Case("SM4-ECB enc 1KB",
                () -> Sm4.encrypt(msg1k, KEY),
                () -> com.antherd.smcrypto.sm4.Sm4.encrypt(msg1k, KEY)));
        compareCases.add(new Case("SM4-ECB dec 1KB",
                () -> Sm4.decrypt(jSm4Enc1k, KEY),
                () -> com.antherd.smcrypto.sm4.Sm4.decrypt(jsSm4Enc1k, KEY)));
        compareCases.add(new Case("SM2 generateKeyPair",
                () -> Utils.generateKeyPairHex(),
                () -> com.antherd.smcrypto.sm2.Sm2.generateKeyPairHex()));
        compareCases.add(new Case("SM2 enc 22B",
                () -> Sm2.doEncrypt(msg22, pub),
                () -> com.antherd.smcrypto.sm2.Sm2.doEncrypt(msg22, pub)));
        compareCases.add(new Case("SM2 dec 22B",
                () -> Sm2.doDecrypt(jSm2Enc, pri),
                () -> com.antherd.smcrypto.sm2.Sm2.doDecrypt(jsSm2Enc, pri)));
        compareCases.add(new Case("SM2 enc 200B",
                () -> Sm2.doEncrypt(msg200, pub),
                () -> com.antherd.smcrypto.sm2.Sm2.doEncrypt(msg200, pub)));
        compareCases.add(new Case("SM2 dec 200B",
                () -> Sm2.doDecrypt(jSm2Enc200, pri),
                () -> com.antherd.smcrypto.sm2.Sm2.doDecrypt(jsSm2Enc200, pri)));
        compareCases.add(new Case("SM2 sign(raw)",
                () -> Sm2.doSignature(msg22, pri),
                () -> com.antherd.smcrypto.sm2.Sm2.doSignature(msg22, pri)));
        compareCases.add(new Case("SM2 sign(hash)",
                () -> Sm2.doSignature(msg22, pri, null, false, true, pub, null),
                () -> com.antherd.smcrypto.sm2.Sm2.doSignature(msg22, pri,
                        new com.antherd.smcrypto.sm2.SignatureOptions(null, false, true, pub, null))));
        compareCases.add(new Case("SM2 verify",
                () -> Sm2.doVerifySignature(msg22, jSign, pub),
                () -> com.antherd.smcrypto.sm2.Sm2.doVerifySignature(msg22, jsSign, pub)));

        // Java 侧开销分解：算法本身 vs 十六进制/字符串编解码
        final byte[] b22 = msg22.getBytes("UTF-8");
        final byte[] b1k = msg1k.getBytes("UTF-8");
        final byte[] keyBytes = CipUtils.hexToArray(KEY);
        final String hex1k = CipUtils.arrayToHex(b1k);

        final byte[][] sm4Raw1k = {Sm4.encrypt(b1k, keyBytes)};

        List<Case> javaOnlyCases = new ArrayList<>();
        javaOnlyCases.add(new Case("SM3 算法 22B (byte[])", () -> Sm3.sm3(b22), null));
        javaOnlyCases.add(new Case("SM3 算法 1KB (byte[])", () -> Sm3.sm3(b1k), null));
        javaOnlyCases.add(new Case("SM4 算法 enc 1KB (byte[])", () -> Sm4.encrypt(b1k, keyBytes), null));
        javaOnlyCases.add(new Case("SM4 算法 dec 1KB (byte[])", () -> Sm4.decrypt(sm4Raw1k[0], keyBytes), null));
        javaOnlyCases.add(new Case("hex arrayToHex 32B", () -> CipUtils.arrayToHex(b22), null));
        javaOnlyCases.add(new Case("hex arrayToHex 1KB", () -> CipUtils.arrayToHex(b1k), null));
        javaOnlyCases.add(new Case("hex hexToArray 2KB", () -> CipUtils.hexToArray(hex1k), null));
        javaOnlyCases.add(new Case("utf8 arrayToUtf8 1KB", () -> new String(b1k, "UTF-8"), null));

        List<Case> allCases = new ArrayList<>(compareCases);
        allCases.addAll(javaOnlyCases);

        // 预热轮：整轮丢弃
        for (int round = 0; round < WARMUP_ROUNDS; round++) {
            runRound(allCases, true);
            Thread.sleep(ROUND_GAP_MS);
        }
        // 测量轮：逐轮收集
        for (int round = 0; round < MEASURE_ROUNDS; round++) {
            runRound(allCases, false);
            Thread.sleep(ROUND_GAP_MS);
        }

        System.out.println();
        System.out.printf("%-24s %13s %17s %15s %17s %11s%n",
                "操作", "Java(us/op)", "波动", "Nashorn(us/op)", "波动", "倍数");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Case c : compareCases) {
            System.out.printf("%-24s %13.2f %17s %15.2f %17s %10.1fx%n",
                    c.name, median(c.javaSamples), range(c.javaSamples),
                    median(c.jsSamples), range(c.jsSamples),
                    median(c.jsSamples) / median(c.javaSamples));
        }
        System.out.println("--------------------------------------------------------------------------------------------------");

        System.out.println();
        System.out.println("=== Java 侧开销分解（算法 vs 编解码） ===");
        System.out.printf("%-24s %13s %17s%n", "操作", "Java(us/op)", "波动");
        System.out.println("-------------------------------------------------------");
        for (Case c : javaOnlyCases) {
            System.out.printf("%-24s %13.2f %17s%n", c.name, median(c.javaSamples), range(c.javaSamples));
        }

        // SM2 里 k*G 的收益：点池取点 vs 同步生成（取点会消耗池，次数控制在池容量以内）
        List<Double> poolSamples = new ArrayList<>();
        List<Double> genSamples = new ArrayList<>();
        for (int round = 0; round < 5; round++) {
            Thread.sleep(500); // 等后台把点池补满
            poolSamples.add(measureN(() -> Sm2Cipher.getPoint(), 5, 20));
            genSamples.add(measureN(() -> Utils.generateKeyPairHex(), 5, 20));
        }
        System.out.println();
        System.out.println("=== Java 侧 k*G 开销 ===");
        System.out.printf("%-28s %10.2f us/op  (%s)%n", "Sm2Cipher.getPoint() 池中取点", median(poolSamples), range(poolSamples));
        System.out.printf("%-28s %10.2f us/op  (%s)%n", "Utils.generateKeyPairHex() 同步", median(genSamples), range(genSamples));

        System.out.println();
        System.out.println("sink=" + sink);
    }

    public static void runRound(List<Case> cases, boolean warmup) throws Exception {
        for (Case c : cases) {
            double javaUs = measureOnce(c.javaTask);
            if (!warmup) {
                c.javaSamples.add(javaUs);
            }
            if (c.jsTask != null) {
                double jsUs = measureOnce(c.jsTask);
                if (!warmup) {
                    c.jsSamples.add(jsUs);
                }
            }
        }
    }

    /**
     * 单次测量：按单次耗时决定窗口长度，在窗口内连续执行并统计平均值
     */
    public static double measureOnce(Task task) throws Exception {
        long s0 = System.nanoTime();
        consume(task.run());
        long oneMs = Math.max(1, (System.nanoTime() - s0) / 1_000_000L);
        // 保证至少 5 次样本，慢操作（js 的 SM2 单次百毫秒级）自动放大窗口
        long windowMs = Math.max(TARGET_MS, Math.min(2000, oneMs * 5));

        long start = System.nanoTime();
        long limit = start + windowMs * 1_000_000L;
        long count = 0;
        do {
            consume(task.run());
            count++;
        } while (System.nanoTime() < limit);
        return (System.nanoTime() - start) / 1000.0 / count;
    }

    /**
     * 固定次数测量，用于会消耗点池、不能长时间循环的操作
     */
    public static double measureN(Task task, int warmup, int times) throws Exception {
        for (int i = 0; i < warmup; i++) {
            consume(task.run());
        }
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            consume(task.run());
        }
        return (System.nanoTime() - start) / 1000.0 / times;
    }

    public static double median(List<Double> samples) {
        List<Double> copy = new ArrayList<>(samples);
        Collections.sort(copy);
        int size = copy.size();
        if (size == 0) {
            return 0;
        }
        return (size & 1) == 1 ? copy.get(size / 2) : (copy.get(size / 2 - 1) + copy.get(size / 2)) / 2;
    }

    public static String range(List<Double> samples) {
        if (samples.isEmpty()) {
            return "-";
        }
        List<Double> copy = new ArrayList<>(samples);
        Collections.sort(copy);
        return String.format("%.2f~%.2f", copy.get(0), copy.get(copy.size() - 1));
    }

    static void consume(Object val) {
        if (val instanceof String) {
            sink += ((String) val).length();
        } else if (val instanceof Boolean) {
            sink += ((Boolean) val) ? 1 : 2;
        } else if (val != null) {
            sink += 1;
        }
    }

    public static String repeat(String s, int times) {
        StringBuilder builder = new StringBuilder(s.length() * times);
        for (int i = 0; i < times; i++) {
            builder.append(s);
        }
        return builder.toString();
    }
}
