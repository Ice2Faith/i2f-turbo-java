package i2f.extension.ai.rag.sqlite;

import i2f.ai.std.rag.RagDocumentsStore;
import i2f.ai.std.rag.data.RagDocument;
import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.mutator.BaseMutator;
import i2f.std.consts.StdConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/11/9 16:42
 * @desc
 */
@Data
@NoArgsConstructor
public class SqliteRagDocumentsStore implements RagDocumentsStore, BaseMutator<SqliteRagDocumentsStore> {
    public static final String DEFAULT_DB_FILE_PATH = StdConst.RUNTIME_PERSIST_DIR + "/" + SqliteVecUtils.DIR_NAME + "/sqlite-vec-docs.db";
    public static final String DEFAULT_TABLE_NAME = "tb_vec_docs";
    public static final DateTimeFormatter SQLITE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected String dbFilePath = DEFAULT_DB_FILE_PATH;
    protected String tableName = DEFAULT_TABLE_NAME;
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public List<String> getDocumentAllIds(String documentId) {
        try (Connection conn = getConnection()) {
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id from " + tableName + " where document_id=?",
                    documentId
            ));
            List<String> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    String id = str(row.get("id"));
                    if (id != null) {
                        ret.add(id);
                    }
                }
            }
            return ret;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void removeDocuments(String documentId) {
        try (Connection conn = getConnection()) {
            JdbcResolver.update(conn, BindSql.of("delete from " + tableName + " where document_id=?",
                    documentId
            ));
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<RagDocument> getDocumentsByIds(Collection<String> ids) {
        try (Connection conn = getConnection()) {
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id,document_id,separate_no,content")
                    .add(" from " + tableName)
                    .add(" where id ").in(ids)
            );
            List<RagDocument> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    String id = str(row.get("id"));
                    String docId = str(row.get("document_id"));
                    int separateNo = ofInt(row.get("separate_no"), -1);
                    String content = str(row.get("content"));

                    RagDocument doc = new RagDocument();
                    doc.setId(id);
                    doc.setDocumentId(docId);
                    doc.setSeparateNo(separateNo);
                    doc.setContent(content);

                    ret.add(doc);
                }
            }
            return ret;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void saveAll(Collection<RagDocument> documents) {
        try (Connection conn = getConnection()) {
            Iterator<RagDocument> rawIterator = documents.iterator();
            Iterator<List<Object>> iterator = new Iterator<List<Object>>() {
                @Override
                public boolean hasNext() {
                    return rawIterator.hasNext();
                }

                @Override
                public List<Object> next() {
                    RagDocument next = rawIterator.next();
                    List<Object> ret = new ArrayList<>();
                    ret.add(next.getId());
                    ret.add(next.getDocumentId());
                    ret.add(next.getSeparateNo());
                    ret.add(next.getContent());
                    return ret;
                }
            };
            JdbcResolver.batchByListableValues(conn, "insert into " + tableName + "(id,document_id,separate_no,content) values(?,?,?,?)",
                    iterator, 30
            );
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<RagDocument> nearbyDocuments(String documentId, int separateNoStart, int separateNoEnd) {
        try (Connection conn = getConnection()) {
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id,document_id,separate_no,content")
                    .add(" from " + tableName)
                    .add(" where document_id= ?", documentId)
                    .add(" and separate_no >= ?", separateNoStart)
                    .add(" and separate_no <= ?", separateNoEnd)
            );
            List<RagDocument> ret = new ArrayList<>();
            List<Map<String, Object>> rows = res.getRows();
            if (rows != null && !rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    String id = str(row.get("id"));
                    String docId = str(row.get("document_id"));
                    int separateNo = ofInt(row.get("separate_no"), -1);
                    String content = str(row.get("content"));

                    RagDocument doc = new RagDocument();
                    doc.setId(id);
                    doc.setDocumentId(docId);
                    doc.setSeparateNo(separateNo);
                    doc.setContent(content);

                    ret.add(doc);
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

    public int ofInt(Object obj, int def) {
        if (obj == null) {
            return def;
        }
        try {
            return new BigDecimal(String.valueOf(obj)).intValue();
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
            JdbcResolver.update(conn, BindSql.of("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                    "   id text primary key,\n" +
                    "   document_id text,\n" +
                    "   separate_no integer,\n" +
                    "   content text\n" +
                    ")"));
        }
    }
}
