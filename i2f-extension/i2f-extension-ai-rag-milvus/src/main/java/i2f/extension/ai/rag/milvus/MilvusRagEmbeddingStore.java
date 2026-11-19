package i2f.extension.ai.rag.milvus;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagVector;
import i2f.mutator.BaseMutator;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.index.request.CreateIndexReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/11/19 8:32
 * @desc
 */
@Data
@NoArgsConstructor
public class MilvusRagEmbeddingStore implements RagEmbeddingStore, BaseMutator<MilvusRagEmbeddingStore> {
    public static final String DEFAULT_COLLECTION_NAME = "tb_vec";
    public static final int DEFAULT_DIMENSION = 1024;
    public static final int DEFAULT_CONTENT_MAX_LENGTH = 4096;
    public static final int DEFAULT_METADATA_MAX_LENGTH = 4096;
    public static final DateTimeFormatter MILVUS_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String FIELD_ID = "id";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_VECTOR = "vector";
    public static final String FIELD_META_DATA = "meta_data";
    public static final String FIELD_CREATE_TIME = "create_time";

    private MilvusClientV2 client;
    private Gson gson = new Gson();

    private String collectionName = DEFAULT_COLLECTION_NAME;
    private int dimension = DEFAULT_DIMENSION;

    private int contentMaxLength = DEFAULT_CONTENT_MAX_LENGTH;
    private int metadataMaxLength = DEFAULT_METADATA_MAX_LENGTH;

    public MilvusRagEmbeddingStore(MilvusClientV2 client) {
        this.client = client;
    }

    public static MilvusClientV2 newClient(String url, String username, String password) {
        ConnectConfig.ConnectConfigBuilder builder = ConnectConfig.builder();
        if (url == null || url.isEmpty()) {
            url = "http://localhost:19530";
        }
        builder.uri(url);
        if (username != null) {
            builder.username(username);
        }
        if (password != null) {
            builder.password(password);
        }
        ConnectConfig connectConfig = builder.build();
        ;

        MilvusClientV2 client = new MilvusClientV2(connectConfig);
        return client;
    }

    public void createCollection() {
        Boolean ok = client.hasCollection(HasCollectionReq.builder()
                .collectionName(collectionName)
                .build());
        if (ok != null && ok) {
            return;
        }

        // 创建集合
        CreateCollectionReq.CollectionSchema schema = MilvusClientV2.CreateSchema();

        schema.addField(AddFieldReq.builder()
                .fieldName(FIELD_ID)
                .dataType(DataType.VarChar)
                .isPrimaryKey(true)
                .autoID(false)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName(FIELD_CONTENT)
                .dataType(DataType.VarChar)
                .maxLength(contentMaxLength)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName(FIELD_VECTOR)
                .dataType(DataType.FloatVector)
                .dimension(dimension)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName(FIELD_META_DATA)
                .dataType(DataType.VarChar)
                .maxLength(metadataMaxLength)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName(FIELD_CREATE_TIME)
                .dataType(DataType.VarChar)
                .maxLength(32)
                .build());

        CreateCollectionReq req = CreateCollectionReq.builder()
                .collectionName(collectionName)
                .collectionSchema(schema)
                .build();

        client.createCollection(req);

        // 创建索引
        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("M", 32);
        extraParam.put("efConstruction", 256);

        IndexParam indexParam = IndexParam.builder()
                .fieldName(FIELD_VECTOR)
                .indexType(IndexParam.IndexType.HNSW)
                .metricType(IndexParam.MetricType.COSINE)
                .extraParams(extraParam)
                .build();

        CreateIndexReq indexReq = CreateIndexReq.builder()
                .collectionName(collectionName)
                .indexParams(Collections.singletonList(indexParam))
                .build();
        client.createIndex(indexReq);
    }

    @Override
    public String store(RagEmbedding embedding) {
        String id = embedding.getId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString().replace("-", "");
        }
        embedding.setId(id);

        List<JsonObject> data = new ArrayList<>();

