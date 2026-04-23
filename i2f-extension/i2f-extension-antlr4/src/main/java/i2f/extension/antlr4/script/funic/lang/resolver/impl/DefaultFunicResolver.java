package i2f.extension.antlr4.script.funic.lang.resolver.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;
import i2f.extension.antlr4.script.funic.lang.functions.FunicBuiltinFunctions;
import i2f.extension.antlr4.script.funic.lang.functions.FunicFunctionHelper;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.method.FunicMethod;
import i2f.extension.antlr4.script.funic.lang.operator.DoubleOperatorFunction;
import i2f.extension.antlr4.script.funic.lang.operator.PrefixOperatorFunction;
import i2f.extension.antlr4.script.funic.lang.operator.SuffixOperatorFunction;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicSupplier;
import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.iterator.iterator.Iterators;
import i2f.match.regex.RegexUtil;
import i2f.reflect.ReflectResolver;
import i2f.reflect.RichConverter;
import i2f.typeof.TypeOf;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/4/22 9:15
 * @desc
 */
public class DefaultFunicResolver implements FunicResolver {
    protected MathContext MATH_CONTEXT = new MathContext(32, RoundingMode.HALF_UP);
    protected ConcurrentHashMap<String, PrefixOperatorFunction> prefixOperatorFunctionMap = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, SuffixOperatorFunction> suffixOperatorFunctionMap = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, DoubleOperatorFunction> doubleOperatorFunctionMap = new ConcurrentHashMap<>();

    protected static ThreadLocal<DefaultFunicVisitor> VISITOR = new InheritableThreadLocal<>();

    {
        initPrefixOperatorFunctions();
        initSuffixOperatorFunctions();
        initDoubleOperatorFunctions();
    }

    protected void initPrefixOperatorFunctions() {
        prefixOperatorFunctionMap.put("!", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            boolean ok = toBoolean(value, visitor);
            return !ok;
        });
        prefixOperatorFunctionMap.put("not", prefixOperatorFunctionMap.get("!"));

