package i2f.serialize.str.text;

import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.ReflectResolver;
import i2f.serialize.std.str.IStringObjectSerializer;
import i2f.serialize.std.str.text.exception.FormatTextSerializeException;
import i2f.typeof.TypeOf;

/**
 * @author Ice2Faith
 * @date 2022/4/14 10:14
 * @desc
 */
public class FormatTextSerializer implements IStringObjectSerializer {
    private IStringObjectSerializer serializer;

    public FormatTextSerializer(IStringObjectSerializer serializer) {
        this.serializer = serializer;
    }

    public String textSerialize(Object obj) {
        if (obj == null) {
            return "null:";
        }
        Class clazz = obj.getClass();
        String className = clazz.getName();
        if (className.startsWith("java.lang.")) {
            className = "$" + className.substring("java.lang.".length());
        }
        if (TypeOf.isBaseType(clazz)) {
            return className + ":" + obj;
        }
        String text = serializer.serialize(obj);
        return className + ":" + text;
    }

    public Object textDeserialize(String str) {
        int idx = str.indexOf(":");
        if (idx <= 0) {
            throw new FormatTextSerializeException("not valid text serialize format:" + str);
        }
        String className = str.substring(0, idx);
        if (className.startsWith("$")) {
            className = "java.lang." + className.substring(1);
        }
        String text = str.substring(idx + 1);
        if ("null".equals(className)) {
            return null;
        }
        Object val = null;
        Class<?> clazz = ReflectResolver.loadClass(className);
        if (TypeOf.isBaseType(clazz)) {
            val = ObjectConvertor.tryConvertAsType(text, clazz);
        } else {
            val = serializer.deserialize(text, clazz);
        }
        return val;
    }

    public Object serializeCopy(Object obj) {
        String text = textSerialize(obj);
        return textDeserialize(text);
    }

    @Override
    public String serialize(Object data) {
        return textSerialize(data);
    }

    @Override
    public Object deserialize(String enc) {
        return textDeserialize(enc);
    }
}
