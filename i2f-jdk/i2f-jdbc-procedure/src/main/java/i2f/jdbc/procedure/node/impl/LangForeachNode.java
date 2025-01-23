package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangForeachNode implements ExecutorNode {
    public static final String TAG_NAME="lang-foreach";
    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
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
        Object obj = executor.visit(collectionScript, context.getParams());
        if (obj == null) {
            return;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(itemName, context.getParams().get(itemName));
        bakParams.put(firstName, context.getParams().get(firstName));
        bakParams.put(indexName, context.getParams().get(indexName));
        if (obj instanceof Iterable) {
            Iterable<?> iter = (Iterable<?>) obj;
            boolean isFirst = true;
            int index = 0;
            for (Object item : iter) {
                Object val = executor.resultValue(item, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);
                // 覆盖堆栈
                context.getParams().put(itemName, val);
                context.getParams().put(firstName, isFirst);
                context.getParams().put(indexName, index);
                isFirst = false;
                index++;
                try {
                    executor.execAsProcedure(node, context);
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
                context.getParams().put(itemName, val);
                context.getParams().put(firstName, isFirst);
                context.getParams().put(indexName, index);
                isFirst = false;
                index++;
                try {
                    executor.execAsProcedure(node, context);
                } catch (ContinueSignalException e) {
                    continue;
                } catch (BreakSignalException e) {
                    break;
                }
            }
        }
        // 还原堆栈
        context.getParams().put(itemName, bakParams.get(itemName));
        context.getParams().put(firstName, bakParams.get(firstName));
        context.getParams().put(indexName, bakParams.get(indexName));
    }

}
