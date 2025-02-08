package i2f.extension.opencv.javacv;

import i2f.extension.opencv.data.OpenCvDataFileProvider;
import i2f.io.file.FileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/7/31 10:26
 * @desc 使用 opencv 进行人脸检测与识别
 * 默认使用LBPH进行人脸识别
 * 训练train和测试test均使用图片目录结构
 * 目录结构一致
 * 训练数据目录下的每个子目录
 * 子目录名称就是label的名称
 * 子目录中的文件就是人脸图片
 * 所以，目录结构大致这样
 * trained
 * tom
 * tom-01.png
 * tom-02.jpg
 * jack
 * jack-01.png
 * jack-02.jpeg
 * 另外，支持模型的保存save和载入load
 * 以模型文件 test.xml 为例
 * 则会产生如下文件
 * test.xml 模型文件
 * test.labels.txt 标签文件
 * test.properties 配置文件
 * 可以根据实际需求，调整LBPH为Fisher或者其他人脸识别引擎
 * 同时根据需要变更CascadeClassifier为其他的人脸检测引擎
 */
@Data
@NoArgsConstructor
public class OpenCvFaceRecognizer implements Closeable {
    public static final String DEFAULT_FRONTFACE_CASCASE_XML = "haarcascades/haarcascade_frontalface_default.xml";
    private CascadeClassifier classifier = new CascadeClassifier(OpenCvDataFileProvider.getClasspathOpenCvDataFile(DEFAULT_FRONTFACE_CASCASE_XML).getAbsolutePath());
    private FaceRecognizer recognizer = LBPHFaceRecognizer.create();
    private List<String> labelNameList = new ArrayList<>();
    private int width = 256;
    private int height = 256;
    private double confidenceLimit = 40;

    public OpenCvFaceRecognizer(CascadeClassifier classifier) {
        this.classifier = classifier;
    }

    public OpenCvFaceRecognizer(CascadeClassifier classifier, FaceRecognizer recognizer) {
        this.classifier = classifier;
        this.recognizer = recognizer;
    }

