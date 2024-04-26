package i2f.functional.lambda;


import i2f.functional.lambda.converter.FieldLambdaConverter;
import i2f.functional.lambda.converter.MethodLambdaConverter;
import i2f.functional.lambda.converter.SerializedLambdaConverter;

/**
 * @author Ice2Faith
 * @date 2024/3/29 17:35
 * @desc
 */
public class Lambdas {
    public static final FieldLambdaConverter FIELD = FieldLambdaConverter.INSTANCE;
    public static final MethodLambdaConverter METHOD = MethodLambdaConverter.INSTANCE;
    public static final SerializedLambdaConverter LAMBDA = SerializedLambdaConverter.INSTANCE;
}
