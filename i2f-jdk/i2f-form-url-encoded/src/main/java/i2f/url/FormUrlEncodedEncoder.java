package i2f.url;

import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.ObjectRouteResolver;
import i2f.reflect.RichConverter;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/7/4 9:02
 * @desc
 */
public class FormUrlEncodedEncoder {

    public static String toForm(Object obj) {
        Map<String, Object> routeMap = ObjectRouteResolver.toRouteMap(obj);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : routeMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                key = URLEncoder.encode(key, "UTF-8");
            } catch (Exception e) {

            }
            if (key.isEmpty()) {
                continue;
            }
            if (value == null) {
                builder.append("&").append(key).append("=");
                continue;
            }
            Class<?> clazz = value.getClass();
            if (TypeOf.isBaseType(clazz)
                    || ObjectConvertor.isBooleanType(clazz)
                    || ObjectConvertor.isNumericType(clazz)
                    || ObjectConvertor.isCharType(clazz)) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (Exception e) {

                }
                builder.append("&").append(key).append("=").append(value);
                continue;
            }

            if (ObjectConvertor.isDateType(clazz)) {
                Date date = (Date) ObjectConvertor.tryConvertAsType(value, Date.class);
                value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (Exception e) {

                }
                builder.append("&").append(key).append("=").append(value);
                continue;
            }

            if (TypeOf.typeOfAny(clazz, CharSequence.class, URL.class, URI.class)) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (Exception e) {

                }
                builder.append("&").append(key).append("=").append(value);
                continue;
            }

            if (TypeOf.typeOf(clazz, Charset.class)) {
                Charset charset = (Charset) value;
                value = charset.name();
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (Exception e) {

                }
                builder.append("&").append(key).append("=").append(value);
                continue;
            }

            if (TypeOf.typeOfAny(clazz, Class.class)) {
                Class<?> aClass = (Class<?>) value;
                value = aClass.getName();
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (Exception e) {

                }
                builder.append("&").append(key).append("=").append(value);
                continue;
            }

            try {
                value = URLEncoder.encode(String.valueOf(value), "UTF-8");
            } catch (Exception e) {

            }
            builder.append("&").append(key).append("=").append(value);
        }
        String str = builder.toString();
        if (!str.isEmpty()) {
            str = str.substring(1);
        }
        return str;
    }

    public static <T> T ofFormBean(String str, Class<T> beanClass) {
        return ofFormBean(str, beanClass, null);
    }

    public static <T> T ofFormBean(String str, Class<T> beanClass, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofFormMapTree(str, keyMapper);
        return RichConverter.convert(mapTree, beanClass);
    }

    public static <T> T ofFormBean(String str, TypeToken<T> token) {
        return ofFormBean(str, token, null);
    }

    public static <T> T ofFormBean(String str, TypeToken<T> token, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofFormMapTree(str, keyMapper);
        return RichConverter.convert(mapTree, token);
    }

    public static <T> T ofFormBean(String str, Type type) {
        return ofFormBean(str, type, null);
    }

    public static <T> T ofFormBean(String str, Type type, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofFormMapTree(str, keyMapper);
        return RichConverter.convert2Type(mapTree, type);
    }

    public static Map<String, Object> ofFormMapTree(String str) {
        return ofFormMapTree(str, null);
    }

    public static Map<String, Object> ofFormMapTree(String str, Function<String, String> keyMapper) {
        Map<String, List<String>> map = toMap(str);
        Map<String, String> flatMap = ObjectRouteResolver.toFlatMap(map);
        return ObjectRouteResolver.ofMapTree(flatMap, keyMapper);
    }

    public static Map<String, List<String>> toMap(String str) {
        Map<String, List<String>> ret = new TreeMap<>();
        String[] pairs = str.split("&");
        for (String pair : pairs) {
            String[] arr = pair.split("=", 2);
            String key = arr[0];
            try {
                key = URLDecoder.decode(key, "UTF-8");
            } catch (Exception e) {

            }
            String val = "";
            if (arr.length == 2) {
                val = arr[1];
            }
            if (!ret.containsKey(key)) {
                ret.put(key, new ArrayList<>());
            }

            try {
                val = URLDecoder.decode(val, "UTF-8");
            } catch (Exception e) {

            }
            ret.get(key).add(val);
        }
        return ret;
    }

}
