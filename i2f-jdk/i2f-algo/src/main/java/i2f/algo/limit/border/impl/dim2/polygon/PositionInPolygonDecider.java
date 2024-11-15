package i2f.algo.limit.border.impl.dim2.polygon;

import i2f.algo.limit.border.BorderInDecider;
import i2f.graphics.d2.Point;
import i2f.graphics.d2.polygon.PolygonLocationTool;
import i2f.graphics.d2.shape.Polygon;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:55
 * @desc
 */
public class PositionInPolygonDecider implements BorderInDecider<Point, Polygon> {
    @Override
    public boolean isInRange(Point elem, Polygon range) {
        return PolygonLocationTool.windInPolygon(elem, range.points);
    }
}
