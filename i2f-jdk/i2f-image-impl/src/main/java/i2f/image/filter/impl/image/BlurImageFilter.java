package i2f.image.filter.impl.image;

import i2f.color.Rgba;
import i2f.image.filter.std.IImageFilter;
import i2f.math.MathUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/6/18 0:47
 * @desc 图像模糊
 */
public class BlurImageFilter implements IImageFilter {
    protected double radius = 3;

    public BlurImageFilter(double radius) {
        this.radius = radius;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {

        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(simg.getWidth(), simg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        int rr = (int) (radius + 1);
        int rsum = (int) MathUtil.sumi(rr);
        for (int x = 0; x < dimg.getWidth(); x++) {
            for (int y = 0; y < dimg.getHeight(); y++) {
                List<Integer> dises = new ArrayList<>();
                List<Rgba> colors = new ArrayList<>();
                int[] cnts = new int[rr + 1];
                for (int i = 0; i < rr + 1; i++) {
                    cnts[i] = 0;
                }
                for (int i = (0 - rr); i <= rr; i++) {
                    if (x + i < 0 || x + i >= dimg.getWidth()) {
                        continue;
                    }
                    for (int j = (0 - rr); j <= rr; j++) {
                        if (y + j < 0 || y + j >= dimg.getHeight()) {
                            continue;
                        }
                        double dis = MathUtil.distance(x, y, x + i, y + j);
                        if (dis > radius) {
                            continue;
                        }
                        int idis = (int) dis;
                        cnts[idis]++;
                        dises.add(idis);
                        colors.add(Rgba.argb(simg.getRGB(x + i, y + j)));
                    }
                }

                double da = 0;
                double dr = 0;
                double dg = 0;
                double db = 0;
                int colorsSize = colors.size();
                for (int i = 0; i < colorsSize; i++) {
                    int dis = dises.get(i);
                    Rgba c = colors.get(i);
                    int ndis = rr - dis;
                    double rate = ndis * 1.0 / rsum;
                    double srate = rate / cnts[dis];
                    da += c.a * srate;
                    dr += c.r * srate;
                    dg += c.g * srate;
                    db += c.b * srate;
                }


                Rgba dc = Rgba.rgba((int) (MathUtil.between(dr, 0, 255)), (int) (MathUtil.between(dg, 0, 255)), (int) (MathUtil.between(db, 0, 255)), (int) (MathUtil.between(da, 0, 255)));
                dimg.setRGB(x, y, dc.argb());
            }
        }

        return dimg;
    }

}
