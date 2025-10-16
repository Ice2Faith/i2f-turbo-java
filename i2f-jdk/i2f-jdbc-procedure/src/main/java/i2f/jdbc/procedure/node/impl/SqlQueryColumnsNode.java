package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryColumnsNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_QUERY_COLUMNS;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        BindSql bql = SqlDialect.getSqlDialectList(datasource, node, context, executor);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (executor.isDebug()) {
            bql = bql.concat(getTrackingComment(node));
        }
        List<?> row = executor.sqlQueryColumns(datasource, bql, context);
        if (executor.isDebug()) {
            executor.logger().logDebug("found columns is empty: " + row.isEmpty() + "! at " + getNodeLocation(node));
        }
        if (result != null) {
            executor.visitSet(context, result, row);
        }
    }

}
