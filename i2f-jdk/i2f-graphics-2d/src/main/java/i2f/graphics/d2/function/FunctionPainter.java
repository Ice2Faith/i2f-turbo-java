package i2f.graphics.d2.function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/28 15:09
 * @desc
 */
public class FunctionPainter {
    public static void main(String[] args) throws IOException {
//        testCross();
//        testCenter();
        testArguments();
    }

    public static void testArguments() throws IOException {
        BufferedImage img = drawFunction(0, Math.PI * 2, 0.02,
                -1, -1,
                null,
                15, 15,
                FunctionType.ARGUMENTS,
                (t) -> new double[]{
                        16*Math.pow(Math.sin(t),3.0),
                        13*Math.cos(t)-5*Math.cos(2*t)-2*Math.cos(3*t)-Math.cos(4*t),
                        5*t,
                        t*Math.sin(3*t)
                });
        ImageIO.write(img, "png", new File("./tmp.png"));
    }

    public static void testCenter() throws IOException {
        BufferedImage img = drawFunction(0, Math.PI*2, 0.02,
                -1, -1,
                null,
                15, 15,
                FunctionType.CENTER,
                (x) -> new double[]{
                        x,
                        Math.sin(x)+x*Math.cos(x),
                        Math.abs(Math.sin(x))+Math.abs(Math.cos(x))

                });
        ImageIO.write(img, "png", new File("./tmp.png"));
    }

    public static void testCross() throws IOException {
        BufferedImage img = drawFunction(-25, 5, 0.2,
                -1, -1,
                null,
                15, 15,
                FunctionType.CROSS,
                (x) -> new double[]{
                        Math.sin(x),
                        Math.cos(x),
                        Math.pow(1.12, x)
                });
        ImageIO.write(img, "png", new File("./tmp.png"));
    }

    public enum FunctionType {
        CROSS, // 直角坐标，返回值 yArr: y1,y2,y3 ...
        CENTER, // 极坐标，返回值 yArr: radius1,radius2 ...
        ARGUMENTS // 参数方程，返回值 yArr: x1,y1,x2,y2,...
    }
    public static BufferedImage drawFunction(double beginX, double endX, double accX,
                                             Function<Double, double[]> yValueMapper) {
        return drawFunction(beginX,endX,accX,
                1080,720,null,
                20,20,FunctionType.CROSS,
                yValueMapper);
    }
    public static BufferedImage drawFunction(double beginX, double endX, double accX,
                                             int width, int height,
                                             Function<Double, double[]> yValueMapper) {
        return drawFunction(beginX,endX,accX,
                width,height,null,
                20,20,FunctionType.CROSS,
                yValueMapper);
    }

    public static BufferedImage drawFunction(double beginX, double endX, double accX,
                                             int width, int height, BufferedImage img,
                                             Function<Double, double[]> yValueMapper) {
        return drawFunction(beginX,endX,accX,
                width,height,img,
                20,20,FunctionType.CROSS,
                yValueMapper);
    }

    public static BufferedImage drawFunction(double beginX, double endX, double accX,
                                             int width, int height, BufferedImage img,
                                             double xAxisCount, double yAxisCount,
                                             Function<Double, double[]> yValueMapper) {
        return drawFunction(beginX,endX,accX,
                width,height,img,
                xAxisCount,yAxisCount,FunctionType.CROSS,
                yValueMapper);
    }

