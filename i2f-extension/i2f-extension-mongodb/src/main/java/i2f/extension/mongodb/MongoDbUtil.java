package i2f.extension.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import i2f.reflect.ReflectResolver;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/7/12 19:33
 * @desc
 */
public class MongoDbUtil {
    protected MongoClient mongoClient;
    protected MongoDatabase database;
    protected MongoCollection<Document> collection;

    public MongoDbUtil(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoDbUtil(MongoDbMeta meta) {
        this.mongoClient = getClient(meta);
    }

    public static MongoClient getClient(MongoDbMeta meta) {
        MongoCredential credential = MongoCredential.createPlainCredential(meta.getUsername(), meta.getSource(), meta.getPassword().toCharArray());
        MongoClient client = new MongoClient(
                new ServerAddress(meta.getHost(), meta.getPort()),
                credential,
                MongoClientOptions.builder()
                        .connectTimeout(meta.getConnectTimeout()).build());
        return client;
    }

    public MongoDatabase getDatabase(String database) {
        this.database = mongoClient.getDatabase(database);
        return this.database;
    }

    public MongoCollection<Document> getCollection(String collection) {
        this.collection = database.getCollection(collection);
        return this.collection;
    }

    public void insert(Document doc) {
        this.collection.insertOne(doc);
    }

    public void insert(Map<String, Object> map) {
        Document doc = new Document(map);
        insert(doc);
    }

    public void insert(Object bean) {
        Map<String, Object> map = new HashMap<>();
        ReflectResolver.bean2map(bean, map);
        insert(map);
    }

    public void insertBatch(List<Document> list) {
        this.collection.insertMany(list);
    }

    public void insertBatchMap(List<Map<String, Object>> list) {
        List<Document> docs = new ArrayList<>(list.size());
        for (Map<String, Object> item : list) {
            Document doc = new Document(item);
            docs.add(doc);
        }
        insertBatch(docs);
    }

}
