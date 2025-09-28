package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.executor.event.ExecutorContextEvent;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
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

    public String getPreferLocation() {
        String location = null;
        if (pointContext != null) {
            location = (String) pointContext.get(AbstractExecutorNode.POINT_KEY_LOCATION);
        }
        if (location == null || location.isEmpty()) {
            location = XmlNode.getNodeLocation(node);
        }
        return location;
    }
}
