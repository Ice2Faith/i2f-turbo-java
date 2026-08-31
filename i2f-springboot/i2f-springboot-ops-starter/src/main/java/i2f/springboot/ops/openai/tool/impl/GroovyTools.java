package i2f.springboot.ops.openai.tool.impl;

import groovy.lang.GroovyShell;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.extension.groovy.GroovyScript;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
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
@Data
@NoArgsConstructor
@Component
@Tools(tags = {
        "groovy"
})
public class GroovyTools implements ApplicationContextAware, EnvironmentAware {
    private ApplicationContext applicationContext;
    private Environment environment;

    @Tool(
            tags = {
                    AiTags.EXECUTABLE_VALUE,
                    AiTags.HUMAN_VALUE,
                    AiTags.SCRIPT_VALUE
            },
            description = "run groovy script, returns the result of the last statement, and the result must be JSON-serializable. \n" +
                    "Note: \n" +
                    "   - Standard output is not captured. Please use the logger variable (`java.io.PrintWriter logger`) for logging.\n" +
                    "   - embed variables: \n" +
                    "       - `org.springframework.context.ApplicationContext applicationContext` \n" +
                    "       - `org.springframework.core.env.Environment environment` \n"
    )
    public Map<String, Object> groovy_run_script(@ToolParam(value = "script", description = "the script, groovy script")
                                                 String script) {
        Map<String, Object> ret = new HashMap<>();

        StringWriter writer = new StringWriter();
        PrintWriter logger = new PrintWriter(writer);

        Map<String, Object> params = new HashMap<>();
        params.put("logger", logger);
        params.put("applicationContext", applicationContext);
        params.put("environment", environment);

        Object obj = GroovyScript.evalScript(script, params);

        ret.put("returns", obj);
        ret.put("logs", writer.toString());
        return ret;
    }
}
