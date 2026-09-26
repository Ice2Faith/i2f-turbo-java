package i2f.ai.std.rag;

import java.io.File;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/7/14 18:36
 * @desc
 */
public interface RagFileReader {
    boolean support(File file);

    String read(File file) throws IOException;
}
