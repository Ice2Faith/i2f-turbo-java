package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangTryNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-try";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> nodes = node.getChildren();
        if (nodes == null) {
            return;
        }
        XmlNode bodyNode = null;
        List<XmlNode> catchNodes = new ArrayList<>();
        XmlNode finallyNode = null;
        for (XmlNode item : nodes) {
            if (!XmlNode.NODE_ELEMENT.equals(item.getNodeType())) {
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
            executor.execAsProcedure(bodyNode, context, false, false);
        } catch (Throwable e) {
            boolean handled = false;
            for (XmlNode catchNode : catchNodes) {
                String type = catchNode.getTagAttrMap().get(AttrConsts.TYPE);
                String exName = catchNode.getTagAttrMap().get(AttrConsts.E);
                if (type == null || type.isEmpty()) {
                    type = "java.lang.Throwable";
                }
                type=type.trim();
                if (exName == null || exName.isEmpty()) {
                    exName = AttrConsts.E;
                }
                // 备份堆栈
                Map<String, Object> bakParams = new LinkedHashMap<>();
                bakParams.put(exName, context.getParams().get(exName));

                String[] arr = type.split("\\|");
                for (String item : arr) {
                    String clsName=item.trim();
                    if(clsName.isEmpty()){
                        continue;
                    }
                    Class<?> clazz = executor.loadClass(item);
                    if (clazz == null) {
                        throw new IllegalStateException("missing catch exception type of : " + item);
                    }
                    if (clazz.isAssignableFrom(e.getClass())) {
                        executor.execAsProcedure(catchNode, context, false, false);
                        handled = true;
                        break;
                    }
                }


                // 还原堆栈
                context.getParams().put(exName, bakParams.get(exName));
            }
            if (!handled) {
                throw e;
            }
        } finally {
            if (finallyNode != null) {
                executor.execAsProcedure(finallyNode, context, false, false);
            }
        }
    }
}
