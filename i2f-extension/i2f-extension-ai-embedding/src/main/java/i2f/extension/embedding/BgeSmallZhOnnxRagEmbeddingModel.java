package i2f.extension.embedding;

import i2f.extension.embedding.core.OnnxBertBiEncoder;
import i2f.extension.embedding.core.PoolingMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2026/9/30 16:33
 * @desc
 */
public class BgeSmallZhOnnxRagEmbeddingModel extends OnnxRagEmbeddingModel {
    public BgeSmallZhOnnxRagEmbeddingModel() {
        super(OnnxBertBiEncoder.of(getResource("assets/onnx/bge-small-zh/bge-small-zh.onnx"),
                getResource("assets/onnx/bge-small-zh/tokenizer.json"),
                PoolingMode.CLS)
        );
    }

    public static InputStream getResource(String resourceName) {
        try {
            File file = new File("./" + resourceName);
            if (file.exists()) {
                return new FileInputStream(file);
            }
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream ret = loader.getResourceAsStream("/" + resourceName);
            if(ret==null){
                // not found, try use lanchain4j path
                int idx=resourceName.lastIndexOf("/");
                if(idx>=0){
                    resourceName=resourceName.substring(idx+1);
                }
                ret=loader.getResourceAsStream("/"+resourceName);
            }
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
