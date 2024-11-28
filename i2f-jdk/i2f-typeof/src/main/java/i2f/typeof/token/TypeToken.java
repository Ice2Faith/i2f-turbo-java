package i2f.typeof.token;

import i2f.typeof.token.data.TypeNode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2022/5/25 13:43
 * @desc 这个类必须是抽象的，使用时直接抽象实例化一个本对象子类，通过getType方法获取类型即可
 * 示例：
 * Class type=new TypeToken<Integer>(){}.getType();
 * 得到的type即为java.lang.Integer
 * 另外，在反射时，也可以根据具体的Field类型，得到泛型的类型
 * 这样就可以在泛型反射时，能够正确实例化类型
 */
public abstract class TypeToken<T> {

    public Class<T> getType() {
        return getGenericsClassType(getClass());
    }

    public Type type() {
        return getGenericsClassTypes(getClass())[0];
    }

    @Override
    public String toString() {
        return getType().getName();
    }

    public static <E> Class<E> getMapKeyType(Class<E> clazz, String fieldName) {
        return getFieldType(clazz, fieldName, 0);
    }

    public static <E> Class<E> getMapValueType(Class<E> clazz, String fieldName) {
        return getFieldType(clazz, fieldName, 1);
    }

    public static <E> Class<E> getListType(Class<?> clazz, String fieldName) {
        return getFieldType(clazz, fieldName, 0);
    }

    public static <E> Class<E> getFieldType(Class<?> clazz, String fieldName) {
        return getFieldType(clazz, fieldName, 0);
    }

    public static <E> Class<E> getFieldType(Class<?> clazz, String fieldName, int idx) {
        Field field = getClassField(clazz, fieldName);
        return (Class<E>) rawType(getGenericsFieldTypes(field)[idx]);
    }

    public static Field getClassField(Class<?> clazz, String fieldName) {
        Field field = null;
        Exception ex = null;
        if (field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                ex = e;
            }
        }
        if (field == null) {
            try {
                field = clazz.getField(fieldName);
            } catch (Exception e) {
                ex = e;
            }
        }
        if (field == null) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
        return field;
    }

    public static <E> Class<E> rawType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<E>) ((ParameterizedType) type).getRawType();
        }
        return (Class<E>) type;
    }

    public static <E> Class<E> getGenericsClassType(Class<?> clazz) {
        Type type = getGenericsClassTypes(clazz)[0];
        return rawType(type);
    }

    public static Type[] getGenericsClassTypes(Class<?> clazz) {
        Type genType = clazz.getGenericSuperclass();
        return getGenericsTypes(genType);
    }

    public static <E> Class<E> getGenericsFieldType(Field field) {
        return (Class<E>) rawType(getGenericsFieldTypes(field)[0]);
    }

    public static Type[] getGenericsFieldTypes(Field field) {
        Type genType = field.getGenericType();
        return getGenericsTypes(genType);
    }

    public static Type[] getGenericsTypes(Type genType) {
        if (genType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) genType;
            return ptype.getActualTypeArguments();
        }
        throw new UnsupportedOperationException("not a generics type!");
    }


    public TypeNode fullType() {
        Class<?> clazz = getClass();
        Type genType = clazz.getGenericSuperclass();
        TypeNode node = getFullGenericType(genType);
        return node.getArgs().get(0);
    }

    public static TypeNode fullFieldType(Class<?> clazz, String fieldName) {
        Field field = getClassField(clazz, fieldName);
        Type genType = field.getGenericType();
        return getFullGenericType(genType);
    }

    public static TypeNode getFullGenericType(Type genType) {
        if (genType instanceof Class) {
            TypeNode node = new TypeNode();
            node.setType((Class<?>) genType);
            node.setArgs(new ArrayList<>());
            return node;
        }
        if (genType instanceof WildcardType) {
            WildcardType type = (WildcardType) genType;
            TypeNode node = new TypeNode();
            node.setType((Class<?>) Object.class);
            node.setArgs(new ArrayList<>());
            return node;
        }
        if (genType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) genType;

            Type rawType = ptype.getRawType();
            TypeNode root = new TypeNode();
            root.setType((Class<?>) rawType);
            root.setArgs(new ArrayList<>());

            Type[] actualTypeArguments = ptype.getActualTypeArguments();
            for (Type item : actualTypeArguments) {
                TypeNode next = getFullGenericType(item);
                if (next == null) {
                    next = new TypeNode();
                    next.setType((Class<?>) Object.class);
                    next.setArgs(new ArrayList<>());
                }
                root.getArgs().add(next);
            }

            return root;
        }
        return null;
    }
}
