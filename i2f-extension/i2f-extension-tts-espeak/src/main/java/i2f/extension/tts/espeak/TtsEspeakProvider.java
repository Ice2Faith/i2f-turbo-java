package i2f.extension.tts.espeak;

import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import i2f.std.consts.StdConst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Ice2Faith
 * @date 2024/7/24 14:00
 * @desc
 */
public class TtsEspeakProvider {
    public static final String OFFICIAL_URL = "https://espeak.sourceforge.net/";

    public static final String CLASS_PATH = "windows/espeak.zip";
    public static final String BIN_PATH = "./" + StdConst.RUNTIME_PERSIST_DIR + "/espeak/windows";
    public static final String BIN_FILE = BIN_PATH + "/espeak.exe";
    public static final String RESOURCE_PATH = BIN_PATH + "/espeak-data";
    private static final AtomicBoolean initialed = new AtomicBoolean(false);

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() throws IOException {
        if (initialed.getAndSet(true)) {
            return;
        }
        File releaseDir = new File(BIN_PATH);
        if (releaseDir.exists()) {
            return;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream(CLASS_PATH);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                zis.closeEntry();
                continue;
            }
            String path = entry.getName();
            File saveFile = new File(releaseDir, path);
            FileUtil.useParentDir(saveFile);
            FileOutputStream fos = new FileOutputStream(saveFile);
            StreamUtil.streamCopy(zis, fos, true, false);
            zis.closeEntry();
        }
        zis.close();
    }

    public static void text2speech(String str) throws Exception {
        File txtFile = new File(BIN_PATH, FileUtil.getTempFileName() + ".txt");
        try {
            FileUtil.save(str, "UTF-8", txtFile);
            Process process = Runtime.getRuntime().exec(BIN_FILE + " --path=" + RESOURCE_PATH + "  -b 1 -v zh -f " + BIN_PATH + "/" + txtFile.getName());
            process.waitFor();
        } finally {
            txtFile.delete();
        }
    }

    public static void text2speech(String str, File wavFile) throws Exception {
        File txtFile = new File(BIN_PATH, FileUtil.getTempFileName() + ".txt");
        try {
            FileUtil.save(str, "UTF-8", txtFile);
            File tempFile = new File(BIN_PATH, FileUtil.getTempFileName() + ".wav");
            Process process = Runtime.getRuntime().exec(BIN_FILE + " --path=" + RESOURCE_PATH + "  -b 1 -v zh -f " + BIN_PATH + "/" + txtFile.getName() + " -w " + BIN_PATH + "/" + tempFile.getName());
            process.waitFor();
            FileUtil.useParentDir(wavFile);
            FileUtil.move(wavFile, tempFile);
        } finally {
            txtFile.delete();
        }

    }
}
