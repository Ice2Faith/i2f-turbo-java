package i2f.extension.cron.test;

import i2f.extension.cron.CronUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/3 14:56
 */
public class TestCronUtil {
    public static void main(String[] args) {
        List<Date> list = CronUtil.nextQuartzCronTimes("0 0/7 * * * ?", 10);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : list) {
            String str = fmt.format(date);
            System.out.println(str);
        }
    }
}
