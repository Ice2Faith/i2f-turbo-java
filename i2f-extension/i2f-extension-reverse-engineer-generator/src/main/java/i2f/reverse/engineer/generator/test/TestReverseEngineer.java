package i2f.reverse.engineer.generator.test;

import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.data.TableMeta;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.reverse.engineer.generator.ReverseEngineerGenerator;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/11/28 20:09
 * @desc
 */
public class TestReverseEngineer {
    public static void main(String[] args) throws Exception {
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

        result = ReverseEngineerGenerator.drawioEr(tableMetas);
        output = new File(dir, "er.xml.drawio");
        StreamUtil.writeString(result, output);

        result = ReverseEngineerGenerator.er(tableMetas);
        output = new File(dir, "er.xml");
        StreamUtil.writeString(result, output);

//        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/doc/table-ddl.oracle.sql.vm", "UTF-8");
//        result = ReverseEngineerGenerator.tablesDoc(tableMetas,tpl);
//        output=new File(dir,"tables-ddl.oracle.sql");
//        StreamUtil.writeString(result,output);

        conn.close();

        System.out.println("ok");
    }
}
