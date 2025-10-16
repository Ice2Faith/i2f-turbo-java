package i2f.jdbc.procedure.node;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:36
 */
public interface ExecutorNode {
    default String[] alias() {
        return null;
    }

    String tag();

    default boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        if (Objects.equals(tag(), node.getTagName())) {
            return true;
        }
        String[] alias = alias();
        if (alias != null) {
            for (String tag : alias) {
                if (Objects.equals(tag, node.getTagName())) {
                    return true;
                }
            }
        }
        return false;
    }

    default void reportGrammar(XmlNode node, Consumer<String> warnPoster) {

    }

    void exec(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor);
}
