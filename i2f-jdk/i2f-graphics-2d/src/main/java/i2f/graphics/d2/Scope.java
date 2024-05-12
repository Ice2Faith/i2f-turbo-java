package i2f.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/17 23:26
 * @desc 描述二维空间范围
 */
@Data
@NoArgsConstructor
public class Scope {
    public Point point=new Point(0,0);
    public Size size=new Size(0,0);

    public Scope(double x, double y, double dx, double dy) {
        this.point = new Point(x, y);
        this.size = new Size(dx, dy);
    }

    public Scope(Point point, Size size) {
        this.point = point;
        this.size = size;
    }

    public Scope(Point lt,Point rb){
        this.point=lt;
        this.size=new Size(rb.x-lt.x,rb.y-lt.y);
    }

    public double left(){
        return point.x;
    }

    public double top(){
        return point.y;
    }

    public double right(){
        return point.x+size.dx;
    }

    public double bottom(){
        return point.y+size.dy;
    }
}
