package i2f.extension.ai.dashscope.rag.rerank;

import com.alibaba.dashscope.rerank.TextReRank;
import com.alibaba.dashscope.rerank.TextReRankOutput;
import com.alibaba.dashscope.rerank.TextReRankParam;
import com.alibaba.dashscope.rerank.TextReRankResult;
import i2f.ai.std.rag.rerank.RagRerankModel;
import i2f.ai.std.rag.rerank.data.RagRerankDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/9 8:50
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeAiRerankModel implements RagRerankModel {
    protected String model = TextReRank.Models.GTE_RERANK_V2;

    public DashScopeAiRerankModel model(String model) {
        this.model = model;
        return this;
    }

    @Override
    public List<RagRerankDocument> rerank(String question, List<String> contents, int topN) {
        try {
            TextReRank reRank = new TextReRank();
            TextReRankResult result = reRank.call(TextReRankParam.builder()
                    .model(model)
                    .query(question)
                    .documents(contents)
                    .topN(topN)
                    .build());
            TextReRankOutput output = result.getOutput();
            List<TextReRankOutput.Result> results = output.getResults();
            List<RagRerankDocument> ret = new ArrayList<>();
            int n = 0;
            for (TextReRankOutput.Result item : results) {
                TextReRankOutput.Document document = item.getDocument();
                String text = document.getText();

                RagRerankDocument doc = new RagRerankDocument();
                doc.setText(text);
                doc.setIndex(item.getIndex());
                doc.setScore(item.getRelevanceScore());
                ret.add(doc);

                n++;
                if (n >= topN) {
                    break;
                }
            }
            return ret;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
