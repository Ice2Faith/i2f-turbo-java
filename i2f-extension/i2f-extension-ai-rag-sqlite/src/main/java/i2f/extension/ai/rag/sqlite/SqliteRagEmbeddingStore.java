package i2f.extension.ai.rag.sqlite;

import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagVector;
import i2f.bindsql.BindSql;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.mutator.BaseMutator;
import i2f.resources.ResourceUtil;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.std.consts.StdConst;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/7/2 9:07
 * @desc 使用 sqlite-vec 拓展实现的嵌入式向量数据库
 * 拓展下载地址： https://github.com/asg017/sqlite-vec/releases?spm=5176.28103460.0.0.96a02988ZjGZEM
 */
@Data
@NoArgsConstructor
public class SqliteRagEmbeddingStore implements RagEmbeddingStore, BaseMutator<SqliteRagEmbeddingStore> {
    public static final String DIR_NAME = "sqlite-vec";
    public static final String DEFAULT_DB_FILE_PATH = StdConst.RUNTIME_PERSIST_DIR + "/" + DIR_NAME + "/sqlite-vec.db";
    public static final String DEFAULT_TABLE_NAME = "tb_vec";
    public static final int DEFAULT_DIMENSION = 1024;
    public static final DateTimeFormatter SQLITE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected String dbFilePath = DEFAULT_DB_FILE_PATH;
    protected String tableName = DEFAULT_TABLE_NAME;
    protected int dimension = DEFAULT_DIMENSION;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public String store(RagEmbedding embedding) {
        try (Connection conn = getConnection()) {
            String id = embedding.getId();
            if (id == null || id.isEmpty()) {
                id = UUID.randomUUID().toString().replace("-", "");
            }
            JdbcResolver.update(conn, BindSql.of("insert into " + tableName + " (id,content,vector,meta_data,create_time) values (?,?,?,?,?)",
                    id, embedding.getContent(),
                    jsonSerializer.serialize(embedding.getVector().getArray()),
                    jsonSerializer.serialize(embedding.getMetadata()),
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
    public List<RagEmbedding> similar(RagVector vector, int topN) {
        try (Connection conn = getConnection()) {
            QueryResult res = JdbcResolver.query(conn,
                    BindSql.of("SELECT id,content,vector,meta_data,create_time,distance FROM " + tableName + " " +
                                    "WHERE vector MATCH ? " +
                                    "ORDER BY distance LIMIT ?",
                            jsonSerializer.serialize(vector.getArray()),
                            topN
                    )
            );
            List<RagEmbedding> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    byte[] arr = (byte[]) row.get("vector");
                    float[] vec = jdbcBytes2FloatArray(arr);

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
        return getConnectionInner();
    }

    public void init() throws SQLException {
        if (initialized.getAndSet(true)) {
            return;
        }
        releaseLib();
        createTable();
    }

    protected Connection getConnectionInner() throws SQLException {
        // 启用拓展
        SQLiteConfig config = new SQLiteConfig();
        config.enableLoadExtension(true);

        // 使用配置初始化数据库连接
        Connection conn = JdbcResolver.getConnection("org.sqlite.JDBC", "jdbc:sqlite:" + dbFilePath, config.toProperties());

        // 加载 sqlite-vec 扩展库（注意路径和文件后缀，Windows 为 .dll，Linux 为 .so）
        String libFilePath = getLibFilePath();
        JdbcResolver.query(conn, BindSql.of("SELECT load_extension('" + libFilePath + "')"));

        return conn;
    }

    public void createTable() throws SQLException {
        try (Connection conn = getConnectionInner()) {
            // 创建 vec0 虚拟表（例如 4 维的 float 向量）
            JdbcResolver.update(conn, BindSql.of("CREATE VIRTUAL TABLE IF NOT EXISTS " + tableName + " USING vec0(\n" +
                    "   id text primary key,\n" +
                    "   content text,\n" +
                    "   vector float[" + dimension + "] distance_metric=cosine,\n" +
                    "   meta_data text,\n" +
                    "   create_time text\n" +
                    ")"));
        }
    }


    public static float[] jdbcBytes2FloatArray(byte[] vectorBytes) {
        if (vectorBytes == null) {
            return null;
        }
        if (vectorBytes.length == 0) {
            return new float[0];
        }
        FloatBuffer buffer = ByteBuffer.wrap(vectorBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] floatArray = new float[buffer.remaining()];
        buffer.get(floatArray);
        return floatArray;
    }

    public static String getLibName() {
        String libName = System.mapLibraryName("vec0");
        return libName;
    }

    public static String getLibFilePath() {
        String libName = getLibName();
        return StdConst.RUNTIME_PERSIST_DIR + "/" + DIR_NAME + "/" + libName;
    }

    public static void releaseLib() {
        try {
            String libFilePath = getLibFilePath();
            File saveFile = new File(libFilePath);
            saveFile = new File(saveFile.getAbsolutePath());

            File libDir = new File(saveFile.getParent());
            libDir = new File(libDir.getAbsolutePath());
            if (!libDir.exists()) {
                libDir.mkdirs();
            }

            InputStream is = ResourceUtil.getClasspathResourceAsStream("/assets/sqlite-vec/" + saveFile.getName());
            StreamUtil.writeBytes(is, saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
