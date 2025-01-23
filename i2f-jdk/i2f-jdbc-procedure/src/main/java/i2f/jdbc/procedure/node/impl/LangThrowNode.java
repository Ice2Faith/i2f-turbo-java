package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.lang.reflect.Constructor;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangThrowNode implements ExecutorNode {
    public static final String TAG_NAME = "lang-throw";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String message = (String) executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        String exceptionType = node.getTagAttrMap().get(AttrConsts.TYPE);
        String cause = node.getTagAttrMap().get(AttrConsts.CAUSE);
        Throwable ex = (Throwable) executor.visit(cause, context.getParams());
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
