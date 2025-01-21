package i2f.jdbc.procedure.node.impl;

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
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, JdbcProcedureExecutor executor) {
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

        int begin = (int) executor.applyFeatures(beginExpr, node.getAttrFeatureMap().get("begin"),params,node);
        int end = (int) executor.applyFeatures(endExpr,  node.getAttrFeatureMap().get("end"),params,node);
        int incr = (int) executor.applyFeatures(incrExpr, node.getAttrFeatureMap().get("incr"),params,node);

        boolean loop=begin<end;
        boolean isFirst = true;
        int index = 0;
        for (int j = begin; loop==(j < end); j += incr) {
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
