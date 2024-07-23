package i2f.extension.slf4j.log.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:53
 * @desc
 */
public class TestSlf4jLogAdapter {
    public static void main(String[] args){
        Logger logger= LoggerFactory.getLogger(TestSlf4jLogAdapter.class);
        logger.info("adapter");
    }
}
