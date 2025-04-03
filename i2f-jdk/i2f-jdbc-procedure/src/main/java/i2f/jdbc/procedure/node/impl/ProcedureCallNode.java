package i2f.jdbc.procedure.node.impl;


import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ProcedureCallNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.PROCEDURE_CALL;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
        if(refid==null || refid.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.REFID);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String refid = (String)executor.attrValue(AttrConsts.REFID,FeatureConsts.STRING,node,context);
        Boolean paramsShare=(Boolean) executor.attrValue(AttrConsts.PARAMS_SHARE,FeatureConsts.BOOLEAN,node,context);

        boolean needPrepareParams=true;
        Map<String, Object> callParams = null;
        String paramsText = node.getTagAttrMap().get(AttrConsts.PARAMS);
        if (paramsText != null && !paramsText.isEmpty()) {
            Object value = executor.attrValue(AttrConsts.PARAMS, FeatureConsts.VISIT, node, context);
            if (value instanceof Map) {
                callParams = (Map<String, Object>) value;
            }
        }
        if(paramsShare!=null && paramsShare){
            callParams=context;
            needPrepareParams=false;
        }
        if (callParams == null) {
            callParams = executor.newParams(context);
            needPrepareParams=false;
        }
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            if (AttrConsts.PARAMS.equals(name)) {
                continue;
            }
            if(AttrConsts.PARAMS_SHARE.equals(name)){
                continue;
            }
            if (AttrConsts.RESULT.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
            callParams.put(name, val);
        }

        if(needPrepareParams) {
            executor.prepareParams(callParams);
        }

        try {
            callParams = executor.exec(refid, callParams, false, false);
        } catch (ControlSignalException e) {

        } catch (Throwable e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }

        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if(result!=null && !result.isEmpty()){
            executor.visitSet(context,result,callParams);
        }
    }

}
