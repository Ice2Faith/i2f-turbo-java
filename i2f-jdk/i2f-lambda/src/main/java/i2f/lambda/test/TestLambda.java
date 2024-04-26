package i2f.lambda.test;

import i2f.lambda.inflater.LambdaInflater;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:12
 * @desc
 */
public class TestLambda {

    public static void main(String[] args) throws Exception {

        // 无异常的接口，不接收有异常的实现
//        testConsumer(TestFunctional::consumerException); // error
//        testConsumer(TestFunctional::consumerIoException); // error

        // 包装类型可兼容基本类型
        testIntConsumer(TestLambda::consumer);
        testIntConsumer(TestLambda::consumerInt);
        testIntConsumer(TestLambda::consumerException);

        // 带异常的接口，可以接受无异常的和子异常的
        testExceptionConsumer(TestLambda::consumer);
        testExceptionConsumer(TestLambda::consumerException);
        testExceptionConsumer(TestLambda::consumerIoException);
        testExceptionConsumer(TestLambda::consumerMulException);
        testExceptionConsumer(TestLambda::consumerInt);

        testIoExceptionConsumer(TestLambda::consumer);
//        testIoExceptionConsumer(TestFunctional::consumerException); // error
        testIoExceptionConsumer(TestLambda::consumerIoException);
//        testIoExceptionConsumer(TestFunctional::consumerMulException); // error
        testIoExceptionConsumer(TestLambda::consumerInt);


        // array类型，实际上是object，可以进行泛型
        testByteArraySupplier(TestLambda::supplier);

        // 总结
        // 方法引用的检查点：返回值，参数类型，异常类型
        // 返回值类型，参数类型需要兼容，包含基本类型与包装类型的兼容，个数也要相等
        // 异常类型只需要兼容，个数可以少，但是需要时公共异常父类
        // array类型，实际上是object，可以进行泛型
    }

    // 如果要使用lambda
    @FunctionalInterface
    public interface IntConsumer extends Serializable {
        void accept(int val) throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionConsumer<T> extends Serializable {
        void accept(T val) throws Exception;
    }

    @FunctionalInterface
    public interface IoExceptionConsumer<T> extends Serializable {
        void accept(T val) throws IOException;
    }

    @FunctionalInterface
    public interface ByteArraySupplier extends Serializable {
        byte[] accept() throws IOException;
    }

    public static void testByteArraySupplier(ByteArraySupplier supplier) throws Exception {
        System.out.println("-------------------");
        SerializedLambda lambda = LambdaInflater.getSerializedLambda(supplier);
        System.out.println(lambda.getImplClass());
        System.out.println(lambda.getImplMethodName());
        System.out.println(lambda.getImplMethodSignature());
    }

    public static <T> void testExceptionConsumer(ExceptionConsumer<T> consumer) throws Exception {
        System.out.println("-------------------");
        SerializedLambda lambda = LambdaInflater.getSerializedLambda(consumer);
        System.out.println(lambda.getImplClass());
        System.out.println(lambda.getImplMethodName());
        System.out.println(lambda.getImplMethodSignature());
    }

    public static <T> void testIoExceptionConsumer(IoExceptionConsumer<T> consumer) throws Exception {
        System.out.println("-------------------");
        SerializedLambda lambda = LambdaInflater.getSerializedLambda(consumer);
        System.out.println(lambda.getImplClass());
        System.out.println(lambda.getImplMethodName());
        System.out.println(lambda.getImplMethodSignature());
    }

    public static <T> void testIntConsumer(IntConsumer consumer) throws Exception {
        System.out.println("-------------------");
        SerializedLambda lambda = LambdaInflater.getSerializedLambda(consumer);
        System.out.println(lambda.getImplClass());
        System.out.println(lambda.getImplMethodName());
        System.out.println(lambda.getImplMethodSignature());
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
