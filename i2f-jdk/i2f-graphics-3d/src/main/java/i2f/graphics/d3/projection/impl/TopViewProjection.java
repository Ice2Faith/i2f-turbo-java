package i2f.graphics.d3.projection.impl;

import i2f.graphics.d2.Point;
import i2f.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/18 20:18
 * @desc 俯视图, yoz, 转换到xoy(投影到xoy平面)
 */
@Data
@NoArgsConstructor
public class TopViewProjection extends AbstractMatrixProjection {
    public TopViewProjection(boolean enableMatrix) {
        super(enableMatrix);
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {0, -1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    @Override
    public Point proj(D3Point point) {
        return new Point(0 - point.x, 0 - point.y);
    }

    @Override
    public D3Point beforeMatrixReturn(D3Point p) {
        return swapXZ(p);
    }
}
