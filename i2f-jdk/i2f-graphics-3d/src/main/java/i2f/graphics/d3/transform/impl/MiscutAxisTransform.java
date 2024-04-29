package i2f.graphics.d3.transform.impl;

import i2f.graphics.d3.D3Point;
import i2f.graphics.d3.transform.ID3Transform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/18 21:27
 * @desc 错切变换，坐标依次沿x,y,z轴错切，分别离开z,y轴向x轴移动gz,dy距离，依次类推
 */
@Data
@NoArgsConstructor
public class MiscutAxisTransform implements ID3Transform {
    protected boolean enableMatrix = false;
    public double xgz;
    public double xdy;

    public double yhz;
    public double ybx;

    public double zfy;
    public double zcx;

    public MiscutAxisTransform(boolean enableMatrix, double xgz, double xdy, double yhz, double ybx, double zfy, double zcx) {
        this.enableMatrix = enableMatrix;
        this.xgz = xgz;
        this.xdy = xdy;
        this.yhz = yhz;
        this.ybx = ybx;
        this.zfy = zfy;
        this.zcx = zcx;
    }

    @Override
    public D3Point transform(D3Point point) {
        ID3Transform trans = new MiscutAxisXTransform(enableMatrix, xgz, xdy);
        D3Point p = trans.transform(point);
        trans = new MiscutAxisYTransform(enableMatrix, yhz, ybx);
        p = trans.transform(p);
        trans = new MiscutAxisZTransform(enableMatrix, zfy, zcx);
        return trans.transform(p);
    }
}
