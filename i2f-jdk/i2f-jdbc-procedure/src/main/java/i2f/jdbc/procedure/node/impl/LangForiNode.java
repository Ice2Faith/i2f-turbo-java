package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangForiNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-fori".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String beginExpr = node.getTagAttrMap().get("begin");
        String endExpr = node.getTagAttrMap().get("end");
        String incrExpr = node.getTagAttrMap().get("incr");
        String itemName = node.getTagAttrMap().get("item");
        String firstName = node.getTagAttrMap().get("first");
        String indexName = node.getTagAttrMap().get("index");
        if (itemName == null || itemName.isEmpty()) {
            itemName = "item";
        }
        if (firstName == null || firstName.isEmpty()) {
            firstName = "first";
        }
        if (indexName == null || indexName.isEmpty()) {
            indexName = "index";
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(itemName, context.getParams().get(itemName));
        bakParams.put(firstName, context.getParams().get(firstName));
        bakParams.put(indexName, context.getParams().get(indexName));

        int begin = (int) executor.attrValue("begin", "visit", node, context);
        int end = (int) executor.attrValue("end", "visit", node, context);
        int incr = (int) executor.attrValue("incr", "visit", node, context);

        boolean loop = begin < end;
        boolean isFirst = true;
        int index = 0;
        for (int j = begin; loop == (j < end); j += incr) {
            Object val = j;
            val = executor.resultValue(val, node.getAttrFeatureMap().get("item"), node, context);
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

        // 还原堆栈
        context.getParams().put(itemName, bakParams.get(itemName));
        context.getParams().put(firstName, bakParams.get(firstName));
        context.getParams().put(indexName, bakParams.get(indexName));
    }

}
