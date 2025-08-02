package i2f.extension.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import i2f.lru.LruMap;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2025/3/4 21:39
 * @desc
 */
public class GroovyScript {
    public static Object evalScript(String script, Map<String, Object> params) {
        Binding binding = new Binding(params == null ? new HashMap<>() : params);
        GroovyShell shell = new GroovyShell(binding);
        return shell.evaluate(script);
    }

    public static final GroovyShell DEFAULT_SHELL = new GroovyShell();
    protected static final LruMap<String, Class> SCRIPT_MAP = new LruMap<>(4096);

    public static Object eval(String script, Map<String, Object> params) {
        Binding binding = new Binding(params == null ? new HashMap<>() : params);
        Class clazz = parseAsClass(script);
        Script run = InvokerHelper.createScript(clazz, binding);
        return run.run();
    }

    public static Class parseAsClass(String script) {
        if (script == null) {
            return null;
        }
        script = script.trim();

        Class clazz = SCRIPT_MAP.get(script);
        if (clazz != null) {
            return clazz;
        }
        clazz = DEFAULT_SHELL.getClassLoader().parseClass(script, "GvyRc" + getTextDigest(script) + ".groovy");
        SCRIPT_MAP.put(script, clazz);
        return clazz;
    }

    public static String getTextDigest(String text) {
        StringBuilder hexBuilder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hex = digest.digest(text.getBytes());
            for (int i = 0; i < hex.length; i++) {
                hexBuilder.append(String.format("%02x", (int) (hex[i] & 0x0ff)));
            }
        } catch (NoSuchAlgorithmException e) {
            hexBuilder.append(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        }
        return hexBuilder.toString();
    }

}
