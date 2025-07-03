package i2f.extension.cron.test;

import com.cronutils.model.CronType;
import i2f.extension.cron.CronExecutor;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2025/7/3 14:56
 */
public class TestCronExecutor {
    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 1; i++) {
            testExecutor();
        }

        System.in.read();
    }

    public static void testExecutor() {
        SecureRandom rand = new SecureRandom();
        CronExecutor executor = new CronExecutor();
        for (int i = 1; i < 30; i++) {
            final int taskIter = i;
            executor.submit(CronType.QUARTZ, "0/" + i + " * * * * ?", () -> {
                try {
                    Thread.sleep(rand.nextInt(3000));
                } catch (Exception e) {

                }
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                System.out.println("exec" + taskIter + ":" + fmt.format(date));
            });
        }
    }
}
