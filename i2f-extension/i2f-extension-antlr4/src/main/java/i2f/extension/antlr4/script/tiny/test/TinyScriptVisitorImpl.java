package i2f.extension.antlr4.script.tiny.test;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/20 21:03
 * @desc
 */
public class TinyScriptVisitorImpl implements TinyScriptVisitor<Object> {
    protected Map<String, Object> context = new HashMap<>();
    protected TinyScriptResolver resolver=new DefaultTinyScriptResolver();

    public TinyScriptVisitorImpl(Map<String, Object> context) {
        this.context = context;
    }

    public TinyScriptVisitorImpl(Map<String, Object> context, TinyScriptResolver resolver) {
        this.context = context;
        this.resolver = resolver;
    }

    @Override
    public Object visitScript(TinyScriptParser.ScriptContext ctx) {
        int count = ctx.getChildCount();
        if (count < 1) {
            throw new IllegalArgumentException("missing script!");
        }
        Object ret = null;
        for (int i = 0; i < count; i++) {
            ParseTree item = ctx.getChild(i);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (!";".equals(term)) {
                    throw new IllegalArgumentException("invalid grammar separator, expect ';' buf found '" + term + "'!");
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
    }

    @Override
    public Object visitExpress(TinyScriptParser.ExpressContext ctx) {
        int count = ctx.getChildCount();
        if (count < 1) {
            throw new IllegalArgumentException("missing express!");
        }
        ParseTree item = ctx.getChild(0);
        if (item instanceof TinyScriptParser.ExpressContext) {
            if (count == 3) {
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
                Object leftValue = visitExpress(leftCtx);
                String operator = (String) visitTerminal(operatorCtx);
                Object rightValue = visitExpress(rightCtx);
                return resolver.resolveDoubleOperator(leftValue, operator, rightValue);
            } else {
                throw new IllegalArgumentException("invalid grammar express expect 3 parts for double operator, but found " + count + "!");
            }
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
        }
        throw new IllegalArgumentException("un-support express found : " + ctx.getText());
    }

    @Override
    public Object visitEqualValue(TinyScriptParser.EqualValueContext ctx) {
        int count = ctx.getChildCount();
        if (count != 3) {
            throw new IllegalArgumentException("missing equal value, expect 3 parts, but found " + count + "!");
        }
        ParseTree namingNode = ctx.getChild(0);
        ParseTree equalNode = ctx.getChild(1);
        ParseTree expressNode = ctx.getChild(2);
        if (!(namingNode instanceof TerminalNode)) {
            throw new IllegalArgumentException("invalid equal value, expect naming node, but found " + namingNode.getClass() + "!");
        }
        if (!(equalNode instanceof TerminalNode)) {
            throw new IllegalArgumentException("invalid equal value, expect equal node, but found " + namingNode.getClass() + "!");
        }
        if (!(expressNode instanceof TinyScriptParser.ExpressContext)) {
            throw new IllegalArgumentException("invalid equal value, expect express node, but found " + namingNode.getClass() + "!");
        }
        TerminalNode namingCtx = (TerminalNode) namingNode;
        TerminalNode equalCtx = (TerminalNode) equalNode;
        TinyScriptParser.ExpressContext expressCtx = (TinyScriptParser.ExpressContext) expressNode;
        String naming = (String) visitTerminal(namingCtx);
        String equal = (String) visitTerminal(equalCtx);
        if (!"=".equals(equal)) {
            throw new IllegalArgumentException("invalid equal value, expect '=', but found '" + equal + "'!");
        }
        if (naming == null || naming.isEmpty()) {
            throw new IllegalArgumentException("invalid equal value, expect naming, but found '" + naming + "'!");
        }
        Object value = visitExpress(expressCtx);
        resolver.setValue(context,naming, value);
        return value;
    }

    @Override
    public Object visitNewInstance(TinyScriptParser.NewInstanceContext ctx) {
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
    }

    @Override
    public Object visitInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx) {
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
                    throw new IllegalArgumentException("invalid invoke function separator, expect '.' buf found '" + term + "'!");
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
    }

    @Override
    public Object visitFunctionCall(TinyScriptParser.FunctionCallContext ctx) {
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

        return resolver.resolveFunctionCall(value, isNew, naming, args);
    }

    @Override
    public Object visitRefCall(TinyScriptParser.RefCallContext ctx) {
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
    }

    @Override
    public Object visitArgumentList(TinyScriptParser.ArgumentListContext ctx) {
        int count = ctx.getChildCount();
        List<Object> ret = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ParseTree item = ctx.getChild(i);
            if (item instanceof TerminalNode) {
                TerminalNode nextTerm = (TerminalNode) item;
                String term = (String) visitTerminal(nextTerm);
                if (!",".equals(term)) {
                    throw new IllegalArgumentException("invalid argument separator, expect ',' buf found '" + term + "'!");
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
    }

    @Override
    public Object visitArgument(TinyScriptParser.ArgumentContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
            throw new IllegalArgumentException("missing argument!");
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
        }
        throw new IllegalArgumentException("un-support argument found : " + ctx.getText());
    }

    @Override
    public Object visitConstValue(TinyScriptParser.ConstValueContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
            throw new IllegalArgumentException("missing const value!");
        }
        ParseTree item = ctx.getChild(0);
        if (item instanceof TinyScriptParser.ConstBoolContext) {
            TinyScriptParser.ConstBoolContext nextCtx = (TinyScriptParser.ConstBoolContext) item;
            return visitConstBool(nextCtx);
        } else if (item instanceof TinyScriptParser.ConstStringContext) {
            TinyScriptParser.ConstStringContext nextCtx = (TinyScriptParser.ConstStringContext) item;
            return visitConstString(nextCtx);
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
    }

    @Override
    public Object visitRefValue(TinyScriptParser.RefValueContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
            term = term.substring("${".length(), term.length() - "}".length());
            term = term.trim();
            Object value = resolver.getValue(context,term);
            return value;
        }
        throw new IllegalArgumentException("un-support ref value found : " + ctx.getText());
    }

    @Override
    public Object visitConstBool(TinyScriptParser.ConstBoolContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
    }

    @Override
    public Object visitConstString(TinyScriptParser.ConstStringContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
            term = term.replace("\\r", "\r");
            term = term.replace("\\n", "\n");
            term = term.replace("\\t", "\t");
            term = term.replace("\\\\", "\\");
            term = term.replace("\\\"", "\"");
            term = term.replace("\\'", "'");
            return term;
        }
        throw new IllegalArgumentException("un-support const string found : " + ctx.getText());
    }

    @Override
    public Object visitDecNumber(TinyScriptParser.DecNumberContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
    }

    @Override
    public Object visitHexNumber(TinyScriptParser.HexNumberContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
    }

    @Override
    public Object visitOtcNumber(TinyScriptParser.OtcNumberContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
    }

    @Override
    public Object visitBinNumber(TinyScriptParser.BinNumberContext ctx) {
        int count = ctx.getChildCount();
        if (count < 0) {
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
    }

    @Override
    public Object visit(ParseTree tree) {
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
        }
        throw new IllegalArgumentException("un-support visit type: " + tree.getClass().getSimpleName());
    }

    @Override
    public Object visitChildren(RuleNode node) {
        return null;
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        return node.getText();
    }

    @Override
    public Object visitErrorNode(ErrorNode node) {
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
