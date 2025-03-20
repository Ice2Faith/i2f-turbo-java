package i2f.bindsql.stringify.impl.dialect;

import i2f.bindsql.stringify.impl.BasicBindSqlStringifier;
import i2f.convert.obj.ObjectConvertor;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2025/3/20 15:08
 */
public class PostgreSqlBindSqlStringifier extends BasicBindSqlStringifier {
    public static final PostgreSqlBindSqlStringifier INSTANCE = new PostgreSqlBindSqlStringifier();

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "TO_TIMESTAMP('" + DATE_TYPE_FORMATTER.format(time) + "', 'YYYY-MM-DD HH24:MI:SS')";
    }
}
