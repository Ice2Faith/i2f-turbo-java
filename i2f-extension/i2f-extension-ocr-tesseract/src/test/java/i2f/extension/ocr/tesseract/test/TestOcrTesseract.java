package i2f.extension.ocr.tesseract.test;

import i2f.extension.ocr.tesseract.OcrTesseractProvider;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2024/7/24 19:03
 * @desc
 */
public class TestOcrTesseract {
    public static void main(String[] args) throws TesseractException {
        String str = OcrTesseractProvider.recognize(new File("./tmp.png"));
        System.out.println(str);
    }
}
