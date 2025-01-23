package i2f.jdbc.procedure.test;

import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.impl.BasicJdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 15:30
 */
public class TestProcedureExecutor {
    public static void main(String[] args) throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-procedure/src/main/java/i2f/jdbc/procedure/test/test-basic.xml");
        XmlNode node = JdbcProcedureParser.parse(new FileInputStream(file));

//        BasicJdbcProcedureExecutor executor = new BasicJdbcProcedureExecutor();

        BasicJdbcProcedureExecutor executor = new DefaultJdbcProcedureExecutor();
        executor.getDebug().set(true);

        Map<String, Object> params = executor.createParams();
        ExecuteContext context = new ExecuteContext(params);

        context.getParams().put("list", new ArrayList<>(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1)));
        executor.exec(node, context);

    }
}
