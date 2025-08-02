package i2f.template.render.core;


import i2f.convert.obj.ObjectConvertor;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2021/10/22
 */
public class ObjectFinder {
    /**
     * 根据Json的路由匹配模式，得到对象的值
     * 例如：
     * elem.args[2].value@java.lang.Integer.parseInt
     * 其他合法的例子：
     * elem
     * elem.args
     * elem[10]
     * elem.args[2]
     * elem.key@java.lang.Integer.instanceof
     * 这将会获得传入对象的elem属性 的args可迭代对象的第2个元素 的value属性 使用Integer类的parseInt对将value作为入参，其返回的结果作为最终结果
     * 特别的，当方法域(@符号之后最后一个.之后)为instanceof关键字时，表示使用构造方法构造对象
     * 其中当类名没有找到此类，将尝试查找java.lang包下的类，也没有的话将尝试java.utl包，否则视为找不到
     * 使用案例
     * Object val=getObjectByDotKeyWithReference(obj,"elem.args[2].age@java.lang.Integer.parseInt")
     *
     * @param obj
     * @param dotKey
     * @return
     */
    public static Object getObjectByDotKeyWithReference(Object obj, String dotKey) {
        return getObjectByDotKeyWithReference(obj, dotKey, null);
    }

    public static Object getObjectByDotKeyWithReference(Object obj, String dotKey, List<String> baseRefPackages) {
        String simpleDotKey = dotKey;
        String referenceDotKey = null;
        if (dotKey != null) {
            int idx = dotKey.indexOf("@");
            if (idx >= 0) {
                simpleDotKey = dotKey.substring(0, idx);
                referenceDotKey = dotKey.substring(idx + 1);
            }
        }


        Object val = Visitor.visit(simpleDotKey, obj).get();
        val = ObjectFinder.referenceDotKeyConvert(val, referenceDotKey, baseRefPackages);
        return val;
    }

    /**
     * 根据Json的路由匹配模式，得到对象的转换结果
     * 例如：
     * size
     * java.lang.Integer.parseInt
     * 类找不到，将依次尝试java.lang包，java.utl包，java.math包，java.time包，java.io包下的类名，最后以传入对象本身类兜底
     * 因此，
     * 这样也是可以的：
     * String.valueOf 隐含java.lang
     * Integer.parseInt
     * reflect.Array.copyOf 隐含java.util
     * reflect.Field.getDeclareAnnotations
     * 这将会获得传入对象的作为入参值，进行parseInt转换，结果作为最终结果
     * 也就意味着，这种方法是需要满足一定条件的
     * 1.必须应该有返回值，但是也可以没有
     * 2.最多有一个参数
     * 3.这个参数可以接受传入的对象类型，方法不会去管数据类型是否匹配
     * 未指明类名时，默认在传入对象本类查找方法
     * 指明类名时，从对应类名中查找方法
     * 特别的，当函数名为【instanceof】关键字时，意味着是使用构造函数
     *
     * @param obj
     * @param dotKey
     * @return
     */
    public static Object referenceDotKeyConvert(Object obj, String dotKey) {
        return referenceDotKeyConvert(obj, dotKey, null);
    }

    /**
     * 是上个函数的底层依赖，支持@方法指定包名前缀，以减少模板中全类名的书写
     *
     * @param obj
     * @param dotKey
     * @param basePackages
     * @return
     */
    public static Object referenceDotKeyConvert(Object obj, String dotKey, List<String> basePackages) {
        if (dotKey == null || "".equals(dotKey)) {
            return obj;
        }
        dotKey = dotKey.trim();
        if (dotKey == null || "".equals(dotKey)) {
            return obj;
        }
        if (basePackages == null) {
            basePackages = new ArrayList<>();
        }
        /**
         * 添加默认java的基本包
         */
        basePackages.add(0, "java.io");
        basePackages.add(0, "java.time");
        basePackages.add(0, "java.math");
        basePackages.add(0, "java.util");
        basePackages.add(0, "java.lang");

        String refClassName = null;
        String refMethodName = dotKey;
        int idx = dotKey.lastIndexOf(".");
        if (idx >= 0) {
            refClassName = dotKey.substring(0, idx);
            refMethodName = dotKey.substring(idx + 1);
        }
        Class refClass = null;
        if (refClassName != null && !"".equals(refClassName)) {
            refClass = ReflectResolver.loadClass(refClassName);
        }
        if (basePackages != null) {
            for (String item : basePackages) {
                if (refClass != null) {
                    break;
                }
                String langRefCLass = item + "." + refClassName;
                refClass = ReflectResolver.loadClass(langRefCLass);
            }
        }
        if (refClass == null) {
            if (obj != null) {
                refClass = obj.getClass();
            }
        }
        if ("class".equals(refMethodName)) {
            return getConvertObjectByClass(obj, refClass);
        }
        if ("instanceof".equals(refMethodName)) {
            return getConvertObjectByConstructor(obj, refClass);
        }
        return getConvertObjectByMethodName(obj, refClass, refMethodName);
    }

    public static Object getCollectionIndexObj(Collection col, int idx) {
        if (idx < 0) {
            return null;
        }
        int i = 0;
        Object ret = null;
        Iterator it = col.iterator();
        while (it.hasNext()) {
            ret = it.next();
            if (i == idx) {
                break;
            }
            i++;
        }
        if (i == idx) {
            return ret;
        }
        return null;
    }

