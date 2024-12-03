# useage

- add those maven dependency into your dependencies

```xml

<dependency>
    <groupId>i2f.turbo</groupId>
    <artifactId>i2f-extension-reverse-engineer-generator</artifactId>
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${pom.basedir}/lib/i2f-extension-reverse-engineer-generator-1.0.jar</systemPath>
</dependency>

<dependency>
<groupId>i2f.turbo</groupId>
<artifactId>translate-en2zh</artifactId>
<version>1.0</version>
<scope>system</scope>
<systemPath>${pom.basedir}/lib/i2f-translate-en2zh-1.0.jar</systemPath>
</dependency>

<dependency>
<groupId>org.xerial</groupId>
<artifactId>sqlite-jdbc</artifactId>
<version>3.43.0.0</version>
</dependency>

<dependency>
<groupId>org.apache.velocity</groupId>
<artifactId>velocity</artifactId>
<version>1.7</version>
</dependency>

```

- add this class into your springboot application

```java
package i2f.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.extension.reverse.engineer.generator.ReverseEngineerGenerator;
import i2f.extension.reverse.engineer.generator.database.TableContext;
import i2f.extension.reverse.engineer.generator.database.JavaCodeContext;
import i2f.extension.velocity.VelocityGenerator;
import i2f.io.stream.StreamUtil;
import i2f.resources.ResourceUtil;
import i2f.spring.mvc.metadata.api.ApiLine;
import i2f.spring.mvc.metadata.api.ApiMethod;
import i2f.spring.mvc.metadata.module.ModuleController;
import i2f.text.StringUtils;
import i2f.translate.en2zh.impl.SimpleWordTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/11/30 16:15
 */
@Component
public class TestGeneratorRunner implements ApplicationRunner {
    public static final Logger log = LoggerFactory.getLogger(TestGeneratorRunner.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String appName = context.getEnvironment().getProperty("spring.application.name", "noappname");
        String baseUrl = "";

        log.info("generate appname {}", appName);
        log.info("generate baseUrl {}", baseUrl);

        File dir = new File("./output/" + appName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        log.info("generate output {}", dir.getAbsolutePath());

        String result = "";
        File output = new File(dir, "tmp.tmp");

        SimpleWordTranslator translator = new SimpleWordTranslator();

        Map<String, String> map = new HashMap<>();
        map.put("xxljob", "XXL任务");
        map.put("xxl", "XXL");
        map.put("id", "ID");
        map.put("pwd", "密码");
        map.put("html", "页面");

        String contextPath = StringUtils.concatPath(baseUrl, context.getEnvironment().getProperty("server.servlet.context-path", "/"));

        log.info("generate context path {}", contextPath);

        translator.getPriorWordTranslateMap().putAll(map);

        if (true) {
            log.info("begin generate database doc ...");
            Connection conn = dataSource.getConnection();

            DatabaseMetadataProvider provider = DatabaseMetadataProvider.findProvider(conn);

            log.info("generate attach metadata provider {}", provider);

            log.info("generate fetch database metadata ...");
            List<TableMeta> tableMetas = new ArrayList<>();

            List<String> databaseList = Arrays.asList("PUBLIC");
            for (String database : databaseList) {
                List<TableMeta> tables = provider.getTables(conn, database);

                for (TableMeta table : tables) {
                    String lowerCase = table.getName().toLowerCase();
                    if (lowerCase.startsWith("dynamic_")) {
                        continue;
                    }
                    TableMeta tableInfo = provider.getTableInfo(conn, database, table.getName());
                    tableMetas.add(tableInfo);
                }

            }


            conn.close();

            log.info("generate convert database metadata ...");
            for (TableMeta tableMeta : tableMetas) {
                tableMeta.fillColumnIndexMeta();
                if (StringUtils.isEmpty(tableMeta.getComment())) {
                    tableMeta.setComment(translator.translate(tableMeta.getName()).replaceAll("_", ""));
                }
                List<ColumnMeta> columns = tableMeta.getColumns();
                for (ColumnMeta column : columns) {
                    if (StringUtils.isEmpty(column.getComment())) {
                        column.setComment(translator.translate(column.getName()).replaceAll("_", ""));
                    }
                }
            }

            log.info("generate database table metas json ...");
            result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tableMetas);
            output = new File(dir, "table-metas.json");
            StreamUtil.writeString(result, output);
            log.info("generate database table metas json .");

            log.info("generate database table design doc ...");
            result = ReverseEngineerGenerator.tablesDesignDoc(tableMetas);
            output = new File(dir, "table-design.html");
            StreamUtil.writeString(result, output);
            log.info("generate database table design doc end.");

            log.info("generate database table er-0 drawio ...");
            result = ReverseEngineerGenerator.er0DrawIo(tableMetas);
            output = new File(dir, "table-er-0.xml.drawio");
            StreamUtil.writeString(result, output);
            log.info("generate database table er-0 drawio end.");

            log.info("generate database table er-1 drawio ...");
            result = ReverseEngineerGenerator.er1DrawIo(tableMetas);
            output = new File(dir, "table-er-1.xml.drawio");
            StreamUtil.writeString(result, output);
            log.info("generate database table er-1 drawio end.");

            log.info("generate database table er-2 drawio ...");
            result = ReverseEngineerGenerator.er2DrawIo(tableMetas);
            output = new File(dir, "table-er-2.xml.drawio");
            StreamUtil.writeString(result, output);
            log.info("generate database table er-2 drawio end.");

            log.info("generate database table er-1 xml-painter ...");
            result = ReverseEngineerGenerator.er1XmlPainter(tableMetas);
            output = new File(dir, "table-er-1.xml-painter.xml");
            StreamUtil.writeString(result, output);
            log.info("generate database table er-1 xml-painter end.");

            log.info("generate database table ddl mysql ...");
            result = ReverseEngineerGenerator.tablesDdlMysql(tableMetas);
            output = new File(dir, "table-ddl.mysql.sql");
            StreamUtil.writeString(result, output);
            log.info("generate database table ddl mysql end.");

            log.info("generate database table ddl oracle ...");
            result = ReverseEngineerGenerator.tablesDdlOracle(tableMetas);
            output = new File(dir, "table-ddl.oracle.sql");
            StreamUtil.writeString(result, output);
            log.info("generate database table ddl oracle end.");

            log.info("generate database table struct ...");
            result = ReverseEngineerGenerator.tablesStructDoc(tableMetas);
            output = new File(dir, "table-struct.html");
            StreamUtil.writeString(result, output);
            log.info("generate database table struct end.");

            log.info("generate database docs end.");

            if (!tableMetas.isEmpty()) {
                log.info("generate database table code render params ...");
                TableMeta table = tableMetas.get(0);

                JavaCodeContext codeCtx = new JavaCodeContext();
                codeCtx.setPkg("i2f.test");
                codeCtx.setAuthor("Ice2Faith");
                TableContext tableCtx = TableContext.parse(table);
                Map<String, Object> codeParams = new HashMap<>();
                codeParams.put("code", codeCtx);
                codeParams.put("table", tableCtx);

                result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(codeParams);
                output = new File(dir, "code-render-params.json");
                StreamUtil.writeString(result, output);
                log.info("generate database table code render params end.");


                // VelocityGenerator.batchRender("./output/tpl/code",
                //         codeParams,
                //         "./output/tpl/result",
                //         "UTF-8");
            }
        }


        translator.getPriorWordTranslateMap().remove("set");

        log.info("generate api design doc ...");
        result = ReverseEngineerGenerator.apisDesignMvcControllers(context, e -> {
            if (e.getName().startsWith("org.springframework.")) {
                return false;
            }
            return true;
        }, (apis) -> {
            for (ApiMethod api : apis) {
                List<String> urls = api.getUrls();
                urls.replaceAll(e -> StringUtils.concatPath(contextPath, e));
                if (StringUtils.isEmpty(api.getComment())) {
                    Method srcMethod = api.getSrcMethod();
                    Class<?> declaringClass = srcMethod.getDeclaringClass();
                    String className = declaringClass.getSimpleName();
                    if (className.endsWith("Controller")) {
                        className = className.substring(0, className.length() - "Controller".length());
                    }
                    String name = api.getSrcMethodName();
                    api.setComment(translator.translate(className) + " >> " + translator.translate(name));
                }
                if (!api.getComment().contains(">>")) {
                    Method srcMethod = api.getSrcMethod();
                    String name = srcMethod.getName();
                    String trans = translator.translate(name);
                    if (!trans.equals(api.getComment())) {
                        api.setComment(api.getComment() + " >> " + trans);
                    }
                }
                List<ApiLine> args1 = api.getArgs();
                for (ApiLine apiLine : args1) {
                    if (StringUtils.isEmpty(apiLine.getComment())) {
                        apiLine.setComment(translator.translate(apiLine.getName()));
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine aReturn : returns) {
                    if (StringUtils.isEmpty(aReturn.getComment())) {
                        aReturn.setComment(translator.translate(aReturn.getName()));
                    }
                }
            }
        }, null);
        output = new File(dir, "api-design.html");
        StreamUtil.writeString(result, output);

        log.info("generate api design doc end.");


        log.info("generate module design doc ...");
        result = ReverseEngineerGenerator.modulesDesignMvcControllers(context, e -> {
            if (e.getName().startsWith("org.springframework.")) {
                return false;
            }
            return true;
        }, (modules) -> {
            for (ModuleController module : modules) {
                if (StringUtils.isEmpty(module.getComment())) {
                    module.setComment(translator.translate(module.getClassName()));
                }
                List<ApiMethod> methods = module.getMethods();
                for (ApiMethod method : methods) {
                    if (StringUtils.isEmpty(method.getComment())) {
                        method.setComment(translator.translate(method.getSrcMethodName()));
                    }
                }
            }
        });
        output = new File(dir, "module-design.html");
        StreamUtil.writeString(result, output);

        log.info("generate module design doc end.");

        log.info("generate api doc ...");
        String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/api/api.html.vm", "UTF-8");
        result = ReverseEngineerGenerator.apiMvcsControllers(context, e -> {
            if (e.getName().startsWith("org.springframework.")) {
                return false;
            }
            return true;
        }, (apis) -> {
            for (ApiMethod api : apis) {
                List<String> urls = api.getUrls();
                urls.replaceAll(e -> StringUtils.concatPath(contextPath, e));
                if (StringUtils.isEmpty(api.getComment())) {
                    Method srcMethod = api.getSrcMethod();
                    Class<?> declaringClass = srcMethod.getDeclaringClass();
                    String className = declaringClass.getSimpleName();
                    if (className.endsWith("Controller")) {
                        className = className.substring(0, className.length() - "Controller".length());
                    }
                    String name = api.getSrcMethodName();
                    api.setComment(translator.translate(className) + "-" + translator.translate(name));
                }
                List<ApiLine> args1 = api.getArgs();
                for (ApiLine apiLine : args1) {
                    if (StringUtils.isEmpty(apiLine.getComment())) {
                        apiLine.setComment(translator.translate(apiLine.getName()));
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine aReturn : returns) {
                    if (StringUtils.isEmpty(aReturn.getComment())) {
                        aReturn.setComment(translator.translate(aReturn.getName()));
                    }
                }
            }
        }, null, tpl);
        output = new File(dir, "api.html");
        StreamUtil.writeString(result, output);

        log.info("generate api doc end.");

        log.info("generate finished!");
    }
}

```

- start your application
- and wait console output this, are ok!

```text
generate finished!
```

- now, you can find generate docs in this folder

```shell
./output
```