        prefixOperatorFunctionMap.put("~", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("bit reverse cannot be null");
            }
            Class<?> clazz = value.getClass();
            if (TypeOf.typeOfAny(clazz, short.class, Short.class)) {
                short v = (Short) value;
                return ~v;
            }
            if (TypeOf.typeOfAny(clazz, int.class, Integer.class)) {
                int v = (Integer) value;
                return ~v;
            }
            if (TypeOf.typeOfAny(clazz, long.class, Long.class)) {
                long v = (Long) value;
                return ~v;
            }
            if (TypeOf.typeOfAny(clazz, float.class, Float.class)) {
                float v = (Float) value;
                return ~((int) v);
            }
            if (TypeOf.typeOfAny(clazz, double.class, Double.class)) {
                double v = (Double) value;
                return ~((long) v);
            }
            if (value instanceof Number) {
                long v = ((Number) value).longValue();
                return ~v;
            }
            throw new FunicThrowException("un-support bit reverse for type:" + clazz);
        });
        prefixOperatorFunctionMap.put("-", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math negative cannot be null");
            }
            BigDecimal num = ofBigDecimal(value);
            BigDecimal ret = num.negate(MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
        prefixOperatorFunctionMap.put("++", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math incr cannot be null");
            }
            BigDecimal num = ofBigDecimal(value);
            BigDecimal ret = num.add(BigDecimal.ONE, MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
        prefixOperatorFunctionMap.put("--", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math decr cannot be null");
            }
            BigDecimal num = ofBigDecimal(value);
            BigDecimal ret = num.subtract(BigDecimal.ONE, MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
    }

    protected void initSuffixOperatorFunctions() {
        suffixOperatorFunctionMap.put("!", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math factor cannot be null");
            }
            BigDecimal decimal = ofBigDecimal(value);
            BigInteger sum = BigInteger.ONE;
            BigInteger integer = decimal.toBigInteger();
            while (integer.compareTo(BigInteger.ONE) > 0) {
                sum = sum.multiply(integer);
                integer = integer.subtract(BigInteger.ONE);
            }
            return castAsNumberType(sum, value);
        });
        suffixOperatorFunctionMap.put("%", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math percent cannot be null");
            }
            BigDecimal decimal = ofBigDecimal(value);
            BigDecimal ret = decimal.divide(new BigDecimal("100"), MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
        prefixOperatorFunctionMap.put("++", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math incr cannot be null");
            }
            BigDecimal num = ofBigDecimal(value);
            BigDecimal ret = num.add(BigDecimal.ONE, MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
        prefixOperatorFunctionMap.put("--", (value, visitor) -> {
            value = unwrapSupplierValue(value);
            if (value == null) {
                throw new FunicThrowException("math decr cannot be null");
            }
            BigDecimal num = ofBigDecimal(value);
            BigDecimal ret = num.subtract(BigDecimal.ONE, MATH_CONTEXT);
            return castAsNumberType(ret, value);
        });
    }

    protected void initDoubleOperatorFunctions() {
        doubleOperatorFunctionMap.put("*", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math multiply left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math multiply right cannot be null");
            }
            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigDecimal ret = lv.multiply(rv, MATH_CONTEXT);
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("/", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math divide left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math divide right cannot be null");
            }
            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigDecimal ret = lv.divide(rv, MATH_CONTEXT);
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("//", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math hard-divide left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math hard-divide right cannot be null");
            }
            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigInteger ret = lv.divide(rv, MATH_CONTEXT).toBigInteger();
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("%", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math mod left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math mod right cannot be null");
            }
            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigInteger ret = lv.toBigInteger().mod(rv.toBigInteger());
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("+", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (TypeOf.instanceOf(leftValue, CharSequence.class)
                    || TypeOf.instanceOf(rightValue, CharSequence.class)) {
                return leftValue + "" + rightValue;
            }
            if (leftValue == null) {
                throw new FunicThrowException("math add left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math add right cannot be null");
            }
            if (ObjectConvertor.isDateType(leftValue.getClass())
                    && ObjectConvertor.isNumericType(rightValue.getClass())) {
                LocalDateTime date = (LocalDateTime) ObjectConvertor.tryConvertAsType(leftValue, LocalDateTime.class);
                BigDecimal decimal = new BigDecimal(String.valueOf(rightValue));
                LocalDateTime ret = date.plusDays(decimal.longValue());
                return ObjectConvertor.tryConvertAsType(ret, leftValue.getClass());
            }

            if (ObjectConvertor.isDateType(rightValue.getClass())
                    && ObjectConvertor.isNumericType(leftValue.getClass())) {
                LocalDateTime date = (LocalDateTime) ObjectConvertor.tryConvertAsType(rightValue, LocalDateTime.class);
                BigDecimal decimal = new BigDecimal(String.valueOf(leftValue));
                LocalDateTime ret = date.plusDays(decimal.longValue());
                return ObjectConvertor.tryConvertAsType(ret, rightValue.getClass());
            }

            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigDecimal ret = lv.add(rv, MATH_CONTEXT);
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("-", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math subtract left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math subtract right cannot be null");
            }
            BigDecimal lv = ofBigDecimal(leftValue);
            BigDecimal rv = ofBigDecimal(rightValue);
            BigDecimal ret = lv.subtract(rv, MATH_CONTEXT);
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put("===", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, true);
            return v == 0;
        });
        doubleOperatorFunctionMap.put("teq", doubleOperatorFunctionMap.get("==="));
        doubleOperatorFunctionMap.put("!==", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, true);
            return v != 0;
        });
        doubleOperatorFunctionMap.put("tneq", doubleOperatorFunctionMap.get("!=="));
        doubleOperatorFunctionMap.put(">", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v > 0;
        });
        doubleOperatorFunctionMap.put("gt", doubleOperatorFunctionMap.get(">"));
        doubleOperatorFunctionMap.put(">=", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v >= 0;
        });
        doubleOperatorFunctionMap.put("gte", doubleOperatorFunctionMap.get(">="));
        doubleOperatorFunctionMap.put("<", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v < 0;
        });
        doubleOperatorFunctionMap.put("lt", doubleOperatorFunctionMap.get("<"));
        doubleOperatorFunctionMap.put("<=", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v <= 0;
        });
        doubleOperatorFunctionMap.put("lte", doubleOperatorFunctionMap.get("<="));
        doubleOperatorFunctionMap.put("==", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v == 0;
        });
        doubleOperatorFunctionMap.put("eq", doubleOperatorFunctionMap.get("=="));
        doubleOperatorFunctionMap.put("!=", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            int v = compare(leftValue, rightValue, false);
            return v != 0;
        });
        doubleOperatorFunctionMap.put("neq", doubleOperatorFunctionMap.get("!="));
        doubleOperatorFunctionMap.put("<>", doubleOperatorFunctionMap.get("!="));
        doubleOperatorFunctionMap.put("in", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (rightValue == null) {
                return false;
            }
            Iterator<?> iterator = wrapAsIterator(rightValue, visitor);
            while (iterator.hasNext()) {
                Object item = iterator.next();
                int v = compare(leftValue, item, false);
                if (v == 0) {
                    return true;
                }
            }
            return false;
        });
        doubleOperatorFunctionMap.put("not;in", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (rightValue == null) {
                return true;
            }
            Iterator<?> iterator = wrapAsIterator(rightValue, visitor);
            while (iterator.hasNext()) {
                Object item = iterator.next();
                int v = compare(leftValue, item, false);
                if (v == 0) {
                    return false;
                }
            }
            return true;
        });
        doubleOperatorFunctionMap.put("instanceof", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (rightValue == null) {
                return false;
            }
            if (rightValue instanceof Class) {
                return TypeOf.instanceOf(leftValue, (Class<?>) rightValue);
            }
            return TypeOf.instanceOf(leftValue, rightValue.getClass());
        });
        doubleOperatorFunctionMap.put("is", doubleOperatorFunctionMap.get("instanceof"));

        doubleOperatorFunctionMap.put("&&", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            boolean ok = toBoolean(leftValue, visitor);
            if (!ok) {
                return false;
            }
            rightValue = unwrapSupplierValue(rightValue);
            return toBoolean(rightValue, visitor);
        });
        doubleOperatorFunctionMap.put("and", doubleOperatorFunctionMap.get("&&"));

        doubleOperatorFunctionMap.put("||", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            boolean ok = toBoolean(leftValue, visitor);
            if (ok) {
                return true;
            }
            rightValue = unwrapSupplierValue(rightValue);
            return toBoolean(rightValue, visitor);
        });
        doubleOperatorFunctionMap.put("or", doubleOperatorFunctionMap.get("||"));

        doubleOperatorFunctionMap.put("<<", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-left-move left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-left-move right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv << rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });
        doubleOperatorFunctionMap.put(">>>", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-sign-right-move left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-sign-right-move right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv >>> rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });

        doubleOperatorFunctionMap.put(">>", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-right-move left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-right-move right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv >> rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });

        doubleOperatorFunctionMap.put("^", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-xor left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-xor right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv ^ rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });

        doubleOperatorFunctionMap.put("&", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-and left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-and right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv & rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });

        doubleOperatorFunctionMap.put("|", (leftValue, rightValue, visitor) -> {
            leftValue = unwrapSupplierValue(leftValue);
            rightValue = unwrapSupplierValue(rightValue);
            if (leftValue == null) {
                throw new FunicThrowException("math bit-or left cannot be null");
            }
            if (rightValue == null) {
                throw new FunicThrowException("math bit-or right cannot be null");
            }
            long lv = ofBigDecimal(leftValue).longValue();
            long rv = ofBigDecimal(rightValue).longValue();
            long ret = lv | rv;
            return castAsNumberType(ret, leftValue, rightValue);
        });
    }

    protected Object unwrapSupplierValue(Object value) {
        if (value instanceof FunicSupplier) {
            FunicSupplier<?> supplier = (FunicSupplier<?>) value;
            return supplier.get();
        }
        return value;
    }

    public BigDecimal ofBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(String.valueOf(value));
    }

    public int compare(Object left, Object right, boolean forceType) {
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
                return ((Comparable) left).compareTo(right);
            }
        }
        if (ObjectConvertor.isNumericType(left.getClass()) && ObjectConvertor.isNumericType(right.getClass())) {
            BigDecimal bl = new BigDecimal(String.valueOf(left));
            BigDecimal br = new BigDecimal(String.valueOf(right));
            return bl.compareTo(br);
        }
        if (ObjectConvertor.isDateType(left.getClass()) && ObjectConvertor.isDateType(right.getClass())) {
            try {
                Date bl = (Date) ObjectConvertor.tryConvertAsType(left, Date.class);
                Date br = (Date) ObjectConvertor.tryConvertAsType(right, Date.class);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (ObjectConvertor.isBooleanType(left.getClass()) && ObjectConvertor.isBooleanType(right.getClass())) {
            try {
                Boolean bl = (Boolean) ObjectConvertor.tryConvertAsType(left, Boolean.class);
                Boolean br = (Boolean) ObjectConvertor.tryConvertAsType(right, Boolean.class);
                return bl.compareTo(br);
            } catch (Exception e) {

            }
        }
        if (!forceType) {
            if (ObjectConvertor.isBooleanType(left.getClass()) || ObjectConvertor.isBooleanType(right.getClass())) {
                try {
                    Boolean bl = ObjectConvertor.toBoolean(left);
                    Boolean br = ObjectConvertor.toBoolean(right);
                    return bl.compareTo(br);
                } catch (Exception e) {

                }
            }
            if (TypeOf.typeOfAny(left.getClass(), String.class, CharSequence.class, Appendable.class) || TypeOf.typeOfAny(right.getClass(), String.class, CharSequence.class, Appendable.class)) {
                String sl = String.valueOf(left);
                String sr = String.valueOf(right);
                return sl.compareTo(sr);
            }
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
        if (!forceType) {
            return Integer.compare(left.hashCode(), right.hashCode());
        }
        return 1;
    }

    @Override
    public Object multilineString(String text, List<String> features, DefaultFunicVisitor visitor) {
        Object ret = text;
        for (String feature : features) {
            ret = applyMultilineFeature(ret, feature, visitor);
        }
        return ret;
    }

    public Object applyMultilineFeature(Object ret, String feature, DefaultFunicVisitor visitor) {
        if ("trim".equals(feature)) {
            return ret == null ? null : String.valueOf(ret).trim();
        }
        if ("render".equals(feature)) {
            return ret == null ? null : renderString(String.valueOf(ret), visitor);
        }
        if ("align".equals(feature)) {
            if (ret == null) {
                return null;
            }
            String text = String.valueOf(ret);
            String[] arr = text.split("\n");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                String line = arr[i];
                if (i > 0) {
                    builder.append("\n");
                }
                int idx = line.indexOf("|");
                if (idx >= 0) {
                    line = line.substring(idx + 1);
                }
                builder.append(line);
            }
            return builder.toString();
        }
        return ret;
    }

    @Override
    public String renderString(String text, DefaultFunicVisitor visitor) {
        if (text == null) {
            return text;
        }
        return RegexUtil.regexFindAndReplace(text, "\\$\\!?\\{[^\\}]+\\}", s -> {
            boolean emptyFlag = false;
            if (s.startsWith("$!{")) {
                emptyFlag = true;
                s = s.substring(3, s.length() - 1);
            } else {
                emptyFlag = false;
                s = s.substring(2, s.length() - 1);
            }
            Object obj = getRenderPlaceHolderValue(s, visitor);
            if (obj == null) {
                if (emptyFlag) {
                    obj = "";
                }
            }
            return String.valueOf(obj);
        });
    }

    public Object getRenderPlaceHolderValue(String express, DefaultFunicVisitor visitor) {
        return Funic.script(express, visitor);
    }

    @Override
    public Object getFieldValue(Object target, String fieldName, DefaultFunicVisitor visitor) {
        if (target instanceof Class) {
            return getStaticFieldOrEnum((Class) target, fieldName, visitor);
        }
        if (target instanceof Map) {
            Map map = (Map) target;
            return map.get(fieldName);
        }
        try {
            return ReflectResolver.valueGet(target, fieldName);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        }
    }

    @Override
    public void setFieldValue(Object target, String fieldName, Object value, DefaultFunicVisitor visitor) {
        if (target instanceof Class) {
            setStaticField((Class<?>) target, fieldName, value, visitor);
            return;
        }
        if (target instanceof Map) {
            Map map = (Map) target;
            map.put(fieldName, value);
            return;
        }
        try {
            ReflectResolver.valueSet(target, fieldName, value);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        }
    }

    @Override
    public Object getSquareFieldValue(Object target, Object squareKey, DefaultFunicVisitor visitor) {
        if (target == null) {
            return null;
        }
        Iterator<?> iterator = null;
        if (target instanceof Iterator) {
            iterator = (Iterator<?>) target;
        } else if (target instanceof Iterable) {
            iterator = Iterators.of((Iterable) target);
        } else if (target.getClass().isArray()) {
            iterator = Iterators.ofArrayObject(target);
        } else if (target instanceof Enumeration) {
            iterator = Iterators.of((Enumeration) target);
        } else if (target instanceof CharSequence) {
            CharSequence sequence = (CharSequence) target;
            char[] arr = new char[sequence.length()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = sequence.charAt(i);
            }
            iterator = Iterators.of(arr);
        }
        if (iterator != null) {
            int index = 0;
            if (squareKey instanceof Number) {
                index = ((Number) squareKey).intValue();
            } else {
                index = new BigDecimal(String.valueOf(squareKey)).intValue();
            }
            int i = 0;
            while (iterator.hasNext()) {
                Object elem = iterator.next();
                if (i == index) {
                    return elem;
                }
                i++;
            }
            throw new IndexOutOfBoundsException("index is: " + index);
        } else if (target instanceof Map) {
            Map map = (Map) target;
            if (map.containsKey(squareKey)) {
                return map.get(squareKey);
            }
            return map.get(squareKey == null ? null : String.valueOf(squareKey));
        } else if (target instanceof Class) {
            Class<?> clazz = (Class<?>) target;
            return getStaticFieldOrEnum(clazz, String.valueOf(squareKey), visitor);
        } else {
            String fieldName = String.valueOf(squareKey);
            try {
                return ReflectResolver.valueGet(target, fieldName);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new FunicThrowException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void setSquareFieldValue(Object target, Object squareKey, Object value, DefaultFunicVisitor visitor) {
        if (target == null) {
            throw new NullPointerException("target is null, cannot set value");
        }
        if (target instanceof List) {
            List list = (List) target;
            int index = 0;
            if (squareKey instanceof Number) {
                index = ((Number) squareKey).intValue();
            } else {
                index = new BigDecimal(String.valueOf(squareKey)).intValue();
            }
            list.set(index, value);
        } else if (target.getClass().isArray()) {
            int index = 0;
            if (squareKey instanceof Number) {
                index = ((Number) squareKey).intValue();
            } else {
                index = new BigDecimal(String.valueOf(squareKey)).intValue();
            }
            Array.set(target, index, value);
        } else if (target instanceof Map) {
            Map map = (Map) target;
            Class keyType = null;
            for (Object item : map.entrySet()) {
                Map.Entry entry = (Map.Entry) item;
                Object key = entry.getKey();
                if (key != null) {
                    keyType = key.getClass();
                    break;
                }
            }
            if (keyType != null) {
                Object mapKey = RichConverter.convert(squareKey, keyType);
                map.put(mapKey, value);
            } else {
                map.put(squareKey == null ? null : String.valueOf(squareKey), value);
            }
        } else if (target instanceof Class) {
            setStaticField((Class) target, String.valueOf(squareKey), value, visitor);
        } else {
            try {
                ReflectResolver.valueSet(target, String.valueOf(squareKey), value);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new FunicThrowException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Object getStaticFieldOrEnum(Class<?> type, String fieldName, DefaultFunicVisitor visitor) {
        if (type.isEnum()) {
            Object[] arr = type.getEnumConstants();
            for (Object item : arr) {
                if (String.valueOf(item).equals(fieldName)) {
                    return item;
                }
            }
        }
        try {
            return ReflectResolver.valueGetStatic(type, fieldName);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        }
    }

    @Override
    public Object setStaticField(Class<?> type, String fieldName, Object value, DefaultFunicVisitor visitor) {
        try {
            return ReflectResolver.valueSetStatic(type, fieldName, value);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        }
    }

    @Override
    public Object invokeInstanceMethod(Object target, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            List<Object> argsList = args.stream().map(e -> e.getValue()).collect(Collectors.toList());
            List<IMethod> list = new ArrayList<>();
            List<IMethod> expandList = getExpandInstanceMethod(target, methodName, visitor);
            if (expandList != null) {
                list.addAll(expandList);
            }
            Map<Method, Class<?>> methodsMap = ReflectResolver.getMethodsByName(target.getClass(), methodName);
            if (methodsMap != null) {
                for (Map.Entry<Method, Class<?>> entry : methodsMap.entrySet()) {
                    list.addAll(FunicFunctionHelper.ofInstanceAsStaticMethod(target, entry.getKey()));
                }
            }
            IMethod method = ReflectResolver.matchExecMethod(list, argsList);
            return doExecMethod(target, method, argsList, visitor);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        } finally {
            VISITOR.remove();
        }
    }

    public List<IMethod> getExpandInstanceMethod(Object target, String methodName, DefaultFunicVisitor visitor) {
        return new ArrayList<>();
    }

    public Object doExecMethod(Object target, IMethod method, List<Object> args, DefaultFunicVisitor visitor) throws Exception {
        return ReflectResolver.execMethod(target, method, args);
    }

    @Override
    public Object invokeGlobalMethod(String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            List<IMethod> methods = searchGlobalMethod(methodName, visitor);
            List<Object> argsList = args.stream().map(e -> e.getValue()).collect(Collectors.toList());
            IMethod method = ReflectResolver.matchExecMethod(methods, argsList);
            if (method != null) {
                if (method instanceof FunicMethod) {
                    // 复制副本，避免多线程下的问题
                    FunicMethod copied = ((FunicMethod) method).copy();
                    copied.setVisitor(visitor);
                    method = copied;
                }
                try {
                    return doExecMethod(null, method, argsList, visitor);
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    }
                    throw new FunicThrowException(e.getMessage(), e);
                }
            } else {
                throw new FunicThrowException("not found match method : " + methodName);
            }
        } finally {
            VISITOR.remove();
        }
    }

    public List<IMethod> searchGlobalMethod(String methodName, DefaultFunicVisitor visitor) {
        List<IMethod> ret = new ArrayList<>();
        List<IMethod> registerList = visitor.getRegistryMethods().get(methodName);
        if (registerList != null) {
            ret.addAll(registerList);
        }
        CopyOnWriteArrayList<IMethod> list = Funic.GLOBAL_METHODS.get(methodName);
        if (list != null) {
            ret.addAll(list);
        }
        List<IMethod> builtin = getBuiltinGlobalMethods(methodName, visitor);
        if (builtin != null) {
            ret.addAll(builtin);
        }
        return ret;
    }

    public List<IMethod> getBuiltinGlobalMethods(String methodName, DefaultFunicVisitor visitor) {
        List<IMethod> ret = FunicFunctionHelper.ofInstanceMethods(new FunicBuiltinFunctions(visitor));
        return ret.stream().filter(e -> e.getName().equals(methodName)).collect(Collectors.toList());
    }

    @Override
    public Object invokeStaticMethod(Class<?> type, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            List<Object> argsList = args.stream().map(e -> e.getValue()).collect(Collectors.toList());
            Method method = ReflectResolver.matchMethod(type, methodName, argsList.toArray(new Object[0]));
            return doExecMethod(null, new JdkMethod(method), argsList, visitor);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        } finally {
            VISITOR.remove();
        }
    }

    @Override
    public Object newInstance(Class<?> clazz, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            List<Object> argsList = args.stream().map(e -> e.getValue()).collect(Collectors.toList());
            return doNewInstance(clazz, argsList, visitor);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new FunicThrowException(e.getMessage(), e);
        } finally {
            VISITOR.remove();
        }
    }

    public Object doNewInstance(Class<?> clazz, List<Object> args, DefaultFunicVisitor visitor) throws Exception {
        return ReflectResolver.getInstance(clazz, args.toArray(new Object[0]));
    }

    @Override
    public Object newArray(Class<?> elementType, int count, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            return Array.newInstance(elementType, count);
        } finally {
            VISITOR.remove();
        }
    }

    public Object castAsNumberType(Object value, Object sourceType) {
        if (value == null) {
            return null;
        }
        if (sourceType == null) {
            return value;
        }
        Class<?> valueClass = value.getClass();
        Class<?> sourceClass = sourceType.getClass();
        Object ret = ObjectConvertor.tryConvertAsType(value, sourceClass);
        if (TypeOf.instanceOf(ret, sourceClass)) {
            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(ret))) == 0) {
                return ret;
            }
        }
        return value;
    }

    public Object castAsNumberType(Object value, Object leftSourceType, Object rightSourceType) {
        if (value == null) {
            return null;
        }
        if (leftSourceType == null && rightSourceType == null) {
            return value;
        }
        if (leftSourceType != null && rightSourceType != null) {
            if (TypeOf.instanceOf(leftSourceType, BigDecimal.class)) {
                return castAsNumberType(value, leftSourceType);
            }
            if (TypeOf.instanceOf(rightSourceType, BigDecimal.class)) {
                return castAsNumberType(value, rightSourceType);
            }
            if (TypeOf.instanceOf(leftSourceType, BigInteger.class)) {
                if (TypeOf.typeOfAny(rightSourceType.getClass(), float.class, Float.class, double.class, Double.class)) {
                    return castAsNumberType(value, BigDecimal.ONE);
                }
                return castAsNumberType(value, BigInteger.ONE);
            }
            if (TypeOf.instanceOf(rightSourceType, BigInteger.class)) {
                if (TypeOf.typeOfAny(leftSourceType.getClass(), float.class, Float.class, double.class, Double.class)) {
                    return castAsNumberType(value, BigDecimal.ONE);
                }
                return castAsNumberType(value, BigInteger.ONE);
            }
            if (TypeOf.typeOfAny(leftSourceType.getClass(), float.class, Float.class, double.class, Double.class) || TypeOf.typeOfAny(rightSourceType.getClass(), float.class, Float.class, double.class, Double.class)) {
                double ref = 1.0;
                return castAsNumberType(value, ref);
            }
            if (TypeOf.typeOfAny(leftSourceType.getClass(), long.class, Long.class) || TypeOf.typeOfAny(rightSourceType.getClass(), long.class, Long.class)) {
                long ref = 1L;
                return castAsNumberType(value, ref);
            }
            if (TypeOf.typeOfAny(leftSourceType.getClass(), int.class, Integer.class) || TypeOf.typeOfAny(rightSourceType.getClass(), int.class, Integer.class)) {
                int ref = 1;
                return castAsNumberType(value, ref);
            }
            return castAsNumberType(value, BigDecimal.ONE);
        } else if (leftSourceType != null) {
            return castAsNumberType(value, leftSourceType);
        } else {
            return castAsNumberType(value, rightSourceType);
        }
    }

    @Override
    public Object prefixOperator(Object value, String operator, DefaultFunicVisitor visitor) {
        PrefixOperatorFunction function = prefixOperatorFunctionMap.get(operator);
        if (function != null) {
            return function.apply(value, visitor);
        }
        throw new FunicThrowException("un-support prefix operator: " + operator);
    }

    @Override
    public Object suffixOperator(Object target, String operator, DefaultFunicVisitor visitor) {
        SuffixOperatorFunction function = suffixOperatorFunctionMap.get(operator);
        if (function != null) {
            return function.apply(target, visitor);
        }
        throw new FunicThrowException("un-support suffix operator: " + operator);
    }

    @Override
    public Object doubleOperator(Object leftValue, String operator, Object rightValue, DefaultFunicVisitor visitor) {
        DoubleOperatorFunction function = doubleOperatorFunctionMap.get(operator);
        if (function != null) {
            return function.apply(leftValue, rightValue, visitor);
        }
        throw new FunicThrowException("un-support double operator: " + operator);
    }

    @Override
    public List<Object> unpackList(Object iterable, DefaultFunicVisitor visitor) {
        if (iterable == null) {
            return new ArrayList<>();
        }
        Iterator<?> iterator = wrapAsIterator(iterable, visitor);
        List<Object> ret = new ArrayList<>();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            ret.add(item);
        }
        return ret;
    }

    @Override
    public Map<String, Object> unpackMap(Object object, DefaultFunicVisitor visitor) {
        if (object == null) {
            return new HashMap<>();
        }
        Map<String, Object> ret = new HashMap<>();
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                ret.put(key == null ? null : String.valueOf(key), entry.getValue());
            }
            return ret;
        }
        ReflectResolver.bean2map(object, ret);
        return ret;
    }

    @Override
    public Class<?> findClass(String className, DefaultFunicVisitor visitor) {
        VISITOR.set(visitor);
        try {
            Class<?> clazz = ReflectResolver.loadClass(className);
            if (clazz != null) {
                return clazz;
            }
            List<String> searchPackages = getSearchPackages(visitor);
            for (String pkg : searchPackages) {
                if (pkg.endsWith(";")) {
                    pkg = pkg.substring(0, pkg.length() - 1);
                }
                if (pkg.endsWith(".*")) {
                    pkg = pkg.substring(0, pkg.length() - 2);
                }
                clazz = ReflectResolver.loadClass(pkg + className);
                if (clazz != null) {
                    return clazz;
                }
            }
            return clazz;
        } finally {
            VISITOR.remove();
        }
    }

    public List<String> getSearchPackages(DefaultFunicVisitor visitor) {
        List<String> searchPackages = new ArrayList<>();
        List<String> contextPkgs = visitor.getImportPackages();
        if (contextPkgs != null) {
            searchPackages.addAll(contextPkgs);
        }
        List<String> defaultPkgs = getDefaultImportPackages();
        searchPackages.addAll(Funic.IMPORT_PACKAGES);
        searchPackages.addAll(defaultPkgs);

        return searchPackages;
    }

    public List<String> getDefaultImportPackages() {
        String[] pkgs = {
                "java.lang",
                "java.util",
                "java.util.concurrent",
                "java.util.concurrent.atomic",
                "java.time",
                "java.math",
                "java.text",
                "java.sql",
                "javax.sql",
                "java.lang.reflect"
        };
        return new ArrayList<>(Arrays.asList(pkgs));
    }

    @Override
    public boolean toBoolean(Object value, DefaultFunicVisitor visitor) {
        return ObjectConvertor.toBoolean(value);
    }

    @Override
    public Iterator<?> wrapAsIterator(Object value, DefaultFunicVisitor visitor) {
        if (value == null) {
            return null;
        }
        if (value instanceof Iterator) {
            return (Iterator<?>) value;
        }
        if (value instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) value;
            return iterable.iterator();
        }
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return map.entrySet().iterator();
        }
        if (value instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) value;
            return Iterators.of(enumeration);
        }
        Class<?> clazz = value.getClass();
        if (clazz.isArray()) {
            return Iterators.ofArrayObject(value);
        }
        throw new FunicThrowException("un-support wrap as iterator: " + value.getClass());
    }

    @Override
    public Object convertType(Object value, Class<?> type, DefaultFunicVisitor visitor) {
        return RichConverter.convert(value, type);
    }

    @Override
    public boolean onPreRegistryContextImportPackage(String packageName, DefaultFunicVisitor visitor) {
        return true;
    }

    @Override
    public boolean onPreRegisterContextGlobalMethod(IMethod method, DefaultFunicVisitor visitor) {
        return true;
    }
}
