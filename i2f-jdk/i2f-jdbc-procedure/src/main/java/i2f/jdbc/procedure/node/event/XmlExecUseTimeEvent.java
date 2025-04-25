package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.executor.event.ExecutorContextEvent;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/24 14:05
 */
@Data
@NoArgsConstructor
public class XmlExecUseTimeEvent extends ExecutorContextEvent {
    protected ExecutorNode executorNode;
    protected Map<String, Object> pointContext;
    protected XmlNode node;
    protected long useTs;
}