        JsonObject obj = new JsonObject();
        obj.addProperty(FIELD_ID, embedding.getId());
        obj.addProperty(FIELD_CONTENT, embedding.getContent());
        obj.add(FIELD_VECTOR, JsonParser.parseString(gson.toJson(embedding.getVector().getArray())));
        obj.addProperty(FIELD_META_DATA, gson.toJson(embedding.getMetadata()));
        obj.addProperty(FIELD_CREATE_TIME, MILVUS_TIME_FORMATTER.format(LocalDateTime.now()));
        data.add(obj);

        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();

        InsertResp resp = client.insert(insertReq);

        return embedding.getId();
    }


    @Override
    public void remove(String id) {

        DeleteReq req = DeleteReq.builder()
                .collectionName(collectionName)
                .ids(Collections.singletonList(id))
                .build();

        DeleteResp resp = client.delete(req);
    }

    @Override
    public List<RagEmbedding> similar(RagVector vector, int topN) {
        List<RagEmbedding> ret = new ArrayList<>();

        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(Collections.singletonList(new FloatVec(vector.toFloatList())))
                .annsField(FIELD_VECTOR)
                .topK(topN)
                .outputFields(Arrays.asList(FIELD_ID, FIELD_CONTENT, FIELD_VECTOR, FIELD_META_DATA, FIELD_CREATE_TIME))
                .metricType(IndexParam.MetricType.COSINE)
                .consistencyLevel(ConsistencyLevel.EVENTUALLY)
                .build();

        SearchResp resp = client.search(searchReq);

        List<List<SearchResp.SearchResult>> results = resp.getSearchResults();
        for (List<SearchResp.SearchResult> list : results) {
            for (SearchResp.SearchResult r : list) {
                Map<String, Object> entity = r.getEntity();

                RagEmbedding embedding = new RagEmbedding();
                embedding.setId(str(entity.get(FIELD_ID)));
                embedding.setContent(str(entity.get(FIELD_CONTENT)));
                RagVector ragVector = null;
                Object vec = entity.get(FIELD_VECTOR);
                if (vec != null) {
                    if (vec instanceof Iterable) {
                        List<Double> doubleList = new ArrayList<>();
                        boolean ok = true;
                        Iterable<?> col = (Iterable<?>) vec;
                        for (Object obj : col) {
                            if (obj instanceof Float) {
                                doubleList.add((double) (float) (Float) obj);
                            } else if (obj instanceof Double) {
                                doubleList.add((Double) obj);
                            } else {
                                ok = false;
                            }
                        }
                        if (ok && !doubleList.isEmpty()) {
                            ragVector = RagVector.fromList(doubleList);
                        }
                    } else if (vec.getClass().isArray()) {
                        List<Double> doubleList = new ArrayList<>();
                        boolean ok = true;
                        int len = Array.getLength(vec);
                        for (int i = 0; i < len; i++) {
                            Object obj = Array.get(vec, i);
                            if (obj instanceof Float) {
                                doubleList.add((double) (float) (Float) obj);
                            } else if (obj instanceof Double) {
                                doubleList.add((Double) obj);
                            } else {
                                ok = false;
                            }
                        }
                        if (ok && !doubleList.isEmpty()) {
                            ragVector = RagVector.fromList(doubleList);
                        }
                    } else {
                        try {
                            List<Double> doubleList = gson.fromJson(gson.toJson(vec), new TypeToken<List<Double>>() {
                            }.getType());
                            if (doubleList != null && !doubleList.isEmpty()) {
                                ragVector = RagVector.fromList(doubleList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                embedding.setVector(ragVector);
                Map<String, Object> metadata = new HashMap<>();
                Object meta = entity.get(FIELD_META_DATA);
                try {
                    Map<String, Object> obj = gson.fromJson(str(meta), new TypeToken<Map<String, Object>>() {
                    }.getType());
                    if (obj != null) {
                        metadata = obj;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                embedding.setMetadata(metadata);
                embedding.setScore(r.getScore());
                ret.add(embedding);
            }
        }

        return ret;
    }

    public static String str(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }
}
