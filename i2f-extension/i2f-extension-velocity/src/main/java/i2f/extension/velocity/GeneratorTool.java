package i2f.extension.velocity;

import i2f.extension.velocity.stringify.Stringifier;
import i2f.extension.velocity.stringify.impl.DefaultStringifier;
import i2f.extension.velocity.stringify.impl.ListableStringifier;
import i2f.mixin.all.AllMixins;
import i2f.os.OsUtil;
import i2f.serialize.str.json.impl.Json2;
import i2f.serialize.str.xml.impl.Xml2;
import i2f.typeof.TypeOf;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2022/2/12 8:36
 * @desc
 */
public class GeneratorTool implements AllMixins {
    public static final GeneratorTool INSTANCE = new GeneratorTool();
    public final Stringifier STRINGIFIER = new ListableStringifier();

    public String str(Object obj) {
        if (STRINGIFIER.support(obj)) {
            return STRINGIFIER.stringify(obj);
        }
        return DefaultStringifier.INSTANCE.stringify(obj);
    }

    /**
     * fmt参数含义：%s 指代循环变量，有且只有一个%s,此参数可以为空
     *
     * @param obj
     * @param fmt
     * @param sep
     * @param open
     * @param close
     * @return
     */
    public String join(Object obj, String fmt, String sep, String open, String close) {
        StringBuilder builder = new StringBuilder();
        if (open != null) {
            builder.append(open);
        }
        if (obj instanceof Collection) {
            boolean isFirst = true;
            Collection col = (Collection) obj;
            Iterator it = col.iterator();
            while (it.hasNext()) {
                if (!isFirst) {
                    if (sep != null) {
                        builder.append(sep);
                    }
                }
                String val = str(it.next());
                if (isnull(fmt)) {
                    builder.append(val);
                } else {
                    builder.append(String.format(fmt, val));
                }
                isFirst = false;
            }

        } else if (is_array(obj)) {
            boolean isFirst = true;
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (!isFirst) {
                    if (sep != null) {
                        builder.append(sep);
                    }
                }
                String val = str(Array.get(obj, i));
                if (isnull(fmt)) {
                    builder.append(val);
                } else {
                    builder.append(String.format(fmt, val));
                }
                isFirst = false;

            }
        }
        if (close != null) {
            builder.append(close);
        }
        return builder.toString();
    }

    public List<Integer> fori(int begin, int end, int step) {
        List<Integer> ret = new ArrayList<>();
        for (int i = begin; i != end; i += step) {
            ret.add(i);
        }
        return ret;
    }

    public void cmd(String cmdline, boolean wait) {
        try {
            Process process = Runtime.getRuntime().exec(cmdline);
            if (wait) {
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String cmdResult(String cmdLine, String charset) throws IOException, InterruptedException {
        return OsUtil.runCmd(cmdLine, charset);
    }

    public List<Map<String, Object>> list(Object itr) {
        List<Map<String, Object>> ret = new ArrayList<>();
        if (itr instanceof Collection) {
            Collection col = (Collection) itr;
            int index = 0;
            int size = col.size();
            Iterator it = col.iterator();
            while (it.hasNext()) {
                Object val = it.next();
                Map<String, Object> obj = new HashMap<>();
                obj.put("first", index == 0);
                obj.put("last", index == (size - 1));
                obj.put("index", index);
                obj.put("size", size);
                obj.put("value", val);
                ret.add(obj);
                index++;
            }
        } else if (is_array(itr)) {
            int size = Array.getLength(itr);
            for (int i = 0; i < size; i++) {
                Object val = Array.get(itr, i);
                Map<String, Object> obj = new HashMap<>();
                obj.put("first", i == 0);
                obj.put("last", i == (size - 1));
                obj.put("index", i);
                obj.put("size", size);
                obj.put("value", val);
                ret.add(obj);
            }
        }
        return ret;
    }

    public boolean instanceOf(Object obj, String typeName) {
        if (isnull(obj) || is_empty(typeName)) {
            return false;
        }
        Class objCls = obj.getClass();
        Object[][] baseMapping = {
                {"string", String.class},
                {"int", Integer.class},
                {"short", Short.class},
                {"byte", Byte.class},
                {"char", Character.class},
                {"long", Long.class},
                {"float", Float.class},
                {"double", Double.class},
                {"date", Date.class}
        };
        String lowType = typeName.toLowerCase();
        for (Object[] item : baseMapping) {
            if (item[0].equals(lowType)) {
                if (isInTypes(objCls, (Class) item[1])) {
                    return true;
                }
            }
        }
        try {
            Class typeCls = Class.forName(typeName);
            return isInTypes(objCls, typeCls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String toXmlString(Object obj) {
        return Xml2.toXmlString(str(obj));
    }

    public String toJsonString(Object obj) {
        return Json2.INSTANCE.toJson(obj);
    }

    public String[] split(String str, boolean trimBefore, String regex, int limit, boolean removeEmpty) {
        String[] ret = new String[]{};
        if (str == null) {
            return ret;
        }
        if (trimBefore) {
            str = str.trim();
        }
        ret = str.split(regex, limit);
        Vector<String> result = new Vector<>();
        for (String item : ret) {
            if (removeEmpty) {
                if ("".equals(item)) {
                    continue;
                }
            }
            result.add(item);
        }
        ret = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ret[i] = result.get(i);
        }
        return ret;
    }

    public boolean isInTypes(Class target, Class... types) {
        if (target == null) {
            return false;
        }
        return TypeOf.typeOfAny(target, types);
    }
}
