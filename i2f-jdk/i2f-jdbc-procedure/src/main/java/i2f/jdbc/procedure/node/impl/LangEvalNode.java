package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;

import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-eval";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        String script = node.getTextBody();
        if((value==null || value.isEmpty()) && (script==null || script.isEmpty())){
            String errorMsg="missing value attribute or element body";
            warnPoster.accept("xproc4j report xml grammar, at "+node.getLocationFile()+":"+node.getLocationLineNumber()+" error: "+errorMsg);
            return;
        }
        if(value!=null && !value.isEmpty()){
            try{
                GrammarReporter.reportExprFeatureGrammar(value,FeatureConsts.EVAL,node,"attribute "+AttrConsts.VALUE,warnPoster);
            }catch(Exception e){
                String errorMsg="attribute "+AttrConsts.VALUE+" expression error: "+e.getMessage();
                warnPoster.accept("xproc4j report xml grammar, at "+node.getLocationFile()+":"+node.getLocationLineNumber()+" error: "+errorMsg);
            }
        }
        if(script!=null && !script.isEmpty()){
            try{
                GrammarReporter.reportExprFeatureGrammar(script,FeatureConsts.EVAL,node,"element body",warnPoster);
            }catch(Exception e){
                String errorMsg="element body expression error: "+e.getMessage();
                warnPoster.accept("xproc4j report xml grammar, at "+node.getLocationFile()+":"+node.getLocationLineNumber()+" error: "+errorMsg);
            }
        }
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String value = node.getTagAttrMap().get(AttrConsts.VALUE);
        String script = node.getTextBody();
        if (value != null && !value.isEmpty()) {
            script = (String) executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        }
        Object val = executor.eval(script, context.getParams());
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null && !result.isEmpty()) {
            val = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, val);
        }
    }

}
