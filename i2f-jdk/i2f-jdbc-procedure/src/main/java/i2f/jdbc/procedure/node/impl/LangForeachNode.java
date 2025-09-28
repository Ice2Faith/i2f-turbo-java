package i2f.jdbc.procedure.node.impl;

import i2f.clock.SystemClock;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangForeachNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FOREACH;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String collection = node.getTagAttrMap().get(AttrConsts.COLLECTION);
        if (collection == null || collection.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.COLLECTION);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String collectionScript = node.getTagAttrMap().get(AttrConsts.COLLECTION);
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
        Object obj = executor.visit(collectionScript, context);
        if (obj == null) {
            return;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(itemName, executor.visit(itemName, context));
        bakParams.put(firstName, executor.visit(firstName, context));
        bakParams.put(indexName, executor.visit(indexName, context));
        try {
            Iterator<?> iterator = null;
            if (obj instanceof Iterator) {
                iterator = (Iterator<?>) obj;
            } else if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                iterator = map.entrySet().iterator();
            } else if (obj instanceof Iterable) {
                Iterable<?> iter = (Iterable<?>) obj;
                iterator = iter.iterator();
            } else if (obj.getClass().isArray()) {
                iterator = new Iterator<Object>() {
                    private int index = 0;
                    private int len = Array.getLength(obj);

                    @Override
                    public boolean hasNext() {
                        return index < len;
                    }

                    @Override
                    public Object next() {
                        return Array.get(obj, index++);
                    }
                };
            } else if (obj instanceof Enumeration) {
                Enumeration<?> iter = (Enumeration<?>) obj;
                iterator = new Iterator<Object>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasMoreElements();
                    }

                    @Override
                    public Object next() {
                        return iter.nextElement();
                    }
                };
            }
            if (iterator != null) {
                Map<String, Object> pointContext = new HashMap<>();
                String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
                String location = getNodeLocation(node);
                boolean isDebugMode = executor.isDebug();
                pointContext.put(POINT_KEY_LOCATION, location);
                pointContext.put(POINT_KEY_IS_DEBUG_MODE, isDebugMode);
                pointContext.put(POINT_KEY_SNAPSHOT_TRACE_ID, snapshotTraceId);

                Iterator<?> iter = iterator;
                boolean isFirst = true;
                int index = 0;
                while (iter.hasNext()) {
                    long bts = SystemClock.currentTimeMillis();

                    pointContext.put(POINT_KEY_BEGIN_TS, bts);
                    pointContext.put(POINT_KEY_LOOP_IS_FIRST, isFirst);
                    pointContext.put(POINT_KEY_LOOP_INDEX, index);
                    pointContext.put(POINT_KEY_LOCATION, location + "@" + index);

                    Object item = iter.next();
                    Object val = executor.resultValue(item, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);

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
            } else {
                throw new ThrowSignalException("object cannot do foreach loop of type: " + obj.getClass() + " on express: " + collectionScript);
            }
        } finally {
            // 还原堆栈
            executor.visitSet(context, itemName, bakParams.get(itemName));
            executor.visitSet(context, firstName, bakParams.get(firstName));
            executor.visitSet(context, indexName, bakParams.get(indexName));
        }

    }

}
