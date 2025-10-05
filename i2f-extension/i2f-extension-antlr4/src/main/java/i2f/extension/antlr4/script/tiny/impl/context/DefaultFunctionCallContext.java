package i2f.extension.antlr4.script.tiny.impl.context;

import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:58
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultFunctionCallContext {
    protected TinyScriptResolver resolver;
    protected Object context;
    protected Object target;
    protected boolean isNew;
    protected String naming;
    protected List<Object> argList;
}
