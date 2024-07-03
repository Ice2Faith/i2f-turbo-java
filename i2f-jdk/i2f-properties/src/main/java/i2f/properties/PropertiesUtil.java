package i2f.properties;

import i2f.reflect.RichConverter;
import i2f.reflect.vistor.Visitor;
import i2f.text.StringUtils;
import i2f.typeof.token.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/7/3 21:49
 * @desc
 */
public class PropertiesUtil {


    public static Properties load(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return load(fis);
    }

    public static Properties load(URL url) throws IOException {
        InputStream is = url.openStream();
        return load(is);
    }

    public static Properties load(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        is.close();
        return properties;
    }

    public static <T> T loadAsBean(Properties props, Class<T> beanClass) {
        return loadAsBean(props, null, beanClass);
    }

    public static <T> T loadAsBean(Properties props, String prefix, Class<T> beanClass) {
        Map<String, Object> mapTree = loadAsMapTree(props, StringUtils::toCamel);
        Object root = mapTree;
        if (prefix != null && !prefix.isEmpty()) {
            root = Visitor.visit(prefix, mapTree).get();
        }
        return RichConverter.convert(root, beanClass);
    }

    public static <T> T loadAsBean(Properties props, TypeToken<T> token) {
        return loadAsBean(props, null, token);
    }

    public static <T> T loadAsBean(Properties props, String prefix, TypeToken<T> token) {
        Map<String, Object> mapTree = loadAsMapTree(props, StringUtils::toCamel);
        Object root = mapTree;
        if (prefix != null && !prefix.isEmpty()) {
            root = Visitor.visit(prefix, mapTree).get();
        }
        return RichConverter.convert(root, token);
    }

    public static <T> T loadAsBean(Properties props, Type type) {
        return loadAsBean(props, null, type);
    }

    public static <T> T loadAsBean(Properties props, String prefix, Type type) {
        Map<String, Object> mapTree = loadAsMapTree(props, StringUtils::toCamel);
        Object root = mapTree;
        if (prefix != null && !prefix.isEmpty()) {
            root = Visitor.visit(prefix, mapTree).get();
        }
        return RichConverter.convert2Type(root, type);
    }

    public static Map<String, Object> loadAsMapTree(Properties props) {
        return loadAsMapTree(props, null);
    }

    public static Map<String, Object> loadAsMapTree(Properties props, Function<String, String> keyMapper) {
        Map<String, String> map = toMap(props);
        return groupMap(map,keyMapper);
    }

    public static Map<String, String> toMap(Properties props) {
        Map<String, String> ret = new TreeMap<>();
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            String value = props.getProperty(name);
            ret.put(name, value);
        }
        return ret;
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
                    String putKey=arr[0];
                    if(keyMapper!=null){
                        putKey=keyMapper.apply(putKey);
                    }
                    ret.put(putKey, new TreeMap<>());
                }
                Map<String, String> obj = (Map<String, String>) ret.get(arr[0]);
                String putKey=arr[1];
                if(keyMapper!=null){
                    putKey=keyMapper.apply(putKey);
                }
                obj.put(putKey, entry.getValue());
            } else {
                String putKey=arr[0];
                if(keyMapper!=null){
                    putKey=keyMapper.apply(putKey);
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
