package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.page.ApiOffsetSize;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryListNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "sql-query-list";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        List<Map.Entry<String, String>> dialectScriptList = SqlDialect.getSqlDialectList(node, context, executor);
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        Object scriptObj = executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context);
        Integer offset = (Integer) executor.attrValue(AttrConsts.OFFSET, FeatureConsts.INT, node, context);
        Integer limit = (Integer) executor.attrValue(AttrConsts.LIMIT, FeatureConsts.INT, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        Class<?> resultType = (Class<?>) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.CLASS, node, context);
        if (resultType == null) {
            resultType = Map.class;
        }
        String script="";
        BindSql bql=null;
        if(scriptObj instanceof BindSql){
            bql=(BindSql) scriptObj;
        }else{
            script=String.valueOf(scriptObj==null?"":scriptObj);
        }
        if (script == null || script.isEmpty()) {
            script = node.getTagBody();
        }
        if (dialectScriptList.isEmpty()) {
            dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, script));
        }
        if(bql==null) {
            bql = executor.sqlScript(datasource, dialectScriptList, context);
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
        executor.logDebug(() -> "found data is empty: " + row.isEmpty() + "! at " + getNodeLocation(node));
        if (result != null && !result.isEmpty()) {
            executor.visitSet(context, result, row);
        }
    }


}
