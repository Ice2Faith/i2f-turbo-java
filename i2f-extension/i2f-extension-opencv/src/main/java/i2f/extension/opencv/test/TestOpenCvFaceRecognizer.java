package i2f.extension.opencv.test;

import i2f.extension.opencv.OpenCvFaceRecognizer;
import i2f.extension.opencv.OpenCvProvider;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/31 11:09
 * @desc
 */
public class TestOpenCvFaceRecognizer {
    public static void main(String[] args) throws IOException {
        File xmlFile = OpenCvProvider.getClasspathOpenCvDataFile("haarcascades/haarcascade_frontalface_default.xml");

        CascadeClassifier classifier = new CascadeClassifier(xmlFile.getAbsolutePath());

        OpenCvFaceRecognizer recognizer = new OpenCvFaceRecognizer(classifier);

        File modelFile = new File("./models/opencv-face.xml");

        if (modelFile.exists()) {
            System.out.println("load model file : " + modelFile);
            recognizer.load(modelFile);

        } else {
            File trainedDir = new File("./trained");
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


        File testDir = new File("./testing");
        if (!testDir.exists()) {
            testDir.mkdirs();
        }

        recognizer.test(testDir);

        recognizer.predictAsLabelAndMarkRect(new File("./testing/s7/4.pgm.png"), new File("./testing/s7/4.pgm.mark.png"));

        recognizer.close();
    }
}
