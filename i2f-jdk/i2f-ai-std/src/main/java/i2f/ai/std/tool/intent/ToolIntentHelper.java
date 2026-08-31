package i2f.ai.std.tool.intent;

import i2f.ai.std.tool.intent.impl.ReadonlyToolIntent;
import i2f.lru.LruMap;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/8/31 15:33
 * @desc
 */
public class ToolIntentHelper {
    private static final LruMap<Method,Map<String,IToolIntent>> cacheParse=new LruMap<>(4096);
    public static Map<String, IToolIntent> parse(Method method) {
        Map<String, IToolIntent> ret = cacheParse.get(method);
        if(ret!=null){
            return new LinkedHashMap<>(ret);
        }
        Map<String, IToolIntent> map = parse0(method);
        cacheParse.put(method,new LinkedHashMap<>(map));
        return map;
    }
    public static Map<String, IToolIntent> parse0(Method method) {
        Map<String, IToolIntent> ret = new LinkedHashMap<>();
        Class<?> declaringClass = method.getDeclaringClass();
        ToolIntent cann = declaringClass.getAnnotation(ToolIntent.class);
        if (cann != null) {
            parseInto(cann, ret);
        }
        ToolIntent mann = method.getAnnotation(ToolIntent.class);
        if (mann != null) {
            parseInto(mann, ret);
        }
        return ret;
    }

    public static void parseInto(ToolIntent ann, Map<String, IToolIntent> ret) {
        if (ann == null) {
            return;
        }
        ToolIntents[] arr = ann.value();
        if (arr != null) {
            for (ToolIntents item : arr) {
                ret.put(item.label(), item);
            }
        }
        ToolIntentItem[] items = ann.items();
        if (items != null) {
            for (ToolIntentItem item : items) {
                ret.put(item.value(), new ReadonlyToolIntent(item.value(), item.description()));
            }
        }
        Class<? extends IToolIntent>[] from = ann.from();
        if (from != null) {
            for (Class<? extends IToolIntent> clazz : from) {
                try {
                    IToolIntent item = clazz.newInstance();
                    ret.put(item.label(), new ReadonlyToolIntent(item.label(), item.description()));
                } catch (Throwable e) {
                    // ignore
                }
            }
        }
    }
}
