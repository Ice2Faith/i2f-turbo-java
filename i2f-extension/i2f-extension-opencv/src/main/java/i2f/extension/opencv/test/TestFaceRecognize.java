package i2f.extension.opencv.test;

import i2f.extension.opencv.OpenCvProvider;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import java.io.File;
import java.util.*;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/30 18:39
 * @desc
 */
public class TestFaceRecognize {
    public static void main(String[] args){
        File xmlFile = OpenCvProvider.getClasspathOpenCvDataFile("haarcascades/haarcascade_frontalface_default.xml");

        CascadeClassifier classifier=new CascadeClassifier(xmlFile.getAbsolutePath());

        FaceRecognizer recognizer= LBPHFaceRecognizer.create();

        File dir=new File("./trained");
        if(!dir.exists()){
            dir.mkdirs();
        }

        List<String> labelNameList=new ArrayList<>();
        Map<String,List<File>> labelFilesMap=new LinkedHashMap<>();

        File[] labelDirs = dir.listFiles();
        for (File labelDir : labelDirs) {
            if(!labelDir.isDirectory()){
                continue;
            }
            labelNameList.add(labelDir.getName());
            labelFilesMap.put(labelDir.getName(),new ArrayList<>());
            for (File imgFile : labelDir.listFiles()) {
                if(!imgFile.isFile()){
                    continue;
                }
                labelFilesMap.get(labelDir.getName()).add(imgFile);
            }
        }

        System.out.println(labelNameList);


        List<Mat> matList=new ArrayList<>();
        List<Integer> labelList=new ArrayList<>();
        for (int i = 0; i < labelNameList.size(); i++) {
            String labelName = labelNameList.get(i);
            List<File> files = labelFilesMap.get(labelName);
            if(files==null || files.isEmpty()){
                continue;
            }
            for (File file : files) {
                Mat mat = opencv_imgcodecs.imread(file.getAbsolutePath());

                Mat grayMat=mat.clone();

                opencv_imgproc.cvtColor(mat,grayMat,opencv_imgproc.COLOR_BGR2GRAY);

                mat.close();

                RectVector rects=new RectVector();
                classifier.detectMultiScale(grayMat,rects);

                Rect[] arr = rects.get();
                if(arr.length>0) {
                    Mat submat = new Mat(grayMat,arr[0]);

                    opencv_imgproc.resize(submat,submat,new Size(256,256));

                    matList.add(submat);
                    labelList.add(i);
                }

                rects.close();
            }

        }

        System.out.println(labelList);
        int[] labelArr=new int[labelList.size()];
        for (int i = 0; i < labelList.size(); i++) {
            labelArr[i]=labelList.get(i);
        }
        Mat labelMat=new Mat(labelArr);
        MatVector vector=new MatVector();
        for (Mat mat : matList) {
            vector.push_back(mat);
        }
        recognizer.train(vector,labelMat);
        vector.close();
        labelMat.close();
        for (Mat mat : matList) {
            mat.close();
        }

        File testFile=new File("./testing/s10/4.pgm");
        Mat mat = opencv_imgcodecs.imread(testFile.getAbsolutePath());

        Mat grayMat=mat.clone();

        opencv_imgproc.cvtColor(mat,grayMat,opencv_imgproc.COLOR_BGR2GRAY);

        mat.close();

        RectVector rects=new RectVector();
        classifier.detectMultiScale(grayMat,rects);

        Mat submat = grayMat.apply(rects.get()[0]);
        opencv_imgproc.resize(submat,submat,new Size(256,256));

        rects.close();

        int[] labels=new int[labelNameList.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i]=i;
        }
        double[] confidences=new double[labelNameList.size()];
        recognizer.predict(submat,labels,confidences);

        submat.close();

        System.out.println(Arrays.toString(labels));
        System.out.println(Arrays.toString(confidences));

        List<Map.Entry<Integer,Double>> list=new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            list.add(new AbstractMap.SimpleEntry<>(labels[i],confidences[i]));
        }

        list.sort((e1,e2)->Double.compare(e1.getValue(),e2.getValue()));

        System.out.println(list);

        Map.Entry<Integer, Double> entry = list.get(0);
        System.out.println(labelNameList.get(entry.getKey())+": "+entry);
    }
}
