package i2f.jdbc.producer.test;

import i2f.jdbc.producer.executor.SqlProducerExecutor;
import i2f.jdbc.producer.executor.impl.SqlProducerExecutorImpl;
import i2f.jdbc.producer.parser.SqlProducerParser;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 15:30
 */
public class TestProducerExecutor {
    public static void main(String[] args) throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-producer/src/main/java/i2f/jdbc/producer/test.xml");
        XmlNode node = SqlProducerParser.parse(new FileInputStream(file));


        SqlProducerExecutor executor = SqlProducerExecutorImpl.create();

        Map<String, Object> params = new HashMap<>();
        Map<String, XmlNode> nodeMap = new HashMap<>();

        params.put("list", new ArrayList<>(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1)));
        executor.exec(node, params, nodeMap);

    }
}
