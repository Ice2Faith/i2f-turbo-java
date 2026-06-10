package i2f.tools.yi;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2026/6/10 15:15
 * @desc
 */
public class TestBaZi {
    public static void main(String[] args) {
        BaZi baZi = BaZi.of(GanZhiDate.of(LocalDateTime.of(1999, 4, 12, 6, 0)));
        System.out.println(baZi.getSimpleInfo(true,null));
    }
}
