package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlScopeNode implements ExecutorNode {
    public static final String TAG_NAME = "sql-scope";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String datasources = node.getTagAttrMap().get(AttrConsts.DATASOURCES);
        String[] arr = datasources.split(",");
        Map<String, DataSource> datasourceMap = context.paramsComputeIfAbsent(ParamsConsts.DATASOURCES, (key) -> new HashMap<>());
        Set<String> targets = new HashSet<>();
        for (String item : arr) {
            if ("all".equals(item)) {
                targets.addAll(datasourceMap.keySet());
            }
            if (datasourceMap.containsKey(item)) {
                targets.add(item);
            }
        }
        HashMap<Object, Connection> originConnMap = context.paramsComputeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
        Map<String, Connection> bakConnMap = new HashMap<>();
        for (String name : targets) {
            Connection conn = originConnMap.get(name);
            if (conn != null) {
                bakConnMap.put(name, conn);
            }
            originConnMap.remove(name);
        }

        try {
            executor.execAsProcedure(node, context, false, false);
        } finally {
            for (String name : targets) {
                Connection conn = originConnMap.get(name);
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                originConnMap.remove(name);
            }
            originConnMap.putAll(bakConnMap);
        }
    }
}
