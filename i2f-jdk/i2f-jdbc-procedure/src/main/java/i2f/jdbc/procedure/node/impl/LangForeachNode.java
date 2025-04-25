package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
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
        if (obj instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) obj;
            boolean isFirst = true;
            int index = 0;
            for (Object item : iter) {
                Object val = executor.resultValue(item, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);
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
                }

            }
        } else if (obj.getClass().isArray()) {
            boolean isFirst = true;
            int index = 0;
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object val = Array.get(obj, i);
                val = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);

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
                }
            }
        }
        // 还原堆栈
        executor.visitSet(context, itemName, bakParams.get(itemName));
        executor.visitSet(context, firstName, bakParams.get(firstName));
        executor.visitSet(context, indexName, bakParams.get(indexName));
    }

}
