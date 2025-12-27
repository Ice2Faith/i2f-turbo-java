package i2f.extension.antlr4.script.tiny.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.extension.antlr4.script.tiny.impl.context.DefaultFunctionCallContext;
import i2f.invokable.method.IMethod;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public class DefaultTinyScriptResolver implements TinyScriptResolver {
    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    public static final BigDecimal NUM_100 = new BigDecimal("100");

    public static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
    protected final AtomicBoolean debug = new AtomicBoolean(true);

    @FunctionalInterface
    protected static interface DoubleOperatorFunction {
        Object resolve(Object context, Object left, String operator, Object right);
    }

    protected static final ConcurrentHashMap<String, DoubleOperatorFunction> DOUBLE_OPERATOR_MAP = new ConcurrentHashMap<>();

    static {
        DOUBLE_OPERATOR_MAP.put("as", (context, left, operator, right) -> {
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
            return ret;
        });
        DOUBLE_OPERATOR_MAP.put("cast", DOUBLE_OPERATOR_MAP.get("as"));

        DOUBLE_OPERATOR_MAP.put("is", (context, left, operator, right) -> {
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
        });
        DOUBLE_OPERATOR_MAP.put("instanceof", DOUBLE_OPERATOR_MAP.get("is"));
        DOUBLE_OPERATOR_MAP.put("typeof", DOUBLE_OPERATOR_MAP.get("is"));

        DOUBLE_OPERATOR_MAP.put("in", (context, left, operator, right) -> {
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
        });
        DOUBLE_OPERATOR_MAP.put("notin", DOUBLE_OPERATOR_MAP.get("in"));

        DOUBLE_OPERATOR_MAP.put(">=", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num >= 0;
        });
        DOUBLE_OPERATOR_MAP.put("gte", DOUBLE_OPERATOR_MAP.get(">="));

        DOUBLE_OPERATOR_MAP.put("<=", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num <= 0;
        });
        DOUBLE_OPERATOR_MAP.put("lte", DOUBLE_OPERATOR_MAP.get("<="));

        DOUBLE_OPERATOR_MAP.put("<>", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num != 0;
        });
        DOUBLE_OPERATOR_MAP.put("neq", DOUBLE_OPERATOR_MAP.get("<>"));
        DOUBLE_OPERATOR_MAP.put("!=", DOUBLE_OPERATOR_MAP.get("<>"));
        DOUBLE_OPERATOR_MAP.put("ne", DOUBLE_OPERATOR_MAP.get("<>"));

        DOUBLE_OPERATOR_MAP.put(">", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num > 0;
        });
        DOUBLE_OPERATOR_MAP.put("gt", DOUBLE_OPERATOR_MAP.get(">"));

        DOUBLE_OPERATOR_MAP.put("<", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num < 0;
        });
        DOUBLE_OPERATOR_MAP.put("lt", DOUBLE_OPERATOR_MAP.get("<"));

        DOUBLE_OPERATOR_MAP.put("==", (context, left, operator, right) -> {
            int num = compare(left, right);
            return num == 0;
        });
        DOUBLE_OPERATOR_MAP.put("eq", DOUBLE_OPERATOR_MAP.get("=="));

        DOUBLE_OPERATOR_MAP.put("+", (context, left, operator, right) -> {
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
            } else if (ObjectConvertor.isDateType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                LocalDateTime lv = (LocalDateTime) ObjectConvertor.tryConvertAsType(left, LocalDateTime.class);
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                LocalDateTime ret = lv.plusDays(rv.longValue());
                return ObjectConvertor.tryConvertAsType(ret, left.getClass());
            } else if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isDateType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(right));
                LocalDateTime rv = (LocalDateTime) ObjectConvertor.tryConvertAsType(left, LocalDateTime.class);
                LocalDateTime ret = rv.plusDays(lv.longValue());
                return ObjectConvertor.tryConvertAsType(ret, left.getClass());
            }
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        });

        DOUBLE_OPERATOR_MAP.put("-", (context, left, operator, right) -> {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.subtract(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            } else if (ObjectConvertor.isDateType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                LocalDateTime lv = (LocalDateTime) ObjectConvertor.tryConvertAsType(left, LocalDateTime.class);
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                LocalDateTime ret = lv.plusDays(0 - rv.longValue());
                return ObjectConvertor.tryConvertAsType(ret, left.getClass());
            } else if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isDateType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(right));
                LocalDateTime rv = (LocalDateTime) ObjectConvertor.tryConvertAsType(left, LocalDateTime.class);
                LocalDateTime ret = rv.plusDays(0 - lv.longValue());
                return ObjectConvertor.tryConvertAsType(ret, left.getClass());
            }
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        });

        DOUBLE_OPERATOR_MAP.put("*", (context, left, operator, right) -> {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.multiply(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        });

        DOUBLE_OPERATOR_MAP.put("/", (context, left, operator, right) -> {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                BigDecimal ret = lv.divide(rv, MATH_CONTEXT);
                return convertNumberType(ret, (Number) left, (Number) right);
            }
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        });

        DOUBLE_OPERATOR_MAP.put("%", (context, left, operator, right) -> {
            if (ObjectConvertor.isNumericType(left.getClass())
                    && ObjectConvertor.isNumericType(right.getClass())) {
                BigDecimal lv = new BigDecimal(String.valueOf(left));
                BigDecimal rv = new BigDecimal(String.valueOf(right));
                long num = lv.longValue() % rv.longValue();
                return num;
            }
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        });


    }

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
    public void openDebugger(Object context, String tag, String conditionExpression) {
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
    public boolean toBoolean(Object context, Object ret) {
        return ObjectConvertor.toBoolean(ret);
    }

    @Override
    public Object resolveDoubleOperator(Object context, Supplier<Object> left, String operator, Supplier<Object> right) {
        if ("&&".equals(operator) || "and".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left.get());
            if (!bl) {
                return bl;
            }
            boolean br = ObjectConvertor.toBoolean(right.get());
            return br;
        } else if ("||".equals(operator) || "or".equals(operator)) {
            boolean bl = ObjectConvertor.toBoolean(left.get());
            if (bl) {
                return bl;
            }
            boolean br = ObjectConvertor.toBoolean(right.get());
            return br;
        } else {
            return resolveDoubleOperator(context, left.get(), operator, right.get());
        }
    }

    @Override
    public Object resolveDoubleOperator(Object context, Object left, String operator, Object right) {
        DoubleOperatorFunction func = DOUBLE_OPERATOR_MAP.get(operator);
        if (func == null) {
            throw new IllegalArgumentException("un-support operator :" + operator + " for left type(" + (left == null ? "null" : left.getClass()) + ")" + " and right type(" + (right == null ? "null" : right.getClass()) + ")");
        }
        return func.resolve(context, left, operator, right);
    }

    @Override
    public Object resolvePrefixOperator(Object context, String operator, Object value) {
        if ("!".equals(operator) || "not".equals(operator)) {
            boolean bv = ObjectConvertor.toBoolean(value);
            return !bv;
        }
        if ("-".equals(operator)) {
            if (value != null) {
                if (ObjectConvertor.isNumericType(value.getClass())) {
                    BigDecimal lv = new BigDecimal(String.valueOf(value));
                    BigDecimal ret = lv.negate();
                    return convertNumberType(ret, (Number) ret, (Number) ret);
                }
            }
        }
        throw new IllegalArgumentException("un-support prefix operator :" + operator + " for type(" + (value == null ? "null" : value.getClass()) + ")");
    }

    @Override
    public Object resolveSuffixOperator(Object context, Object value, String operator) {
        if ("%".equals(operator)) {
            if (value != null) {
                if (ObjectConvertor.isNumericType(value.getClass())) {
                    BigDecimal lv = new BigDecimal(String.valueOf(value));
                    BigDecimal ret = lv.divide(NUM_100, MATH_CONTEXT);
                    return convertNumberType(ret, (Number) ret, (Number) ret);
                }
            }
        }
        throw new IllegalArgumentException("un-support prefix operator :" + operator + " for type(" + (value == null ? "null" : value.getClass()) + ")");
    }

    public static Object convertNumberType(BigDecimal num, Number left, Number right) {
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

    public static int compare(Object left, Object right) {
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
        if (ObjectConvertor.isDateType(left.getClass())
                && ObjectConvertor.isDateType(right.getClass())) {
            try {
                Date bl = (Date) ObjectConvertor.tryConvertAsType(left, Date.class);
                Date br = (Date) ObjectConvertor.tryConvertAsType(right, Date.class);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (ObjectConvertor.isNumericType(left.getClass())
                || ObjectConvertor.isNumericType(right.getClass())) {
            try {
                BigDecimal bl = new BigDecimal(String.valueOf(left));
                BigDecimal br = new BigDecimal(String.valueOf(right));
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (ObjectConvertor.isBooleanType(left.getClass())
                || ObjectConvertor.isBooleanType(right.getClass())) {
            try {
                Boolean bl = ObjectConvertor.toBoolean(left);
                Boolean br = ObjectConvertor.toBoolean(right);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (TypeOf.typeOfAny(left.getClass(), String.class, CharSequence.class, Appendable.class)
                || TypeOf.typeOfAny(right.getClass(), String.class, CharSequence.class, Appendable.class)) {
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
    public Class<?> loadClass(Object context, String className) {
        return ReflectResolver.loadClass(className);
    }

    public IMethod findMethod(Object context, String naming, List<Object> args) {
        CopyOnWriteArrayList<IMethod> methods = TinyScript.BUILTIN_METHOD.get(naming);
        if (methods != null && !methods.isEmpty()) {
            IMethod method = ReflectResolver.matchExecMethod(methods, args);
            return method;
        }
        return null;
    }

    public Reference<Object> beforeFunctionCall(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
        return Reference.nop();
    }

    public Map<String, Object> castArgumentListAsNamingMap(Object context, List<Object> argList) {
        Map<String, Object> map = new HashMap<>();
        for (Object arg : argList) {
            if (arg instanceof TinyScriptVisitorImpl.NamingBindArgument) {
                TinyScriptVisitorImpl.NamingBindArgument val = (TinyScriptVisitorImpl.NamingBindArgument) arg;
                map.put(val.naming, val.value);
            }
        }
        return map;
    }


    public Object getFunctionCallContext(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
        DefaultFunctionCallContext ret = new DefaultFunctionCallContext();
        ret.setResolver(this);
        ret.setContext(context);
        ret.setTarget(target);
        ret.setNew(isNew);
        ret.setNaming(naming);
        ret.setArgList(argList);
        return ret;
    }

    @Override
    public Object resolveFunctionCall(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
        Object functionCallContext = getFunctionCallContext(context, target, isNew, naming, argList);
        TinyScript.FUNCTION_CALL_CONTEXT.set(functionCallContext);
        try {
            Reference<Object> ref = beforeFunctionCall(context, target, isNew, naming, argList);
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
                clazz = loadClass(context, naming);
            } else {
                if (target == null) {
                    int idx = naming.lastIndexOf(".");
                    if (idx > 0) {
                        String className = naming.substring(0, idx);
                        naming = naming.substring(idx + 1);
                        clazz = loadClass(context, className);
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

                if (clazz != null) {
                    Method method = ReflectResolver.matchExecMethod(clazz, methodName, args);
                    if (method != null) {
                        return ReflectResolver.execMethod(target, clazz, methodName, args);
                    }
                }

                IMethod method = findMethod(context, methodName, args);
                if (method != null) {
                    Object ret = ReflectResolver.execMethod(target, method, args);
                    return ret;
                }

                Object ret = ReflectResolver.execMethod(target, clazz, methodName, args);
                return ret;
            }
        } finally {
            TinyScript.FUNCTION_CALL_CONTEXT.remove();
        }
    }

    @Override
    public String renderString(Object context, String text) {
        return RegexUtil.regexFindAndReplace(text, "[\\\\]*\\$\\{[^}]+\\}", (str) -> {
            str = str.substring("${".length(), str.length() - "}".length());
            str = str.trim();
            Object value = getValue(context, str);
            return String.valueOf(value);
        });
    }

    @Override
    public String multilineString(Object context, String text, List<String> features) {
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
                text = renderString(context, text);
            }
        }
        return text;
    }
}
