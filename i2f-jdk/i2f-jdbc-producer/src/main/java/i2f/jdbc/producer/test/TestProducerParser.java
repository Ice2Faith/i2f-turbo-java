package i2f.jdbc.producer.test;

import i2f.jdbc.producer.parser.SqlProducerParser;
import i2f.jdbc.producer.parser.data.XmlNode;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author Ice2Faith
 * @date 2025/1/20 15:30
 */
public class TestProducerParser {
    public static void main(String[] args) throws Exception {
        File file = new File("./i2f-jdk/i2f-jdbc-producer/src/main/java/i2f/jdbc/producer/producer.xml");
        XmlNode node = SqlProducerParser.parse(new FileInputStream(file));

        System.out.println("ok");
    }
}
