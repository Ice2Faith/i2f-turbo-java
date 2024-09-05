package i2f.extension.mybatis.dynamic;

import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MybatisDynamicScriptExecutor {
    public static final String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String SCRIPT_TAG_NAME = "script";
    public static final Configuration DEFAULT_CONFIGURATION = new Configuration();

    static {
        DEFAULT_CONFIGURATION.setLogImpl(StdOutImpl.class);
        DEFAULT_CONFIGURATION.setCallSettersOnNulls(true);
        DEFAULT_CONFIGURATION.setMapUnderscoreToCamelCase(true);
    }

    public static String wrapAsScriptXml(String script) {
        return XML_PREFIX + "\n"
                + "<" + SCRIPT_TAG_NAME + ">\n"
                + script + "\n"
                + "</" + SCRIPT_TAG_NAME + ">\n";
    }

    public static Configuration getSafeConfiguration(Configuration configuration) {
        if (configuration != null) {
            return configuration;
        }
        return DEFAULT_CONFIGURATION;
    }

    public static <T> T find(String script, Object params, Class<T> returnType,
                             Connection conn) throws SQLException {
        return find(script, params, returnType, conn, null, null, null);
    }

    public static <T> T find(String script, Object params, Class<T> returnType,
                             Connection conn,
                             Configuration configuration) throws SQLException {
        return find(script, params, returnType, conn, null, null, configuration);
    }

    public static <T> T find(String script, Object params, Class<T> returnType,
                             Connection conn,
                             String statementId, String resultMapId,
                             Configuration configuration) throws SQLException {
        List<T> list = query(script, params, returnType, conn, statementId, resultMapId, configuration);
        if (list.isEmpty()) {
            return null;
        }
        Iterator<T> iterator = list.iterator();
        T next = iterator.next();
        boolean hasMore = iterator.hasNext();
        if (hasMore) {
            throw new SQLException("result row expect one, but more found!");
        }
        return next;
    }

    public static <T> List<T> query(String script, Object params, Class<T> returnType,
                                    Connection conn) throws SQLException {
        return query(script, params, returnType, conn, null, null, null);
    }

    public static <T> List<T> query(String script, Object params, Class<T> returnType,
                                    Connection conn,
                                    Configuration configuration) throws SQLException {
        return query(script, params, returnType, conn, null, null, configuration);
    }

    public static <T> List<T> query(String script, Object params, Class<T> returnType,
                                    Connection conn,
                                    String statementId, String resultMapId,
                                    Configuration configuration) throws SQLException {
        if (statementId == null || statementId.isEmpty()) {
            statementId = "statement_" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }
        if (resultMapId == null || resultMapId.isEmpty()) {
            resultMapId = "resultMap_" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }
        Configuration safeConfiguration = getSafeConfiguration(configuration);

        SqlSource sqlSource = parseSqlSource(script, safeConfiguration);

        MappedStatement mappedStatement = new MappedStatement.Builder(safeConfiguration,
                statementId,
                sqlSource,
                SqlCommandType.SELECT)
                .resultMaps(new ArrayList<>(
                        Collections.singletonList(
                                new ResultMap.Builder(safeConfiguration, resultMapId, returnType, new ArrayList<>())
                                        .build()
                        )
                ))
                .build();

        SimpleExecutor simpleExecutor = new SimpleExecutor(safeConfiguration, new JdbcTransaction(conn));
        List<T> list = simpleExecutor.doQuery(mappedStatement, params, RowBounds.DEFAULT, null, null);

        return list;
    }

    public static int update(String script, Object params,
                             Connection conn) throws SQLException {
        return update(script, params, conn, null, null, null);
    }

    public static int update(String script, Object params,
                             Connection conn,
                             Configuration configuration) throws SQLException {
        return update(script, params, conn, null, null, configuration);
    }

    public static int update(String script, Object params,
                             Connection conn,
                             String statementId, String resultMapId,
                             Configuration configuration) throws SQLException {
        if (statementId == null || statementId.isEmpty()) {
            statementId = "statement_" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }
        if (resultMapId == null || resultMapId.isEmpty()) {
            resultMapId = "resultMap_" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        }
        Configuration safeConfiguration = getSafeConfiguration(configuration);

        SqlSource sqlSource = parseSqlSource(script, safeConfiguration);

        MappedStatement mappedStatement = new MappedStatement.Builder(safeConfiguration,
                statementId,
                sqlSource,
                SqlCommandType.UPDATE)
                .build();

        SimpleExecutor simpleExecutor = new SimpleExecutor(safeConfiguration, new JdbcTransaction(conn));
        int cnt = simpleExecutor.doUpdate(mappedStatement, params);

        return cnt;
    }

    public static SqlSource parseSqlSource(String script) {
        return parseSqlSource(script, null);
    }

    public static SqlSource parseSqlSource(String script, Configuration configuration) {
        Configuration safeConfiguration = getSafeConfiguration(configuration);

        String xml = wrapAsScriptXml(script);
        XPathParser parser = new XPathParser(xml);
        XNode scriptNode = parser.evalNode(SCRIPT_TAG_NAME);

        XMLScriptBuilder xmlScriptBuilder = new XMLScriptBuilder(safeConfiguration, scriptNode);
        SqlSource sqlSource = xmlScriptBuilder.parseScriptNode();

        return sqlSource;
    }
}
