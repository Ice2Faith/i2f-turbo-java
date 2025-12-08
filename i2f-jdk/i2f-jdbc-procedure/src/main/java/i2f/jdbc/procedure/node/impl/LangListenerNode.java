package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.event.ExecutorContextEvent;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.typeof.TypeOf;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/8 18:01
 * @desc
 */
public class LangListenerNode extends AbstractExecutorNode {
    public static final String TAG_NAME= TagConsts.LANG_LISTENER;
    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String name = executor.convertAs(executor.attrValue(AttrConsts.NAME, FeatureConsts.STRING, node, context),String.class);
        String type = executor.convertAs(executor.attrValue(AttrConsts.TYPE, FeatureConsts.STRING, node, context), String.class);
        String target=executor.convertAs(executor.attrValue(AttrConsts.TARGET,FeatureConsts.STRING,node,context),String.class);

        if(type==null || type.isEmpty()){
            type= XProc4jEvent.class.getName();
        }
        if(target==null||target.isEmpty()){
            target="target";
        }
        Class<?> typeClass=executor.loadClass(type);

        String typeName=type;
        String targetName=target;
        Map<String, XProc4jEventListener> listeners = executor.visitAs(ParamsConsts.LISTENERS, context);
        listeners.put(name, new XProc4jEventListener() {
            @Override
            public boolean support(XProc4jEvent event) {
                if(typeClass!=null) {
                    return TypeOf.typeOf(event.getClass(), typeClass);
                }else{
                    return event.getClass().getName().equals(typeName);
                }
            }

            @Override
            public boolean handle(XProc4jEvent event) {
                if(event instanceof ExecutorContextEvent){
                    ExecutorContextEvent evt = (ExecutorContextEvent) event;
                    JdbcProcedureExecutor evtExecutor = evt.getExecutor();
                    Map<String, Object> evtContext = evt.getContext();
                    Map<String, Object> callParams = evtExecutor.newParams(evtContext);
                    evtExecutor.visitSet(callParams,targetName,evt);
                    evtExecutor.execAsProcedure(node,callParams);
                }else{
                    Map<String, Object> callParams = executor.createParams();
                    executor.visitSet(callParams,targetName,event);
                    executor.execAsProcedure(node,callParams);
                }
                return false;
            }
        });
    }



}
