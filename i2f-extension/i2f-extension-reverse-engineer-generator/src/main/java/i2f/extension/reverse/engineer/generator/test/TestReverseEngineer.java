package i2f.extension.reverse.engineer.generator.test;

import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.data.TableMeta;
import i2f.extension.reverse.engineer.generator.ReverseEngineerGenerator;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.resources.ResourceUtil;
import i2f.spring.mvc.metadata.api.ApiMethod;
import i2f.spring.mvc.metadata.api.ApiMethodResolver;
import i2f.spring.mvc.metadata.module.ModuleController;
import i2f.spring.mvc.metadata.module.ModuleResolver;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/11/28 20:09
 * @desc
 */
public class TestReverseEngineer {
    public static void main(String[] args) throws Exception {
//        testDatabase();

        testApi();


    }

    public static void testApi() throws Exception {
        Class<?> clazz = ReverseEngineerGenerator.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();

        List<ApiMethod> apiMethods = new ArrayList<>();

        for (Method method : declaredMethods) {
            ApiMethod apiMethod = ApiMethodResolver.parseMethod(method);
            apiMethods.add(apiMethod);
        }


        File dir = new File("./output");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String result = ReverseEngineerGenerator.apis(apiMethods);
        File output = new File(dir, "api-design.html");
        StreamUtil.writeString(result, output);

        ModuleController module = ModuleResolver.parse(ReverseEngineerGenerator.class);
        result = ReverseEngineerGenerator.modulesMvc(Arrays.asList(module));
        output = new File(dir, "module-design.html");
        StreamUtil.writeString(result, output);


        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/api/api.html.vm", "UTF-8");
        result = ReverseEngineerGenerator.apis(apiMethods, tpl);
        output = new File(dir, "api.html");
        StreamUtil.writeString(result, output);

        System.out.println("ok");
    }

    public static void testDatabase() throws Exception {
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        for (Driver driver : load) {
            System.out.println(driver.getClass());
        }
        Connection conn = JdbcResolver.getConnection("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai",
                "root",
                "xxx123456");

        DatabaseMetadataProvider provider = DatabaseMetadataProvider.findProvider(conn);

        List<String> catalogs = provider.getCatalogs(conn);

        List<String> schemas = provider.getSchemas(conn);

        List<String> databases = provider.getDatabases(conn);

        List<TableMeta> tables = provider.getTables(conn, "test_db");

        List<TableMeta> tableMetas = new ArrayList<>();
        for (TableMeta table : tables) {
            TableMeta tableMeta = provider.getTableInfo(conn, table.getDatabase(), table.getName());
            tableMetas.add(tableMeta.fillColumnIndexMeta());
        }


        File dir = new File("./output");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String result = ReverseEngineerGenerator.tablesDesignDoc(tableMetas);
        File output = new File(dir, "tables-design.html");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.tablesStructDoc(tableMetas);
        output = new File(dir, "tables-struct.html");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.tablesDdlMysql(tableMetas);
        output = new File(dir, "tables-ddl.mysql.sql");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.tablesDdlOracle(tableMetas);
        output = new File(dir, "tables-ddl.oracle.sql");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.erDrawio(tableMetas);
        output = new File(dir, "er.xml.drawio");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.er(tableMetas);
        output = new File(dir, "er.xml");
        StreamUtil.writeString(result, output);

        conn.close();

        System.out.println("ok");
    }
}
