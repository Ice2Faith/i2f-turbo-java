package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangThreadPoolNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_THREAD_POOL;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String count = node.getTagAttrMap().get(AttrConsts.COUNT);
        if (count == null || count.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.COUNT);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        int count = executor.convertAs(executor.attrValue(AttrConsts.COUNT, FeatureConsts.INT, node, context), Integer.class);
        String type = executor.convertAs(executor.attrValue(AttrConsts.TYPE, FeatureConsts.STRING, node, context), String.class);
        ExecutorService pool = null;
        if ("forkjoin".equalsIgnoreCase(type)) {
            pool = new ForkJoinPool(count);
        } else {
            pool = Executors.newFixedThreadPool(count);
        }
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            executor.visitSet(context, result, pool);
        }
    }
}
