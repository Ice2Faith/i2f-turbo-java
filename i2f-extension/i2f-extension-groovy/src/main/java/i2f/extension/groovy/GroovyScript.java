package i2f.extension.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/3/4 21:39
 * @desc
 */
public class GroovyScript {
    public static Object eval(String script, Map<String, Object> params) {
        Binding binding = new Binding(params == null ? new HashMap<>() : params);
        GroovyShell shell = new GroovyShell(binding);
        return shell.evaluate(script);
    }
}
