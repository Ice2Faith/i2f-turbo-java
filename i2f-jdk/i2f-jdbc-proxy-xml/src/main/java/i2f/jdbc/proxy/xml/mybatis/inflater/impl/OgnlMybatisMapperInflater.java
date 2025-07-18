package i2f.jdbc.proxy.xml.mybatis.inflater.impl;

import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/10/9 18:45
 */
public class OgnlMybatisMapperInflater extends MybatisMapperInflater {

    public static final OgnlMybatisMapperInflater INSTANCE = new OgnlMybatisMapperInflater();

    @Override
    public boolean testExpression(String expression, Object params) {
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
    public Object evalExpression(String expression, Object params) {
        try {
            return OgnlUtil.evaluateExpression(expression, params);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object runScript(String script, String lang, Map<String, Object> params, MybatisMapperNode node) {
        if (lang != null) {
            lang = lang.trim().toLowerCase();
        }
        if (lang == null || lang.isEmpty() || "ognl".equalsIgnoreCase(lang)) {
            try {
                Object obj = OgnlUtil.evaluateExpression(script, params);
                return obj;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException("un-support script lang=" + lang + " !");
    }
}
