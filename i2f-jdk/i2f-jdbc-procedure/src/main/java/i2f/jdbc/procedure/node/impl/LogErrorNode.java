package i2f.jdbc.procedure.node.impl;


import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LogErrorNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LOG_ERROR;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {

    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {

        String value = (String) executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        Throwable e = (Throwable) executor.attrValue(AttrConsts.E, FeatureConsts.VISIT, node, context);

        executor.logger().logError(() -> {

            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;
            if (value != null) {
                builder.append(value);
                isFirst = false;
            }
            for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
                String name = entry.getKey();
                if (AttrConsts.VALUE.equals(name)) {
                    continue;
                }
                if (AttrConsts.E.equals(name)) {
                    continue;
                }
                String script = entry.getValue();
                Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(val).append("(").append(val == null ? "null" : val.getClass().getSimpleName()).append(")");
                isFirst = false;
            }

            return builder.toString();
        }, e);
    }

}
