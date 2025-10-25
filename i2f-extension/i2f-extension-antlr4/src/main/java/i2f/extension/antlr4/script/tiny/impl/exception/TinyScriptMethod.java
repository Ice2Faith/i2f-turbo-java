package i2f.extension.antlr4.script.tiny.impl.exception;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptVisitorImpl;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptReturnException;
import i2f.invokable.method.IMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/10/25 22:13
 * @desc
 */
@Data
@NoArgsConstructor
public class TinyScriptMethod implements IMethod {
    protected String name;
    protected List<String> parameters = new ArrayList<>();
    protected TinyScriptParser.ScriptBlockContext scriptBlockContext;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return TinyScriptMethod.class;
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC | Modifier.STATIC;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        int size = parameters.size();
        Class<?>[] ret = new Class<?>[size];
        for (int i = 0; i < size; i++) {
            ret[i] = Object.class;
        }
        return ret;
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        TinyScriptVisitorImpl visitor = (TinyScriptVisitorImpl) ivkObj;
        ConcurrentHashMap<String, CopyOnWriteArrayList<TinyScriptMethod>> declareFunctionMap = visitor.getDeclareFunctionMap();
        TinyScriptResolver resolver = visitor.getResolver();
        Object context = visitor.getContext();
        Object global = visitor.getGlobal();
        if (global == null) {
            global = context;
        }

        Map<String, Object> callContext = new HashMap<>();
        callContext.put("global", global);
        for (int i = 0; i < parameters.size(); i++) {
            callContext.put(parameters.get(i), args[i]);
        }

        TinyScriptVisitorImpl nextVisitor = new TinyScriptVisitorImpl(callContext, resolver);
        nextVisitor.setDeclareFunctionMap(declareFunctionMap);
        nextVisitor.setGlobal(global);
        try {
            Object ret = nextVisitor.visitScriptBlock(scriptBlockContext);
            return ret;
        } catch (TinyScriptControlException e) {
            if (e instanceof TinyScriptReturnException) {
                TinyScriptReturnException retEx = (TinyScriptReturnException) e;
                return retEx.getRetValue();
            }
        }
        return null;
    }

    @Override
    public void setAccessible(boolean accessible) {

    }
}
