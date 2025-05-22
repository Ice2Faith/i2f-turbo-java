package i2f.extension.antlr4.calculator.impl;

import i2f.extension.antlr4.calculator.CalculatorParser;
import i2f.extension.antlr4.calculator.CalculatorVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/1/10 13:57
 */
public class CalculatorVisitorImpl implements CalculatorVisitor<Object> {
    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    public static final BigDecimal CONST_PI = new BigDecimal("3.141592653589793238462643383279");
    public static final BigDecimal CONST_E = new BigDecimal("2.718281828459045235360287471352");
    public static final BigDecimal CONST_100 = new BigDecimal("100");
    public static final BigDecimal CONST_NEG_1 = new BigDecimal("-1");
    public static final BigDecimal CONST_180 = new BigDecimal("180");
    public static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Object visitEval(CalculatorParser.EvalContext ctx) {
        int count = ctx.getChildCount();
        if (count < 1) {
            throw new IllegalArgumentException("missing formula!");
        }
        ParseTree item = ctx.getChild(0);
        if (count > 1) {
            ParseTree equal = ctx.getChild(1);
            if (!(equal instanceof TerminalNode)) {
                throw new IllegalArgumentException("invalid formula ending!");
            }
            TerminalNode nextTerm = (TerminalNode) equal;
            String term = (String) visitTerminal(nextTerm);
            if (!"=".equalsIgnoreCase(term)) {
                throw new IllegalArgumentException("invalid formula ending, expect '=' but found '" + term + "'!");
            }
        }
        if (item instanceof CalculatorParser.ExprContext) {
            CalculatorParser.ExprContext next = (CalculatorParser.ExprContext) item;
            Object ret = visitExpr(next);
            return ret;
        } else if (item instanceof CalculatorParser.NumberContext) {
            CalculatorParser.NumberContext next = (CalculatorParser.NumberContext) item;
            Object ret = visitNumber(next);
            return ret;
        }

        throw new IllegalArgumentException("invalid formula!");
    }

    @Override
    public Object visitNumber(CalculatorParser.NumberContext ctx) {
        int count = ctx.getChildCount();
        ParseTree item = ctx.getChild(0);
        if (item instanceof CalculatorParser.ConstNumberContext) {
            CalculatorParser.ConstNumberContext nextCtx = (CalculatorParser.ConstNumberContext) item;
            return visitConstNumber(nextCtx);
        } else if (item instanceof CalculatorParser.DecNumberContext) {
            CalculatorParser.DecNumberContext nextCtx = (CalculatorParser.DecNumberContext) item;
            return visitDecNumber(nextCtx);
        } else if (item instanceof CalculatorParser.HexNumberContext) {
            CalculatorParser.HexNumberContext nextCtx = (CalculatorParser.HexNumberContext) item;
            return visitHexNumber(nextCtx);
        } else if (item instanceof CalculatorParser.OtcNumberContext) {
            CalculatorParser.OtcNumberContext nextCtx = (CalculatorParser.OtcNumberContext) item;
            return visitOtcNumber(nextCtx);
        } else if (item instanceof CalculatorParser.BinNumberContext) {
            CalculatorParser.BinNumberContext nextCtx = (CalculatorParser.BinNumberContext) item;
            return visitBinNumber(nextCtx);
        } else if (item instanceof CalculatorParser.HighNumberContext) {
            CalculatorParser.HighNumberContext nextCtx = (CalculatorParser.HighNumberContext) item;
            return visitHighNumber(nextCtx);
        }
        throw new IllegalArgumentException("un-support number found : " + ctx.getText());
    }

