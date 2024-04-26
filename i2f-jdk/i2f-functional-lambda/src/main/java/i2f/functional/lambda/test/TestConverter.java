package i2f.functional.lambda.test;

import i2f.functional.lambda.Lambdas;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/4/22 15:39
 * @desc
 */
public class TestConverter {
    public static void main(String[] args) {
        System.out.println(Lambdas.METHOD.get(TestConverter::consumer));
        System.out.println(Lambdas.METHOD.get(TestConverter::consumerInt));
        System.out.println(Lambdas.METHOD.get(TestConverter::consumerException));
        System.out.println(Lambdas.METHOD.get(TestConverter::consumerIoException));
        System.out.println(Lambdas.METHOD.get(TestConverter::consumerMulException));
        System.out.println(Lambdas.METHOD.get(TestConverter::predicate));
        System.out.println(Lambdas.METHOD.get(TestConverter::supplier));
    }


    public static Boolean predicate(Integer val) {

        return true;
    }

    public static byte[] supplier() {

        return null;
    }

    public static void consumer(Integer val) {

    }


    public static void consumerInt(int val) {

    }

    public static void consumerException(Integer val) throws Exception {

    }

    public static void consumerIoException(Integer val) throws IOException {

    }

    public static void consumerMulException(Integer val) throws IOException, SQLException {

    }
}
