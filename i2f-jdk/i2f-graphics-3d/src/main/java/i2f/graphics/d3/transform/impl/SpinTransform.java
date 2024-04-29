package i2f.graphics.d3.transform.impl;

import i2f.graphics.d3.D3Point;
import i2f.graphics.d3.transform.ID3Transform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/18 21:27
 * @desc 旋转变换，坐标依次绕x,y,z轴旋转sx,sy,sz角度（弧度制）,相对于轴右手向旋转
 */
@Data
@NoArgsConstructor
public class SpinTransform implements ID3Transform {
    protected boolean enableMatrix = false;
    public double sx;
    public double sy;
    public double sz;


    public SpinTransform(boolean enableMatrix, double sx, double sy, double sz) {
        this.enableMatrix = enableMatrix;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override
    public D3Point transform(D3Point point) {
        ID3Transform trans = new SpinXTransform(enableMatrix, sx);
        D3Point p = trans.transform(point);
        trans = new SpinYTransform(enableMatrix, sy);
        p = trans.transform(p);
        trans = new SpinZTransform(enableMatrix, sz);
        return trans.transform(p);
    }
}
