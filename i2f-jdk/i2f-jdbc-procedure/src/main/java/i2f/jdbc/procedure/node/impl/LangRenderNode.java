package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangRenderNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-render".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
        String script = node.getTextBody();
        String val = executor.render(script, params);
        String result = node.getTagAttrMap().get("result");
        if (result != null && !result.isEmpty()) {
            Object res = executor.resultValue(val, node.getAttrFeatureMap().get("result"), node, params, nodeMap);
            executor.setParamsObject(params, result, res);
        }
    }

}
