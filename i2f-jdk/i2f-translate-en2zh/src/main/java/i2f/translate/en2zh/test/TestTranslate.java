package i2f.translate.en2zh.test;

import i2f.database.metadata.bean.BeanDatabaseMetadataResolver;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.context.impl.DirectJdbcInvokeContextProvider;
import i2f.translate.ITranslator;
import i2f.translate.en2zh.data.TranslateEn2ZhDom;
import i2f.translate.en2zh.impl.SimpleWordTranslator;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/30 18:26
 */
public class TestTranslate {
    public static void main(String[] args) throws Exception {
//        testTranslate();

        testTranslateFile();
    }

    public static void testTranslateFile() {
        try (ITranslator translator = new SimpleWordTranslator()) {
            String text = StreamUtil.readString(new File("i2f-springboot/i2f-springboot-security-starter/src/main/java/i2f/springboot/security/SecurityAutoConfiguration.java"));
            String result = translator.translate(text);
            System.out.println(result);
        } catch (Exception e) {

        }
    }

    public static void testTranslate() throws Exception {

        try (ITranslator translator = new SimpleWordTranslator()) {

            testTranslateByClass(translator, JdbcResolver.class);

            testTranslateByClass(translator, TranslateEn2ZhDom.class);

            testTranslateByClass(translator, DirectJdbcInvokeContextProvider.class);

            testTranslateByClass(translator, BeanDatabaseMetadataResolver.class);

        }
        System.out.println("ok");
    }

    private static void testTranslateByClass(ITranslator translator, Class<?> clazz) throws SQLException {
        String translate = translator.translate(clazz.getSimpleName());
        System.out.println("==========class==========");
        System.out.println(clazz.getName());
        System.out.println(translate);


        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        fields.addAll(Arrays.asList(clazz.getFields()));

        for (Field field : fields) {
            String name = field.getName();
            String translate1 = translator.translate(name);
            System.out.println("==========field==========");
            System.out.println(name);
            System.out.println(translate1);
        }

        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        methods.addAll(Arrays.asList(clazz.getMethods()));

        for (Method method : methods) {
            String name = method.getName();
            String translate1 = translator.translate(name);
            System.out.println("==========method==========");
            System.out.println(name);
            System.out.println(translate1);

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                name = parameter.getName();
                translate1 = translator.translate(name);
                System.out.println("==========parameter==========");
                System.out.println(name);
                System.out.println(translate1);
            }
        }

    }

}
