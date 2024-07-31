package i2f.extension.opencv;

import i2f.extension.opencv.data.OpenCvDataFileProvider;
import i2f.io.file.FileUtil;
import i2f.os.OsUtil;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/25 18:46
 * @desc
 */
public class OpenCvProvider {
    public static final String OFFICIAL_URL = "https://opencv.org/";
    public static final String DLL_BASE_PREFIX = "opencv_java";
    public static final String DLL_BASE_NAME = DLL_BASE_PREFIX + "430";

    public static final String[] CLASS_PATHS = {
            "lib/" + DLL_BASE_NAME + "_x64.dll",
            "lib/" + DLL_BASE_NAME + "_x86.dll"
    };

    private static final AtomicBoolean initialed = new AtomicBoolean(false);

    public static final String HAARCASCADE_FRONTALFACE_ALT_XML = "haarcascades/haarcascade_frontalface_alt.xml";

    public static final String HAARCASCADE_FULLBODY_XML = "haarcascades/haarcascade_fullbody.xml";

    public static final String HAARCASCADE_EYE_XML = "haarcascades/haarcascade_eye.xml";

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadNative();
    }

    public static void init() throws IOException {
        if (initialed.getAndSet(true)) {
            return;
        }
        File releaseDir = new File(OpenCvDataFileProvider.ROOT_PATH);
        if (releaseDir.exists()) {
            return;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String path : CLASS_PATHS) {
            InputStream is = loader.getResourceAsStream(path);
            File saveFile = new File(releaseDir, new File(path).getName());
            FileUtil.save(is, saveFile);
            is.close();
        }
        System.out.println("please copy *.dll files to your system %PATH% variable directory from " + OpenCvDataFileProvider.ROOT_PATH + ".");

    }

    public static void loadNative() {
        String dllName = DLL_BASE_PREFIX + OpenCvVersion.VERSION;
        if (OsUtil.is64bit()) {
            dllName = dllName + "_x64";
        } else {
            dllName = dllName + "_x86";
        }
        dllName = System.mapLibraryName(dllName);
        String dllPath = new File(OpenCvDataFileProvider.ROOT_PATH, dllName).getAbsolutePath();
        System.out.println("load opencv lib : " + dllPath);
        System.load(dllPath);
    }

    public static List<Rectangle> detectFrontFace(File imgFile) {
        return detectFrontFace(imgFile, false, null);
    }

    public static List<Rectangle> detectFrontFace(File imgFile, boolean showInGui) {
        return detectFrontFace(imgFile, showInGui, null);
    }

    public static List<Rectangle> detectFrontFace(File imgFile, boolean showInGui, File markSaveFile) {
        return detectMultiScale(imgFile, HAARCASCADE_FRONTALFACE_ALT_XML, showInGui, markSaveFile);
    }

    public static List<Rectangle> detectFullBody(File imgFile) {
        return detectFullBody(imgFile, false, null);
    }

    public static List<Rectangle> detectFullBody(File imgFile, boolean showInGui) {
        return detectFullBody(imgFile, showInGui, null);
    }

    public static List<Rectangle> detectFullBody(File imgFile, boolean showInGui, File markSaveFile) {
        return detectMultiScale(imgFile, HAARCASCADE_FULLBODY_XML, showInGui, markSaveFile);
    }

    public static List<Rectangle> detectEye(File imgFile) {
        return detectEye(imgFile, false, null);
    }

    public static List<Rectangle> detectEye(File imgFile, boolean showInGui) {
        return detectEye(imgFile, showInGui, null);
    }

    public static List<Rectangle> detectEye(File imgFile, boolean showInGui, File markSaveFile) {
        return detectMultiScale(imgFile, HAARCASCADE_EYE_XML, showInGui, markSaveFile);
    }

    public static List<Rectangle> detectMultiScale(File imgFile, String cascadeXmlFileName) {
        return detectMultiScale(imgFile, cascadeXmlFileName, false, null);
    }

    public static List<Rectangle> detectMultiScale(File imgFile, String cascadeXmlFileName, boolean showInGui) {
        return detectMultiScale(imgFile, cascadeXmlFileName, showInGui, null);
    }

    public static List<Rectangle> detectMultiScale(File imgFile, String cascadeXmlFileName, boolean showInGui, File markSaveFile) {
        List<Rectangle> ret = new ArrayList<>();

        Mat imgMat = Imgcodecs.imread(imgFile.getAbsolutePath());

        File fontFaceXmlFile = OpenCvDataFileProvider.getClasspathOpenCvDataFile(cascadeXmlFileName);

        CascadeClassifier classifier = new CascadeClassifier(fontFaceXmlFile.getAbsolutePath());

        MatOfRect matOfRect = new MatOfRect();
        classifier.detectMultiScale(imgMat, matOfRect);

        Rect[] arr = matOfRect.toArray();
        if (arr != null) {
            for (Rect item : arr) {
                Rectangle rect = new Rectangle(item.x, item.y, item.width, item.height);
                ret.add(rect);
                if (markSaveFile != null || showInGui) {
                    Imgproc.rectangle(imgMat,
                            new Point(item.x, item.y),
                            new Point(item.x + item.width, item.y + item.height),
                            new Scalar(0, 0, 255));// BGR
                }
            }
        }

        if (showInGui) {
            HighGui.imshow("Detect Image", imgMat);
            HighGui.waitKey();
        }

        if (markSaveFile != null) {
            FileUtil.useParentDir(markSaveFile);
            Imgcodecs.imwrite(markSaveFile.getAbsolutePath(), imgMat);
        }
        matOfRect.release();

        imgMat.release();

        return ret;
    }

    public static List<List<java.awt.Point>> findContours(File imgFile) {
        return findContours(imgFile, false, null);
    }

    public static List<List<java.awt.Point>> findContours(File imgFile, boolean showInGui) {
        return findContours(imgFile, showInGui, null);
    }

    public static List<List<java.awt.Point>> findContours(File imgFile, boolean showInGui, File markSaveFile) {
        List<List<java.awt.Point>> ret = new ArrayList<>();

        //读取图片
        Mat imgMat = Imgcodecs.imread(imgFile.getAbsolutePath());

        // 转灰度
        Mat imgGray = imgMat.clone();
        Imgproc.cvtColor(imgMat, imgGray, Imgproc.COLOR_BGR2GRAY);

        Mat imgBlur = imgGray.clone();
        Mat imgSobelX = imgGray.clone();
        Mat imgSobelY = imgGray.clone();

        //对图像进行高斯模糊去噪，梯度计算对噪声很敏感；
        Imgproc.GaussianBlur(imgGray, imgBlur, new Size(5, 5), 1.0);

        imgGray.release();

        //调用Sobel函数计算图像在x,y方向梯度
        Imgproc.Sobel(imgBlur, imgSobelX, -1, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
        Imgproc.Sobel(imgBlur, imgSobelY, -1, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);

        // 梯度累加
        Core.addWeighted(imgSobelX, 0.5, imgSobelY, 0.5, 0, imgBlur);

        imgBlur.release();

        //调用convertScaleAbs函数将x,y梯度图像像素值限制在0-255；src – 原图 dst – 目标图 alpha – 乘数因子/beta – 偏移量
        Core.convertScaleAbs(imgSobelX, imgSobelX, 1, 0);
        Core.convertScaleAbs(imgSobelY, imgSobelY, 1, 0);

        // 调用addWeight函数将x,y梯度图像融合
        Mat imgSobel = new Mat();
        Core.addWeighted(imgSobelX, 1, imgSobelY, 1, 0, imgSobel);

        imgSobelX.release();
        imgSobelY.release();

        // 调用threshold函数对融合图像进行二值化；
        Mat imgBinary = new Mat();
        Imgproc.threshold(imgSobel, imgBinary, 0, 255, Imgproc.THRESH_BINARY);

        imgSobel.release();

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchyMat = new Mat();
        Imgproc.findContours(imgBinary, contours, hierarchyMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);


        if (markSaveFile != null || showInGui) {
            Imgproc.drawContours(imgMat, contours, -1, new Scalar(0, 0, 255));
        }

        for (MatOfPoint contour : contours) {
            List<Point> list = contour.toList();
            List<java.awt.Point> item = new ArrayList<>();
            for (Point point : list) {
                item.add(new java.awt.Point((int) point.x, (int) point.y));
            }
            ret.add(item);
            contour.release();
        }

        hierarchyMat.release();

        if (showInGui) {
            HighGui.imshow("Contours Image", imgMat);
            HighGui.waitKey();
        }

        if (markSaveFile != null) {
            FileUtil.useParentDir(markSaveFile);
            Imgcodecs.imwrite(markSaveFile.getAbsolutePath(), imgMat);
        }


        imgBinary.release();

        imgMat.release();

        return ret;
    }


}
