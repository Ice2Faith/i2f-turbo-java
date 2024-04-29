package i2f.graphics.d2.tranform;

import i2f.graphics.d2.Point;

/**
 * @author Ice2Faith
 * @date 2022/6/21 14:52
 * @desc 2D变换接口
 */
public interface ITransform {
    Point transform(Point p);
}
