package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlTransRollbackNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "sql-trans-rollback";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String datasource = node.getTagAttrMap().get(AttrConsts.DATASOURCE);
        executor.sqlTransRollback(datasource, context);
    }

}
