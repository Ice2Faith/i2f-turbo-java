package i2f.extension.antlr4.script.funic.lang;

import i2f.extension.antlr4.script.funic.grammar.FunicLexer;
import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.errors.DefaultAntlrErrorListener;
import i2f.extension.antlr4.script.funic.lang.errors.DefaultErrorStrategy;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.invokable.method.IMethod;
import i2f.lru.LruMap;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

    static {
        ERROR_LISTENER.add(DefaultAntlrErrorListener.INSTANCE);
    }


    public static Object script(String formula, Object context) {
        return script(formula, context, null);
    }

    public static Object script(String formula, Object context, FunicResolver resolver) {
        FunicParser.RootContext tree = parse(formula);
        return script(tree, context, resolver);
    }

    public static Object script(FunicParser.RootContext tree, Object context) {
        return script(tree, context, null);
    }

    public static Object script(FunicParser.RootContext tree, Object context, FunicResolver resolver) {
        DefaultFunicVisitor visitor = new DefaultFunicVisitor(context, resolver);
        return script(tree, visitor);
    }

    public static Object script(String formula, DefaultFunicVisitor visitor) {
        FunicParser.RootContext root = parse(formula);
        return script(root, visitor);
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
