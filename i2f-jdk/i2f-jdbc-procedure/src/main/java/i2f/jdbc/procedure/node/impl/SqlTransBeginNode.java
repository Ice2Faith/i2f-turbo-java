package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlTransBeginNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-trans-begin".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        String datasource = node.getTagAttrMap().get("datasource");
        String isolation = node.getTagAttrMap().get("isolation");
        int val = Connection.TRANSACTION_READ_COMMITTED;
        try {
            val = Integer.parseInt(isolation);
        } catch (Exception e) {
            if ("NONE".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_NONE;
            } else if ("READ_COMMITTED".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_READ_COMMITTED;
            } else if ("READ_UNCOMMITTED".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_READ_UNCOMMITTED;
            } else if ("REPEATABLE_READ;".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_REPEATABLE_READ;
            } else if ("SERIALIZABLE".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_SERIALIZABLE;
            }
        }
        if (!Arrays.asList(
                        Connection.TRANSACTION_NONE,
                        Connection.TRANSACTION_READ_COMMITTED,
                        Connection.TRANSACTION_READ_UNCOMMITTED,
                        Connection.TRANSACTION_REPEATABLE_READ,
                        Connection.TRANSACTION_SERIALIZABLE)
                .contains(val)
        ) {
            val = Connection.TRANSACTION_READ_COMMITTED;
        }
        executor.sqlTransBegin(datasource, val, params);
    }

}
