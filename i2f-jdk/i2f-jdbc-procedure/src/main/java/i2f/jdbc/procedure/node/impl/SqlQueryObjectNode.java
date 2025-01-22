package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlQueryObjectNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-query-object".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<Map.Entry<String,String>> dialectScriptList=new ArrayList<>();
        List<XmlNode> children = node.getChildren();
        if(children!=null) {
            for (XmlNode item : children) {
                if ("sql-dialect".equals(item.getTagName())) {
                    String databases = item.getTagAttrMap().get("databases");
                    String script = node.getTagAttrMap().get("script");
                    if (script != null && !script.isEmpty()) {
                        script = (String) executor.visit(script, context.getParams());
                    } else {
                        script = node.getTagBody();
                    }
                    dialectScriptList.add(new AbstractMap.SimpleEntry<>(databases, script));
                }
            }
        }
        String datasource = node.getTagAttrMap().get("datasource");
        String script = node.getTagAttrMap().get("script");
        String result = node.getTagAttrMap().get("result");
        String resultTypeName = node.getTagAttrMap().get("result-type");
        Class<?> resultType = executor.loadClass(resultTypeName);
        if (resultType == null) {
            resultType = Map.class;
        }
        if (script != null && !script.isEmpty()) {
            script = (String) executor.visit(script, context.getParams());
        } else {
            script = node.getTagBody();
        }
        if(dialectScriptList.isEmpty()){
            dialectScriptList.add(new AbstractMap.SimpleEntry<>(null,script));
        }
        Object obj = executor.sqlQueryObject(datasource, dialectScriptList, context.getParams(), resultType);
        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }
    }


}
