package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.page.ApiOffsetSize;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryRowNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_QUERY_ROW;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        BindSql bql = SqlDialect.getSqlDialectList(datasource, node, context, executor);
        boolean limited = executor.toBoolean(executor.attrValue(AttrConsts.LIMITED, FeatureConsts.BOOLEAN, node, context));
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        Class<?> resultType = (Class<?>) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.CLASS, node, context);
        if (resultType == null) {
            resultType = Map.class;
        }

        ApiOffsetSize page = null;
        if (limited) {
            page = new ApiOffsetSize(null, 1);
        }
        if (page != null) {
            bql = executor.sqlWrapPage(datasource, bql, page, context);
        }
        if (executor.isDebug()) {
            bql = bql.concat(getTrackingComment(node));
        }
        Object row = executor.sqlQueryRow(datasource, bql, context, resultType);
        boolean isEmpty = (row == null);
        if (executor.isDebug()) {
            executor.logger().logDebug("found data is null: " + isEmpty + "! at " + getNodeLocation(node));
        }
        if (result != null) {
            executor.visitSet(context, result, row);
        }
    }


}
