package i2f.jdbc.procedure.test;

import i2f.jdbc.datasource.impl.DirectConnectionDatasource;
import i2f.jdbc.meta.JdbcMeta;
import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.impl.BasicJdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 15:30
 */
public class TestProcedureExecutor {
    public static void main(String[] args) throws Exception {
//        testProcedure();

        testBasic();
    }

    public static void testProcedure() throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-procedure/src/main/java/i2f/jdbc/procedure/test/test-procedure.xml");
        XmlNode node = JdbcProcedureParser.parse(file);

        BasicJdbcProcedureExecutor executor = new DefaultJdbcProcedureExecutor();
        executor.getDebug().set(true);

        Map<String, Object> params = executor.createParams();
        ExecuteContext context = new ExecuteContext(params);

        Map<String, DataSource> datasourceMap = (Map<String, DataSource>) params.get(ParamsConsts.DATASOURCES);

        datasourceMap.put("local",
                new DirectConnectionDatasource(
                        new JdbcMeta(
                                "oracle.jdbc.driver.OracleDriver",
                                "jdbc:oracle:thin:@localhost:1521/orcl",
                                "admin",
                                "123456"
                        )
                )
        );
        datasourceMap.put(ParamsConsts.DEFAULT_DATASOURCE, datasourceMap.get("local"));
        datasourceMap.put("oracle",
                new DirectConnectionDatasource(
                        new JdbcMeta(
                                "oracle.jdbc.driver.OracleDriver",
                                "jdbc:oracle:thin:@localhost:1521/orcl",
                                "test",
                                "123456"
                        )
                )
        );
        datasourceMap.put("gbase",
                new DirectConnectionDatasource(
                        new JdbcMeta(
                                "com.gbase.jdbc.Driver",
                                "jdbc:gbase://localhost:5258/spsv_dev",
                                "admin",
                                "123456"
                        )
                )
        );


        executor.exec(node, context, false, true);
    }

    public static void testBasic() throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-procedure/src/main/java/i2f/jdbc/procedure/test/test-basic.xml");
        XmlNode node = JdbcProcedureParser.parse(file);

//        BasicJdbcProcedureExecutor executor = new BasicJdbcProcedureExecutor();

        BasicJdbcProcedureExecutor executor = new DefaultJdbcProcedureExecutor();
        executor.getDebug().set(true);

        Map<String, Object> params = executor.createParams();
        ExecuteContext context = new ExecuteContext(params);

        context.getParams().put("list", new ArrayList<>(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1)));

        ProcedureMeta nodeMeta = ProcedureMeta.ofMeta(node);
        context.getNodeMap().put(nodeMeta.getName(), nodeMeta);

        TestSimpleJavaCaller caller = new TestSimpleJavaCaller();
        ProcedureMeta javaMeta = ProcedureMeta.ofMeta(caller);
        context.getNodeMap().put("SIMPLE", javaMeta);

        context.getNodeMap().put(javaMeta.getName(), javaMeta);

        executor.exec(node, context, false, true);

        executor.exec(javaMeta.getName(), context);

        executor.exec("SIMPLE",context);

        System.out.println("===========================================");
        JdbcProcedureExecutorCaller executorCaller = new DefaultJdbcProcedureExecutorCaller(executor,
                new JdbcProcedureContext(context.getNodeMap()));

        executorCaller.call("SIMPLE", context.getParams());

        executorCaller.call("BASIC", context.getParams());
    }
}
