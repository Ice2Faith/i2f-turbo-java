package i2f.extension.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.StringReader;
import java.sql.*;

/**
 * @author Ice2Faith
 * @date 2022/3/9 19:56
 * @desc 用于在Clob与String之间自动转换
 * 插入时：
 * #{val,javaType=java.lang.String,jdbcType=CLOB,typeHandler=com.i2f...StringClobTypeHandler}
 * 查询时：
 * <result javaType="java.lang.String" jdbcType="CLOB" typeHandler="com.i2f...StringClobTypeHandler"/>
 * mybatis-plus的注解：
 * @TableField(jdbcType = JdbcType.CLOB, typeHandler = StringClobTypeHandler.class)
 */
@MappedTypes({String.class})
@MappedJdbcTypes(value = {JdbcType.CLOB, JdbcType.NCLOB})
public class StringClobTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setCharacterStream(index, new StringReader(parameter), parameter.length());
    }


    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = null;
        Clob clob = rs.getClob(columnName);
        if (clob != null) {
            value = clob.getSubString(1L, (int) clob.length());
        }

        return value;
    }


    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = null;
        Clob clob = cs.getClob(columnIndex);
        if (clob != null) {
            value = clob.getSubString(1L, (int) clob.length());
        }

        return value;
    }


    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = null;
        Clob clob = rs.getClob(columnIndex);
        if (clob != null) {
            value = clob.getSubString(1L, (int) clob.length());
        }

        return value;
    }
}
