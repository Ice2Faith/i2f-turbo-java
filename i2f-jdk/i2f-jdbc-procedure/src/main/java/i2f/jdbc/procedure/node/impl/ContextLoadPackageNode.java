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
public class ContextLoadPackageNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "context-load-package";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String pkg = node.getTagAttrMap().get(AttrConsts.PACKAGE);
        String clazz = node.getTagAttrMap().get(AttrConsts.CLASS);
        if((pkg==null || pkg.isEmpty()) && (clazz==null || clazz.isEmpty())){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.PACKAGE+"/"+AttrConsts.CLASS);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String pkg = node.getTagAttrMap().get(AttrConsts.PACKAGE);
        if (pkg != null && !"".equals(pkg)) {
            if (!pkg.endsWith(".")) {
                pkg = pkg + ".";
            }
            ContextHolder.LOAD_PACKAGE_SET.add(pkg);
        }

        String clazz = node.getTagAttrMap().get(AttrConsts.CLASS);
        if (clazz != null && !"".equals(clazz)) {
            int idx = clazz.lastIndexOf(".");
            if (idx > 0) {
                pkg = clazz.substring(0, idx + 1);
                ContextHolder.LOAD_PACKAGE_SET.add(pkg);
            }
        }

    }
}
