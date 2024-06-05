package i2f.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLType;

/**
 * @author Ice2Faith
 * @date 2024/6/5 20:07
 * @desc
 */
@Data
@NoArgsConstructor
public class NamingOutputParameter {
    protected String name;

    protected int sqlType;
    protected SQLType type;

    protected boolean input;
    protected Object value;



    public NamingOutputParameter(String name, int sqlType) {
        this.name = name;
        this.sqlType = sqlType;
    }

    public NamingOutputParameter(String name, SQLType type) {
        this.name = name;
        this.type = type;
    }

    public NamingOutputParameter(String name, int sqlType, Object value) {
        this.name = name;
        this.sqlType = sqlType;
        this.input=true;
        this.value = value;
    }

    public NamingOutputParameter(String name, SQLType type, Object value) {
        this.name = name;
        this.type = type;
        this.input=true;
        this.value = value;
    }

    public static NamingOutputParameter of(String name, int sqlType) {
        return new NamingOutputParameter(name,sqlType);
    }

    public static NamingOutputParameter of(String name, SQLType type) {
        return new NamingOutputParameter(name,type);
    }

    public static NamingOutputParameter of(String name, int sqlType, Object value) {
        return new NamingOutputParameter(name,sqlType,value);
    }

    public static NamingOutputParameter of(String name, SQLType type, Object value) {
        return new NamingOutputParameter(name, type,value);
    }
}
