package i2f.extension.antlr4.script.tiny.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.match.regex.RegexUtil;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public class DefaultTinyScriptResolver implements TinyScriptResolver {
    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    public static final BigDecimal NUM_100=new BigDecimal("100");

    public static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
    protected final AtomicBoolean debug = new AtomicBoolean(true);

    @Override
    public void debug(boolean enable) {
        this.debug.set(enable);
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
        if (debug.get()) {
            System.out.println(String.format("%s [%5s] [%15s] : %s",
                    LOG_TIME_FORMATTER.format(LocalDateTime.now()),
                    "DEBUG",
                    "tiny-script",
                    String.valueOf(supplier.get())
            ));
        }
    }

    @Override
    public void openDebugger(String tag, Object context, String conditionExpression) {
        if (debug.get()) {
            System.out.println("debugger [" + tag + "] [" + conditionExpression + "] wait for input line to continue.");
            System.out.println("continue.");
        }
    }

    @Override
    public void setValue(Object context, String name, Object value) {
        Visitor visitor = Visitor.visit(name, context);
        visitor.set(value);
    }

    @Override
    public Object getValue(Object context, String name) {
        Visitor visitor = Visitor.visit(name, context);
        return visitor.get();
    }

    @Override
    public boolean toBoolean(Object ret) {
        return ObjectConvertor.toBoolean(ret);
    }

    @Override
    public Object resolveDoubleOperator(Supplier<Object> left, String operator, Supplier<Object> right) {
        if ("&&".equals(operator) || "and".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left);
            if (!bl) {
                return bl;
            }
            boolean br = ObjectConvertor.toBoolean(right);
            return br;
        } else if ("||".equals(operator) || "or".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left);
            if (bl) {
                return bl;
            }
            boolean br = ObjectConvertor.toBoolean(right);
            return br;
        } else {
            return resolveDoubleOperator(left.get(), operator, right.get());
        }
    }

    @Override
    public Object resolveDoubleOperator(Object left, String operator, Object right) {
        if ("as".equals(operator) || "cast".equals(operator)) {
            if (right == null) {
                throw new ClassCastException("target type is null");
            }
            if (left == null) {
                return null;
            }
            Class<?> type = right.getClass();
            if (right instanceof Class) {
                type = (Class<?>) right;
            }
            Object ret = ObjectConvertor.tryConvertAsType(left, type);
            if (ret != left) {
                return ret;
            }
            if (!TypeOf.instanceOf(ret, type)) {
                throw new ClassCastException("Cannot cast " + (left == null ? null : left.getClass()) + " to " + type);
            }
        } else if ("is".equals(operator) || "instanceof".equals(operator) || "typeof".equals(operator)) {
            if (left == null) {
                return false;
            }
            if (right == null) {
                return false;
            }
            Class<?> clsLeft = left.getClass();
            Class<?> clsRight = right.getClass();
            if (left instanceof Class) {
                clsLeft = (Class<?>) left;
            }
            if (right instanceof Class) {
                clsRight = (Class<?>) right;
            }
            return TypeOf.typeOf(clsLeft, clsRight);
        } else if ("in".equals(operator) || "notin".equals(operator)) {
            if (right == null) {
                return false;
            }
            if (right instanceof Iterable) {
                Iterable<?> iterable = (Iterable<?>) right;
                boolean isIn = false;
                for (Object item : iterable) {
                    int cmp = compare(left, item);
                    if (cmp == 0) {
                        isIn = true;
                        break;
                    }
                }
                if ("in".equals(operator)) {
                    return isIn;
                }
                if ("notin".equals(operator)) {
                    return !isIn;
                }
            }
            if (right instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) right;
                boolean isIn = false;
                for (Object item : map.keySet()) {
                    int cmp = compare(left, item);
                    if (cmp == 0) {
                        isIn = true;
                        break;
                    }
                }
                if ("in".equals(operator)) {
                    return isIn;
                }
                if ("notin".equals(operator)) {
                    return !isIn;
                }
            } else if (right.getClass().isArray()) {
                boolean isIn = false;
                int len = Array.getLength(right);
                for (int i = 0; i < len; i++) {
                    Object item = Array.get(right, i);
                    int cmp = compare(left, item);
                    if (cmp == 0) {
                        isIn = true;
                        break;
                    }
                }
                if ("in".equals(operator)) {
                    return isIn;
                }
                if ("notin".equals(operator)) {
                    return !isIn;
                }
            } else {
                boolean isIn = false;
                Object item = right;
                int cmp = compare(left, item);
                if (cmp == 0) {
                    isIn = true;
                }
                if ("in".equals(operator)) {
                    return isIn;
                }
                if ("notin".equals(operator)) {
                    return !isIn;
                }
            }
            return false;
        } else if (">=".equals(operator) || "gte".equals(operator)) {
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
            } else if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.add(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("-".equals(operator)) {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.subtract(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("*".equals(operator)) {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.multiply(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("/".equals(operator)) {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.divide(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
        } else if ("%".equals(operator)) {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
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
        if("-".equals(operator)){
            if (value != null) {
                if (ObjectConvertor.isNumericType(value.getClass())) {
                    BigDecimal lv = new BigDecimal(String.valueOf(value));
                    BigDecimal ret = lv.negate();
                    return convertNumberType(ret, (Number) ret, (Number) ret);
                }
            }
        }
        throw new IllegalArgumentException("un-support prefix operator :" + operator);
    }

    @Override
    public Object resolveSuffixOperator(Object value, String operator) {
        if("%".equals(operator)){
            if (value != null) {
                if (ObjectConvertor.isNumericType(value.getClass())) {
                    BigDecimal lv = new BigDecimal(String.valueOf(value));
                    BigDecimal ret = lv.divide(NUM_100,MATH_CONTEXT);
                    return convertNumberType(ret, (Number) ret, (Number) ret);
                }
            }
        }
        throw new IllegalArgumentException("un-support prefix operator :" + operator);
    }

    public Object convertNumberType(BigDecimal num, Number left, Number right) {
        if (TypeOf.instanceOf(left, BigDecimal.class)
                || TypeOf.instanceOf(right, BigDecimal.class)) {
            return num;
        }
        if (TypeOf.instanceOf(left, BigInteger.class)
                || TypeOf.instanceOf(right, BigDecimal.class)) {
            return num.toBigInteger();
        }
        if (TypeOf.instanceOf(left, Double.class)
                || TypeOf.instanceOf(right, Double.class)) {
            return num.doubleValue();
        }
        if (TypeOf.instanceOf(left, Float.class)
                || TypeOf.instanceOf(right, Float.class)) {
            return num.floatValue();
        }
        if (TypeOf.instanceOf(left, Long.class)
                || TypeOf.instanceOf(right, Long.class)) {
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
        if (TypeOf.typeOf(right.getClass(), left.getClass())) {
            if (left instanceof Comparable<?>) {
                return ((Comparable) left).compareTo(right);
            }
        }
        if (TypeOf.typeOf(left.getClass(), right.getClass())) {
            if (left instanceof Comparable<?>) {
                int ret = ((Comparable) left).compareTo(right);
                if (ret == 0) {
                    return 0;
                }
                if (ret > 0) {
                    return -1;
                }
                return 1;
            }
        }
        if (ObjectConvertor.isNumericType(left.getClass())
                && ObjectConvertor.isNumericType(right.getClass())) {
            BigDecimal bl = new BigDecimal(String.valueOf(left));
            BigDecimal br = new BigDecimal(String.valueOf(right));
            return bl.compareTo(br);
        }
        if (TypeOf.instanceOf(left, CharSequence.class)
                || TypeOf.instanceOf(right, CharSequence.class)) {
            String sl = String.valueOf(left);
            String sr = String.valueOf(right);
            return sl.compareTo(sr);
        }
        try {
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
        } catch (Exception e) {

        }
        return Integer.compare(left.hashCode(), right.hashCode());
    }

    @Override
    public Class<?> loadClass(String className) {
        return ReflectResolver.loadClass(className);
    }

    public Method findMethod(String naming, List<Object> args) {
        CopyOnWriteArrayList<Method> list = TinyScript.BUILTIN_METHOD.get(naming);
        if (list != null && !list.isEmpty()) {
            return ReflectResolver.matchExecutable(list, args);
        }
        return null;
    }

    public Reference<Object> beforeFunctionCall(Object target, boolean isNew, String naming, List<Object> argList) {
        return Reference.nop();
    }

    public Map<String, Object> castArgumentListAsNamingMap(List<Object> argList) {
        Map<String, Object> map = new HashMap<>();
        for (Object arg : argList) {
            if (arg instanceof TinyScriptVisitorImpl.NamingBindArgument) {
                TinyScriptVisitorImpl.NamingBindArgument val = (TinyScriptVisitorImpl.NamingBindArgument) arg;
                map.put(val.naming, val.value);
            }
        }
        return map;
    }

    @Override
    public Object resolveFunctionCall(Object target, boolean isNew, String naming, List<Object> argList) {
        Reference<Object> ref = beforeFunctionCall(target, isNew, naming, argList);
        if (ref != null) {
            if (ref.isValue()) {
                return ref.get();
            }
        }
        List<Object> args = new ArrayList<>();
        for (Object arg : argList) {
            if (arg instanceof TinyScriptVisitorImpl.NamingBindArgument) {
                TinyScriptVisitorImpl.NamingBindArgument val = (TinyScriptVisitorImpl.NamingBindArgument) arg;
                args.add(val.value);
            } else {
                args.add(arg);
            }
        }
        Class<?> clazz = null;
        if (isNew) {
            clazz = loadClass(naming);
        } else {
            if (target == null) {
                int idx = naming.lastIndexOf(".");
                if (idx > 0) {
                    String className = naming.substring(0, idx);
                    naming = naming.substring(idx + 1);
                    clazz = loadClass(className);
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

            Method method = findMethod(naming, args);
            if (method != null) {
                Object ret = ReflectResolver.execMethod(target, method, args);
                return ret;
            }

            Object ret = ReflectResolver.execMethod(target, clazz, methodName, args);
            return ret;
        }
    }

    @Override
    public String renderString(String text, Object context) {
        return RegexUtil.regexFindAndReplace(text, "[\\\\]*\\$\\{[^}]+\\}", (str) -> {
            str = str.substring("${".length(), str.length() - "}".length());
            str = str.trim();
            Object value = getValue(context, str);
            return String.valueOf(value);
        });
    }

    @Override
    public String multilineString(String text, List<String> features, Object context) {
        for (String feature : features) {
            if ("trim".equals(feature)) {
                text = text.trim();
            } else if ("align".equals(feature)) {
                StringBuilder builder = new StringBuilder();
                String[] arr = text.split("\n");
                for (String item : arr) {
                    int idx = item.indexOf("|");
                    if (idx >= 0) {
                        builder.append(item.substring(idx + 1));
                    } else {
                        builder.append(item);
                    }
                    builder.append("\n");
                }
                text = builder.toString();
            } else if ("render".equals(feature)) {
                text = renderString(text, context);
            }
        }
        return text;
    }
}
