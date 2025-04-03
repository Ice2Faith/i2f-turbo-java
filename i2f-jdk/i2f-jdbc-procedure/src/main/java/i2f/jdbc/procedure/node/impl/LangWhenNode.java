package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangWhenNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_WHEN;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
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
        if(test!=null && !test.isEmpty()) {
            try {
                GrammarReporter.reportAttributeFeatureGrammar(AttrConsts.TEST,node,FeatureConsts.EVAL,warnPoster);
            } catch (Exception e) {
                warnPoster.accept(TAG_NAME + " attribute " + AttrConsts.TEST+"["+test+"]"+" expression maybe wrong!");
            }
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        boolean ok = (boolean) executor.attrValue(AttrConsts.TEST, FeatureConsts.TEST, node, context);
        if (ok) {
            executor.execAsProcedure(node, context, false, false);
        }
    }


}
