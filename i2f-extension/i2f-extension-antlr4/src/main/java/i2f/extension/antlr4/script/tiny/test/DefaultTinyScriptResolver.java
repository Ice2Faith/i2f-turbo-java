package i2f.extension.antlr4.script.tiny.test;

import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public class DefaultTinyScriptResolver implements TinyScriptResolver {
    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);

    @Override
    public void setValue(Map<String,Object> context, String name, Object value) {
        Visitor visitor = Visitor.visit(name, context);
        visitor.set(value);
    }

    @Override
    public Object getValue(Map<String,Object> context, String name) {
        Visitor visitor = Visitor.visit(name, context);
        return visitor.get();
    }

    @Override
    public Object resolveDoubleOperator(Object left, String operator, Object right) {
        if ("&&".equals(operator) || "and".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left);
            boolean br = ObjectConvertor.toBoolean(right);
            return bl&&br;
        }else if ("||".equals(operator) || "or".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left);
            boolean br = ObjectConvertor.toBoolean(right);
            return bl||br;
        }else if (">=".equals(operator) || "gte".equals(operator)) {
            int num = compare(left, right);
            return num >= 0;
        } else if ("<=".equals(operator) || "lte".equals(operator)) {
            int num = compare(left, right);
            return num <= 0;
        } else if ("<>".equals(operator) || "neq".equals(operator)
                || "!=".equals(operator) || "ne".equals(operator)) {
            int num = compare(left, right);
            return num != 0;
        } else if (">".equals(operator) || "gt".equals(operator)) {
            int num = compare(left, right);
            return num > 0;
        } else if ("<".equals(operator) || "lt".equals(operator)) {
            int num = compare(left, right);
            return num < 0;
        } else if ("==".equals(operator) || "eq".equals(operator)) {
            int num = compare(left, right);
            return num == 0;
        } else if ("+".equals(operator)) {
            if (left instanceof CharSequence) {
                return left + "" + right;
            } else if (left instanceof Appendable) {
                return left + "" + right;
            } else if (right instanceof CharSequence) {
                return left + "" + right;
            } else if (right instanceof Appendable) {
                return left + "" + right;
            } else if (left instanceof Number && right instanceof Number) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.add(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("-".equals(operator)) {
            if (left instanceof Number && right instanceof Number) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.subtract(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("*".equals(operator)) {
            if (left instanceof Number && right instanceof Number) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.multiply(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("/".equals(operator)) {
            if (left instanceof Number && right instanceof Number) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.divide(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("%".equals(operator)) {
            if (left instanceof Number && right instanceof Number) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                long num = lv.longValue() % rv.longValue();
                return num;
            }
        }
        throw new IllegalArgumentException("un-support operator :" + operator);
    }

    @Override
    public Object resolvePrefixOperator(String operator, Object value) {
        if ("!".equals(operator) || "not".equals(operator)) {
            boolean bv = ObjectConvertor.toBoolean(value);
            return !bv;
        }
        throw new IllegalArgumentException("un-support prefix operator :" + operator);
    }

    public Object convertNumberType(BigDecimal num, Number left, Number right) {
        if(TypeOf.instanceOf(left, BigDecimal.class)
                ||TypeOf.instanceOf(right,BigDecimal.class)){
            return num;
        }
        if(TypeOf.instanceOf(left, BigInteger.class)
                ||TypeOf.instanceOf(right,BigDecimal.class)){
            return num.toBigInteger();
        }
        if(TypeOf.instanceOf(left,Double.class)
                ||TypeOf.instanceOf(right,Double.class)){
            return num.doubleValue();
        }
        if(TypeOf.instanceOf(left,Float.class)
                ||TypeOf.instanceOf(right,Float.class)){
            return num.floatValue();
        }
        if(TypeOf.instanceOf(left,Long.class)
                ||TypeOf.instanceOf(right,Long.class)){
            return num.longValue();
        }
        return num.intValue();
    }

    public int compare(Object left, Object right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        if (left.equals(right)) {
            return 0;
        }
        if (left.getClass().equals(right.getClass())) {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
        }
        if (left instanceof Comparable<?>) {
            return ((Comparable) left).compareTo(right);
        }
        if (right instanceof Comparable<?>) {
            int ret = ((Comparable) right).compareTo(left);
            if (ret == 0) {
                return 0;
            }
            if (ret > 0) {
                return -1;
            }
            return 1;
        }
        return Integer.compare(left.hashCode(), right.hashCode());
    }

    @Override
    public Object resolveFunctionCall(Object target, boolean isNew, String naming, List<Object> args) {
        Class<?> clazz = null;
        if (isNew) {
            clazz = ReflectResolver.loadClass(naming);
        } else {
            if (target == null) {
                int idx = naming.lastIndexOf(".");
                if (idx > 0) {
                    String className = naming.substring(0, idx);
                    naming = naming.substring(idx + 1);
                    clazz = ReflectResolver.loadClass(className);
                }
            } else {
                clazz = target.getClass();
            }
        }
        if (isNew) {
            Object ret = ReflectResolver.execNewInstance(clazz, args);
            return ret;
        } else {
            String methodName = naming;

            Object ret = ReflectResolver.execMethod(target, clazz, methodName, args);
            return ret;
        }
    }
}
