package i2f.extension.antlr4.funvi.lang.impl;

import i2f.extension.antlr4.funvi.grammar.FunviParser;
import i2f.extension.antlr4.funvi.grammar.FunviVisitor;
import i2f.extension.antlr4.funvi.lang.exception.impl.*;
import i2f.extension.antlr4.funvi.lang.resolver.impl.DefaultFunviResolver;
import i2f.extension.antlr4.funvi.lang.resolver.FunviResolver;
import i2f.jvm.JvmUtil;
import i2f.extension.antlr4.funvi.lang.exception.FunviException;
import i2f.extension.antlr4.funvi.lang.exception.impl.FunviEvaluateException;
import i2f.extension.antlr4.funvi.lang.exception.impl.FunviReturnException;
import i2f.extension.antlr4.funvi.lang.value.ParameterValue;
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
public class DefaultFunviVisitor implements FunviVisitor<Object> {
    protected Object context = new HashMap<>();
    protected String scriptFileName;
    protected int scriptLineOffset;
    protected FunviResolver resolver = new DefaultFunviResolver();

    public DefaultFunviVisitor(Object context) {
        this.context = context;
    }

    public DefaultFunviVisitor(Object context, FunviResolver resolver) {
        this.context = context;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    public DefaultFunviVisitor(Object context, String scriptFileName, FunviResolver resolver) {
        this.context = context;
        this.scriptFileName = scriptFileName;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    public DefaultFunviVisitor(Object context, String scriptFileName, int scriptLineOffset, FunviResolver resolver) {
        this.context = context;
        this.scriptFileName = scriptFileName;
        this.scriptLineOffset = scriptLineOffset;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }


    @Override
    public Object visitRoot(FunviParser.RootContext ctx) {
        Object ret = null;
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.SegmentContext) {
                    FunviParser.SegmentContext nextCtx = (FunviParser.SegmentContext) next;
                    Object nextValue = visitSegment(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (FunviContinueException | FunviBreakException e) {

        } catch (FunviReturnException e) {
            return e.getRetValue();
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Object visitSegment(FunviParser.SegmentContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.BlockContext) {
                    FunviParser.BlockContext nextCtx = (FunviParser.BlockContext) next;
                    Object nextValue = visitBlock(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                } else if (next instanceof FunviParser.ValueContext) {
                    FunviParser.ValueContext nextCtx = (FunviParser.ValueContext) next;
                    Object nextValue = visitValue(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                } else if (next instanceof FunviParser.TextContext) {
                    FunviParser.TextContext nextCtx = (FunviParser.TextContext) next;
                    Object nextValue = visitText(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlock(FunviParser.BlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof FunviParser.IfBlockContext) {
                FunviParser.IfBlockContext nextCtx = (FunviParser.IfBlockContext) next;
                ret = visitIfBlock(nextCtx);
            } else if (next instanceof FunviParser.CommonBlockContext) {
                FunviParser.CommonBlockContext nextCtx = (FunviParser.CommonBlockContext) next;
                ret = visitCommonBlock(nextCtx);
            }

            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitCommonBlock(FunviParser.CommonBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            FunviParser.BlockHeadContext nameCtx = null;
            FunviParser.ParametersContext parametersCtx = null;
            FunviParser.BlockBodyContext bodyCtx = null;

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.BlockHeadContext) {
                    nameCtx = (FunviParser.BlockHeadContext) next;
                } else if (next instanceof FunviParser.ParametersContext) {
                    parametersCtx = (FunviParser.ParametersContext) next;
                } else if (next instanceof FunviParser.BlockBodyContext) {
                    bodyCtx = (FunviParser.BlockBodyContext) next;
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
                     this);

            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitIfBlock(FunviParser.IfBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            FunviParser.ParametersContext ifParametersCtx = null;
            FunviParser.BlockBodyContext ifBodyCtx = null;
            List<FunviParser.ElseBlockContext> elseCtxList = new ArrayList<>();

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.ParametersContext) {
                    ifParametersCtx = (FunviParser.ParametersContext) next;
                } else if (next instanceof FunviParser.BlockBodyContext) {
                    ifBodyCtx = (FunviParser.BlockBodyContext) next;
                } else if (next instanceof FunviParser.ElseBlockContext) {
                    elseCtxList.add((FunviParser.ElseBlockContext) next);
                }
            }

            List<Map.Entry<FunviParser.ParametersContext, FunviParser.BlockBodyContext>> branches = new ArrayList<>();
            branches.add(new AbstractMap.SimpleEntry<>(ifParametersCtx, ifBodyCtx));

            for (FunviParser.ElseBlockContext elseCtx : elseCtxList) {
                Map.Entry<FunviParser.ParametersContext, FunviParser.BlockBodyContext> elseEntry = (Map.Entry<FunviParser.ParametersContext, FunviParser.BlockBodyContext>) visitElseBlock(elseCtx);
                branches.add(elseEntry);
            }

            for (Map.Entry<FunviParser.ParametersContext, FunviParser.BlockBodyContext> entry : branches) {
                FunviParser.ParametersContext parameterCtx = entry.getKey();
                boolean cond = false;
                if (parameterCtx != null) {
                    List<ParameterValue> elseParameterList = (List<ParameterValue>) visitParameters(parameterCtx);
                    ParameterValue elseParameter = elseParameterList.get(0);
                    Object elseVal = resolver.parameter(elseParameter.getExpression(),  this);
                    cond = resolver.toBoolean(elseVal);
                } else {
                    cond = true;
                }
                if (cond) {
                    FunviParser.BlockBodyContext bodyCtx = entry.getValue();
                    if (bodyCtx == null) {
                        throw new FunviEvaluateException("if block require body!");
                    }
                    return visitBlockBody(bodyCtx);
                }
            }

            return null;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitElseBlock(FunviParser.ElseBlockContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;

            FunviParser.ParametersContext parametersCtx = null;
            FunviParser.BlockBodyContext bodyCtx = null;

            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.ParametersContext) {
                    parametersCtx = (FunviParser.ParametersContext) next;
                } else if (next instanceof FunviParser.BlockBodyContext) {
                    bodyCtx = (FunviParser.BlockBodyContext) next;
                }
            }

            return new AbstractMap.SimpleEntry<>(parametersCtx, bodyCtx);
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlockHead(FunviParser.BlockHeadContext ctx) {
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
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBlockBody(FunviParser.BlockBodyContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.SegmentContext) {
                    FunviParser.SegmentContext nextCtx = (FunviParser.SegmentContext) next;
                    Object nextValue = visitSegment(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitParameters(FunviParser.ParametersContext ctx) {
        try {
            debugNode(ctx);
            List<ParameterValue> ret = new ArrayList<>();
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.ParameterContext) {
                    FunviParser.ParameterContext nextCtx = (FunviParser.ParameterContext) next;
                    ParameterValue nextValue = (ParameterValue) visitParameter(nextCtx);
                    ret.add(nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitParameter(FunviParser.ParameterContext ctx) {
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
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitValue(FunviParser.ValueContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            ParseTree next = ctx.getChild(0);
            if (next instanceof TerminalNode) {
                TerminalNode nextCtx = (TerminalNode) next;
                Object nextValue = visitTerminal(nextCtx);
                ret = resolver.value(nextValue == null ? null : String.valueOf(nextValue),
                         this);
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitText(FunviParser.TextContext ctx) {
        try {
            debugNode(ctx);
            Object ret = null;
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree next = ctx.getChild(i);
                if (next instanceof FunviParser.ContentContext) {
                    FunviParser.ContentContext nextCtx = (FunviParser.ContentContext) next;
                    Object nextValue = visitContent(nextCtx);
                    ret = resolver.concat(ret, nextValue);
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitContent(FunviParser.ContentContext ctx) {
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
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visit(ParseTree tree) {
        if (tree instanceof FunviParser.RootContext) {
            FunviParser.RootContext nextCtx = (FunviParser.RootContext) tree;
            return visitRoot(nextCtx);
        } else if (tree instanceof FunviParser.SegmentContext) {
            FunviParser.SegmentContext nextCtx = (FunviParser.SegmentContext) tree;
            return visitSegment(nextCtx);
        } else if (tree instanceof FunviParser.BlockContext) {
            FunviParser.BlockContext nextCtx = (FunviParser.BlockContext) tree;
            return visitBlock(nextCtx);
        } else if (tree instanceof FunviParser.ValueContext) {
            FunviParser.ValueContext nextCtx = (FunviParser.ValueContext) tree;
            return visitValue(nextCtx);
        } else if (tree instanceof FunviParser.TextContext) {
            FunviParser.TextContext nextCtx = (FunviParser.TextContext) tree;
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
            if (e instanceof FunviException) {
                throw (FunviException) e;
            }
            throw new FunviEvaluateException(getTreeLocationText("location ", node, " ") + "cause by: " + e.getMessage(), e);
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
