package i2f.extension.antlr4.funvi.lang;

import i2f.extension.antlr4.funvi.grammar.FunviLexer;
import i2f.extension.antlr4.funvi.grammar.FunviVisitor;
import i2f.extension.antlr4.funvi.lang.errors.FunviErrorStrategy;
import i2f.extension.antlr4.funvi.lang.impl.FunviVisitorImpl;
import i2f.extension.antlr4.funvi.lang.listener.DefaultAntlrErrorListener;
import i2f.extension.antlr4.funvi.lang.resolver.FunviResolver;
import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.extension.antlr4.funvi.grammar.FunviParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/1/10 22:07
 */
public class Funvi {

    public static final LruMap<String, FunviParser.RootContext> TREE_MAP = new LruMap<>(4096);

    public static final CopyOnWriteArrayList<ANTLRErrorListener> ERROR_LISTENER = new CopyOnWriteArrayList<>();

    static {
        ERROR_LISTENER.add(DefaultAntlrErrorListener.INSTANCE);
    }

    public static Object render(File scriptFile, Object context) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return render(formula, context, scriptFile.getName(), 0, null);
    }

    public static Object render(File scriptFile, Object context, FunviResolver resolver) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return render(formula, context, scriptFile.getName(), 0, resolver);
    }

    public static Object render(String formula, Object context) {
        return render(formula, context, null, 0, null);
    }

    public static Object render(String formula, Object context, String scriptFileName) {
        return render(formula, context, scriptFileName, 0, null);
    }

    public static Object render(String formula, Object context, FunviResolver resolver) {
        return render(formula, context, null, 0, resolver);
    }

    public static Object render(String formula, Object context,
                                String scriptFileName, int scriptLineOffset,
                                FunviResolver resolver) {
        FunviParser.RootContext tree = parse(formula);
        return render(tree, context, scriptFileName, scriptLineOffset, resolver);
    }

    public static Object render(FunviParser.RootContext tree, Object context) {
        return render(tree, context, null, 0, null);
    }

    public static Object render(FunviParser.RootContext tree, Object context, String scriptFileName) {
        return render(tree, context, scriptFileName, 0, null);
    }

    public static Object render(FunviParser.RootContext tree, Object context, FunviResolver resolver) {
        return render(tree, context, null, 0, resolver);
    }

    public static Object render(FunviParser.RootContext tree, Object context,
                                String scriptFileName, int scriptLineOffset,
                                FunviResolver resolver) {
        FunviVisitor<Object> visitor = new FunviVisitorImpl(context, scriptFileName, scriptLineOffset, resolver);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static FunviParser.RootContext parse(String formula) {
        if (formula != null) {
            formula = formula.trim();
        }
        try {
            FunviParser.RootContext ret = TREE_MAP.get(formula);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {

        }
        CommonTokenStream tokens = parseTokens(formula);
        FunviParser parser = new FunviParser(tokens);
        parser.setErrorHandler(new FunviErrorStrategy());
        for (ANTLRErrorListener item : ERROR_LISTENER) {
            if (item == null) {
                continue;
            }
            parser.addErrorListener(item);
        }
        FunviParser.RootContext tree = parser.root();
        try {
            TREE_MAP.put(formula, tree);
        } catch (Exception e) {

        }
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        FunviLexer lexer = new FunviLexer(input);
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
