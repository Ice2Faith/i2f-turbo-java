package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlScriptNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_SCRIPT;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        List<Map.Entry<String, String>> dialectScriptList = SqlDialect.getSqlDialectList(node, context, executor);
        String datasource = node.getTagAttrMap().get(AttrConsts.DATASOURCE);
        String script = node.getTagAttrMap().get(AttrConsts.SCRIPT);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (script != null && !script.isEmpty()) {
            script = (String) executor.visit(script, context);
        } else {
            script = node.getTagBody();
        }
        if (dialectScriptList.isEmpty()) {
            dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, script));
        }
        BindSql bql = executor.sqlScript(datasource, dialectScriptList, context);
        if(executor.isDebug()){
            bql=bql.concat(getTrackingComment(node));
        }
        if (result != null && !result.isEmpty()) {
            executor.visitSet(context, result, bql);
        }
    }

}
