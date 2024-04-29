package i2f.math;

import java.util.Random;

/**
 * @author Ice2Faith
 * @date 2022/6/17 22:56
 * @desc
 */
public class Calc {
    public static final double PI = 3.141592653549626;
    public static final double PI_RADIAN = PI;
    public static final double PI2 = PI * 2;
    public static final double PI_ANGLE = 180.0;
    public static final double PI_ANGLE2 = PI_ANGLE * 2;
    public static Random rand = new Random();

    /**
     * 求三角形三边边长分别为lenA,lenB,lenC的三角形中
     * lenA的对角A的弧度
     * 因此，如果你要求B的弧度，则参数为：lenB,lenA,lenC
     * 也就是说，想要求那条边的对角，那条边就是第一个参数，第二第三个参数为其他两边，无顺序要求
     * 原理：
     * 余弦定理：余弦定理公式是cosA=(b^2+c^2-a^2) /2bc，cosA=邻边比斜边
     * A角的对边就是a边
     * 因此，语义为：角A的cos值=(两邻边的平方和-对边的平方和)/(2被两邻边积)
     *
     * @param lenA
     * @param lenB
     * @param lenC
     * @return
     */
    public static double radianTriangle(double lenA, double lenB, double lenC) {
        double cosa = (Math.pow(lenB, 2.0) + Math.pow(lenC, 2.0) - Math.pow(lenA, 2.0)) / (2 * lenB * lenC);
        double ra = Math.acos(cosa);
        return regularRadian(ra);
    }

    /**
     * 求三角形三边边长分别为lenA,lenB,lenC的三角形中
     * lenA的对角A的角度
     *
     * @param lenA
     * @param lenB
     * @param lenC
     * @return
     */
    public static double angleTriangle(double lenA, double lenB, double lenC) {
        double ra = radianTriangle(lenA, lenB, lenC);
        return Calc.regularAngle(Calc.radian2angle(ra));
    }

    /**
     * 插值平滑
     * 一般用来平滑过度颜色，实现渐变色
     * 或者在线条绘制中，进行线条绘制点确定
     *
     * @param rate
     * @param start
     * @param end
     * @return
     */
    public static double smooth(double rate, double start, double end) {
        return start * rate + end * (1.0 - rate);
    }

    public static int smooth(double rate, int start, int end) {
        return (int) (start * rate + end * (1.0 - rate));
    }

    public static long smooth(double rate, long start, long end) {
        return (long) (start * rate + end * (1.0 - rate));
    }

    /**
     * 绝对值
     *
     * @param val
     * @return
     */
    public static double abs(double val) {
        if (val < 0) {
            return 0 - val;
        }
        return val;
    }

    /**
     * 绝对值
     *
     * @param val
     * @return
     */
    public static int abs(int val) {
        if (val < 0) {
            return -val;
        }
        return val;
    }

    /**
     * 绝对值
     *
     * @param val
     * @return
     */
    public static long abs(long val) {
        if (val < 0) {
            return -val;
        }
        return val;
    }

    /**
     * 相反数
     *
     * @param val
     * @return
     */
    public static double opposite(double val) {
        return 0 - val;
    }

    /**
     * 相反数
     *
     * @param val
     * @return
     */
    public static int opposite(int val) {
        return -val;
    }

    /**
     * 相反数
     *
     * @param val
     * @return
     */
    public static long opposite(long val) {
        return -val;
    }

    /**
     * 倒数
     *
     * @param val
     * @return
     */
    public static double reciprocal(double val) {
        return 1.0 / val;
    }

    /**
     * 距离
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double distance(double d1, double d2) {
        return abs(d2 - d1);
    }

    /**
     * 平方
     *
     * @param val
     * @return
     */
    public static double square(double val) {
        return Math.pow(val, 2.0);
    }

    /**
     * 立方
     *
     * @param val
     * @return
     */
    public static double cube(double val) {
        return Math.pow(val, 3.0);
    }

