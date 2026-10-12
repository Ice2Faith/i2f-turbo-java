package i2f.algo.limit.border;

import i2f.algo.limit.border.impl.dim1.NumberRange;
import i2f.graphics.d2.Point;
import i2f.graphics.d2.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Ice2Faith
 * @date 2026/10/12 11:03
 * @desc
 */
public class BorderLimitUtilTest {
    public static void main(String[] args) {
        Random random = new Random();
        int cnt = random.nextInt(90) + 10;
        for (int i = 0; i < cnt; i++) {
            System.out.println("=============================================");
            int num1 = random.nextInt(100);
            int num2 = random.nextInt(100) + num1;

            int num3 = random.nextInt(100);
            int num4 = random.nextInt(100) + num3;

            Double fnum = BorderLimitUtil.findNumberBorder((double) num1, (double) num2, new NumberRange((double) num3, (double) num4));
            System.out.println("find=" + fnum);

        }

        List<Point> polygon = new ArrayList<>();
        polygon.add(new Point(119.416421, 26.023969));
        polygon.add(new Point(119.417018, 26.023887));
        polygon.add(new Point(119.416857, 26.022865));
        polygon.add(new Point(119.416246, 26.022966));

        System.out.println("========================================================");
        Point bd = BorderLimitUtil.findPolygonBorder(new Point(119.415954, 26.023319),
                new Point(119.416771, 26.023628),
                new Polygon(polygon));
        System.out.println("polygon border=" + bd);

    }
}
