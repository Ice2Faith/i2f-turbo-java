package i2f.extension.antlr4.tpl.impl;

import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.extension.antlr4.tpl.grammar.TplLexer;
import i2f.extension.antlr4.tpl.grammar.TplParser;
import i2f.extension.antlr4.tpl.grammar.TplVisitor;
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
public class Tpl {

    public static final LruMap<String, TplParser.RootContext> TREE_MAP = new LruMap<>(4096);

    public static final CopyOnWriteArrayList<ANTLRErrorListener> ERROR_LISTENER = new CopyOnWriteArrayList<>();

    static {
        ERROR_LISTENER.add(DefaultAntlrErrorListener.INSTANCE);
    }

    public static Object render(File scriptFile, Object context) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return render(formula, context, scriptFile.getName(), 0, null);
    }

    public static Object render(File scriptFile, Object context, TplResolver resolver) throws Exception {
        String formula = StreamUtil.readString(scriptFile, StandardCharsets.UTF_8.name());
        return render(formula, context, scriptFile.getName(), 0, resolver);
    }

    public static Object render(String formula, Object context) {
        return render(formula, context, null, 0, null);
    }

    public static Object render(String formula, Object context, String scriptFileName) {
        return render(formula, context, scriptFileName, 0, null);
    }

    public static Object render(String formula, Object context, TplResolver resolver) {
        return render(formula, context, null, 0, resolver);
    }

    public static Object render(String formula, Object context,
                                String scriptFileName, int scriptLineOffset,
                                TplResolver resolver) {
        TplParser.RootContext tree = parse(formula);
        return render(tree, context, scriptFileName, scriptLineOffset, resolver);
    }

    public static Object render(TplParser.RootContext tree, Object context) {
        return render(tree, context, null, 0, null);
    }

    public static Object render(TplParser.RootContext tree, Object context, String scriptFileName) {
        return render(tree, context, scriptFileName, 0, null);
    }

    public static Object render(TplParser.RootContext tree, Object context, TplResolver resolver) {
        return render(tree, context, null, 0, resolver);
    }

    public static Object render(TplParser.RootContext tree, Object context,
                                String scriptFileName, int scriptLineOffset,
                                TplResolver resolver) {
        TplVisitor<Object> visitor = new TplVisitorImpl(context, scriptFileName, scriptLineOffset, resolver);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static TplParser.RootContext parse(String formula) {
        if (formula != null) {
            formula = formula.trim();
        }
        try {
            TplParser.RootContext ret = TREE_MAP.get(formula);
            if (ret != null) {
                return ret;
            }
        } catch (Exception e) {

        }
        CommonTokenStream tokens = parseTokens(formula);
        TplParser parser = new TplParser(tokens);
        parser.setErrorHandler(new TplErrorStrategy());
        for (ANTLRErrorListener item : ERROR_LISTENER) {
            if (item == null) {
                continue;
            }
            parser.addErrorListener(item);
        }
        TplParser.RootContext tree = parser.root();
        try {
            TREE_MAP.put(formula, tree);
        } catch (Exception e) {

        }
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        TplLexer lexer = new TplLexer(input);
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
