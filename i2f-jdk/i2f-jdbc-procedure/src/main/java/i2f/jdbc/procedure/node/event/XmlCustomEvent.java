package i2f.jdbc.procedure.node.event;

import i2f.jdbc.procedure.executor.event.ExecutorContextEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/6/1 16:15
 * @desc
 */
@Data
@NoArgsConstructor
public class XmlCustomEvent extends ExecutorContextEvent {
    protected String eventType;
    protected XmlNode node;
    protected Map<String, Object> params;
}
