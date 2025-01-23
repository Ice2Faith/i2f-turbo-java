package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.base.JdbcTrans;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.sql.Connection;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlTransBeginNode implements ExecutorNode {
    public static final String TAG_NAME="sql-trans-begin";
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String datasource = node.getTagAttrMap().get(AttrConsts.DATASOURCE);
        String isolation = node.getTagAttrMap().get(AttrConsts.ISOLATION);
        int val= JdbcTrans.getJdbcTransIsolation(isolation);
        executor.sqlTransBegin(datasource, val, context.getParams());
    }

}
