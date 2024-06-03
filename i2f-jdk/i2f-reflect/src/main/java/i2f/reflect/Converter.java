package i2f.reflect;


import java.lang.reflect.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/6/3 15:23
 * @desc
 */
public class Converter {


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
     * @param obj
     * @param type
     * @return
     * @param <T>
     */
    public static<T> T convert(Object obj,Type type){
        if(obj==null ||type==null){
            return null;
        }

        Class<?> objClass=obj.getClass();

        Class<?> targetClass=null;
        Type[] targetArgumentTypes = null;
        if(type instanceof ParameterizedType){
            // 处理类型具备泛型的情况，比如Map<String,Object>,List<SysUser>等具体的泛型类型
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            targetClass=(Class<?>)rawType;
            targetArgumentTypes = parameterizedType.getActualTypeArguments();
        }else{
            targetClass=(Class<?>)type;
        }

        // 目标类型为String，直接转换
        if(String.class.isAssignableFrom(targetClass)){
            return (T)String.valueOf(obj);
        }

        // 主要分为Collection,Map,Bean三种，前两种需要默认实现，因为入参可能为interface或者abstract类型，无法实例化
        Class[] defaultImpls={ArrayList.class, HashMap.class};
        T ret=null;
        // 如果能实例化就实例化，否则使用默认子类实例化
        try{
            ret=(T)targetClass.newInstance();
        }catch(Exception e){
            for (Class clazz : defaultImpls) {
                try{
                    if(targetClass.isAssignableFrom(clazz)){
                        ret=(T)clazz.newInstance();
                        break;
                    }
                }catch(Exception ex){

                }
            }
        }

        if(ret==null){
            throw new IllegalArgumentException("un-support instance type:"+type);
        }

        if(Collection.class.isAssignableFrom(targetClass)){
            // 目标类型为Collection
            // 则具有一个泛型参数，获取第0个即可
            Collection col=(Collection)ret;

            if(objClass.isArray()){
                // 源类型为数组
                int len= Array.getLength(obj);
                for (int i = 0; i < len; i++) {
                    Object item=Array.get(obj,i);
                    Object elem = convert(item, targetArgumentTypes[0]);
                    col.add(elem);
                }
            }else if(Iterable.class.isAssignableFrom(objClass)){
                // 源类型为Iterable
                Iterable iter = (Iterable) obj;
                for (Object item : iter) {
                    Object elem = convert(item, targetArgumentTypes[0]);
                    col.add(elem);
                }
            }else if(Iterator.class.isAssignableFrom(objClass)){
                // 源类型为Iterator
                Iterator iterator = (Iterator) obj;
                while(iterator.hasNext()){
                    Object item=iterator.hasNext();
                    Object elem = convert(item, targetArgumentTypes[0]);
                    col.add(elem);
                }
            }else if(Enumeration.class.isAssignableFrom(objClass)){
                // 源类型为Enumeration
                Enumeration enumeration = (Enumeration) obj;
                while(enumeration.hasMoreElements()){
                    Object item = enumeration.nextElement();
                    Object elem = convert(item, targetArgumentTypes[0]);
                    col.add(elem);
                }
            }else{
                throw new IllegalArgumentException("un-support convert from "+objClass+" to "+targetClass);
            }
        }else if(Map.class.isAssignableFrom(targetClass)){
            // 目标类型为Map
            Map map=(Map)ret;
            if(Map.class.isAssignableFrom(objClass)){
                // 源类型为Map
                // 直接对拷
                Map<?,?> src=(Map<?,?>)obj;
                for (Map.Entry<?,?> entry : src.entrySet()) {
                    Object key = convert(entry.getKey(), targetArgumentTypes[0]);
                    Object value = convert(entry.getValue(), targetArgumentTypes[1]);
                    map.put(key,value);
                }
            }else{
                // 按照bean处理
                // 先处理基本类型之间的转换
                if(Arrays.asList(int.class,long.class,short.class,char.class,byte.class,boolean.class,float.class,double.class,
                                Integer.class,Long.class,Short.class,Character.class,Byte.class,Boolean.class,Float.class,Double.class)
                        .contains(objClass)
                && Arrays.asList(int.class,long.class,short.class,char.class,byte.class,boolean.class,float.class,double.class,
                                Integer.class,Long.class,Short.class,Character.class,Byte.class,Boolean.class,Float.class,Double.class)
                        .contains(targetClass)
                ){
                    // TODO 数值类型的，使用BigDecimal进行中间转换
                    // 与布尔转换时，采用非零即为true原则
                    // 与char/byte转换时强转为ASCII码值方式

                }else{
                    // 否则按照bean处理
                    Map<String,Field> srcFieldsMap=new LinkedHashMap<>();
                    for (Field field : objClass.getDeclaredFields()) {
                        srcFieldsMap.put(field.getName(),field);
                    }
                    for (Field field : objClass.getFields()) {
                        srcFieldsMap.put(field.getName(),field);
                    }
                    // TODO 其实应该递归获取父类中的fields也一起包含进来

                    for (Map.Entry<String, Field> entry : srcFieldsMap.entrySet()) {
                        Field field = entry.getValue();
                        try{
                            field.setAccessible(true);
                            Object value = field.get(obj);
                            map.put(entry.getKey(),value);
                        }catch(Exception e){
                            // 处理异常
                        }
                    }


                }
            }
        }else{
            // 目标类型为bean
            if(Map.class.isAssignableFrom(objClass)){
                // TODO map 转 bean

            }else{
                // bean 转 bean 处理
                // 先处理基本类型之间的转换
                if(Arrays.asList(int.class,long.class,short.class,char.class,byte.class,boolean.class,float.class,double.class,
                                Integer.class,Long.class,Short.class,Character.class,Byte.class,Boolean.class,Float.class,Double.class)
                        .contains(objClass)
                        && Arrays.asList(int.class,long.class,short.class,char.class,byte.class,boolean.class,float.class,double.class,
                                Integer.class,Long.class,Short.class,Character.class,Byte.class,Boolean.class,Float.class,Double.class)
                        .contains(targetClass)
                ){
                    // TODO 数值类型的，使用BigDecimal进行中间转换
                    // 与布尔转换时，采用非零即为true原则
                    // 与char/byte转换时强转为ASCII码值方式

                }else{
                    // 否则按照bean处理
                    Map<String,Field> srcFieldMap=new LinkedHashMap<>();
                    for (Field field : objClass.getDeclaredFields()) {
                        srcFieldMap.put(field.getName(),field);
                    }
                    for (Field field : objClass.getFields()) {
                        srcFieldMap.put(field.getName(),field);
                    }

                    Map<String,Field> dstFieldMap=new LinkedHashMap<>();
                    for (Field field : targetClass.getDeclaredFields()) {
                        dstFieldMap.put(field.getName(),field);
                    }
                    for (Field field : targetClass.getFields()) {
                        dstFieldMap.put(field.getName(),field);
                    }
                    // TODO 其实应该递归获取父类中的fields也一起包含进来

                    // 对同名字段进行复制

                    for (Map.Entry<String, Field> entry : srcFieldMap.entrySet()) {
                        if(!dstFieldMap.containsKey(entry.getKey())){
                            continue;
                        }
                        Field srcField = entry.getValue();
                        Field dstField = dstFieldMap.get(entry.getKey());
                        try{
                            srcField.setAccessible(true);
                            Object value = srcField.get(obj);

                            dstField.setAccessible(true);
                            dstField.set(ret,value);
                        }catch(Exception e){
                            // 处理异常
                        }
                    }
                }
            }
        }

        return ret;
    }

}
