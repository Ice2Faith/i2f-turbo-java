package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ContextInvokeMethodClassNode implements ExecutorNode {
    public static final String TAG_NAME = "context-invoke-method-class";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String clazz = node.getTagAttrMap().get(AttrConsts.CLASS);
        if (clazz != null && !"".equals(clazz)) {
            Class<?> cls = executor.loadClass(clazz);
            if (cls != null) {
                ContextHolder.registryAllInvokeMethods(cls);
            }
        }

    }
}
