package i2f.springboot.ops.app.data.metadata;

import i2f.springboot.ops.app.util.AppUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:40
 * @desc
 */
@Data
@NoArgsConstructor
public class AppClassDto {
    protected List<String> modifiers;
    protected List<AppFieldDto> fieldList;
    protected List<AppMethodDto> methodList;
    protected List<AppClassNameDto> interfaceList;
    protected AppClassNameDto superClass;

    public static AppClassDto of(Class<?> clazz) {
        AppClassDto ret = new AppClassDto();
        ret.modifiers = AppUtil.resolveModifier(clazz.getModifiers());
        ret.fieldList = new ArrayList<>();
        ret.methodList = new ArrayList<>();
        ret.interfaceList = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        ret.superClass = AppClassNameDto.of(superclass == null ? Void.class : superclass);
        Set<Field> fields = new LinkedHashSet<>();
        try {
            fields.addAll(Arrays.asList(clazz.getFields()));
        } catch (Exception e) {

        }
        try {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        } catch (Exception e) {

        }
        for (Field field : fields) {
            ret.fieldList.add(AppFieldDto.of(field));
        }
        Set<Method> methods = new LinkedHashSet<>();
        try {
            methods.addAll(Arrays.asList(clazz.getMethods()));
        } catch (Exception e) {

        }
        try {
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        } catch (Exception e) {

        }
        for (Method method : methods) {
            ret.methodList.add(AppMethodDto.of(method));
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class<?> item : interfaces) {
                ret.interfaceList.add(AppClassNameDto.of(item));
            }
        }

        return ret;
    }
}