    @Override
    public Object visitExpr(CalculatorParser.ExprContext ctx) {
        int count = ctx.getChildCount();
        ParseTree item = ctx.getChild(0);
        if (item instanceof CalculatorParser.BracketContext) {
            CalculatorParser.BracketContext next = (CalculatorParser.BracketContext) item;
            Object ret = visitBracket(next);
            return ret;
        } else if (item instanceof CalculatorParser.ConvertorContext) {
            CalculatorParser.ConvertorContext next = (CalculatorParser.ConvertorContext) item;
            Object ret = visitConvertor(next);
            return ret;
        } else if (item instanceof CalculatorParser.NumberContext) {
            CalculatorParser.NumberContext next = (CalculatorParser.NumberContext) item;
            Object ret = visitNumber(next);
            return ret;
        } else if (count == 2) {
            ParseTree sec = ctx.getChild(1);
            if (item instanceof CalculatorParser.ExprContext) {
                CalculatorParser.ExprContext nextExpr = (CalculatorParser.ExprContext) item;
                BigDecimal ret = (BigDecimal) visitExpr(nextExpr);
                if (sec instanceof CalculatorParser.SuffixOperatorContext) {
                    CalculatorParser.SuffixOperatorContext nextSuffix = (CalculatorParser.SuffixOperatorContext) sec;
                    String suffix = (String) visitSuffixOperator(nextSuffix);
                    return resolveSuffixOperator(ret, suffix);
                } else {
                    throw new IllegalArgumentException("bad expression found!");
                }
            } else if (item instanceof CalculatorParser.PrefixOperatorContext) {
                CalculatorParser.PrefixOperatorContext nextPrefix = (CalculatorParser.PrefixOperatorContext) item;
                String prefix = (String) visitPrefixOperator(nextPrefix);
                if (sec instanceof CalculatorParser.ExprContext) {
                    CalculatorParser.ExprContext nextExpr = (CalculatorParser.ExprContext) sec;
                    BigDecimal ret = (BigDecimal) visitExpr(nextExpr);
                    return resolvePrefixOperator(ret, prefix);
                } else {
                    throw new IllegalArgumentException("bad expression found!");
                }
            }
        } else if (count == 3) {
            BigDecimal left = null;
            String oper = null;
            BigDecimal right = null;
            ParseTree sec = ctx.getChild(1);
            ParseTree trd = ctx.getChild(2);
            if (item instanceof CalculatorParser.ExprContext) {
                CalculatorParser.ExprContext nextExpr = (CalculatorParser.ExprContext) item;
                left = (BigDecimal) visitExpr(nextExpr);
            } else {
                throw new IllegalArgumentException("bad expression found!");
            }
            if (trd instanceof CalculatorParser.ExprContext) {
                CalculatorParser.ExprContext nextExpr = (CalculatorParser.ExprContext) trd;
                right = (BigDecimal) visitExpr(nextExpr);
            } else {
                throw new IllegalArgumentException("bad expression found!");
            }

            if (sec instanceof CalculatorParser.OperatorV5Context) {
                CalculatorParser.OperatorV5Context nextOper = (CalculatorParser.OperatorV5Context) sec;
                oper = (String) visitOperatorV5(nextOper);
            } else if (sec instanceof CalculatorParser.OperatorV4Context) {
                CalculatorParser.OperatorV4Context nextOper = (CalculatorParser.OperatorV4Context) sec;
                oper = (String) visitOperatorV4(nextOper);
            } else if (sec instanceof CalculatorParser.OperatorV3Context) {
                CalculatorParser.OperatorV3Context nextOper = (CalculatorParser.OperatorV3Context) sec;
                oper = (String) visitOperatorV3(nextOper);
            } else if (sec instanceof CalculatorParser.OperatorV2Context) {
                CalculatorParser.OperatorV2Context nextOper = (CalculatorParser.OperatorV2Context) sec;
                oper = (String) visitOperatorV2(nextOper);
            } else if (sec instanceof CalculatorParser.OperatorV1Context) {
                CalculatorParser.OperatorV1Context nextOper = (CalculatorParser.OperatorV1Context) sec;
                oper = (String) visitOperatorV1(nextOper);
            } else if (sec instanceof CalculatorParser.OperatorV0Context) {
                CalculatorParser.OperatorV0Context nextOper = (CalculatorParser.OperatorV0Context) sec;
                oper = (String) visitOperatorV0(nextOper);
            } else {
                throw new IllegalArgumentException("bad expression found!");
            }

            return resolveDoubleOperator(left, oper, right);
        }
        throw new IllegalArgumentException("un-support expression found : " + ctx.getText());
    }

