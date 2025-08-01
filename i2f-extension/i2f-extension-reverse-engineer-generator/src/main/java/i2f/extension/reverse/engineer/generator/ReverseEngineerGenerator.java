package i2f.extension.reverse.engineer.generator;


import i2f.database.metadata.bean.BeanDatabaseMetadataResolver;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.extension.reverse.engineer.generator.database.JavaCodeContext;
import i2f.extension.reverse.engineer.generator.database.TableContext;
import i2f.extension.reverse.engineer.generator.er.er0.drawio.DatabaseEr0DrawIoGenerator;
import i2f.extension.reverse.engineer.generator.er.er1.drawio.DatabaseEr1DrawIoAdapter;
import i2f.extension.reverse.engineer.generator.er.er1.drawio.DrawIoErElem;
import i2f.extension.reverse.engineer.generator.er.er1.xmlpainter.ErContext;
import i2f.extension.reverse.engineer.generator.er.er2.drawio.DatabaseEr2DrawIoGenerator;
import i2f.extension.velocity.VelocityGenerator;
import i2f.resources.ResourceUtil;
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
 * @author Ice2Faith
 * @date 2022/6/15 16:04
 * @desc
 */
public class ReverseEngineerGenerator {
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
        TableMeta table = DatabaseMetadataProviders.findProvider(conn).getTableInfo(conn, null, tableName);
        return generate(table, template, codeCtx);
    }

    public static void batch(Connection conn, String tableName, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DatabaseMetadataProviders.findProvider(conn).getTableInfo(conn, null, tableName);
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

    public static String er0DrawIo(List<TableMeta> tables) throws IOException {
        return DatabaseEr0DrawIoGenerator.render(tables);
    }

    public static String er1XmlPainter(List<TableMeta> tables) throws Exception {
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/er/er1/xmlpainter/er-1.xml.vm", "UTF-8");
        return er1(tables, tpl);
    }

    public static String er1(List<TableMeta> tables, String template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ErContext ctx = ErContext.parse(tables);
        map.put("er", ctx);
        return VelocityGenerator.render(template, map);
    }

    public static String er1(String template, Class<?>... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class<?> item : beanClasses) {
            TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(item);
            list.add(table);
        }
        return er1(list, template);
    }

    public static String er1(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
        for (String item : tableNames) {
            TableMeta table = provider.getTableInfo(conn, null, item);
            list.add(table);
        }
        return er1(list, template);
    }

    public static String er1DrawIo(List<TableMeta> tables, String template) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<DrawIoErElem> elems = DatabaseEr1DrawIoAdapter.parseEr(tables);
        map.put("elems", elems);
        return VelocityGenerator.render(template, map);
    }

    public static String er1DrawIo(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/er/er1/drawio/er-1.xml.drawio.vm", "UTF-8");
        return er1DrawIo(tables, tpl);
    }

    public static String er2DrawIo(List<TableMeta> tables) throws IOException {
        return DatabaseEr2DrawIoGenerator.render(tables);
    }


    public static String tablesDesignDoc(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/table-design.html.vm", "UTF-8");
        return tablesDoc(tables, tpl);
    }

    public static String tablesStructDoc(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/table/table-struct.html.vm", "UTF-8");
        return tablesDoc(tables, tpl);
    }

    public static String tablesDdlMysql(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/ddl/table-ddl.mysql.sql.vm", "UTF-8");
        return tablesDoc(tables, tpl);
    }

    public static String tablesDdlOracle(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/ddl/table-ddl.oracle.sql.vm", "UTF-8");
        return tablesDoc(tables, tpl);
    }

    public static String tablesDoc(List<TableMeta> tables, String template) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("tables", tables);
        return VelocityGenerator.render(template, params);
    }

    public static String tablesDoc(String template, Class<?>... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class<?> item : beanClasses) {
            TableMeta table = BeanDatabaseMetadataResolver.getTableMeta(item);
            list.add(table);
        }
        return tablesDoc(list, template);
    }

    public static String tablesDoc(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
        for (String item : tableNames) {
            TableMeta table = provider.getTableInfo(conn, null, item);
            list.add(table);
        }
        return tablesDoc(list, template);
    }


    public static String apisDesign(List<ApiMethod> apis) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/api-design.html.vm", "UTF-8");
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

    public static String apisDesignMvcSameComments(ApplicationContext context) throws IOException {
        return apisDesignMvcSameComments(context, null, null);
    }

    public static String apisDesignMvcSameComments(ApplicationContext context, ApiMethodResolver.TraceLevel level) throws IOException {
        return apisDesignMvcSameComments(context, null, level);
    }

    public static String apisDesignMvcSameComments(ApplicationContext context, Predicate<Class<?>> filter, ApiMethodResolver.TraceLevel level) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/api-design.html.vm", "UTF-8");
        return apiMvcsSameComments(context, filter, level, tpl);
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

    public static String apisDesignMvcControllers(ApplicationContext context) throws IOException {
        return apisDesignMvcControllers(context, null, null, ApiMethodResolver.TraceLevel.BASIC);
    }

    public static String apisDesignMvcControllers(ApplicationContext context, ApiMethodResolver.TraceLevel level) throws IOException {
        return apisDesignMvcControllers(context, null, null, level);
    }

    public static String apisDesignMvcControllers(ApplicationContext context, Consumer<List<ApiMethod>> preProcessor, ApiMethodResolver.TraceLevel level) throws IOException {
        return apisDesignMvcControllers(context, null, preProcessor, level);
    }

    public static String apisDesignMvcControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, ApiMethodResolver.TraceLevel level) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/api-design.html.vm", "UTF-8");
        return apiMvcsControllers(context, filter, preProcessor, level, tpl);
    }

    public static String apiMvcsControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        Set<Class<?>> controllers = ApiMethodResolver.getSpringMvcControllers(context);
        List<ApiMethod> apis = new LinkedList<>();
        for (Class<?> controller : controllers) {
            if (filter == null || filter.test(controller)) {
                apis.addAll(ApiMethodResolver.getMvcApiMethods(controller, level));
            }
        }

        if (preProcessor != null) {
            preProcessor.accept(apis);
        }

        return apis(apis, template);
    }

    public static String modulesDesignMvc(List<ModuleController> modules) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/module-design.html.vm", "UTF-8");
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

    public static String modulesDesignMvc(Class<?> controller, Class<?>... controllers) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/module-design.html.vm", "UTF-8");
        return modulesMvc(tpl, controller, controllers);
    }

    public static String modulesMvc(String template, Class<?> controller, Class<?>... controllers) throws IOException {
        List<ModuleController> modules = new LinkedList<>();
        modules.add(ModuleResolver.parse(controller));
        for (Class<?> item : controllers) {
            modules.add(ModuleResolver.parse(item));
        }
        return modulesMvc(modules, template);
    }

    public static String modulesDesignMvcControllers(ApplicationContext context) throws IOException {
        return modulesDesignMvcControllers(context, null, null);
    }

    public static String modulesDesignMvcControllers(ApplicationContext context, Consumer<List<ModuleController>> preProcessor) throws IOException {
        return modulesDesignMvcControllers(context, null, preProcessor);
    }

    public static String modulesDesignMvcControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ModuleController>> preProcessor) throws IOException {
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/design/module-design.html.vm", "UTF-8");
        return modulesMvcControllers(context, filter, preProcessor, tpl);
    }

    public static String modulesMvcControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ModuleController>> preProcessor, String template) throws IOException {
        Set<Class<?>> controllers = ApiMethodResolver.getSpringMvcControllers(context);
        List<ModuleController> modules = new LinkedList<>();
        for (Class<?> controller : controllers) {
            if (filter == null || filter.test(controller)) {
                modules.add(ModuleResolver.parse(controller));
            }
        }

        if (preProcessor != null) {
            preProcessor.accept(modules);
        }

        return modulesMvc(modules, template);
    }

}
