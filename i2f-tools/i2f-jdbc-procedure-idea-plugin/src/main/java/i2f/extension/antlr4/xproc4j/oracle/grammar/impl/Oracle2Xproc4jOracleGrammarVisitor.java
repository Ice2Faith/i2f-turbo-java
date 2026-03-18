package i2f.extension.antlr4.xproc4j.oracle.grammar.impl;

import i2f.extension.antlr4.xproc4j.oracle.grammar.OracleGrammarParser;
import i2f.extension.antlr4.xproc4j.oracle.grammar.OracleGrammarVisitor;
import i2f.match.regex.RegexUtil;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/17 11:14
 * @desc
 */
public class Oracle2Xproc4jOracleGrammarVisitor implements OracleGrammarVisitor<String> {

    protected ConvertType mode = ConvertType.XPROC4J;

    public Oracle2Xproc4jOracleGrammarVisitor() {

    }

    public Oracle2Xproc4jOracleGrammarVisitor(ConvertType type) {
        if (type != null) {
            this.mode = type;
        }
    }

    @Override
    public String visitConvert(OracleGrammarParser.ConvertContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        boolean isFirst = true;
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.ScriptContext) {
                OracleGrammarParser.ScriptContext nextCtx = (OracleGrammarParser.ScriptContext) child;
                if (!isFirst) {
                    builder.append("\n");
                }
                String nextText = visitScript(nextCtx);
                builder.append(nextText);
                isFirst = false;
            }
        }

        return builder.toString();
    }

    @Override
    public String visitScript(OracleGrammarParser.ScriptContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        boolean isFirst = true;
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.SegmentContext) {
                OracleGrammarParser.SegmentContext nextCtx = (OracleGrammarParser.SegmentContext) child;
                if (!isFirst) {
                    builder.append("\n");
                }
                String nextText = visitSegment(nextCtx);
                builder.append(nextText);
                isFirst = false;
            }
        }

        String ret = builder.toString();
        return mergeEvalTs(ret);
    }

    public String mergeEvalTs(String text) {
        if (text == null) {
            return null;
        }
        return RegexUtil.regexFindAndReplace(text, "\\s*\\<\\/lang-eval-ts\\>\\s*\\<lang-eval-ts\\>", e -> "");
    }

    @Override
    public String visitSegment(OracleGrammarParser.SegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.ExecuteImmediadeVariableSegmentContext) {
                OracleGrammarParser.ExecuteImmediadeVariableSegmentContext nextCtx = (OracleGrammarParser.ExecuteImmediadeVariableSegmentContext) child;
                String nextText = visitExecuteImmediadeVariableSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.DeclareProcedureSegmentContext) {
                OracleGrammarParser.DeclareProcedureSegmentContext nextCtx = (OracleGrammarParser.DeclareProcedureSegmentContext) child;
                String nextText = visitDeclareProcedureSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.DeclareVariableSegmentContext) {
                OracleGrammarParser.DeclareVariableSegmentContext nextCtx = (OracleGrammarParser.DeclareVariableSegmentContext) child;
                String nextText = visitDeclareVariableSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.AssignSegmentContext) {
                OracleGrammarParser.AssignSegmentContext nextCtx = (OracleGrammarParser.AssignSegmentContext) child;
                String nextText = visitAssignSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.IfElseSegmentContext) {
                OracleGrammarParser.IfElseSegmentContext nextCtx = (OracleGrammarParser.IfElseSegmentContext) child;
                String nextText = visitIfElseSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.CommitSegmentContext) {
                OracleGrammarParser.CommitSegmentContext nextCtx = (OracleGrammarParser.CommitSegmentContext) child;
                String nextText = visitCommitSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.RollbackSegmentContext) {
                OracleGrammarParser.RollbackSegmentContext nextCtx = (OracleGrammarParser.RollbackSegmentContext) child;
                String nextText = visitRollbackSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionCompositeSegmentContext) {
                OracleGrammarParser.ConditionCompositeSegmentContext nextCtx = (OracleGrammarParser.ConditionCompositeSegmentContext) child;
                String nextText = visitConditionCompositeSegment(nextCtx);
                builder.append(nextText);
            } else {
                throw new IllegalStateException("un-support segment branch: " + child.getClass());
            }
        }
        return builder.toString();
    }

    @Override
    public String visitDeclareProcedureSegment(OracleGrammarParser.DeclareProcedureSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        List<String> terminalList = new ArrayList<>();
        String arguments = null;
        String name = null;
        String type = "procedure";
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) child;
                String text = visitTerminal(nextCtx);
                if ("(".equals(text)) {
                    name = terminalList.get(terminalList.size() - 1);
                    type = terminalList.get(terminalList.size() - 2).toLowerCase();
                }
                terminalList.add(text);
            } else if (child instanceof OracleGrammarParser.ArgumentDeclareListSegmentContext) {
                OracleGrammarParser.ArgumentDeclareListSegmentContext nextCtx = (OracleGrammarParser.ArgumentDeclareListSegmentContext) child;
                String nextText = visitArgumentDeclareListSegment(nextCtx);
                arguments = nextText;
            }
        }
        builder.append("<procedure id=\"").append(name.toUpperCase()).append("\"").append("\n")
                .append(arguments == null ? "" : "\t" + arguments.replace("\n", "\n\t")).append("\n")
                .append("function".equals(type) ? "return.out=\"\"\n" : "")
                .append(">").append("\n")
                .append("\n")
                .append("</procedure>").append("\n");

        return builder.toString();
    }

    @Override
    public String visitArgumentDeclareListSegment(OracleGrammarParser.ArgumentDeclareListSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.ArgumentDeclareSegmentContext) {
                OracleGrammarParser.ArgumentDeclareSegmentContext nextCtx = (OracleGrammarParser.ArgumentDeclareSegmentContext) child;
                String nextText = visitArgumentDeclareSegment(nextCtx);
                if (!isFirst) {
                    builder.append("\n");
                }
                builder.append(nextText);
                isFirst = false;
            }
        }

        return builder.toString();
    }

    @Override
    public String visitArgumentDeclareSegment(OracleGrammarParser.ArgumentDeclareSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> terminals = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) child;
                String nextText = visitTerminal(nextCtx);
                terminals.add(nextText);
            }
        }
        String name = terminals.get(0).toUpperCase();
        String type = terminals.get(terminals.size() - 1).toUpperCase();
        boolean isOut = false;
        for (String item : terminals) {
            if ("OUT".equalsIgnoreCase(item)) {
                isOut = true;
                break;
            }
        }
        if (Arrays.asList("INT", "SMALLINT", "TINYINT").contains(type)) {
            type = "int";
        } else if (Arrays.asList("BIGINT", "LONG").contains(type)) {
            type = "long";
        } else if (Arrays.asList("FLOAT", "DOUBLE", "REAL", "NUMBER", "DECIMAL").contains(type)) {
            type = "double";
        } else {
            type = "string";
        }
        builder.append(name).append(".").append(type).append(isOut ? ".out" : "").append("=\"\"");
        return builder.toString();
    }

    @Override
    public String visitDeclareVariableSegment(OracleGrammarParser.DeclareVariableSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        String name = null;
        String type = null;
        String defaultValue = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.SqlIdentifierContext) {
                OracleGrammarParser.SqlIdentifierContext nextCtx = (OracleGrammarParser.SqlIdentifierContext) child;
                String nextText = visitSqlIdentifier(nextCtx);
                nextText = unescapeSqlIdentifier(nextText);
                name = nextText.toUpperCase();
            } else if (child instanceof OracleGrammarParser.SqlDataTypeContext) {
                OracleGrammarParser.SqlDataTypeContext nextCtx = (OracleGrammarParser.SqlDataTypeContext) child;
                String nextText = visitSqlDataType(nextCtx);
                type = nextText;
            } else if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                defaultValue = nextText;
            }
        }
        builder.append("<lang-eval-ts>").append("\n");
        if (defaultValue != null) {
            builder.append(name).append("=").append(defaultValue).append(";");
        } else {
            builder.append(name).append("=").append("null;");
        }
        builder.append("\n").append("</lang-eval-ts>");
        return builder.toString();
    }

    @Override
    public String visitSqlDataType(OracleGrammarParser.SqlDataTypeContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> numbers = new ArrayList<>();
        String name = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.SqlIdentifierContext) {
                OracleGrammarParser.SqlIdentifierContext nextCtx = (OracleGrammarParser.SqlIdentifierContext) child;
                String nextText = visitSqlIdentifier(nextCtx);
                name = nextText.toUpperCase();
            } else if (child instanceof OracleGrammarParser.SqlNumberContext) {
                OracleGrammarParser.SqlNumberContext nextCtx = (OracleGrammarParser.SqlNumberContext) child;
                String nextText = visitSqlNumber(nextCtx);
                numbers.add(nextText);
            }
        }
        builder.append(name);
        if (!numbers.isEmpty()) {
            builder.append("(");
            boolean isFirst = true;
            for (String number : numbers) {
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(number);
                isFirst = false;
            }
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public String visitConditionSegment(OracleGrammarParser.ConditionSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        if (count == 3) {
            ParseTree child = ctx.getChild(0);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext leftCtx = (OracleGrammarParser.VariableSegmentContext) child;
                TerminalNode operatorCtx = (TerminalNode) ctx.getChild(1);
                OracleGrammarParser.VariableSegmentContext rightCtx = (OracleGrammarParser.VariableSegmentContext) ctx.getChild(2);

                String leftText = visitVariableSegment(leftCtx);
                String operatorText = visitTerminal(operatorCtx).toLowerCase();
                String rightText = visitVariableSegment(rightCtx);
                if ("<>".equals(operatorText)) {
                    operatorText = "!=";
                }
                if ("=".equals(operatorText)) {
                    operatorText = "==";
                }
                operatorText = operatorText.replace("<", "&lt;");
                if ("like".equals(operatorText)) {
                    if (mode == ConvertType.OGNL) {
                        builder.append(unescapeSqlIdentifier(leftText)).append(".contains(").append(unescapeSqlIdentifier(rightText)).append(")");
                    } else {
                        builder.append("like(").append(leftText).append(", ").append(rightText).append(")");
                    }
                } else {
                    if (mode == ConvertType.OGNL) {
                        builder.append(unescapeSqlIdentifier(leftText)).append(" ").append(operatorText).append(" ").append(unescapeSqlIdentifier(rightText));
                    } else {
                        builder.append(leftText).append(" ").append(operatorText).append(" ").append(rightText);
                    }
                }
            }
        } else if (count == 1) {
            ParseTree child = ctx.getChild(0);
            if (child instanceof OracleGrammarParser.ConditionIsNullSegmentContext) {
                OracleGrammarParser.ConditionIsNullSegmentContext nextCtx = (OracleGrammarParser.ConditionIsNullSegmentContext) child;
                String nextText = visitConditionIsNullSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionIsNotNullSegmentContext) {
                OracleGrammarParser.ConditionIsNotNullSegmentContext nextCtx = (OracleGrammarParser.ConditionIsNotNullSegmentContext) child;
                String nextText = visitConditionIsNotNullSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionNotLikeSegmentContext) {
                OracleGrammarParser.ConditionNotLikeSegmentContext nextCtx = (OracleGrammarParser.ConditionNotLikeSegmentContext) child;
                String nextText = visitConditionNotLikeSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionBetweenSegmentContext) {
                OracleGrammarParser.ConditionBetweenSegmentContext nextCtx = (OracleGrammarParser.ConditionBetweenSegmentContext) child;
                String nextText = visitConditionBetweenSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionInSegmentContext) {
                OracleGrammarParser.ConditionInSegmentContext nextCtx = (OracleGrammarParser.ConditionInSegmentContext) child;
                String nextText = visitConditionInSegment(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.ConditionNotInSegmentContext) {
                OracleGrammarParser.ConditionNotInSegmentContext nextCtx = (OracleGrammarParser.ConditionNotInSegmentContext) child;
                String nextText = visitConditionNotInSegment(nextCtx);
                builder.append(nextText);
            }
        }
        return builder.toString();
    }

    @Override
    public String visitConditionInSegment(OracleGrammarParser.ConditionInSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        String name = null;
        String vars = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                name = nextText;
            } else if (child instanceof OracleGrammarParser.VariableListSegmentContext) {
                OracleGrammarParser.VariableListSegmentContext nextCtx = (OracleGrammarParser.VariableListSegmentContext) child;
                String nextText = visitVariableListSegment(nextCtx);
                vars = nextText;
            }
        }
        if (mode == ConvertType.OGNL) {
            builder.append(unescapeSqlIdentifier(name)).append(" ").append("in").append(" {").append(vars).append("}");
        } else {
            builder.append(name).append(" ").append("in").append(" [").append(vars).append("]");
        }
        return builder.toString();
    }

    @Override
    public String visitConditionNotInSegment(OracleGrammarParser.ConditionNotInSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        String name = null;
        String vars = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                name = nextText;
            } else if (child instanceof OracleGrammarParser.VariableListSegmentContext) {
                OracleGrammarParser.VariableListSegmentContext nextCtx = (OracleGrammarParser.VariableListSegmentContext) child;
                String nextText = visitVariableListSegment(nextCtx);
                vars = nextText;
            }
        }
        if (mode == ConvertType.OGNL) {
            builder.append(unescapeSqlIdentifier(name)).append(" ").append("not in").append(" {").append(vars).append("}");
        } else {
            builder.append(name).append(" ").append("notin").append(" [").append(vars).append("]");
        }
        return builder.toString();
    }

    @Override
    public String visitVariableListSegment(OracleGrammarParser.VariableListSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(nextText);
                isFirst = false;
            }
        }
        return builder.toString();
    }

    @Override
    public String visitConditionBetweenSegment(OracleGrammarParser.ConditionBetweenSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                list.add(nextText);
            }
        }
        builder.append("(").append(list.get(0)).append(" >= ").append(list.get(1))
                .append(" and ").append(list.get(0)).append(" &lt; ").append(list.get(2)).append(")");
        return builder.toString();
    }

    @Override
    public String visitConditionNotLikeSegment(OracleGrammarParser.ConditionNotLikeSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                list.add(nextText);
            }
        }
        if (mode == ConvertType.OGNL) {
            builder.append("!").append(unescapeSqlIdentifier(list.get(0))).append(".contains(").append(unescapeSqlIdentifier(list.get(1))).append(")");
        } else {
            builder.append("!like(").append(list.get(0)).append(", ").append(list.get(1)).append(")");
        }
        return builder.toString();
    }

    @Override
    public String visitConditionIsNotNullSegment(OracleGrammarParser.ConditionIsNotNullSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                list.add(nextText);
            }
        }
        if (mode == ConvertType.OGNL) {
            builder.append("(")
                    .append(unescapeSqlIdentifier(list.get(0))).append("==null")
                    .append(" or ").append(unescapeSqlIdentifier(list.get(0))).append("==''")
                    .append(")");
        } else {
            builder.append("!evl(").append(list.get(0)).append(")");
        }
        return builder.toString();
    }

    @Override
    public String visitConditionIsNullSegment(OracleGrammarParser.ConditionIsNullSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                list.add(nextText);
            }
        }
        if (mode == ConvertType.OGNL) {
            builder.append("(")
                    .append(unescapeSqlIdentifier(list.get(0))).append("==null")
                    .append(" or ").append(unescapeSqlIdentifier(list.get(0))).append("==''")
                    .append(")");
        } else {
            builder.append("evl(").append(list.get(0)).append(")");
        }
        return builder.toString();
    }

    @Override
    public String visitConditionCompositeSegment(OracleGrammarParser.ConditionCompositeSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        ParseTree item = ctx.getChild(0);
        if (item instanceof TerminalNode) { // ( conditionCompositeSegment )
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof OracleGrammarParser.ConditionCompositeSegmentContext) {
                    OracleGrammarParser.ConditionCompositeSegmentContext nextCtx = (OracleGrammarParser.ConditionCompositeSegmentContext) child;
                    String nextText = visitConditionCompositeSegment(nextCtx);
                    builder.append(nextText);
                } else if (child instanceof TerminalNode) {
                    TerminalNode nextCtx = (TerminalNode) child;
                    String nextText = visitTerminal(nextCtx).toLowerCase();
                    builder.append(" ").append(nextText).append(" ");
                }
            }
        } else if (item instanceof OracleGrammarParser.ConditionSegmentContext) { // conditionSegment
            OracleGrammarParser.ConditionSegmentContext nextCtx = (OracleGrammarParser.ConditionSegmentContext) item;
            String nextText = visitConditionSegment(nextCtx);
            builder.append(nextText);
        } else if (item instanceof OracleGrammarParser.ConditionCompositeSegmentContext) { // conditionCompositeSegment (KEY_AND|KEY_OR) conditionCompositeSegment
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof OracleGrammarParser.ConditionCompositeSegmentContext) {
                    OracleGrammarParser.ConditionCompositeSegmentContext nextCtx = (OracleGrammarParser.ConditionCompositeSegmentContext) child;
                    String nextText = visitConditionCompositeSegment(nextCtx);
                    builder.append(nextText);
                } else if (child instanceof TerminalNode) {
                    TerminalNode nextCtx = (TerminalNode) child;
                    String nextText = visitTerminal(nextCtx).toLowerCase();
                    builder.append(" ").append(nextText).append(" ");
                }
            }
        }
        return builder.toString();
    }

    @Override
    public String visitIfElseSegment(OracleGrammarParser.IfElseSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, String>> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.ConditionCompositeSegmentContext) {
                OracleGrammarParser.ConditionCompositeSegmentContext nextCtx = (OracleGrammarParser.ConditionCompositeSegmentContext) child;
                String nextText = visitConditionCompositeSegment(nextCtx);
                list.add(new AbstractMap.SimpleEntry<>(nextText, null));
            } else if (child instanceof OracleGrammarParser.ScriptContext) {
                OracleGrammarParser.ScriptContext nextCtx = (OracleGrammarParser.ScriptContext) child;
                String nextText = visitScript(nextCtx);
                Map.Entry<String, String> entry = list.get(list.size() - 1);
                if (entry.getValue() == null) {
                    entry.setValue(nextText);
                } else {
                    list.add(new AbstractMap.SimpleEntry<>(null, nextText));
                }
            }
        }
        if (mode == ConvertType.TINY_SCRIPT) {
            builder.append("<lang-eval-ts>").append("\n");
            for (int i = 0; i < list.size(); i++) {
                Map.Entry<String, String> entry = list.get(i);
                String value = entry.getValue();
                if (value.startsWith("<lang-eval-ts>")) {
                    value = value.substring("<lang-eval-ts>".length(), value.length() - "</lang-eval-ts>".length());
                }
                if (entry.getKey() != null) {
                    if (i != 0) {
                        builder.append("\nelse ");
                    }
                    builder.append(" if (").append(entry.getKey()).append(" ) {").append("\n")
                            .append(value).append("\n")
                            .append("} ");
                } else {
                    builder.append(" else {").append("\n")
                            .append(value).append("\n")
                            .append("} ");
                }
                if (i == list.size() - 1) {
                    builder.append(";").append("\n");
                }
            }
            builder.append("</lang-eval-ts>").append("\n");
        } else {
            builder.append("<lang-choose>").append("\n");
            for (int i = 0; i < list.size(); i++) {
                Map.Entry<String, String> entry = list.get(i);
                if (entry.getKey() != null) {
                    builder.append("<lang-when test").append(mode == ConvertType.OGNL ? "" : ".eval-ts").append("=\"").append(entry.getKey()).append("\">").append("\n")
                            .append(entry.getValue()).append("\n")
                            .append("</lang-when>").append("\n");
                } else {
                    builder.append("<lang-otherwise>").append("\n")
                            .append(entry.getValue()).append("\n")
                            .append("</lang-otherwise>").append("\n");
                }
            }
            builder.append("</lang-choose>").append("\n");
        }
        return builder.toString();
    }

    @Override
    public String visitVariableSegment(OracleGrammarParser.VariableSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        if (count == 1) {
            ParseTree child = ctx.getChild(0);
            if (child instanceof OracleGrammarParser.SqlStringContext) { // sqlString
                OracleGrammarParser.SqlStringContext nextCtx = (OracleGrammarParser.SqlStringContext) child;
                String nextText = visitSqlString(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.SqlNumberContext) { // sqlNumber
                OracleGrammarParser.SqlNumberContext nextCtx = (OracleGrammarParser.SqlNumberContext) child;
                String nextText = visitSqlNumber(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.SqlNullContext) { // sqlNull
                OracleGrammarParser.SqlNullContext nextCtx = (OracleGrammarParser.SqlNullContext) child;
                String nextText = visitSqlNull(nextCtx);
                builder.append(nextText);
            } else if (child instanceof OracleGrammarParser.SqlIdentifierContext) { // sqlIdentifier
                OracleGrammarParser.SqlIdentifierContext nextCtx = (OracleGrammarParser.SqlIdentifierContext) child;
                String nextText = visitSqlIdentifier(nextCtx);
                builder.append(nextText.toUpperCase());
            } else if (child instanceof OracleGrammarParser.FunctionSegmentContext) { // functionSegment
                OracleGrammarParser.FunctionSegmentContext nextCtx = (OracleGrammarParser.FunctionSegmentContext) child;
                String nextText = visitFunctionSegment(nextCtx);
                builder.append(nextText);
            }
        } else if (count >= 3) {
            ParseTree first = ctx.getChild(0);
            if (first instanceof TerminalNode) { // '(' variableSegment ')'
                for (int i = 0; i < count; i++) {
                    ParseTree child = ctx.getChild(i);
                    if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                        OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                        String nextText = visitVariableSegment(nextCtx);
                        builder.append(nextText);
                    } else if (child instanceof TerminalNode) {
                        TerminalNode nextCtx = (TerminalNode) child;
                        String nextText = visitTerminal(nextCtx);
                        builder.append(" ").append(nextText).append(" ");
                    }
                }
            } else {
                ParseTree second = ctx.getChild(1);
                if (second instanceof TerminalNode) {
                    TerminalNode sepCtx = (TerminalNode) second;
                    String separator = visitTerminal(sepCtx);
                    if ("||".equals(separator)) { // variableSegment ('||' variableSegment)+
                        boolean isFirst = true;
                        for (int i = 0; i < count; i++) {
                            ParseTree child = ctx.getChild(i);
                            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                                String nextText = visitVariableSegment(nextCtx);
                                if (!isFirst) {
                                    if (mode == ConvertType.TINY_SCRIPT) {
                                        builder.append(" + ");
                                    }
                                }
                                if (isSqlIdentifier(nextText)) {
                                    builder.append(nextText.replace("${", "$!{"));
                                } else if (isSqlString(nextText)) {
                                    if (mode == ConvertType.TINY_SCRIPT) {
                                        builder.append(nextText);
                                    } else {
                                        builder.append(unescapeSqlString(nextText));
                                    }
                                } else {
                                    builder.append(nextText);
                                }
                                isFirst = false;
                            }
                        }
                    } else { // variableSegment ('*' | '/' | '+' | '-') variableSegment
                        for (int i = 0; i < count; i++) {
                            ParseTree child = ctx.getChild(i);
                            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                                String nextText = visitVariableSegment(nextCtx);
                                builder.append(nextText);
                            } else if (child instanceof TerminalNode) {
                                TerminalNode nextCtx = (TerminalNode) child;
                                String nextText = visitTerminal(nextCtx);
                                builder.append(" ").append(nextText).append(" ");
                            }
                        }
                    }
                }
            }

        }
        return builder.toString();
    }

    public boolean isSqlIdentifier(String text) {
        if (text == null) {
            return false;
        }
        if (text.startsWith("$")) {
            return true;
        }
        return false;
    }

    public String escapeSqlIdentifier(String text) {
        if (text == null) {
            return null;
        }
        return "${" + text + "}";
    }

    public String unescapeSqlIdentifier(String text) {
        if (text == null) {
            return null;
        }
        if (text.startsWith("${")) {
            text = text.substring(2, text.length() - 1);
        }
        return text;
    }

    public boolean isSqlString(String text) {
        if (text == null) {
            return false;
        }
        if (text.startsWith("'")) {
            return true;
        }
        return false;
    }

    public String escapeSqlString(String text) {
        if (text == null) {
            return null;
        }
        return "'" + text.replace("'", "''") + "'";
    }

    public String unescapeSqlString(String text) {
        if (text == null) {
            return null;
        }
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        return text.replace("''", "'");
    }

    public String escapeXmlString(String text) {
        if (text == null) {
            return null;
        }
        text = text.replace("<", "&lt;");
        return text;
    }

    protected String functionName;

    @Override
    public String visitFunctionSegment(OracleGrammarParser.FunctionSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        String name = null;
        String arguments = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.SqlIdentifierContext) {
                OracleGrammarParser.SqlIdentifierContext nextCtx = (OracleGrammarParser.SqlIdentifierContext) child;
                String nextText = visitSqlIdentifier(nextCtx);
                name = unescapeSqlIdentifier(nextText);
                name = name.toUpperCase();
                functionName = name;
            } else if (child instanceof OracleGrammarParser.ArgumentListSegmentContext) {
                OracleGrammarParser.ArgumentListSegmentContext nextCtx = (OracleGrammarParser.ArgumentListSegmentContext) child;
                ConvertType bakMode = mode;
                mode = ConvertType.TINY_SCRIPT;
                String nextText = visitArgumentListSegment(nextCtx);
                mode = bakMode;
                arguments = nextText;
            }
        }
        functionName = null;

        if ("nvl".equals(name)) {
            name = "evl";
        }
        if (name.startsWith("f_")
                || name.startsWith("sp_")) {
            name = name.toUpperCase();
        }
        builder.append(name).append("(").append(arguments == null ? "" : arguments).append(")");
        return builder.toString();
    }

    @Override
    public String visitArgumentListSegment(OracleGrammarParser.ArgumentListSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        int count = ctx.getChildCount();
        int argumentIndex = 0;
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.VariableSegmentContext) {
                OracleGrammarParser.VariableSegmentContext nextCtx = (OracleGrammarParser.VariableSegmentContext) child;
                String nextText = visitVariableSegment(nextCtx);
                if (!isFirst) {
                    builder.append(", ");
                }
                if (argumentIndex == 1) {
                    if ("substr".equals(functionName)) {
                        try {
                            nextText = String.valueOf(Integer.parseInt(nextText) - 1);
                        } catch (Throwable e) {
                            nextText = "((" + nextText + ") - 1)";
                        }
                    } else if ("to_date".equals(functionName)
                            || "to_char".equals(functionName)) {
                        nextText = nextText.replace("YYYY", "yyyy");
                        nextText = nextText.replace("mm", "MM");
                        nextText = nextText.replace("DD", "dd");
                        nextText = nextText.replace("hh24", "HH");
                        nextText = nextText.replace("HH24", "HH");
                        nextText = nextText.replace("hh12", "hh");
                        nextText = nextText.replace("HH12", "hh");
                        nextText = nextText.replace("mi", "mm");
                        nextText = nextText.replace("MI", "mm");
                        nextText = nextText.replace("SS", "ss");
                    }
                }
                builder.append(nextText);
                argumentIndex++;
                isFirst = false;
            }
        }
        return builder.toString();
    }

    @Override
    public String visitCommitSegment(OracleGrammarParser.CommitSegmentContext ctx) {
        return "<sql-trans-commit/>";
    }

    @Override
    public String visitRollbackSegment(OracleGrammarParser.RollbackSegmentContext ctx) {
        return "<sql-trans-rollback/>";
    }

    @Override
    public String visitExecuteImmediadeVariableSegment(OracleGrammarParser.ExecuteImmediadeVariableSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) child;
                String nextText = visitTerminal(nextCtx);
                list.add(nextText);
            }
        }
        if (list.size() == 3) {
            builder.append("<sql-update script=\"").append(list.get(2)).append("\"/>").append("\n");
        } else if (list.size() == 5) {
            builder.append("<sql-update")
                    .append(" result=\"").append(list.get(4)).append("\"")
                    .append(" script=\"").append(list.get(2)).append("\"/>").append("\n");
        }
        return builder.toString();
    }

    @Override
    public String visitAssignSegment(OracleGrammarParser.AssignSegmentContext ctx) {
        StringBuilder builder = new StringBuilder();
        String name = null;
        String value = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof OracleGrammarParser.SqlIdentifierContext) {
                OracleGrammarParser.SqlIdentifierContext nextCtx = (OracleGrammarParser.SqlIdentifierContext) child;
                String nextText = visitSqlIdentifier(nextCtx);
                name = unescapeSqlIdentifier(nextText);
            } else if (child instanceof OracleGrammarParser.SegmentContext) {
                OracleGrammarParser.SegmentContext nextCtx = (OracleGrammarParser.SegmentContext) child;
                String nextText = visitSegment(nextCtx);
                value = nextText;
            }
        }
        if (value.contains("$!{")) {
            value = escapeXmlString(value);
            if (mode == ConvertType.TINY_SCRIPT) {
                builder.append("<lang-eval-ts>").append("\n");
                builder.append(name.toUpperCase()).append("=").append("R\"").append(value).append("\"").append(";").append("\n");
                builder.append("</lang-eval-ts>");
            } else {
                if (value.contains("\n") || value.length() > 100) {
                    builder.append("<lang-render result=\"").append(name.toUpperCase()).append("\"").append(" _lang=\"sql\">\n")
                            .append(value).append("\n")
                            .append("</lang-render>").append("");
                } else {
                    builder.append("<lang-set result=\"").append(name.toUpperCase()).append("\"")
                            .append(" value.render=\"").append(value).append("\"")
                            .append(" _lang=\"sql\"/>\n");
                }
            }


        } else if (isSqlString(value)) {
            value = escapeXmlString(unescapeSqlString(value));
            if (mode == ConvertType.TINY_SCRIPT) {
                builder.append("<lang-eval-ts>").append("\n");
                builder.append(name.toUpperCase()).append("=").append("\"").append(value).append("\"").append(";").append("\n");
                builder.append("</lang-eval-ts>");
            } else {
                if (value.contains("\n") || value.length() > 100) {
                    builder.append("<lang-string result=\"").append(name.toUpperCase()).append("\"").append(" _lang=\"sql\">\n")
                            .append(value).append("\n")
                            .append("</lang-string>").append("");
                } else {
                    builder.append("<lang-set result=\"").append(name.toUpperCase()).append("\"")
                            .append(" value.string=\"").append(value).append("\"")
                            .append(" _lang=\"sql\"/>\n");
                }
            }
        } else {
            builder.append("<lang-eval-ts>").append("\n");
            builder.append(name.toUpperCase()).append("=").append(value).append(";").append("\n");
            builder.append("</lang-eval-ts>");
        }
        return builder.toString();
    }

    @Override
    public String visitSqlNull(OracleGrammarParser.SqlNullContext ctx) {
        return "null";
    }

    @Override
    public String visitSqlIdentifier(OracleGrammarParser.SqlIdentifierContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalNode nextCtx = (TerminalNode) child;
        String nextText = visitTerminal(nextCtx);
        nextText = unescapeSqlString(nextText);
        return escapeSqlIdentifier(nextText);
    }

    @Override
    public String visitSqlNumber(OracleGrammarParser.SqlNumberContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalNode nextCtx = (TerminalNode) child;
        String nextText = visitTerminal(nextCtx);
        nextText = nextText.replace("_", "");
        return nextText;
    }

    @Override
    public String visitSqlString(OracleGrammarParser.SqlStringContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalNode nextCtx = (TerminalNode) child;
        String nextText = visitTerminal(nextCtx);
        nextText = unescapeSqlString(nextText);
        return escapeSqlString(nextText);
    }

    @Override
    public String visit(ParseTree parseTree) {
        if (parseTree instanceof OracleGrammarParser.ConvertContext) {
            OracleGrammarParser.ConvertContext nextCtx = (OracleGrammarParser.ConvertContext) parseTree;
            return visitConvert(nextCtx);
        } else if (parseTree instanceof OracleGrammarParser.ScriptContext) {
            OracleGrammarParser.ScriptContext nextCtx = (OracleGrammarParser.ScriptContext) parseTree;
            return visitScript(nextCtx);
        } else if (parseTree instanceof OracleGrammarParser.SegmentContext) {
            OracleGrammarParser.SegmentContext nextCtx = (OracleGrammarParser.SegmentContext) parseTree;
            return visitSegment(nextCtx);
        }
        throw new IllegalStateException("un-support visit node :" + parseTree.getClass());
    }

    @Override
    public String visitChildren(RuleNode ruleNode) {
        StringBuilder builder = new StringBuilder();
        int count = ruleNode.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ruleNode.getChild(i);
            String nextText = visit(child);
            builder.append(nextText).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String visitTerminal(TerminalNode terminalNode) {
        return terminalNode.getText();
    }

    @Override
    public String visitErrorNode(ErrorNode errorNode) {
        throw new IllegalStateException("error node!");
    }
}
