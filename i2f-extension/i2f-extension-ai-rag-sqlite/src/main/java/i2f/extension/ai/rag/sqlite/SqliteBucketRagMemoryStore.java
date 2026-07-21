package i2f.extension.ai.rag.sqlite;

import i2f.ai.std.rag.BucketRagEmbeddingStore;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagVector;
import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.mutator.BaseMutator;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.std.consts.StdConst;
import lombok.Data;
import lombok.NoArgsConstructor;

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
 * @desc 使用 sqlite-vec 拓展实现的嵌入式向量数据库
 * 拓展下载地址： https://github.com/asg017/sqlite-vec/releases?spm=5176.28103460.0.0.96a02988ZjGZEM
 */
@Data
@NoArgsConstructor
public class SqliteBucketRagMemoryStore implements BucketRagEmbeddingStore, BaseMutator<SqliteBucketRagMemoryStore> {

    public static final String DEFAULT_DB_FILE_PATH = StdConst.RUNTIME_PERSIST_DIR + "/" + SqliteVecUtils.DIR_NAME + "/sqlite-memory-vec.db";
    public static final String DEFAULT_TABLE_NAME = "tb_vec";
    public static final int DEFAULT_DIMENSION = 1024;
    public static final DateTimeFormatter SQLITE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected String dbFilePath = DEFAULT_DB_FILE_PATH;
    protected String tableName = DEFAULT_TABLE_NAME;
    protected int dimension = DEFAULT_DIMENSION;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public String store(RagEmbedding embedding, String bucket) {
        try (Connection conn = getConnection()) {
            String id = embedding.getId();
            if (id == null || id.isEmpty()) {
                id = UUID.randomUUID().toString().replace("-", "");
            }
            JdbcResolver.update(conn, BindSql.of("insert into " + tableName + " (id,content,vector,meta_data,bucket,create_time) values (?,?,?,?,?)",
                    id, embedding.getContent(),
                    jsonSerializer.serialize(embedding.getVector().getArray()),
                    jsonSerializer.serialize(embedding.getMetadata()),
                    bucket,
                    SQLITE_TIME_FORMATTER.format(LocalDateTime.now())
            ));
            return id;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void remove(String id) {
        try (Connection conn = getConnection()) {
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
            QueryResult res = JdbcResolver.query(conn,
                    BindSql.of("SELECT id,content,vector,meta_data,bucket,create_time,distance FROM " + tableName + " " +
                                    "WHERE vector MATCH ? ", jsonSerializer.serialize(vector.getArray())
                            )
                            .when(buckets, e -> e != null && !e.isEmpty(), (bql, list) -> {
                                return bql.and().add("bucket").in(list);
                            })
                            .add("ORDER BY distance LIMIT ?", topN)
            );
            List<RagEmbedding> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    byte[] arr = (byte[]) row.get("vector");
                    float[] vec = SqliteVecUtils.jdbcBytes2FloatArray(arr);

                    // distance 表示相似度，[0,2] 区间，值越小，越相似
                    // 2-distance=score;
                    RagEmbedding item = new RagEmbedding();
                    item.setId(str(row.get("id")));
                    item.setContent(str(row.get("content")));
                    item.setVector(RagVector.fromFloatArray(vec));
                    item.setScore((2 - numeric(row.get("distance"), 3)) / 2.0);
                    String json = str(row.get("meta_data"));
                    if (json != null) {
                        try {
                            item.setMetadata((Map<String, Object>) jsonSerializer.deserialize(json));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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
        return SqliteVecUtils.getConnection(dbFilePath);
    }

    public void init() throws SQLException {
        if (initialized.getAndSet(true)) {
            return;
        }
        SqliteVecUtils.releaseLib();
        createTable();
    }


    public void createTable() throws SQLException {
        try (Connection conn = SqliteVecUtils.getConnection(dbFilePath)) {
            // 创建 vec0 虚拟表（例如 4 维的 float 向量）
            JdbcResolver.update(conn, BindSql.of("CREATE VIRTUAL TABLE IF NOT EXISTS " + tableName + " USING vec0(\n" +
                    "   id text primary key,\n" +
                    "   content text,\n" +
                    "   vector float[" + dimension + "] distance_metric=cosine,\n" +
                    "   bucket text,\n" +
                    "   meta_data text,\n" +
                    "   create_time text\n" +
                    ")"));
        }
    }

}
