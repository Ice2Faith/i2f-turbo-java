package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.MatchException;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangRetryNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_RETRY;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {

    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Integer count = executor.convertAs(executor.attrValue(AttrConsts.COUNT, FeatureConsts.INT, node, context), Integer.class);
        String type = executor.convertAs(executor.attrValue(AttrConsts.TYPE, FeatureConsts.STRING, node, context), String.class);
        Long delay = executor.convertAs(executor.attrValue(AttrConsts.DELAY, FeatureConsts.LONG, node, context), Long.class);
        String unit = executor.convertAs(executor.attrValue(AttrConsts.TIME_UNIT, FeatureConsts.STRING, node, context), String.class);
        Double incr = executor.convertAs(executor.attrValue(AttrConsts.INCR, FeatureConsts.DOUBLE, node, context), Double.class);
        if (count == null) {
            count = 1;
        }
        if (delay == null) {
            delay = 30L;
        }
        if (incr == null) {
            incr = 1.0;
        }
        TimeUnit timeUnit = NodeTime.getTimeUnit(unit, TimeUnit.MILLISECONDS);
        if (type == null || type.isEmpty()) {
            type = "java.lang.Throwable";
        }
        List<Class<?>> catchClasses = new ArrayList<>();
        if (type != null) {
            type = type.trim();
            String[] arr = type.split("[,;\\|]");
            for (String item : arr) {
                item = item.trim();
                try {
                    Class<?> clazz = executor.loadClass(item);
                    if (clazz != null) {
                        catchClasses.add(clazz);
                    }
                } catch (Throwable ex) {

                }
            }
        }
        Throwable ex = null;
        int tryCount = 0;
        double sleepTime = timeUnit.toMillis(delay);
        while (true) {
            if (tryCount >= count) {
                break;
            }
            tryCount++;
            try {
                executor.execAsProcedure(node, context, false, false);
                break;
            } catch (Throwable e) {
                ex = e;
                Map.Entry<Throwable, Boolean> matched = MatchException.matchException(e, node.getAttrFeatureMap().get(AttrConsts.TYPE), catchClasses);
                if (!matched.getValue()) {
                    break;
                }
            }
            try {
                Thread.sleep((long) sleepTime);
            } catch (InterruptedException e) {

            }
            sleepTime = sleepTime * incr;
        }

        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object res = executor.resultValue(tryCount, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }

        if (ex != null) {
            throw new ThrowSignalException(ex);
        }

    }
}
