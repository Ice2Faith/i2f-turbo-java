package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ContextInvokeMethodClassNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "context-invoke-method-class";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String clazz = node.getTagAttrMap().get(AttrConsts.CLASS);
        if(clazz==null || clazz.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.CLASS);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String clazz = node.getTagAttrMap().get(AttrConsts.CLASS);
        if (clazz != null && !"".equals(clazz)) {
            Class<?> cls = executor.loadClass(clazz);
            if (cls != null) {
                ContextHolder.registryAllInvokeMethods(cls);
            }
        }

    }
}
