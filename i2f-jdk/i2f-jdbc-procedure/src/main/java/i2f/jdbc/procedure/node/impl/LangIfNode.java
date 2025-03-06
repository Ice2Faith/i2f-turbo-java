package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangIfNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-if";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String test = node.getTagAttrMap().get(AttrConsts.TEST);
        if(test==null || test.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.TEST);
        }
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        boolean ok = (boolean) executor.attrValue(AttrConsts.TEST, FeatureConsts.TEST, node, context);
        if (ok) {
            executor.execAsProcedure(node, context, false, false);
        }
    }


}
