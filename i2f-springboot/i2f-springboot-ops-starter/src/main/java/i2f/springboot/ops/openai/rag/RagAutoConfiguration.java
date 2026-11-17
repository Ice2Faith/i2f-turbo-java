package i2f.springboot.ops.openai.rag;

import i2f.ai.std.rag.BucketRagEmbeddingStore;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.springboot.ai.rags.AiRagsAutoConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/7/3 11:37
 * @desc
 */
@ConditionalOnExpression("${ai.rags.enable:true}")
@Slf4j
@Data
@Configuration
@AutoConfigureAfter(AiRagsAutoConfiguration.class)
public class RagAutoConfiguration {

    @ConditionalOnExpression("${ai.rags.memory.enable:true}")
    @ConditionalOnMissingBean(MemoryTools.class)
    public MemoryTools memoryTools(@Autowired RagEmbeddingModel ragEmbeddingModel,
                                   @Autowired BucketRagEmbeddingStore bucketRagEmbeddingStore) {
        return new MemoryTools(ragEmbeddingModel, bucketRagEmbeddingStore);
    }

}
