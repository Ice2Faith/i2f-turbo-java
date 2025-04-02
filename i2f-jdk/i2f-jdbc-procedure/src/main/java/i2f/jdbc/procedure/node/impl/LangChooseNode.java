package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangChooseNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_CHOOSE;

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        List<XmlNode> list = node.getChildren();
        if(list==null ||list.isEmpty()){
            return;
        }
        for (XmlNode itemNode : list) {
            String type = itemNode.getNodeType();
            if (!XmlNode.NODE_ELEMENT.equals(type)) {
                continue;
            }
            if (TagConsts.LANG_WHEN.equals(itemNode.getTagName())) {
                String test=itemNode.getTagAttrMap().get(AttrConsts.TEST);
                if(test!=null && !test.isEmpty()) {
                    try {
                        GrammarReporter.reportAttributeFeatureGrammar(AttrConsts.TEST,node,FeatureConsts.EVAL,warnPoster);
                    } catch (Exception e) {
                        warnPoster.accept(TAG_NAME + " attribute " + AttrConsts.TEST+"["+test+"]"+" expression maybe wrong!");
                    }
                }
            }
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        List<XmlNode> list = node.getChildren();
        if(list==null ||list.isEmpty()){
            return;
        }
        XmlNode testNode = null;
        XmlNode otherNode = null;
        for (XmlNode itemNode : list) {
            String type = itemNode.getNodeType();
            if (!XmlNode.NODE_ELEMENT.equals(type)) {
                continue;
            }
            if (TagConsts.LANG_WHEN.equals(itemNode.getTagName())) {
                if (testNode == null) {
                    boolean ok = (boolean) executor.attrValue(AttrConsts.TEST, FeatureConsts.TEST, itemNode, context);
                    if (ok) {
                        testNode = itemNode;
                    }
                }
            } else if (TagConsts.LANG_OTHERWISE.equals(itemNode.getTagName())) {
                otherNode = itemNode;
            }
        }
        XmlNode invokeNode = null;
        if (testNode != null) {
            invokeNode = testNode;
        } else {
            invokeNode = otherNode;
        }
        if (invokeNode != null) {
            executor.execAsProcedure(invokeNode, context, false, false);
        }
    }

}
