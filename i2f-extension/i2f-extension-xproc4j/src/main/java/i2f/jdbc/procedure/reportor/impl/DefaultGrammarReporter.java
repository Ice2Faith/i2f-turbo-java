package i2f.jdbc.procedure.reportor.impl;

import groovy.lang.GroovyShell;
import i2f.compiler.MemoryCompiler;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.node.impl.LangEvalJavaNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reporter.impl.BasicGrammarReporter;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/3/6 15:49
 */
public class DefaultGrammarReporter extends BasicGrammarReporter {

    @Override
    public void reportExprFeatureGrammar(String expr, String feature, XmlNode node, String location, Consumer<String> warnPoster) {
        super.reportExprFeatureGrammar(expr, feature, node, location, warnPoster);
        try {
            if (FeatureConsts.EVAL.equals(feature)) {
                OgnlUtil.parseExpressionTreeNode(expr);
            } else if (FeatureConsts.EVAL_GROOVY.equals(feature)) {
                GroovyShell shell = new GroovyShell();
                shell.parse(expr);
            } else if (FeatureConsts.EVAL_JAVA.equals(feature)) {
                Map.Entry<String, String> codeEntry = LangEvalJavaNode.getFullJavaSourceCode("", "", expr);
                try {
                    MemoryCompiler.findCompileClass(codeEntry.getValue(), codeEntry.getKey() + ".java", codeEntry.getKey());
                } catch (Exception e) {
                    throw new IllegalArgumentException(e.getMessage() + "\n\tcompile source code:\n" + codeEntry.getValue(), e);
                }
            } else if (FeatureConsts.EVAL_JS.equals(feature)) {

            } else if (FeatureConsts.EVAL_TINYSCRIPT.equals(feature)
                    || FeatureConsts.EVAL_TS.equals(feature)) {
                ANTLRErrorListener listener = new BaseErrorListener() {
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                        String errorMsg = "line " + line + ":" + charPositionInLine + " " + msg;
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: " + errorMsg);
                    }
                };
                TinyScript.ERROR_LISTENER.add(listener);
                try {
                    TinyScript.parse(expr);
                } finally {
                    TinyScript.ERROR_LISTENER.remove(listener);
                }
            } else if (FeatureConsts.EVAL_FUNIC.equals(feature)) {
                ANTLRErrorListener listener = new BaseErrorListener() {
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                        String errorMsg = "line " + line + ":" + charPositionInLine + " " + msg;
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: " + errorMsg);
                    }
                };
                Funic.ERROR_LISTENER.add(listener);
                try {
                    Funic.parse(expr);
                } finally {
                    Funic.ERROR_LISTENER.remove(listener);
                }
            }
        } catch (Throwable e) {
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, check tag [" + node.getTagName() + "] on " + location + ", at " + XmlNode.getNodeLocation(node) + " for expr: " + expr + " error: " + e.getMessage());
        }
    }
}
