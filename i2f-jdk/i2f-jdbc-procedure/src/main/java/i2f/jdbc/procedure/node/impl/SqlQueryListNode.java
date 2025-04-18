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

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryListNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_QUERY_LIST;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        BindSql bql = SqlDialect.getSqlDialectList(datasource, node, context, executor);
        Integer offset = executor.convertAs( executor.attrValue(AttrConsts.OFFSET, FeatureConsts.INT, node, context),Integer.class);
        Integer limit = executor.convertAs( executor.attrValue(AttrConsts.LIMIT, FeatureConsts.INT, node, context),Integer.class);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        Class<?> resultType = (Class<?>) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.CLASS, node, context);
        if (resultType == null) {
            resultType = Map.class;
        }

        ApiOffsetSize page = null;
        if (offset != null || limit != null) {
            page = new ApiOffsetSize(offset, limit);
        }
        if (page != null) {
            bql = executor.sqlWrapPage(datasource, bql, page, context);
        }
        if(executor.isDebug()){
            bql=bql.concat(getTrackingComment(node));
        }
        List<?> row = executor.sqlQueryList(datasource, bql, context, resultType);
        if(executor.isDebug()) {
            executor.logDebug("found data is empty: " + row.isEmpty() + "! at " + getNodeLocation(node));
        }
        if (result != null) {
            executor.visitSet(context, result, row);
        }
    }


}
