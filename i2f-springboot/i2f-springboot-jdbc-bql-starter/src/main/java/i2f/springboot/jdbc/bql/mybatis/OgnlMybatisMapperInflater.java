package i2f.springboot.jdbc.bql.mybatis;

import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/10/9 18:45
 */
public class OgnlMybatisMapperInflater extends MybatisMapperInflater {

    public static final OgnlMybatisMapperInflater INSTANCE = new OgnlMybatisMapperInflater();

    @Override
    public boolean testExpression(String expression, Map<String, Object> params) {
        try {
            Object ret = OgnlUtil.evaluateExpression(expression, params);
            if (ret instanceof Boolean) {
                return (Boolean) ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object evalExpression(String expression, Map<String, Object> params) {
        try {
            return OgnlUtil.evaluateExpression(expression, params);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}