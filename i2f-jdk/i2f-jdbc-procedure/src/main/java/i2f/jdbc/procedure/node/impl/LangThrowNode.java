package i2f.jdbc.procedure.node.impl;

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
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-throw".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String message = (String) executor.attrValue("value", "visit", node, context);
        String exceptionType = node.getTagAttrMap().get("type");
        String cause = node.getTagAttrMap().get("cause");
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
