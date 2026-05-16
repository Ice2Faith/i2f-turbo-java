package i2f.extension.antlr4.script.funic.lang;

import i2f.extension.antlr4.script.funic.grammar.FunicLexer;
import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.errors.DefaultAntlrErrorListener;
import i2f.extension.antlr4.script.funic.lang.errors.DefaultErrorStrategy;
import i2f.extension.antlr4.script.funic.lang.functions.FunicFunctionHelper;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.invokable.method.IMethod;
import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.mixin.MixinProxyFactory;
import i2f.mixin.all.AllMixins;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/4/22 9:00
 * @desc
 */
public class Funic {
    public static final LruMap<String, FunicParser.RootContext> TREE_MAP = new LruMap<>(4096);

    public static final CopyOnWriteArrayList<ANTLRErrorListener> ERROR_LISTENER = new CopyOnWriteArrayList<>();

    public static final CopyOnWriteArrayList<String> IMPORT_PACKAGES = new CopyOnWriteArrayList<>();
    public static final ConcurrentHashMap<String, CopyOnWriteArrayList<IMethod>> GLOBAL_METHODS = new ConcurrentHashMap<>();

    public static final ThreadLocal<DefaultFunicVisitor> VISITOR = new InheritableThreadLocal<>();
    public static final ThreadLocal<Object> FUNCTION_CALL_CONTEXT = new InheritableThreadLocal<>();

    static {
        ERROR_LISTENER.add(DefaultAntlrErrorListener.INSTANCE);

        registryMethods(MixinProxyFactory.getMixinInstance(AllMixins.class));
        registryMethods(System.out, e -> e.getName().toLowerCase().contains("print"));
        registryMethods(Math.class, e -> !e.getName().toLowerCase().contains("extra"));
    }

    public static Object script(File scriptFile, Object context) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return script(formula, context, scriptFile.getName(), null);
    }

    public static Object script(File scriptFile, Object context, FunicResolver resolver) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return script(formula, context, scriptFile.getName(), resolver);
    }


    public static Object script(String formula, Object context) {
        return script(formula, context, null, null);
    }

    public static Object script(String formula, Object context, String scriptFileName) {
        return script(formula, context, scriptFileName, null);
    }

    public static Object script(String formula, Object context, FunicResolver resolver) {
        FunicParser.RootContext tree = parse(formula);
        return script(tree, context, null, resolver);
    }

    public static Object script(String formula, Object context, String scriptFileName, FunicResolver resolver) {
        FunicParser.RootContext tree = parse(formula);
        return script(tree, context, scriptFileName, resolver);
    }

    public static Object script(FunicParser.RootContext tree, Object context) {
        return script(tree, context, null, null);
    }

    public static Object script(FunicParser.RootContext tree, Object context, String scriptFileName) {
        return script(tree, context, scriptFileName, null);
    }

    public static Object script(FunicParser.RootContext tree, Object context, FunicResolver resolver) {
        return script(tree, context, null, resolver);
    }

    public static Object script(FunicParser.RootContext tree, Object context, String scriptFileName, FunicResolver resolver) {
        DefaultFunicVisitor visitor = new DefaultFunicVisitor(context, scriptFileName, resolver);
        return script(tree, visitor);
    }

    public static Object script(String formula, DefaultFunicVisitor visitor) {
        FunicParser.RootContext root = parse(formula);
        return script(root, visitor);
    }

    public static void registryMethods(Class<?> clazz) {
        registryMethods(clazz, null);
    }

    public static void registryMethods(Class<?> clazz, Predicate<Method> filter) {
        List<IMethod> methods = FunicFunctionHelper.ofMethods(clazz, filter);
        registryMethods(methods);
    }

    public static void registryMethods(Object target) {
        registryMethods(target,null);
    }

    public static void registryMethods(Object target, Predicate<Method> filter) {
        List<IMethod> methods = FunicFunctionHelper.ofInstanceMethods(target, filter);
        registryMethods(methods);
    }

    public static void registryMethods(Iterable<? extends IMethod> iterable) {
        if (iterable == null) {
            return;
        }
        for (IMethod item : iterable) {
            String name = item.getName();
            CopyOnWriteArrayList<IMethod> list = GLOBAL_METHODS.computeIfAbsent(name, key -> new CopyOnWriteArrayList<>());
            list.add(item);
        }
    }

    public static Object script(FunicParser.RootContext tree, DefaultFunicVisitor visitor) {
        FunicValue value = visitor.visit(tree);
        Object ret = value.get();
        return ret;
    }

    public static FunicParser.RootContext parse(String formula) {
        if (formula != null) {
            formula = formula.trim();
        }
        try {
            FunicParser.RootContext ret = TREE_MAP.get(formula);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {

        }
        CommonTokenStream tokens = parseTokens(formula);
        FunicParser parser = new FunicParser(tokens);
        parser.setErrorHandler(new DefaultErrorStrategy());
        for (ANTLRErrorListener item : ERROR_LISTENER) {
            if (item == null) {
                continue;
            }
            parser.addErrorListener(item);
        }
        FunicParser.RootContext tree = parser.root();
        try {
            TREE_MAP.put(formula, tree);
        } catch (Exception e) {

        }
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        FunicLexer lexer = new FunicLexer(input);
        for (ANTLRErrorListener item : ERROR_LISTENER) {
            if (item == null) {
                continue;
            }
            lexer.addErrorListener(item);
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }
}
