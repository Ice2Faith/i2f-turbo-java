package i2f.extension.opencv.javacv.test;


import i2f.extension.opencv.data.OpenCvDataFileProvider;
import i2f.extension.opencv.javacv.OpenCvFaceRecognizer;
import i2f.std.consts.StdConst;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/7/31 11:09
 * @desc
 */
public class TestOpenCvFaceRecognizer {
    public static void main(String[] args) throws IOException {
        File xmlFile = OpenCvDataFileProvider.getClasspathOpenCvDataFile("haarcascades/haarcascade_frontalface_default.xml");

        CascadeClassifier classifier = new CascadeClassifier(xmlFile.getAbsolutePath());

        OpenCvFaceRecognizer recognizer = new OpenCvFaceRecognizer(classifier);

        File modelFile = new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/models/opencv-face.xml");

        if (modelFile.exists()) {
            System.out.println("load model file : " + modelFile);
            recognizer.load(modelFile);

        } else {
            File trainedDir = new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/trained");
            System.out.println("training model by " + trainedDir + " ...");
            recognizer.train(trainedDir);

            System.out.println("save model file : " + modelFile);
            recognizer.save(modelFile);
        }


        List<String> labelNameList = recognizer.getLabelNameList();
        System.out.print("labelNameList:");
        for (int i = 0; i < labelNameList.size(); i++) {
            System.out.print(i + ":" + labelNameList.get(i) + ", ");
        }
        System.out.println();


        if (args.length == 0) {
            File testDir = new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/testing");
            if (!testDir.exists()) {
                testDir.mkdirs();
            }

            recognizer.test(testDir);

            Map.Entry<String, Rectangle> result = recognizer.predictAsLabelAndMarkRect(
                    new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/testing/s7/4.pgm.verify.png"),
                    new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/testing/s7/4.pgm.mark.png"));
            System.out.println(result);
        } else {
            for (String arg : args) {
                System.out.println("--------------------------------");
                File file = new File(arg);
                if (!file.exists() || !file.isFile()) {
                    System.out.println("file not found! " + arg);
                    continue;
                }

                File saveFile = new File(file.getParentFile(), file.getName() + ".mark.png");
                Map.Entry<String, Rectangle> result = recognizer.predictAsLabelAndMarkRect(file, saveFile);
                System.out.println("result: " + result);
                System.out.println("mark file: " + saveFile);
            }
        }
        recognizer.close();
    }
}
