package i2f.extension.tts.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import i2f.io.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/24 15:27
 * @desc
 */
public class TtsJacobProvider {
    public static final String OFFICIAL_URL = "https://github.com/freemansoft/jacob-project";

    public static final String[] CLASS_PATHS = {
            "lib/jacob-1.21-x64.dll",
            "lib/jacob-1.21-x86.dll"
    };
    public static final String DLL_PATH = "./jacob";
    private static final AtomicBoolean initialed = new AtomicBoolean(false);

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() throws IOException {
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, new File(DLL_PATH, LibraryLoader.getPreferredDLLName() + ".dll").getAbsolutePath());
        if (initialed.getAndSet(true)) {
            return;
        }
        File releaseDir = new File(DLL_PATH);
        if (releaseDir.exists()) {
            return;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String path : CLASS_PATHS) {
            InputStream is = loader.getResourceAsStream(path);
            File saveFile = new File(releaseDir, new File(path).getName());
            FileUtil.save(is, saveFile);
            is.close();
        }
        System.out.println("please copy *.dll files to your system %PATH% variable directory from " + DLL_PATH + ".");
    }

    public static void text2speech(String str) throws Exception {
        /*1. 创建音响对象*/
        ActiveXComponent activeXComponent = new ActiveXComponent("Sapi.SpVoice");

        /*2. 设置音量（0~100）*/
        activeXComponent.setProperty("Volume", new Variant(80));

        /*3. 设置朗读速度（-10~10）*/
        activeXComponent.setProperty("Rate", new Variant(2));

        /*4. 选取读取对象*/
        Dispatch dispatch = activeXComponent.getObject();

        /*5. 执行朗读*/
        Dispatch.call(dispatch, "Speak", new Variant(str));

        /*6. 关闭对象和音响*/
        dispatch.safeRelease();
        activeXComponent.safeRelease();
    }

    public static void text2speech(String str, File wavFile) throws Exception {
        // 调用dll朗读方法
        ActiveXComponent ax = new ActiveXComponent("Sapi.SpVoice");
        // 输入的语言内容
        Dispatch dispatch = ax.getObject();

        //开始生成语音文件，构建文件流
        ax = new ActiveXComponent("Sapi.SpFileStream");
        Dispatch sfFileStream = ax.getObject();
        //设置文件生成格式
        ax = new ActiveXComponent("Sapi.SpAudioFormat");
        Dispatch fileFormat = ax.getObject();

        // 设置音频流格式
        Dispatch.put(fileFormat, "Type", new Variant(22));
        // 设置文件输出流格式
        Dispatch.putRef(sfFileStream, "Format", fileFormat);

        // 调用输出文件流打开方法，创建一个音频文件
        Dispatch.call(sfFileStream, "Open", new Variant(wavFile.getAbsolutePath()), new Variant(3), new Variant(true));
        // 设置声音对应输出流为输出文件对象
        Dispatch.putRef(dispatch, "AudioOutputStream", sfFileStream);

        // 设置音量
        Dispatch.put(dispatch, "Volume", new Variant(80));
        // 设置速度
        Dispatch.put(dispatch, "Rate", new Variant(2));
        // 执行朗读
        Dispatch.call(dispatch, "Speak", new Variant(str));
        // 关闭输出文件
        Dispatch.call(sfFileStream, "Close");

        // 关闭资源
        sfFileStream.safeRelease();
        fileFormat.safeRelease();
        // 关闭朗读的操作
        dispatch.safeRelease();
        ax.safeRelease();

    }
}
