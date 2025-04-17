package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.count.ICountWrapper;
import i2f.bindsql.data.PageBindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.check.Predicates;
import i2f.clock.SystemClock;
import i2f.clock.std.IClock;
import i2f.compiler.MemoryCompiler;
import i2f.container.builder.map.MapBuilder;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseType;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptException;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptBreakException;
import i2f.extension.groovy.GroovyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.extension.velocity.VelocityGenerator;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.LangConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.BasicJdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;
import i2f.jdbc.procedure.provider.types.class4j.JdbcProcedureJavaCallerMetaProvider;
import i2f.jdbc.procedure.provider.types.class4j.impl.ListableJdbcProcedureJavaCallerMetaProvider;
import i2f.jdbc.procedure.provider.types.xml.JdbcProcedureXmlNodeMetaProvider;
import i2f.jdbc.procedure.provider.types.xml.impl.AbstractJdbcProcedureXmlNodeMetaCacheProvider;
import i2f.jdbc.procedure.registry.JdbcProcedureMetaProviderRegistry;
import i2f.jdbc.procedure.registry.impl.ListableJdbcProcedureMetaProviderRegistry;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.lru.LruMap;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexFindPartMeta;
import i2f.page.Page;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.script.ScriptProvider;
import i2f.text.StringUtils;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;
import i2f.typeof.token.data.TypeNode;
import i2f.uid.SnowflakeLongUid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalJavaNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL_JAVA;
    public static final String CLASS_NAME_HOLDER = "$#$##$###";
    public static final Pattern RETURN_PATTERN = Pattern.compile("\\s*return\\s*");

    public static void main(String[] args){
        /*language=java*/
        String script= "(int)params.get(\"a\")+(double)params.get(\"b\")";
        Map<String,Object> context=new HashMap<>();
        context.put("a",1);
        context.put("b",2.5);
        Object obj = evalJava(context,null,script);
        System.out.println(obj);
    }

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT !=node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        List<XmlNode> children = node.getChildren();
        XmlNode importNode = null;
        XmlNode memberNode = null;
        XmlNode bodyNode = null;
        if (children != null && !children.isEmpty()) {
            for (XmlNode item : children) {
                if (TagConsts.LANG_JAVA_IMPORT.equals(item.getTagName())) {
                    importNode = item;
                }
                if (TagConsts.LANG_JAVA_MEMBE.equals(item.getTagName())) {
                    memberNode = item;
                }
                if (TagConsts.LANG_JAVA_BODY.equals(item.getTagName())) {
                    bodyNode = node;
                }
            }
        }

        if (bodyNode == null) {
            bodyNode = node;
        }

        String importSegment = "";
        String memberSegment = "";
        String bodySegment = "";
        if (importNode != null) {
            importSegment = importNode.getTextBody();
        }
        if (memberNode != null) {
            memberSegment = memberNode.getTextBody();
        }
        if (bodyNode != null) {
            bodySegment = bodyNode.getTextBody();
        }
        Map.Entry<String, String> codeEntry = getFullJavaSourceCode(importSegment, memberSegment, bodySegment);

        try {
            MemoryCompiler.findCompileClass(codeEntry.getValue(), codeEntry.getKey() + ".java", codeEntry.getKey());
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new IllegalArgumentException(e.getMessage() + "\n\tcompile source code:\n" + codeEntry.getValue(), e);
            }
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        List<XmlNode> children = node.getChildren();
        XmlNode importNode = null;
        XmlNode memberNode = null;
        XmlNode bodyNode = null;
        if (children != null && !children.isEmpty()) {
            for (XmlNode item : children) {
                if (TagConsts.LANG_JAVA_IMPORT.equals(item.getTagName())) {
                    importNode = item;
                }
                if (TagConsts.LANG_JAVA_MEMBE.equals(item.getTagName())) {
                    memberNode = item;
                }
                if (TagConsts.LANG_JAVA_BODY.equals(item.getTagName())) {
                    bodyNode = node;
                }
            }
        }

        if (bodyNode == null) {
            bodyNode = node;
        }

        String importSegment = "";
        String memberSegment = "";
        String bodySegment = "";
        if (importNode != null) {
            importSegment = importNode.getTextBody();
        }
        if (memberNode != null) {
            memberSegment = memberNode.getTextBody();
        }
        if (bodyNode != null) {
            bodySegment = bodyNode.getTextBody();
        }

        Object obj = evalJava(context, executor, importSegment, memberSegment, bodySegment);

        if (result != null) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    @Override
    public boolean support(String lang) {
        return LangConsts.JAVA.equals(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        Object obj = evalJava(params, executor, "", "", script);
        return obj;
    }

    public static String castAsImportPackageName(String className) {
        if (className == null || className.isEmpty()) {
            return null;
        }
        int idx = className.lastIndexOf(".");
        if (idx <= 0) {
            return className;
        }
        return className.substring(0, idx + 1) + "*";
    }

    public static final String EVAL_JAVA_IMPORTS = new StringBuilder()
            .append("import ").append(castAsImportPackageName(JdbcProcedure.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(FeatureConsts.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(DefaultJdbcProcedureContext.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureContext.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(BasicJdbcProcedureExecutor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureExecutor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(SqlDialect.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(AbstractExecutorNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(LangEvalJavaNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ExecutorNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(XmlNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureParser.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ListableJdbcProcedureJavaCallerMetaProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureJavaCallerMetaProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(AbstractJdbcProcedureXmlNodeMetaCacheProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureXmlNodeMetaProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureMetaProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ListableJdbcProcedureMetaProviderRegistry.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureMetaProviderRegistry.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(GrammarReporter.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(BreakSignalException.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(SignalException.class.getName())).append(";").append("\n")
            ///////////////////////////////////////////////////////////////////////////////////////////////
            .append("import ").append(castAsImportPackageName(JdbcResolver.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(QueryColumn.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(DatabaseType.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(BindSql.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(IPageWrapper.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(PageBindSql.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ICountWrapper.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(MemoryCompiler.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ScriptProvider.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ReflectResolver.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(Visitor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ObjectConvertor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(MapBuilder.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(Predicates.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(RegexUtil.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(RegexFindPartMeta.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(Page.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(Reference.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(StringUtils.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(TypeOf.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(TypeToken.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(TypeNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(SnowflakeLongUid.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(IClock.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(SystemClock.class.getName())).append(";").append("\n")
            ///////////////////////////////////////////////////////////////////////////////////////////////
            .append("import ").append(castAsImportPackageName(TinyScript.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(TinyScriptException.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(TinyScriptBreakException.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(GroovyScript.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(OgnlUtil.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(VelocityGenerator.class.getName())).append(";").append("\n")
            ///////////////////////////////////////////////////////////////////////////////////////////////
            .append("import ").append("java.util.*").append(";").append("\n")
            .append("import ").append("java.math.*").append(";").append("\n")
            .append("import ").append("java.time.*").append(";").append("\n")
            .append("import ").append("java.text.*").append(";").append("\n")
            .append("import ").append("java.util.regex.*").append(";").append("\n")
            .append("import ").append("java.io.*").append(";").append("\n")
            .append("import ").append("java.lang.reflect.*").append(";").append("\n")
            .append("import ").append("javax.sql.*").append(";").append("\n")
            .append("import ").append("java.util.concurrent.*").append(";").append("\n")
            .append("import ").append("java.nio.*").append(";").append("\n")
            .append("import ").append("java.nio.charset.*").append(";").append("\n")
            .append("import ").append("java.util.concurrent.atomic.*").append(";").append("\n")
            .append("import ").append("java.util.concurrent.locks.*").append(";").append("\n")
            .append("import ").append("java.time.chrono.*").append(";").append("\n")
            .append("import ").append("java.time.format.*").append(";").append("\n")
            .append("import ").append("java.time.temporal.*").append(";").append("\n")
            .append("import ").append("java.time.zone.*").append(";").append("\n")
            .toString();

    public static final LruMap<String, Map.Entry<String, String>> FULL_JAVA_SOURCE_MAP = new LruMap<>(2048);

    public static Object evalJava(Map<String, Object> context, JdbcProcedureExecutor executor, String bodySegment) {
        return evalJava(context,executor,"","",bodySegment);
    }

    public static Object evalJava(Map<String, Object> context, JdbcProcedureExecutor executor, String importSegment, String memberSegment, String bodySegment) {
        Map.Entry<String, String> codeEntry = getFullJavaSourceCode(importSegment, memberSegment, bodySegment);
        String className = codeEntry.getKey();
        String javaSourceCode = codeEntry.getValue();
        Object obj = null;
        try {
            obj = MemoryCompiler.compileCall(javaSourceCode,
                    className + ".java",
                    className,
                    "exec",
                     executor, context
            );
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new IllegalStateException(e.getMessage() + "\n\tcompile source code:\n" + codeEntry.getValue(), e);
            }
        }
        return obj;
    }

    public static Map.Entry<String, String> getFullJavaSourceCode(String importSegment, String memberSegment, String bodySegment) {
        if(importSegment!=null){
            importSegment=importSegment.trim();
        }
        if(memberSegment!=null){
            memberSegment=memberSegment.trim();
        }
        if(bodySegment!=null){
            bodySegment=bodySegment.trim();
        }
        if (importSegment == null) {
            importSegment = "";
        }
        if (memberSegment == null) {
            memberSegment = "";
        }
        if (bodySegment == null) {
            bodySegment = "";
        }
        String cacheKey = importSegment + "###" + memberSegment + "###" + bodySegment + "###";
        Map.Entry<String, String> ret = FULL_JAVA_SOURCE_MAP.get(cacheKey);
        if (ret != null) {
            return new AbstractMap.SimpleEntry<>(ret.getKey(), ret.getValue());
        }

        bodySegment = bodySegment.trim();
        Matcher matcher = RETURN_PATTERN.matcher(bodySegment);
        StringBuilder builder = new StringBuilder();
        builder.append(EVAL_JAVA_IMPORTS);

        builder.append(importSegment).append("\n");
        builder.append("public class ").append(CLASS_NAME_HOLDER).append("{").append("\n");
        builder.append(memberSegment).append("\n");
        builder.append("public Object exec(JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable {").append("\n");
        if (!matcher.find()) {
            String[] lines = bodySegment.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (i == lines.length - 1) {
                    String str = line.trim();
                    if (!str.isEmpty() && !"}".equals(str)) {
                        builder.append(" return ");
                        builder.append(line);
                        if (!str.endsWith(";")) {
                            builder.append(";");
                        }
                    } else {
                        builder.append(line);
                    }
                    builder.append("\n");
                } else {
                    builder.append(line).append("\n");
                }
            }
        } else {
            builder.append(bodySegment);
            if (!bodySegment.endsWith(";") && !bodySegment.endsWith("}")) {
                builder.append(";");
            }
        }
        builder.append("\n");
        builder.append("}").append("\n");
        builder.append("}").append("\n");

        String javaSourceCode = builder.toString();

        StringBuilder hexBuilder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hex = digest.digest(javaSourceCode.getBytes());
            for (int i = 0; i < hex.length; i++) {
                hexBuilder.append(String.format("%02x", (int) (hex[i] & 0x0ff)));
            }
        } catch (NoSuchAlgorithmException e) {
            hexBuilder.append(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        }
        String className = "RC" + hexBuilder;
        javaSourceCode = javaSourceCode.replace(CLASS_NAME_HOLDER, className);

        Map.Entry<String, String> val = new AbstractMap.SimpleEntry<>(className, javaSourceCode);
        try {
            FULL_JAVA_SOURCE_MAP.put(cacheKey, new AbstractMap.SimpleEntry<>(val.getKey(), val.getValue()));
        } catch (Exception e) {

        }
        return val;
    }
}