    public File getModelLabelFile(File modelFile) {
        File modelLabelFile = new File(modelFile.getParentFile(), modelFile.getName() + ".labels.txt");
        String fileName = modelFile.getName();
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            String name = fileName.substring(0, idx);
            modelLabelFile = new File(modelFile.getParentFile(), name + ".label.txt");
        }
        return modelLabelFile;
    }

    public File getModelProprtiesFile(File modelFile) {
        File modelPropertiesFile = new File(modelFile.getParentFile(), modelFile.getName() + ".properties");
        String fileName = modelFile.getName();
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            String name = fileName.substring(0, idx);
            modelPropertiesFile = new File(modelFile.getParentFile(), name + ".properties");
        }
        return modelPropertiesFile;
    }

    public void load(File modelFile) throws IOException {
        FileUtil.useParentDir(modelFile);
        File modelLabelFile = getModelLabelFile(modelFile);
        recognizer.read(modelFile.getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelLabelFile), "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                labelNameList.add(line);
            }
        }
        File modelProprtiesFile = getModelProprtiesFile(modelFile);
        try (InputStream fis = new FileInputStream(modelProprtiesFile)) {
            Properties properties = new Properties();
            properties.load(fis);
            this.width = Integer.valueOf(properties.getProperty("width", "256"));
            this.height = Integer.valueOf(properties.getProperty("height", "256"));
            this.confidenceLimit = Double.valueOf(properties.getProperty("confidence.limit", "40"));
        }
    }

    public void save(File modelFile) throws IOException {
        FileUtil.useParentDir(modelFile);
        File modelLabelFile = getModelLabelFile(modelFile);
        recognizer.write(modelFile.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelLabelFile), "UTF-8"))) {
            for (String item : labelNameList) {
                writer.write(item);
                writer.newLine();
            }
        }
        File modelProprtiesFile = getModelProprtiesFile(modelFile);
        try (OutputStream fos = new FileOutputStream(modelProprtiesFile)) {
            Properties properties = new Properties();
            properties.put("width", String.valueOf(this.width));
            properties.put("height", String.valueOf(this.height));
            properties.put("confidence.limit", String.valueOf(this.confidenceLimit));
            properties.store(fos, null);
        }
    }

    public void train(File trainDir) {
        train(trainDir, false);
    }

    public void train(File trainDir, boolean saveTrainImg) {

        Map<String, List<File>> labelFilesMap = new LinkedHashMap<>();

        File[] labelDirs = trainDir.listFiles();
        if (labelDirs == null || labelDirs.length == 0) {
            return;
        }

        for (File labelDir : labelDirs) {
            if (!labelDir.isDirectory()) {
                continue;
            }
            File[] files = labelDir.listFiles();
            if (files == null || files.length == 0) {
                continue;
            }
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
        for (Map.Entry<String, List<File>> entry : labelFilesMap.entrySet()) {
            String labelName = entry.getKey();
            List<File> files = entry.getValue();
            if (files == null || files.isEmpty()) {
                continue;
            }
            int labelIndex = labelNameList.indexOf(labelName);
            if (labelIndex < 0) {
                labelIndex = labelNameList.size();
                labelNameList.add(labelName);
            }
            for (File file : files) {
                Mat submat = getFaceRecognizeMat(file);

                if (saveTrainImg) {
                    File pngFile = new File(file.getParentFile(), file.getName() + ".train.png");

                    opencv_imgcodecs.imwrite(pngFile.getAbsolutePath(), submat);
                }
                matList.add(submat);
                labelList.add(labelIndex);
            }

        }

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
        for (Mat mat : matList) {
            mat.close();
        }
        vector.close();
        labelMat.close();

    }

    public void test(File testDir) {
        test(testDir, false);
    }

    public void test(File testDir, boolean saveTestImg) {
        File[] labelDirs = testDir.listFiles();
        if (labelDirs == null || labelDirs.length == 0) {
            return;
        }
        for (File labelDir : labelDirs) {
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

                Mat submat = getFaceRecognizeMat(testFile);

                if (saveTestImg) {
                    File pngFile = new File(testFile.getParentFile(), testFile.getName() + ".test.png");

                    opencv_imgcodecs.imwrite(pngFile.getAbsolutePath(), submat);
                }

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
    }

    public Map.Entry<String, Rectangle> predictAsLabelWithRect(File imgFile) {
        Map.Entry<Mat, Rectangle> entry = getFaceRecognizeMatWithRect(imgFile);
        Mat submat = entry.getKey();
        String label = predictAsLabel(submat);
        submat.close();
        return new AbstractMap.SimpleEntry<>(label, entry.getValue());
    }

    public Map.Entry<String, Rectangle> predictAsLabelAndMarkRect(File imgFile, File markFile) {
        Mat mat = opencv_imgcodecs.imread(imgFile.getAbsolutePath());
        Map.Entry<Mat, Rectangle> entry = getFaceRecognizeMatWithRect(mat);
        Rectangle rect = entry.getValue();
        if (rect != null) {
            Rect rct = new Rect(rect.x, rect.y, rect.width, rect.height);
            opencv_imgproc.rectangle(mat, rct, new Scalar(0, 0, 256, 256));
            opencv_imgcodecs.imwrite(markFile.getAbsolutePath(), mat);
        }
        mat.close();
        Mat submat = entry.getKey();
        String label = predictAsLabel(submat);
        submat.close();
        return new AbstractMap.SimpleEntry<>(label, entry.getValue());
    }

    public String predictAsLabel(File imgFile) {
        Mat submat = getFaceRecognizeMat(imgFile);
        String label = predictAsLabel(submat);
        submat.close();
        return label;
    }

    public String predictAsLabel(Mat imgMat) {
        Map.Entry<String, Double> entry = predictAs(imgMat);
        return entry.getKey();
    }

    public Map.Entry<String, Double> predictAs(File imgFile) {
        Mat submat = getFaceRecognizeMat(imgFile);
        Map.Entry<String, Double> entry = predictAs(submat);
        submat.close();
        return entry;
    }

    public Map.Entry<String, Double> predictAs(Mat imgMat) {
        Map.Entry<int[], double[]> entry = predict(imgMat);
        int[] labels = entry.getKey();
        double[] confidences = entry.getValue();
        boolean result = confidences[0] <= confidenceLimit && confidences[0] > -0.5;
        return new AbstractMap.SimpleEntry<>(result ? labelNameList.get(labels[0]) : null, confidences[0]);
    }

    public Map.Entry<int[], double[]> predict(File imgFile) {
        Mat submat = getFaceRecognizeMat(imgFile);
        Map.Entry<int[], double[]> entry = predict(submat);
        submat.close();
        return entry;
    }

    public Map.Entry<int[], double[]> predict(Mat imgMat) {

        int[] labels = new int[3];
        Arrays.fill(labels, -1);
        double[] confidences = new double[3];
        Arrays.fill(confidences, -1.0);
        recognizer.predict(imgMat, labels, confidences);

        return new AbstractMap.SimpleEntry<>(labels, confidences);
    }

    public Mat getFaceRecognizeMat(File imgFile) {
        return getFaceRecognizeMatWithRect(imgFile).getKey();
    }

    public Map.Entry<Mat, Rectangle> getFaceRecognizeMatWithRect(File imgFile) {
        Mat mat = opencv_imgcodecs.imread(imgFile.getAbsolutePath());
        Map.Entry<Mat, Rectangle> entry = getFaceRecognizeMatWithRect(mat);

        mat.close();

        return entry;
    }

    public Map.Entry<Mat, Rectangle> getFaceRecognizeMatWithRect(Mat mat) {

        Mat grayMat = mat.clone();

        opencv_imgproc.cvtColor(mat, grayMat, opencv_imgproc.COLOR_BGR2GRAY);

        RectVector rects = new RectVector();
        classifier.detectMultiScale(grayMat, rects);

        Rect[] arr = rects.get();
        Rectangle rectangle = null;

        Mat submat = grayMat;

        if (arr.length > 0) {
            submat = new Mat(grayMat, arr[0]);
            grayMat.close();
            rectangle = new Rectangle(arr[0].x(), arr[0].y(), arr[0].width(), arr[0].height());
        }

        rects.close();

        opencv_imgproc.resize(submat, submat, new Size(width, height));

        return new AbstractMap.SimpleEntry<>(submat, rectangle);
    }


    @Override
    public void close() throws IOException {
        this.recognizer.close();
        this.classifier.close();
    }
}
