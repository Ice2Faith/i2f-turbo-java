package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFormatDateNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FORMAT_DATE;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        if (value == null || value.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.VALUE);
        }
        String pattern = node.getTagAttrMap().get(AttrConsts.PATTERN);
        if (pattern == null || pattern.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.PATTERN);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
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
        if (result != null) {
            value = executor.resultValue(value, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, value);
        }
    }


}
