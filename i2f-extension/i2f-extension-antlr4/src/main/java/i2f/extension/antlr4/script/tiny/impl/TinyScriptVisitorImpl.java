package i2f.extension.antlr4.script.tiny.impl;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptException;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptBreakException;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptContinueException;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptEvaluateException;
import i2f.extension.antlr4.script.tiny.impl.exception.impl.TinyScriptReturnException;
import i2f.reflect.ReflectResolver;
import i2f.typeof.TypeOf;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/20 21:03
 * @desc
 */
public class TinyScriptVisitorImpl implements TinyScriptVisitor<Object> {
    protected Object context = new HashMap<>();
    protected TinyScriptResolver resolver = new DefaultTinyScriptResolver();

    public TinyScriptVisitorImpl(Object context) {
        this.context = context;
    }

    public TinyScriptVisitorImpl(Object context, TinyScriptResolver resolver) {
        this.context = context;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    public void debugNode(ParseTree context) {
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

    @Override
    public Object visitScript(TinyScriptParser.ScriptContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing segments!");
            }
            TinyScriptParser.SegmentsContext segmentsCtx = null;
            TerminalNode eofNode = null;
            if (count == 2) {
                segmentsCtx = (TinyScriptParser.SegmentsContext) ctx.getChild(0);
                eofNode = (TerminalNode) ctx.getChild(1);
            } else {
                eofNode = (TerminalNode) ctx.getChild(0);
            }
            String eof = (String) visitTerminal(eofNode);
            if (!"<EOF>".equals(eof)) {
                throw new IllegalArgumentException("invalid grammar, expect '<EOF>' but found '" + eof + "'!");
            }
            if (segmentsCtx == null) {
                return null;
            }
            return visitSegments(segmentsCtx);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitSegments(TinyScriptParser.SegmentsContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing segments!");
            }
            Object ret = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!";".equals(term)) {
                        throw new IllegalArgumentException("invalid grammar separator, expect ';' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ExpressContext) {
                    TinyScriptParser.ExpressContext nextCtx = (TinyScriptParser.ExpressContext) item;
                    Object value = visitExpress(nextCtx);
                    ret = value;
                } else {
                    throw new IllegalArgumentException("invalid grammar node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitExpress(TinyScriptParser.ExpressContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing express!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TinyScriptParser.IfSegmentContext) {
                TinyScriptParser.IfSegmentContext nextCtx = (TinyScriptParser.IfSegmentContext) item;
                return visitIfSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.ForeachSegmentContext) {
                TinyScriptParser.ForeachSegmentContext nextCtx = (TinyScriptParser.ForeachSegmentContext) item;
                return visitForeachSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.ForSegmentContext) {
                TinyScriptParser.ForSegmentContext nextCtx = (TinyScriptParser.ForSegmentContext) item;
                return visitForSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.WhileSegmentContext) {
                TinyScriptParser.WhileSegmentContext nextCtx = (TinyScriptParser.WhileSegmentContext) item;
                return visitWhileSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.ControlSegmentContext) {
                TinyScriptParser.ControlSegmentContext nextCtx = (TinyScriptParser.ControlSegmentContext) item;
                return visitControlSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.TrySegmentContext) {
                TinyScriptParser.TrySegmentContext nextCtx = (TinyScriptParser.TrySegmentContext) item;
                return visitTrySegment(nextCtx);
            } else if (item instanceof TerminalNode) {
                if (count == 2) {
                    TerminalNode termNode = (TerminalNode) ctx.getChild(0);
                    String term = (String) visitTerminal(termNode);
                    String prefixOperator = term;
                    ParseTree expressNode = ctx.getChild(1);
                    if (!(expressNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid double-operator right node, expect express, but found type: " + expressNode.getClass());
                    }
                    TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) expressNode;
                    Object value = visitExpress(expressCtx);
                    return resolver.resolvePrefixOperator(context, prefixOperator, value);
                }
            } else if (item instanceof TinyScriptParser.ExpressContext) {
                if (count == 2) {
                    ParseTree leftNode = item;
                    ParseTree operatorNode = ctx.getChild(1);
                    if (!(leftNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid suffix-operator left node, expect express, but found type: " + leftNode.getClass());
                    }
                    if (!(operatorNode instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid suffix-operator operator node, expect operator, but found type: " + operatorNode.getClass());
                    }

                    TinyScriptParser.ExpressContext leftCtx = (TinyScriptParser.ExpressContext) leftNode;
                    TerminalNode operatorCtx = (TerminalNode) operatorNode;
                    Object left = visitExpress(leftCtx);
                    String operator = (String) visitTerminal(operatorCtx);
                    return resolver.resolveSuffixOperator(context, left, operator);
                } else if (count == 3) {
                    ParseTree leftNode = item;
                    ParseTree operatorNode = ctx.getChild(1);
                    ParseTree rightNode = ctx.getChild(2);
                    if (!(leftNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid double-operator left node, expect express, but found type: " + leftNode.getClass());
                    }
                    if (!(operatorNode instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid double-operator middle node, expect operator, but found type: " + operatorNode.getClass());
                    }
                    if (!(rightNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid double-operator right node, expect express, but found type: " + leftNode.getClass());
                    }
                    TinyScriptParser.ExpressContext leftCtx = (TinyScriptParser.ExpressContext) leftNode;
                    TerminalNode operatorCtx = (TerminalNode) operatorNode;
                    TinyScriptParser.ExpressContext rightCtx = (TinyScriptParser.ExpressContext) rightNode;
                    Supplier<Object> leftSupplier = () -> visitExpress(leftCtx);
                    Supplier<Object> rightSupplier = () -> visitExpress(rightCtx);
                    String operator = (String) visitTerminal(operatorCtx);
                    return resolver.resolveDoubleOperator(context, leftSupplier, operator, rightSupplier);
                } else if (count == 5) {
                    ParseTree condNode = item;
                    ParseTree questionNode = ctx.getChild(1);
                    ParseTree trueNode = ctx.getChild(2);
                    ParseTree elseNode = ctx.getChild(3);
                    ParseTree falseNode = ctx.getChild(4);
                    if (!(condNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid ternary-operator condition node, expect express, but found type: " + condNode.getClass());
                    }
                    if (!(questionNode instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid ternary-operator '?' terminal node, expect operator, but found type: " + questionNode.getClass());
                    }
                    if (!(trueNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid ternary-operator true-value node, expect express, but found type: " + trueNode.getClass());
                    }
                    if (!(elseNode instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid ternary-operator ':' terminal node, expect operator, but found type: " + elseNode.getClass());
                    }
                    if (!(falseNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid ternary-operator false-value node, expect express, but found type: " + falseNode.getClass());
                    }
                    TinyScriptParser.ExpressContext condCtx = (TinyScriptParser.ExpressContext) condNode;
                    TerminalNode questionCtx = (TerminalNode) questionNode;
                    TinyScriptParser.ExpressContext trueCtx = (TinyScriptParser.ExpressContext) trueNode;
                    TerminalNode elseCtx = (TerminalNode) elseNode;
                    TinyScriptParser.ExpressContext falseCtx = (TinyScriptParser.ExpressContext) falseNode;

                    String question = (String) visitTerminal(questionCtx);
                    if (!"?".equals(question)) {
                        throw new IllegalArgumentException("invalid  ternary-operator operator, expect '?' but found '" + question + "'!");
                    }
                    String elseSep = (String) visitTerminal(elseCtx);
                    if (!":".equals(elseSep)) {
                        throw new IllegalArgumentException("invalid  ternary-operator operator, expect ':' but found '" + elseSep + "'!");
                    }
                    Object obj = visitExpress(condCtx);
                    boolean ok = resolver.toBoolean(context, obj);
                    if (ok) {
                        return visitExpress(trueCtx);
                    } else {
                        return visitExpress(falseCtx);
                    }
                } else {
                    throw new IllegalArgumentException("invalid grammar express expect 2/3/5 parts for double operator, but found " + count + "!");
                }
            } else if (item instanceof TinyScriptParser.ParenSegmentContext) {
                TinyScriptParser.ParenSegmentContext nextCtx = (TinyScriptParser.ParenSegmentContext) item;
                return visitParenSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.EqualValueContext) {
                TinyScriptParser.EqualValueContext nextCtx = (TinyScriptParser.EqualValueContext) item;
                return visitEqualValue(nextCtx);
            } else if (item instanceof TinyScriptParser.NewInstanceContext) {
                TinyScriptParser.NewInstanceContext nextCtx = (TinyScriptParser.NewInstanceContext) item;
                return visitNewInstance(nextCtx);
            } else if (item instanceof TinyScriptParser.InvokeFunctionContext) {
                TinyScriptParser.InvokeFunctionContext nextCtx = (TinyScriptParser.InvokeFunctionContext) item;
                return visitInvokeFunction(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstValueContext) {
                TinyScriptParser.ConstValueContext nextCtx = (TinyScriptParser.ConstValueContext) item;
                return visitConstValue(nextCtx);
            } else if (item instanceof TinyScriptParser.RefValueContext) {
                TinyScriptParser.RefValueContext nextCtx = (TinyScriptParser.RefValueContext) item;
                return visitRefValue(nextCtx);
            } else if (item instanceof TinyScriptParser.JsonValueContext) {
                TinyScriptParser.JsonValueContext nextCtx = (TinyScriptParser.JsonValueContext) item;
                return visitJsonValue(nextCtx);
            } else if (item instanceof TinyScriptParser.DebuggerSegmentContext) {
                TinyScriptParser.DebuggerSegmentContext nextCtx = (TinyScriptParser.DebuggerSegmentContext) item;
                return visitDebuggerSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.ThrowSegmentContext) {
                TinyScriptParser.ThrowSegmentContext nextCtx = (TinyScriptParser.ThrowSegmentContext) item;
                return visitThrowSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.NegtiveSegmentContext) {
                TinyScriptParser.NegtiveSegmentContext nextCtx = (TinyScriptParser.NegtiveSegmentContext) item;
                return visitNegtiveSegment(nextCtx);
            } else if (item instanceof TinyScriptParser.StaticEnumValueContext) {
                TinyScriptParser.StaticEnumValueContext nextCtx = (TinyScriptParser.StaticEnumValueContext) item;
                return visitStaticEnumValue(nextCtx);
            }
            throw new IllegalArgumentException("un-support express found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitNegtiveSegment(TinyScriptParser.NegtiveSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 2) {
                throw new IllegalArgumentException("missing negtive segment block!");
            }
            ParseTree termNode = ctx.getChild(0);
            ParseTree objectNode = ctx.getChild(1);
            if (!(termNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid negtive segment block, expect terminal node, but found " + ctx.getClass() + "!");
            }
            if (!(objectNode instanceof TinyScriptParser.ExpressContext)) {
                throw new IllegalArgumentException("invalid negtive segment block, expect express node, but found " + ctx.getClass() + "!");
            }
            TerminalNode termCtx = (TerminalNode) termNode;
            String term = (String) visitTerminal(termCtx);
            if (!"-".equals(term)) {
                throw new IllegalArgumentException("invalid negtive operator, expect '-' but found '" + term + "'!");
            }
            TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) objectNode;
            Object obj = visitExpress(expressCtx);
            return resolver.resolvePrefixOperator(context, term, obj);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static class CatchMetadata {
        public TinyScriptParser.CatchBodyBlockContext catchBodyCtx;
        public TinyScriptParser.NamingBlockContext namingCtx;
        public List<TinyScriptParser.ClassNameBlockContext> classNameCtxList;
    }

    @Override
    public Object visitTrySegment(TinyScriptParser.TrySegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing try segment!");
            }
            TinyScriptParser.TryBodyBlockContext tryBodyCtx = null;
            List<CatchMetadata> catchList = new ArrayList<>();
            TinyScriptParser.FinallyBodyBlockContext finallyCtx = null;
            CatchMetadata catchMetadata = new CatchMetadata();
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!Arrays.asList("try", "catch", "(", "|", ")", "finally").contains(term)) {
                        throw new IllegalArgumentException("invalid grammar separator, expect 'try/catch/(/|/)/finally' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.TryBodyBlockContext) {
                    TinyScriptParser.TryBodyBlockContext nextCtx = (TinyScriptParser.TryBodyBlockContext) item;
                    tryBodyCtx = nextCtx;
                } else if (item instanceof TinyScriptParser.ClassNameBlockContext) {
                    catchMetadata = new CatchMetadata();
                    TinyScriptParser.ClassNameBlockContext nextCtx = (TinyScriptParser.ClassNameBlockContext) item;
                    catchMetadata.classNameCtxList.add(nextCtx);
                } else if (item instanceof TinyScriptParser.NamingBlockContext) {
                    TinyScriptParser.NamingBlockContext nextCtx = (TinyScriptParser.NamingBlockContext) item;
                    catchMetadata.namingCtx = nextCtx;
                } else if (item instanceof TinyScriptParser.CatchBodyBlockContext) {
                    TinyScriptParser.CatchBodyBlockContext nextCtx = (TinyScriptParser.CatchBodyBlockContext) item;
                    catchMetadata.catchBodyCtx = nextCtx;
                    catchList.add(catchMetadata);
                } else if (item instanceof TinyScriptParser.FinallyBodyBlockContext) {
                    TinyScriptParser.FinallyBodyBlockContext nextCtx = (TinyScriptParser.FinallyBodyBlockContext) item;
                    finallyCtx = nextCtx;
                } else {
                    throw new IllegalArgumentException("invalid try segment node type: " + item.getClass());
                }
            }
            try {
                return visitTryBodyBlock(tryBodyCtx);
            } catch (Throwable e) {
                for (CatchMetadata item : catchList) {
                    if (item.classNameCtxList == null || item.classNameCtxList.isEmpty()) {
                        continue;
                    }
                    List<TinyScriptParser.ClassNameBlockContext> ctxList = item.classNameCtxList;
                    boolean matched = false;
                    for (TinyScriptParser.ClassNameBlockContext clsCtx : ctxList) {
                        String clsName = (String) visitClassNameBlock(clsCtx);
                        Class<?> clazz = resolver.loadClass(context, clsName);
                        if (clazz != null) {
                            if (TypeOf.instanceOf(e, clazz)) {
                                matched = true;
                                break;
                            }
                        }
                    }
                    if (matched) {
                        if (item.namingCtx != null) {
                            String naming = (String) visitNamingBlock(item.namingCtx);
                            resolver.setValue(context, naming, e);
                        }
                        if (item.catchBodyCtx != null) {
                            visitCatchBodyBlock(item.catchBodyCtx);
                        }
                        break;
                    }
                }
            } finally {
                if (finallyCtx != null) {
                    visitFinallyBodyBlock(finallyCtx);
                }
            }
            throw new IllegalArgumentException("un-support try segment found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitThrowSegment(TinyScriptParser.ThrowSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 2) {
                throw new IllegalArgumentException("missing throw segment block!");
            }
            ParseTree termNode = ctx.getChild(0);
            ParseTree objectNode = ctx.getChild(1);
            if (!(termNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid throw segment block, expect terminal node, but found " + ctx.getClass() + "!");
            }
            if (!(objectNode instanceof TinyScriptParser.ExpressContext)) {
                throw new IllegalArgumentException("invalid throw segment block, expect express node, but found " + ctx.getClass() + "!");
            }
            TerminalNode termCtx = (TerminalNode) termNode;
            String term = (String) visitTerminal(termCtx);
            if (!"throw".equals(term)) {
                throw new IllegalArgumentException("invalid keyword, expect 'throw' but found '" + term + "'!");
            }
            TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) objectNode;
            Object obj = visitExpress(expressCtx);
            if (obj instanceof Throwable) {
                throw (Throwable) obj;
            } else {
                throw new IllegalArgumentException("invalid throw object type, expect type of Throwable.class, but found " + (obj == null ? "null" : obj.getClass()) + "!");
            }
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitTryBodyBlock(TinyScriptParser.TryBodyBlockContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing try body block!");
            }
            ParseTree item = ctx.getChild(0);
            if (!(item instanceof TinyScriptParser.ScriptBlockContext)) {
                throw new IllegalArgumentException("invalid try body block, expect script block, but found " + ctx.getClass() + "!");
            }
            TinyScriptParser.ScriptBlockContext nextCtx = (TinyScriptParser.ScriptBlockContext) item;
            return visitScriptBlock(nextCtx);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitCatchBodyBlock(TinyScriptParser.CatchBodyBlockContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing catch body block!");
            }
            ParseTree item = ctx.getChild(0);
            if (!(item instanceof TinyScriptParser.ScriptBlockContext)) {
                throw new IllegalArgumentException("invalid catch body block, expect script block, but found " + ctx.getClass() + "!");
            }
            TinyScriptParser.ScriptBlockContext nextCtx = (TinyScriptParser.ScriptBlockContext) item;
            return visitScriptBlock(nextCtx);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitFinallyBodyBlock(TinyScriptParser.FinallyBodyBlockContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing finally body block!");
            }
            ParseTree item = ctx.getChild(0);
            if (!(item instanceof TinyScriptParser.ScriptBlockContext)) {
                throw new IllegalArgumentException("invalid finally body block, expect script block, but found " + ctx.getClass() + "!");
            }
            TinyScriptParser.ScriptBlockContext nextCtx = (TinyScriptParser.ScriptBlockContext) item;
            return visitScriptBlock(nextCtx);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitClassNameBlock(TinyScriptParser.ClassNameBlockContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing class name block!");
            }
            ParseTree item = ctx.getChild(0);
            if (!(item instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid class name block expect naming, but found " + ctx.getClass() + "!");
            }
            TerminalNode terminalNode = (TerminalNode) item;
            String className = (String) visitTerminal(terminalNode);
            return className;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitParenSegment(TinyScriptParser.ParenSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing parent segment!");
            }
            if (count == 3) {
                ParseTree lParenNode = ctx.getChild(0);
                ParseTree expressNode = ctx.getChild(1);
                ParseTree rParenNode = ctx.getChild(2);
                if (!(lParenNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid paren left node, expect '(', but found type: " + lParenNode.getClass());
                }
                if (!(expressNode instanceof TinyScriptParser.ExpressContext)) {
                    throw new IllegalArgumentException("invalid paren right node, expect express, but found type: " + expressNode.getClass());
                }
                if (!(rParenNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid paren right node, expect ')', but found type: " + rParenNode.getClass());
                }
                TerminalNode lParenCtx = (TerminalNode) lParenNode;
                TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) expressNode;
                TerminalNode rParenCtx = (TerminalNode) rParenNode;
                String lParen = (String) visitTerminal(lParenCtx);
                String rParen = (String) visitTerminal(rParenCtx);
                if (!"(".equals(lParen)) {
                    throw new IllegalArgumentException("invalid paren left node, expect '(', but found : " + lParen);
                }
                if (!")".equals(rParen)) {
                    throw new IllegalArgumentException("invalid paren right node, expect '(', but found : " + rParen);
                }
                Object value = visitExpress(expressCtx);
                return value;
            }
            throw new IllegalArgumentException("invalid grammar parent segment expect 3 parts for paren segment (...), but found " + count + "!");
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }


    @Override
    public Object visitDebuggerSegment(TinyScriptParser.DebuggerSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing debugger segment!");
            }
            TinyScriptParser.NamingBlockContext namingCtx = null;
            TinyScriptParser.ConditionBlockContext condCtx = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!Arrays.asList("debugger", "(", ")").contains(term)) {
                        throw new IllegalArgumentException("invalid grammar separator, expect 'debugger/(/)' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ConditionBlockContext) {
                    TinyScriptParser.ConditionBlockContext nextCtx = (TinyScriptParser.ConditionBlockContext) item;
                    condCtx = nextCtx;
                } else if (item instanceof TinyScriptParser.NamingBlockContext) {
                    TinyScriptParser.NamingBlockContext nextCtx = (TinyScriptParser.NamingBlockContext) item;
                    namingCtx = nextCtx;
                } else {
                    throw new IllegalArgumentException("invalid debugger segment node type: " + item.getClass());
                }
            }
            String tag = null;
            boolean ok = true;
            if (namingCtx != null) {
                tag = (String) visitNamingBlock(namingCtx);
            }
            if (condCtx != null) {
                ok = (Boolean) visitConditionBlock(condCtx);
            }
            if (ok) {
                resolver.openDebugger(context, tag, (condCtx == null ? null : condCtx.getText()));
            }
            return null;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitIfSegment(TinyScriptParser.IfSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing if segment!");
            }
            boolean cond = false;
            boolean open = false;
            TinyScriptParser.ScriptBlockContext elseCtx = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!Arrays.asList("if", "(", ")", "else", "elif").contains(term)) {
                        throw new IllegalArgumentException("invalid if segment separator, expect 'if/(/)/else/elif' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ConditionBlockContext) {
                    TinyScriptParser.ConditionBlockContext nextCtx = (TinyScriptParser.ConditionBlockContext) item;
                    cond = (Boolean) visitConditionBlock(nextCtx);
                    open = true;
                } else if (item instanceof TinyScriptParser.ScriptBlockContext) {
                    TinyScriptParser.ScriptBlockContext nextCtx = (TinyScriptParser.ScriptBlockContext) item;
                    if (!open) {
                        elseCtx = nextCtx;
                    }
                    open = false;
                    if (cond) {
                        Object value = visitScriptBlock(nextCtx);
                        return value;
                    }
                } else {
                    throw new IllegalArgumentException("invalid if segment node type: " + item.getClass());
                }
            }
            if (!open) {
                if (elseCtx != null) {
                    Object value = visitScriptBlock(elseCtx);
                    return value;
                }
            }
            return null;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static String unescapeString(String term) {
        term = term.replace("\\r", "\r");
        term = term.replace("\\n", "\n");
        term = term.replace("\\t", "\t");
        term = term.replace("\\\"", "\"");
        term = term.replace("\\'", "'");
        term = term.replace("\\\\", "\\");
        return term;
    }

    @Override
    public Object visitControlSegment(TinyScriptParser.ControlSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing control segment!");
            }
            ParseTree controlNode = ctx.getChild(0);
            if (!(controlNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid control segment node type: " + controlNode.getClass());
            }
            TerminalNode controlCtx = (TerminalNode) controlNode;
            String control = (String) visitTerminal(controlCtx);
            if ("break".equals(control)) {
                throw new TinyScriptBreakException();
            } else if ("continue".equals(control)) {
                throw new TinyScriptContinueException();
            } else if ("return".equals(control)) {
                if (count > 1) {
                    ParseTree expressNode = ctx.getChild(1);
                    if (!(expressNode instanceof TinyScriptParser.ExpressContext)) {
                        throw new IllegalArgumentException("invalid control return segment node type: " + expressNode.getClass());
                    }
                    TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) expressNode;
                    Object retValue = visitExpress(expressCtx);
                    throw new TinyScriptReturnException(retValue);
                } else {
                    throw new TinyScriptReturnException();
                }
            }
            throw new IllegalArgumentException("un-support control segment node found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitWhileSegment(TinyScriptParser.WhileSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing while segment!");
            }
            TinyScriptParser.ConditionBlockContext contitionCtx = null;
            TinyScriptParser.ScriptBlockContext scriptCtx = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TinyScriptParser.ConditionBlockContext) {
                    contitionCtx = (TinyScriptParser.ConditionBlockContext) item;
                } else if (item instanceof TinyScriptParser.ScriptBlockContext) {
                    scriptCtx = (TinyScriptParser.ScriptBlockContext) item;
                } else {
                    if (!(item instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid while segment node type: " + item.getClass());
                    }
                    TerminalNode terminalNode = (TerminalNode) item;
                    String term = (String) visitTerminal(terminalNode);
                    if (!Arrays.asList("while", "(", ")").contains(term)) {
                        throw new IllegalArgumentException("invalid while segment node , expect '(/)' , but found '" + term + "'!");
                    }
                }
            }
            if (contitionCtx == null) {
                throw new IllegalArgumentException("invalid while segment, missing condition node!");
            }
            if (scriptCtx == null) {
                throw new IllegalArgumentException("invalid while segment, missing script node!");
            }
            Object lastValue = null;
            while (true) {
                boolean ok = (Boolean) visitConditionBlock(contitionCtx);
                if (!ok) {
                    break;
                }
                try {
                    lastValue = visitScriptBlock(scriptCtx);
                } catch (TinyScriptBreakException e) {
                    break;
                } catch (TinyScriptContinueException e) {
                    continue;
                }
            }
            return lastValue;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitForSegment(TinyScriptParser.ForSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing for segment!");
            }
            TinyScriptParser.ExpressContext initCtx = null;
            TinyScriptParser.ConditionBlockContext conditionCtx = null;
            TinyScriptParser.ExpressContext incrCtx = null;
            TinyScriptParser.ScriptBlockContext scriptCtx = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TinyScriptParser.ExpressContext) {
                    if (initCtx == null) {
                        initCtx = (TinyScriptParser.ExpressContext) item;
                    } else {
                        incrCtx = (TinyScriptParser.ExpressContext) item;
                    }
                } else if (item instanceof TinyScriptParser.ConditionBlockContext) {
                    conditionCtx = (TinyScriptParser.ConditionBlockContext) item;
                } else if (item instanceof TinyScriptParser.ScriptBlockContext) {
                    scriptCtx = (TinyScriptParser.ScriptBlockContext) item;
                } else {
                    if (!(item instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid for segment node type: " + item.getClass());
                    }
                    TerminalNode terminalNode = (TerminalNode) item;
                    String term = (String) visitTerminal(terminalNode);
                    if (!Arrays.asList("for", "(", ";", ")").contains(term)) {
                        throw new IllegalArgumentException("invalid for segment node , expect '(/;/)' , but found '" + term + "'!");
                    }
                }
            }
            if (initCtx == null) {
                throw new IllegalArgumentException("invalid for segment, missing init node!");
            }
            if (conditionCtx == null) {
                throw new IllegalArgumentException("invalid for segment, missing condition node!");
            }
            if (incrCtx == null) {
                throw new IllegalArgumentException("invalid for segment, missing incr node!");
            }
            if (scriptCtx == null) {
                throw new IllegalArgumentException("invalid for segment, missing script node!");
            }
            Object lastValue = null;
            Object initValue = visitExpress(initCtx);
            while (true) {
                boolean ok = (Boolean) visitConditionBlock(conditionCtx);
                if (!ok) {
                    break;
                }
                try {
                    lastValue = visitScriptBlock(scriptCtx);
                } catch (TinyScriptBreakException e) {
                    break;
                } catch (TinyScriptContinueException e) {
                    Object incrValue = visitExpress(incrCtx);
                    continue;
                }

                Object incrValue = visitExpress(incrCtx);
            }

            return lastValue;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitForeachSegment(TinyScriptParser.ForeachSegmentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing foreach segment!");
            }
            TinyScriptParser.NamingBlockContext namingCtx = null;
            TinyScriptParser.ExpressContext expressCtx = null;
            TinyScriptParser.ScriptBlockContext scriptCtx = null;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TinyScriptParser.NamingBlockContext) {
                    namingCtx = (TinyScriptParser.NamingBlockContext) item;
                } else if (item instanceof TinyScriptParser.ExpressContext) {
                    expressCtx = (TinyScriptParser.ExpressContext) item;
                } else if (item instanceof TinyScriptParser.ScriptBlockContext) {
                    scriptCtx = (TinyScriptParser.ScriptBlockContext) item;
                } else {
                    if (!(item instanceof TerminalNode)) {
                        throw new IllegalArgumentException("invalid foreach segment node type: " + item.getClass());
                    }
                    TerminalNode terminalNode = (TerminalNode) item;
                    String term = (String) visitTerminal(terminalNode);
                    if (!Arrays.asList("foreach", "(", ":", ")").contains(term)) {
                        throw new IllegalArgumentException("invalid foreach segment node , expect '(/:/)' , but found '" + term + "'!");
                    }
                }
            }
            if (namingCtx == null) {
                throw new IllegalArgumentException("invalid foreach segment, missing naming node!");
            }
            if (expressCtx == null) {
                throw new IllegalArgumentException("invalid foreach segment, missing express node!");
            }
            if (scriptCtx == null) {
                throw new IllegalArgumentException("invalid foreach segment, missing script node!");
            }
            String naming = (String) visitNamingBlock(namingCtx);
            Object iter = visitExpress(expressCtx);
            Object lastValue = null;
            if (iter instanceof Iterable) {
                Iterable<?> iterable = (Iterable<?>) iter;
                for (Object item : iterable) {
                    try {
                        resolver.setValue(context, naming, item);
                        lastValue = visitScriptBlock(scriptCtx);
                    } catch (TinyScriptBreakException e) {
                        break;
                    } catch (TinyScriptContinueException e) {
                        continue;
                    }

                }
            } else if (iter instanceof Iterator) {
                Iterator<?> iterator = (Iterator<?>) iter;

                while (iterator.hasNext()) {
                    Object item = iterator.next();
                    try {
                        resolver.setValue(context, naming, item);
                        lastValue = visitScriptBlock(scriptCtx);
                    } catch (TinyScriptBreakException e) {
                        break;
                    } catch (TinyScriptContinueException e) {
                        continue;
                    }

                }
            } else if (iter instanceof Enumeration) {
                Enumeration<?> enumeration = (Enumeration<?>) iter;

                while (enumeration.hasMoreElements()) {
                    Object item = enumeration.nextElement();
                    try {
                        resolver.setValue(context, naming, item);
                        lastValue = visitScriptBlock(scriptCtx);
                    } catch (TinyScriptBreakException e) {
                        break;
                    } catch (TinyScriptContinueException e) {
                        continue;
                    }

                }
            } else if (iter instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) iter;
                for (Object item : map.keySet()) {
                    try {
                        resolver.setValue(context, naming, item);
                        lastValue = visitScriptBlock(scriptCtx);
                    } catch (TinyScriptBreakException e) {
                        break;
                    } catch (TinyScriptContinueException e) {
                        continue;
                    }

                }
            } else if (iter != null && iter.getClass().isArray()) {
                int len = Array.getLength(iter);
                for (int i = 0; i < len; i++) {
                    Object item = Array.get(iter, i);
                    try {
                        resolver.setValue(context, naming, item);
                        lastValue = visitScriptBlock(scriptCtx);
                    } catch (TinyScriptBreakException e) {
                        break;
                    } catch (TinyScriptContinueException e) {
                        continue;
                    }
                }
            } else {
                try {
                    resolver.setValue(context, naming, iter);
                    lastValue = visitScriptBlock(scriptCtx);
                } catch (TinyScriptBreakException e) {

                } catch (TinyScriptContinueException e) {

                }
            }
            return lastValue;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConditionBlock(TinyScriptParser.ConditionBlockContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 1) {
                throw new IllegalArgumentException("missing condition block, expect 1 express, but found " + count + "!");
            }
            ParseTree item = ctx.getChild(0);
            if (!(item instanceof TinyScriptParser.ExpressContext)) {
                throw new IllegalArgumentException("invalid condition block express node, expect express, but found type: " + item.getClass());
            }
            TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) item;
            Object ret = visitExpress(expressCtx);
            return resolver.toBoolean(context, ret);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitNamingBlock(TinyScriptParser.NamingBlockContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing naming block!");
            }
            ParseTree namingNode = ctx.getChild(0);
            if (!(namingNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid naming block node type: " + namingNode.getClass());
            }
            TerminalNode namingCtx = (TerminalNode) namingNode;
            String naming = (String) visitTerminal(namingCtx);
            return naming;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitEqualValue(TinyScriptParser.EqualValueContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 3) {
                throw new IllegalArgumentException("missing equal value, expect 3 parts, but found " + count + "!");
            }
            ParseTree namingNode = ctx.getChild(0);
            ParseTree equalNode = ctx.getChild(1);
            ParseTree expressNode = ctx.getChild(2);
            if (!(namingNode instanceof TerminalNode)
                    && !(namingNode instanceof TinyScriptParser.ExtractExpressContext)
                    && !(namingNode instanceof TinyScriptParser.StaticEnumValueContext)
            ) {
                throw new IllegalArgumentException("invalid equal value, expect naming/extract/static-enum node, but found " + namingNode.getClass() + "!");
            }
            if (!(equalNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid equal value, expect equal node, but found " + namingNode.getClass() + "!");
            }
            if (!(expressNode instanceof TinyScriptParser.ExpressContext)) {
                throw new IllegalArgumentException("invalid equal value, expect express node, but found " + namingNode.getClass() + "!");
            }

            TerminalNode equalCtx = (TerminalNode) equalNode;
            TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) expressNode;
            String equal = (String) visitTerminal(equalCtx);
            if (!Arrays.asList("=", "?=", ".=", "+=", "-=", "*=", "/=", "%=").contains(equal)) {
                throw new IllegalArgumentException("invalid equal value, expect '=,?=,.=,+=,-=,*=,/=,%=', but found '" + equal + "'!");
            }
            Object value = null;
            if (!Arrays.asList("?=", ".=").contains(equal)) {
                value = visitExpress(expressCtx);
            }

            if (namingNode instanceof TerminalNode) {
                TerminalNode namingCtx = (TerminalNode) namingNode;
                String naming = (String) visitTerminal(namingCtx);
                if (naming == null || naming.isEmpty()) {
                    throw new IllegalArgumentException("invalid equal value, expect naming, but found '" + naming + "'!");
                }
                if (!"=".equals(equal)) {
                    Object oldVal = resolver.getValue(context, naming);
                    if ("?=".equals(equal)) {
                        if (oldVal == null) {
                            value = visitExpress(expressCtx);
                            resolver.setValue(context, naming, value);
                        }
                    } else if (".=".equals(equal)) {
                        if (oldVal != null) {
                            value = visitExpress(expressCtx);
                            resolver.setValue(context, naming, value);
                        }
                    } else if ("+=".equals(equal)) {
                        value = resolver.resolveDoubleOperator(context, oldVal, "+", value);
                        resolver.setValue(context, naming, value);
                    } else if ("-=".equals(equal)) {
                        value = resolver.resolveDoubleOperator(context, oldVal, "-", value);
                        resolver.setValue(context, naming, value);
                    } else if ("*=".equals(equal)) {
                        value = resolver.resolveDoubleOperator(context, oldVal, "*", value);
                        resolver.setValue(context, naming, value);
                    } else if ("/=".equals(equal)) {
                        value = resolver.resolveDoubleOperator(context, oldVal, "/", value);
                        resolver.setValue(context, naming, value);
                    } else if ("%=".equals(equal)) {
                        value = resolver.resolveDoubleOperator(context, oldVal, "%", value);
                        resolver.setValue(context, naming, value);
                    }
                } else {
                    resolver.setValue(context, naming, value);
                }
            } else if (namingNode instanceof TinyScriptParser.ExtractExpressContext) {
                TinyScriptParser.ExtractExpressContext extractCtx = (TinyScriptParser.ExtractExpressContext) namingNode;
                Map<String, String> extractMapping = (Map<String, String>) visitExtractExpress(extractCtx);
                for (Map.Entry<String, String> entry : extractMapping.entrySet()) {
                    String naming = entry.getKey();
                    String express = entry.getValue();
                    Object val = resolver.getValue(value, express);
                    resolver.setValue(context, naming, val);
                }
            } else if (namingNode instanceof TinyScriptParser.StaticEnumValueContext) {
                TinyScriptParser.StaticEnumValueContext staticCtx = (TinyScriptParser.StaticEnumValueContext) namingNode;
                Map.Entry<Class<?>, Object> entry = getStaticEnumValue(staticCtx);
                Object ctxValue = entry.getValue();
                if (ctxValue instanceof Field) {
                    Field field = (Field) ctxValue;
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers)) {
                        throw new IllegalArgumentException("un-support static field : " + field);
                    }
                    if (Modifier.isFinal(modifiers)) {
                        throw new IllegalArgumentException("un-support set static final field : " + field);
                    }
                    field.setAccessible(true);

                    if (!"=".equals(equal)) {
                        Object oldVal = field.get(null);
                        if ("?=".equals(equal)) {
                            if (oldVal == null) {
                                value = visitExpress(expressCtx);
                                field.set(null, value);
                            }
                        } else if (".=".equals(equal)) {
                            if (oldVal != null) {
                                value = visitExpress(expressCtx);
                                field.set(null, value);
                            }
                        } else if ("+=".equals(equal)) {
                            value = resolver.resolveDoubleOperator(context, oldVal, "+", value);
                            field.set(null, value);
                        } else if ("-=".equals(equal)) {
                            value = resolver.resolveDoubleOperator(context, oldVal, "-", value);
                            field.set(null, value);
                        } else if ("*=".equals(equal)) {
                            value = resolver.resolveDoubleOperator(context, oldVal, "*", value);
                            field.set(null, value);
                        } else if ("/=".equals(equal)) {
                            value = resolver.resolveDoubleOperator(context, oldVal, "/", value);
                            field.set(null, value);
                        } else if ("%=".equals(equal)) {
                            value = resolver.resolveDoubleOperator(context, oldVal, "%", value);
                            field.set(null, value);
                        }
                    } else {
                        field.set(null, value);
                    }
                } else {
                    return value;
                }
            }
            return value;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitExtractExpress(TinyScriptParser.ExtractExpressContext ctx) {
        try {
            debugNode(ctx);
            Map<String, String> ret = new LinkedHashMap<>();
            int count = ctx.getChildCount();
            if (count != 3 && count != 4) {
                throw new IllegalArgumentException("missing json map value!");
            }
            ParseTree startNode = ctx.getChild(0);
            ParseTree leftNode = ctx.getChild(1);
            ParseTree pairsNode = null;
            ParseTree rightNode = null;
            if (count == 3) {
                rightNode = ctx.getChild(2);
            } else {
                pairsNode = ctx.getChild(2);
                rightNode = ctx.getChild(3);
            }
            if (!(startNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid extract express value left node, expect '#', but found type: " + startNode.getClass());
            }
            if (!(leftNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid extract express value left node, expect '{', but found type: " + leftNode.getClass());
            }
            if (!(rightNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid extract express value right node, expect '}', but found type: " + rightNode.getClass());
            }
            if (pairsNode != null) {
                if (!(pairsNode instanceof TinyScriptParser.ExtractPairsContext)) {
                    throw new IllegalArgumentException("invalid extract express value middle node, expect extract pairs node, but found type: " + pairsNode.getClass());
                }
            }
            TerminalNode startCtx = (TerminalNode) startNode;
            TerminalNode leftCtx = (TerminalNode) leftNode;
            TinyScriptParser.ExtractPairsContext pairsCtx = null;
            TerminalNode rightCtx = (TerminalNode) rightNode;
            if (pairsNode != null) {
                pairsCtx = (TinyScriptParser.ExtractPairsContext) pairsNode;
            }
            String start = (String) visitTerminal(startCtx);
            String left = (String) visitTerminal(leftCtx);
            String right = (String) visitTerminal(rightCtx);
            if (!"#".equals(start)) {
                throw new IllegalArgumentException("invalid extract express value left node, expect '#', but found : " + start);
            }
            if (!"{".equals(left)) {
                throw new IllegalArgumentException("invalid extract express value left node, expect '{', but found : " + left);
            }
            if (!"}".equals(right)) {
                throw new IllegalArgumentException("invalid extract express value right right node, expect '}', but found : " + right);
            }
            if (pairsCtx != null) {
                return visitExtractPairs(new ExtractPairsContextImpl(pairsCtx, ret));
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static class ExtractPairsContextImpl extends TinyScriptParser.ExtractPairsContext {
        public TinyScriptParser.ExtractPairsContext target;
        public Object value;

        public ExtractPairsContextImpl(TinyScriptParser.ExtractPairsContext target, Object value) {
            super(null, 0);
            this.target = target;
            this.value = value;
        }
    }

    @Override
    public Object visitExtractPairs(TinyScriptParser.ExtractPairsContext ctx) {
        try {
            debugNode(ctx);
            Map<String, String> ret = new LinkedHashMap<>();
            if (ctx instanceof ExtractPairsContextImpl) {
                ExtractPairsContextImpl impl = (ExtractPairsContextImpl) ctx;
                ctx = impl.target;
                ret = (Map<String, String>) impl.value;
            }
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!",".equals(term)) {
                        throw new IllegalArgumentException("invalid extract pairs separator, expect ',' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ExtractPairContext) {
                    TinyScriptParser.ExtractPairContext nextCtx = (TinyScriptParser.ExtractPairContext) item;
                    Map.Entry<String, String> value = (Map.Entry<String, String>) visitExtractPair(nextCtx);
                    ret.put(value.getKey(), value.getValue());
                } else {
                    throw new IllegalArgumentException("invalid extract pairs node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitExtractPair(TinyScriptParser.ExtractPairContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 1 && count != 3) {
                throw new IllegalArgumentException("missing extract pair!");
            }
            ParseTree keyNode = ctx.getChild(0);
            if (!(keyNode instanceof TerminalNode) && !(keyNode instanceof TinyScriptParser.ConstStringContext)) {
                throw new IllegalArgumentException("invalid extract pair key node, expect naming/string, but found type: " + keyNode.getClass());
            }
            String key = null;
            if (keyNode instanceof TerminalNode) {
                TerminalNode termCtx = (TerminalNode) keyNode;
                key = (String) visitTerminal(termCtx);
            } else {
                TinyScriptParser.ConstStringContext nextCtx = (TinyScriptParser.ConstStringContext) keyNode;
                key = (String) visitConstString(nextCtx);
            }

            if (count == 1) {
                return new AbstractMap.SimpleEntry<>(key, key);
            } else {
                ParseTree separatorNode = ctx.getChild(1);
                ParseTree valueNode = ctx.getChild(2);
                if (!(separatorNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid extract pair separator node, expect ':', but found type: " + separatorNode.getClass());
                }
                if (!(valueNode instanceof TerminalNode) && !(valueNode instanceof TinyScriptParser.ConstStringContext)) {
                    throw new IllegalArgumentException("invalid extract pair value node, expect naming/string node, but found type: " + valueNode.getClass());
                }

                TerminalNode sepCtx = (TerminalNode) separatorNode;
                String sep = (String) visitTerminal(sepCtx);
                if (!":".equals(sep)) {
                    throw new IllegalArgumentException("invalid json pair separator node, expect ':', but found : " + sep);
                }

                String value = null;
                if (valueNode instanceof TerminalNode) {
                    TerminalNode termCtx = (TerminalNode) valueNode;
                    value = (String) visitTerminal(termCtx);
                } else {
                    TinyScriptParser.ConstStringContext nextCtx = (TinyScriptParser.ConstStringContext) valueNode;
                    value = (String) visitConstString(nextCtx);
                }

                return new AbstractMap.SimpleEntry<>(value, value);
            }

        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitNewInstance(TinyScriptParser.NewInstanceContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 2) {
                throw new IllegalArgumentException("missing new instance, expect 2 parts, but found " + count + "!");
            }
            ParseTree newNode = ctx.getChild(0);
            ParseTree invokeNode = ctx.getChild(1);
            if (!(newNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid new instance, expect new node, but found " + newNode.getClass() + "!");
            }
            if (!(invokeNode instanceof TinyScriptParser.InvokeFunctionContext)) {
                throw new IllegalArgumentException("invalid new instance, expect invoke function node, but found " + invokeNode.getClass() + "!");
            }
            TerminalNode newCtx = (TerminalNode) newNode;
            TinyScriptParser.InvokeFunctionContext invokeCtx = (TinyScriptParser.InvokeFunctionContext) invokeNode;
            String newTerm = (String) visitTerminal(newCtx);
            if (!"new".equals(newTerm)) {
                throw new IllegalArgumentException("invalid new instance, expect 'new', but found '" + newTerm + "'!");
            }
            Object ret = visitInvokeFunction(new InvokeFunctionContextImpl(invokeCtx, true));
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx) {
        try {
            debugNode(ctx);
            boolean isNew = false;
            if (ctx instanceof InvokeFunctionContextImpl) {
                InvokeFunctionContextImpl implCtx = (InvokeFunctionContextImpl) ctx;
                isNew = implCtx.isNew;
                ctx = implCtx.target;
            }
            int count = ctx.getChildCount();
            if (count < 1) {
                throw new IllegalArgumentException("missing invoke function!");
            }
            Object ret = null;
            boolean firstCall = true;
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!".".equals(term)) {
                        throw new IllegalArgumentException("invalid invoke function separator, expect '.' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.RefCallContext) {
                    TinyScriptParser.RefCallContext nextCtx = (TinyScriptParser.RefCallContext) item;
                    Object value = visitRefCall(nextCtx);
                    ret = value;
                    firstCall = false;
                } else if (item instanceof TinyScriptParser.FunctionCallContext) {
                    TinyScriptParser.FunctionCallContext nextCtx = (TinyScriptParser.FunctionCallContext) item;
                    if (firstCall) {
                        Object value = visitFunctionCall(new FunctionCallContextImpl(nextCtx, null, isNew));
                        ret = value;
                    } else {
                        Object value = visitFunctionCall(new FunctionCallContextImpl(nextCtx, ret, false));
                        ret = value;
                    }
                    firstCall = false;
                } else {
                    throw new IllegalArgumentException("invalid invoke function node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitFunctionCall(TinyScriptParser.FunctionCallContext ctx) {
        try {
            debugNode(ctx);
            boolean isNew = false;
            Object value = null;
            if (ctx instanceof FunctionCallContextImpl) {
                FunctionCallContextImpl implCtx = (FunctionCallContextImpl) ctx;
                isNew = implCtx.isNew;
                ctx = implCtx.target;
                value = implCtx.value;
            }
            int count = ctx.getChildCount();
            if (count != 3 && count != 4) {
                throw new IllegalArgumentException("missing function call, expect 3/4 parts, but found " + count + " !");
            }
            ParseTree namingNode = ctx.getChild(0);
            ParseTree lParenNode = ctx.getChild(1);
            ParseTree argsNode = null;
            ParseTree rParenNode = null;
            if (count == 3) {
                rParenNode = ctx.getChild(2);
            } else {
                argsNode = ctx.getChild(2);
                rParenNode = ctx.getChild(3);
            }

            TerminalNode namingCtx = (TerminalNode) namingNode;
            TerminalNode lParenCtx = (TerminalNode) lParenNode;
            TinyScriptParser.ArgumentListContext argsCtx = (TinyScriptParser.ArgumentListContext) argsNode;
            TerminalNode rParenCtx = (TerminalNode) rParenNode;

            String naming = (String) visitTerminal(namingCtx);
            String lParen = (String) visitTerminal(lParenCtx);
            String rparen = (String) visitTerminal(rParenCtx);
            if (!"(".equals(lParen)) {
                throw new IllegalArgumentException("invalid function call value, expect '(', but found '" + lParen + "'!");
            }
            if (!")".equals(rparen)) {
                throw new IllegalArgumentException("invalid function call value, expect ')', but found '" + rparen + "'!");
            }
            if (naming == null || naming.isEmpty()) {
                throw new IllegalArgumentException("invalid equal value, expect naming, but found '" + naming + "'!");
            }
            List<Object> args = new ArrayList<>();
            if (argsCtx != null) {
                args = (List<Object>) visitArgumentList(argsCtx);
            }
            if (args == null) {
                args = new ArrayList<>();
            }

            return resolver.resolveFunctionCall(context, value, isNew, naming, args);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitRefCall(TinyScriptParser.RefCallContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 3 && count != 4) {
                throw new IllegalArgumentException("missing ref call, expect 3 parts, but found " + count + " !");
            }
            ParseTree refNode = ctx.getChild(0);
            ParseTree dotNode = ctx.getChild(1);
            ParseTree funcNode = ctx.getChild(2);
            if (!(refNode instanceof TinyScriptParser.RefValueContext)) {
                throw new IllegalArgumentException("invalid ref call, expect ref-value node, but found " + refNode.getClass() + "!");
            }
            if (!(dotNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid ref call, expect dot node, but found " + dotNode.getClass() + "!");
            }
            if (!(funcNode instanceof TinyScriptParser.FunctionCallContext)) {
                throw new IllegalArgumentException("invalid ref call, expect function call node, but found " + funcNode.getClass() + "!");
            }
            TinyScriptParser.RefValueContext refCtx = (TinyScriptParser.RefValueContext) refNode;
            TerminalNode dotCtx = (TerminalNode) dotNode;
            TinyScriptParser.FunctionCallContext funcCtx = (TinyScriptParser.FunctionCallContext) funcNode;
            String dot = (String) visitTerminal(dotCtx);
            if (!".".equals(dot)) {
                throw new IllegalArgumentException("invalid ref call, expect '.', but found '" + dot + "'!");
            }
            Object value = visitRefValue(refCtx);
            Object ret = visitFunctionCall(new FunctionCallContextImpl(funcCtx, value, false));
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitArgumentList(TinyScriptParser.ArgumentListContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            List<Object> ret = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!",".equals(term)) {
                        throw new IllegalArgumentException("invalid argument separator, expect ',' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ArgumentContext) {
                    TinyScriptParser.ArgumentContext nextCtx = (TinyScriptParser.ArgumentContext) item;
                    Object value = visitArgument(nextCtx);
                    ret.add(value);
                } else {
                    throw new IllegalArgumentException("invalid argument list node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitScriptBlock(TinyScriptParser.ScriptBlockContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 3 && count != 2) {
                throw new IllegalArgumentException("missing script block, expect 3/2 parts, but found " + count + "!");
            }
            ParseTree leftNode = ctx.getChild(0);
            if (count == 2) {
                ParseTree rightNode = ctx.getChild(1);
                if (!(leftNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid script block left node, expect '{', but found type: " + leftNode.getClass());
                }
                if (!(rightNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid script block right node, expect '}', but found type: " + rightNode.getClass());
                }
                TerminalNode leftCtx = (TerminalNode) leftNode;
                TerminalNode rightCtx = (TerminalNode) rightNode;
                String left = (String) visitTerminal(leftCtx);
                String right = (String) visitTerminal(rightCtx);
                if (!"{".equals(left)) {
                    throw new IllegalArgumentException("invalid script block left node, expect '(', but found : " + left);
                }
                if (!"}".equals(right)) {
                    throw new IllegalArgumentException("invalid script block right node, expect '(', but found : " + right);
                }
                return null;
            } else {
                ParseTree segmentsNode = ctx.getChild(1);
                ParseTree rightNode = ctx.getChild(2);
                if (!(leftNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid script block left node, expect '{', but found type: " + leftNode.getClass());
                }
                if (!(segmentsNode instanceof TinyScriptParser.SegmentsContext)) {
                    throw new IllegalArgumentException("invalid script block right node, expect segments, but found type: " + segmentsNode.getClass());
                }
                if (!(rightNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid script block right node, expect '}', but found type: " + rightNode.getClass());
                }
                TerminalNode leftCtx = (TerminalNode) leftNode;
                TinyScriptParser.SegmentsContext segmentsCtx = (TinyScriptParser.SegmentsContext) segmentsNode;
                TerminalNode rightCtx = (TerminalNode) rightNode;
                String left = (String) visitTerminal(leftCtx);
                String right = (String) visitTerminal(rightCtx);
                if (!"{".equals(left)) {
                    throw new IllegalArgumentException("invalid script block left node, expect '(', but found : " + left);
                }
                if (!"}".equals(right)) {
                    throw new IllegalArgumentException("invalid script block right node, expect '(', but found : " + right);
                }
                Object value = visitSegments(segmentsCtx);
                return value;
            }
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitArgument(TinyScriptParser.ArgumentContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing argument!");
            }
            if (count == 3) {
                ParseTree namingNode = ctx.getChild(0);
                ParseTree sepNode = ctx.getChild(1);
                ParseTree valueNode = ctx.getChild(2);
                if (!(namingNode instanceof TerminalNode)
                        && !(namingNode instanceof TinyScriptParser.ConstStringContext)) {
                    throw new IllegalArgumentException("invalid argument, expect naming/string node, but found " + namingNode.getClass() + "!");
                }
                if (!(sepNode instanceof TerminalNode)) {
                    throw new IllegalArgumentException("invalid argument, expect separator ':' node, but found " + sepNode.getClass() + "!");
                }
                if (!(valueNode instanceof TinyScriptParser.ArgumentValueContext)) {
                    throw new IllegalArgumentException("invalid argument, expect argument-value node, but found " + valueNode.getClass() + "!");
                }
                String naming = null;
                if (namingNode instanceof TerminalNode) {
                    TerminalNode namingCtx = (TerminalNode) namingNode;
                    naming = (String) visitTerminal(namingCtx);
                } else if (namingNode instanceof TinyScriptParser.ConstStringContext) {
                    TinyScriptParser.ConstStringContext namingCtx = (TinyScriptParser.ConstStringContext) namingNode;
                    naming = (String) visitConstString(namingCtx);
                }
                if (naming == null || naming.isEmpty()) {
                    throw new IllegalArgumentException("invalid argument naming, expect naming, but found '" + naming + "'!");
                }
                TerminalNode sepCtx = (TerminalNode) sepNode;
                TinyScriptParser.ArgumentValueContext valueCtx = (TinyScriptParser.ArgumentValueContext) valueNode;
                String sep = (String) visitTerminal(sepCtx);
                if (!":".equals(sep)) {
                    throw new IllegalArgumentException("invalid argument, expect separator ':' node, but found '" + sep + "'!");
                }
                Object value = visitArgumentValue(valueCtx);
                return new NamingBindArgument(naming, value);
            } else if (count == 1) {
                ParseTree argNode = ctx.getChild(0);
                if (!(argNode instanceof TinyScriptParser.ArgumentValueContext)) {
                    throw new IllegalArgumentException("invalid argument, expect argument-value node, but found " + argNode.getClass() + "!");
                }
                TinyScriptParser.ArgumentValueContext nextCtx = (TinyScriptParser.ArgumentValueContext) argNode;
                return visitArgumentValue(nextCtx);
            }
            throw new IllegalArgumentException("un-support argument found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitArgumentValue(TinyScriptParser.ArgumentValueContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing argument value!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TinyScriptParser.ExpressContext) {
                TinyScriptParser.ExpressContext nextCtx = (TinyScriptParser.ExpressContext) item;
                return visitExpress(nextCtx);
            }
            throw new IllegalArgumentException("un-support argument value found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public Map.Entry<Class<?>, String> getStaticEnumValueParts(TinyScriptParser.StaticEnumValueContext ctx) {
        int count = ctx.getChildCount();
        if (count <= 0) {
            throw new IllegalArgumentException("missing static/enum value!");
        }
        String className = null;
        String valueName = null;
        if (count == 2) {
            ParseTree termNode = ctx.getChild(0);
            ParseTree namingNode = ctx.getChild(1);
            TerminalNode termCtx = (TerminalNode) termNode;
            TerminalNode namingCtx = (TerminalNode) namingNode;
            String term = (String) visitTerminal(termCtx);
            String naming = (String) visitTerminal(namingCtx);
            if (!"@".equals(term)) {
                throw new IllegalArgumentException("invalid grammar term, expect '@' but found '" + term + "'!");
            }
            int idx = naming.lastIndexOf(".");
            if (idx <= 0) {
                throw new IllegalArgumentException("invalid grammar fullNaming, expect 'ClassName.ValueName' but found '" + naming + "'!");
            }
            className = naming.substring(0, idx);
            valueName = naming.substring(idx + 1);
        } else if (count == 3) {
            ParseTree classNameNode = ctx.getChild(0);
            ParseTree termNode = ctx.getChild(1);
            ParseTree namingNode = ctx.getChild(2);
            TerminalNode classNameCtx = (TerminalNode) classNameNode;
            TerminalNode termCtx = (TerminalNode) termNode;
            TerminalNode namingCtx = (TerminalNode) namingNode;
            String cls = (String) visitTerminal(classNameCtx);
            String term = (String) visitTerminal(termCtx);
            String naming = (String) visitTerminal(namingCtx);
            if (!"@".equals(term)) {
                throw new IllegalArgumentException("invalid grammar term, expect '@' but found '" + term + "'!");
            }
            if (naming.contains(".")) {
                throw new IllegalArgumentException("invalid grammar valueName, expect 'ID' not contains '.' but found '" + naming + "'!");
            }
            className = cls;
            valueName = naming;
        } else {
            throw new IllegalArgumentException("un-support static/enum value, expect 2/3 child but found : " + ctx.getText());
        }
        if (className == null || className.isEmpty()
                || valueName == null || valueName.isEmpty()) {
            throw new IllegalArgumentException("un-support static/enum value, expect className.valueName child but found : " + className + "." + valueName);
        }
        Class<?> clazz = resolver.loadClass(context, className);
        return new AbstractMap.SimpleEntry<>(clazz, valueName);
    }

    public Map.Entry<Class<?>, Object> getStaticEnumValue(TinyScriptParser.StaticEnumValueContext ctx) {
        Map.Entry<Class<?>, String> entry = getStaticEnumValueParts(ctx);
        Class<?> clazz = entry.getKey();
        String valueName = entry.getValue();
        if (clazz.isEnum()) {
            Object[] enums = clazz.getEnumConstants();
            for (Object item : enums) {
                Enum em = (Enum) item;
                if (em.name().equals(valueName)) {
                    return new AbstractMap.SimpleEntry<>(clazz, em);
                }
                if (String.valueOf(em).equals(valueName)) {
                    return new AbstractMap.SimpleEntry<>(clazz, em);
                }
            }
            throw new IllegalArgumentException("not found enum value : " + valueName + " in " + clazz);
        } else {
            Field field = ReflectResolver.getField(clazz, valueName);
            if (field == null) {
                throw new IllegalArgumentException("not found static field : " + valueName + " in " + clazz);
            }
            return new AbstractMap.SimpleEntry<>(clazz, field);
        }
    }

    @Override
    public Object visitStaticEnumValue(TinyScriptParser.StaticEnumValueContext ctx) {
        try {
            debugNode(ctx);
            Map.Entry<Class<?>, Object> entry = getStaticEnumValue(ctx);
            Object value = entry.getValue();
            if (value instanceof Field) {
                Field field = (Field) value;
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    throw new IllegalArgumentException("un-support static field : " + field);
                }
                field.setAccessible(true);
                return field.get(null);
            } else {
                return value;
            }
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstValue(TinyScriptParser.ConstValueContext ctx) {
        try {
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const value!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TinyScriptParser.ConstBoolContext) {
                TinyScriptParser.ConstBoolContext nextCtx = (TinyScriptParser.ConstBoolContext) item;
                return visitConstBool(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstClassContext) {
                TinyScriptParser.ConstClassContext nextCtx = (TinyScriptParser.ConstClassContext) item;
                return visitConstClass(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstNullContext) {
                TinyScriptParser.ConstNullContext nextCtx = (TinyScriptParser.ConstNullContext) item;
                return visitConstNull(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstStringContext) {
                TinyScriptParser.ConstStringContext nextCtx = (TinyScriptParser.ConstStringContext) item;
                return visitConstString(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstMultilineStringContext) {
                TinyScriptParser.ConstMultilineStringContext nextCtx = (TinyScriptParser.ConstMultilineStringContext) item;
                return visitConstMultilineString(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstRenderStringContext) {
                TinyScriptParser.ConstRenderStringContext nextCtx = (TinyScriptParser.ConstRenderStringContext) item;
                return visitConstRenderString(nextCtx);
            } else if (item instanceof TinyScriptParser.DecNumberContext) {
                TinyScriptParser.DecNumberContext nextCtx = (TinyScriptParser.DecNumberContext) item;
                return visitDecNumber(nextCtx);
            } else if (item instanceof TinyScriptParser.HexNumberContext) {
                TinyScriptParser.HexNumberContext nextCtx = (TinyScriptParser.HexNumberContext) item;
                return visitHexNumber(nextCtx);
            } else if (item instanceof TinyScriptParser.OtcNumberContext) {
                TinyScriptParser.OtcNumberContext nextCtx = (TinyScriptParser.OtcNumberContext) item;
                return visitOtcNumber(nextCtx);
            } else if (item instanceof TinyScriptParser.BinNumberContext) {
                TinyScriptParser.BinNumberContext nextCtx = (TinyScriptParser.BinNumberContext) item;
                return visitBinNumber(nextCtx);
            }
            throw new IllegalArgumentException("un-support const value found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitRefValue(TinyScriptParser.RefValueContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing ref value!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing ref value!");
                }
                term = term.trim();
                char modifier = term.charAt(1);
                if (modifier == '{') {
                    term = term.substring("${".length(), term.length() - "}".length());
                } else {
                    term = term.substring("$!{".length(), term.length() - "}".length());
                }
                term = term.trim();
                Object value = resolver.getValue(context, term);
                if (modifier == '!') {
                    if (value == null) {
                        value = "";
                    }
                }
                return value;
            }
            throw new IllegalArgumentException("un-support ref value found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstClass(TinyScriptParser.ConstClassContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const class!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const class!");
                }
                term = term.trim();
                if (term.endsWith(".class")) {
                    term = term.substring(0, term.length() - ".class".length());
                }
                return resolver.loadClass(context, term);
            }
            throw new IllegalArgumentException("un-support const class found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }


    @Override
    public Object visitConstBool(TinyScriptParser.ConstBoolContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const boolean!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const boolean!");
                }
                term = term.trim();
                if ("true".equals(term)) {
                    return true;
                } else if ("false".equals(term)) {
                    return false;
                }
                throw new IllegalArgumentException("bad const boolean value: " + term + "!");
            }
            throw new IllegalArgumentException("un-support const boolean found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstNull(TinyScriptParser.ConstNullContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const null!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const null!");
                }
                term = term.trim();
                if ("null".equals(term)) {
                    return null;
                }
                throw new IllegalArgumentException("bad const null value: " + term + "!");
            }
            throw new IllegalArgumentException("un-support const null found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstString(TinyScriptParser.ConstStringContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const string!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const string!");
                }
                term = term.trim();
                term = term.substring("\"".length(), term.length() - "\"".length());
                term = unescapeString(term);
                return term;
            }
            throw new IllegalArgumentException("un-support const string found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstMultilineString(TinyScriptParser.ConstMultilineStringContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const multiline string!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const multiline string!");
                }
                term = term.trim();
                StringBuilder builder = new StringBuilder();
                String firstLine = "";
                String firstChar = term.substring(0, 1);
                String escapeFirstCharRegex = "\\" + firstChar;
                String[] arr = term.split("\n");
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    str = str.replace(escapeFirstCharRegex, firstChar);
                    str = str.replace("\\\\", "\\");
                    if (i == 0) {
                        firstLine = arr[0];
                        continue;
                    }
                    if (i == arr.length - 1) {
                        continue;
                    }
                    builder.append(str).append("\n");
                }
                firstLine = firstLine.trim();
                firstLine = firstLine.substring("```".length());
                String[] features = firstLine.split("\\.");
                List<String> featuresList = new ArrayList<>();
                for (String feature : features) {
                    if (feature.isEmpty()) {
                        continue;
                    }
                    featuresList.add(feature);
                }
                term = resolver.multilineString(context, builder.toString(), featuresList);
                return term;
            }
            throw new IllegalArgumentException("un-support const multiline string found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitConstRenderString(TinyScriptParser.ConstRenderStringContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing const render string!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing const render string!");
                }
                term = term.trim();
                term = term.substring("r\"".length(), term.length() - "\"".length());
                term = unescapeString(term);
                term = resolver.renderString(context, term);
                return term;
            }
            throw new IllegalArgumentException("un-support const render string found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitDecNumber(TinyScriptParser.DecNumberContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing dec number!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing dec number!");
                }
                term = term.trim();
                String cmp = term.toUpperCase();
                char lastChar = cmp.charAt(cmp.length() - 1);
                if (lastChar == 'F' || lastChar == 'L') {
                    term = term.substring(0, term.length() - 1);
                }
                boolean isFloat = term.contains(".");
                BigDecimal num = new BigDecimal(term);
                if (lastChar == 'F') {
                    return num.floatValue();
                }
                if (lastChar == 'L') {
                    return num.longValue();
                }
                if (isFloat) {
                    return num.doubleValue();
                }
                return num.intValue();
            }
            throw new IllegalArgumentException("un-support dec number found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitHexNumber(TinyScriptParser.HexNumberContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing hex number!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing hex number!");
                }
                term = term.trim();
                term = term.substring("0x".length());
                String cmp = term.toUpperCase();
                char lastChar = cmp.charAt(cmp.length() - 1);
                if (lastChar == 'L') {
                    term = term.substring(0, term.length() - 1);
                }
                if (lastChar == 'L') {
                    return Long.parseLong(term, 16);
                }
                return Integer.parseInt(term, 16);
            }
            throw new IllegalArgumentException("un-support hex number found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitOtcNumber(TinyScriptParser.OtcNumberContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing otc number!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing otc number!");
                }
                term = term.trim();
                term = term.substring("0t".length());
                String cmp = term.toUpperCase();
                char lastChar = cmp.charAt(cmp.length() - 1);
                if (lastChar == 'L') {
                    term = term.substring(0, term.length() - 1);
                }
                if (lastChar == 'L') {
                    return Long.parseLong(term, 8);
                }
                return Integer.parseInt(term, 8);
            }
            throw new IllegalArgumentException("un-support otc number found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitBinNumber(TinyScriptParser.BinNumberContext ctx) {
        try {
//            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing bin number!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (term == null) {
                    throw new IllegalArgumentException("missing bin number!");
                }
                term = term.trim();
                term = term.substring("0b".length());
                String cmp = term.toUpperCase();
                char lastChar = cmp.charAt(cmp.length() - 1);
                if (lastChar == 'L') {
                    term = term.substring(0, term.length() - 1);
                }
                if (lastChar == 'L') {
                    return Long.parseLong(term, 2);
                }
                return Integer.parseInt(term, 2);
            }
            throw new IllegalArgumentException("un-support bin number found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitJsonValue(TinyScriptParser.JsonValueContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count <= 0) {
                throw new IllegalArgumentException("missing json value!");
            }
            ParseTree item = ctx.getChild(0);
            if (item instanceof TinyScriptParser.InvokeFunctionContext) {
                TinyScriptParser.InvokeFunctionContext nextCtx = (TinyScriptParser.InvokeFunctionContext) item;
                return visitInvokeFunction(nextCtx);
            } else if (item instanceof TinyScriptParser.ConstValueContext) {
                TinyScriptParser.ConstValueContext nextCtx = (TinyScriptParser.ConstValueContext) item;
                return visitConstValue(nextCtx);
            } else if (item instanceof TinyScriptParser.RefValueContext) {
                TinyScriptParser.RefValueContext nextCtx = (TinyScriptParser.RefValueContext) item;
                return visitRefValue(nextCtx);
            } else if (item instanceof TinyScriptParser.JsonArrayValueContext) {
                TinyScriptParser.JsonArrayValueContext nextCtx = (TinyScriptParser.JsonArrayValueContext) item;
                return visitJsonArrayValue(nextCtx);
            } else if (item instanceof TinyScriptParser.JsonMapValueContext) {
                TinyScriptParser.JsonMapValueContext nextCtx = (TinyScriptParser.JsonMapValueContext) item;
                return visitJsonMapValue(nextCtx);
            }
            throw new IllegalArgumentException("un-support json value found : " + ctx.getText());
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitJsonMapValue(TinyScriptParser.JsonMapValueContext ctx) {
        try {
            debugNode(ctx);
            Map<String, Object> ret = new LinkedHashMap<>();
            int count = ctx.getChildCount();
            if (count != 2 && count != 3) {
                throw new IllegalArgumentException("missing json map value!");
            }
            ParseTree leftNode = ctx.getChild(0);
            ParseTree pairsNode = null;
            ParseTree rightNode = null;
            if (count == 2) {
                rightNode = ctx.getChild(1);
            } else {
                pairsNode = ctx.getChild(1);
                rightNode = ctx.getChild(2);
            }
            if (!(leftNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid json map value left node, expect '{', but found type: " + leftNode.getClass());
            }
            if (!(rightNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid json map value right node, expect '}', but found type: " + rightNode.getClass());
            }
            if (pairsNode != null) {
                if (!(pairsNode instanceof TinyScriptParser.JsonPairsContext)) {
                    throw new IllegalArgumentException("invalid json map value middle node, expect json pairs node, but found type: " + pairsNode.getClass());
                }
            }
            TerminalNode leftCtx = (TerminalNode) leftNode;
            TinyScriptParser.JsonPairsContext pairsCtx = null;
            TerminalNode rightCtx = (TerminalNode) rightNode;
            if (pairsNode != null) {
                pairsCtx = (TinyScriptParser.JsonPairsContext) pairsNode;
            }
            String left = (String) visitTerminal(leftCtx);
            String right = (String) visitTerminal(rightCtx);
            if (!"{".equals(left)) {
                throw new IllegalArgumentException("invalid json map value left node, expect '{', but found : " + left);
            }
            if (!"}".equals(right)) {
                throw new IllegalArgumentException("invalid json map value right right node, expect '}', but found : " + right);
            }
            if (pairsCtx != null) {
                return visitJsonPairs(new JsonPairsContextImpl(pairsCtx, ret));
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static class JsonPairsContextImpl extends TinyScriptParser.JsonPairsContext {
        public TinyScriptParser.JsonPairsContext target;
        public Object value;

        public JsonPairsContextImpl(TinyScriptParser.JsonPairsContext target, Object value) {
            super(null, 0);
            this.target = target;
            this.value = value;
        }
    }

    @Override
    public Object visitJsonPairs(TinyScriptParser.JsonPairsContext ctx) {
        try {
            debugNode(ctx);
            Map<String, Object> ret = new LinkedHashMap<>();
            if (ctx instanceof JsonPairsContextImpl) {
                JsonPairsContextImpl impl = (JsonPairsContextImpl) ctx;
                ctx = impl.target;
                ret = (Map<String, Object>) impl.value;
            }
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!",".equals(term)) {
                        throw new IllegalArgumentException("invalid json pairs separator, expect ',' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.JsonPairContext) {
                    TinyScriptParser.JsonPairContext nextCtx = (TinyScriptParser.JsonPairContext) item;
                    Map.Entry<String, Object> value = (Map.Entry<String, Object>) visitJsonPair(nextCtx);
                    ret.put(value.getKey(), value.getValue());
                } else {
                    throw new IllegalArgumentException("invalid json pairs node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitJsonPair(TinyScriptParser.JsonPairContext ctx) {
        try {
            debugNode(ctx);
            int count = ctx.getChildCount();
            if (count != 3) {
                throw new IllegalArgumentException("missing json pair!");
            }
            ParseTree keyNode = ctx.getChild(0);
            ParseTree separatorNode = ctx.getChild(1);
            ParseTree valueNode = ctx.getChild(2);
            if (!(keyNode instanceof TerminalNode) && !(keyNode instanceof TinyScriptParser.ConstStringContext)) {
                throw new IllegalArgumentException("invalid json pair key node, expect naming/string, but found type: " + keyNode.getClass());
            }
            if (!(separatorNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid json pair separator node, expect ':', but found type: " + separatorNode.getClass());
            }
            if (!(valueNode instanceof TinyScriptParser.ExpressContext)) {
                throw new IllegalArgumentException("invalid json pair value node, expect express node, but found type: " + valueNode.getClass());
            }

            TerminalNode sepCtx = (TerminalNode) separatorNode;
            String sep = (String) visitTerminal(sepCtx);
            if (!":".equals(sep)) {
                throw new IllegalArgumentException("invalid json pair separator node, expect ':', but found : " + sep);
            }

            String key = null;
            if (keyNode instanceof TerminalNode) {
                TerminalNode termCtx = (TerminalNode) keyNode;
                key = (String) visitTerminal(termCtx);
            } else {
                TinyScriptParser.ConstStringContext nextCtx = (TinyScriptParser.ConstStringContext) keyNode;
                key = (String) visitConstString(nextCtx);
            }

            TinyScriptParser.ExpressContext valueCtx = (TinyScriptParser.ExpressContext) valueNode;
            Object value = visitExpress(valueCtx);

            return new AbstractMap.SimpleEntry<>(key, value);
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitJsonArrayValue(TinyScriptParser.JsonArrayValueContext ctx) {
        try {
            debugNode(ctx);
            List<Object> ret = new ArrayList<>();
            int count = ctx.getChildCount();
            if (count != 2 && count != 3) {
                throw new IllegalArgumentException("missing json array value!");
            }
            ParseTree leftNode = ctx.getChild(0);
            ParseTree itemsNode = null;
            ParseTree rightNode = null;
            if (count == 2) {
                rightNode = ctx.getChild(1);
            } else {
                itemsNode = ctx.getChild(1);
                rightNode = ctx.getChild(2);
            }
            if (!(leftNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid json array value left node, expect '[', but found type: " + leftNode.getClass());
            }
            if (!(rightNode instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid json array value right node, expect ']', but found type: " + rightNode.getClass());
            }
            if (itemsNode != null) {
                if (!(itemsNode instanceof TinyScriptParser.JsonItemListContext)) {
                    throw new IllegalArgumentException("invalid json array value middle node, expect json pairs node, but found type: " + itemsNode.getClass());
                }
            }
            TerminalNode leftCtx = (TerminalNode) leftNode;
            TinyScriptParser.JsonItemListContext itemsCtx = null;
            TerminalNode rightCtx = (TerminalNode) rightNode;
            if (itemsNode != null) {
                itemsCtx = (TinyScriptParser.JsonItemListContext) itemsNode;
            }
            String left = (String) visitTerminal(leftCtx);
            String right = (String) visitTerminal(rightCtx);
            if (!"[".equals(left)) {
                throw new IllegalArgumentException("invalid json array value left node, expect '[', but found : " + left);
            }
            if (!"]".equals(right)) {
                throw new IllegalArgumentException("invalid json array value right right node, expect ']', but found : " + right);
            }
            if (itemsCtx != null) {
                return visitJsonItemList(new JsonItemListContextImpl(itemsCtx, ret));
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static class JsonItemListContextImpl extends TinyScriptParser.JsonItemListContext {
        public TinyScriptParser.JsonItemListContext target;
        public Object value;

        public JsonItemListContextImpl(TinyScriptParser.JsonItemListContext target, Object value) {
            super(null, 0);
            this.target = target;
            this.value = value;
        }
    }

    @Override
    public Object visitJsonItemList(TinyScriptParser.JsonItemListContext ctx) {
        try {
            debugNode(ctx);
            List<Object> ret = new ArrayList<>();
            if (ctx instanceof JsonItemListContextImpl) {
                JsonItemListContextImpl impl = (JsonItemListContextImpl) ctx;
                ctx = impl.target;
                ret = (List<Object>) impl.value;
            }
            int count = ctx.getChildCount();
            for (int i = 0; i < count; i++) {
                ParseTree item = ctx.getChild(i);
                if (item instanceof TerminalNode) {
                    TerminalNode nextTerm = (TerminalNode) item;
                    String term = (String) visitTerminal(nextTerm);
                    if (!",".equals(term)) {
                        throw new IllegalArgumentException("invalid json item list separator, expect ',' but found '" + term + "'!");
                    }
                } else if (item instanceof TinyScriptParser.ExpressContext) {
                    TinyScriptParser.ExpressContext nextCtx = (TinyScriptParser.ExpressContext) item;
                    Object value = visitExpress(nextCtx);
                    ret.add(value);
                } else {
                    throw new IllegalArgumentException("invalid json item list node type: " + item.getClass());
                }
            }
            return ret;
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", ctx, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    public static class NamingBindArgument {
        public String naming;
        public Object value;

        public NamingBindArgument() {

        }

        public NamingBindArgument(String naming, Object value) {
            this.naming = naming;
            this.value = value;
        }
    }


    @Override
    public Object visit(ParseTree tree) {
        try {
            debugNode(tree);
            if (tree instanceof TinyScriptParser.ScriptContext) {
                TinyScriptParser.ScriptContext nextCtx = (TinyScriptParser.ScriptContext) tree;
                return visitScript(nextCtx);
            } else if (tree instanceof TinyScriptParser.ExpressContext) {
                TinyScriptParser.ExpressContext nextCtx = (TinyScriptParser.ExpressContext) tree;
                return visitExpress(nextCtx);
            } else if (tree instanceof TinyScriptParser.EqualValueContext) {
                TinyScriptParser.EqualValueContext nextCtx = (TinyScriptParser.EqualValueContext) tree;
                return visitEqualValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.NewInstanceContext) {
                TinyScriptParser.NewInstanceContext nextCtx = (TinyScriptParser.NewInstanceContext) tree;
                return visitNewInstance(nextCtx);
            } else if (tree instanceof TinyScriptParser.InvokeFunctionContext) {
                TinyScriptParser.InvokeFunctionContext nextCtx = (TinyScriptParser.InvokeFunctionContext) tree;
                return visitInvokeFunction(nextCtx);
            } else if (tree instanceof TinyScriptParser.ConstValueContext) {
                TinyScriptParser.ConstValueContext nextCtx = (TinyScriptParser.ConstValueContext) tree;
                return visitConstValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.RefValueContext) {
                TinyScriptParser.RefValueContext nextCtx = (TinyScriptParser.RefValueContext) tree;
                return visitRefValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.RefCallContext) {
                TinyScriptParser.RefCallContext nextCtx = (TinyScriptParser.RefCallContext) tree;
                return visitRefCall(nextCtx);
            } else if (tree instanceof TinyScriptParser.ArgumentListContext) {
                TinyScriptParser.ArgumentListContext nextCtx = (TinyScriptParser.ArgumentListContext) tree;
                return visitArgumentList(nextCtx);
            } else if (tree instanceof TinyScriptParser.FunctionCallContext) {
                TinyScriptParser.FunctionCallContext nextCtx = (TinyScriptParser.FunctionCallContext) tree;
                return visitFunctionCall(nextCtx);
            } else if (tree instanceof TinyScriptParser.ArgumentContext) {
                TinyScriptParser.ArgumentContext nextCtx = (TinyScriptParser.ArgumentContext) tree;
                return visitArgument(nextCtx);
            } else if (tree instanceof TinyScriptParser.DecNumberContext) {
                TinyScriptParser.DecNumberContext nextCtx = (TinyScriptParser.DecNumberContext) tree;
                return visitDecNumber(nextCtx);
            } else if (tree instanceof TinyScriptParser.HexNumberContext) {
                TinyScriptParser.HexNumberContext nextCtx = (TinyScriptParser.HexNumberContext) tree;
                return visitHexNumber(nextCtx);
            } else if (tree instanceof TinyScriptParser.OtcNumberContext) {
                TinyScriptParser.OtcNumberContext nextCtx = (TinyScriptParser.OtcNumberContext) tree;
                return visitOtcNumber(nextCtx);
            } else if (tree instanceof TinyScriptParser.BinNumberContext) {
                TinyScriptParser.BinNumberContext nextCtx = (TinyScriptParser.BinNumberContext) tree;
                return visitBinNumber(nextCtx);
            } else if (tree instanceof TinyScriptParser.ConstNullContext) {
                TinyScriptParser.ConstNullContext nextCtx = (TinyScriptParser.ConstNullContext) tree;
                return visitConstNull(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonValueContext) {
                TinyScriptParser.JsonValueContext nextCtx = (TinyScriptParser.JsonValueContext) tree;
                return visitJsonValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonArrayValueContext) {
                TinyScriptParser.JsonArrayValueContext nextCtx = (TinyScriptParser.JsonArrayValueContext) tree;
                return visitJsonArrayValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonMapValueContext) {
                TinyScriptParser.JsonMapValueContext nextCtx = (TinyScriptParser.JsonMapValueContext) tree;
                return visitJsonMapValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonPairsContext) {
                TinyScriptParser.JsonPairsContext nextCtx = (TinyScriptParser.JsonPairsContext) tree;
                return visitJsonPairs(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonPairContext) {
                TinyScriptParser.JsonPairContext nextCtx = (TinyScriptParser.JsonPairContext) tree;
                return visitJsonPair(nextCtx);
            } else if (tree instanceof TinyScriptParser.JsonItemListContext) {
                TinyScriptParser.JsonItemListContext nextCtx = (TinyScriptParser.JsonItemListContext) tree;
                return visitJsonItemList(nextCtx);
            } else if (tree instanceof TinyScriptParser.IfSegmentContext) {
                TinyScriptParser.IfSegmentContext nextCtx = (TinyScriptParser.IfSegmentContext) tree;
                return visitIfSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ConditionBlockContext) {
                TinyScriptParser.ConditionBlockContext nextCtx = (TinyScriptParser.ConditionBlockContext) tree;
                return visitConditionBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.ScriptBlockContext) {
                TinyScriptParser.ScriptBlockContext nextCtx = (TinyScriptParser.ScriptBlockContext) tree;
                return visitScriptBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.NamingBlockContext) {
                TinyScriptParser.NamingBlockContext nextCtx = (TinyScriptParser.NamingBlockContext) tree;
                return visitNamingBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.ForeachSegmentContext) {
                TinyScriptParser.ForeachSegmentContext nextCtx = (TinyScriptParser.ForeachSegmentContext) tree;
                return visitForeachSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ForSegmentContext) {
                TinyScriptParser.ForSegmentContext nextCtx = (TinyScriptParser.ForSegmentContext) tree;
                return visitForSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.WhileSegmentContext) {
                TinyScriptParser.WhileSegmentContext nextCtx = (TinyScriptParser.WhileSegmentContext) tree;
                return visitWhileSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ControlSegmentContext) {
                TinyScriptParser.ControlSegmentContext nextCtx = (TinyScriptParser.ControlSegmentContext) tree;
                return visitControlSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ParenSegmentContext) {
                TinyScriptParser.ParenSegmentContext nextCtx = (TinyScriptParser.ParenSegmentContext) tree;
                return visitParenSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.TrySegmentContext) {
                TinyScriptParser.TrySegmentContext nextCtx = (TinyScriptParser.TrySegmentContext) tree;
                return visitTrySegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.TryBodyBlockContext) {
                TinyScriptParser.TryBodyBlockContext nextCtx = (TinyScriptParser.TryBodyBlockContext) tree;
                return visitTryBodyBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.CatchBodyBlockContext) {
                TinyScriptParser.CatchBodyBlockContext nextCtx = (TinyScriptParser.CatchBodyBlockContext) tree;
                return visitCatchBodyBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.FinallyBodyBlockContext) {
                TinyScriptParser.FinallyBodyBlockContext nextCtx = (TinyScriptParser.FinallyBodyBlockContext) tree;
                return visitFinallyBodyBlock(nextCtx);
            } else if (tree instanceof TinyScriptParser.DebuggerSegmentContext) {
                TinyScriptParser.DebuggerSegmentContext nextCtx = (TinyScriptParser.DebuggerSegmentContext) tree;
                return visitDebuggerSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ConstClassContext) {
                TinyScriptParser.ConstClassContext nextCtx = (TinyScriptParser.ConstClassContext) tree;
                return visitConstClass(nextCtx);
            } else if (tree instanceof TinyScriptParser.ThrowSegmentContext) {
                TinyScriptParser.ThrowSegmentContext nextCtx = (TinyScriptParser.ThrowSegmentContext) tree;
                return visitThrowSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.NegtiveSegmentContext) {
                TinyScriptParser.NegtiveSegmentContext nextCtx = (TinyScriptParser.NegtiveSegmentContext) tree;
                return visitNegtiveSegment(nextCtx);
            } else if (tree instanceof TinyScriptParser.ExtractExpressContext) {
                TinyScriptParser.ExtractExpressContext nextCtx = (TinyScriptParser.ExtractExpressContext) tree;
                return visitExtractExpress(nextCtx);
            } else if (tree instanceof TinyScriptParser.ExtractPairsContext) {
                TinyScriptParser.ExtractPairsContext nextCtx = (TinyScriptParser.ExtractPairsContext) tree;
                return visitExtractPairs(nextCtx);
            } else if (tree instanceof TinyScriptParser.ExtractPairContext) {
                TinyScriptParser.ExtractPairContext nextCtx = (TinyScriptParser.ExtractPairContext) tree;
                return visitExtractPair(nextCtx);
            } else if (tree instanceof TinyScriptParser.StaticEnumValueContext) {
                TinyScriptParser.StaticEnumValueContext nextCtx = (TinyScriptParser.StaticEnumValueContext) tree;
                return visitStaticEnumValue(nextCtx);
            } else if (tree instanceof TinyScriptParser.SegmentsContext) {
                TinyScriptParser.SegmentsContext nextCtx = (TinyScriptParser.SegmentsContext) tree;
                return visitSegments(nextCtx);
            }
        } catch (TinyScriptBreakException e) {

        } catch (TinyScriptContinueException e) {

        } catch (TinyScriptReturnException e) {
            if (e.isHasRetValue()) {
                return e.getRetValue();
            } else {
                return null;
            }
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", tree, " ") + "cause by: " + e.getMessage(), e);
        }
        throw new IllegalArgumentException("un-support visit type: " + tree.getClass().getSimpleName());
    }


    @Override
    public Object visitChildren(RuleNode node) {
//        debugNode(node);
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        try {
//            debugNode(node);
            return node.getText();
        } catch (Throwable e) {
            if (e instanceof TinyScriptException) {
                throw (TinyScriptException) e;
            }
            throw new TinyScriptEvaluateException(getTreeLocationText("location ", node, " ") + "cause by: " + e.getMessage(), e);
        }
    }

    @Override
    public Object visitErrorNode(ErrorNode node) {
//        debugNode(node);
        return null;
    }

    public static class InvokeFunctionContextImpl extends TinyScriptParser.InvokeFunctionContext {
        public TinyScriptParser.InvokeFunctionContext target;
        public boolean isNew;

        public InvokeFunctionContextImpl(TinyScriptParser.InvokeFunctionContext target, boolean isNew) {
            super(null, 0);
            this.target = target;
            this.isNew = isNew;
        }
    }

    public static class FunctionCallContextImpl extends TinyScriptParser.FunctionCallContext {
        public TinyScriptParser.FunctionCallContext target;
        public Object value;
        public boolean isNew;

        public FunctionCallContextImpl(TinyScriptParser.FunctionCallContext target, Object value, boolean isNew) {
            super(null, 0);
            this.target = target;
            this.value = value;
            this.isNew = isNew;
        }
    }
}
