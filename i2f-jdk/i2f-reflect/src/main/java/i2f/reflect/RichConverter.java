package i2f.reflect;


import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.test.TestRole;
import i2f.reflect.test.TestRoleType;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/6/3 15:23
 * @desc
 */
public class RichConverter {

    public static void main(String[] args) {
//        if(true){
//            List<Integer> ret = convert(new int[]{1, 2, 3, 4, 5, 6}, new TypeToken<List<Integer>>() {});
//            System.out.println(ObjectConvertor.stringify(ret,null));
//        }

//        if(true){
//            Long[] ret = convert(new int[]{1, 2, 3, 4, 5, 6}, Long[].class);
//            System.out.println(ObjectConvertor.stringify(ret, null));
//        }

//        if(true){
//            TestRole bean = new TestRole();
//            bean.setKey("root");
//            bean.setName("超管");
//            bean.setPerms(new ArrayList<>(Arrays.asList("home","user")));
//
//            Map<String, Object> ret = convert(bean, new TypeToken<LinkedHashMap<String, Object>>() {});
//            System.out.println(ObjectConvertor.stringify(ret,null));
//
//            TestRole cvt = convert(ret, TestRole.class);
//            System.out.println(ObjectConvertor.stringify(cvt,null));
//
//            TestRoleType<String,LinkedList<String>> gcv = convert(ret, new TypeToken<TestRoleType<String,LinkedList<String>>>() {});
//            System.out.println(ObjectConvertor.stringify(gcv,null));
//
//            TestRoleType<String,Set<String>> stv = convert(ret, new TypeToken<TestRoleType<String,Set<String>>>() {});
//            System.out.println(ObjectConvertor.stringify(stv,null));
//
//            TestRoleType<String,String[]> acv = convert(ret, new TypeToken<TestRoleType<String,String[]>>() {});
//            System.out.println(ObjectConvertor.stringify(acv,null));
//        }

        if (true) {
            List<TestRole> list = new ArrayList<>();

            TestRole root = new TestRole();
            root.setKey("root");
            root.setName("超管");
            root.setPerms(new ArrayList<>(Arrays.asList("home", "user")));
            list.add(root);

            TestRole admin = new TestRole();
            admin.setKey("admin");
            admin.setName("管理");
            admin.setPerms(new ArrayList<>(Arrays.asList("home")));
            list.add(admin);

            LinkedList p1 = convert(list, LinkedList.class);
            System.out.println(p1);

            LinkedList<Map> p2 = convert(list, new TypeToken<LinkedList<Map>>() {
            });
            System.out.println(p2);

            LinkedList<Map<String, String>> p3 = convert(list, new TypeToken<LinkedList<Map<String, String>>>() {
            });
            System.out.println(p3);

            LinkedList<TestRoleType<String, String[]>> p4 = convert(p2, new TypeToken<LinkedList<TestRoleType<String, String[]>>>() {
            });
            System.out.println(p4);


            LinkedList<TestRole> p5 = convert(p4, new TypeToken<LinkedList<TestRole>>() {
            });
            System.out.println(p5);
        }
    }

    public static <T> T convert(Object obj, Class<T> clazz) {
        return convert2Type(obj, clazz);
    }

    public static <T> T convert(Object obj, TypeToken<T> token) {
        return convert2Type(obj, token.type());
    }

    public static <T> T convert2Type(Object obj, Type type) {
        return convert2Type(obj, type, null);
    }

