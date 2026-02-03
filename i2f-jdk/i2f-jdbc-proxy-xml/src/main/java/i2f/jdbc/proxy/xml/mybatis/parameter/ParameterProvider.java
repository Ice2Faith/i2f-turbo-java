package i2f.jdbc.proxy.xml.mybatis.parameter;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/2/2 14:12
 * @desc 用于在XML的占位符中使用
 * #{name,provider=ParameterProvider}
 * ${name,provider=ParameterProvider}
 * 用以进行对name表达式进行取值，可以用于从其他载体中取出变量，例如环境变量等
 * 同时，也支持返回一个BindSql对象用于直接绑定变量的SQL片段
 */
@FunctionalInterface
public interface ParameterProvider {
    /**
     *
     * @param expression 表达式
     * @param params 根对象
     * @param isDollar 是否是在 ${} 占位符中使用
     * @return
     */
    Object apply(String expression, Map<String, Object> params, boolean isDollar);
}
