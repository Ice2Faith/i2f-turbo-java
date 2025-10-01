package i2f.jdbc.procedure.test;

import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2025/1/20 15:30
 */
public class TestProcedureParser {
    public static void main(String[] args) throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-procedure/src/test/java/i2f/jdbc/procedure/test/test-basic.xml");
        XmlNode node = JdbcProcedureParser.parse(file);

        System.out.println("ok");
    }
}
