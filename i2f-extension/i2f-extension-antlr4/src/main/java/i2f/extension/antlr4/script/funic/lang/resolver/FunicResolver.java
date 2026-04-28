package i2f.extension.antlr4.script.funic.lang.resolver;

import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.invokable.method.IMethod;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/22 9:06
 * @desc
 */
public interface FunicResolver {
    Object multilineString(String text, List<String> features, DefaultFunicVisitor visitor);

    String renderString(String text, DefaultFunicVisitor visitor);

    Object getFieldValue(Object target, String fieldName, DefaultFunicVisitor visitor);

    void setFieldValue(Object target, String fieldName, Object value, DefaultFunicVisitor visitor);

    Object getSquareFieldValue(Object target, Object squareKey, DefaultFunicVisitor visitor);

    void setSquareFieldValue(Object target, Object squareKey, Object value, DefaultFunicVisitor visitor);

    Object getStaticFieldOrEnum(Class<?> type, String fieldName, DefaultFunicVisitor visitor);

    Object setStaticField(Class<?> type, String fieldName, Object value, DefaultFunicVisitor visitor);

    Object invokeInstanceMethod(Object target, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor);

    Object invokeGlobalMethod(String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor);

    Object invokeStaticMethod(Class<?> type, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor);

    Object newInstance(Class<?> clazz, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor);

    Object newArray(Class<?> elementType, int count, DefaultFunicVisitor visitor);

    Object prefixOperator(Object value, String operator, DefaultFunicVisitor visitor);

    Object suffixOperator(Object target, String operator, DefaultFunicVisitor visitor);

    Object doubleOperator(Object leftValue, String operator, Object rightValue, DefaultFunicVisitor visitor);

    List<Object> unpackList(Object iterable, DefaultFunicVisitor visitor);

    Map<String, Object> unpackMap(Object object, DefaultFunicVisitor visitor);

    Class<?> findClass(String className, DefaultFunicVisitor visitor);

    int compare(Object left, Object right, boolean forceType);

    boolean toBoolean(Object value, DefaultFunicVisitor visitor);

    Iterator<?> wrapAsIterator(Object value, DefaultFunicVisitor visitor);

    Object convertType(Object value, Class<?> type, DefaultFunicVisitor visitor);

    boolean onPreRegistryContextImportPackage(String packageName, DefaultFunicVisitor visitor);

    boolean onPreRegisterContextGlobalMethod(IMethod method, DefaultFunicVisitor visitor);
}
