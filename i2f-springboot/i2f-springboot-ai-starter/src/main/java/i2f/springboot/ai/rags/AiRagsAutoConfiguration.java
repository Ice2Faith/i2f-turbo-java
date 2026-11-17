package i2f.springboot.ai.rags;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.openai.rag.HttpOpenAiRagEmbeddingModel;
import i2f.ai.std.rag.*;
import i2f.ai.std.rag.data.RagLoadDocumentsOptions;
import i2f.ai.std.rag.impl.*;
import i2f.extension.ai.rag.sqlite.SqliteBucketRagMemoryStore;
import i2f.extension.ai.rag.sqlite.SqliteRagDocumentsStore;
import i2f.extension.ai.rag.sqlite.SqliteRagEmbeddingStore;
import i2f.extension.document.pdf.PdfConvertUtil;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.io.file.FileUtil;
import i2f.spring.core.SpringContext;
import i2f.spring.web.rest.SpringWebRestClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/11/17 16:43
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.rags.enable:true}")
@Configuration
@Slf4j
@Data
@EnableConfigurationProperties(AiRagsEmbeddingModelProperties.class)
public class AiRagsAutoConfiguration implements ApplicationContextAware {

    @Autowired
    private AiRagsEmbeddingModelProperties properties;

    private ApplicationContext applicationContext;

    @ConditionalOnExpression("${i2f.springboot.ai.rags.embedding.enable:true}")
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

    @ConditionalOnExpression("${i2f.springboot.ai.rags.store.enable:true}")
    @ConditionalOnMissingBean(RagEmbeddingStore.class)
    @Bean
    public RagEmbeddingStore ragEmbeddingStore() {
        return new SqliteRagEmbeddingStore().toMutator()
                .set(u -> u::setDimension, properties.getDimension())
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .done();
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.docs-store.enable:true}")
    @ConditionalOnMissingBean(RagDocumentsStore.class)
    @Bean
    public RagDocumentsStore ragDocumentsStore() {
        return new SqliteRagDocumentsStore().toMutator()
                .done();
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.worker.enable:true}")
    @ConditionalOnMissingBean(RagWorker.class)
    @Bean
    public RagWorker ragWorker(@Autowired RagEmbeddingModel ragEmbeddingModel,
                               @Autowired RagEmbeddingStore ragEmbeddingStore,
                               @Autowired(required = false) RagDocumentsStore ragDocumentsStore) {
        RagWorker ret = new RagWorker().toMutator()
                .set(u -> u::setModel, ragEmbeddingModel)
                .set(u -> u::setStore, ragEmbeddingStore)
                .set(u -> u::setDocumentsStore, ragDocumentsStore)
                .done();
        new Thread(() -> {
            try {
                Thread.sleep(3 * 1000);
                loadDocs(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }).start();
        return ret;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.tool.enable:true}")
    @ConditionalOnMissingBean(RagTools.class)
    @Bean
    public RagTools ragTools(@Autowired RagWorker ragWorker) {
        return new RagTools(ragWorker);
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.reader.markitdown.enable:true}")
    @Bean
    public MarkitdownCmdRagFileReader markitdownCmdRagFileReader() {
        return MarkitdownCmdRagFileReader.INSTANCE;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.reader.easycor.enable:true}")
    @Bean
    public EasyOcrCmdRagFileReader easyOcrCmdRagFileReader() {
        return EasyOcrCmdRagFileReader.INSTANCE;
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.reader.pdf-easycor.enable:true}")
    @ConditionalOnClass({
            PDDocument.class
    })
    @Bean
    public PdfEasyOcrCmdRagFileReader pdfEasyOcrCmdRagFileReader() {
        return new PdfEasyOcrCmdRagFileReader(PdfConvertUtil::pdf2images);
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.reader.pandoc.enable:true}")
    @Bean
    public PandocCmdRagFileReader pandocCmdRagFileReader() {
        return PandocCmdRagFileReader.INSTANCE;
    }

    public void loadDocs(RagWorker worker) throws Exception {
        File dir = new File(properties.getDocsPath());
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        RagHelper.loadDocuments(dir, null,
                worker,
                new RagLoadDocumentsOptions().toMutator()
                        .set(u -> u::setSplitter, new SimpleRecursiveRagTextSplitter().toMutator()
                                .set(u -> u::setMaxSegmentSizeInChars, properties.getMaxSegmentSizeInChars())
                                .set(u -> u::setMaxOverlapRate, properties.getMaxOverlapRate())
                                .done())
                        .set(u -> u::setStoreBatchSize, properties.getDocsEmbedBatchSize())
                        .apply(options -> {
                            ContextRagFileReader reader = new ContextRagFileReader(new SpringContext(applicationContext));
                            options.setFileReader(reader);
                        })
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

    @ConditionalOnExpression("${i2f.springboot.ai.rags.memory.bucket.enable:true}")
    @ConditionalOnMissingBean(BucketRagEmbeddingStore.class)
    @Bean
    public BucketRagEmbeddingStore bucketRagEmbeddingStore() {
        return new SqliteBucketRagMemoryStore().toMutator()
                .set(u -> u::setDimension, properties.getDimension())
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .done();
    }

    @ConditionalOnExpression("${i2f.springboot.ai.rags.memory.enable:true}")
    @ConditionalOnMissingBean(MemoryTools.class)
    public MemoryTools memoryTools(@Autowired RagEmbeddingModel ragEmbeddingModel,
                                   @Autowired BucketRagEmbeddingStore bucketRagEmbeddingStore) {
        return new MemoryTools(ragEmbeddingModel, bucketRagEmbeddingStore);
    }

}
