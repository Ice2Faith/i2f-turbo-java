package i2f.tools.yi;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2026/6/9 15:39
 * @desc
 */
public class TestGanZhiDate {
    public static void main(String[] args) {
        GanZhiDate ret = GanZhiDate.of(LocalDateTime.now());
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 1, 1, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 12, 31, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 2, 3, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 2, 4, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 2, 5, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 2, 6, 0, 0, 0));
        System.out.println(ret);

        ret = GanZhiDate.of(LocalDateTime.of(2026, 2, 7, 0, 0, 0));
        System.out.println(ret);
    }
}
