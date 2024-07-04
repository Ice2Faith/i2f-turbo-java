package i2f.reflect;

import i2f.convert.obj.ObjectConvertor;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/7/4 10:37
 * @desc 对象路由转换解析器
 * 实现如下形式的互转
 * user.username=admin
 * user.age=12
 * user.roles[0].name=admin
 * 也就是可以用于properties文件的生成，通过toRouteMap方法
 * 也可以用于properties文件的解析，通过ofBean/ofMapTree
 * 同样，也可以在此基础上进行拓展
 * 实现例如：url-query-params/url-form-encoded的解析与生成
 */
public class ObjectRouteResolver {
    public static Map<String, Object> toRouteMap(Object obj) {
        Map<String, Object> ret = new LinkedHashMap<>();
        toRouteMapNext(ret, obj, "");
        return ret;
    }

    public static void toRouteMapNext(Map<String, Object> builder, Object obj, String path) {
        if (path == null) {
            path = "";
        }
        if (obj == null) {
            builder.put(path, obj);
        }
        Class<?> clazz = obj.getClass();

        if (TypeOf.isBaseType(clazz)
                || ObjectConvertor.isBooleanType(clazz)
                || ObjectConvertor.isNumericType(clazz)
                || ObjectConvertor.isCharType(clazz)) {
            builder.put(path, obj);
            return;
        }

        if (ObjectConvertor.isDateType(clazz)) {
            builder.put(path, obj);
            return;
        }

        if (TypeOf.typeOfAny(clazz, CharSequence.class, URL.class, URI.class)) {
            builder.put(path, obj);
            return;
        }

        if (TypeOf.typeOf(clazz, Charset.class)) {
            builder.put(path, obj);
            return;
        }

        if (TypeOf.typeOfAny(clazz, Class.class)) {
            builder.put(path, obj);
            return;
        }


        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();
                String nextPath = null;
                if (path.isEmpty()) {
                    nextPath = key;
                } else {
                    nextPath = path + "." + key;
                }
                toRouteMapNext(builder, value, nextPath);
            }
        } else if (obj instanceof Collection) {
            Collection<?> cols = (Collection<?>) obj;
            int i = 0;
            for (Object value : cols) {
                String nextPath = null;
                if (path.isEmpty()) {
                    nextPath = "[" + i + "]";
                } else {
                    nextPath = path + "[" + i + "]";
                }
                toRouteMapNext(builder, value, nextPath);
                i++;
            }
        } else if (clazz.isArray()) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(obj, i);
                String nextPath = null;
                if (path.isEmpty()) {
                    nextPath = "[" + i + "]";
                } else {
                    nextPath = path + "[" + i + "]";
                }
                toRouteMapNext(builder, value, nextPath);
            }
        } else {
            Set<Field> fields = ReflectResolver.getFields(clazz).keySet();
            for (Field field : fields) {
                try {
                    String key = field.getName();
                    Object value = ReflectResolver.valueGet(obj, field);
                    String nextPath = null;
                    if (path.isEmpty()) {
                        nextPath = key;
                    } else {
                        nextPath = path + "." + key;
                    }
                    toRouteMapNext(builder, value, nextPath);
                } catch (Exception e) {

                }
            }
        }
    }

    public static Map<String, String> toFlatMap(Map<String, List<String>> map) {
        Map<String, String> ret = new TreeMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.isEmpty()) {
                ret.put(key, "");
            } else if (value.size() == 1) {
                ret.put(key, value.get(0));
            } else {
                int i = 0;
                for (String item : value) {
                    String putKey = key + "[" + i + "]";
                    ret.put(putKey, item);
                    i++;
                }
            }
        }
        return ret;
    }

    public static <T> T ofBean(Map<String, String> map, Class<T> beanClass) {
        return ofBean(map, beanClass, null);
    }

    public static <T> T ofBean(Map<String, String> map, Class<T> beanClass, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofMapTree(map, keyMapper);
        return RichConverter.convert(mapTree, beanClass);
    }

    public static <T> T ofBean(Map<String, String> map, TypeToken<T> token) {
        return ofBean(map, token, null);
    }

    public static <T> T ofBean(Map<String, String> map, TypeToken<T> token, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofMapTree(map, keyMapper);
        return RichConverter.convert(mapTree, token);
    }

    public static <T> T ofBean(Map<String, String> map, Type type) {
        return ofBean(map, type, null);
    }

    public static <T> T ofBean(Map<String, String> map, Type type, Function<String, String> keyMapper) {
        Map<String, Object> mapTree = ofMapTree(map, keyMapper);
        return RichConverter.convert2Type(mapTree, type);
    }

    public static Map<String, Object> ofMapTree(Map<String, String> map) {
        return ofMapTree(map, null);
    }

    public static Map<String, Object> ofMapTree(Map<String, String> map, Function<String, String> keyMapper) {
        return groupMap(map, keyMapper);
    }

    public static Map<String, Object> groupMap(Map<String, String> map) {
        return groupMap(map, null);
    }

    public static Map<String, Object> groupMap(Map<String, String> map, Function<String, String> keyMapper) {
        Map<String, Object> ret = new TreeMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] arr = key.split("\\.", 2);
            if (arr.length == 2) {
                if (!ret.containsKey(arr[0])) {
                    String putKey = arr[0];
                    if (keyMapper != null) {
                        putKey = keyMapper.apply(putKey);
                    }
                    ret.put(putKey, new TreeMap<>());
                }
                Map<String, String> obj = (Map<String, String>) ret.get(arr[0]);
                String putKey = arr[1];
                if (keyMapper != null) {
                    putKey = keyMapper.apply(putKey);
                }
                obj.put(putKey, entry.getValue());
            } else {
                String putKey = arr[0];
                if (keyMapper != null) {
                    putKey = keyMapper.apply(putKey);
                }
                ret.put(putKey, entry.getValue());
            }
        }

        Map<String, Object> root = new TreeMap<>();
        Map<String, List<Object>> rootList = new TreeMap<>();

        for (Map.Entry<String, Object> entry : ret.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            Object next = obj;
            if (obj instanceof Map) {
                Map<String, String> item = (Map<String, String>) obj;
                next = groupMap(item, keyMapper);
            }
            if (key.endsWith("]")) {
                int idx = key.lastIndexOf("[");
                String name = key.substring(0, idx);
                if (!rootList.containsKey(name)) {
                    rootList.put(name, new ArrayList<>());
                }
                if (keyMapper != null) {
                    name = keyMapper.apply(name);
                }
                rootList.get(name).add(next);
            } else {
                if (keyMapper != null) {
                    key = keyMapper.apply(key);
                }
                root.put(key, next);
            }
        }

        root.putAll(rootList);
        return root;
    }
}
