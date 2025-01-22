package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlCursorNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "sql-cursor".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        XmlNode queryNode = null;
        XmlNode bodyNode = null;
        for (XmlNode item : children) {
            if ("sql-query-list".equals(item.getTagName())) {
                queryNode = item;
            }
            if ("lang-body".equals(item.getTagName())) {
                bodyNode = item;
            }
        }

        if (queryNode == null) {
            throw new IllegalStateException("missing cursor query node!");
        }

        if (bodyNode == null) {
            throw new IllegalStateException("missing cursor body node!");
        }

        Integer batchSize = (Integer) executor.attrValue("batch-size", "int", node, context);
        String item = node.getTagAttrMap().get("item");
        if (item == null || item.isEmpty()) {
            item = "item";
        }

        String datasource = (String) executor.attrValue("datasource", "visit", node, context);
        String script = (String) executor.attrValue("script", "visit", node, context);
        String resultTypeName = (String) executor.attrValue("result-type", "visit", node, context);
        Class<?> resultType = executor.loadClass(resultTypeName);
        if (resultType == null) {
            resultType = Map.class;
        }
        if (script == null || script.isEmpty()) {
            script = node.getTagBody();
        }

        int pageIndex = 0;
        if (batchSize == null || batchSize <= 0) {
            batchSize = 2000;
        }
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(item, executor.visit(item, context.getParams()));
        while (true) {
            List<?> list = executor.sqlQueryPage(datasource, script, context.getParams(), resultType, pageIndex, batchSize);
            if (list.isEmpty()) {
                break;
            }

            boolean breakSignal = false;
            int count = 0;
            for (Object obj : list) {
                count++;
                try {
                    executor.setParamsObject(context.getParams(), item, obj);
                } catch (ContinueSignalException e) {
                    continue;
                } catch (BreakSignalException e) {
                    breakSignal = true;
                    break;
                }
            }

            if (breakSignal) {
                break;
            }

            if (count < batchSize) {
                break;
            }

            pageIndex++;
        }

        for (Map.Entry<String, Object> entry : bakParams.entrySet()) {
            executor.setParamsObject(context.getParams(), entry.getKey(), entry.getValue());
        }
    }
}
