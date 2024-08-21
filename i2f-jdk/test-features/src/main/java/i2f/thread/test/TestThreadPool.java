package i2f.thread.test;

import i2f.thread.DynamicSharedThreadPool;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/4/12 17:12
 * @desc
 */
public class TestThreadPool {
    public static void main(String[] args) throws InterruptedException {

        DynamicSharedThreadPool pool = new DynamicSharedThreadPool();

        SecureRandom random = new SecureRandom();

        int count = 5000;
        int[] sleepArr = {0, 0, 0, 30, 3000, 10, 300, 8000, 1000, 90, 10000, 2000, 200, 5000, 3, 2, 1, 1, 2, 3, 100, 200, 300, 400, 500, 400, 300, 200, 100, 50, 30, 10, 5, 2, 1, 1};
        long[] execArr = new long[sleepArr.length];
        for (int t = 0; t < sleepArr.length; t++) {

            boolean isFirst = t == 0;
            long tbs = System.currentTimeMillis();
            int currSleepTs = sleepArr[t];
            CountDownLatch latch = new CountDownLatch(count);
            System.out.println("sleep:" + currSleepTs);
            new Thread(() -> {
                try {
                    long sleepTs = isFirst ? 8000 : 0;
                    if (sleepTs > 0) {
                        Thread.sleep(sleepTs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                for (int i = 0; i < count; i++) {
                    int v = i;
                    try {
                        pool.submit(() -> {
                            try {
//                            System.out.println("begin ... "+ v);
                                if (currSleepTs > 0) {
                                    Thread.sleep(random.nextInt(currSleepTs));
                                }
                                int cnt = random.nextInt(currSleepTs + 1);
                                double sum = 0;
                                for (int j = 0; j < cnt; j++) {
                                    double p = Math.sin(j);
                                    p = Math.sqrt(p);
                                    p = Math.pow(p, j);
                                    sum += p;
                                }
//                            System.out.println("end ... "+v+",sum="+sum);
                                return sum;
                            } finally {
                                latch.countDown();
                            }
                        });
                        Thread.sleep(random.nextInt(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            latch.await();

            System.out.println("ok");

            long tes = System.currentTimeMillis();
            long diff = tes - tbs;
            execArr[t] = diff;
        }

        for (int i = 0; i < sleepArr.length; i++) {
            System.out.println("result:" + sleepArr[i] + ":" + execArr[i]);
        }

        Thread.sleep(60 * 1000);

        pool.submit(() -> {
            System.out.println("ok");
        });
    }
}
