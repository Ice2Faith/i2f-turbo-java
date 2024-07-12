package i2f.graphics.d2;


import i2f.math.Calc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/6/18 23:52
 * @desc
 */
public class Bezier {
    public static double bernstein(int idx, int cnt, double rate) {
        double cni = 1, tp = 1;
        for (int j = 1; j <= idx; j++) {
            if (cnt - j + 1 > 0) {
                cni *= cnt - j + 1;
            }
            tp *= j;
        }
        cni = cni / tp;
        return cni * Math.pow(rate, idx) * Math.pow(1 - rate, cnt - idx);
    }

    public static Point bezierPoint(double rate, List<Point> points) {
        double sumX = 0, sumY = 0;
        int listSize = points.size();
        int i = 0;
        for (Point point : points) {
            double brate = bernstein(i, listSize - 1, rate);
            sumX += point.x * brate;
            sumY += point.y * brate;
            i++;
        }
        return new Point(sumX, sumY);
    }

    public static List<Point> resamples(List<Point> points, int resampleCount) {
        List<Point> ret = new ArrayList<>(resampleCount);
        for (int i = 0; i < resampleCount; i++) {
            Point p = Bezier.bezierPoint(1.0 * i / resampleCount, points);
            ret.add(p);
        }
        return ret;
    }

    public static List<Point> samples(List<Point> points) {
        double len = 0;
        Point pre = null;
        int i = 0;
        for (Point point : points) {
            if (i == 0) {
                pre = point;
            } else {
                Point cur = point;
                len += Calc.abs(cur.x - pre.x) + Calc.abs(cur.y - pre.y);
            }
            i++;
        }
        len *= 1.2;
        int count = (int) len;
        return resamples(points, count);
    }

}
