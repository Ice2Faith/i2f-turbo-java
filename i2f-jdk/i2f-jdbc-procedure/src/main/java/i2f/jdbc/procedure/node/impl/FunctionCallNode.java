package i2f.jdbc.procedure.node.impl;


import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class FunctionCallNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "function-call";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);


        Map<String, Object> callParams = null;
        String paramsText = node.getTagAttrMap().get(AttrConsts.PARAMS);
        if (paramsText != null && !paramsText.isEmpty()) {
            Object value = executor.attrValue(AttrConsts.PARAMS, FeatureConsts.VISIT, node, context);
            if (value instanceof Map) {
                callParams = (Map<String, Object>) value;
            }
        }
        if (callParams == null) {
            callParams = executor.newParams(context);
        }
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            if (AttrConsts.PARAMS.equals(name)) {
                continue;
            }
            if (AttrConsts.RESULT.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);
            callParams.put(name, val);
        }


        ExecuteContext callContext = new ExecuteContext();
        callContext.setNodeMap(context.getNodeMap());
        callContext.setParams(callParams);
        try {
            callParams = executor.exec(refid, callContext);
        } catch (ControlSignalException e) {

        } catch (Throwable e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }

        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if(result!=null){
            Object ret = callContext.paramsGet(ParamsConsts.RETURN);
            context.paramsSet(result,ret);
        }
    }

}
