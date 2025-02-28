package i2f.extension.antlr4.script.tiny.impl;

import i2f.extension.antlr4.script.tiny.TinyScriptLexer;
import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import i2f.extension.antlr4.script.tiny.impl.context.TinyScriptFunctions;
import i2f.lru.LruMap;
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
    public static final LruMap<String,TinyScriptParser.ScriptContext> TREE_MAP=new LruMap<>(4096);

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
        registryBuiltMethodByStaticMethod(Math.class, (method) -> {
            String name = method.getName();
            return !name.startsWith("copy")
                    && !name.startsWith("next");
        });
        registryBuiltMethodByStaticMethod(Thread.class, (method) -> {
            String name = method.getName();
            return !name.startsWith("sleep")
                    && !name.startsWith("yield");
        });
        registryBuiltMethodByStaticMethod(System.class);
        registryBuiltMethodByStaticMethod(TinyScriptFunctions.class);
    }

    public static Object script(String formula, Object context) {
        return script(formula, context, null);
    }

    public static Object script(String formula, Object context, TinyScriptResolver resolver) {
        TinyScriptParser.ScriptContext tree = parse(formula);
        return script(tree, context, resolver);
    }

    public static Object script(TinyScriptParser.ScriptContext tree, Object context) {
        return script(tree, context, null);
    }

    public static Object script(TinyScriptParser.ScriptContext tree, Object context, TinyScriptResolver resolver) {
        TinyScriptVisitor<Object> visitor = new TinyScriptVisitorImpl(context, resolver);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static TinyScriptParser.ScriptContext parse(String formula) {
        try {
            TinyScriptParser.ScriptContext ret = TREE_MAP.get(formula);
            if(ret!=null){
                return ret;
            }
        } catch (Exception e) {

        }
        CommonTokenStream tokens = parseTokens(formula);
        TinyScriptParser parser = new TinyScriptParser(tokens);
        TinyScriptParser.ScriptContext tree = parser.script();
        try {
            TREE_MAP.put(formula,tree);
        } catch (Exception e) {

        }
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
