package i2f.jdbc.producer.executor.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.node.impl.*;
import i2f.jdbc.producer.parser.data.XmlNode;
import i2f.reflect.vistor.Visitor;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class SqlProducerExecutorImpl implements SqlProducerExecutor {
    protected final CopyOnWriteArrayList<ExecutorNode> nodes = new CopyOnWriteArrayList<>();

    private SqlProducerExecutorImpl() {

    }

    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        ret.add(new LangBreakNode());
        ret.add(new LangChooseNode());
        ret.add(new LangContinueNode());
        ret.add(new LangEvalNode());
        ret.add(new LangForeachNode());
        ret.add(new LangForiNode());
        ret.add(new LangIfNode());
        ret.add(new LangPrintlnNode());
        ret.add(new LangRenderNode());
        ret.add(new LangStringNode());
        ret.add(new LangTryNode());
        ret.add(new LangWhenNode());
        ret.add(new LangWhileNode());
        ret.add(new ProducerNode());
        ret.add(new ScriptIncludeNode());
        ret.add(new ScriptSegmentNode());
        ret.add(new SqlQueryListNode());
        ret.add(new SqlQueryObjectNode());
        ret.add(new SqlQueryRowNode());
        ret.add(new SqlTransBeginNode());
        ret.add(new SqlTransCommitNode());
        ret.add(new SqlTransRollbackNode());
        ret.add(new SqlUpdateNode());
        ret.add(new TextNode());
        return ret;
    }

    public static SqlProducerExecutor create() {
        SqlProducerExecutorImpl ret = new SqlProducerExecutorImpl();
        ret.getNodes().addAll(defaultExecutorNodes());
        return ret;
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        for (ExecutorNode item : nodes) {
            if (item.support(node)) {
                item.exec(node, params, nodeMap, this);
                break;
            }
        }
    }

    @Override
    public void execAsProducer(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        ProducerNode execNode = null;
        for (ExecutorNode item : nodes) {
            if (item instanceof ProducerNode) {
                execNode = (ProducerNode) item;
                break;
            }
        }
        if (execNode == null) {
            execNode = ProducerNode.INSTANCE;
        }
        execNode.exec(node, params, nodeMap, this);
    }

    @Override
    public Class<?> loadClass(String className) {
        if ("long".equals(className)) {
            return Long.class;
        }
        if ("int".equals(className)) {
            return Integer.class;
        }
        if ("short".equals(className)) {
            return Short.class;
        }
        if ("byte".equals(className)) {
            return Byte.class;
        }
        if ("char".equals(className)) {
            return Character.class;
        }
        if ("float".equals(className)) {
            return Float.class;
        }
        if ("double".equals(className)) {
            return Double.class;
        }
        if ("boolean".equals(className)) {
            return Boolean.class;
        }
        String[] prefixes = {
                "",
                "java.lang.",
                "java.util.",
                "java.sql.",
                "java.math.",
                "java.io.",
                "java.time.",
                "java.reflect.",
                "java.concurrent.",
                "java.concurrent.atomic."
        };
        Class<?> clazz = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String prefix : prefixes) {
            try {
                clazz = loader.loadClass(prefix + className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {

            }
            try {
                clazz = Class.forName(prefix + className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {

            }
        }
        return clazz;
    }


    @Override
    public boolean test(String test, Map<String, Object> params) {
        System.out.println("test:" + test);
        try {
            boolean ok = Boolean.parseBoolean(test);
            return ok;
        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public Object eval(String script, Map<String, Object> params) {
        System.out.println("eval:" + script);
        try {
            return Integer.parseInt(script);
        } catch (Exception e) {

        }
        try {
            return Long.parseLong(script);
        } catch (Exception e) {

        }
        try {
            return Boolean.parseBoolean(script);
        } catch (Exception e) {

        }
        try {
            return Double.parseDouble(script);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public Object visit(String script, Map<String, Object> params) {
        System.out.println("visit:" + script);
        if (params.containsKey(script)) {
            return params.get(script);
        }
        return Visitor.visit(script, params);
    }

    @Override
    public String render(String script, Map<String, Object> params) {
        System.out.println("render:" + script);
        return script;
    }

    public Connection getConnection(String datasource, Map<String, Object> params) {
        if (datasource == null || datasource.isEmpty()) {
            datasource = "primary";
        }
        Map<String, Connection> datasourceMap = (Map<String, Connection>) params.get("datasources");
        Connection conn = datasourceMap.get(datasource);
        return conn;
    }

    @Override
    public List<?> sqlQueryList(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryList:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            List<?> list = JdbcResolver.list(conn, new BindSql(script), resultType);
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Object sqlQueryObject(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryObject:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            Object obj = JdbcResolver.get(conn, new BindSql(script), resultType);
            return obj;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object sqlQueryRow(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryRow:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            Object row = JdbcResolver.find(conn, new BindSql(script), resultType);
            return row;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public int sqlUpdate(String datasource, String script, Map<String, Object> params) {
        System.out.println("sqlUpdate:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            int num = JdbcResolver.update(conn, new BindSql(script));
            return num;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransBegin(String datasource, int isolation, Map<String, Object> params) {
        System.out.println("sqlTransBegin:" + datasource);
        try {
            if (isolation < 0) {
                isolation = Connection.TRANSACTION_READ_COMMITTED;
            }
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(isolation);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransCommit(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransCommit:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.commit();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransRollback(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransRollback:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.rollback();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransNone(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransNone:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}
