package i2f.jdbc.procedure.reportor;

import i2f.compiler.MemoryCompiler;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.impl.LangEvalJavaNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.match.regex.RegexPattens;
import groovy.lang.GroovyShell;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/3/6 15:49
 */
public class GrammarReporter {
    protected static ExecutorService pool = new ForkJoinPool(Math.min(16, Runtime.getRuntime().availableProcessors()) * 2);
    protected static ExecutorService exprPool = new ForkJoinPool(Math.min(32, Runtime.getRuntime().availableProcessors()) * 2);

    public static void reportGrammar(JdbcProcedureExecutor executor, Map<String, ProcedureMeta> map, Consumer<String> warnPoster) {
        if (map == null) {
            return;
        }
        warnPoster.accept(XProc4jConsts.NAME+" report grammar running ...");
        long bts = System.currentTimeMillis();
        AtomicInteger allReportCount = new AtomicInteger(0);
        AtomicInteger allNodeCount = new AtomicInteger(0);
        try {
            int mapSize = map.size();
            CountDownLatch latch = new CountDownLatch(mapSize);
            AtomicInteger reportSize = new AtomicInteger(0);
            for (Map.Entry<String, ProcedureMeta> entry : map.entrySet()) {
                pool.submit(() -> {
                    try {
                        reportSize.incrementAndGet();
                        ProcedureMeta meta = entry.getValue();
                        if (meta.getType() == ProcedureMeta.Type.XML) {
                            XmlNode node = (XmlNode) meta.getTarget();
                            executor.logDebug(() -> XProc4jConsts.NAME+" grammar report rate " + String.format("%6.02f%%", reportSize.get() * 100.0 / mapSize) + ", on node: " + node.getTagName() + ", at " + AbstractExecutorNode.getNodeLocation(node));
                            AtomicInteger reportCount = new AtomicInteger(0);
                            AtomicInteger nodeCount = new AtomicInteger(0);
                            reportGrammar(node, executor, warnPoster, reportCount, nodeCount);
                            allReportCount.addAndGet(reportCount.get());
                            allNodeCount.addAndGet(nodeCount.get());
                            if (reportCount.get() > 0) {
                                warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + node.getLocationFile() + " found issue statistic, issue:" + reportCount.get() + ", nodes:" + nodeCount.get());
                            }
                        }
                    } catch (Exception e) {
                        warnPoster.accept(XProc4jConsts.NAME+" reporter run error:" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (Throwable e) {
            warnPoster.accept(XProc4jConsts.NAME+" reporter run error:" + e.getMessage());
            e.printStackTrace();
        }
        long ets = System.currentTimeMillis();
        warnPoster.accept(XProc4jConsts.NAME+" report grammar final statistic, issue:" + allReportCount.get() + ", nodes:" + allNodeCount.get() + ", use seconds:" + ((ets - bts) / 1000));
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
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node) + " error: " + msg);
                    });
                } catch (Throwable e) {
                    if (reportCount != null) {
                        reportCount.incrementAndGet();
                    }
                    warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node) + " error: " + e.getMessage());
                }
                break;
            }
        }
        String[] INVALID_STR_ARR={
                "''",
//                "||",
                "{{",
                "}}",
                "{}",
                "$}",
                "$ {",
        };
        if(Arrays.asList(
                TagConsts.SQL_QUERY_LIST,
                TagConsts.SQL_QUERY_ROW,
                TagConsts.SQL_QUERY_OBJECT,
                TagConsts.SQL_UPDATE,
                TagConsts.SQL_SCRIPT,
                TagConsts.LANG_RENDER,
                TagConsts.LANG_STRING
        ).contains(node.getTagName())){
            String body = node.getTextBody();
            if(node.getTagName().startsWith("sql-") || "sql".equalsIgnoreCase(node.getTagAttrMap().get("_lang"))){
                body=body.replaceAll(RegexPattens.SINGLE_LINE_COMMENT_SQL_REGEX,"");
                body=body.replaceAll(RegexPattens.MULTI_LINE_COMMENT_REGEX,"");
                String tstr=body.trim();
                if(tstr.endsWith(";")){
                    warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node) + " error: sql maybe end with ';'");
                }
                for (String sstr : INVALID_STR_ARR) {
                    if(body.contains(sstr)){
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node) + " error: sql maybe end with \""+sstr+"\"");
                    }
                }

            }

            reportEnclosedCharString(body,node,warnPoster);
        }

        int latchSize = node.getTagAttrMap().size();
        CountDownLatch latch = new CountDownLatch(latchSize);
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            exprPool.submit(() -> {
                try {
                    reportAttributeFeatureGrammar(key, node, FeatureConsts.STRING, warnPoster);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<XmlNode> children = node.getChildren();
        if (children != null) {
            for (XmlNode item : children) {
                reportGrammar(item, executor, warnPoster, reportCount, nodeCount);
            }
        }

    }

    public static void reportEnclosedCharString(String body,XmlNode node,Consumer<String> warnPoster){
        body=body.trim();
        if(Arrays.asList("(","[","{","'","\"").contains(body)){
            return;
        }
        Stack<Character> parenStack=new Stack<>();
        Stack<Character> strStack=new Stack<>();
        char[] arr = body.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch=arr[i];
            if(ch=='{' || ch=='(' || ch=='['){
                parenStack.push(ch);
            }else if(ch=='}'){
                if(parenStack.isEmpty()){
                    warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe missing left '"+ch+"' , right is: "+body.substring(i));
                }else{
                    char pop = parenStack.peek();
                    if(pop=='{'){
                        parenStack.pop();
                    }else{
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe not paired on '"+ch+"', expect '"+pop+"' right match , right is: "+body.substring(i));
                    }
                }
            }else if(ch==']'){
                if(parenStack.isEmpty()){
                    warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe missing left '"+ch+"' , right is: "+body.substring(i));
                }else{
                    char pop = parenStack.peek();
                    if(pop=='['){
                        parenStack.pop();
                    }else{
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe not paired on '"+ch+"', expect '"+pop+"' right match , right is: "+body.substring(i));
                    }
                }
            }else if(ch==')'){
                if(parenStack.isEmpty()){
                    warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe missing left '"+ch+"' , right is: "+body.substring(i));
                }else{
                    char pop = parenStack.peek();
                    if(pop=='('){
                        parenStack.pop();
                    }else{
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe not paired on '"+ch+"', expect '"+pop+"' right match , right is: "+body.substring(i));
                    }
                }
            }else if(ch=='\''){
                if(strStack.isEmpty()){
                    strStack.push(ch);
                }else{
                    char pop = strStack.peek();
                    if(pop=='\''){
                        strStack.pop();
                    }else{
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe not enclosed on ["+ch+"], expect ["+pop+"] right match , right is: "+body.substring(i));
                    }
                }
            }else if(ch=='"'){
                if(strStack.isEmpty()){
                    strStack.push(ch);
                }else{
                    char pop = strStack.peek();
                    if(pop=='"'){
                        strStack.pop();
                    }else{
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node)+" char at:"+i + " error: sql maybe not enclosed on ["+ch+"], expect ["+pop+"] right match , right is: "+body.substring(i));
                    }
                }
            }
        }
    }

    public static void reportAttributeFeatureGrammar(String attr, XmlNode node, String defaultFeature, Consumer<String> warnPoster) {
        String test = node.getTagAttrMap().get(attr);
        if (test == null) {
            return;
        }
        reportEnclosedCharString(test,node,warnPoster);
        List<String> features = node.getAttrFeatureMap().get(attr);
        if (features != null && !features.isEmpty()) {
            for (String feature : features) {
                reportExprFeatureGrammar(test, feature, node, "attribute " + attr, warnPoster);
                break;
            }
        } else {
            reportExprFeatureGrammar(test, defaultFeature, node, "attribute " + attr, warnPoster);
        }
    }

    public static void reportExprFeatureGrammar(String expr, String feature, XmlNode node, String location, Consumer<String> warnPoster) {
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
                        warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, at " + AbstractExecutorNode.getNodeLocation(node) + " error: " + errorMsg);
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
            warnPoster.accept(XProc4jConsts.NAME+" report xml grammar, check tag [" + node.getTagName() + "] on " + location + ", at " + AbstractExecutorNode.getNodeLocation(node) + " for expr: " + expr + " error: " + e.getMessage());
        }
    }
}