    public static BigDecimal resolveDoubleOperator(BigDecimal left, String oper, BigDecimal right) {
        if ("**".equalsIgnoreCase(oper)
                || "muls".equalsIgnoreCase(oper)) {
            BigDecimal min = new BigDecimal(left.min(right).toString(), MATH_CONTEXT);
            BigDecimal max = new BigDecimal(left.max(right).toString(), MATH_CONTEXT);
            BigDecimal ret = BigDecimal.ONE;
            while (min.compareTo(max) < 0) {
                ret = ret.multiply(min, MATH_CONTEXT);
                min = min.add(BigDecimal.ONE, MATH_CONTEXT);
            }
            return ret;
        } else if ("++".equalsIgnoreCase(oper)
                || "adds".equalsIgnoreCase(oper)) {
            BigDecimal min = new BigDecimal(left.min(right).toString(), MATH_CONTEXT);
            BigDecimal max = new BigDecimal(left.max(right).toString(), MATH_CONTEXT);
            BigDecimal ret = BigDecimal.ZERO;
            while (min.compareTo(max) < 0) {
                ret = ret.add(min, MATH_CONTEXT);
                min = min.add(BigDecimal.ONE, MATH_CONTEXT);
            }
            return ret;
        } else if ("&".equalsIgnoreCase(oper)
                || "and".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl & vr);
        } else if ("|".equalsIgnoreCase(oper)
                || "or".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl | vr);
        } else if ("xor".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl ^ vr);
        } else if ("<<".equalsIgnoreCase(oper)
                || "lmov".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl << vr);
        } else if (">>".equalsIgnoreCase(oper)
                || "rmov".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl >> vr);
        } else if (">>>".equalsIgnoreCase(oper)
                || "srmov".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            return BigDecimal.valueOf(vl >>> vr);
        } else if ("log".equalsIgnoreCase(oper)) {
            double vl = left.doubleValue();
            double vr = right.doubleValue();
            return BigDecimal.valueOf(Math.log(vr)).divide(BigDecimal.valueOf(Math.log(vl)), MATH_CONTEXT);
        } else if ("^".equalsIgnoreCase(oper)
                || "pow".equalsIgnoreCase(oper)) {
            double vl = left.doubleValue();
            double vr = right.doubleValue();
            return BigDecimal.valueOf(Math.pow(vl, vr));
        } else if ("//".equalsIgnoreCase(oper)) {
            return left.divide(right, MATH_CONTEXT).setScale(0, RoundingMode.FLOOR);
        } else if ("*".equalsIgnoreCase(oper)
                || "mul".equalsIgnoreCase(oper)) {
            return left.multiply(right, MATH_CONTEXT);
        } else if ("/".equalsIgnoreCase(oper)
                || "div".equalsIgnoreCase(oper)) {
            return left.divide(right, MATH_CONTEXT);
        } else if ("%".equalsIgnoreCase(oper)
                || "mod".equalsIgnoreCase(oper)) {
            long vl = left.longValue();
            long vr = right.longValue();
            boolean isNeg = vl < 0;
            vl = Math.abs(vl);
            vr = Math.abs(vr);
            long num = vl % vr;
            return BigDecimal.valueOf(isNeg ? -num : num);
        } else if ("+".equalsIgnoreCase(oper)
                || "add".equalsIgnoreCase(oper)) {
            return left.add(right, MATH_CONTEXT);
        } else if ("-".equalsIgnoreCase(oper)
                || "sub".equalsIgnoreCase(oper)) {
            return left.subtract(right, MATH_CONTEXT);
        } else if (">=".equalsIgnoreCase(oper)
                || "gte".equalsIgnoreCase(oper)) {
            return left.compareTo(right) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        } else if ("<=".equalsIgnoreCase(oper)
                || "lte".equalsIgnoreCase(oper)) {
            return left.compareTo(right) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        } else if ("!=".equalsIgnoreCase(oper)
                || "neq".equalsIgnoreCase(oper)
                || "<>".equalsIgnoreCase(oper)
                || "ne".equalsIgnoreCase(oper)) {
            return left.compareTo(right) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        } else if ("==".equalsIgnoreCase(oper)
                || "eq".equalsIgnoreCase(oper)) {
            return left.compareTo(right) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        } else if (">".equalsIgnoreCase(oper)
                || "gt".equalsIgnoreCase(oper)) {
            return left.compareTo(right) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        } else if ("<".equalsIgnoreCase(oper)
                || "lt".equalsIgnoreCase(oper)) {
            return left.compareTo(right) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        throw new IllegalArgumentException("un-support double operator found : " + oper);
    }

    public static BigDecimal resolvePrefixOperator(BigDecimal ret, String prefix) {
        if ("+".equalsIgnoreCase(prefix)) {
            return ret;
        } else if ("-".equalsIgnoreCase(prefix)
                || "neg".equalsIgnoreCase(prefix)) {
            return ret.multiply(CONST_NEG_1, MATH_CONTEXT);
        } else if ("abs".equalsIgnoreCase(prefix)) {
            return ret.abs(MATH_CONTEXT);
        } else if ("ln".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.log(num);
            return BigDecimal.valueOf(num);
        } else if ("sin".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.sin(num);
            return BigDecimal.valueOf(num);
        } else if ("cos".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.cos(num);
            return BigDecimal.valueOf(num);
        } else if ("tan".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.tan(num);
            return BigDecimal.valueOf(num);
        } else if ("arcsin".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.asin(num);
            return BigDecimal.valueOf(num);
        } else if ("arccos".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.acos(num);
            return BigDecimal.valueOf(num);
        } else if ("arctan".equalsIgnoreCase(prefix)) {
            double num = ret.doubleValue();
            num = Math.atan(num);
            return BigDecimal.valueOf(num);
        } else if ("angle".equalsIgnoreCase(prefix)) {
            return ret.divide(CONST_PI, MATH_CONTEXT).multiply(CONST_180, MATH_CONTEXT);
        } else if ("radian".equalsIgnoreCase(prefix)) {
            return ret.divide(CONST_180, MATH_CONTEXT).multiply(CONST_PI, MATH_CONTEXT);
        } else if ("floor".equalsIgnoreCase(prefix)) {
            return ret.setScale(0, RoundingMode.FLOOR);
        } else if ("round".equalsIgnoreCase(prefix)) {
            return ret.setScale(0, RoundingMode.HALF_UP);
        } else if ("ceil".equalsIgnoreCase(prefix)) {
            return ret.setScale(0, RoundingMode.CEILING);
        } else if ("rand".equalsIgnoreCase(prefix)) {
            long num = ret.longValue();
            boolean isNeg = num < 0;
            num = Math.abs(num);
            num = RANDOM.nextLong() % num;
            return BigDecimal.valueOf(isNeg ? -num : num);
        } else if ("feibo".equalsIgnoreCase(prefix)) {
            return calcFeibo(ret);
        } else if ("~".equalsIgnoreCase(prefix)
                || "not".equalsIgnoreCase(prefix)) {
            long num = ret.longValue();
            return BigDecimal.valueOf(~num);
        } else if ("sqrt".equalsIgnoreCase(prefix)) {
            return BigDecimal.valueOf(Math.sqrt(ret.doubleValue()));
//            return ret.sqrt(MATH_CONTEXT);
        }
        throw new IllegalArgumentException("un-support prefix operator found : " + prefix);
    }

    public static BigDecimal resolveSuffixOperator(BigDecimal ret, String suffix) {
        if ("!".equalsIgnoreCase(suffix)) {
            return calcFactorial(ret);
        } else if ("%%".equalsIgnoreCase(suffix)
                || "per".equalsIgnoreCase(suffix)) {
            return ret.divide(CONST_100, MATH_CONTEXT);
        }
        throw new IllegalArgumentException("un-support suffix operator found : " + suffix);
    }

    protected static final long[] FEIBO_RESULT = new long[100];

    public static long getFeiboNumber(long num) {
        if (num < 0) {
            return -1;
        }
        if (num < 100) {
            long val = FEIBO_RESULT[(int) num];
            if (val > 0) {
                return val;
            }
        }
        long val = -1;
        if (num == 0 || num == 1) {
            val = 1;
        } else {
            val = getFeiboNumber(num - 1) + getFeiboNumber(num - 2);
        }
        if (val > 0) {
            if (num < 100) {
                FEIBO_RESULT[(int) num] = val;
            }
        }
        return val;
    }

    public static BigDecimal calcFeibo(BigDecimal ret) {
        long num = ret.longValue();
        num = getFeiboNumber(num);
        return BigDecimal.valueOf(num);
    }

    public static BigDecimal calcFactorial(BigDecimal ret) {
        long num = ret.longValue();
        boolean isNeg = num < 0;
        num = Math.abs(num);
        BigDecimal val = new BigDecimal(1);
        for (long i = 0; i < num; i++) {
            val = val.multiply(BigDecimal.valueOf(i), MATH_CONTEXT);
        }
        return isNeg ? val.multiply(CONST_NEG_1, MATH_CONTEXT) : val;
    }

    @Override
    public Object visitBracket(CalculatorParser.BracketContext ctx) {
        ParseTree item = ctx.getChild(1);
        if (item instanceof CalculatorParser.ExprContext) {
            CalculatorParser.ExprContext nextCtx = (CalculatorParser.ExprContext) item;
            return visitExpr(nextCtx);
        }
        throw new IllegalArgumentException("invalid bracket expression: " + ctx.getText());
    }

    @Override
    public Object visitConvertor(CalculatorParser.ConvertorContext ctx) {
        int count = ctx.getChildCount();
        ParseTree item = ctx.getChild(0);
        if (item instanceof TerminalNode) {
            TerminalNode nextNode = (TerminalNode) item;
            String name = (String) visitTerminal(nextNode);
            List<BigDecimal> numbers = new ArrayList<>();
            for (int i = 2; i < count - 1; i += 2) {
                ParseTree node = ctx.getChild(i);
                if (node instanceof CalculatorParser.ExprContext) {
                    CalculatorParser.ExprContext nextExpr = (CalculatorParser.ExprContext) node;
                    BigDecimal num = (BigDecimal) visitExpr(nextExpr);
                    numbers.add(num);
                }
            }
            return resolveConvertor(name, numbers);
        }
        throw new IllegalArgumentException("invalid convertor found: " + ctx.getText());
    }

    public static BigDecimal resolveConvertor(String name, List<BigDecimal> numbers) {
        if ("min".equalsIgnoreCase(name)) {
            if (numbers.isEmpty()) {
                throw new IllegalArgumentException("min convertor require least one argument!");
            }
            BigDecimal ret = numbers.get(0);
            for (BigDecimal item : numbers) {
                if (ret.compareTo(item) > 0) {
                    ret = item;
                }
            }
            return ret;
        } else if ("max".equalsIgnoreCase(name)) {
            if (numbers.isEmpty()) {
                throw new IllegalArgumentException("max convertor require least one argument!");
            }
            BigDecimal ret = numbers.get(0);
            for (BigDecimal item : numbers) {
                if (ret.compareTo(item) < 0) {
                    ret = item;
                }
            }
            return ret;
        } else if ("sum".equalsIgnoreCase(name)) {
            BigDecimal sum = BigDecimal.ZERO;
            for (BigDecimal item : numbers) {
                sum = sum.add(item, MATH_CONTEXT);
            }
            return sum;
        } else if ("avg".equalsIgnoreCase(name)) {
            BigDecimal sum = BigDecimal.ZERO;
            int count = 0;
            for (BigDecimal item : numbers) {
                sum = sum.add(item, MATH_CONTEXT);
                count++;
            }
            return sum.divide(BigDecimal.valueOf(count), MATH_CONTEXT);
        }
        throw new IllegalArgumentException("un-support convertor found: " + name);
    }

    @Override
    public Object visitPrefixOperator(CalculatorParser.PrefixOperatorContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitSuffixOperator(CalculatorParser.SuffixOperatorContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV5(CalculatorParser.OperatorV5Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV4(CalculatorParser.OperatorV4Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV3(CalculatorParser.OperatorV3Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV2(CalculatorParser.OperatorV2Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV1(CalculatorParser.OperatorV1Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitOperatorV0(CalculatorParser.OperatorV0Context ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitConstNumber(CalculatorParser.ConstNumberContext ctx) {
        ParseTree item = ctx.getChild(0);
        if (item instanceof TerminalNode) {
            TerminalNode nextNode = (TerminalNode) item;
            String oper = (String) visitTerminal(nextNode);
            return resolveConstNumber(oper);
        }
        throw new IllegalArgumentException("invalid const number found: " + ctx.getText());
    }

    public BigDecimal resolveConstNumber(String oper) {
        if ("pi".equalsIgnoreCase(oper)) {
            return CONST_PI;
        } else if ("e".equalsIgnoreCase(oper)) {
            return CONST_E;
        } else if ("randf".equalsIgnoreCase(oper)) {
            return BigDecimal.valueOf(RANDOM.nextDouble());
        }
        throw new IllegalArgumentException("invalid const number found: " + oper);
    }

    @Override
    public Object visitDecNumber(CalculatorParser.DecNumberContext ctx) {
        String text = ctx.getText().replaceAll("\\_", "");
        return new BigDecimal(text);
    }

    @Override
    public Object visitHexNumber(CalculatorParser.HexNumberContext ctx) {
        String text = ctx.getText().replaceAll("\\_", "");
        String hex = text.substring(2);
        return BigDecimal.valueOf(Long.parseLong(hex, 16));
    }

    @Override
    public Object visitOtcNumber(CalculatorParser.OtcNumberContext ctx) {
        String text = ctx.getText().replaceAll("\\_", "");
        String hex = text.substring(2);
        return BigDecimal.valueOf(Long.parseLong(hex, 8));
    }

    @Override
    public Object visitBinNumber(CalculatorParser.BinNumberContext ctx) {
        String text = ctx.getText().replaceAll("\\_", "");
        String hex = text.substring(2);
        return BigDecimal.valueOf(Long.parseLong(hex, 2));
    }

    @Override
    public Object visitHighNumber(CalculatorParser.HighNumberContext ctx) {
        String text = ctx.getText().replaceAll("\\_", "");
        String pair = text.substring(2);
        String[] arr = pair.split(":");
        int radix = Integer.parseInt(arr[0]);
        String hex = arr[1];
        return BigDecimal.valueOf(Long.parseLong(hex, radix));
    }

    @Override
    public Object visit(ParseTree tree) {
        if (tree instanceof CalculatorParser.EvalContext) {
            CalculatorParser.EvalContext nextCtx = (CalculatorParser.EvalContext) tree;
            return visitEval(nextCtx);
        } else if (tree instanceof CalculatorParser.ExprContext) {
            CalculatorParser.ExprContext nextCtx = (CalculatorParser.ExprContext) tree;
            return visitExpr(nextCtx);
        } else if (tree instanceof CalculatorParser.NumberContext) {
            CalculatorParser.NumberContext nextCtx = (CalculatorParser.NumberContext) tree;
            return visitNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.ConstNumberContext) {
            CalculatorParser.ConstNumberContext nextCtx = (CalculatorParser.ConstNumberContext) tree;
            return visitConstNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.DecNumberContext) {
            CalculatorParser.DecNumberContext nextCtx = (CalculatorParser.DecNumberContext) tree;
            return visitDecNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.HexNumberContext) {
            CalculatorParser.HexNumberContext nextCtx = (CalculatorParser.HexNumberContext) tree;
            return visitHexNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.OtcNumberContext) {
            CalculatorParser.OtcNumberContext nextCtx = (CalculatorParser.OtcNumberContext) tree;
            return visitOtcNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.BinNumberContext) {
            CalculatorParser.BinNumberContext nextCtx = (CalculatorParser.BinNumberContext) tree;
            return visitBinNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.HighNumberContext) {
            CalculatorParser.HighNumberContext nextCtx = (CalculatorParser.HighNumberContext) tree;
            return visitHighNumber(nextCtx);
        } else if (tree instanceof CalculatorParser.BracketContext) {
            CalculatorParser.BracketContext nextCtx = (CalculatorParser.BracketContext) tree;
            return visitBracket(nextCtx);
        } else if (tree instanceof CalculatorParser.ConvertorContext) {
            CalculatorParser.ConvertorContext nextCtx = (CalculatorParser.ConvertorContext) tree;
            return visitConvertor(nextCtx);
        } else if (tree instanceof CalculatorParser.SuffixOperatorContext) {
            CalculatorParser.SuffixOperatorContext nextCtx = (CalculatorParser.SuffixOperatorContext) tree;
            return visitSuffixOperator(nextCtx);
        } else if (tree instanceof CalculatorParser.PrefixOperatorContext) {
            CalculatorParser.PrefixOperatorContext nextCtx = (CalculatorParser.PrefixOperatorContext) tree;
            return visitPrefixOperator(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV5Context) {
            CalculatorParser.OperatorV5Context nextCtx = (CalculatorParser.OperatorV5Context) tree;
            return visitOperatorV5(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV4Context) {
            CalculatorParser.OperatorV4Context nextCtx = (CalculatorParser.OperatorV4Context) tree;
            return visitOperatorV4(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV3Context) {
            CalculatorParser.OperatorV3Context nextCtx = (CalculatorParser.OperatorV3Context) tree;
            return visitOperatorV3(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV2Context) {
            CalculatorParser.OperatorV2Context nextCtx = (CalculatorParser.OperatorV2Context) tree;
            return visitOperatorV2(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV1Context) {
            CalculatorParser.OperatorV1Context nextCtx = (CalculatorParser.OperatorV1Context) tree;
            return visitOperatorV1(nextCtx);
        } else if (tree instanceof CalculatorParser.OperatorV0Context) {
            CalculatorParser.OperatorV0Context nextCtx = (CalculatorParser.OperatorV0Context) tree;
            return visitOperatorV0(nextCtx);
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
}
