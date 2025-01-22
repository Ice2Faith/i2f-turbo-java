package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/22 13:57
 */
@Data
@NoArgsConstructor
public class ExecuteContext {
    protected Map<String, Object> params = new HashMap<>();
    protected Map<String, XmlNode> nodeMap = new HashMap<>();
}
