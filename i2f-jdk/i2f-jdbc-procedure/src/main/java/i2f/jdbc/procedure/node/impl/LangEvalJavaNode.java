package i2f.jdbc.procedure.node.impl;

import i2f.clock.SystemClock;
import i2f.compiler.MemoryCompiler;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.match.regex.RegexUtil;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalJavaNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-eval-java";
    public static final String CLASS_NAME_HOLDER = "$#$##$###";
    public static final Pattern RETURN_PATTERN = Pattern.compile("\\s*return\\s*");

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        List<XmlNode> children = node.getChildren();
        XmlNode importNode = null;
        XmlNode memberNode = null;
        XmlNode bodyNode = null;
        if (children != null && !children.isEmpty()) {
            for (XmlNode item : children) {
                if ("lang-java-import".equals(item.getTagName())) {
                    importNode = item;
                }
                if ("lang-java-member".equals(item.getTagName())) {
                    memberNode = item;
                }
                if ("lang-java-body".equals(item.getTagName())) {
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

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }

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
            .append("import ").append(castAsImportPackageName(ExecuteContext.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(XmlNode.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureExecutor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcProcedureExecutorCaller.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(DefaultJdbcProcedureExecutorCaller.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(FeatureConsts.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(JdbcResolver.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(RegexUtil.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ReflectResolver.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(Visitor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(ObjectConvertor.class.getName())).append(";").append("\n")
            .append("import ").append(castAsImportPackageName(SystemClock.class.getName())).append(";").append("\n")
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

    public static Object evalJava(ExecuteContext context, JdbcProcedureExecutor executor, String importSegment, String memberSegment, String bodySegment) {
        if (importSegment == null) {
            importSegment = "";
        }
        if (memberSegment == null) {
            memberSegment = "";
        }
        if (bodySegment == null) {
            bodySegment = "";
        }
        bodySegment = bodySegment.trim();
        Matcher matcher = RETURN_PATTERN.matcher(bodySegment);
        StringBuilder builder = new StringBuilder();
        builder.append(EVAL_JAVA_IMPORTS);

        builder.append(importSegment).append("\n");
        builder.append("public class ").append(CLASS_NAME_HOLDER).append("{").append("\n");
        builder.append(memberSegment).append("\n");
        builder.append("public Object exec(ExecuteContext context, JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable {").append("\n");
        if (!matcher.find()) {
            String[] lines = bodySegment.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (i == lines.length - 1) {
                    String str = line.trim();
                    builder.append(" return ");
                    builder.append(line);
                    if (!str.endsWith(";")) {
                        builder.append(";");
                    }
                    builder.append("\n");
                } else {
                    builder.append(line).append("\n");
                }
            }
        } else {
            builder.append(bodySegment);
            if (!bodySegment.endsWith(";")) {
                builder.append(";");
            }
        }
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

        Object obj = null;
        try {
            obj = MemoryCompiler.compileCall(javaSourceCode,
                    className + ".java",
                    className,
                    "exec",
                    context, executor, context.getParams()
            );
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return obj;
    }
}
