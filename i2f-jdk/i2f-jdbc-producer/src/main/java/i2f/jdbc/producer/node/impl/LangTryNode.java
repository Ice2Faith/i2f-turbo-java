package i2f.jdbc.producer.node.impl;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.node.ExecutorNode;
import i2f.jdbc.producer.parser.data.XmlNode;

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
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap, SqlProducerExecutor executor) {
        List<XmlNode> nodes = node.getChildren();
        XmlNode bodyNode = null;
        List<XmlNode> catchNodes = new ArrayList<>();
        XmlNode finallyNode = null;
        for (XmlNode itemNode : nodes) {
            if (!"element".equals(itemNode.getTagName())) {
                continue;
            }
            if ("lang-body".equals(itemNode.getTagName())) {
                bodyNode = itemNode;
            }
            if ("lang-catch".equals(itemNode.getTagName())) {
                catchNodes.add(itemNode);
            }
            if ("lang-finally".equals(itemNode.getTagName())) {
                finallyNode = itemNode;
            }
        }

        if (bodyNode == null) {
            throw new IllegalArgumentException("missing try segment body!");
        }

        try {
            executor.execAsProducer(bodyNode, params, nodeMap);
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
                bakParams.put(exName, params.get(exName));

                Class<?> clazz = executor.loadClass(type);
                if (clazz == null) {
                    throw new IllegalStateException("missing catch exception type of : " + type);
                }
                if (clazz.isAssignableFrom(e.getClass())) {
                    executor.execAsProducer(catchNode, params, nodeMap);
                    handled = true;
                }

                // 还原堆栈
                params.put(exName, bakParams.get(exName));
            }
            if (!handled) {
                throw e;
            }
        } finally {
            if (finallyNode != null) {
                executor.execAsProducer(finallyNode, params, nodeMap);
            }
        }
    }
}
