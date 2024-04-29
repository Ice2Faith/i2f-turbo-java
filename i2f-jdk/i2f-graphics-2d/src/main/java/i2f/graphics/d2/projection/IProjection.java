package i2f.graphics.d2.projection;

import i2f.graphics.d2.Point;

/**
 * @author Ice2Faith
 * @date 2022/6/19 15:15
 * @desc
 */
public interface IProjection {
    Point projection(Point point);

    void onSizeChange(int width, int height);
}