    public static BufferedImage drawFunction(double beginX, double endX, double accX,
                                             int width, int height, BufferedImage img,
                                             double xAxisCount, double yAxisCount,
                                             FunctionType type,
                                             Function<Double, double[]> yValueMapper) {
        if (width <= 0) {
            width = 1080;
        }
        if (height <= 0) {
            height = 720;
        }
        if (beginX > endX) {
            double tmp = endX;
            beginX = endX;
            endX = tmp;
        }
        if (accX < 0) {
            accX = -accX;
        }
        if (img == null) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        width = img.getWidth();
        height = img.getHeight();

        if (type == null) {
            type = FunctionType.CROSS;
        }

        Graphics2D g = img.createGraphics();
        // clean background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        Double minY = null;
        Double maxY = null;
        Double minX=beginX;
        Double maxX=endX;

        List<List<double[]>> lines=new ArrayList<>();
        for (double x = beginX; x <= endX; x += accX) {
            double[] yArr = yValueMapper.apply(x);
            if (type==FunctionType.CROSS){
                // yArr: y1,y2,y3 ...
                for (int i = 0; i < yArr.length; i++) {
                    double cx = x;
                    double cy = yArr[i];
                    double[] arr = new double[]{cx, cy};

                    if (minY == null || arr[1] < minY) {
                        minY = arr[1];
                    }
                    if (maxY == null || arr[1] > maxY) {
                        maxY = arr[1];
                    }

                    if(minX==null || arr[0] <minX){
                        minX=arr[0];
                    }
                    if(maxX==null || arr[0]>maxX){
                        maxX=arr[0];
                    }

                    if(lines.size()<=i){
                        lines.add(new ArrayList<>(100));
                    }
                    lines.get(i).add(arr);
                }
            } else if (type == FunctionType.CENTER) {
                double angle = x;
                // yArr: radius1,radius2 ...
                for (int i = 0; i < yArr.length; i++) {
                    double radius=yArr[i];
                    double cx = radius * Math.cos(angle);
                    double cy = radius * Math.sin(angle);
                    double[] arr = new double[]{cx, cy};

                    if (minY == null || arr[1] < minY) {
                        minY = arr[1];
                    }
                    if (maxY == null || arr[1] > maxY) {
                        maxY = arr[1];
                    }

                    if(minX==null || arr[0] <minX){
                        minX=arr[0];
                    }
                    if(maxX==null || arr[0]>maxX){
                        maxX=arr[0];
                    }


                    if(lines.size()<=i){
                        lines.add(new ArrayList<>(100));
                    }
                    lines.get(i).add(arr);
                }

            } else if(type == FunctionType.ARGUMENTS){
                // yArr: x1,y1,x2,y2,...
                for (int i = 0; (i+1) < yArr.length; i+=2) {
                    double cx = yArr[i];
                    double cy = yArr[i+1];
                    double[] arr = new double[]{cx, cy};

                    if (minY == null || arr[1] < minY) {
                        minY = arr[1];
                    }
                    if (maxY == null || arr[1] > maxY) {
                        maxY = arr[1];
                    }


                    if(minX==null || arr[0] <minX){
                        minX=arr[0];
                    }
                    if(maxX==null || arr[0]>maxX){
                        maxX=arr[0];
                    }

                    int j=i/2;
                    if(lines.size()<=j){
                        lines.add(new ArrayList<>(100));
                    }
                    lines.get(j).add(arr);
                }
            }
        }

        final double finalWidth = width;

        final double finalDiffX = maxX - minX;
        final double finalDiffY = maxY - minY;

        final double finalMinX = minX;
        Function<Double, Double> xTransform = (x) -> {
            return (x - finalMinX) / finalDiffX * finalWidth;
        };

        final double finalHeight = height;
        final double finalMinY = minY;
        final double finalMaxY = maxY;
        Function<Double, Double> yTransform = (y) -> {
            return finalHeight - ((y - finalMinY) / (finalDiffY) * finalHeight);
        };


        // axis line
        g.setColor(new Color(220, 220, 220));
        g.setFont(new Font(null, 0, 20));
        double midX = finalDiffX / 2.0;
        double stepX = finalDiffX / xAxisCount;
        double px = midX;
        while (px > minX) {
            g.drawLine((int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(minY),
                    (int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(maxY));
            g.drawString(String.format("%.2f", px), (int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(minY));
            px -= stepX;
        }

        px = midX;
        while (px < maxX) {
            g.drawLine((int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(minY),
                    (int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(maxY));
            g.drawString(String.format("%.2f", px), (int) (double) xTransform.apply(px),
                    (int) (double) yTransform.apply(minY));
            px += stepX;
        }


        double midY = finalDiffY / 2.0;
        double stepY = finalDiffY / yAxisCount;
        double py = midY;
        while (py > minY) {
            g.drawLine((int) (double) xTransform.apply(minX),
                    (int) (double) yTransform.apply(py),
                    (int) (double) xTransform.apply(maxX),
                    (int) (double) yTransform.apply(py));
            g.drawString(String.format("%.2f", py), (int) (double) xTransform.apply(minX),
                    (int) (double) yTransform.apply(py));
            py -= stepY;
        }

        px = midX;
        while (py < maxY) {
            g.drawLine((int) (double) xTransform.apply(minX),
                    (int) (double) yTransform.apply(py),
                    (int) (double) xTransform.apply(maxX),
                    (int) (double) yTransform.apply(py));
            g.drawString(String.format("%.2f", py), (int) (double) xTransform.apply(minX),
                    (int) (double) yTransform.apply(py));
            py += stepY;
        }


        // draw x,y axis
        // x axis
        g.setColor(Color.BLUE);
        g.drawLine((int) (double) xTransform.apply(-2 * finalDiffX),
                (int) (double) yTransform.apply(0.0),
                (int) (double) xTransform.apply(2 * finalDiffX),
                (int) (double) yTransform.apply(0.0));

        // y axis
        g.setColor(Color.RED);
        g.drawLine((int) (double) xTransform.apply(0.0),
                (int) (double) yTransform.apply(-2 * finalDiffY),
                (int) (double) xTransform.apply(0.0),
                (int) (double) yTransform.apply(2 * finalDiffY));


        Random random = new SecureRandom();

        int linesCount = lines.size();
        Color[] colors = new Color[linesCount];
        for (int i = 0; i < linesCount; i++) {
            colors[i] = new Color(random.nextInt(230), random.nextInt(230), random.nextInt(230));
        }


        for (int i = 0; i < lines.size(); i++) {
            g.setColor(colors[i]);

            List<double[]> line=lines.get(i);

            double lastX=0;
            double lastY=0;
            boolean isFirst = true;
            for (double[] xy : line) {
                double x = xy[0];
                if (Double.isInfinite(x)) {
                    x = width;
                }
                if (Double.isNaN(x)) {
                    x = 0;
                }
                double vx = xTransform.apply(x);

                double y = xy[1];
                if (Double.isInfinite(y)) {
                    y = height;
                }
                if (Double.isNaN(y)) {
                    y = 0;
                }
                double vy = yTransform.apply(y);

                if (!isFirst) {
                    g.setColor(colors[i]);
                    g.drawLine((int) (lastX), (int) (lastY),
                            (int) (vx), (int) (vy));
                }

                lastY = vy;

                lastX = vx;
                isFirst = false;
            }

        }

        return img;
    }
}
