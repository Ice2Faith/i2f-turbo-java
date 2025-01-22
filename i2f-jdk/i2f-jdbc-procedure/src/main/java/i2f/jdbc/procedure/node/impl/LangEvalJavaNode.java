package i2f.jdbc.procedure.node.impl;

import i2f.compiler.MemoryCompiler;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalJavaNode implements ExecutorNode {
    public static final String CLASS_NAME_HOLDER = "$#$##$###";
    public static final Pattern RETURN_PATTERN = Pattern.compile("\\s*return\\s*");

    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-eval-java".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get("result");
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

        bodySegment = bodySegment.trim();

        Matcher matcher = RETURN_PATTERN.matcher(bodySegment);

        String className = "RC" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        StringBuilder builder = new StringBuilder();
        builder.append("import ").append(ExecuteContext.class.getName()).append(";").append("\n");
        builder.append("import ").append(JdbcProcedureExecutor.class.getName()).append(";").append("\n");
        builder.append("import ").append("java.util.*").append(";").append("\n");
        builder.append("import ").append("java.math.*").append(";").append("\n");
        builder.append("import ").append("java.time.*").append(";").append("\n");
        builder.append("import ").append("java.text.*").append(";").append("\n");
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

        if (result != null && !result.isEmpty()) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get("result"), node, context);
            executor.setParamsObject(context.getParams(), result, obj);
        }

    }
}
