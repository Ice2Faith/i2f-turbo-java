package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.reflect.ReflectResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class JavaCallNode implements ExecutorNode {
    public static final String TAG_NAME = "java-call";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String targetExpression = node.getTagAttrMap().get(AttrConsts.TARGET);
        Map<String,Object> beanMap = context.paramsComputeIfAbsent(ParamsConsts.BEANS,(key)->new HashMap<>());
        Object target=beanMap.get(targetExpression);
        if(target==null || (!(target instanceof JdbcProcedureJavaCaller))){
            target=executor.attrValue(AttrConsts.TARGET, FeatureConsts.VISIT,node,context);
        }
        if(target==null || (!(target instanceof JdbcProcedureJavaCaller))){
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Object bean = entry.getValue();
                if(bean instanceof JdbcProcedureJavaCaller){
                    String fullName = bean.getClass().getName();
                    if(fullName.equals(targetExpression)){
                        target=bean;
                        break;
                    }
                    String simpleName = bean.getClass().getSimpleName();
                    if(simpleName.equals(targetExpression)){
                        target=bean;
                        break;
                    }
                }
            }
        }
        if(target==null || (!(target instanceof JdbcProcedureJavaCaller))){
            try {
                Class<?> clazz = executor.loadClass(targetExpression);
                if(clazz!=null){
                    target= ReflectResolver.getInstance(clazz);
                }
            } catch (IllegalAccessException e) {

            }
        }
        if(target==null || (!(target instanceof JdbcProcedureJavaCaller))){
            return;
        }
        try {
            JdbcProcedureJavaCaller caller=(JdbcProcedureJavaCaller)target;
            Object val = caller.exec(context, executor, context.getParams());
            String result = node.getTagAttrMap().get(AttrConsts.RESULT);
            if (result != null && !result.isEmpty()) {
                Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                executor.setParamsObject(context.getParams(), result, res);
            }
        } catch (Throwable e) {
            throw new ThrowSignalException(e.getMessage(),e);
        }
    }
}
