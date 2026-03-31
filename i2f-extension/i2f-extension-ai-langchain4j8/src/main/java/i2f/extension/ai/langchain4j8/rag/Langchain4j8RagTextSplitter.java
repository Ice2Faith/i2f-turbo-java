package i2f.extension.ai.langchain4j8.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import i2f.ai.std.rag.RagTextSplitter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 9:53
 * @desc
 */
@Data
@NoArgsConstructor
public class Langchain4j8RagTextSplitter implements RagTextSplitter {
    protected int maxSegmentSizeInChars = 1024;
    protected int maxOverlapSizeInChars = 256;

    public Langchain4j8RagTextSplitter maxSegmentSizeInChars(int maxSegmentSizeInChars) {
        this.maxSegmentSizeInChars = maxSegmentSizeInChars;
        return this;
    }

    public Langchain4j8RagTextSplitter maxOverlapSizeInChars(int maxOverlapSizeInChars) {
        this.maxOverlapSizeInChars = maxOverlapSizeInChars;
        return this;
    }

    @Override
    public List<String> split(String text) {
        DocumentSplitter splitter = DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars);
        List<TextSegment> list = splitter.split(Document.document(text));
        List<String> ret = new ArrayList<>();
        for (TextSegment item : list) {
            ret.add(item.text());
        }
        return ret;
    }
}
