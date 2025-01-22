package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangTryNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-try".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> nodes = node.getChildren();
        if (nodes == null) {
            return;
        }
        XmlNode bodyNode = null;
        List<XmlNode> catchNodes = new ArrayList<>();
        XmlNode finallyNode = null;
        for (XmlNode item : nodes) {
            if (!"element".equals(item.getNodeType())) {
                continue;
            }
            if ("lang-body".equals(item.getTagName())) {
                bodyNode = item;
            }
            if ("lang-catch".equals(item.getTagName())) {
                catchNodes.add(item);
            }
            if ("lang-finally".equals(item.getTagName())) {
                finallyNode = item;
            }
        }

        if (bodyNode == null) {
            throw new IllegalArgumentException("missing try segment body!");
        }

        try {
            executor.execAsProcedure(bodyNode, context);
        } catch (Throwable e) {
            boolean handled = false;
            for (XmlNode catchNode : catchNodes) {
                String type = catchNode.getTagAttrMap().get("type");
                String exName = catchNode.getTagAttrMap().get("e");
                if (exName == null || exName.isEmpty()) {
                    exName = "e";
                }
                // 备份堆栈
                Map<String, Object> bakParams = new LinkedHashMap<>();
                bakParams.put(exName, context.getParams().get(exName));

                Class<?> clazz = executor.loadClass(type);
                if (clazz == null) {
                    throw new IllegalStateException("missing catch exception type of : " + type);
                }
                if (clazz.isAssignableFrom(e.getClass())) {
                    executor.execAsProcedure(catchNode, context);
                    handled = true;
                }

                // 还原堆栈
                context.getParams().put(exName, bakParams.get(exName));
            }
            if (!handled) {
                throw e;
            }
        } finally {
            if (finallyNode != null) {
                executor.execAsProcedure(finallyNode, context);
            }
        }
    }
}
