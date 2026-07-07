package i2f.mixin.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.math.Factorial;
import i2f.math.Fibonacci;
import i2f.math.HexNumberConverter;
import i2f.mixin.consts.MixinConsts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:33
 * @desc
 */
public interface MathMixins {

    default int round(double a) {
        return (int) Math.round(a);
    }

    default double add(double a, double b) {
        return a + b;
    }

    default double sub(double a, double b) {
        return a - b;
    }

    default double div(double a, double b) {
        return a / b;
    }

    default double mul(double a, double b) {
        return a * b;
    }

    default double mod(double a, double b) {
        return ((int) a) % ((int) b);
    }

    default double pow(double a, double b) {
        return Math.pow(a, b);
    }

    default double sqrt(double a) {
        return Math.sqrt(a);
    }

    default double log(double a) {
        return Math.log(a);
    }

    default Object neg(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = obj.negate(MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object abs(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = obj.abs(MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object ln(Object number) {
        if (number == null) {
            return null;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        obj = BigDecimal.valueOf(Math.log(obj.doubleValue()));

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object add(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.add(b2, MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sub(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.subtract(b2, MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object mul(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.multiply(b2, MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object div(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = b1.divide(b2, MixinConsts.MATH_CONTEXT);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object mod(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigInteger.class);
        if (!(num1 instanceof BigInteger)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigInteger.class);
        if (!(num2 instanceof BigInteger)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigInteger b1 = (BigInteger) num1;
        BigInteger b2 = (BigInteger) num2;
        b1 = b1.mod(b2);

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object pow(Object number1, Object number2) {
        if (number1 == null) {
            return null;
        }
        if (number2 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }
        Object num2 = ObjectConvertor.tryConvertAsType(number2, BigDecimal.class);
        if (!(num2 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number2.getClass());
        }

        BigDecimal b1 = (BigDecimal) num1;
        BigDecimal b2 = (BigDecimal) num2;
        b1 = BigDecimal.valueOf(Math.pow(b1.doubleValue(), b2.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sin(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.sin(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object cos(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.cos(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object tan(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.tan(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object asin(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.asin(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object acos(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.acos(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object atan(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.atan(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object sqrt(Object number1) {
        if (number1 == null) {
            return null;
        }
        Object num1 = ObjectConvertor.tryConvertAsType(number1, BigDecimal.class);
        if (!(num1 instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number1.getClass());
        }


        BigDecimal b1 = (BigDecimal) num1;
        b1 = BigDecimal.valueOf(Math.sqrt(b1.doubleValue()));

        return ObjectConvertor.tryConvertAsType(b1, number1.getClass());
    }

    default Object round(Object number) {
        return round(number, 0);
    }

    default Object round(Object number, Integer precision) {
        if (number == null) {
            return null;
        }
        if (precision == null) {
            precision = 0;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        if (precision == 0) {
            obj = new BigDecimal(obj.add(MixinConsts.NUM_0_5).toBigInteger());
        } else if (precision > 0) {
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MixinConsts.MATH_CONTEXT);
            obj = obj.multiply(scale, MixinConsts.MATH_CONTEXT).add(MixinConsts.NUM_0_5);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.divide(scale, MixinConsts.MATH_CONTEXT);
        } else {
            precision = -precision;
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MixinConsts.MATH_CONTEXT);
            obj = obj.divide(scale, MixinConsts.MATH_CONTEXT).add(MixinConsts.NUM_0_5);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.multiply(scale, MixinConsts.MATH_CONTEXT);
        }

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default Object trunc(Object number, Integer precision) {
        if (number == null) {
            return null;
        }
        if (precision == null) {
            precision = 0;
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }

        BigDecimal obj = (BigDecimal) num;
        if (precision == 0) {
            obj = new BigDecimal(obj.toBigInteger());
        } else if (precision > 0) {
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MixinConsts.MATH_CONTEXT);
            obj = obj.multiply(scale, MixinConsts.MATH_CONTEXT);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.divide(scale, MixinConsts.MATH_CONTEXT);
        } else {
            precision = -precision;
            BigDecimal scale = BigDecimal.TEN;
            scale = scale.pow(precision, MixinConsts.MATH_CONTEXT);
            obj = obj.divide(scale, MixinConsts.MATH_CONTEXT);
            obj = new BigDecimal(obj.toBigInteger());
            obj = obj.multiply(scale, MixinConsts.MATH_CONTEXT);
        }

        return ObjectConvertor.tryConvertAsType(obj, number.getClass());
    }

    default double from_radix(Object hex, Integer radix) {
        if (hex == null) {
            throw new IllegalArgumentException("hex to number require text.");
        }
        if (radix == null) {
            throw new IllegalArgumentException("hex to number require radix.");
        }
        double ret = HexNumberConverter.hex2number(String.valueOf(hex), radix);
        return ret;
    }

    default String to_radix(Object number, Integer radix) {
        if (number == null) {
            throw new IllegalArgumentException("hex to number require text.");
        }
        if (radix == null) {
            throw new IllegalArgumentException("hex to number require radix.");
        }
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }
        BigDecimal obj = (BigDecimal) num;
        double val = obj.doubleValue();
        return HexNumberConverter.number2hex(val, radix, 20);
    }

    default Object to_radians(Object number) {
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }
        BigDecimal obj = (BigDecimal) num;
        double ret = Math.toRadians(obj.doubleValue());
        return ObjectConvertor.tryConvertAsType(ret, number.getClass());
    }

    default Object to_degrees(Object number) {
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }
        BigDecimal obj = (BigDecimal) num;
        double ret = Math.toDegrees(obj.doubleValue());
        return ObjectConvertor.tryConvertAsType(ret, number.getClass());
    }

    default BigInteger fibonacci(Object number) {
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }
        BigDecimal obj = (BigDecimal) num;
        BigInteger ret = Fibonacci.get(obj.intValue());
        return ret;
    }

    default BigInteger factorial(Object number) {
        Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
        if (!(num instanceof BigDecimal)) {
            throw new IllegalArgumentException("number cannot cast as number type, of type :" + number.getClass());
        }
        BigDecimal obj = (BigDecimal) num;
        BigInteger ret = Factorial.get(obj.intValue());
        return ret;
    }

    default Object max_of(Object... numbers) {
        return max_of(Arrays.asList(numbers));
    }

    default Object max_of(Iterable<?> numbers) {
        BigDecimal ret = null;
        Object firstNumber = null;
        for (Object number : numbers) {
            if (number == null) {
                continue;
            }
            Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
            if (!(num instanceof BigDecimal)) {
                throw new IllegalArgumentException("number cannot cast as number type, of type :" + num.getClass());
            }
            if (firstNumber == null) {
                firstNumber = number;
            }
            BigDecimal item = (BigDecimal) num;
            if (ret == null) {
                ret = item;
            } else {
                if (ret.compareTo(item) < 0) {
                    ret = item;
                }
            }
        }
        if (ret == null) {
            return null;
        }
        if (firstNumber == null) {
            return ret;
        }
        return ObjectConvertor.tryConvertAsType(ret, firstNumber.getClass());
    }

    default Object min_of(Object... numbers) {
        return min_of(Arrays.asList(numbers));
    }

    default Object min_of(Iterable<?> numbers) {
        BigDecimal ret = null;
        Object firstNumber = null;
        for (Object number : numbers) {
            if (number == null) {
                continue;
            }
            Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
            if (!(num instanceof BigDecimal)) {
                throw new IllegalArgumentException("number cannot cast as number type, of type :" + num.getClass());
            }
            if (firstNumber == null) {
                firstNumber = number;
            }
            BigDecimal item = (BigDecimal) num;
            if (ret == null) {
                ret = item;
            } else {
                if (ret.compareTo(item) > 0) {
                    ret = item;
                }
            }
        }
        if (ret == null) {
            return null;
        }
        if (firstNumber == null) {
            return ret;
        }
        return ObjectConvertor.tryConvertAsType(ret, firstNumber.getClass());
    }

    default Object avg_of(Object... numbers) {
        return avg_of(Arrays.asList(numbers));
    }

    default Object avg_of(Iterable<?> numbers) {
        BigDecimal sum = BigDecimal.ZERO;
        Object firstNumber = null;
        int length = 0;
        for (Object number : numbers) {
            length++;
            if (number == null) {
                continue;
            }
            Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
            if (!(num instanceof BigDecimal)) {
                throw new IllegalArgumentException("number cannot cast as number type, of type :" + num.getClass());
            }
            if (firstNumber == null) {
                firstNumber = number;
            }
            BigDecimal item = (BigDecimal) num;
            sum = sum.add(item);
        }

        BigDecimal ret = sum.divide(BigDecimal.valueOf(length), MixinConsts.MATH_CONTEXT);
        if (firstNumber == null) {
            return ret;
        }
        return ObjectConvertor.tryConvertAsType(ret, firstNumber.getClass());
    }

    default Object sum_of(Object... numbers) {
        return sum_of(Arrays.asList(numbers));
    }

    default Object sum_of(Iterable<?> numbers) {
        BigDecimal sum = BigDecimal.ZERO;
        Object firstNumber = null;
        for (Object number : numbers) {
            if (number == null) {
                continue;
            }
            Object num = ObjectConvertor.tryConvertAsType(number, BigDecimal.class);
            if (!(num instanceof BigDecimal)) {
                throw new IllegalArgumentException("number cannot cast as number type, of type :" + num.getClass());
            }
            if (firstNumber == null) {
                firstNumber = number;
            }
            BigDecimal item = (BigDecimal) num;
            sum = sum.add(item);
        }

        if (firstNumber == null) {
            return sum;
        }
        return ObjectConvertor.tryConvertAsType(sum, firstNumber.getClass());
    }
}
