package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangForiNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-fori";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
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
        bakParams.put(itemName, context.getParams().get(itemName));
        bakParams.put(firstName, context.getParams().get(firstName));
        bakParams.put(indexName, context.getParams().get(indexName));

        int begin = 0;
        if (beginExpr != null && !beginExpr.isEmpty()) {
            begin = (int) executor.attrValue(AttrConsts.BEGIN, FeatureConsts.INT, node, context);
        }
        int end = (int) executor.attrValue(AttrConsts.END, FeatureConsts.INT, node, context);
        int incr = 1;
        if (endExpr != null && !endExpr.isEmpty()) {
            end = (int) executor.attrValue(AttrConsts.INCR, FeatureConsts.INT, node, context);
        }

        boolean loop = begin < end;
        boolean isFirst = true;
        int index = 0;
        for (int j = begin; loop == (j < end); j += incr) {
            Object val = j;
            val = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.ITEM), node, context);
            // 覆盖堆栈
            context.getParams().put(itemName, val);
            context.getParams().put(firstName, isFirst);
            context.getParams().put(indexName, index);
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

        // 还原堆栈
        context.getParams().put(itemName, bakParams.get(itemName));
        context.getParams().put(firstName, bakParams.get(firstName));
        context.getParams().put(indexName, bakParams.get(indexName));
    }

}
