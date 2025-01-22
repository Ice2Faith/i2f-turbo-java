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
public class LangWhileNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-while".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String script = node.getTagAttrMap().get("test");
        String firstName = node.getTagAttrMap().get("first");
        String indexName = node.getTagAttrMap().get("index");
        if (firstName == null || firstName.isEmpty()) {
            firstName = "first";
        }
        if (indexName == null || indexName.isEmpty()) {
            indexName = "index";
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(firstName, context.getParams().get(firstName));
        bakParams.put(indexName, context.getParams().get(indexName));

        boolean isFirst = true;
        int index = 0;
        while ((boolean) executor.attrValue("test", "test", node, context)) {
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
        context.getParams().put(firstName, bakParams.get(firstName));
        context.getParams().put(indexName, bakParams.get(indexName));
    }

}
