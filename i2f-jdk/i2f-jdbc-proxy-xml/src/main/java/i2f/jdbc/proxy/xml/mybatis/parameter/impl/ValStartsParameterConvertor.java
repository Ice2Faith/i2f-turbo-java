package i2f.jdbc.proxy.xml.mybatis.parameter.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.xml.mybatis.parameter.ParameterConvertor;

/**
 * @author Ice2Faith
 * @date 2026/2/3 21:23
 * @desc
 */
public class ValStartsParameterConvertor implements ParameterConvertor {
    public static final ValStartsParameterConvertor INSTANCE=new ValStartsParameterConvertor();
    public static final String NAME="v-starts";
    @Override
    public Object convert(Object obj, String expr, boolean isDollar) {
        if(!(obj instanceof CharSequence)){
            return obj;
        }
        String str=String.valueOf(obj);
        str=str+"%";
        if(isDollar){
            str=str.replace("'","''");
            return "'"+str+"'";
        }
        return BindSql.of("?",str);
    }
}
