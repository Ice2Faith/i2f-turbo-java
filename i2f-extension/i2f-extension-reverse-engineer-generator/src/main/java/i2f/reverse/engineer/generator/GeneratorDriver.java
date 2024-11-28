package i2f.reverse.engineer.generator;


import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.bean.BeanDatabaseMetadataResolver;
import i2f.database.metadata.data.TableMeta;
import i2f.extension.velocity.VelocityGenerator;
import i2f.resources.ResourceUtil;
import i2f.reverse.engineer.generator.database.JavaCodeContext;
import i2f.reverse.engineer.generator.database.TableContext;
import i2f.reverse.engineer.generator.er.ErContext;
import i2f.reverse.engineer.generator.er.drawio.DrawioAdapter;
import i2f.reverse.engineer.generator.er.drawio.DrawioErElem;
import i2f.serialize.str.xml.impl.Xml2;
import i2f.spring.mvc.metadata.api.ApiLine;
import i2f.spring.mvc.metadata.api.ApiMethod;
import i2f.spring.mvc.metadata.api.ApiMethodResolver;
import i2f.spring.mvc.metadata.module.ModuleController;
import i2f.spring.mvc.metadata.module.ModuleResolver;
import i2f.text.StringUtils;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/6/15 16:04
 * @desc
 */
