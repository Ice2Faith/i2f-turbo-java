package i2f.springboot.ops.openai.rag;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.openai.rag.HttpOpenAiRagEmbeddingModel;
import i2f.ai.std.rag.*;
import i2f.ai.std.rag.data.RagLoadDocumentsOptions;
import i2f.ai.std.rag.impl.ListableRagFileReader;
import i2f.ai.std.rag.impl.MarkitdownCmdRagFileReader;
import i2f.ai.std.rag.impl.PandocCmdRagFileReader;
import i2f.ai.std.rag.impl.SimpleRecursiveRagTextSplitter;
import i2f.extension.ai.rag.sqlite.SqliteRagEmbeddingStore;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.io.file.FileUtil;
import i2f.spring.web.rest.SpringWebRestClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/7/3 11:37
 * @desc
 */
@ConditionalOnExpression("${ai.rags.enable:true}")
@Slf4j
@Data
@Configuration
@EnableConfigurationProperties(RagEmbeddingModelProperties.class)
public class RagAutoConfiguration {

    @Autowired
    private RagEmbeddingModelProperties properties;


    @ConditionalOnMissingBean(RagEmbeddingModel.class)
    @Bean
    public RagEmbeddingModel ragEmbeddingModel() {
        return new HttpOpenAiRagEmbeddingModel().toMutator()
                .set(u -> u::setRestClient, new SpringWebRestClient(new RestTemplate()))
                .set(u -> u::setBaseUrl, properties.getBaseUrl())
                .set(u -> u::setApiKey, properties.getApiKey())
                .set(u -> u::setModel, properties.getModel())
                .done();
    }

    @ConditionalOnMissingBean(RagEmbeddingStore.class)
    @Bean
    public RagEmbeddingStore ragEmbeddingStore() {
        return new SqliteRagEmbeddingStore().toMutator()
                .set(u -> u::setDimension, properties.getDimension())
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .done();
    }

    @ConditionalOnMissingBean(RagWorker.class)
    @Bean
    public RagWorker ragWorker(@Autowired RagEmbeddingModel ragEmbeddingModel,
                               @Autowired RagEmbeddingStore ragEmbeddingStore) {
        RagWorker ret = new RagWorker().toMutator()
                .set(u -> u::setModel, ragEmbeddingModel)
                .set(u -> u::setStore, ragEmbeddingStore)
                .done();
        new Thread(() -> {
            try {
                loadDocs(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }).start();
        return ret;
    }

    @ConditionalOnMissingBean(RagTools.class)
    @Bean
    public RagTools ragTools(@Autowired RagWorker ragWorker) {
        return new RagTools(ragWorker);
    }

    public void loadDocs(RagWorker worker) throws Exception {
        File dir = new File(properties.getDocsPath());
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }
        RagHelper.loadDocuments(dir,
                worker,
                new RagLoadDocumentsOptions().toMutator()
                        .set(u -> u::setSplitter, new SimpleRecursiveRagTextSplitter().toMutator()
                                .set(u -> u::setMaxSegmentSizeInChars, properties.getMaxSegmentSizeInChars())
                                .set(u -> u::setMaxOverlapRate, properties.getMaxOverlapRate())
                                .apply(options -> {
                                    ListableRagFileReader reader = new ListableRagFileReader();
                                    if (properties.isEnableMarkitdownDocReader()) {
                                        reader.getReaders().add(MarkitdownCmdRagFileReader.INSTANCE);
                                    } else if (properties.isEnablePandocDocReader()) {
                                        reader.getReaders().add(PandocCmdRagFileReader.INSTANCE);
                                    }
                                    options.setFileReader(reader);
                                })
                                .done())
                        .set(u -> u::setStoreBatchSize, properties.getDocsEmbedBatchSize())
                        .done()
        );

        File historyDir = new File(dir.getParentFile(), "rags_history");
        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }
        File moveDir = new File(historyDir, "history-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        FileUtil.move(moveDir, dir);
        dir.mkdirs();
    }

}
