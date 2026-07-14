package i2f.ai.std.rag.data;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagFileReader;
import i2f.ai.std.rag.RagTextSplitter;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/7/3 14:06
 * @desc
 */
@Data
@NoArgsConstructor
public class RagLoadDocumentsOptions implements BaseMutator<RagLoadDocumentsOptions> {
    protected RagTextSplitter splitter;
    protected int storeBatchSize;
    protected Predicate<File> textFileFilter;
    protected Consumer<RagEmbedding> listener;
    protected RagFileReader fileReader;
}
