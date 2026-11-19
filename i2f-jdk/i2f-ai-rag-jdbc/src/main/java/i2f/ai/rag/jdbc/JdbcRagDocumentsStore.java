package i2f.ai.rag.jdbc;

import i2f.ai.std.rag.RagDocumentsStore;
import i2f.ai.std.rag.data.RagDocument;
import i2f.bindsql.BindSql;
import i2f.database.type.DatabaseType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/11/9 16:42
 * @desc
 */
@Data
@NoArgsConstructor
public class JdbcRagDocumentsStore implements RagDocumentsStore, BaseMutator<JdbcRagDocumentsStore> {
    public static final String DEFAULT_TABLE_NAME = "tb_vec_docs";
    protected String tableName = DEFAULT_TABLE_NAME;
    protected DataSource dataSource;
    protected final AtomicBoolean initialized = new AtomicBoolean(false);

    public JdbcRagDocumentsStore(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getDocumentAllIds(String documentId) {
        try (Connection conn = getConnection()) {
            /*language=sql*/
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id from " + tableName + " where document_id=?",
                    documentId
            ), -1, String::toLowerCase);
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
            /*language=sql*/
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
            /*language=sql*/
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id,document_id,separate_no,content")
                            .add(" from " + tableName)
                            .add(" where id ").in(ids),
                    -1,
                    String::toLowerCase
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
            /*language=sql*/
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
            /*language=sql*/
            QueryResult res = JdbcResolver.query(conn, BindSql.of("select id,document_id,separate_no,content")
                            .add(" from " + tableName)
                            .add(" where document_id= ?", documentId)
                            .add(" and separate_no >= ?", separateNoStart)
                            .add(" and separate_no <= ?", separateNoEnd),
                    -1,
                    String::toLowerCase
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
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            if (DatabaseType.ORACLE == databaseType
                    || DatabaseType.ORACLE_12C == databaseType) {
                /*language=sql*/
                Long cnt = JdbcResolver.get(conn, BindSql.of("SELECT COUNT(*)\n" +
                                "    FROM user_tables\n" +
                                "   WHERE table_name = ?", tableName.toUpperCase()
                        ),
                        Long.class);
                if (cnt == null || cnt == 0) {
                    /*language=sql*/
                    JdbcResolver.update(conn, BindSql.of("CREATE TABLE " + tableName + " (\n" +
                            "   id varchar2(300) primary key,\n" +
                            "   document_id  varchar2(2000),\n" +
                            "   separate_no number,\n" +
                            "   content varchar2(4000)\n" +
                            ")"));

                    /*language=sql*/
                    JdbcResolver.update(conn, BindSql.of("create index idx_rag_doc on " + tableName + "(document_id)"));
                }
            } else {
                /*language=sql*/
                JdbcResolver.update(conn, BindSql.of("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                        "   id varchar(300) primary key,\n" +
                        "   document_id  varchar(2000),\n" +
                        "   separate_no int,\n" +
                        "   content varchar(4000)\n" +
                        ")"));

                /*language=sql*/
                JdbcResolver.update(conn, BindSql.of("create index idx_rag_doc on " + tableName + "(document_id)"));

            }

        }
    }
}