    /**
     * 根据type为目标类型，实现将obj转换为type类型的返回值
     * 支持不相关类型的转换，map的key和class的field等只需要满足名称相同即可
     * 比如，两个不想关的类里面都有一个叫做name的属性，那就可以转换
     * 也或者类里面有一个叫做name的属性，而map里面具有一个name的key，那也可以转换
     * 并且这个过程是一个递归过程，递归转换，终止条件为遇到基本类型，包装类型，java/javax/javafx/sun包下面的类型不进行转换
     * 同时，任意类型可以转换为String类型，true/false/null和数值的字面值的string可以直接转换为对应的类型
     * obj可能为Collection/Map/Bean
     * type可能为Collection/Map/Bean
     * 举例：
     * obj=List<Map<String,Object>>
     * type=Set<SysUser>
     * 或者
     * Map<String,Object>
     * SysUser
     * 也就是说，type可能是一个ParameterizedType的泛型类型，也可能是普通的Class类型
     * type可能是这样来的
     * Type type = method.getGenericReturnType();
     *
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T convert2Type(Object obj, Type type, Type[] relTypes) {
        if (obj == null) {
            return null;
        }
        if (type == null) {
            return (T) obj;
        }
        if (type instanceof TypeVariable) {
            if (relTypes != null) {
                return convert2Type(obj, relTypes[0], null);
            } else {
                return convert2Type(obj, Object.class, null);
            }
        }

        Class<?> objClass = obj.getClass();

        Class<?> targetClass = null;
        Type[] targetArgumentTypes = null;
        TypeVariable[] typeParameters = null;
        if (type instanceof ParameterizedType) {
            // 处理类型具备泛型的情况，比如Map<String,Object>,List<SysUser>等具体的泛型类型
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            targetClass = (Class<?>) rawType;
            targetArgumentTypes = parameterizedType.getActualTypeArguments();
            typeParameters = targetClass.getTypeParameters();
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Class<?> componentType = Object.class;

            Type genericComponentType = genericArrayType.getGenericComponentType();
            targetArgumentTypes = new Type[]{genericComponentType};
            if (genericComponentType instanceof TypeVariable) {
                typeParameters = new TypeVariable[]{(TypeVariable) genericComponentType};
            }

            if (genericComponentType instanceof Class) {
                componentType = (Class<?>) genericComponentType;
            } else {

                if (relTypes != null) {
                    Type elemType = relTypes[0];
                    if (elemType instanceof ParameterizedType) {
                        componentType = (Class<?>) ((ParameterizedType) elemType).getRawType();
                    } else if (elemType instanceof Class) {
                        componentType = (Class<?>) elemType;
                    }
                }
            }

            Object arr = Array.newInstance(componentType, 0);
            targetClass = arr.getClass();

        } else {
            targetClass = (Class<?>) type;
        }

        // 目标类型为String，直接转换
        Object cvt = ObjectConvertor.tryConvertAsType(obj, targetClass);
        if (cvt != null && targetArgumentTypes == null) {
            Class<?> cvtClass = cvt.getClass();
            if (TypeOf.typeOf(cvtClass, targetClass)) {
                return (T) cvt;
            }
        }

        // 主要分为Collection,Map,Bean三种，前两种需要默认实现，因为入参可能为interface或者abstract类型，无法实例化
        Class[] defaultImpls = {ArrayList.class, LinkedHashMap.class, LinkedHashSet.class};
        Object ret = null;
        // 如果能实例化就实例化，否则使用默认子类实例化
        try {
            // 如果目标类型为Array，则先按照集合处理，因为数组长度不确定
            if (targetClass.isArray()) {
                ret = new ArrayList<>();
            } else {
                ret = targetClass.newInstance();
            }
        } catch (Exception e) {
            for (Class clazz : defaultImpls) {
                try {
                    if (targetClass.isAssignableFrom(clazz)) {
                        ret = clazz.newInstance();
                        break;
                    }
                } catch (Exception ex) {

                }
            }
        }

        if (ret == null) {
            if (TypeOf.instanceOf(cvt, targetClass)) {
                return (T) cvt;
            }
            throw new IllegalArgumentException("un-support instance type:" + type);
        }

        if (Collection.class.isAssignableFrom(targetClass) || targetClass.isArray()) {
            // 目标类型为Collection
            // 则具有一个泛型参数，获取第0个即可
            Collection col = (Collection) ret;
            Type elementType = null;
            if (targetClass.isArray()) {
                elementType = targetClass.getComponentType();
            } else {
                elementType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[0];
            }

            boolean useRelTypes = false;
            if (elementType instanceof TypeVariable) {
                if (relTypes != null) {
                    elementType = relTypes[0];
                    useRelTypes = true;
                }
            }

            if (objClass.isArray()) {
                // 源类型为数组
                int len = Array.getLength(obj);
                for (int i = 0; i < len; i++) {
                    Object item = Array.get(obj, i);
                    Object elem = convert2Type(item, elementType, useRelTypes ? null : relTypes);
                    col.add(elem);
                }
            } else if (Iterable.class.isAssignableFrom(objClass)) {
                // 源类型为Iterable
                Iterable iter = (Iterable) obj;
                for (Object item : iter) {
                    Object elem = convert2Type(item, elementType, useRelTypes ? null : relTypes);
                    col.add(elem);
                }
            } else if (Iterator.class.isAssignableFrom(objClass)) {
                // 源类型为Iterator
                Iterator iterator = (Iterator) obj;
                while (iterator.hasNext()) {
                    Object item = iterator.hasNext();
                    Object elem = convert2Type(item, elementType, useRelTypes ? null : relTypes);
                    col.add(elem);
                }
            } else if (Enumeration.class.isAssignableFrom(objClass)) {
                // 源类型为Enumeration
                Enumeration enumeration = (Enumeration) obj;
                while (enumeration.hasMoreElements()) {
                    Object item = enumeration.nextElement();
                    Object elem = convert2Type(item, elementType, useRelTypes ? null : relTypes);
                    col.add(elem);
                }
            } else {
                throw new IllegalArgumentException("un-support convert from " + objClass + " to " + targetClass);
            }
        } else if (Map.class.isAssignableFrom(targetClass)) {
            // 目标类型为Map
            Map map = (Map) ret;
            if (Map.class.isAssignableFrom(objClass)) {
                // 源类型为Map
                Map<?, ?> src = (Map<?, ?>) obj;
                for (Map.Entry<?, ?> entry : src.entrySet()) {
                    boolean useRelTypes = false;
                    Type keyType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[0];
                    if (keyType instanceof TypeVariable) {
                        if (relTypes != null) {
                            keyType = relTypes[0];
                            useRelTypes = true;
                        }
                    }

                    Type valType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[1];
                    if (valType instanceof TypeVariable) {
                        if (relTypes != null) {
                            valType = relTypes[useRelTypes ? 1 : 0];
                            useRelTypes = true;
                        }
                    }

                    Object key = convert2Type(entry.getKey(), keyType, useRelTypes ? null : relTypes);
                    Object value = convert2Type(entry.getValue(), valType, useRelTypes ? null : relTypes);
                    map.put(key, value);
                }
            } else {
                // 按照bean处理

                // 否则按照bean处理
                Map<Field, Class<?>> srcFields = ReflectResolver.getFields(objClass);
                Map<String, Field> srcFieldsMap = new LinkedHashMap<>();
                for (Field field : srcFields.keySet()) {
                    srcFieldsMap.put(field.getName(), field);
                }

                for (Map.Entry<String, Field> entry : srcFieldsMap.entrySet()) {
                    Field field = entry.getValue();
                    try {
                        Object value = ReflectResolver.valueGet(obj, field);

                        boolean useRelTypes = false;
                        Type keyType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[0];
                        if (keyType instanceof TypeVariable) {
                            if (relTypes != null) {
                                keyType = relTypes[0];
                                useRelTypes = true;
                            }
                        }

                        Type valType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[1];
                        if (valType instanceof TypeVariable) {
                            if (relTypes != null) {
                                valType = relTypes[useRelTypes ? 1 : 0];
                                useRelTypes = true;
                            }
                        }
                        Object key = convert2Type(entry.getKey(), keyType, useRelTypes ? null : relTypes);
                        value = convert2Type(value, valType, useRelTypes ? null : relTypes);
                        map.put(key, value);
                    } catch (Exception e) {
                        // 处理异常
                    }

                }
            }
        } else {
            // 目标类型为bean
            if (Map.class.isAssignableFrom(objClass)) {
                Map<Field, Class<?>> dstFields = ReflectResolver.getFields(targetClass);
                Map<String, Field> dstFieldsMap = new LinkedHashMap<>();
                for (Field field : dstFields.keySet()) {
                    dstFieldsMap.put(field.getName(), field);
                }

                Map<?, ?> map = (Map<?, ?>) obj;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object key = entry.getKey();
                    String keyName = convert2Type(key, String.class, null);
                    Field field = dstFieldsMap.get(keyName);
                    if (field == null) {
                        continue;
                    }
                    boolean useRelTypes = false;
                    Type fieldType = field.getGenericType();
                    if (fieldType instanceof TypeVariable) {
                        if (typeParameters == null) {
                            if (relTypes != null) {
                                int i = 0;
                                while (i < relTypes.length) {
                                    if (fieldType.equals(relTypes[i])) {
                                        break;
                                    }
                                    i++;
                                }
                                if (i < relTypes.length) {
                                    fieldType = relTypes[i];
                                } else {
                                    fieldType = relTypes[0];
                                }
                                useRelTypes = true;
                            } else {
                                fieldType = Object.class;
                            }
                        } else {
                            int i = 0;
                            while (i < typeParameters.length) {
                                if (fieldType.equals(typeParameters[i])) {
                                    break;
                                }
                                i++;
                            }
                            fieldType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[i];
                        }
                    }
                    try {
                        Object value = convert2Type(entry.getValue(), fieldType, useRelTypes ? null : relTypes);
                        ReflectResolver.valueSet(ret, field, value);
                    } catch (Exception e) {

                    }
                }

            } else {
                // bean 转 bean 处理

                Map<Field, Class<?>> srcFields = ReflectResolver.getFields(objClass);
                Map<String, Field> srcFieldsMap = new LinkedHashMap<>();
                for (Field field : srcFields.keySet()) {
                    srcFieldsMap.put(field.getName(), field);
                }

                Map<Field, Class<?>> dstFields = ReflectResolver.getFields(targetClass);
                Map<String, Field> dstFieldsMap = new LinkedHashMap<>();
                for (Field field : dstFields.keySet()) {
                    dstFieldsMap.put(field.getName(), field);
                }

                // 对同名字段进行复制

                for (Map.Entry<String, Field> entry : srcFieldsMap.entrySet()) {
                    if (!dstFieldsMap.containsKey(entry.getKey())) {
                        continue;
                    }
                    Field srcField = entry.getValue();
                    Field dstField = dstFieldsMap.get(entry.getKey());
                    try {
                        Object value = ReflectResolver.valueGet(obj, srcField);

                        boolean useRelTypes = false;
                        Type fieldType = dstField.getGenericType();
                        if (fieldType instanceof TypeVariable) {
                            if (typeParameters == null) {
                                if (relTypes != null) {
                                    int i = 0;
                                    while (i < relTypes.length) {
                                        if (fieldType.equals(relTypes[i])) {
                                            break;
                                        }
                                        i++;
                                    }
                                    if (i < relTypes.length) {
                                        fieldType = relTypes[i];
                                    } else {
                                        fieldType = relTypes[0];
                                    }
                                    useRelTypes = true;
                                } else {
                                    fieldType = Object.class;
                                }
                            } else {
                                int i = 0;
                                while (i < typeParameters.length) {
                                    if (fieldType.equals(typeParameters[i])) {
                                        break;
                                    }
                                    i++;
                                }
                                fieldType = targetArgumentTypes == null ? Object.class : targetArgumentTypes[i];
                            }
                        }
                        value = convert2Type(value, fieldType, useRelTypes ? null : relTypes);
                        ReflectResolver.valueSet(ret, dstField, value);
                    } catch (Exception e) {
                        // 处理异常
                    }
                }

            }
        }

        // 处理目标类型为Array类型的返回值
        if (targetClass.isArray()) {
            Collection col = (Collection) ret;
            int size = col.size();
            Class<?> componentType = targetClass.getComponentType();
            Object arr = Array.newInstance(componentType, size);
            int i = 0;
            for (Object item : col) {
                Array.set(arr, i, item);
                i++;
            }
            ret = arr;
        }

        return (T) ret;
    }

    /**
     * 查找指定类currentClass继承父类targetClass或者实现接口targetClass是使用的泛型参数
     * 这样就可以得到实现类实现了父中的具体泛型类型
     *
     * @param currentClass
     * @param targetClass
     * @return
     */
    public static Type[] fetchRelType(Class<?> currentClass, Class<?> targetClass) {
        return fetchRelTypeNext(currentClass, targetClass);
    }

    /**
     * 具体的递归查找实现
     *
     * @param currentType
     * @param targetClass
     * @return
     */
    public static Type[] fetchRelTypeNext(Type currentType, Class<?> targetClass) {
        if (currentType.equals(targetClass)) {
            return null;
        }
        if (currentType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) currentType;
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals(targetClass)) {
                return parameterizedType.getActualTypeArguments();
            }
            currentType = rawType;
        }
        if (currentType instanceof TypeVariable) {
            return null;
        }
        Class<?> currentClass = (Class<?>) currentType;
        Type genericSuperclass = currentClass.getGenericSuperclass();
        if (genericSuperclass != null) {
            Type[] types = fetchRelTypeNext(genericSuperclass, targetClass);
            if (types != null) {
                return types;
            }
        }
        Type[] genericInterfaces = currentClass.getGenericInterfaces();
        if (genericInterfaces != null) {
            for (Type genericInterface : genericInterfaces) {
                Type[] types = fetchRelTypeNext(genericInterface, targetClass);
                if (types != null) {
                    return types;
                }
            }
        }
        return null;
    }

}
