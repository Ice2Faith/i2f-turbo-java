package i2f.jdbc.procedure.reportor;

import groovy.lang.GroovyShell;
import i2f.compiler.MemoryCompiler;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.impl.LangEvalJavaNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/3/6 15:49
 */
public class GrammarReporter {
    public static void reportGrammar(JdbcProcedureExecutor executor, Map<String, ProcedureMeta> map, Consumer<String> warnPoster) {
        if(map==null){
            return;
        }
        long bts=System.currentTimeMillis();
        AtomicInteger allReportCount = new AtomicInteger(0);
        AtomicInteger allNodeCount = new AtomicInteger(0);
        try {
            for (Map.Entry<String, ProcedureMeta> entry : map.entrySet()) {
                ProcedureMeta meta = entry.getValue();
                if (meta.getType() == ProcedureMeta.Type.XML) {
                    XmlNode node = (XmlNode) meta.getTarget();
                    AtomicInteger reportCount = new AtomicInteger(0);
                    AtomicInteger nodeCount = new AtomicInteger(0);
                    reportGrammar(node, executor, warnPoster, reportCount, nodeCount);
                    allReportCount.addAndGet(reportCount.get());
                    allNodeCount.addAndGet(nodeCount.get());
                    if (reportCount.get() > 0) {
                        warnPoster.accept("xproc4j report xml grammar, at " + node.getLocationFile() + " found issue statistic, issue:" + reportCount.get() + ", nodes:" + nodeCount.get());
                    }
                }
            }
        } catch (Throwable e) {
            warnPoster.accept("xproc4j reporter run error:"+e.getMessage());
            e.printStackTrace();
        }
        long ets=System.currentTimeMillis();
        warnPoster.accept("xproc4j report grammar final statistic, issue:" + allReportCount.get() + ", nodes:" + allNodeCount.get()+", use seconds:"+((ets-bts)/1000));
    }

    public static void reportGrammar(XmlNode node,
                                     JdbcProcedureExecutor executor,
                                     Consumer<String> warnPoster,
                                     AtomicInteger reportCount,
                                     AtomicInteger nodeCount) {
        if (node == null) {
            return;
        }
        if (nodeCount != null) {
            nodeCount.incrementAndGet();
        }
        List<ExecutorNode> nodes = executor.getNodes();
        for (ExecutorNode executorNode : nodes) {
            if (executorNode.support(node)) {
                try {
                    executorNode.reportGrammar(node, (msg) -> {
                        if (reportCount != null) {
                            reportCount.incrementAndGet();
                        }
                        warnPoster.accept("xproc4j report xml grammar, at " + node.getLocationFile() + ":" + node.getLocationLineNumber() + " error: " + msg);
                    });
                } catch (Throwable e) {
                    if (reportCount != null) {
                        reportCount.incrementAndGet();
                    }
                    warnPoster.accept("xproc4j report xml grammar, at " + node.getLocationFile() + ":" + node.getLocationLineNumber() + " error: " + e.getMessage());
                }
                break;
            }
        }
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            reportAttributeFeatureGrammar(key,node,FeatureConsts.STRING,warnPoster);
        }
        List<XmlNode> children = node.getChildren();
        if (children == null) {
            return;
        }
        for (XmlNode item : children) {
            reportGrammar(item, executor, warnPoster, reportCount, nodeCount);
        }
    }

    public static void reportAttributeFeatureGrammar(String attr,XmlNode node,String defaultFeature,Consumer<String> warnPoster){
        String test = node.getTagAttrMap().get(attr);
        if(test==null){
            return;
        }
        List<String> features = node.getAttrFeatureMap().get(test);
        if(features!=null && !features.isEmpty()){
            for (String feature : features) {
                reportExprFeatureGrammar(test,feature,node,"attribute "+attr,warnPoster);
                break;
            }
        }else{
            reportExprFeatureGrammar(test,defaultFeature,node,"attribute "+attr,warnPoster);
        }
    }

    public static void reportExprFeatureGrammar(String expr,String feature,XmlNode node,String location,Consumer<String> warnPoster){
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
                        warnPoster.accept("xproc4j report xml grammar, at " + node.getLocationFile() + ":" + node.getLocationLineNumber() + " error: " + errorMsg);
                    }
                };
                TinyScript.ERROR_LISTENER.add(listener);
                try {
                    TinyScript.parse(expr);
                } finally {
                    TinyScript.ERROR_LISTENER.remove(listener);
                }
            }
        } catch (Throwable e) {
            warnPoster.accept("xproc4j report xml grammar, check tag ["+node.getTagName()+"] on "+location+", at " + node.getLocationFile() + ":" + node.getLocationLineNumber()+" for expr: "+ expr + " error: " + e.getMessage());
        }
    }
}
