package i2f.springboot.ops.openai.rag;

import i2f.ai.std.rag.BucketRagEmbeddingStore;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.extension.ai.rag.sqlite.SqliteBucketRagMemoryStore;
import i2f.extension.ai.rag.sqlite.SqliteRagEmbeddingStore;
import i2f.jdbc.datasource.impl.DirectConnectionDatasource;
import i2f.springboot.ops.datasource.provider.DatasourceCollector;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 14:48
 * @desc
 */
@ConditionalOnExpression("${ai.rags.datasource.enable:true}")
@Data
@NoArgsConstructor
@Configuration
public class RagDataSourceCollector implements DatasourceCollector, ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Override
    public Map<String, DataSource> collect() {
        Map<String, DataSource> ret = new HashMap<>();
        try {
            Map<String, RagEmbeddingStore> map = applicationContext.getBeansOfType(RagEmbeddingStore.class);
            for (Map.Entry<String, RagEmbeddingStore> entry : map.entrySet()) {
                RagEmbeddingStore store = entry.getValue();
                if (store instanceof SqliteRagEmbeddingStore) {
                    SqliteRagEmbeddingStore item = (SqliteRagEmbeddingStore) store;
                    String dsName = "rag_" + entry.getKey();
                    ret.put(dsName, new DirectConnectionDatasource(item::getConnection));
                }
            }
        } catch (Exception e) {
            // ignore error
        }
        try {
            Map<String, BucketRagEmbeddingStore> map = applicationContext.getBeansOfType(BucketRagEmbeddingStore.class);
            for (Map.Entry<String, BucketRagEmbeddingStore> entry : map.entrySet()) {
                BucketRagEmbeddingStore store = entry.getValue();
                if (store instanceof SqliteBucketRagMemoryStore) {
                    SqliteBucketRagMemoryStore item = (SqliteBucketRagMemoryStore) store;
                    String dsName = "ragBucket_" + entry.getKey();
                    ret.put(dsName, new DirectConnectionDatasource(item::getConnection));
                }
            }
        } catch (Exception e) {
            // ignore error
        }
        return ret;
    }
}
