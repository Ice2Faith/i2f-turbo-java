package i2f.ai.std.rag;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/25 11:53
 * @desc
 */
@Data
@NoArgsConstructor
public class RagEmbedding {
    protected String id;
    protected RagVector vector;
    protected String content;
    protected double score;
    protected Map<String, Object> metadata = new LinkedHashMap<>();

    public static int compareByScoreDesc(RagEmbedding v1, RagEmbedding v2) {
        return Double.compare(v2.getScore(), v1.getScore());
    }

    public RagEmbedding copy() {
        RagEmbedding ret = new RagEmbedding();
        ret.setId(this.getId());
        ret.setVector(this.getVector().copy());
        ret.setContent(this.getContent());
        ret.setScore(this.getScore());
        ret.setMetadata(new LinkedHashMap<>());
        Map<String, Object> map = this.getMetadata();
        if (map != null) {
            ret.getMetadata().putAll(map);
        }
        return ret;
    }
}
