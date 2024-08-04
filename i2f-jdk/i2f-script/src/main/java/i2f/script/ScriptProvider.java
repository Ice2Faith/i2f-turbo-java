package i2f.script;

import i2f.script.exception.ScriptFeatureNotSupportException;

import javax.script.*;
import java.io.Reader;

/**
 * @author Ice2Faith
 * @date 2024/8/4 11:11
 * @desc
 */
public class ScriptProvider implements ScriptEngine, Invocable, Compilable {
    public static final ScriptEngineManager manager = new ScriptEngineManager();
    public static final String JAVA_SCRIPT_ENGINE_SHORT_NAME = "JavaScript";

    private ScriptEngine engine;

    public ScriptProvider(ScriptEngine engine) {
        this.engine = engine;
    }

    public static ScriptEngine getEngine(String shortName) {
        ScriptEngine engine = manager.getEngineByName(shortName);
        return engine;
    }

    public static ScriptEngine getJavaScriptEngine() {
        return getEngine(JAVA_SCRIPT_ENGINE_SHORT_NAME);
    }

    public static ScriptProvider getInstance(String shortName) {
        return new ScriptProvider(getEngine(shortName));
    }

    public static ScriptProvider getJavaScriptInstance() {
        return new ScriptProvider(getJavaScriptEngine());
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        if (!(engine instanceof Compilable)) {
            throw new ScriptFeatureNotSupportException("engine not has Compilable feature!");
        }
        Compilable compilable = (Compilable) engine;
        return compilable.compile(script);
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
        if (!(engine instanceof Compilable)) {
            throw new ScriptFeatureNotSupportException("engine not has Compilable feature!");
        }
        Compilable compilable = (Compilable) engine;
        return compilable.compile(script);
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (!(engine instanceof Invocable)) {
            throw new ScriptFeatureNotSupportException("engine not has Invocable feature!");
        }
        Invocable invocable = (Invocable) engine;
        return invocable.invokeMethod(thiz, name, args);
    }

    @Override
    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (!(engine instanceof Invocable)) {
            throw new ScriptFeatureNotSupportException("engine not has Invocable feature!");
        }
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(name, args);
    }

    @Override
    public <T> T getInterface(Class<T> clasz) {
        if (!(engine instanceof Invocable)) {
            throw new IllegalArgumentException("engine not has Invocable feature!");
        }
        Invocable invocable = (Invocable) engine;
        return invocable.getInterface(clasz);
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clasz) {
        if (!(engine instanceof Invocable)) {
            throw new IllegalArgumentException("engine not has Invocable feature!");
        }
        Invocable invocable = (Invocable) engine;
        return invocable.getInterface(thiz, clasz);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return engine.eval(script, context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return engine.eval(reader, context);
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return engine.eval(script);
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return engine.eval(reader);
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return engine.eval(script, n);
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return engine.eval(reader, n);
    }

    @Override
    public void put(String key, Object value) {
        engine.put(key, value);
    }

    @Override
    public Object get(String key) {
        return engine.get(key);
    }

    @Override
    public Bindings getBindings(int scope) {
        return engine.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        engine.setBindings(bindings, scope);
    }

    @Override
    public Bindings createBindings() {
        return engine.createBindings();
    }

    @Override
    public ScriptContext getContext() {
        return engine.getContext();
    }

    @Override
    public void setContext(ScriptContext context) {
        engine.setContext(context);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return engine.getFactory();
    }


}
