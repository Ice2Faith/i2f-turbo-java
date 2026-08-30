package i2f.springboot.ops.openai.tool.impl;

import groovy.lang.GroovyShell;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.extension.groovy.GroovyScript;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/8/30 21:06
 * @desc
 */
@ConditionalOnExpression("${ai.tools.groovy.enable:false}")
@ConditionalOnClass({
        GroovyShell.class,
        GroovyScript.class
})
@Component
@Tools(tags = {
        "groovy"
})
public class GroovyTools {
    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.SCRIPT_VALUE
            },
            description = "run groovy script"
    )
    public Object groovy_run_script(@ToolParam(value = "script", description = "the script, groovy script")
                                    String script) {
        Map<String, Object> params = new HashMap<>();
        Object obj = GroovyScript.evalScript(script, params);
        return obj;
    }
}
