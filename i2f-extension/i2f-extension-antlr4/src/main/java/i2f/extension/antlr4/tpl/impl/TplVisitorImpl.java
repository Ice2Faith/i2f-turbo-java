package i2f.extension.antlr4.tpl.impl;

import i2f.jvm.JvmUtil;
import i2f.extension.antlr4.tpl.grammar.TplParser;
import i2f.extension.antlr4.tpl.grammar.TplVisitor;
import i2f.extension.antlr4.tpl.impl.exception.TplException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplBreakException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplContinueException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplEvaluateException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplReturnException;
import i2f.extension.antlr4.tpl.impl.value.ParameterValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/7/31 16:11
 * @desc
 */
@Data
@NoArgsConstructor
public class TplVisitorImpl implements TplVisitor<Object> {
    protected Object context = new HashMap<>();
    protected String scriptFileName;
    protected int scriptLineOffset;
    protected TplResolver resolver = new DefaultTplResolver();

    public TplVisitorImpl(Object context) {
        this.context = context;
    }

    public TplVisitorImpl(Object context, TplResolver resolver) {
        this.context = context;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    public TplVisitorImpl(Object context, String scriptFileName, TplResolver resolver) {
        this.context = context;
        this.scriptFileName = scriptFileName;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    public TplVisitorImpl(Object context, String scriptFileName, int scriptLineOffset, TplResolver resolver) {
        this.context = context;
        this.scriptFileName = scriptFileName;
        this.scriptLineOffset = scriptLineOffset;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }


    @Override
    public Object visitRoot(TplParser.RootContext ctx) {
        Object ret = null;
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.SegmentContext) {
                    TplParser.SegmentContext nextCtx = (TplParser.SegmentContext) next;
                    Object nextValue = visitSegment(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (TplContinueException | TplBreakException e) {

        } catch (TplReturnException e) {
            return e.getRetValue();
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Object visitSegment(TplParser.SegmentContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.BlockContext) {
                    TplParser.BlockContext nextCtx = (TplParser.BlockContext) next;
                    Object nextValue = visitBlock(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                } else if (next instanceof TplParser.ValueContext) {
                    TplParser.ValueContext nextCtx = (TplParser.ValueContext) next;
                    Object nextValue = visitValue(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                } else if (next instanceof TplParser.TextContext) {
                    TplParser.TextContext nextCtx = (TplParser.TextContext) next;
                    Object nextValue = visitText(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlock(TplParser.BlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof TplParser.IfBlockContext) {
                TplParser.IfBlockContext nextCtx = (TplParser.IfBlockContext) next;
                ret = visitIfBlock(nextCtx);
            } else if (next instanceof TplParser.CommonBlockContext) {
                TplParser.CommonBlockContext nextCtx = (TplParser.CommonBlockContext) next;
                ret = visitCommonBlock(nextCtx);
            }

            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitCommonBlock(TplParser.CommonBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            TplParser.BlockHeadContext nameCtx = null;
            TplParser.ParametersContext parametersCtx = null;
            TplParser.BlockBodyContext bodyCtx = null;

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.BlockHeadContext) {
                    nameCtx = (TplParser.BlockHeadContext) next;
                } else if (next instanceof TplParser.ParametersContext) {
                    parametersCtx = (TplParser.ParametersContext) next;
                } else if (next instanceof TplParser.BlockBodyContext) {
                    bodyCtx = (TplParser.BlockBodyContext) next;
                }
            }

            Object blockName = visitBlockHead(nameCtx);
            List<ParameterValue> parameterList = new ArrayList<>();
            if (parametersCtx != null) {
                parameterList = (List<ParameterValue>) visitParameters(parametersCtx);
            }

            ret = resolver.block(blockName == null ? null : String.valueOf(blockName),
                    parameterList,
                    bodyCtx,
                    context, this);

            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitIfBlock(TplParser.IfBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            TplParser.ParametersContext ifParametersCtx = null;
            TplParser.BlockBodyContext ifBodyCtx = null;
            List<TplParser.ElseBlockContext> elseCtxList = new ArrayList<>();

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.ParametersContext) {
                    ifParametersCtx = (TplParser.ParametersContext) next;
                } else if (next instanceof TplParser.BlockBodyContext) {
                    ifBodyCtx = (TplParser.BlockBodyContext) next;
                } else if (next instanceof TplParser.ElseBlockContext) {
                    elseCtxList.add((TplParser.ElseBlockContext) next);
                }
            }

            List<Map.Entry<TplParser.ParametersContext, TplParser.BlockBodyContext>> branches = new ArrayList<>();
            branches.add(new AbstractMap.SimpleEntry<>(ifParametersCtx, ifBodyCtx));

            for (TplParser.ElseBlockContext elseCtx : elseCtxList) {
                Map.Entry<TplParser.ParametersContext, TplParser.BlockBodyContext> elseEntry = (Map.Entry<TplParser.ParametersContext, TplParser.BlockBodyContext>) visitElseBlock(elseCtx);
                branches.add(elseEntry);
            }

            for (Map.Entry<TplParser.ParametersContext, TplParser.BlockBodyContext> entry : branches) {
                TplParser.ParametersContext parameterCtx = entry.getKey();
                boolean cond = false;
                if (parameterCtx != null) {
                    List<ParameterValue> elseParameterList = (List<ParameterValue>) visitParameters(parameterCtx);
                    ParameterValue elseParameter = elseParameterList.get(0);
                    Object elseVal = resolver.parameter(elseParameter.getExpression(), context, this);
                    cond = resolver.toBoolean(elseVal);
                } else {
                    cond = true;
                }
                if (cond) {
                    TplParser.BlockBodyContext bodyCtx = entry.getValue();
                    if (bodyCtx == null) {
                        throw new TplEvaluateException("if block require body!");
                    }
                    return visitBlockBody(bodyCtx);
                }
            }

            return null;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitElseBlock(TplParser.ElseBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            TplParser.ParametersContext parametersCtx = null;
            TplParser.BlockBodyContext bodyCtx = null;

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.ParametersContext) {
                    parametersCtx = (TplParser.ParametersContext) next;
                } else if (next instanceof TplParser.BlockBodyContext) {
                    bodyCtx = (TplParser.BlockBodyContext) next;
                }
            }

            return new AbstractMap.SimpleEntry<>(parametersCtx, bodyCtx);
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlockHead(TplParser.BlockHeadContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) next;
                Object nextValue = visitTerminal(nextCtx);
                String str = String.valueOf(nextValue);
                ret = str.substring(1);
            }

            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlockBody(TplParser.BlockBodyContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.SegmentContext) {
                    TplParser.SegmentContext nextCtx = (TplParser.SegmentContext) next;
                    Object nextValue = visitSegment(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitParameters(TplParser.ParametersContext ctx) {
        try {
            debugNode(ctx);
            List<ParameterValue> ret = new ArrayList<>();
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.ParameterContext) {
                    TplParser.ParameterContext nextCtx = (TplParser.ParameterContext) next;
                    ParameterValue nextValue = (ParameterValue) visitParameter(nextCtx);
                    ret.add(nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitParameter(TplParser.ParameterContext ctx) {
        try {
            debugNode(ctx);
            ParameterValue ret = new ParameterValue();
            ret.setNode(ctx);

            int count = ctx.getChildCount();
            if (count == 1) {
                ParseTree next = ctx.getChild(0);
                if (next instanceof TerminalNode) {
                    TerminalNode nextCtx = (TerminalNode) next;
                    Object nextValue = visitTerminal(nextCtx);

                    ret.setExpression(nextValue == null ? null : String.valueOf(nextValue));
                }
            } else if (count == 3) {
                ParseTree name = ctx.getChild(0);
                if (name instanceof TerminalNode) {
                    TerminalNode nextCtx = (TerminalNode) name;
                    Object nameValue = visitTerminal(nextCtx);

                    ret.setName(nameValue == null ? null : String.valueOf(nameValue));
                }

                ParseTree next = ctx.getChild(2);
                if (next instanceof TerminalNode) {
                    TerminalNode nextCtx = (TerminalNode) next;
                    Object nextValue = visitTerminal(nextCtx);

                    ret.setExpression(nextValue == null ? null : String.valueOf(nextValue));
                }
            }


            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitValue(TplParser.ValueContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) next;
                Object nextValue = visitTerminal(nextCtx);
                ret = resolver.value(nextValue == null ? null : String.valueOf(nextValue),
                        context, this);
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitText(TplParser.TextContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof TplParser.ContentContext) {
                    TplParser.ContentContext nextCtx = (TplParser.ContentContext) next;
                    Object nextValue = visitContent(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitContent(TplParser.ContentContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) next;
                Object nextValue = visitTerminal(nextCtx);
                ret = nextValue;
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visit(ParseTree tree) {
        if (tree instanceof TplParser.RootContext) {
            TplParser.RootContext nextCtx = (TplParser.RootContext) tree;
            return visitRoot(nextCtx);
        } else if (tree instanceof TplParser.SegmentContext) {
            TplParser.SegmentContext nextCtx = (TplParser.SegmentContext) tree;
            return visitSegment(nextCtx);
        } else if (tree instanceof TplParser.BlockContext) {
            TplParser.BlockContext nextCtx = (TplParser.BlockContext) tree;
            return visitBlock(nextCtx);
        } else if (tree instanceof TplParser.ValueContext) {
            TplParser.ValueContext nextCtx = (TplParser.ValueContext) tree;
            return visitValue(nextCtx);
        } else if (tree instanceof TplParser.TextContext) {
            TplParser.TextContext nextCtx = (TplParser.TextContext) tree;
            return visitText(nextCtx);
        }
        return null;
    }

    @Override
    public Object visitChildren(RuleNode node) {
        // debugNode(node);
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        try {
            // debugNode(node);
            String text = node.getText();
            return text;
        } catch (Throwable e) {
            if (e instanceof TplException) {
                throw (TplException) e;
            }
            throw new TplEvaluateException(getTreeLocationText("location ", node, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitErrorNode(ErrorNode errorNode) {
        // debugNode(node);
        return null;
    }


    public void debugNode(ParseTree context) {
        if (JvmUtil.isDebug()) {
            if (context instanceof ParserRuleContext) {
                ParserRuleContext ruleContext = (ParserRuleContext) context;
                Token start = ruleContext.getStart();
                if (start == null) {
                    int count = 3;
                    do {
                        ParserRuleContext parent = ruleContext.getParent();
                        if (parent != null) {
                            ruleContext = parent;
                            start = ruleContext.getStart();
                        }
                        if (start == null) {
                            break;
                        }
                        count--;
                    } while (count >= 0);
                }
                if (start != null) {
                    resolver.debugBridge(scriptFileName == null ? "virtual_script.tis" : scriptFileName,
                            scriptLineOffset + start.getLine(),
                            () -> {
                                Map<String, Object> variableMap = new HashMap<>();
                                variableMap.put("astNode", context);
                                variableMap.put("root", this.context);
                                variableMap.put("visitor", this);
                                return variableMap;
                            });
                }
            }
        }
        resolver.debugLog(() -> context.getClass().getSimpleName().replace("$", ".") + ": " + context.getText() + getTreeLocationText(", location ", context, null));
    }

    public String getTreeLocationText(String prefix, ParseTree context, String suffix) {
        String loc = "";
        if (context instanceof ParserRuleContext) {
            ParserRuleContext ruleContext = (ParserRuleContext) context;
            Token start = ruleContext.getStart();
            if (start != null) {
                if (prefix == null) {
                    prefix = "";
                }
                if (suffix == null) {
                    suffix = "";
                }
                loc = prefix + "at line " + start.getLine() + ":" + start.getCharPositionInLine() + suffix;
            }
        }
        return loc;
    }
}