    public static Field getClassFieldByName(Class clazz, String fieldName) {
        Set<Field> sets = new HashSet<>(48);
        Field[] declare = clazz.getDeclaredFields();
        Field[] fields = clazz.getFields();
        if (declare != null) {
            sets.addAll(Arrays.asList(declare));
        }
        if (fields != null) {
            sets.addAll(Arrays.asList(fields));
        }
        for (Field item : sets) {
            String name = item.getName();
            if (name.equals(fieldName)) {
                return item;
            }
        }
        return null;
    }

    public static Object getConvertObjectByClass(Object obj, Class refClass) {
        if (obj != null) {
            return obj.getClass();
        }
        return refClass;
    }

    public static Object getConvertObjectByMethodName(Object obj, Class refClass, String refMethodName) {
        Set<Method> methods = ReflectResolver.findMethod(refClass, e -> e.getName().equals(refMethodName));
        if (methods.isEmpty()) {
            return obj;
        }
        List<Method> possibleMethod = new ArrayList<>();
        List<Method> matchMethod = new ArrayList<>();
        for (Method item : methods) {
            int pcount = item.getParameterCount();
            Class pclazz = item.getReturnType();
            if (pcount > 1) {
                continue;
            }
            if (pcount == 1) {
                Class typeClass = item.getParameterTypes()[0];
                Object newVal = ObjectConvertor.tryConvertAsType(obj, typeClass);
                if (TypeOf.instanceOf(newVal, typeClass)) {
                    matchMethod.add(item);
                }
            }
            possibleMethod.add(item);
        }
        if (possibleMethod.isEmpty()) {
            return obj;
        }

        boolean isMatch = true;
        Object retVal = obj;
        Method method = possibleMethod.get(0);
        Object ivkObj = obj;
        if (!matchMethod.isEmpty()) {
            isMatch = true;
            method = matchMethod.get(0);
            method.setAccessible(true);

            if (Modifier.isStatic(method.getModifiers())) {
                ivkObj = null;
            } else {
                ivkObj = obj;
                Class objClass = obj.getClass();
                if (objClass.equals(refClass)) {
                    ivkObj = obj;
                } else {
                    try {
                        ivkObj = ReflectResolver.getInstance(refClass);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
        } else {
            isMatch = false;
            method = possibleMethod.get(0);
            method.setAccessible(true);

            if (Modifier.isStatic(method.getModifiers())) {
                ivkObj = null;
            } else {
                ivkObj = obj;
                Class objClass = obj.getClass();
                if (objClass.equals(refClass)) {
                    ivkObj = obj;
                } else {
                    try {
                        ivkObj = ReflectResolver.getInstance(refClass);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }

            }
        }

        retVal = getConvertObjectByInvokeMethod(method, ivkObj, obj, isMatch);

        return retVal;
    }

    public static Object getConvertObjectByInvokeMethod(Method method, Object ivkObj, Object obj, boolean isMatch) {
        Object retVal = obj;
        boolean isVoidRet = void.class.equals(method.getReturnType());
        try {
            if (method.getParameterCount() > 0) {
                Object cvtObj = obj;
                if (isMatch) {
                    Class typeClass = method.getParameterTypes()[0];
                    cvtObj = ObjectConvertor.tryConvertAsType(obj, typeClass);
                }
                if (isVoidRet) {
                    method.invoke(ivkObj, cvtObj);
                    retVal = cvtObj;
                } else {
                    retVal = method.invoke(ivkObj, cvtObj);
                }
            } else {
                if (isVoidRet) {
                    method.invoke(ivkObj);
                    retVal = obj;
                } else {
                    retVal = method.invoke(ivkObj);
                }
            }
        } catch (Exception e) {

        }
        return retVal;
    }


    public static Object getConvertObjectByConstructor(Object obj, Class<?> refClass) {
        Set<Constructor<Object>> constructors = ReflectResolver.getConstructors(refClass);
        List<Constructor> possibleConstructors = new ArrayList<>();
        List<Constructor> matchConstructors = new ArrayList<>();
        for (Constructor item : constructors) {
            int pcount = item.getParameterCount();
            if (pcount > 1) {
                continue;
            }
            if (pcount == 1) {
                Class typeClass = item.getParameterTypes()[0];
                Object newVal = ObjectConvertor.tryConvertAsType(obj, typeClass);
                if (TypeOf.instanceOf(newVal, typeClass)) {
                    matchConstructors.add(item);
                }
            }
            possibleConstructors.add(item);
        }
        if (possibleConstructors.isEmpty()) {
            return obj;
        }
        Object retVal = obj;
        boolean isMatch = false;
        Constructor cons = possibleConstructors.get(0);
        if (!matchConstructors.isEmpty()) {
            isMatch = true;
            cons = matchConstructors.get(0);
        } else {
            isMatch = false;
            cons = possibleConstructors.get(0);
        }
        retVal = getConvertObjectByConstructor(obj, cons, isMatch);

        return retVal;
    }

    public static Object getConvertObjectByConstructor(Object obj, Constructor cons, boolean isMatch) {
        Object retVal = obj;
        try {
            if (cons.getParameterCount() > 0) {
                Object cvtObj = obj;
                if (isMatch) {
                    Class typeCLass = cons.getParameterTypes()[0];
                    cvtObj = ObjectConvertor.tryConvertAsType(obj, typeCLass);
                }
                retVal = cons.newInstance(cvtObj);
            } else {
                retVal = cons.newInstance();
            }
        } catch (Exception e) {

        }
        return retVal;
    }
}
