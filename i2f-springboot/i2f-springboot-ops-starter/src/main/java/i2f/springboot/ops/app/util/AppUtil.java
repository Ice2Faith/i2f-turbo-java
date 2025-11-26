package i2f.springboot.ops.app.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:57
 * @desc
 */
public class AppUtil {
    public static Class<?> findClass(String className){
        if(className==null){
            return null;
        }
        try{
            Class<?> clazz = Class.forName(className);
            if(clazz!=null){
                return clazz;
            }
        }catch(Throwable e){

        }
        try{
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            if(clazz!=null){
                return clazz;
            }
        }catch(Throwable e){

        }
        return null;
    }

    public static Class<?> loadClass(String className) throws Exception {
        Class<?> clazz = findClass(className);
        if(clazz!=null){
            return clazz;
        }
        return findClass(className.replace("$","."));
    }

    public static List<String> resolveModifier(int mod){
        List<String> ret=new ArrayList<>();
        if(Modifier.isPublic(mod)){
            ret.add("public");
        }
        if(Modifier.isProtected(mod)){
            ret.add("protected");
        }
        if(Modifier.isPrivate(mod)){
            ret.add("private");
        }
        if(Modifier.isStatic(mod)){
            ret.add("static");
        }
        if(Modifier.isFinal(mod)){
            ret.add("final");
        }
        if(Modifier.isAbstract(mod)){
            ret.add("abstract");
        }
        if(Modifier.isInterface(mod)){
            ret.add("interface");
        }
        if(Modifier.isNative(mod)){
            ret.add("native");
        }
        if(Modifier.isSynchronized(mod)){
            ret.add("synchronized");
        }
        if(Modifier.isTransient(mod)){
            ret.add("transient");
        }
        if(Modifier.isVolatile(mod)){
            ret.add("volatile");
        }
        return ret;
    }
}
