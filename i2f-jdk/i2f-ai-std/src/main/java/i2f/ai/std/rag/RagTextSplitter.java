package i2f.ai.std.rag;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 9:27
 * @desc
 */
public interface RagTextSplitter {
    List<String> split(String text);
}
