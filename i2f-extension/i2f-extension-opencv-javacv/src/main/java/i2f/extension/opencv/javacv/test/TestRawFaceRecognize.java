package i2f.extension.opencv.javacv.test;

import i2f.extension.opencv.data.OpenCvDataFileProvider;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import java.io.*;
import java.util.Arrays;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/7/30 18:39
 * @desc
 */
public class TestRawFaceRecognize {
    public static void main(String[] args) throws IOException {
        File modelFile = new File("./models/face.model.xml");
        File modelLabelFile = new File("./models/face.model.labels.txt");
        if (!modelFile.getParentFile().exists()) {
            modelFile.getParentFile().mkdirs();
        }

        File xmlFile = OpenCvDataFileProvider.getClasspathOpenCvDataFile("haarcascades/haarcascade_frontalface_default.xml");

        CascadeClassifier classifier = new CascadeClassifier(xmlFile.getAbsolutePath());


        FaceRecognizer recognizer = LBPHFaceRecognizer.create();
        List<String> labelNameList = new ArrayList<>();

        if (modelFile.exists() && modelFile.isFile()) {
            recognizer.read(modelFile.getAbsolutePath());
            System.out.println("load model file: " + modelFile);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelLabelFile), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                labelNameList.add(line);
            }
            reader.close();
        } else {
            File dir = new File("./trained");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            Map<String, List<File>> labelFilesMap = new LinkedHashMap<>();

            File[] labelDirs = dir.listFiles();
            for (File labelDir : labelDirs) {
                if (!labelDir.isDirectory()) {
                    continue;
                }
                File[] files = labelDir.listFiles();
                if (files == null || files.length == 0) {
                    continue;
                }
                labelNameList.add(labelDir.getName());
                labelFilesMap.put(labelDir.getName(), new ArrayList<>());
                for (File imgFile : files) {
                    if (!imgFile.isFile()) {
                        continue;
                    }
                    labelFilesMap.get(labelDir.getName()).add(imgFile);
                }
            }

            List<Mat> matList = new ArrayList<>();
            List<Integer> labelList = new ArrayList<>();
            for (int i = 0; i < labelNameList.size(); i++) {
                String labelName = labelNameList.get(i);
                List<File> files = labelFilesMap.get(labelName);
                if (files == null || files.isEmpty()) {
                    continue;
                }
                for (File file : files) {
                    Mat mat = opencv_imgcodecs.imread(file.getAbsolutePath());

                    Mat grayMat = mat.clone();

                    opencv_imgproc.cvtColor(mat, grayMat, opencv_imgproc.COLOR_BGR2GRAY);

                    mat.close();

                    RectVector rects = new RectVector();
                    classifier.detectMultiScale(grayMat, rects);

                    Rect[] arr = rects.get();

                    Mat submat = grayMat;

                    if (arr.length > 0) {
                        submat = new Mat(grayMat, arr[0]);
                        grayMat.close();
                    }

                    rects.close();


                    opencv_imgproc.resize(submat, submat, new Size(256, 256));

//                    File pngFile=new File(file.getParentFile(),file.getName()+".png");
//
//                    opencv_imgcodecs.imwrite(pngFile.getAbsolutePath(),submat);

                    matList.add(submat);
                    labelList.add(i);
                }

            }

            System.out.println("labelList:" + labelList);
            int[] labelArr = new int[labelList.size()];
            for (int i = 0; i < labelList.size(); i++) {
                labelArr[i] = labelList.get(i);
            }
            Mat labelMat = new Mat(labelArr);
            MatVector vector = new MatVector();
            for (Mat mat : matList) {
                vector.push_back(mat);
            }
            recognizer.train(vector, labelMat);
            vector.close();
            labelMat.close();
            for (Mat mat : matList) {
                mat.close();
            }

            recognizer.write(modelFile.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelLabelFile), "UTF-8"));
            for (String item : labelNameList) {
                writer.write(item);
                writer.newLine();
            }
            writer.close();
        }

        System.out.print("labelNameList:");

        for (int i = 0; i < labelNameList.size(); i++) {
            System.out.print(i + ":" + labelNameList.get(i) + ", ");
        }
        System.out.println();

        File testDir = new File("./testing");
        if (!testDir.exists()) {
            testDir.mkdirs();
        }
        for (File labelDir : testDir.listFiles()) {
            if (!labelDir.isDirectory()) {
                continue;
            }
            File[] files = labelDir.listFiles();
            if (files == null || files.length == 0) {
                continue;
            }
            String labelDirName = labelDir.getName();
            String testLabelName = labelDirName;
            if (testLabelName.startsWith("non-")) {
                testLabelName = testLabelName.substring("non-".length());
            }
            for (File testFile : files) {

                System.out.println("---------------------------------");
                System.out.println("testing:" + testLabelName + " / " + labelDirName + " / " + testFile.getName());

                Mat mat = opencv_imgcodecs.imread(testFile.getAbsolutePath());

                Mat grayMat = mat.clone();

                opencv_imgproc.cvtColor(mat, grayMat, opencv_imgproc.COLOR_BGR2GRAY);

                mat.close();

                RectVector rects = new RectVector();
                classifier.detectMultiScale(grayMat, rects);

                Rect[] arr = rects.get();

                Mat submat = grayMat;
                if (arr.length > 0) {
                    submat = grayMat.apply(arr[0]);
                    grayMat.close();
                }
                opencv_imgproc.resize(submat, submat, new Size(256, 256));

//                File pngFile = new File(testFile.getParentFile(), testFile.getName() + ".test.png");
//
//                opencv_imgcodecs.imwrite(pngFile.getAbsolutePath(), submat);


                rects.close();

                int[] labels = new int[3];
                Arrays.fill(labels, -1);
                double[] confidences = new double[3];
                Arrays.fill(confidences, -1.0);
                recognizer.predict(submat, labels, confidences);

                int preLabel = recognizer.predict_label(submat);

                submat.close();

                System.out.println("labels:" + Arrays.toString(labels));
                System.out.println("confidences:" + Arrays.toString(confidences));

                List<Map.Entry<Integer, Double>> list = new ArrayList<>();
                for (int i = 0; i < labels.length; i++) {
                    list.add(new AbstractMap.SimpleEntry<>(labels[i], confidences[i]));
                }

                System.out.println("list:" + list);

                Map.Entry<Integer, Double> entry = list.get(0);
                String confLabelName = labelNameList.get(entry.getKey());
                System.out.println("result:" + confLabelName + ": " + entry);

                boolean result = entry.getValue() <= 40 && entry.getValue() > -0.5;
                System.out.println("final:" + result);

                String preLabelName = labelNameList.get(preLabel);
                System.out.println("predict:" + preLabelName + ":" + preLabel);

                System.out.println("isNon:" + (!labelDirName.equals(testLabelName)));
                System.out.println(testLabelName.equals(confLabelName));
                System.out.println(testLabelName.equals(preLabelName));
            }
        }


        recognizer.close();
    }
}
