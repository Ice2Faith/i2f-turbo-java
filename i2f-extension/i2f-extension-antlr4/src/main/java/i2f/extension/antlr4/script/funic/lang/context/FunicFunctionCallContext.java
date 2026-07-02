package i2f.extension.antlr4.script.funic.lang.context;

import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:58
 * @desc
 */
@Data
@NoArgsConstructor
public class FunicFunctionCallContext {
    protected DefaultFunicVisitor visitor;
    protected Type type;
    protected Class<?> callClass;
    protected Object invokeTarget;
    protected String methodName;
    protected List<Map.Entry<String, Object>> argsList;

    public static enum Type {
        NEW_INSTANCE,
        INSTANCE_METHOD,
        STATIC_METHOD,
        GLOBAL_METHOD
    }

}
