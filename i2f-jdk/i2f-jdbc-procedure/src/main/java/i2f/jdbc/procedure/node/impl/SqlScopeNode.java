package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlScopeNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_SCOPE;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String datasources = node.getTagAttrMap().get(AttrConsts.DATASOURCES);
        if (datasources == null || datasources.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.DATASOURCES);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasources = node.getTagAttrMap().get(AttrConsts.DATASOURCES);
        String[] arr = datasources.split(",");
        Map<String, DataSource> datasourceMap = (Map<String, DataSource>) context.computeIfAbsent(ParamsConsts.DATASOURCES, (key) -> new HashMap<>());
        Set<String> targets = new HashSet<>();
        for (String item : arr) {
            if ("all".equals(item)) {
                targets.addAll(datasourceMap.keySet());
            }
            if (datasourceMap.containsKey(item)) {
                targets.add(item);
            }
        }
        Map<String, Connection> originConnMap = (Map<String, Connection>) context.computeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
        Map<String, Connection> bakConnMap = new HashMap<>();
        for (String name : targets) {
            Connection conn = originConnMap.get(name);
            if (conn != null) {
                bakConnMap.put(name, conn);
            }
            originConnMap.remove(name);
        }

        Throwable ex = null;
        try {
            executor.execAsProcedure(node, context, false, false);
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            if (ex instanceof ControlSignalException) {
                ex = null;
            }
            for (String name : targets) {
                Connection conn = originConnMap.get(name);
                executor.closeConnection(conn, name, null, ex);
                originConnMap.remove(name);
            }
            originConnMap.putAll(bakConnMap);
        }
    }
}
