package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
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
public class LangStringJoinNode implements ExecutorNode {
    public static final String TAG_NAME="lang-string-join";
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        Object separator = executor.attrValue(AttrConsts.SEPARATOR, FeatureConsts.STRING, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<Integer, String>> argList = new ArrayList<>();
        Map<String, String> attrMap = node.getTagAttrMap();
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            String key = entry.getKey();
            if(!key.startsWith(AttrConsts.ARG)){
                continue;
            }
            int idx = Integer.parseInt(key.substring(AttrConsts.ARG.length()));
            argList.add(new AbstractMap.SimpleEntry<>(idx, entry.getValue()));
        }
        argList.sort((o1, o2) -> Integer.compare(o1.getKey(), o2.getKey()));

        boolean isFirst = true;
        for (Map.Entry<Integer, String> entry : argList) {
            String attrName = AttrConsts.ARG + entry.getKey();
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            Object val = executor.attrValue(attrName, FeatureConsts.VISIT, node, context);
            builder.append(val);
            isFirst = false;
        }

        Object value = builder.toString();
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, value);
        }
    }


}
