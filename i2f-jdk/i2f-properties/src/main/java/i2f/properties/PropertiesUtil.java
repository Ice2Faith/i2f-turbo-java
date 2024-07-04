package i2f.properties;

import i2f.reflect.ObjectRouteResolver;
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
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
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
        return ObjectRouteResolver.ofMapTree(map, keyMapper);
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

}
