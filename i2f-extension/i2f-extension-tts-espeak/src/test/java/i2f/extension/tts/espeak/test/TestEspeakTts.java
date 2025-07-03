package i2f.extension.tts.espeak.test;

import i2f.extension.tts.espeak.TtsEspeakProvider;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2024/7/24 14:23
 * @desc
 */
public class TestEspeakTts {
    public static void main(String[] args) throws Exception {
        String str = "hello everyone! here is e-speak text to speech test !\n" +
                "大家好！这里是 e-speak 文本转语音测试！";
        TtsEspeakProvider.text2speech(str);

        File file = new File("./tmp.wav");
        TtsEspeakProvider.text2speech(str, file);
    }
}
