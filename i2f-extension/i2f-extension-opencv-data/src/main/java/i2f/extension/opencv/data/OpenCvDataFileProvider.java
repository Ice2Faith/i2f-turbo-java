package i2f.extension.opencv.data;

import i2f.io.file.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/7/31 14:09
 * @desc
 */
public class OpenCvDataFileProvider {
    public static final String ROOT_PATH = "./opencv";
    public static final String DATA_PATH = ROOT_PATH + "/data";

    public static File getClasspathOpenCvDataFile(String fontFaceXmlName) {
        try {
            return FileUtil.getClasspathExtraFile(new File(DATA_PATH), "lib/data/" + fontFaceXmlName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
