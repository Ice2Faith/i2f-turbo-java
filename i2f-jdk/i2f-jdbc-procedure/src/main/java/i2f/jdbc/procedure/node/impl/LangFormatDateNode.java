package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFormatDateNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-format-date";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue(AttrConsts.VALUE, FeatureConsts.VISIT, node, context);
        String pattern = (String) executor.attrValue(AttrConsts.PATTERN, FeatureConsts.STRING, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (value instanceof Date) {
            SimpleDateFormat fmt = new SimpleDateFormat(pattern);
            value = fmt.format((Date) value);
        } else if (value instanceof Temporal) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            value = formatter.format((Temporal) value);
        }
        if (result != null && !result.isEmpty()) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, value);
        }
    }


}
