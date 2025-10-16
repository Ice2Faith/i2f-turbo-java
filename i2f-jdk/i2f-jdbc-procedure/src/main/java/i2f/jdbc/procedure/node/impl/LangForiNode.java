package i2f.jdbc.procedure.node.impl;

import i2f.clock.SystemClock;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangForiNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FORI;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String end = node.getTagAttrMap().get(AttrConsts.END);
        if (end == null || end.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.END);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String beginExpr = node.getTagAttrMap().get(AttrConsts.BEGIN);
        String endExpr = node.getTagAttrMap().get(AttrConsts.END);
        String incrExpr = node.getTagAttrMap().get(AttrConsts.INCR);
        String itemName = node.getTagAttrMap().get(AttrConsts.ITEM);
        String firstName = node.getTagAttrMap().get(AttrConsts.FIRST);
        String indexName = node.getTagAttrMap().get(AttrConsts.INDEX);
        if (itemName == null || itemName.isEmpty()) {
            itemName = AttrConsts.ITEM;
        }
        if (firstName == null || firstName.isEmpty()) {
            firstName = AttrConsts.FIRST;
        }
        if (indexName == null || indexName.isEmpty()) {
            indexName = AttrConsts.INDEX;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(itemName, executor.visit(itemName, context));
        bakParams.put(firstName, executor.visit(firstName, context));
        bakParams.put(indexName, executor.visit(indexName, context));

        int begin = 0;
        if (beginExpr != null) {
            begin = executor.convertAs(executor.attrValue(AttrConsts.BEGIN, FeatureConsts.INT, node, context), Integer.class);
        }
        int end = executor.convertAs(executor.attrValue(AttrConsts.END, FeatureConsts.INT, node, context), Integer.class);
        int incr = 1;
        if (incrExpr != null) {
            incr = executor.convertAs(executor.attrValue(AttrConsts.INCR, FeatureConsts.INT, node, context), Integer.class);
        }

        Map<String, Object> pointContext = new HashMap<>();
        String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        String location = getNodeLocation(node);
        boolean isDebugMode = executor.isDebug();
        pointContext.put(POINT_KEY_LOCATION, location);
        pointContext.put(POINT_KEY_IS_DEBUG_MODE, isDebugMode);
        pointContext.put(POINT_KEY_SNAPSHOT_TRACE_ID, snapshotTraceId);

        boolean isFirst = true;
        int index = 0;
        for (int j = begin; j < end; j += incr) {
            long bts = SystemClock.currentTimeMillis();

            pointContext.put(POINT_KEY_BEGIN_TS, bts);
            pointContext.put(POINT_KEY_LOOP_IS_FIRST, isFirst);
            pointContext.put(POINT_KEY_LOOP_INDEX, index);
            pointContext.put(POINT_KEY_LOCATION, location + "@" + index);

            Object val = j;
            val = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);

            pointContext.put(POINT_KEY_LOOP_VALUE, val);
            // 覆盖堆栈
            executor.visitSet(context, itemName, val);
            executor.visitSet(context, firstName, isFirst);
            executor.visitSet(context, indexName, index);
            isFirst = false;
            index++;
            try {
                executor.execAsProcedure(node, context, false, false);
            } catch (ContinueSignalException e) {
                continue;
            } catch (BreakSignalException e) {
                break;
            } finally {
                long ets = SystemClock.currentTimeMillis();
                long useTs = ets - bts;
                XmlExecUseTimeEvent event = new XmlExecUseTimeEvent();
                event.setExecutorNode(this);
                event.setPointContext(pointContext);
                event.setNode(node);
                event.setContext(context);
                event.setExecutor(executor);
                executor.sendEvent(event);
                event.setUseTs(useTs);
                executor.publishEvent(event);
            }
        }

        // 还原堆栈
        executor.visitSet(context, itemName, bakParams.get(itemName));
        executor.visitSet(context, firstName, bakParams.get(firstName));
        executor.visitSet(context, indexName, bakParams.get(indexName));
    }

}
