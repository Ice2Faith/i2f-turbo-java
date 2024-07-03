package i2f.properties.test;

import i2f.properties.PropertiesUtil;

import java.io.File;
import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2024/7/3 23:07
 * @desc
 */
public class TestProperties {
    public static void main(String[] args) throws Exception {
        File file = new File(".\\i2f-jdk\\i2f-properties\\src\\main\\java\\i2f\\properties\\test\\test.properties");
        Properties properties = PropertiesUtil.load(file);
        TestBeanProperties log = PropertiesUtil.loadAsBean(properties, "log", TestBeanProperties.class);
        System.out.println("ok");
    }
}
