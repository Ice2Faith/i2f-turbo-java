package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.executor.SqlProcedureExecutor;
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
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProcedureExecutor executor) {
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
        bakParams.put(itemName, params.get(itemName));
        bakParams.put(firstName, params.get(firstName));
        bakParams.put(indexName, params.get(indexName));

        int begin = (int) executor.eval(beginExpr, params);
        int end = (int) executor.eval(endExpr, params);
        int incr = (int) executor.eval(incrExpr, params);

        boolean isFirst = true;
        int index = 0;
        for (int j = begin; j < end; j += incr) {
            // 覆盖堆栈
            params.put(itemName, j);
            params.put(firstName, isFirst);
            params.put(indexName, index);
            try {
                executor.execAsProducer(node, params, nodeMap);
            } catch (ContinueSignalException e) {
                continue;
            } catch (BreakSignalException e) {
                break;
            }
            isFirst = false;
            index++;
        }

        // 还原堆栈
        params.put(itemName, bakParams.get(itemName));
        params.put(firstName, bakParams.get(firstName));
        params.put(indexName, bakParams.get(indexName));
    }

}
