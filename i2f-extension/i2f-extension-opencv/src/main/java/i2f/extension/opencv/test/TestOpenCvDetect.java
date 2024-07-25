package i2f.extension.opencv.test;

import i2f.extension.opencv.OpenCvProvider;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/25 19:56
 * @desc
 */
public class TestOpenCvDetect {
    public static void main(String[] args){
        File file=new File("./tmp.jpeg");
        File saveFile=new File("./tmp-save.jpg");
        List<Rectangle> rectangles = OpenCvProvider.detectFrontFace(file,true,saveFile);
        for (Rectangle rectangle : rectangles) {
            System.out.println(rectangle);
        }
    }
}
