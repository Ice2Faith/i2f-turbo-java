package i2f.jdbc.procedure.reportor;

import groovy.lang.GroovyShell;
import i2f.compiler.MemoryCompiler;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.procedure.consts.*;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.impl.LangEvalJavaNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.lru.LruList;
import i2f.match.regex.RegexPattens;
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

    public static void reportGrammar(JdbcProcedureExecutor executor, Map<String, ProcedureMeta> metaMap, Consumer<String> warnPoster) {
        if (metaMap == null) {
            return;
        }
        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar running ...");
        long bts = System.currentTimeMillis();
        AtomicInteger allReportCount = new AtomicInteger(0);
        AtomicInteger allNodeCount = new AtomicInteger(0);
        try {
            int mapSize = metaMap.size();
            CountDownLatch latch = new CountDownLatch(mapSize);
            AtomicInteger reportSize = new AtomicInteger(0);
            for (Map.Entry<String, ProcedureMeta> entry : metaMap.entrySet()) {
                pool.submit(() -> {
                    try {
                        reportSize.incrementAndGet();
                        ProcedureMeta meta = entry.getValue();
                        if (meta.getType() == ProcedureMeta.Type.XML) {
                            XmlNode node = (XmlNode) meta.getTarget();
                            if (executor.isDebug()) {
                                executor.logDebug(XProc4jConsts.NAME + " report xml grammar rate " + String.format("%6.02f%%", reportSize.get() * 100.0 / mapSize) + ", on node: " + node.getTagName() + ", at " + XmlNode.getNodeLocation(node));
                            }
                            AtomicInteger reportCount = new AtomicInteger(0);
                            AtomicInteger nodeCount = new AtomicInteger(0);
                            reportGrammar(node, metaMap, executor, warnPoster, reportCount, nodeCount);
                            allReportCount.addAndGet(reportCount.get());
                            allNodeCount.addAndGet(nodeCount.get());
                            if (reportCount.get() > 0) {
                                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + node.getLocationFile() + " found issue statistic, issue:" + reportCount.get() + ", nodes:" + nodeCount.get());
                            }
                        }
                    } catch (Exception e) {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar run error:" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (Throwable e) {
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar run error:" + e.getMessage());
            e.printStackTrace();
        }
        long ets = System.currentTimeMillis();
        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar final statistic, issue:" + allReportCount.get() + ", nodes:" + allNodeCount.get() + ", use seconds:" + ((ets - bts) / 1000));
    }

    public static void reportGrammar(XmlNode node,
                                     Map<String, ProcedureMeta> metaMap,
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
        LruList<ExecutorNode> nodes = executor.getNodes();
        ExecutorNode executorNode = nodes.touchFirst(e -> e.support(node));
        if (executorNode != null) {
            try {
                executorNode.reportGrammar(node, (msg) -> {
                    if (reportCount != null) {
                        reportCount.incrementAndGet();
                    }
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: " + msg);
                });
            } catch (Throwable e) {
                if (reportCount != null) {
                    reportCount.incrementAndGet();
                }
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: " + e.getMessage());
            }
        }
        String[] INVALID_STR_ARR = {
                "''",
//                "||",
                "{{",
                "}}",
                "{}",
                "$}",
                "$ {",
        };
        if (Arrays.asList(
                TagConsts.SQL_QUERY_LIST,
                TagConsts.SQL_QUERY_ROW,
                TagConsts.SQL_QUERY_OBJECT,
                TagConsts.SQL_UPDATE,
                TagConsts.SQL_SCRIPT,
                TagConsts.LANG_RENDER,
                TagConsts.LANG_STRING
        ).contains(node.getTagName())) {
            String body = node.getTextBody();
            if (node.getTagName().startsWith("sql-") || "sql".equalsIgnoreCase(node.getTagAttrMap().get("_lang"))) {
                body = body.replaceAll(RegexPattens.SINGLE_LINE_COMMENT_SQL_REGEX, "");
                body = body.replaceAll(RegexPattens.MULTI_LINE_COMMENT_REGEX, "");
                String tstr = body.trim();
                if (tstr.endsWith(";")) {
                    if (reportCount != null) {
                        reportCount.incrementAndGet();
                    }
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: sql maybe end with ';'");
                }
                for (String sstr : INVALID_STR_ARR) {
                    if (body.contains(sstr)) {
                        if (reportCount != null) {
                            reportCount.incrementAndGet();
                        }
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: sql maybe contains \"" + sstr + "\"");
                    }
                }

            }

            if (TagConsts.LANG_STRING.equals(node.getTagName())) {
                if (body.contains("${")
                        || body.contains("#{")
                        || body.contains("$!{")
                        || body.contains("#!{")) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: string maybe change to render mode");
                }
            }

            if (TagConsts.LANG_RENDER.equals(node.getTagName())) {
                if (body.contains("#{")
                        || body.contains("#!{")) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: render maybe contains illegal placeholder: #{/#!{");
                }
            }

            reportEnclosedCharString(body, node, warnPoster);
        }

        if (Arrays.asList(
                TagConsts.PROCEDURE_CALL,
                TagConsts.FUNCTION_CALL
        ).contains(node.getTagName())) {
            Map<String, String> attrMap = node.getTagAttrMap();
            String refid = attrMap.get(AttrConsts.REFID);
            if (refid == null || refid.trim().isEmpty()) {
                if (reportCount != null) {
                    reportCount.incrementAndGet();
                }
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: call node missing or blank attribute [" + AttrConsts.REFID + "]");
            }
            String result = attrMap.get(AttrConsts.RESULT);
            if (result == null || result.trim().isEmpty()) {
                String paramsShare = attrMap.get(AttrConsts.PARAMS_SHARE);
                String params = attrMap.get(AttrConsts.PARAMS);
                boolean reportFlag = true;
                if (paramsShare != null && !paramsShare.isEmpty()) {
                    reportFlag = false;
                }
                if (params != null && !params.isEmpty()) {
                    reportFlag = false;
                }
                if (reportFlag) {
                    if (reportCount != null) {
                        reportCount.incrementAndGet();
                    }
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: call node refid [" + refid + "] missing or blank attribute [" + AttrConsts.RESULT + "]");
                }
            } else {
                if (TagConsts.PROCEDURE_CALL.equals(node.getTagName())) {
                    if (result.startsWith("V_")) {
                        if (reportCount != null) {
                            reportCount.incrementAndGet();
                        }
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: call node refid [" + refid + "] maybe result wrong variable [" + result + "]");

                    }
                }
            }
            ProcedureMeta meta = metaMap.get(refid);
            if (meta != null) {
                List<String> arguments = meta.getArguments();
                for (String argument : arguments) {
                    if (Arrays.asList(
                            AttrConsts.ID,
                            AttrConsts.REFID,
                            AttrConsts.RESULT,
                            ParamsConsts.RETURN
                    ).contains(argument)) {
                        continue;
                    }
                    if (!attrMap.containsKey(argument)) {
                        if (reportCount != null) {
                            reportCount.incrementAndGet();
                        }
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: call node refid [" + refid + "] missing argument [" + argument + "]");
                    }
                }
            } else if (refid != null && !refid.isEmpty()) {
                if (reportCount != null) {
                    reportCount.incrementAndGet();
                }
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: call node missing target refid [" + refid + "]");
            }
        }

        if (!TagConsts.SCRIPT_SEGMENT.equals(node.getTagName())) {
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
        }
        List<XmlNode> children = node.getChildren();
        if (children != null) {
            for (XmlNode item : children) {
                reportGrammar(item, metaMap, executor, warnPoster, reportCount, nodeCount);
            }
        }

    }

    public static void reportEnclosedCharString(String body, XmlNode node, Consumer<String> warnPoster) {
        body = body.trim();
        if (Arrays.asList("(", "[", "{", "'", "\"").contains(body)) {
            return;
        }
        Stack<Character> parenStack = new Stack<>();
        Stack<Character> strStack = new Stack<>();
        char[] arr = body.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            if (ch == '{' || ch == '(' || ch == '[') {
                parenStack.push(ch);
            } else if (ch == '}') {
                if (parenStack.isEmpty()) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe missing left '" + ch + "' , right is: " + body.substring(i));
                } else {
                    char pop = parenStack.peek();
                    if (pop == '{') {
                        parenStack.pop();
                    } else {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe not paired on '" + ch + "', expect '" + pop + "' right match , right is: " + body.substring(i));
                    }
                }
            } else if (ch == ']') {
                if (parenStack.isEmpty()) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe missing left '" + ch + "' , right is: " + body.substring(i));
                } else {
                    char pop = parenStack.peek();
                    if (pop == '[') {
                        parenStack.pop();
                    } else {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe not paired on '" + ch + "', expect '" + pop + "' right match , right is: " + body.substring(i));
                    }
                }
            } else if (ch == ')') {
                if (parenStack.isEmpty()) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe missing left '" + ch + "' , right is: " + body.substring(i));
                } else {
                    char pop = parenStack.peek();
                    if (pop == '(') {
                        parenStack.pop();
                    } else {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe not paired on '" + ch + "', expect '" + pop + "' right match , right is: " + body.substring(i));
                    }
                }
            } else if (ch == '\'') {
                if (strStack.isEmpty()) {
                    strStack.push(ch);
                } else {
                    char pop = strStack.peek();
                    if (pop == '\'') {
                        strStack.pop();
                    } else {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe not enclosed on [" + ch + "], expect [" + pop + "] right match , right is: " + body.substring(i));
                    }
                }
            } else if (ch == '"') {
                if (strStack.isEmpty()) {
                    strStack.push(ch);
                } else {
                    char pop = strStack.peek();
                    if (pop == '"') {
                        strStack.pop();
                    } else {
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + i + " error: sql maybe not enclosed on [" + ch + "], expect [" + pop + "] right match , right is: " + body.substring(i));
                    }
                }
            }
        }
        while (!parenStack.isEmpty()) {
            char pop = parenStack.pop();
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + (-1) + " error: sql maybe missing right parent , left is: " + pop);
        }
        while (!strStack.isEmpty()) {
            char pop = strStack.pop();
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " char at:" + (-1) + " error: sql maybe missing right str enclosed , left is: " + pop);
        }
    }

    public static void reportAttributeFeatureGrammar(String attr, XmlNode node, String defaultFeature, Consumer<String> warnPoster) {
        String test = node.getTagAttrMap().get(attr);
        if (test == null) {
            return;
        }
        if (test.trim().isEmpty()) {
            if (!TagConsts.PROCEDURE.equals(node.getTagName())) {
                List<String> features = node.getAttrFeatureMap().get(attr);
                boolean reportFlag = true;
                if (features != null && !features.isEmpty()) {
                    String feature = features.get(0);
                    if (Arrays.asList(
                            FeatureConsts.NULL,
                            FeatureConsts.BODY_TEXT,
                            FeatureConsts.BODY_XML,
                            FeatureConsts.CURRENT_TIME_MILLIS,
                            FeatureConsts.DATE_NOW,
                            FeatureConsts.SNOW_UID,
                            FeatureConsts.UUID
                    ).contains(feature)) {
                        reportFlag = false;
                    }
                }
                if (reportFlag) {
                    warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, check tag [" + node.getTagName() + "] " + ", at " + XmlNode.getNodeLocation(node) + " for attribute: [" + attr + "]" + node.getAttrFeatureMap().get(attr) + " maybe wrong blank value [" + test + "]");
                }
            }
        }

        reportEnclosedCharString(test, node, warnPoster);
        List<String> features = node.getAttrFeatureMap().get(attr);
        if (features == null || features.isEmpty()) {
            boolean reportFlag = false;
            if (Arrays.asList(
                    AttrConsts.PATTERN,
                    AttrConsts.SEPARATOR
            ).contains(attr)) {
                // do nothing
                reportFlag = false;
            } else if (Arrays.asList(
                    AttrConsts.TYPE,
                    AttrConsts.RESULT_TYPE,
                    AttrConsts.CLASS,
                    AttrConsts.PACKAGE
            ).contains(attr)) {
                if (!test.matches("((\\s*\\|\\s*)?[a-zA-Z_\\$]+(\\.[a-zA-Z0-9_\\$]+)*)+")) {
                    reportFlag = true;
                }
            } else if (!AttrConsts.TEST.equals(attr)) {
                if (!test.matches("((\\s*\\(\\s*)*(\\s*,\\s*)?(\\.)?((\\$)?(@)?[a-zA-Z0-9_]+|(\\$|#)\\{\\s*[a-zA-Z0-9_\\.]+\\s*\\}|\\[\\s*[0-9]+\\s*\\])(\\s*\\)\\s*)*(\\(\\s*\\))?)+")) {
                    reportFlag = true;
                }
            }
            if (reportFlag) {
                warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, check tag [" + node.getTagName() + "] " + ", at " + XmlNode.getNodeLocation(node) + " for attribute: [" + attr + "]" + node.getAttrFeatureMap().get(attr) + " maybe wrong visit expression [" + test + "]");
            }
        }
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
                        warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, at " + XmlNode.getNodeLocation(node) + " error: " + errorMsg);
                    }
                };
                TinyScript.ERROR_LISTENER.add(listener);
                try {
                    TinyScript.parse(expr);
                } finally {
                    TinyScript.ERROR_LISTENER.remove(listener);
                }
            } else if (FeatureConsts.STRING.equals(feature)) {
                if (expr.contains("${")
                        || expr.contains("#{")
                        || expr.contains("$!{")
                        || expr.contains("#!{")) {
                    throw new IllegalArgumentException("string maybe change to render mode");
                }
            } else if (FeatureConsts.RENDER.equals(feature)) {
                if (expr.contains("#{")
                        || expr.contains("#!{")) {
                    throw new IllegalArgumentException("render maybe contains illegal placeholder: #{/#!{");
                }
            }
        } catch (Throwable e) {
            warnPoster.accept(XProc4jConsts.NAME + " report xml grammar, check tag [" + node.getTagName() + "] on " + location + ", at " + XmlNode.getNodeLocation(node) + " for expr: " + expr + " error: " + e.getMessage());
        }
    }
}
