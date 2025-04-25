package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.executor.event.ExecutorContextEvent;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/17 14:08
 */
@Data
@NoArgsConstructor
public class XmlNodeExecEvent extends ExecutorContextEvent {
    protected Type type;

    ;
    protected ExecutorNode executorNode;
    protected Map<String, Object> pointContext;
    protected XmlNode node;
    protected Throwable throwable;

    public static enum Type {
        BEFORE,
        AFTER,
        THROWING,
        FINALLY
    }

}
