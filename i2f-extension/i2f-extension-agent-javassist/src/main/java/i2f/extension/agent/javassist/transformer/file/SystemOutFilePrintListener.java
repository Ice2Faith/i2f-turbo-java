package i2f.extension.agent.javassist.transformer.file;

import java.io.File;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemOutFilePrintListener implements Predicate<File> {
    public static final SystemOutFilePrintListener INSTANCE = new SystemOutFilePrintListener();

    @Override
    public boolean test(File file) {
        System.out.println("=============================\nfile====" + file);
        return true;
    }
}
