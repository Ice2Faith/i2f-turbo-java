package i2f.graphics.d2.shape;

import i2f.graphics.d2.Point;
import i2f.graphics.d2.Scope;
import i2f.graphics.d2.Size;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:10
 * @desc 矩形
 */
@Data
public class Rectangle extends Scope {
    public Rectangle() {
    }

    public Rectangle(double x, double y, double dx, double dy) {
        super(x, y, dx, dy);
    }

    public Rectangle(Point point, Size size) {
        super(point, size);
    }

    public Rectangle(Point lt, Point rb) {
        super(lt, rb);
    }
}
