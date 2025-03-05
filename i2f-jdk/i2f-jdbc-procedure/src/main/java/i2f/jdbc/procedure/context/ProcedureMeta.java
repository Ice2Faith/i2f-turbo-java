package i2f.jdbc.procedure.context;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:45
 * @desc
 */
@Data
@NoArgsConstructor
public class ProcedureMeta {
    public static enum Type {
        XML,
        JAVA
    }

    protected Type type;
    protected String name;
    protected Object target;
    protected List<String> arguments = new ArrayList<>();
    protected Map<String, List<String>> argumentFeatures = new LinkedHashMap<>();

}
