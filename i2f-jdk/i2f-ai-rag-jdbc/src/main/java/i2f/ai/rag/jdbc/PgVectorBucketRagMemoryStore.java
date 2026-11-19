package i2f.ai.rag.jdbc;

import i2f.ai.std.rag.BucketRagEmbeddingStore;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagVector;
import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.mutator.BaseMutator;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.typeof.token.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/7/2 9:07
 * @desc 使用 pg-vector 拓展实现的postgre向量数据库
 */
@Data
@NoArgsConstructor
public class PgVectorBucketRagMemoryStore implements BucketRagEmbeddingStore, BaseMutator<PgVectorBucketRagMemoryStore> {

    public static final String DEFAULT_TABLE_NAME = "tb_vec";
    public static final int DEFAULT_DIMENSION = 1024;
    public static final DateTimeFormatter PGVEC_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected DataSource dataSource;
    protected String tableName = DEFAULT_TABLE_NAME;
    protected int dimension = DEFAULT_DIMENSION;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    public PgVectorBucketRagMemoryStore(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String store(RagEmbedding embedding, String bucket) {
        try (Connection conn = getConnection()) {
            String id = embedding.getId();
            if (id == null || id.isEmpty()) {
                id = UUID.randomUUID().toString().replace("-", "");
            }
            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("insert into " + tableName + " (id,content,vector,meta_data,bucket,create_time) values (?,?,?::vector,?,?,?)",
                    id, embedding.getContent(),
                    jsonSerializer.serialize(embedding.getVector().getArray()),
                    jsonSerializer.serialize(embedding.getMetadata()),
                    bucket,
                    PGVEC_TIME_FORMATTER.format(LocalDateTime.now())
            ));
            return id;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void remove(String id) {
        try (Connection conn = getConnection()) {
            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("delete from " + tableName + " where id=?",
                    id
            ));
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<RagEmbedding> similar(RagVector vector, int topN, Collection<String> buckets) {
        try (Connection conn = getConnection()) {
            String vecJson = jsonSerializer.serialize(vector.getArray());
            /*language=sql*/
            QueryResult res = JdbcResolver.query(conn,
                    BindSql.of("SELECT id,content,vector,meta_data,bucket,create_time,vector <=> ?::vector AS distance FROM " + tableName + " " +
                                    "WHERE 1=1 ", vecJson
                            )
                            .when(buckets, e -> e != null && !e.isEmpty(), (bql, list) -> {
                                return bql.and().add("bucket").in(list);
                            })
                            .add("ORDER BY vector <=> ?::vector LIMIT ?", vecJson, topN),
                    -1, String::toLowerCase
            );
            List<RagEmbedding> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    String vecStr = (String) row.get("vector");
                    List<Double> vec = (List<Double>) jsonSerializer.deserialize(vecStr, new TypeToken<List<Double>>() {
                    }.type());

                    // distance 表示相似度，[0,2] 区间，值越小，越相似
                    // 2-distance=score;
                    RagEmbedding item = new RagEmbedding();
                    item.setId(str(row.get("id")));
                    item.setContent(str(row.get("content")));
                    item.setVector(RagVector.fromList(vec));
                    item.setScore((2 - numeric(row.get("distance"), 3)) / 2.0);
                    String json = str(row.get("meta_data"));
                    if (json != null) {
                        try {
                            item.setMetadata((Map<String, Object>) jsonSerializer.deserialize(json));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    item.setContent(str(row.get("bucket")));
                    ret.add(item);
                }
            }

            return ret;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static String str(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    public double numeric(Object obj, double def) {
        if (obj == null) {
            return def;
        }
        try {
            return new BigDecimal(String.valueOf(obj)).doubleValue();
        } catch (Exception e) {

        }
        return def;
    }


    public Connection getConnection() throws SQLException {
        init();
        return getInnerConnection();
    }

    public Connection getInnerConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void init() throws SQLException {
        if (initialized.getAndSet(true)) {
            return;
        }
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createTable() throws SQLException {
        try (Connection conn = getInnerConnection()) {
            // 创建 vector 拓展表（例如 4 维的 float 向量）
            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("CREATE EXTENSION IF NOT EXISTS vector"));

            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("CREATE TABLE IF NOE EXISTS " + tableName + " (\n" +
                    "    id        varchar(300) PRIMARY KEY,\n" +
                    "    content   TEXT,\n" +
                    "    vector VECTOR(" + dimension + "), \n" +
                    "    bucket varchar(300),\n" +
                    "    meta_data text,\n" +
                    "    create_time varchar(64)\n" +
                    ")"));

            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("create index idx" + tableName + "_bucket" +
                    " on " + tableName + "(bucket)"));

            /*language=sql*/
            JdbcResolver.update(conn, BindSql.of("CREATE INDEX ON " + tableName + " USING hnsw (vector vector_cosine_ops)\n" +
                    "            WITH (m = 32, ef_construction = 256)"));
        }
    }

}
