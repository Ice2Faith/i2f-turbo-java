package i2f.extension.ocr.tesseract;

import i2f.std.consts.StdConst;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/7/24 18:45
 * @desc
 */
public class OcrTesseractProvider {
    public static final String TRAINED_DATA_DOWNLOAD_URL = "https://github.com/tesseract-ocr/tessdata";
    public static final String TRAINED_DATA_FILE = "./" + StdConst.RUNTIME_PERSIST_DIR + "/tesseract/ocr";
    public static final String TRAINED_DATA_SUFFIX = ".traineddata";
    public static final String[] LANGUAGES = {
            "chi_sim",
            "chi_tra",
            "eng"
    };

    public static Tesseract getDefaultTesseract() throws TesseractException {
        return getTesseract(LANGUAGES[0]);
    }

    public static Tesseract getTesseract(String lang) throws TesseractException {
        File trainedDataDir = new File(TRAINED_DATA_FILE);
        if (!trainedDataDir.exists()) {
            trainedDataDir.mkdirs();
            throw new TesseractException("please copy you needs *" + TRAINED_DATA_SUFFIX + " files into " + TRAINED_DATA_FILE + ", which cloud download from " + TRAINED_DATA_DOWNLOAD_URL);
        }
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(trainedDataDir.getAbsolutePath());
        tesseract.setLanguage(lang);
        return tesseract;
    }

    public static String recognize(InputStream imgStream) throws TesseractException {
        try {
            BufferedImage img = ImageIO.read(imgStream);
            return recognize(img);
        } catch (IOException e) {
            throw new TesseractException(e.getMessage(), e);
        }
    }

    public static String recognize(BufferedImage img) throws TesseractException {
        Tesseract tesseract = getDefaultTesseract();
        String ret = tesseract.doOCR(img);
        return ret;
    }

    public static String recognize(File imgFile) throws TesseractException {
        Tesseract tesseract = getDefaultTesseract();
        String ret = tesseract.doOCR(imgFile);
        return ret;
    }


}
