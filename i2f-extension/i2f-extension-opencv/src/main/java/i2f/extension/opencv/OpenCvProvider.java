package i2f.extension.opencv;

import i2f.io.file.FileUtil;
import i2f.os.OsUtil;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/25 18:46
 * @desc
 */
public class OpenCvProvider {
    public static final String OFFICIAL_URL = "https://opencv.org/";

    public static final String[] CLASS_PATHS = {
            "lib/opencv_java345_x64.dll",
            "lib/opencv_java345_x86.dll"
    };
    public static final String DLL_PATH = "./opencv";
    public static final String DATA_PATH = DLL_PATH + "/data";
    private static final AtomicBoolean initialed = new AtomicBoolean(false);

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
        File releaseDir = new File(DLL_PATH);
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
        System.out.println("please copy *.dll files to your system %PATH% variable directory from " + DLL_PATH + ".");

    }
    public static void loadNative(){
        String dllName = "opencv_java345";
        if (OsUtil.is64bit()) {
            dllName = dllName + "_x64";
        } else {
            dllName = dllName + "_x86";
        }
        dllName = System.mapLibraryName(dllName);
        System.load(new File(DLL_PATH, dllName).getAbsolutePath());
    }
    public static List<Rectangle> detectFrontFace(File imgFile) {
        return detectFrontFace(imgFile,false,null);
    }
    public static List<Rectangle> detectFrontFace(File imgFile,boolean showInGui) {
        return detectFrontFace(imgFile,showInGui,null);
    }
    public static List<Rectangle> detectFrontFace(File imgFile,boolean showInGui,File markSaveFile) {
        return detectMultiScale(imgFile, "haarcascades/haarcascade_frontalface_alt.xml",showInGui,markSaveFile);
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

        File fontFaceXmlFile = getClasspathOpenCvDataFile(cascadeXmlFileName);

        CascadeClassifier classifier = new CascadeClassifier(fontFaceXmlFile.getAbsolutePath());

        MatOfRect matOfRect = new MatOfRect();
        classifier.detectMultiScale(imgMat, matOfRect);

        Rect[] arr = matOfRect.toArray();
        if(arr!=null) {
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

    public static File getClasspathOpenCvDataFile(String fontFaceXmlName) {
        File fontFaceXmlFile = new File(DATA_PATH, fontFaceXmlName);
        if (!fontFaceXmlFile.exists()) {
            try {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("lib/data/" + fontFaceXmlName);
                FileUtil.save(is, fontFaceXmlFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fontFaceXmlFile;
    }

}
