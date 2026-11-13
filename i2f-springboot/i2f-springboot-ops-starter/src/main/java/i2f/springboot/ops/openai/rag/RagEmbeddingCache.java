package i2f.springboot.ops.openai.rag;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.std.consts.StdConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/11/13 9:45
 * @desc
 */
public class RagEmbeddingCache {
    protected final String type;
    protected final File saveFile;
    protected final LruMap<String, RagEmbedding> cache = new LruMap<>(8192);
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    public RagEmbeddingCache(String type) {
        this.type = type;
        this.saveFile = new File(StdConst.RUNTIME_TMP_DIR + "/embedding-cache/" + type + ".json");
    }


    @Data
    @NoArgsConstructor
    public static class EmbeddingPojo {
        protected List<RagEmbedding> list;
    }


    public void store() throws IOException {
        File parentFile = new File(this.saveFile.getAbsolutePath()).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        EmbeddingPojo pojo = new EmbeddingPojo();
        pojo.setList(new ArrayList<>(cache.values()));

        String json = jsonSerializer.serialize(pojo);
        StreamUtil.writeString(json, saveFile);
    }

    public void load() throws IOException {
        File parentFile = new File(this.saveFile.getAbsolutePath()).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String json = StreamUtil.readString(saveFile);
        EmbeddingPojo pojo = (EmbeddingPojo) jsonSerializer.deserialize(json, EmbeddingPojo.class);

        List<RagEmbedding> list = pojo.getList();
        for (RagEmbedding item : list) {
            cache.put(item.getContent(), item);
        }
    }

    public RagEmbeddingCache run() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    store();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("embedding-cache-flusher");
        thread.start();
        return this;
    }

    public RagEmbedding embed(RagEmbeddingModel model, String text) {
        RagEmbedding embedding = cache.get(text);
        if (embedding != null) {
            return embedding;
        }
        embedding = model.embed(text);
        cache.put(text, embedding);
        return embedding;
    }
}
