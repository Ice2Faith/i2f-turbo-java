package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangTryNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_TRY;

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        List<XmlNode> nodes = node.getChildren();
        if (nodes == null) {
            warnPoster.accept(TAG_NAME+" missing child element");
            return;
        }
        XmlNode bodyNode = null;
        for (XmlNode item : nodes) {
            if (!XmlNode.NODE_ELEMENT.equals(item.getNodeType())) {
                continue;
            }
            if (TagConsts.LANG_BODY.equals(item.getTagName())) {
                bodyNode = item;
            }
        }

        if (bodyNode == null) {
            warnPoster.accept(TAG_NAME+" missing try segment body lang-body element!");
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        List<XmlNode> nodes = node.getChildren();
        if (nodes == null) {
            return;
        }
        XmlNode bodyNode = null;
        List<XmlNode> catchNodes = new ArrayList<>();
        XmlNode finallyNode = null;
        for (XmlNode item : nodes) {
            if (!XmlNode.NODE_ELEMENT.equals(item.getNodeType())) {
                continue;
            }
            if (TagConsts.LANG_BODY.equals(item.getTagName())) {
                bodyNode = item;
            }
            if (TagConsts.LANG_CATCH.equals(item.getTagName())) {
                catchNodes.add(item);
            }
            if (TagConsts.LANG_FINALLY.equals(item.getTagName())) {
                finallyNode = item;
            }
        }

        if (bodyNode == null) {
            throw new IllegalArgumentException("missing try segment body!");
        }

        try {
            executor.execAsProcedure(bodyNode, context, false, false);
        } catch (Throwable e) {
            if(e instanceof ControlSignalException){
                throw (ControlSignalException)e;
            }
            if(e instanceof ThrowSignalException){
                ThrowSignalException ex = (ThrowSignalException) e;
                Throwable cause = ex.getCause();
                if(cause!=null){
                    e=cause;
                }
            }
            if(e instanceof SignalException){
                SignalException ex = (SignalException) e;
                Throwable cause = ex.getCause();
                if(cause!=null){
                    e=cause;
                }
            }

            String traceErrMsg=e.getClass().getName()+": "+e.getMessage();
            executor.visitSet(context, ParamsConsts.TRACE_ERRMSG,traceErrMsg);
            ContextHolder.TRACE_ERRMSG.set(traceErrMsg);

            boolean handled = false;
            for (XmlNode catchNode : catchNodes) {
                String type = catchNode.getTagAttrMap().get(AttrConsts.TYPE);
                String exName = catchNode.getTagAttrMap().get(AttrConsts.E);
                if (type == null || type.isEmpty()) {
                    type = "java.lang.Throwable";
                }
                type=type.trim();
                if (exName == null || exName.isEmpty()) {
                    exName = AttrConsts.E;
                }
                // 备份堆栈
                Map<String, Object> bakParams = new LinkedHashMap<>();
                bakParams.put(exName, context.get(exName));

                executor.visitSet(context,exName,e);

                String[] arr = type.split("\\|");
                for (String item : arr) {
                    String clsName=item.trim();
                    if(clsName.isEmpty()){
                        continue;
                    }
                    Class<?> clazz = executor.loadClass(item);
                    if (clazz == null) {
                        throw new IllegalStateException("missing catch exception type of : " + item);
                    }
                    if (clazz.isAssignableFrom(e.getClass())) {
                        executor.execAsProcedure(catchNode, context, false, false);
                        handled = true;
                        break;
                    }
                }


                // 还原堆栈
                executor.visitSet(context,exName, bakParams.get(exName));
            }
            if (!handled) {
                if(e instanceof RuntimeException){
                    throw (RuntimeException)e;
                }else {
                    throw new ThrowSignalException(e.getMessage(),e);
                }
            }
        } finally {
            if (finallyNode != null) {
                executor.execAsProcedure(finallyNode, context, false, false);
            }
        }
    }
}
