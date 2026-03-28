package i2f.spring.ai.tool;

import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.spring.ai.model.SpringAiJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/28 17:30
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringAiToolCallback implements ToolCallback {
    protected ToolDefinition definition;
    protected ToolRawDefinition rawDefinition;

    public SpringAiToolCallback(ToolDefinition definition, ToolRawDefinition rawDefinition) {
        this.definition = definition;
        this.rawDefinition = rawDefinition;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return definition;
    }

    @Override
    public String call(String toolInput) {
        String callResult = null;
        Throwable callEx = null;
        try {
            Map<String, Object> parametersMap = SpringAiJsonSerializer.INSTANCE.deserializeAsMap(toolInput);
            Object ret = ToolRawHelper.invokeTool(rawDefinition, parametersMap);
            if (ret instanceof CharSequence) {
                callResult = String.valueOf(ret);
            } else {
                String json = SpringAiJsonSerializer.INSTANCE.serialize(ret);
                callResult = json;
            }
            return callResult;
        } catch (Throwable e) {
            callEx = e;
            if (callEx instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) callEx;
                callEx = ite.getTargetException();
            }
            throw new IllegalStateException(callEx.getMessage(), callEx);
        }
    }
}