    /**
     * 最小值
     *
     * @param vals
     * @return
     */
    public static double min(double... vals) {
        double min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] < min) {
                min = vals[i];
            }
        }
        return min;
    }

    /**
     * 最小值
     *
     * @param vals
     * @return
     */
    public static int min(int... vals) {
        int min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] < min) {
                min = vals[i];
            }
        }
        return min;
    }

    /**
     * 最小值
     *
     * @param vals
     * @return
     */
    public static long min(long... vals) {
        long min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] < min) {
                min = vals[i];
            }
        }
        return min;
    }

    /**
     * 最大值
     *
     * @param vals
     * @return
     */
    public static double max(double... vals) {
        double max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] > max) {
                max = vals[i];
            }
        }
        return max;
    }

    /**
     * 最大值
     *
     * @param vals
     * @return
     */
    public static int max(int... vals) {
        int max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] > max) {
                max = vals[i];
            }
        }
        return max;
    }

    /**
     * 最大值
     *
     * @param vals
     * @return
     */
    public static long max(long... vals) {
        long max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] > max) {
                max = vals[i];
            }
        }
        return max;
    }

    /**
     * 求积
     *
     * @param vals
     * @return
     */
    public static double mul(double... vals) {
        double ret = 1.0;
        for (double item : vals) {
            ret *= item;
        }
        return ret;
    }

    /**
     * 求积
     *
     * @param vals
     * @return
     */
    public static int mul(int... vals) {
        int ret = 1;
        for (int item : vals) {
            ret *= item;
        }
        return ret;
    }

    /**
     * 求积
     *
     * @param vals
     * @return
     */
    public static long mul(long... vals) {
        long ret = 1;
        for (long item : vals) {
            ret *= item;
        }
        return ret;
    }

    /**
     * 求积1-i
     *
     * @param i
     * @return
     */
    public static int muli(int i) {
        int ret = 1;
        for (int j = 1; j < i; j++) {
            ret *= j;
        }
        return ret;
    }

    /**
     * 求积1-i
     *
     * @param i
     * @return
     */
    public static long muli(long i) {
        int ret = 1;
        for (int j = 1; j < i; j++) {
            ret *= j;
        }
        return ret;
    }

    /**
     * 求积i-j
     *
     * @param i
     * @param j
     * @return
     */
    public static int mulij(int i, int j) {
        int ret = 1;
        int step = 1;
        if (j < i) {
            step = -1;
        }
        for (int p = i; p != j; p += step) {
            ret *= p;
        }
        return ret;
    }

    /**
     * 求积i-j
     *
     * @param i
     * @param j
     * @return
     */
    public static long mulij(long i, long j) {
        long ret = 1;
        int step = 1;
        if (j < i) {
            step = -1;
        }
        for (long p = i; p != j; p += step) {
            ret *= p;
        }
        return ret;
    }

    /**
     * 求积i-j按照步长step
     *
     * @param i
     * @param j
     * @param step
     * @return
     */
    public static int mulij(int i, int j, int step) {
        int ret = 1;
        for (int p = i; p != j; p += step) {
            ret *= p;
        }
        return ret;
    }

    /**
     * 求积i-j按照步长step
     *
     * @param i
     * @param j
     * @param step
     * @return
     */
    public static long mulij(long i, long j, long step) {
        long ret = 1;
        for (long p = i; p != j; p += step) {
            ret *= p;
        }
        return ret;
    }


    /**
     * 求和
     *
     * @param vals
     * @return
     */
    public static double sum(double... vals) {
        double ret = 0;
        for (double item : vals) {
            ret += item;
        }
        return ret;
    }

    /**
     * 求和
     *
     * @param vals
     * @return
     */
    public static int sum(int... vals) {
        int ret = 0;
        for (int item : vals) {
            ret += item;
        }
        return ret;
    }

    /**
     * 求和
     *
     * @param vals
     * @return
     */
    public static long sum(long... vals) {
        long ret = 0;
        for (long item : vals) {
            ret += item;
        }
        return ret;
    }

    /**
     * 求和0-i
     *
     * @param i
     * @return
     */
    public static int sumi(int i) {
        int ret = 0;
        for (int j = 0; j < i; j++) {
            ret += j;
        }
        return ret;
    }

    /**
     * 求和0-i
     *
     * @param i
     * @return
     */
    public static long sumi(long i) {
        long ret = 0;
        for (long j = 0; j < i; j++) {
            ret += j;
        }
        return ret;
    }

    /**
     * 求和i-j
     *
     * @param i
     * @param j
     * @return
     */
    public static int sumij(int i, int j) {
        int ret = 0;
        int step = 1;
        if (j < i) {
            step = -1;
        }
        for (int p = i; p != j; p += step) {
            ret += p;
        }
        return ret;
    }

    /**
     * 求和i-j
     *
     * @param i
     * @param j
     * @return
     */
    public static long sumij(long i, long j) {
        long ret = 0;
        int step = 1;
        if (j < i) {
            step = -1;
        }
        for (long p = i; p != j; p += step) {
            ret += p;
        }
        return ret;
    }

    /**
     * 求和i-j按照步长step
     *
     * @param i
     * @param j
     * @param step
     * @return
     */
    public static int sumij(int i, int j, int step) {
        int ret = 0;
        for (int p = i; p != j; p += step) {
            ret += p;
        }
        return ret;
    }

    /**
     * 求和i-j按照步长step
     *
     * @param i
     * @param j
     * @param step
     * @return
     */
    public static long sumij(long i, long j, long step) {
        long ret = 0;
        for (long p = i; p != j; p += step) {
            ret += p;
        }
        return ret;
    }

    /**
     * 对平方和开方
     *
     * @param vals
     * @return
     */
    public static double sqrtSquareSum(double... vals) {
        return Math.sqrt(squareSum(vals));
    }

    /**
     * 平方和
     *
     * @param vals
     * @return
     */
    public static double squareSum(double... vals) {
        double ret = 0;
        for (double item : vals) {
            ret += square(item);
        }
        return ret;
    }

    /**
     * 立方和
     *
     * @param vals
     * @return
     */
    public static double cubeSum(double... vals) {
        double ret = 0;
        for (double item : vals) {
            ret += cube(item);
        }
        return ret;
    }

    /**
     * 距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(squareSum(distance(x2, x1), distance(y2, y1)));
    }

    /**
     * 距离
     *
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return
     */
    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(squareSum(distance(x2, x1), distance(y2, y1), distance(z2, z1)));
    }

    /**
     * 排列，从n中取m个
     *
     * @param m
     * @param n
     * @return
     */
    public static int arrangement(int n, int m) {
        return mulij(n - m + 1, n);
    }

    /**
     * 组合，从n中取m个
     *
     * @param n
     * @param m
     * @return
     */
    public static int combination(int n, int m) {
        return arrangement(n, m) / muli(m);
    }

    /**
     * 取fibonacci第i个，i从0开始
     *
     * @param i
     * @return
     */
    public static long fibonacci(int i) {
        return Fibonacci.get(i);
    }

    /**
     * 要求val应大于等于min
     *
     * @param val
     * @param min
     * @return
     */
    public static double gather(double val, double min) {
        if (val < min) {
            val = min;
        }
        return val;
    }

    /**
     * 要求val应大于等于min
     *
     * @param val
     * @param min
     * @return
     */
    public static int gather(int val, int min) {
        if (val < min) {
            val = min;
        }
        return val;
    }

    /**
     * 要求val应大于等于min
     *
     * @param val
     * @param min
     * @return
     */
    public static long gather(long val, long min) {
        if (val < min) {
            val = min;
        }
        return val;
    }

    /**
     * 要求val应小于等于max
     *
     * @param val
     * @param max
     * @return
     */
    public static double lower(double val, double max) {
        if (val > max) {
            val = max;
        }
        return val;
    }

    /**
     * 要求val应小于等于max
     *
     * @param val
     * @param max
     * @return
     */
    public static int lower(int val, int max) {
        if (val > max) {
            val = max;
        }
        return val;
    }

    /**
     * 要求val应小于等于max
     *
     * @param val
     * @param max
     * @return
     */
    public static long lower(long val, long max) {
        if (val > max) {
            val = max;
        }
        return val;
    }

    /**
     * 要求val应大于等于min并小于等于max
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static double between(double val, double min, double max) {
        if (val < min) {
            val = min;
        }
        if (val > max) {
            val = max;
        }
        return val;
    }

    /**
     * 要求val应大于等于min并小于等于max
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static int between(int val, int min, int max) {
        if (val < min) {
            val = min;
        }
        if (val > max) {
            val = max;
        }
        return val;
    }

    /**
     * 要求val应大于等于min并小于等于max
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static long between(long val, long min, long max) {
        if (val < min) {
            val = min;
        }
        if (val > max) {
            val = max;
        }
        return val;
    }


    /**
     * 要求val应该在min-max区间
     * 但是，如果超过max,则采取截去max剩下的部分
     * 也就是一种循环的区间
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static double regular(double val, double min, double max) {
        double dis = max - min;
        if (val < min) {
            while (val < min) {
                val += dis;
            }
        } else {
            while (val > max) {
                val -= dis;
            }
        }
        return val;
    }

    /**
     * 要求val应该在min-max区间
     * 但是，如果超过max,则采取截去max剩下的部分
     * 也就是一种循环的区间
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static int regular(int val, int min, int max) {
        int dis = max - min;
        if (val < min) {
            while (val < min) {
                val += dis;
            }
        } else {
            while (val > max) {
                val -= dis;
            }
        }
        return val;
    }

    /**
     * 要求val应该在min-max区间
     * 但是，如果超过max,则采取截去max剩下的部分
     * 也就是一种循环的区间
     *
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static long regular(long val, long min, long max) {
        long dis = max - min;
        if (val < min) {
            while (val < min) {
                val += dis;
            }
        } else {
            while (val > max) {
                val -= dis;
            }
        }
        return val;
    }

    /**
     * 角度转弧度
     *
     * @param angle
     * @return
     */
    public static double angle2radian(double angle) {
        return (angle / PI_ANGLE) * PI_RADIAN;
    }

    /**
     * 弧度转角度
     *
     * @param radian
     * @return
     */
    public static double radian2angle(double radian) {
        return (radian / PI_RADIAN) * PI_ANGLE;
    }

    /**
     * 规范化为弧度0-2pi
     *
     * @param radian
     * @return
     */
    public static double regularRadian(double radian) {
        if (radian > 0) {
            while (radian > PI2) {
                radian -= PI2;
            }
        } else {
            while (radian < 0) {
                radian += PI2;
            }
        }
        return radian;
    }

    /**
     * 规范化角度0-360
     *
     * @param angle
     * @return
     */
    public static double regularAngle(double angle) {
        if (angle > 0) {
            while (angle > PI_ANGLE2) {
                angle -= PI_ANGLE2;
            }
        } else {
            while (angle < 0) {
                angle += PI_ANGLE2;
            }
        }
        return angle;
    }

    public static int rand() {
        return rand.nextInt();
    }

    public static int rand(int max) {
        return rand.nextInt(max);
    }

    public static int rand(int min, int max) {
        return min + (rand.nextInt(max - min));
    }

    public static double randPercent() {
        return rand.nextDouble();
    }
}
