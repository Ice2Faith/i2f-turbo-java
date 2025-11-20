package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangThreadPoolShutdownNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_THREAD_POOL_SHUTDOWN;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String name = node.getTagAttrMap().get(AttrConsts.POOL);
        if (name == null || name.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.POOL);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        ExecutorService pool=(ExecutorService)executor.attrValue(AttrConsts.POOL, FeatureConsts.VISIT, node, context);
        boolean force = executor.convertAs(executor.attrValue(AttrConsts.FORCE, FeatureConsts.BOOLEAN, node, context), Boolean.class);
        boolean await = executor.convertAs(executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context), Boolean.class);
        long timeout = -1;
        String ttl = node.getTagAttrMap().get(AttrConsts.TIMEOUT);
        if (ttl != null && !ttl.isEmpty()) {
            timeout = executor.convertAs(executor.attrValue(AttrConsts.TIMEOUT, FeatureConsts.LONG, node, context), Long.class);
        }
        String timeUnit = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);

        List<Runnable> tasks=new ArrayList<>();
       if(force){
           tasks = pool.shutdownNow();
       }else{
           pool.shutdown();
       }
       if(await){
           try {
               if (timeout >= 0) {
                   TimeUnit unit = NodeTime.getTimeUnit(timeUnit, TimeUnit.SECONDS);
                   pool.awaitTermination(timeout, unit);
               } else {
                   pool.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
               }
           } catch (InterruptedException e) {
               throw new ThrowSignalException(e.getMessage(),e);
           }
       }
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            executor.visitSet(context, result, tasks);
        }
    }
}
