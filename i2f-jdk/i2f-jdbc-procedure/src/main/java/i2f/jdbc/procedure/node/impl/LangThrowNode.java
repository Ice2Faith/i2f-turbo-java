package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangThrowNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_THROW;

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String type = node.getTagAttrMap().get(AttrConsts.TYPE);
        if(type==null || type.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.TYPE);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        String message = (String) executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        String exceptionType = node.getTagAttrMap().get(AttrConsts.TYPE);
        String cause = node.getTagAttrMap().get(AttrConsts.CAUSE);
        Throwable ex = (Throwable) executor.visit(cause, context);
        Class<?> clazz = executor.loadClass(exceptionType);

        try {
            Constructor<?> constructor = null;
            try {
                constructor = clazz.getConstructor(String.class, Throwable.class);
                if (constructor != null) {
                    Throwable obj = (Throwable) constructor.newInstance(message, ex);
                    throw obj;
                }
            } catch (ReflectiveOperationException e) {

            }

            try {
                constructor = clazz.getConstructor(String.class);
                if (constructor != null) {
                    Throwable obj = (Throwable) constructor.newInstance(message);
                    throw obj;
                }
            } catch (ReflectiveOperationException e) {
            }

            try {
                constructor = clazz.getConstructor(Throwable.class);
                if (constructor != null) {
                    Throwable obj = (Throwable) constructor.newInstance(ex);
                    throw obj;
                }
            } catch (ReflectiveOperationException e) {
            }
        } catch (Throwable e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }

    }

}
