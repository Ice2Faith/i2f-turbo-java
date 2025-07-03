package i2f.extension.asr.vosk.test;

import i2f.extension.asr.vosk.AsrVoskProvider;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/7/24 16:56
 * @desc
 */
public class TestAsrVosk {
    public static void main(String[] args) throws IOException {
        String str = AsrVoskProvider.recognize(new File("./tmp.wav"));
        System.out.println(str);
    }
}