public class GeneratorDriver {
    public static String generate(TableMeta table, String template, JavaCodeContext codeCtx) throws Exception {
        TableContext tableCtx = TableContext.parse(table);
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeCtx);
        map.put("table", tableCtx);
        String result = VelocityGenerator.render(template, map);
        return result;
    }

    public static void batch(TableMeta table, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableContext tableCtx = TableContext.parse(table);
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeCtx);
        map.put("table", tableCtx);
        VelocityGenerator.batchRender(templatePath, map, outputPath, "UTF-8");
    }

    public static String generate(Connection conn, String tableName, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DatabaseMetadataProvider.findProvider(conn).getTableInfo(conn, null, tableName);
        return generate(table, template, codeCtx);
    }

    public static void batch(Connection conn, String tableName, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DatabaseMetadataProvider.findProvider(conn).getTableInfo(conn, null, tableName);
        batch(table, templatePath, outputPath, codeCtx);
    }

    public static String generate(Class<?> beanClass, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(beanClass);
        return generate(table, template, codeCtx);
    }

    public static void batch(Class<?> beanClass, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(beanClass);
        batch(table, templatePath, outputPath, codeCtx);
    }


    public static String er(List<TableMeta> tables, String template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ErContext ctx = ErContext.parse(tables);
        map.put("er", ctx);
        return VelocityGenerator.render(template, map);
    }

    public static String er(String template, Class<?>... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class<?> item : beanClasses) {
            TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(item);
            list.add(table);
        }
        return er(list, template);
    }

    public static String er(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        DatabaseMetadataProvider provider = DatabaseMetadataProvider.findProvider(conn);
        for (String item : tableNames) {
            TableMeta table = provider.getTableInfo(conn, null, item);
            list.add(table);
        }
        return er(list, template);
    }

    public static String drawioEr(List<TableMeta> tables, String template) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<DrawioErElem> elems = DrawioAdapter.parseEr(tables);
        map.put("elems", elems);
        return VelocityGenerator.render(template, map);
    }

    public static String drawioEr(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/drawio/er/er.xml.drawio.vm", "UTF-8");
        return drawioEr(tables, tpl);
    }

    public static String doc(List<TableMeta> tables, String template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("tables", tables);
        return VelocityGenerator.render(template, map);
    }

    public static String doc(String template, Class<?>... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class<?> item : beanClasses) {
            TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(item);
            list.add(table);
        }
        return doc(list, template);
    }

    public static String doc(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        DatabaseMetadataProvider provider = DatabaseMetadataProvider.findProvider(conn);
        for (String item : tableNames) {
            TableMeta table = provider.getTableInfo(conn, null, item);
            list.add(table);
        }
        return doc(list, template);
    }


    public static String apis(List<ApiMethod> apis) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/api-doc.html.vm", "UTF-8");
        return apis(apis, tpl);
    }

    public static String apis(List<ApiMethod> apis, String template) throws IOException {
        Map<String, Object> map = new HashMap<>();
        for (ApiMethod item : apis) {
            item.refresh(false, false);
            for (ApiLine line : item.getArgs()) {
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
            for (ApiLine line : item.getReturns()) {
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
        }
        map.put("apis", apis);
        return VelocityGenerator.render(template, map);
    }

    public static String apiVo(Class<?> voClass, String template) throws IOException {
        return apiVo(voClass, null, template);
    }

    public static String apiVo(Class<?> voClass, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        Method[] methods = voClass.getMethods();
        Method[] declaredMethods = voClass.getDeclaredMethods();
        Set<Method> set = new HashSet<>();
        for (Method item : methods) {
            set.add(item);
        }
        for (Method item : declaredMethods) {
            set.add(item);
        }
        for (Method item : set) {
            apis.add(ApiMethodResolver.parseMethod(item, level));
        }
        return apis(apis, template);
    }

    public static String apiMethod(Method method, String template) throws IOException {
        return apiMethod(method, null, template);
    }

    public static String apiMethod(Method method, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        apis.add(ApiMethodResolver.parseMethod(method, level));
        return apis(apis, template);
    }

    public static String apiMvc(Class<?> clazz, String template) throws IOException {
        return apiMvc(clazz, null, template);
    }

    public static String apiMvc(Class<?> clazz, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = ApiMethodResolver.getMvcApiMethods(clazz, level);
        return apis(apis, template);
    }

    public static String apiMvcs(String template, Class<?> clazz, Class<?>... classes) throws IOException {
        return apiMvcs(null, template, clazz, classes);
    }

    public static String apiMvcs(ApiMethodResolver.TraceLevel level, String template, Class<?> clazz, Class<?>... classes) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        apis.addAll(ApiMethodResolver.getMvcApiMethods(clazz, level));
        for (Class<?> item : classes) {
            apis.addAll(ApiMethodResolver.getMvcApiMethods(item, level));
        }
        return apis(apis, template);
    }

    public static String apiMvcsSameComments(ApplicationContext context, Predicate<Class<?>> filter, String template) throws IOException {
        return apiMvcsSameComments(context, filter, null, template);
    }

    public static String apiMvcsSameComments(ApplicationContext context, Predicate<Class<?>> filter, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        return apiMvcsControllers(context, filter, (apis) -> {
            Map<String, String> commentMap = new HashMap<>();
            for (ApiMethod api : apis) {
                List<ApiLine> args = api.getArgs();
                for (ApiLine arg : args) {
                    String name = arg.getName();
                    String comment = arg.getComment();
                    if (!StringUtils.isEmpty(comment)) {
                        commentMap.put(name, comment);
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine line : returns) {
                    String name = line.getName();
                    String comment = line.getComment();
                    if (!StringUtils.isEmpty(comment)) {
                        commentMap.put(name, comment);
                    }
                }
            }

            for (ApiMethod api : apis) {
                List<ApiLine> args = api.getArgs();
                for (ApiLine arg : args) {
                    String name = arg.getName();
                    String comment = arg.getComment();
                    if (StringUtils.isEmpty(comment)) {
                        if (commentMap.containsKey(name)) {
                            arg.setComment(commentMap.get(name));
                        }
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine line : returns) {
                    String name = line.getName();
                    String comment = line.getComment();
                    if (StringUtils.isEmpty(comment)) {
                        if (commentMap.containsKey(name)) {
                            line.setComment(commentMap.get(name));
                        }
                    }
                }
            }
        }, level, template);
    }

    public static String apiMvcsControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, String template) throws IOException {
        return apiMvcsControllers(context, filter, preProcessor, null, template);
    }

    public static String apiMvcsControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        Set<Class<?>> controllers = ApiMethodResolver.getSpringMvcControllers(context);
        List<ApiMethod> apis = new LinkedList<>();
        if (filter != null) {
            for (Class<?> controller : controllers) {
                if (filter.test(controller)) {
                    apis.addAll(ApiMethodResolver.getMvcApiMethods(controller, level));
                }
            }
        }

        if (preProcessor != null) {
            preProcessor.accept(apis);
        }

        return apis(apis, template);
    }


    public static String modulesMvc(List<ModuleController> modules) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/module-doc.html.vm", "UTF-8");
        return modulesMvc(modules, tpl);
    }

    public static String modulesMvc(List<ModuleController> modules, String template) throws IOException {
        Map<String, Object> params = new HashMap<>();
        for (ModuleController item : modules) {
            List<ApiMethod> methods = item.getMethods();
            for (ApiMethod method : methods) {
                List<ApiLine> args = method.getArgs();
                for (ApiLine arg : args) {
                    arg.refresh(false, false);
                    arg.setTypeName(Xml2.toXmlString(arg.getTypeName()));
                    arg.setComment(Xml2.toXmlString(arg.getComment()));
                }
                List<ApiLine> returns = method.getReturns();
                for (ApiLine line : returns) {
                    line.refresh(false, false);
                    line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                    line.setComment(Xml2.toXmlString(line.getComment()));
                }
            }
        }
        params.put("modules", modules);
        return VelocityGenerator.render(template, params);
    }

    public static String modulesMvc(String template, Class<?> controller, Class<?>... controllers) throws IOException {
        List<ModuleController> modules = new LinkedList<>();
        modules.add(ModuleResolver.parse(controller));
        for (Class<?> item : controllers) {
            modules.add(ModuleResolver.parse(item));
        }
        return modulesMvc(modules, template);
    }

    public static String modulesMvcControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ModuleController>> preProcessor, String template) throws IOException {
        Set<Class<?>> controllers = ApiMethodResolver.getSpringMvcControllers(context);
        List<ModuleController> modules = new LinkedList<>();
        if (filter != null) {
            for (Class<?> controller : controllers) {
                if (filter.test(controller)) {
                    modules.add(ModuleResolver.parse(controller));
                }
            }
        }

        if (preProcessor != null) {
            preProcessor.accept(modules);
        }

        return modulesMvc(modules, template);
    }

    public static String tablesDoc(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/table-doc.html.vm", "UTF-8");
        return tablesDoc(tables, tpl);
    }

    public static String tablesDoc(List<TableMeta> tables, String template) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("tables", tables);
        return VelocityGenerator.render(template, params);
    }
}
