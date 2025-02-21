package i2f.extension.antlr4.script.tiny.impl;

import i2f.extension.antlr4.script.tiny.TinyScriptLexer;
import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2025/1/10 22:07
 */
public class TinyScript {
    public static final ConcurrentHashMap<String, CopyOnWriteArrayList<Method>> BUILTIN_METHOD=new ConcurrentHashMap<>();

    static {
        registryBuiltMethodByStaticMethod(String.class,(method)->{
            return Arrays.asList("join", "format").contains(method.getName());
        });
        registryBuiltMethodByStaticMethod(Integer.class,(method)->{
            String name = method.getName();
            return name.startsWith("to") || name.startsWith("parse");
        });
        registryBuiltMethodByStaticMethod(Long.class,(method)->{
            String name = method.getName();
            return name.startsWith("to") || name.startsWith("parse");
        });
        registryBuiltMethodByStaticMethod(Double.class,(method)->{
            String name = method.getName();
            return name.startsWith("to") || name.startsWith("parse");
        });
        registryBuiltMethodByStaticMethod(Float.class,(method)->{
            String name = method.getName();
            return name.startsWith("to") || name.startsWith("parse");
        });
        registryBuiltMethodByStaticMethod(Boolean.class,(method)->{
            String name = method.getName();
            return name.startsWith("to") || name.startsWith("parse");
        });
    }

    public static Object script(String formula, Object context) {
        TinyScriptParser.ScriptContext tree = parse(formula);
        return script(tree, context);
    }

    public static Object script(TinyScriptParser.ScriptContext tree, Object context) {
        TinyScriptVisitor<Object> visitor = new TinyScriptVisitorImpl(context);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static TinyScriptParser.ScriptContext parse(String formula) {
        CommonTokenStream tokens = parseTokens(formula);
        TinyScriptParser parser = new TinyScriptParser(tokens);
        TinyScriptParser.ScriptContext tree = parser.script();
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        TinyScriptLexer lexer = new TinyScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }

    public static void registryBuiltinMethod(Method method){
        CopyOnWriteArrayList<Method> list = BUILTIN_METHOD.computeIfAbsent(method.getName(), (key) -> new CopyOnWriteArrayList<>());
        list.add(method);
    }

    public static void registryBuiltMethodByStaticMethod(Class<?> clazz){
        registryBuiltMethodByStaticMethod(clazz,null);
    }

    public static void registryBuiltMethodByStaticMethod(Class<?> clazz, Predicate<Method> filter){
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            int mod = method.getModifiers();
            if(!Modifier.isPublic(mod)){
                continue;
            }
            if(!Modifier.isStatic(mod)){
                continue;
            }
            if(Modifier.isAbstract(mod)){
                continue;
            }
            if(filter==null || filter.test(method)){
                registryBuiltinMethod(method);
            }
        }
    }
}
