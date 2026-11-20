package i2f.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Ice2Faith
 * @date 2026/11/20 15:44
 * @desc
 */
@Data
@NoArgsConstructor
public class ResultSetContext {
    protected ResultSet resultSet;
    protected Statement statement;

    public static ResultSetContext of(ResultSet rs, Statement stat) {
        ResultSetContext ret = new ResultSetContext();
        ret.setResultSet(rs);
        ret.setStatement(stat);
        return ret;
    }
}
