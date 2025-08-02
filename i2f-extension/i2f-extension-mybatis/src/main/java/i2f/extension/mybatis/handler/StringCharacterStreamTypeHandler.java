package i2f.extension.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2022/3/9 19:56
 * @desc 用于在CharacterStream与String之间自动转换
 * 插入时：
 * #{val,javaType=java.lang.String,jdbcType=LONGVARCHAR,typeHandler=com.i2f...StringCharacterStreamTypeHandler}
 * 查询时：
 * <result javaType="java.lang.String" jdbcType="LONGVARCHAR" typeHandler="com.i2f...StringCharacterStreamTypeHandler"/>
 * mybatis-plus的注解：
 * @TableField(jdbcType = JdbcType.LONGVARCHAR, typeHandler = StringCharacterStreamTypeHandler.class)
 */
@MappedTypes({String.class})
@MappedJdbcTypes(value = {JdbcType.LONGVARCHAR, JdbcType.LONGVARCHAR})
public class StringCharacterStreamTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setCharacterStream(index, new StringReader(parameter), parameter.length());
    }


    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);

        return value;
    }


    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);

        return value;
    }


    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);

        return value;
    }
}
