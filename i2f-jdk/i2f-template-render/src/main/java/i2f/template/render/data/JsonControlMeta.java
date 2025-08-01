package i2f.template.render.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2021/10/20
 */
@Data
@NoArgsConstructor
public class JsonControlMeta {
    public String action;
    public String routeExpression;
    public Map<String, String> parameters;
}
