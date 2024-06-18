package i2f.reflect.test;

import i2f.reflect.ReflectResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/5 21:38
 * @desc
 */
public class TestAnnotation {
    public static void main(String[] args) {
        Class<?> clazz = TestAnnotation.class;
        Method method = ReflectResolver.getMethod(clazz, "test");
        AnnName ann = ReflectResolver.findAnnotation(method, AnnName.class);
        Class<? extends AnnName> annClass = ann.getClass();
        Class<? extends Annotation> typeClass = ann.annotationType();

        Map<String, Object> values = ReflectResolver.getAnnotationAllValues(method, AnnName.class);
        String[] tags = ReflectResolver.getAnnotationValue(method, AnnName.class, "tags");
        System.out.println("ok");
    }

    @AnnName(value = "test", prefer = true, tags = {"add", "update"})
    public static void test() {

    }
}
