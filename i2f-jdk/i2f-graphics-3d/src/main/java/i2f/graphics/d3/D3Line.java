package i2f.graphics.d3;

import i2f.graphics.d2.Line;
import i2f.graphics.d2.std.ILenght;
import i2f.graphics.d3.projection.ID3Projection;
import i2f.math.MathUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/17 22:49
 * @desc 直线
 */
@Data
@NoArgsConstructor
public class D3Line implements ILenght {
    public D3Point begin;
    public D3Point end;

    public D3Line(D3Point begin, D3Point end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public double length() {
        return MathUtil.distance(this.begin.x, this.begin.y, this.begin.z, this.end.x, this.end.y, this.end.z);
    }

    public Line projection(ID3Projection proj) {
        return new Line(begin.projection(proj), end.projection(proj));
    }


    public double dx() {
        return end.x - begin.x;
    }

    public double dy() {
        return end.y - begin.y;
    }

    public double dz() {
        return end.z - begin.z;
    }
}
