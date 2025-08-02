package i2f.extension.asr.vosk;

import i2f.std.consts.StdConst;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

/**
 * @author Ice2Faith
 * @date 2024/7/24 16:26
 * @desc
 */
public class AsrVoskProvider {
    public static final String MODELS_DOWNLOAD_URL = "https://alphacephei.com/vosk/models";
    public static final String MODEL_CN_SMALL_DOWNLOAD_URL = "https://alphacephei.com/vosk/models/vosk-model-small-cn-0.22.zip";
    public static final String MODEL_PATH = "./" + StdConst.RUNTIME_PERSIST_DIR + "/vosk/vosk-model-small-cn-0.22";
    public static Model model;

    public static void init() {
        init(null);
    }

    public static void init(String modelPath) {
        try {
            if (modelPath == null || modelPath.isEmpty()) {
                modelPath = MODEL_PATH;
            }
            File file = new File(modelPath);
            if (!file.exists()) {
                throw new IOException("model path not found, please ensure which is correct, or download model from " + MODELS_DOWNLOAD_URL);
            }
            if (model != null) {
                model.close();
            }
            model = new Model(MODEL_PATH);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                model.close();
            }));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String recognize(File audioFile) throws IOException {
        FileInputStream fis = new FileInputStream(audioFile);
        return recognize(fis);
    }

    public static String recognize(InputStream is) throws IOException {
        if (model == null) {
            init();
        }
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
             Recognizer recognizer = new Recognizer(model, 16000);
        ) {

            int len = 0;
            byte[] buff = new byte[4096];
            while ((len = ais.read(buff)) > 0) {
                recognizer.acceptWaveForm(buff, len);
            }
            String ret = recognizer.getFinalResult();
            return ret;
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e);
        }
    }

}
