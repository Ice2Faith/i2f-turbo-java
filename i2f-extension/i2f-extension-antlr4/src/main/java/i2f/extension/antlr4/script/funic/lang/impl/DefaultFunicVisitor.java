package i2f.extension.antlr4.script.funic.lang.impl;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.grammar.FunicVisitor;
import i2f.extension.antlr4.script.funic.lang.exception.FunicControlException;
import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;
import i2f.extension.antlr4.script.funic.lang.exception.control.FunicBreakException;
import i2f.extension.antlr4.script.funic.lang.exception.control.FunicContinueException;
import i2f.extension.antlr4.script.funic.lang.exception.control.FunicReturnException;
import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicEvaluateException;
import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicThrowDataException;
import i2f.extension.antlr4.script.funic.lang.lambda.FunicLambda;
import i2f.extension.antlr4.script.funic.lang.method.FunicMethod;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
import i2f.extension.antlr4.script.funic.lang.resolver.impl.DefaultFunicResolver;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.extension.antlr4.script.funic.lang.value.impl.*;
import i2f.invokable.method.IMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/22 9:01
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DefaultFunicVisitor implements FunicVisitor<FunicValue> {
    public static final ExecutorService DEFAULT_GO_POOL = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() * 2);
    protected Object context;
    protected FunicResolver resolver = new DefaultFunicResolver();
    protected CopyOnWriteArrayList<String> importPackages = new CopyOnWriteArrayList<>();
    protected ConcurrentHashMap<String, CopyOnWriteArrayList<IMethod>> registryMethods = new ConcurrentHashMap<>();
    protected ExecutorService goPool = DEFAULT_GO_POOL;

    public DefaultFunicVisitor(Object context, FunicResolver resolver) {
        this.context = context;
        if (resolver != null) {
            this.resolver = resolver;
        }
    }

    @Override
    public FunicValue visitRoot(FunicParser.RootContext ctx) {
        FunicValue ret = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.ScriptContext) {
                FunicParser.ScriptContext nextCtx = (FunicParser.ScriptContext) child;
                try {
                    ret = visitScript(nextCtx);
                } catch (FunicControlException e) {
                    if (e instanceof FunicBreakException) {

                    } else if (e instanceof FunicContinueException) {

                    } else if (e instanceof FunicReturnException) {
                        FunicReturnException returnEx = (FunicReturnException) e;
                        if (returnEx.isHasRetValue()) {
                            return DefaultFunicValue.builder()
                                    .node(ctx)
                                    .value(returnEx.getRetValue())
                                    .build();
                        }
                        break;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public FunicValue visitScript(FunicParser.ScriptContext ctx) {
        FunicValue ret = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.ExpressContext) {
                FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) child;
                ret = visitExpress(nextCtx);
            }
        }
        return ret;
    }

    @Override
    public FunicValue visitExpress(FunicParser.ExpressContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.CircleExpressContext) {
            FunicParser.CircleExpressContext nextCtx = (FunicParser.CircleExpressContext) child;
            return visitCircleExpress(nextCtx);
        }
        if (child instanceof FunicParser.ExpressContext) {
            FunicParser.ExpressContext firstCtx = (FunicParser.ExpressContext) child;
            FunicValue firstValue = visitExpress(firstCtx);

            int count = ctx.getChildCount();
            if (count == 2) {
                ParseTree second = ctx.getChild(1);
                if (second instanceof FunicParser.InstanceFunctionCallRightPartContext) {
                    FunicParser.InstanceFunctionCallRightPartContext secondCtx = (FunicParser.InstanceFunctionCallRightPartContext) second;
                    KeyPairFunicValue secondValue = (KeyPairFunicValue) visitInstanceFunctionCallRightPart(secondCtx);
                    String methodName = secondValue.getKey();
                    List<Map.Entry<String, Object>> args = (List<Map.Entry<String, Object>>) secondValue.getValue();
                    Object ret = resolver.invokeInstanceMethod(firstValue.get(), methodName, args, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.InstanceFieldValueRightPartContext) {
                    FunicParser.InstanceFieldValueRightPartContext secondCtx = (FunicParser.InstanceFieldValueRightPartContext) second;
                    KeyPairFunicValue secondValue = (KeyPairFunicValue) visitInstanceFieldValueRightPart(secondCtx);
                    String operator = secondValue.getKey();
                    if (firstValue.get() == null) {
                        if (".?".equals(operator)) {
                            return NullFunicValue.builder()
                                    .node(ctx)
                                    .build();
                        }
                    }
                    String name = (String) secondValue.getValue();
                    Object value = resolver.getFieldValue(firstValue.get(), name, this);
                    return ParentFunicValue.builder()
                            .node(ctx)
                            .parent(firstValue.get())
                            .key(name)
                            .value(value)
                            .build();
                }
                if (second instanceof FunicParser.SquareQuoteRightPartContext) {
                    FunicParser.SquareQuoteRightPartContext secondCtx = (FunicParser.SquareQuoteRightPartContext) second;
                    FunicValue secondValue = visitSquareQuoteRightPart(secondCtx);
                    Object secondObj = secondValue.get();
                    Object value = resolver.getSquareFieldValue(firstValue.get(), secondObj, this);
                    return ParentFunicValue.builder()
                            .node(ctx)
                            .parent(firstValue.get())
                            .key(secondObj)
                            .value(value)
                            .build();
                }
                if (second instanceof FunicParser.FactorPercentRightPartContext) {
                    FunicParser.FactorPercentRightPartContext secondCtx = (FunicParser.FactorPercentRightPartContext) second;
                    TerminalFunicValue secondValue = (TerminalFunicValue) visitFactorPercentRightPart(secondCtx);
                    String operator = secondValue.getText();
                    Object value = resolver.suffixOperator(firstValue.get(), operator, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(value)
                            .build();
                }
                if (second instanceof FunicParser.IncrDecrAfterRightPartContext) {
                    FunicParser.IncrDecrAfterRightPartContext secondCtx = (FunicParser.IncrDecrAfterRightPartContext) second;
                    TerminalFunicValue secondValue = (TerminalFunicValue) visitIncrDecrAfterRightPart(secondCtx);
                    String operator = secondValue.getText();
                    Object value = resolver.suffixOperator(firstValue.get(), operator, this);
                    ParentFunicValue parentValue = (ParentFunicValue) firstValue;
                    resolver.setSquareFieldValue(parentValue.getParent(), parentValue.getKey(), value, this);
                    return ParentFunicValue.builder()
                            .node(ctx)
                            .parent(parentValue.getParent())
                            .key(parentValue.getKey())
                            .value(firstValue.get())
                            .build();
                }
                if (second instanceof FunicParser.CastAsRightPartContext) {
                    FunicParser.CastAsRightPartContext secondCtx = (FunicParser.CastAsRightPartContext) second;
                    TypeFunicValue secondValue = (TypeFunicValue) visitCastAsRightPart(secondCtx);
                    Class<?> type = secondValue.getType();
                    Object ret = resolver.convertType(firstValue.get(), type, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.ThirdOperateRightPartContext) {
                    FunicParser.ThirdOperateRightPartContext secondCtx = (FunicParser.ThirdOperateRightPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitThirdOperateRightPart(secondCtx);
                    List<Object> list = secondValue.getList();
                    boolean ok = resolver.toBoolean(firstValue.get(), this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ok ? list.get(0) : list.get(1))
                            .build();
                }
                if (second instanceof FunicParser.AssignRightPartContext) {
                    FunicParser.AssignRightPartContext secondCtx = (FunicParser.AssignRightPartContext) second;
                    return getAssignValue(firstValue, ctx, firstCtx, secondCtx);
                }
            } else if (count == 3) {
                ParseTree second = ctx.getChild(1);
                if (second instanceof FunicParser.MathMulDivOperatorPartContext) {
                    FunicParser.MathMulDivOperatorPartContext secondCtx = (FunicParser.MathMulDivOperatorPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitMathMulDivOperatorPart(secondCtx);
                    String operator = (String) secondValue.getList().get(0);

                    FunicParser.ExpressContext rightCtx = (FunicParser.ExpressContext) ctx.getChild(2);
                    FunicValue rightValue = visitExpress(rightCtx);

                    Object value = rightValue.get();
                    Object ret = resolver.doubleOperator(firstValue.get(), operator, value, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.MathAddSubOperatorPartContext) {
                    FunicParser.MathAddSubOperatorPartContext secondCtx = (FunicParser.MathAddSubOperatorPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitMathAddSubOperatorPart(secondCtx);
                    String operator = (String) secondValue.getList().get(0);

                    FunicParser.ExpressContext rightCtx = (FunicParser.ExpressContext) ctx.getChild(2);
                    FunicValue rightValue = visitExpress(rightCtx);

                    Object value = rightValue.get();
                    Object ret = resolver.doubleOperator(firstValue.get(), operator, value, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.CompareOperatorPartContext) {
                    FunicParser.CompareOperatorPartContext secondCtx = (FunicParser.CompareOperatorPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitCompareOperatorPart(secondCtx);
                    String operator = secondValue.getValue().stream().map(e -> (String) e).collect(Collectors.joining(";"));

                    FunicParser.ExpressContext rightCtx = (FunicParser.ExpressContext) ctx.getChild(2);
                    FunicValue rightValue = visitExpress(rightCtx);

                    Object value = rightValue.get();
                    Object ret = resolver.doubleOperator(firstValue.get(), operator, value, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.LogicalLinkOperatorPartContext) {
                    FunicParser.LogicalLinkOperatorPartContext secondCtx = (FunicParser.LogicalLinkOperatorPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitLogicalLinkOperatorPart(secondCtx);
                    String operator = (String) secondValue.getList().get(0);

                    FunicParser.ExpressContext rightCtx = (FunicParser.ExpressContext) ctx.getChild(2);
                    FunicValue rightValue = visitExpress(rightCtx);

                    Object value = rightValue.get();
                    Object ret = resolver.doubleOperator(firstValue.get(), operator, value, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
                if (second instanceof FunicParser.BitOperatorPartContext) {
                    FunicParser.BitOperatorPartContext secondCtx = (FunicParser.BitOperatorPartContext) second;
                    ListFunicValue secondValue = (ListFunicValue) visitBitOperatorPart(secondCtx);
                    String operator = (String) secondValue.getList().get(0);

                    FunicParser.ExpressContext rightCtx = (FunicParser.ExpressContext) ctx.getChild(2);
                    FunicValue rightValue = visitExpress(rightCtx);

                    Object value = rightValue.get();
                    Object ret = resolver.doubleOperator(firstValue.get(), operator, value, this);
                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
            } else {
                ParseTree second = ctx.getChild(1);
                if (second instanceof FunicParser.PipelineFunctionExpressContext) {
                    List<FunicParser.PipelineFunctionExpressContext> pipeList = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        ParseTree item = ctx.getChild(i);
                        if (item instanceof FunicParser.PipelineFunctionExpressContext) {
                            pipeList.add((FunicParser.PipelineFunctionExpressContext) item);
                        }
                    }

                    Object ret = firstValue.get();

                    for (FunicParser.PipelineFunctionExpressContext nextCtx : pipeList) {
                        PipelineFunctionFunicValue pipeValue = (PipelineFunctionFunicValue) visitPipelineFunctionExpress(nextCtx);
                        PipelineFunctionFunicValue.Type type = pipeValue.getType();
                        if (type == PipelineFunctionFunicValue.Type.INSTANCE) {
                            ret = resolver.invokeInstanceMethod(ret, pipeValue.getName(), pipeValue.getArgs(), this);
                        } else if (type == PipelineFunctionFunicValue.Type.STATIC) {
                            List<Map.Entry<String, Object>> args = pipeValue.getArgs();
                            args.add(0, new AbstractMap.SimpleEntry<>(null, ret));
                            ret = resolver.invokeStaticMethod(pipeValue.getClazz(), pipeValue.getName(), args, this);
                        } else if (type == PipelineFunctionFunicValue.Type.GLOBAL) {
                            List<Map.Entry<String, Object>> args = pipeValue.getArgs();
                            args.add(0, new AbstractMap.SimpleEntry<>(null, ret));
                            ret = resolver.invokeGlobalMethod(pipeValue.getName(), args, this);
                        }
                    }

                    return DefaultFunicValue.builder()
                            .node(ctx)
                            .value(ret)
                            .build();
                }
            }
        }
        if (child instanceof FunicParser.NewInstanceExpressContext) {
            FunicParser.NewInstanceExpressContext nextCtx = (FunicParser.NewInstanceExpressContext) child;
            return visitNewInstanceExpress(nextCtx);
        }
        if (child instanceof FunicParser.NewArrayExpressContext) {
            FunicParser.NewArrayExpressContext nextCtx = (FunicParser.NewArrayExpressContext) child;
            return visitNewArrayExpress(nextCtx);
        }
        if (child instanceof FunicParser.IfElseExpressContext) {
            FunicParser.IfElseExpressContext nextCtx = (FunicParser.IfElseExpressContext) child;
            return visitIfElseExpress(nextCtx);
        }
        if (child instanceof FunicParser.WhileExpressContext) {
            FunicParser.WhileExpressContext nextCtx = (FunicParser.WhileExpressContext) child;
            return visitWhileExpress(nextCtx);
        }
        if (child instanceof FunicParser.DoWhileExpressContext) {
            FunicParser.DoWhileExpressContext nextCtx = (FunicParser.DoWhileExpressContext) child;
            return visitDoWhileExpress(nextCtx);
        }
        if (child instanceof FunicParser.ForeachExpressContext) {
            FunicParser.ForeachExpressContext nextCtx = (FunicParser.ForeachExpressContext) child;
            return visitForeachExpress(nextCtx);
        }
        if (child instanceof FunicParser.ForLoopExpressContext) {
            FunicParser.ForLoopExpressContext nextCtx = (FunicParser.ForLoopExpressContext) child;
            return visitForLoopExpress(nextCtx);
        }
        if (child instanceof FunicParser.ForRangeExpressContext) {
            FunicParser.ForRangeExpressContext nextCtx = (FunicParser.ForRangeExpressContext) child;
            return visitForRangeExpress(nextCtx);
        }
        if (child instanceof FunicParser.BreakExpressContext) {
            FunicParser.BreakExpressContext nextCtx = (FunicParser.BreakExpressContext) child;
            return visitBreakExpress(nextCtx);
        }
        if (child instanceof FunicParser.ContinueExpressContext) {
            FunicParser.ContinueExpressContext nextCtx = (FunicParser.ContinueExpressContext) child;
            return visitContinueExpress(nextCtx);
        }
        if (child instanceof FunicParser.ReturnExpressContext) {
            FunicParser.ReturnExpressContext nextCtx = (FunicParser.ReturnExpressContext) child;
            return visitReturnExpress(nextCtx);
        }
        if (child instanceof FunicParser.ThrowExpressContext) {
            FunicParser.ThrowExpressContext nextCtx = (FunicParser.ThrowExpressContext) child;
            return visitThrowExpress(nextCtx);
        }
        if (child instanceof FunicParser.ImportExpressContext) {
            FunicParser.ImportExpressContext nextCtx = (FunicParser.ImportExpressContext) child;
            return visitImportExpress(nextCtx);
        }
        if (child instanceof FunicParser.TryCatchFinallyExpressContext) {
            FunicParser.TryCatchFinallyExpressContext nextCtx = (FunicParser.TryCatchFinallyExpressContext) child;
            return visitTryCatchFinallyExpress(nextCtx);
        }
        if (child instanceof FunicParser.FunctionDeclareExpressContext) {
            FunicParser.FunctionDeclareExpressContext nextCtx = (FunicParser.FunctionDeclareExpressContext) child;
            return visitFunctionDeclareExpress(nextCtx);
        }
        if (child instanceof FunicParser.LambdaExpressContext) {
            FunicParser.LambdaExpressContext nextCtx = (FunicParser.LambdaExpressContext) child;
            return visitLambdaExpress(nextCtx);
        }
        if (child instanceof FunicParser.GoRunExpressContext) {
            FunicParser.GoRunExpressContext nextCtx = (FunicParser.GoRunExpressContext) child;
            return visitGoRunExpress(nextCtx);
        }
        if (child instanceof FunicParser.SynchronizedExpressContext) {
            FunicParser.SynchronizedExpressContext nextCtx = (FunicParser.SynchronizedExpressContext) child;
            return visitSynchronizedExpress(nextCtx);
        }
        if (child instanceof FunicParser.StaticFunctionCallContext) {
            FunicParser.StaticFunctionCallContext nextCtx = (FunicParser.StaticFunctionCallContext) child;
            return visitStaticFunctionCall(nextCtx);
        }
        if (child instanceof FunicParser.GlobalFunctionCallContext) {
            FunicParser.GlobalFunctionCallContext nextCtx = (FunicParser.GlobalFunctionCallContext) child;
            return visitGlobalFunctionCall(nextCtx);
        }
        if (child instanceof FunicParser.StaticFieldValueContext) {
            FunicParser.StaticFieldValueContext nextCtx = (FunicParser.StaticFieldValueContext) child;
            return visitStaticFieldValue(nextCtx);
        }
        if (child instanceof FunicParser.PrefixOperatorPartContext) {
            FunicParser.PrefixOperatorPartContext operatorsCtx = (FunicParser.PrefixOperatorPartContext) child;
            ListFunicValue operatorValue = (ListFunicValue) visitPrefixOperatorPart(operatorsCtx);
            List<Object> operators = operatorValue.getList();

            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
            FunicValue nextValue = visitExpress(nextCtx);
            Object value = resolver.prefixOperator(nextValue.get(), (String) operators.get(0), this);
            return DefaultFunicValue.builder()
                    .node(ctx)
                    .value(value)
                    .build();
        }
        if (child instanceof FunicParser.IncrDecrPrefixOperatorPartContext) {
            FunicParser.IncrDecrPrefixOperatorPartContext operatorsCtx = (FunicParser.IncrDecrPrefixOperatorPartContext) child;
            ListFunicValue operatorValue = (ListFunicValue) visitIncrDecrPrefixOperatorPart(operatorsCtx);
            List<Object> operators = operatorValue.getList();

            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
            FunicValue nextValue = visitExpress(nextCtx);

            Object ret = resolver.prefixOperator(nextValue.get(), (String) operators.get(0), this);
            ParentFunicValue parentValue = (ParentFunicValue) nextValue;
            resolver.setSquareFieldValue(parentValue.getParent(), parentValue.getKey(), ret, this);
            return ParentFunicValue.builder()
                    .node(ctx)
                    .parent(parentValue.getParent())
                    .key(parentValue.getKey())
                    .value(ret)
                    .build();
        }
        if (child instanceof FunicParser.ListValueExpressContext) {
            FunicParser.ListValueExpressContext nextCtx = (FunicParser.ListValueExpressContext) child;
            return visitListValueExpress(nextCtx);
        }
        if (child instanceof FunicParser.MapValueExpressContext) {
            FunicParser.MapValueExpressContext nextCtx = (FunicParser.MapValueExpressContext) child;
            return visitMapValueExpress(nextCtx);
        }
        if (child instanceof FunicParser.ScriptBlockContext) {
            FunicParser.ScriptBlockContext nextCtx = (FunicParser.ScriptBlockContext) child;
            return visitScriptBlock(nextCtx);
        }
        if (child instanceof TerminalNode) {
            TerminalNode nextCtx = (TerminalNode) child;
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(nextCtx);
            String text = terminalValue.getText();
            if ("def".equals(text)) {
                FunicParser.ExpressContext leftCtx = (FunicParser.ExpressContext) ctx.getChild(1);
                FunicParser.AssignRightPartContext rightCtx = (FunicParser.AssignRightPartContext) ctx.getChild(2);
                return getAssignValue(null, ctx, leftCtx, rightCtx);
            }
        }
        if (child instanceof FunicParser.ValueSegmentContext) {
            FunicParser.ValueSegmentContext nextCtx = (FunicParser.ValueSegmentContext) child;
            return visitValueSegment(nextCtx);
        }

        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support key-value node!");
    }

    public FunicValue getAssignValue(FunicValue leftValue, RuleNode triggerNode, FunicParser.ExpressContext leftCtx, FunicParser.AssignRightPartContext rightCtx) {
        if (leftValue == null) {
            leftValue = visitExpress(leftCtx);
        }

        FunicParser.AssignRightPartContext secondCtx = rightCtx;
        KeyPairFunicValue secondValue = (KeyPairFunicValue) visitAssignRightPart(secondCtx);
        String operator = secondValue.getKey();
        Object value = secondValue.getValue();
        if ("+=".equals(operator)) {
            value = resolver.doubleOperator(leftValue.get(), "+", value, this);
        } else if ("-=".equals(operator)) {
            value = resolver.doubleOperator(leftValue.get(), "-", value, this);
        } else if ("*=".equals(operator)) {
            value = resolver.doubleOperator(leftValue.get(), "*", value, this);
        } else if ("/=".equals(operator)) {
            value = resolver.doubleOperator(leftValue.get(), "/", value, this);
        } else if ("%=".equals(operator)) {
            value = resolver.doubleOperator(leftValue.get(), "%", value, this);
        }
        ParentFunicValue parentValue = (ParentFunicValue) leftValue;
        // ?= 空复制语句，只有左侧值为空时，才将右侧的值进行赋值，等价于 if(a==null){a=b;};
        //.= 替换赋值语句，只有左侧值非空时，才将右侧的值进行赋值，等价于 if(a!=null){a=b;};
        if ("?=".equals(operator)) {
            if (leftValue.get() == null) {
                resolver.setSquareFieldValue(parentValue.getParent(), parentValue.getKey(), value, this);
            } else {
                value = leftValue.get();
            }
        } else if (".=".equals(operator)) {
            if (leftValue.get() != null) {
                resolver.setSquareFieldValue(parentValue.getParent(), parentValue.getKey(), value, this);
            } else {
                value = leftValue.get();
            }
        } else {
            resolver.setSquareFieldValue(parentValue.getParent(), parentValue.getKey(), value, this);
        }

        return ParentFunicValue.builder()
                .node(triggerNode)
                .parent(parentValue.getParent())
                .key(parentValue.getKey())
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitLogicalLinkOperatorPart(FunicParser.LogicalLinkOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitCompareOperatorPart(FunicParser.CompareOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitBitOperatorPart(FunicParser.BitOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitMathAddSubOperatorPart(FunicParser.MathAddSubOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitMathMulDivOperatorPart(FunicParser.MathMulDivOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitIncrDecrPrefixOperatorPart(FunicParser.IncrDecrPrefixOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitPrefixOperatorPart(FunicParser.PrefixOperatorPartContext ctx) {
        ListFunicValue listValue = (ListFunicValue) getOperatorsNode(ctx);
        return listValue;
    }

    @Override
    public FunicValue visitPipelineFunctionExpress(FunicParser.PipelineFunctionExpressContext ctx) {
        PipelineFunctionFunicValue ret = new PipelineFunctionFunicValue();
        int count = ctx.getChildCount();
        ParseTree child = null;
        if (count == 2) {
            child = ctx.getChild(1);
            if (child instanceof TerminalNode) {
                ret.setType(PipelineFunctionFunicValue.Type.GLOBAL);
            } else if (child instanceof FunicParser.StaticFunctionCallContext) {
                ret.setType(PipelineFunctionFunicValue.Type.STATIC);
            } else if (child instanceof FunicParser.GlobalFunctionCallContext) {
                ret.setType(PipelineFunctionFunicValue.Type.GLOBAL);
            }
        } else if (count == 3) {
            child = ctx.getChild(2);
            ret.setType(PipelineFunctionFunicValue.Type.INSTANCE);
        }

        if (child instanceof TerminalNode) {
            TerminalNode terminalCtx = (TerminalNode) child;
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalCtx);
            ret.setClazz(null);
            ret.setName(terminalValue.getText());
            ret.setArgs(new ArrayList<>());
        } else if (child instanceof FunicParser.StaticFunctionCallContext) {
            FunicParser.StaticFunctionCallContext nextCtx = (FunicParser.StaticFunctionCallContext) child;
            PipelineFunctionFunicValue nextValue = getStaticFunctionCall(nextCtx);
            ret.setClazz(nextValue.getClazz());
            ret.setName(nextValue.getName());
            ret.setArgs(nextValue.getArgs());
        } else if (child instanceof FunicParser.GlobalFunctionCallContext) {
            FunicParser.GlobalFunctionCallContext nextCtx = (FunicParser.GlobalFunctionCallContext) child;
            PipelineFunctionFunicValue nextValue = getGlobalFunctionCall(nextCtx);
            ret.setClazz(null);
            ret.setName(nextValue.getName());
            ret.setArgs(nextValue.getArgs());
        }

        return ret;
    }

    @Override
    public FunicValue visitSynchronizedExpress(FunicParser.SynchronizedExpressContext ctx) {
        FunicParser.ExpressContext lockCtx = (FunicParser.ExpressContext) ctx.getChild(2);
        FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) ctx.getChild(4);
        FunicValue value = visitExpress(lockCtx);
        Object lockObj = value.get();
        if (lockObj == null) {
            lockObj = this;
        }
        synchronized (lockObj) {
            return visitScriptBlock(scriptCtx);
        }
    }

    @Override
    public FunicValue visitLambdaExpress(FunicParser.LambdaExpressContext ctx) {
        FunicParser.FunctionArgumentsContext argumentsCtx = (FunicParser.FunctionArgumentsContext) ctx.getChild(0);
        ListKeyPairFunicValue argumentsValue = (ListKeyPairFunicValue) visitFunctionArguments(argumentsCtx);
        List<Map.Entry<String, Object>> argumentsList = argumentsValue.getList();

        FunicParser.ScriptBlockContext bodyCtx = (FunicParser.ScriptBlockContext) ctx.getChild(2);

        FunicLambda lambda = FunicLambda.builder()
                .arguments(argumentsList)
                .body(bodyCtx)
                .build();

        return DefaultFunicValue.builder()
                .node(ctx)
                .value(lambda)
                .build();
    }

    @Override
    public FunicValue visitImportExpress(FunicParser.ImportExpressContext ctx) {
        FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) ctx.getChild(1);
        FullNameFunicValue nextValue = (FullNameFunicValue) visitFullName(nextCtx);
        String packageName = nextValue.getName();
        boolean ok = resolver.onPreRegistryContextImportPackage(packageName, this);
        if (ok) {
            importPackages.add(packageName);
        }
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(packageName)
                .build();
    }

    @Override
    public FunicValue visitGoRunExpress(FunicParser.GoRunExpressContext ctx) {
        CompletableFuture<Object> future = null;
        ParseTree child = ctx.getChild(1);
        if (child instanceof FunicParser.ExpressContext) {
            future = CompletableFuture.supplyAsync(() -> {
                FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) child;
                FunicValue value = visitExpress(nextCtx);
                Object obj = value.get();
                if (obj instanceof FunicMethod) {
                    FunicMethod funicMethod = (FunicMethod) obj;
                    if (funicMethod.getParameterCount() == 0) {
                        FunicMethod copied = funicMethod.copy();
                        copied.setVisitor(this);
                        try {
                            obj = copied.invoke(null);
                        } catch (Throwable e) {
                            if (e instanceof RuntimeException) {
                                throw (RuntimeException) e;
                            }
                            throw new FunicThrowException(e.getMessage(), e);
                        }
                    } else {
                        throw new FunicThrowException("go task cannot support run function with args!");
                    }
                } else if (obj instanceof FunicLambda) {
                    FunicLambda funicLambda = (FunicLambda) obj;
                    obj = funicLambda.invoke(this);
                }
                return obj;
            }, goPool);
        } else if (child instanceof FunicParser.ScriptBlockContext) {
            future = CompletableFuture.supplyAsync(() -> {
                FunicParser.ScriptBlockContext nextCTx = (FunicParser.ScriptBlockContext) child;
                FunicValue value = visitScriptBlock(nextCTx);
                return value.get();
            }, goPool);
        } else if (child instanceof FunicParser.LambdaExpressContext) {
            future = CompletableFuture.supplyAsync(() -> {
                FunicParser.LambdaExpressContext nextCTx = (FunicParser.LambdaExpressContext) child;
                FunicValue value = visitLambdaExpress(nextCTx);
                FunicLambda funicLambda = (FunicLambda) value.get();
                return funicLambda.invoke(this);
            }, goPool);
        }


        return DefaultFunicValue.builder()
                .node(ctx)
                .value(future)
                .build();
    }

    @Override
    public FunicValue visitAwaitExpress(FunicParser.AwaitExpressContext ctx) {
        List<Object> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.ExpressContext) {
                FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) child;
                FunicValue value = visitExpress(nextCtx);
                Object obj = value.get();
                if (obj instanceof Future) {
                    Future<?> future = (Future<?>) obj;
                    try {
                        obj = future.get();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new FunicThrowException(e.getMessage(), e);
                    }
                } else if (obj instanceof CountDownLatch) {
                    CountDownLatch latch = (CountDownLatch) obj;
                    try {
                        latch.await();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new FunicThrowException(e.getMessage(), e);
                    }
                } else if (obj instanceof CyclicBarrier) {
                    CyclicBarrier barrier = (CyclicBarrier) obj;
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new FunicThrowException(e.getMessage(), e);
                    }
                } else if (obj instanceof Condition) {
                    Condition condition = (Condition) obj;
                    try {
                        condition.wait();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new FunicThrowException(e.getMessage(), e);
                    }
                } else if (obj instanceof Semaphore) {
                    Semaphore semaphore = (Semaphore) obj;
                    try {
                        semaphore.acquire();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new FunicThrowException(e.getMessage(), e);
                    }
                } else if (obj instanceof Lock) {
                    Lock lock = (Lock) obj;
                    lock.lock();
                }
                list.add(obj);
            }
        }
        if (list.size() == 1) {
            return DefaultFunicValue.builder()
                    .node(ctx)
                    .value(list.get(0))
                    .build();
        }
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(list)
                .build();
    }

    @Override
    public FunicValue visitFunctionDeclareExpress(FunicParser.FunctionDeclareExpressContext ctx) {
        TerminalNode nameCtx = (TerminalNode) ctx.getChild(1);
        FunicParser.FunctionDeclareParametersContext parametersCtx = (FunicParser.FunctionDeclareParametersContext) ctx.getChild(2);
        FunicParser.FunctionDeclareReturnContext returnCtx = null;
        FunicParser.ScriptBlockContext bodyCtx = null;
        int count = ctx.getChildCount();
        if (count == 4) {
            bodyCtx = (FunicParser.ScriptBlockContext) ctx.getChild(3);
        } else {
            returnCtx = (FunicParser.FunctionDeclareReturnContext) ctx.getChild(3);
            bodyCtx = (FunicParser.ScriptBlockContext) ctx.getChild(3);
        }
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();
        ListKeyPairFunicValue parametersValue = (ListKeyPairFunicValue) visitFunctionDeclareParameters(parametersCtx);
        List<Map.Entry<String, Object>> parameterList = parametersValue.getList();
        List<Map.Entry<Class, String>> parameters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : parameterList) {
            Class value = (Class) entry.getValue();
            if (value == null) {
                value = Object.class;
            }
            parameters.add(new AbstractMap.SimpleEntry<>(value, entry.getKey()));
        }
        Class returnType = Object.class;
        if (returnCtx != null) {
            TypeFunicValue returnValue = (TypeFunicValue) visitFunctionDeclareReturn(returnCtx);
            returnType = returnValue.getType();
        }

        IMethod method = FunicMethod.builder()
                .name(name)
                .parameters(parameters)
                .returnType(returnType)
                .body(bodyCtx)
                .visitor(this)
                .build();
        boolean ok = resolver.onPreRegisterContextGlobalMethod(method, this);
        if (ok) {
            CopyOnWriteArrayList<IMethod> list = registryMethods.computeIfAbsent(name, e -> new CopyOnWriteArrayList<>());
            list.add(0, method);
        }
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(method)
                .build();
    }

    @Override
    public FunicValue visitFunctionDeclareReturn(FunicParser.FunctionDeclareReturnContext ctx) {
        FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) ctx.getChild(1);
        FullNameFunicValue nextValue = (FullNameFunicValue) visitFullName(nextCtx);
        Class<?> clazz = resolver.findClass(nextValue.getName(), this);
        return TypeFunicValue.builder()
                .node(ctx)
                .value(clazz)
                .build();
    }

    @Override
    public FunicValue visitFunctionDeclareParameters(FunicParser.FunctionDeclareParametersContext ctx) {
        List<Map.Entry<String, Object>> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.FunctionParameterContext) {
                FunicParser.FunctionParameterContext nextCtx = (FunicParser.FunctionParameterContext) child;
                KeyPairFunicValue nextValue = (KeyPairFunicValue) visitFunctionParameter(nextCtx);
                list.add(new AbstractMap.SimpleEntry<>(nextValue.getKey(), nextValue.getValue()));
            }
        }
        return ListKeyPairFunicValue.builder()
                .node(ctx)
                .value(list)
                .build();
    }

    @Override
    public FunicValue visitFunctionParameter(FunicParser.FunctionParameterContext ctx) {
        int count = ctx.getChildCount();
        if (count == 1) {
            TerminalNode terminalCtx = (TerminalNode) ctx.getChild(0);
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalCtx);
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(terminalValue.getText())
                    .value(null)
                    .build();
        } else if (count == 2) {
            FunicParser.FullNameContext typeCtx = (FunicParser.FullNameContext) ctx.getChild(0);
            FullNameFunicValue typeValue = (FullNameFunicValue) visitFullName(typeCtx);
            String className = typeValue.getName();
            Class<?> clazz = resolver.findClass(className, this);

            TerminalNode terminalCtx = (TerminalNode) ctx.getChild(1);
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalCtx);
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(terminalValue.getText())
                    .value(clazz)
                    .build();
        } else if (count == 3) {
            TerminalNode terminalCtx = (TerminalNode) ctx.getChild(0);
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalCtx);

            FunicParser.FullNameContext typeCtx = (FunicParser.FullNameContext) ctx.getChild(2);
            FullNameFunicValue typeValue = (FullNameFunicValue) visitFullName(typeCtx);
            String className = typeValue.getName();
            Class<?> clazz = resolver.findClass(className, this);

            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(terminalValue.getText())
                    .value(clazz)
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " function parameter not support this formal!");
    }

    @Override
    public FunicValue visitTryCatchFinallyExpress(FunicParser.TryCatchFinallyExpressContext ctx) {
        FunicParser.ScriptBlockContext bodyCtx = null;
        List<FunicParser.CatchBlockContext> catchList = new ArrayList<>();
        FunicParser.ScriptBlockContext finallyCtx = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.ScriptBlockContext) {
                FunicParser.ScriptBlockContext nextCtx = (FunicParser.ScriptBlockContext) child;
                if (bodyCtx == null) {
                    bodyCtx = nextCtx;
                } else {
                    finallyCtx = nextCtx;
                }
            } else if (child instanceof FunicParser.CatchBlockContext) {
                FunicParser.CatchBlockContext nextCtx = (FunicParser.CatchBlockContext) child;
                catchList.add(nextCtx);
            }
        }

        assert bodyCtx != null;

        try {
            return visitScriptBlock(bodyCtx);
        } catch (Throwable e) {
            boolean resolved = false;
            if (e instanceof FunicControlException) {
                resolved = true;
            }
            if (!resolved) {
                if (catchList != null && !catchList.isEmpty()) {
                    for (FunicParser.CatchBlockContext nextCtx : catchList) {
                        CatchBlockFunicValue nextValue = (CatchBlockFunicValue) visitCatchBlock(nextCtx);
                        List<Class<?>> types = nextValue.getTypes();
                        if (types == null || types.isEmpty()) {
                            types = Collections.singletonList(Throwable.class);
                        }
                        Class<? extends Throwable> clazz = e.getClass();
                        for (Class<?> type : types) {
                            if (clazz.equals(type)
                                    || type.isAssignableFrom(clazz)) {
                                Object bakValue = resolver.getFieldValue(context, nextValue.getName(), this);
                                try {
                                    FunicValue value = visitScriptBlock(nextValue.getScriptCtx());
                                    break;
                                } finally {
                                    resolver.setFieldValue(context, nextValue.getName(), bakValue, this);
                                }
                            }
                        }
                    }
                }
            }
            if (!resolved) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new FunicThrowException(e.getMessage(), e);
                }
            }
        } finally {
            if (finallyCtx != null) {
                visitScriptBlock(finallyCtx);
            }
        }
        return NullFunicValue.builder()
                .node(ctx)
                .build();
    }

    @Override
    public FunicValue visitCatchBlock(FunicParser.CatchBlockContext ctx) {
        CatchBlockFunicValue ret = new CatchBlockFunicValue();
        ret.setTypes(new ArrayList<>());
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.FullNameContext) {
                FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) child;
                FullNameFunicValue nextValue = (FullNameFunicValue) visitFullName(nextCtx);
                Class<?> clazz = resolver.findClass(nextValue.getName(), this);
                ret.getTypes().add(clazz);
            } else if (child instanceof FunicParser.ScriptBlockContext) {
                FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) child;
                ret.setScriptCtx(scriptCtx);
            } else if (i == count - 3) {
                TerminalNode terminalNode = (TerminalNode) child;
                TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalNode);
                ret.setName(terminalValue.getText());
            }
        }
        return ret;
    }

    @Override
    public FunicValue visitThrowExpress(FunicParser.ThrowExpressContext ctx) {
        FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
        FunicValue nextValue = visitExpress(nextCtx);
        Object value = nextValue.get();
        if (value instanceof RuntimeException) {
            throw (RuntimeException) value;
        } else if (value instanceof Throwable) {
            Throwable ex = (Throwable) value;
            throw new FunicThrowException(ex.getMessage(), ex);
        }
        throw new FunicThrowDataException(value, "grammar defined!");
    }

    @Override
    public FunicValue visitReturnExpress(FunicParser.ReturnExpressContext ctx) {
        int count = ctx.getChildCount();
        if (count == 2) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
            FunicValue value = visitExpress(nextCtx);
            throw new FunicReturnException("grammar defined!", value.get());
        }
        throw new FunicReturnException("grammar defined!");
    }

    @Override
    public FunicValue visitContinueExpress(FunicParser.ContinueExpressContext ctx) {
        throw new FunicContinueException("grammar defined!");
    }

    @Override
    public FunicValue visitBreakExpress(FunicParser.BreakExpressContext ctx) {
        throw new FunicBreakException("grammar defined!");
    }

    @Override
    public FunicValue visitForRangeExpress(FunicParser.ForRangeExpressContext ctx) {
        TerminalNode nameCtx = (TerminalNode) ctx.getChild(2);
        FunicParser.ConstNumberContext leftCtx = (FunicParser.ConstNumberContext) ctx.getChild(3);
        FunicParser.ConstNumberContext rightCtx = (FunicParser.ConstNumberContext) ctx.getChild(5);
        FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) ctx.getChild(7);
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();
        NumberFunicValue leftValue = (NumberFunicValue) visitConstNumber(leftCtx);
        long left = leftValue.getLong();
        NumberFunicValue rightValue = (NumberFunicValue) visitConstNumber(rightCtx);
        long right = rightValue.getLong();
        AtomicReference<Object> bakNameValue = null;
        FunicValue ret = null;
        try {
            long step = left <= right ? 1 : -1;
            while (left != right) {
                if (bakNameValue == null) {
                    Object obj = resolver.getFieldValue(context, name, this);
                    bakNameValue = new AtomicReference<>(obj);
                }
                resolver.setFieldValue(context, name, left, this);
                try {
                    ret = visitScriptBlock(scriptCtx);
                } catch (FunicControlException e) {
                    if (e instanceof FunicContinueException) {
                        continue;
                    }
                    if (e instanceof FunicBreakException) {
                        break;
                    }
                }
                left += step;
            }
        } finally {
            if (bakNameValue != null) {
                resolver.setFieldValue(context, name, bakNameValue.get(), this);
            }
        }
        if (ret == null) {
            return NullFunicValue.builder()
                    .node(ctx)
                    .build();
        }
        return ret;
    }

    @Override
    public FunicValue visitForLoopExpress(FunicParser.ForLoopExpressContext ctx) {
        int count = ctx.getChildCount();
        FunicParser.ExpressContext beginCtx = null;
        FunicParser.ExpressContext condCtx = null;
        FunicParser.ExpressContext nextCtx = null;
        FunicParser.ScriptBlockContext scriptCtx = null;
        int sepCount = 0;
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) child;
                TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalNode);
                if (";".equals(terminalValue.getText())) {
                    sepCount++;
                }
            } else if (child instanceof FunicParser.ExpressContext) {
                if (sepCount == 0) {
                    beginCtx = (FunicParser.ExpressContext) child;
                } else if (sepCount == 1) {
                    condCtx = (FunicParser.ExpressContext) child;
                } else if (sepCount == 2) {
                    nextCtx = (FunicParser.ExpressContext) child;
                }
            } else if (child instanceof FunicParser.ScriptBlockContext) {
                scriptCtx = (FunicParser.ScriptBlockContext) child;
            }
        }
        assert scriptCtx != null;
        FunicValue ret = null;
        if (beginCtx != null) {
            FunicValue value = visitExpress(beginCtx);
        }
        while (true) {
            if (condCtx != null) {
                FunicValue value = visitExpress(condCtx);
                boolean ok = resolver.toBoolean(value.get(), this);
                if (!ok) {
                    break;
                }
            }
            try {
                ret = visitScriptBlock(scriptCtx);
            } catch (FunicControlException e) {
                if (e instanceof FunicContinueException) {
                    continue;
                }
                if (e instanceof FunicBreakException) {
                    break;
                }
            }
            if (nextCtx != null) {
                FunicValue value = visitExpress(nextCtx);
            }
        }
        if (ret == null) {
            return NullFunicValue.builder()
                    .node(ctx)
                    .build();
        }
        return ret;
    }

    @Override
    public FunicValue visitForeachExpress(FunicParser.ForeachExpressContext ctx) {
        TerminalNode nameCtx = (TerminalNode) ctx.getChild(2);
        FunicParser.ExpressContext iterableCtx = (FunicParser.ExpressContext) ctx.getChild(4);
        FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) ctx.getChild(6);
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();
        FunicValue iterableValue = visitExpress(iterableCtx);
        Iterator<?> iterator = resolver.wrapAsIterator(iterableValue.get(), this);
        AtomicReference<Object> bakNameValue = null;
        FunicValue ret = null;
        try {
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (bakNameValue == null) {
                    Object obj = resolver.getFieldValue(context, name, this);
                    bakNameValue = new AtomicReference<>(obj);
                }
                resolver.setFieldValue(context, name, item, this);
                try {
                    ret = visitScriptBlock(scriptCtx);
                } catch (FunicControlException e) {
                    if (e instanceof FunicContinueException) {
                        continue;
                    }
                    if (e instanceof FunicBreakException) {
                        break;
                    }
                }
            }
        } finally {
            if (bakNameValue != null) {
                resolver.setFieldValue(context, name, bakNameValue.get(), this);
            }
        }
        if (ret == null) {
            return NullFunicValue.builder()
                    .node(ctx)
                    .build();
        }
        return ret;
    }

    @Override
    public FunicValue visitDoWhileExpress(FunicParser.DoWhileExpressContext ctx) {
        FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) ctx.getChild(1);
        FunicParser.ConditionBlockContext condCtx = (FunicParser.ConditionBlockContext) ctx.getChild(3);
        FunicValue ret = null;
        while (true) {
            try {
                ret = visitScriptBlock(scriptCtx);
            } catch (FunicControlException e) {
                if (e instanceof FunicContinueException) {
                    continue;
                }
                if (e instanceof FunicBreakException) {
                    break;
                }
            }
            BooleanFunicValue condValue = (BooleanFunicValue) visitConditionBlock(condCtx);
            if (!condValue.getBoolean()) {
                break;
            }
        }
        if (ret == null) {
            return NullFunicValue.builder()
                    .node(ctx)
                    .build();
        }
        return ret;
    }

    @Override
    public FunicValue visitWhileExpress(FunicParser.WhileExpressContext ctx) {
        FunicParser.ConditionBlockContext condCtx = (FunicParser.ConditionBlockContext) ctx.getChild(1);
        FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) ctx.getChild(2);
        FunicValue ret = null;
        while (true) {
            BooleanFunicValue condValue = (BooleanFunicValue) visitConditionBlock(condCtx);
            if (!condValue.getBoolean()) {
                break;
            }
            try {
                ret = visitScriptBlock(scriptCtx);
            } catch (FunicControlException e) {
                if (e instanceof FunicContinueException) {
                    continue;
                }
                if (e instanceof FunicBreakException) {
                    break;
                }
            }
        }
        if (ret == null) {
            return NullFunicValue.builder()
                    .node(ctx)
                    .build();
        }
        return ret;
    }

    @Override
    public FunicValue visitIfElseExpress(FunicParser.IfElseExpressContext ctx) {
        List<RuleNode> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.ConditionBlockContext) {
                list.add((FunicParser.ConditionBlockContext) child);
            } else if (child instanceof FunicParser.ScriptBlockContext) {
                list.add((FunicParser.ScriptBlockContext) child);
            }
        }
        int size = list.size();
        for (int i = 0; i + 1 < size; i += 2) {
            FunicParser.ConditionBlockContext condCtx = (FunicParser.ConditionBlockContext) list.get(i);
            BooleanFunicValue condValue = (BooleanFunicValue) visitConditionBlock(condCtx);
            if (condValue.getBoolean()) {
                FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) list.get(i + 1);
                return visitScriptBlock(scriptCtx);
            }
        }
        if (size % 2 != 0) {
            FunicParser.ScriptBlockContext scriptCtx = (FunicParser.ScriptBlockContext) list.get(size - 1);
            return visitScriptBlock(scriptCtx);
        }
        return NullFunicValue.builder()
                .node(ctx)
                .build();
    }

    @Override
    public FunicValue visitConditionBlock(FunicParser.ConditionBlockContext ctx) {
        FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
        FunicValue value = visitExpress(nextCtx);
        boolean ok = resolver.toBoolean(value.get(), this);
        return BooleanFunicValue.builder()
                .node(ctx)
                .value(ok)
                .build();
    }

    @Override
    public FunicValue visitScriptBlock(FunicParser.ScriptBlockContext ctx) {
        int count = ctx.getChildCount();
        if (count == 3) {
            FunicParser.ScriptContext nextCtx = (FunicParser.ScriptContext) ctx.getChild(1);
            return visitScript(nextCtx);
        }
        return null;
    }

    @Override
    public FunicValue visitCastAsRightPart(FunicParser.CastAsRightPartContext ctx) {
        FunicParser.TypeClassContext nextCtx = (FunicParser.TypeClassContext) ctx.getChild(1);
        TypeFunicValue nextValue = (TypeFunicValue) visitTypeClass(nextCtx);
        return nextValue;
    }

    @Override
    public FunicValue visitMapValueExpress(FunicParser.MapValueExpressContext ctx) {
        Map<String, Object> map = new LinkedHashMap<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.UnpackMapExpressContext) {
                FunicParser.UnpackMapExpressContext nextCtx = (FunicParser.UnpackMapExpressContext) child;
                MapFunicValue nextValue = (MapFunicValue) visitUnpackMapExpress(nextCtx);
                map.putAll(nextValue.getMap());
            }
        }
        return MapFunicValue.builder()
                .node(ctx)
                .value(map)
                .build();
    }

    @Override
    public FunicValue visitUnpackMapExpress(FunicParser.UnpackMapExpressContext ctx) {
        Map<String, Object> map = new LinkedHashMap<>();
        int count = ctx.getChildCount();
        if (count == 1) {
            FunicParser.KeyValueExpressContext nextCtx = (FunicParser.KeyValueExpressContext) ctx.getChild(0);
            KeyPairFunicValue nextValue = (KeyPairFunicValue) visitKeyValueExpress(nextCtx);
            map.put(nextValue.getKey(), nextValue.getValue());
        } else if (count == 2) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
            FunicValue nextValue = visitExpress(nextCtx);
            Map<String, Object> next = resolver.unpackMap(nextValue.get(), this);
            map.putAll(next);
        }
        return MapFunicValue.builder()
                .node(ctx)
                .value(map)
                .build();
    }

    @Override
    public FunicValue visitKeyValueExpress(FunicParser.KeyValueExpressContext ctx) {
        int count = ctx.getChildCount();
        if (count == 1) {
            FunicParser.VariableValueContext nextCtx = (FunicParser.VariableValueContext) ctx.getChild(0);
            ParentFunicValue value = (ParentFunicValue) visitVariableValue(nextCtx);
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(null)
                    .value(value.get())
                    .build();
        } else if (count == 3) {
            ParseTree first = ctx.getChild(0);
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(2);
            FunicValue nextValue = visitExpress(nextCtx);
            String name = null;
            if (first instanceof TerminalNode) {
                TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal((TerminalNode) first);
                name = terminalValue.getText();
            } else if (first instanceof FunicParser.ConstStringContext) {
                ConstStringFunicValue stringValue = (ConstStringFunicValue) visitConstString((FunicParser.ConstStringContext) first);
                name = stringValue.getText();
            } else if (first instanceof FunicParser.ConstRenderStringContext) {
                ConstStringFunicValue stringValue = (ConstStringFunicValue) visitConstRenderString((FunicParser.ConstRenderStringContext) first);
                name = stringValue.getText();
            }
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(name)
                    .value(nextValue.get())
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support key-value node!");
    }

    @Override
    public FunicValue visitThirdOperateRightPart(FunicParser.ThirdOperateRightPartContext ctx) {
        FunicParser.ExpressContext operatorCtx = (FunicParser.ExpressContext) ctx.getChild(1);
        FunicValue trueValue = visitExpress(operatorCtx);

        FunicParser.ExpressContext nameCtx = (FunicParser.ExpressContext) ctx.getChild(3);
        FunicValue elseValue = visitExpress(nameCtx);

        return ListFunicValue.builder()
                .node(ctx)
                .value(new ArrayList<>(Arrays.asList(
                        trueValue.get(),
                        elseValue.get()
                )))
                .build();
    }

    @Override
    public FunicValue visitInstanceFieldValueRightPart(FunicParser.InstanceFieldValueRightPartContext ctx) {
        TerminalNode operatorCtx = (TerminalNode) ctx.getChild(0);
        TerminalFunicValue operatorValue = (TerminalFunicValue) visitTerminal(operatorCtx);
        String operator = operatorValue.getText();

        TerminalNode nameCtx = (TerminalNode) ctx.getChild(1);
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();

        return KeyPairFunicValue.builder()
                .node(ctx)
                .key(operator)
                .value(name)
                .build();
    }

    @Override
    public FunicValue visitCircleExpress(FunicParser.CircleExpressContext ctx) {
        FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
        FunicValue value = visitExpress(nextCtx);
        return value;
    }

    @Override
    public FunicValue visitNewArrayExpress(FunicParser.NewArrayExpressContext ctx) {
        FunicParser.FullNameContext nameCtx = (FunicParser.FullNameContext) ctx.getChild(1);
        FullNameFunicValue nameValue = (FullNameFunicValue) visitFullName(nameCtx);
        String name = nameValue.getName();

        FunicParser.ConstNumberContext countCtx = (FunicParser.ConstNumberContext) ctx.getChild(3);
        NumberFunicValue countValue = (NumberFunicValue) visitConstNumber(countCtx);
        int count = countValue.getInteger();

        Class<?> clazz = resolver.findClass(name, this);
        Object obj = resolver.newArray(clazz, count, this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(obj)
                .build();
    }

    @Override
    public FunicValue visitNewInstanceExpress(FunicParser.NewInstanceExpressContext ctx) {
        FunicParser.FullNameContext nameCtx = (FunicParser.FullNameContext) ctx.getChild(1);
        FullNameFunicValue nameValue = (FullNameFunicValue) visitFullName(nameCtx);
        String name = nameValue.getName();

        FunicParser.FunctionArgumentsContext argsCtx = (FunicParser.FunctionArgumentsContext) ctx.getChild(2);
        ListKeyPairFunicValue argsValue = (ListKeyPairFunicValue) visitFunctionArguments(argsCtx);
        List<Map.Entry<String, Object>> args = argsValue.getList();

        Class<?> clazz = resolver.findClass(name, this);
        Object obj = resolver.newInstance(clazz, args, this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(obj)
                .build();
    }

    @Override
    public FunicValue visitInstanceFunctionCallRightPart(FunicParser.InstanceFunctionCallRightPartContext ctx) {
        TerminalNode nameCtx = (TerminalNode) ctx.getChild(1);
        TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = terminalValue.getText();

        FunicParser.FunctionArgumentsContext argsCtx = (FunicParser.FunctionArgumentsContext) ctx.getChild(2);
        ListKeyPairFunicValue argsValue = (ListKeyPairFunicValue) visitFunctionArguments(argsCtx);
        List<Map.Entry<String, Object>> args = argsValue.getList();

        return KeyPairFunicValue.builder()
                .node(ctx)
                .key(name)
                .value(args)
                .build();
    }

    public PipelineFunctionFunicValue getGlobalFunctionCall(FunicParser.GlobalFunctionCallContext ctx) {
        TerminalNode nameCtx = (TerminalNode) ctx.getChild(0);
        TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = terminalValue.getText();

        FunicParser.FunctionArgumentsContext argsCtx = (FunicParser.FunctionArgumentsContext) ctx.getChild(1);
        ListKeyPairFunicValue argsValue = (ListKeyPairFunicValue) visitFunctionArguments(argsCtx);
        List<Map.Entry<String, Object>> args = argsValue.getList();

        return PipelineFunctionFunicValue.builder()
                .type(PipelineFunctionFunicValue.Type.GLOBAL)
                .clazz(null)
                .name(name)
                .args(args)
                .build();
    }

    @Override
    public FunicValue visitGlobalFunctionCall(FunicParser.GlobalFunctionCallContext ctx) {
        PipelineFunctionFunicValue nextValue = getGlobalFunctionCall(ctx);

        Object value = resolver.invokeGlobalMethod(nextValue.getName(),
                nextValue.getArgs(),
                this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitSquareQuoteRightPart(FunicParser.SquareQuoteRightPartContext ctx) {
        FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
        FunicValue value = visitExpress(nextCtx);
        return value;
    }

    @Override
    public FunicValue visitFactorPercentRightPart(FunicParser.FactorPercentRightPartContext ctx) {
        TerminalNode terminalNode = (TerminalNode) ctx.getChild(0);
        TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalNode);
        return terminalValue;
    }

    @Override
    public FunicValue visitIncrDecrAfterRightPart(FunicParser.IncrDecrAfterRightPartContext ctx) {
        TerminalNode terminalNode = (TerminalNode) ctx.getChild(0);
        TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal(terminalNode);
        return terminalValue;
    }


    @Override
    public FunicValue visitAssignRightPart(FunicParser.AssignRightPartContext ctx) {
        StringBuilder operator = new StringBuilder();
        Object value = null;
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            if (i < count - 1) {
                TerminalNode operatorCtx = (TerminalNode) ctx.getChild(i);
                TerminalFunicValue operatorValue = (TerminalFunicValue) visitTerminal(operatorCtx);
                if (i > 0) {
                    operator.append(";");
                }
                operator.append(operatorValue.getText());
            } else {
                FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
                FunicValue nextValue = visitExpress(nextCtx);
                value = nextValue.get();
            }
        }
        return KeyPairFunicValue.builder()
                .node(ctx)
                .key(operator.toString())
                .value(value)
                .build();
    }

    public FunicValue getOperatorsNode(RuleNode ctx) {
        List<Object> ret = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            TerminalNode operatorCtx = (TerminalNode) ctx.getChild(i);
            TerminalFunicValue operatorValue = (TerminalFunicValue) visitTerminal(operatorCtx);
            ret.add(operatorValue.getText());
        }
        return ListFunicValue.builder()
                .node(ctx)
                .value(ret)
                .build();
    }

    @Override
    public FunicValue visitStaticFieldValue(FunicParser.StaticFieldValueContext ctx) {
        FunicParser.TypeMemberContext memberCtx = (FunicParser.TypeMemberContext) ctx.getChild(0);
        TypeFunicValue memberValue = (TypeFunicValue) visitTypeMember(memberCtx);
        Class<?> type = memberValue.getType();

        TerminalNode nameCtx = (TerminalNode) ctx.getChild(1);
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();

        Object value = resolver.getStaticFieldOrEnum(type, name, this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(value)
                .build();
    }

    public PipelineFunctionFunicValue getStaticFunctionCall(FunicParser.StaticFunctionCallContext ctx) {
        FunicParser.TypeMemberContext memberCtx = (FunicParser.TypeMemberContext) ctx.getChild(0);
        TypeFunicValue memberValue = (TypeFunicValue) visitTypeMember(memberCtx);
        Class<?> type = memberValue.getType();

        TerminalNode nameCtx = (TerminalNode) ctx.getChild(1);
        TerminalFunicValue nameValue = (TerminalFunicValue) visitTerminal(nameCtx);
        String name = nameValue.getText();

        FunicParser.FunctionArgumentsContext argsCtx = (FunicParser.FunctionArgumentsContext) ctx.getChild(2);
        ListKeyPairFunicValue argsValue = (ListKeyPairFunicValue) visitFunctionArguments(argsCtx);
        List<Map.Entry<String, Object>> args = argsValue.getList();

        return PipelineFunctionFunicValue.builder()
                .type(PipelineFunctionFunicValue.Type.STATIC)
                .clazz(type)
                .name(name)
                .args(args)
                .build();
    }

    @Override
    public FunicValue visitStaticFunctionCall(FunicParser.StaticFunctionCallContext ctx) {
        PipelineFunctionFunicValue nextValue = getStaticFunctionCall(ctx);

        Object value = resolver.invokeStaticMethod(
                nextValue.getClazz(),
                nextValue.getName(), nextValue.getArgs(),
                this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitFunctionArguments(FunicParser.FunctionArgumentsContext ctx) {
        List<Map.Entry<String, Object>> list = new ArrayList<>();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.FunctionArgumentContext) {
                FunicParser.FunctionArgumentContext nextCtx = (FunicParser.FunctionArgumentContext) child;
                KeyPairFunicValue nextValue = (KeyPairFunicValue) visitFunctionArgument(nextCtx);
                list.add(new AbstractMap.SimpleEntry<>(nextValue.getKey(), nextValue.getValue()));
            }
        }
        return ListKeyPairFunicValue.builder()
                .node(ctx)
                .value(list)
                .build();
    }

    @Override
    public FunicValue visitFunctionArgument(FunicParser.FunctionArgumentContext ctx) {
        int count = ctx.getChildCount();
        if (count == 1) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(0);
            FunicValue value = visitExpress(nextCtx);
            Object obj = value.get();
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(null)
                    .value(obj)
                    .build();
        } else if (count == 3) {
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal((TerminalNode) ctx.getChild(0));
            String text = terminalValue.getText();

            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(2);
            FunicValue value = visitExpress(nextCtx);
            Object obj = value.get();
            return KeyPairFunicValue.builder()
                    .node(ctx)
                    .key(text)
                    .value(obj)
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support function argument node!");
    }

    @Override
    public FunicValue visitListValueExpress(FunicParser.ListValueExpressContext ctx) {
        int count = ctx.getChildCount();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof FunicParser.UnpackListExpressContext) {
                FunicParser.UnpackListExpressContext nextCtx = (FunicParser.UnpackListExpressContext) child;
                ListFunicValue nextValue = (ListFunicValue) visitUnpackListExpress(nextCtx);
                list.addAll(nextValue.getList());
            }
        }
        return ListFunicValue.builder()
                .node(ctx)
                .value(list)
                .build();
    }

    @Override
    public FunicValue visitUnpackListExpress(FunicParser.UnpackListExpressContext ctx) {
        int count = ctx.getChildCount();
        if (count == 1) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(0);
            FunicValue value = visitExpress(nextCtx);
            Object obj = value.get();
            return ListFunicValue.builder()
                    .node(ctx)
                    .value(new ArrayList<>(Collections.singletonList(obj)))
                    .build();
        } else if (count == 2) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) ctx.getChild(1);
            FunicValue value = visitExpress(nextCtx);
            Object obj = value.get();
            List<Object> list = resolver.unpackList(obj, this);
            return ListFunicValue.builder()
                    .node(ctx)
                    .value(list)
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support value segment node!");
    }

    @Override
    public FunicValue visitFullName(FunicParser.FullNameContext ctx) {
        StringBuilder builder = new StringBuilder();
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i++) {
            TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal((TerminalNode) ctx.getChild(i));
            builder.append(terminalValue.getText());
        }
        return FullNameFunicValue.builder()
                .node(ctx)
                .value(builder.toString())
                .build();
    }

    @Override
    public FunicValue visitTypeClass(FunicParser.TypeClassContext ctx) {
        FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) ctx.getChild(0);
        FullNameFunicValue value = (FullNameFunicValue) visitFullName(nextCtx);
        Class<?> clazz = resolver.findClass(value.getName(), this);
        return TypeFunicValue.builder()
                .node(ctx)
                .value(clazz)
                .build();
    }

    @Override
    public FunicValue visitTypeReference(FunicParser.TypeReferenceContext ctx) {
        FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) ctx.getChild(1);
        FullNameFunicValue value = (FullNameFunicValue) visitFullName(nextCtx);
        Class<?> clazz = resolver.findClass(value.getName(), this);
        return TypeFunicValue.builder()
                .node(ctx)
                .value(clazz)
                .build();
    }

    @Override
    public FunicValue visitTypeMember(FunicParser.TypeMemberContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.TypeClassContext) {
            FunicParser.TypeClassContext nextCtx = (FunicParser.TypeClassContext) child;
            TypeFunicValue value = (TypeFunicValue) visitTypeClass(nextCtx);
            return value;
        } else if (child instanceof FunicParser.TypeReferenceContext) {
            FunicParser.TypeReferenceContext nextCtx = (FunicParser.TypeReferenceContext) child;
            TypeFunicValue value = (TypeFunicValue) visitTypeReference(nextCtx);
            return value;
        } else if (child instanceof FunicParser.FullNameContext) {
            FunicParser.FullNameContext nextCtx = (FunicParser.FullNameContext) child;
            FullNameFunicValue value = (FullNameFunicValue) visitFullName(nextCtx);
            Class<?> clazz = resolver.findClass(value.getName(), this);
            return TypeFunicValue.builder()
                    .node(nextCtx)
                    .value(clazz)
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support value segment node!");

    }

    @Override
    public FunicValue visitValueSegment(FunicParser.ValueSegmentContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.ConstValueContext) {
            FunicParser.ConstValueContext nextCtx = (FunicParser.ConstValueContext) child;
            return visitConstValue(nextCtx);
        } else if (child instanceof FunicParser.VariableValueContext) {
            FunicParser.VariableValueContext nextCtx = (FunicParser.VariableValueContext) child;
            return visitVariableValue(nextCtx);
        } else if (child instanceof FunicParser.TypeClassContext) {
            FunicParser.TypeClassContext nextCtx = (FunicParser.TypeClassContext) child;
            return visitTypeClass(nextCtx);
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support value segment node!");

    }

    @Override
    public FunicValue visitVariableValue(FunicParser.VariableValueContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalFunicValue terminalValue = (TerminalFunicValue) visitTerminal((TerminalNode) child);
        String text = terminalValue.getText();
        Object value = resolver.getFieldValue(context, text, this);
        return ParentFunicValue.builder()
                .node(ctx)
                .parent(context)
                .key(text)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitConstValue(FunicParser.ConstValueContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.ConstMultiStringContext) {
            FunicParser.ConstMultiStringContext nextCtx = (FunicParser.ConstMultiStringContext) child;
            return visitConstMultiString(nextCtx);
        } else if (child instanceof FunicParser.ConstCharSequenceContext) {
            FunicParser.ConstCharSequenceContext nextCtx = (FunicParser.ConstCharSequenceContext) child;
            return visitConstCharSequence(nextCtx);
        } else if (child instanceof FunicParser.ConstNumericContext) {
            FunicParser.ConstNumericContext nextCtx = (FunicParser.ConstNumericContext) child;
            return visitConstNumeric(nextCtx);
        } else if (child instanceof FunicParser.ConstBooleanContext) {
            FunicParser.ConstBooleanContext nextCtx = (FunicParser.ConstBooleanContext) child;
            return visitConstBoolean(nextCtx);
        } else if (child instanceof FunicParser.ConstNullContext) {
            FunicParser.ConstNullContext nextCtx = (FunicParser.ConstNullContext) child;
            return visitConstNull(nextCtx);
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support const value node!");

    }

    @Override
    public FunicValue visitConstCharSequence(FunicParser.ConstCharSequenceContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.ConstRenderStringContext) {
            FunicParser.ConstRenderStringContext nextCtx = (FunicParser.ConstRenderStringContext) child;
            return visitConstRenderString(nextCtx);
        } else if (child instanceof FunicParser.ConstStringContext) {
            FunicParser.ConstStringContext nextCtx = (FunicParser.ConstStringContext) child;
            return visitConstString(nextCtx);
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support char-sequence node!");
    }

    @Override
    public FunicValue visitConstString(FunicParser.ConstStringContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue nextValue = visitTerminal((TerminalNode) child);
        TerminalFunicValue terminalValue = (TerminalFunicValue) nextValue;
        String text = (String) terminalValue.get();
        return ConstStringFunicValue.builder()
                .node(ctx)
                .value(text)
                .build();
    }

    @Override
    public FunicValue visitConstRenderString(FunicParser.ConstRenderStringContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue nextValue = visitTerminal((TerminalNode) child);
        TerminalFunicValue terminalValue = (TerminalFunicValue) nextValue;
        String text = (String) terminalValue.get();
        String value = resolver.renderString(text, this);
        return ConstStringFunicValue.builder()
                .node(ctx)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitConstMultiString(FunicParser.ConstMultiStringContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue nextValue = visitTerminal((TerminalNode) child);
        TerminalFunicValue terminalValue = (TerminalFunicValue) nextValue;
        String text = (String) terminalValue.get();
        String[] lines = text.split("\n");
        List<String> features = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                String trim = lines[i].trim();
                String[] arr = trim.split("\\.");
                for (String item : arr) {
                    item = item.trim();
                    if (item.isEmpty()) {
                        continue;
                    }
                    features.add(item);
                }
            } else if (i == lines.length - 1) {
                // drop
            } else {
                builder.append(lines[i]);
                if (i < lines.length - 1) {
                    builder.append("\n");
                }
            }
        }

        Object value = resolver.multilineString(text, features, this);
        return DefaultFunicValue.builder()
                .node(ctx)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitConstNumeric(FunicParser.ConstNumericContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof FunicParser.ConstFloatContext) {
            FunicParser.ConstFloatContext nextCtx = (FunicParser.ConstFloatContext) child;
            FunicValue value = visitConstFloat(nextCtx);
            return NumericFunicValue.builder()
                    .node(ctx)
                    .value(value)
                    .build();
        } else if (child instanceof FunicParser.ConstNumberContext) {
            FunicParser.ConstNumberContext nextCtx = (FunicParser.ConstNumberContext) child;
            FunicValue value = visitConstNumber(nextCtx);
            return NumericFunicValue.builder()
                    .node(ctx)
                    .value(value.get())
                    .build();
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", ctx, " ") + " un-support numeric node!");
    }

    @Override
    public FunicValue visitConstNumber(FunicParser.ConstNumberContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue value = visitTerminal((TerminalNode) child);
        return NumberFunicValue.builder()
                .node(ctx)
                .value(value.get())
                .build();
    }

    @Override
    public FunicValue visitConstFloat(FunicParser.ConstFloatContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue value = visitTerminal((TerminalNode) child);
        return FloatFunicValue.builder()
                .node(ctx)
                .value(value.get())
                .build();
    }

    @Override
    public FunicValue visitConstBoolean(FunicParser.ConstBooleanContext ctx) {
        ParseTree child = ctx.getChild(0);
        FunicValue value = visitTerminal((TerminalNode) child);
        return BooleanFunicValue.builder()
                .node(ctx)
                .value((boolean) value.get())
                .build();
    }

    @Override
    public FunicValue visitConstNull(FunicParser.ConstNullContext ctx) {
        // ParseTree child = ctx.getChild(0);
        // VisitorValue value = visitTerminal((TerminalNode) child);
        return NullFunicValue.builder()
                .node(ctx)
                .build();
    }

    @Override
    public FunicValue visit(ParseTree tree) {
        if (tree instanceof FunicParser.RootContext) {
            FunicParser.RootContext nextCtx = (FunicParser.RootContext) tree;
            return visitRoot(nextCtx);
        } else if (tree instanceof FunicParser.ScriptContext) {
            FunicParser.ScriptContext nextCtx = (FunicParser.ScriptContext) tree;
            return visitScript(nextCtx);
        } else if (tree instanceof FunicParser.ExpressContext) {
            FunicParser.ExpressContext nextCtx = (FunicParser.ExpressContext) tree;
            return visitExpress(nextCtx);
        }
        throw new FunicEvaluateException(getTreeLocationText("location ", tree, " ") + " un-support visit node!");
    }

    @Override
    public FunicValue visitChildren(RuleNode node) {
        throw new FunicEvaluateException(getTreeLocationText("location ", node, " ") + " current method not supported!");
    }

    @Override
    public FunicValue visitTerminal(TerminalNode terminalNode) {
        Token symbol = terminalNode.getSymbol();
        int symbolType = symbol.getType();
        String symbolText = symbol.getText();
        String terminalText = terminalNode.getText();
        Object value = terminalText;

        if (FunicParser.TERM_CONST_STRING_MULTILINE == symbolType) {
            String text = terminalText.substring(3, terminalText.length() - 3);
            text = text.replace("\\`", "`");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_STRING_MULTILINE_QUOTE == symbolType) {
            String text = terminalText.substring(3, terminalText.length() - 3);
            text = text.replace("\\\"", "\"");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_STRING_RENDER == symbolType) {
            String text = terminalText.substring(2, terminalText.length() - 1);
            text = text.replace("\\\"", "\"");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_STRING_RENDER_SINGLE == symbolType) {
            String text = terminalText.substring(2, terminalText.length() - 1);
            text = text.replace("\\'", "'");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_STRING == symbolType) {
            String text = terminalText.substring(1, terminalText.length() - 1);
            text = text.replace("\\\"", "\"");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_STRING_SINGLE == symbolType) {
            String text = terminalText.substring(1, terminalText.length() - 1);
            text = text.replace("\\'", "'");
            value = unescapeText(text);
        } else if (FunicParser.TERM_CONST_NUMBER_HEX == symbolType) {
            String text = terminalText.substring(2).toLowerCase();
            try {
                if (text.endsWith("l")) {
                    text = text.substring(0, text.length() - 1);
                    value = Long.parseLong(text, 16);
                } else {
                    value = Integer.parseInt(text, 16);
                }
            } catch (Exception e) {
                value = new BigInteger(text, 16);
            }
        } else if (FunicParser.TERM_CONST_NUMBER_OTC == symbolType) {
            String text = terminalText.substring(2).toLowerCase();
            try {
                if (text.endsWith("l")) {
                    text = text.substring(0, text.length() - 1);
                    value = Long.parseLong(text, 8);
                } else {
                    value = Integer.parseInt(text, 8);
                }
            } catch (Exception e) {
                value = new BigInteger(text, 8);
            }
        } else if (FunicParser.TERM_CONST_NUMBER_BIN == symbolType) {
            String text = terminalText.substring(2).toLowerCase();
            try {
                if (text.endsWith("l")) {
                    text = text.substring(0, text.length() - 1);
                    value = Long.parseLong(text, 2);
                } else {
                    value = Integer.parseInt(text, 2);
                }
            } catch (Exception e) {
                value = new BigInteger(text, 2);
            }
        } else if (FunicParser.TERM_CONST_NUMBER_SCIEN_2 == symbolType) {
            String text = terminalText.toLowerCase();
            try {
                if (text.endsWith("f")) {
                    text = text.substring(0, text.length() - 1);
                    value = Float.parseFloat(text);
                } else {
                    value = Double.parseDouble(text);
                }
            } catch (Exception e) {
                value = new BigDecimal(text);
            }
        } else if (FunicParser.TERM_CONST_NUMBER_SCIEN_1 == symbolType) {
            String text = terminalText.toLowerCase();
            try {
                if (text.endsWith("f")) {
                    text = text.substring(0, text.length() - 1);
                    value = Float.parseFloat(text);
                } else {
                    value = Double.parseDouble(text);
                }
            } catch (Exception e) {
                value = new BigDecimal(text);
            }
        } else if (FunicParser.TERM_CONST_NUMBER_FLOAT == symbolType) {
            String text = terminalText.toLowerCase();
            try {
                if (text.endsWith("f")) {
                    text = text.substring(0, text.length() - 1);
                    value = Float.parseFloat(text);
                } else {
                    value = Double.parseDouble(text);
                }
            } catch (Exception e) {
                value = new BigDecimal(text);
            }
        } else if (FunicParser.TERM_CONST_NUMBER == symbolType) {
            String text = terminalText.toLowerCase();
            try {
                if (text.endsWith("l")) {
                    text = text.substring(0, text.length() - 1);
                    value = Long.parseLong(text);
                } else {
                    value = Integer.parseInt(text);
                }
            } catch (Exception e) {
                value = new BigInteger(text);
            }
        } else if (FunicParser.KW_CONST_BOOLEAN == symbolType) {
            value = Boolean.parseBoolean(terminalText);
        } else if (FunicParser.KW_CONST_NULL == symbolType) {
            value = null;
        } else {
            value = terminalText;
        }

        return TerminalFunicValue.builder()
                .node(terminalNode)
                .symbol(symbol)
                .text(terminalText)
                .value(value)
                .build();
    }

    @Override
    public FunicValue visitErrorNode(ErrorNode node) {
        throw new FunicEvaluateException(getTreeLocationText("location ", node, " ") + " unexpect error node found!");
    }

    public String unescapeText(String str) {
        if (str == null) {
            return str;
        }
        String[][] escapeArr = {
                {"\\n", "\n"},
                {"\\r", "\r"},
                {"\\t", "\t"},
                {"\\\\", "\\"}
        };
        for (String[] pair : escapeArr) {
            str = str.replace(pair[0], pair[1]);
        }
        return str;
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
        String text = context.getText();
        if (text.length() > 50) {
            text = text.substring(0, 50) + "...";
        }
        loc += ", near " + text;
        return loc;
    }
}
