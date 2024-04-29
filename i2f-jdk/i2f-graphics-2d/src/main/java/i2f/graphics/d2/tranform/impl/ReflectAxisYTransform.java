package i2f.graphics.d2.tranform.impl;

import i2f.graphics.d2.Point;

/**
 * @author Ice2Faith
 * @date 2022/6/21 15:00
 * @desc Y轴反射变换
 */
public class ReflectAxisYTransform extends AbstractMatrixTransform {

    public ReflectAxisYTransform(boolean enableMatrix) {
        super(enableMatrix);
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {-1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x = -p.x;
        double y = p.y;
        return new Point(x, y);
    }
}
