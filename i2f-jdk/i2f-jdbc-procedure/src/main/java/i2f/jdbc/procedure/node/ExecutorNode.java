package i2f.jdbc.procedure.node;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:36
 */
public interface ExecutorNode {
    boolean support(XmlNode node);

    default void reportGrammar(XmlNode node, Consumer<String> warnPoster) {

    }

    void exec(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor);
}
