package i2f.bindsql.stringify.impl.dialect;

import i2f.bindsql.stringify.impl.BasicBindSqlStringifier;
import i2f.convert.obj.ObjectConvertor;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2025/3/20 15:08
 */
public class H2BindSqlStringifier extends BasicBindSqlStringifier {
    public static final H2BindSqlStringifier INSTANCE = new H2BindSqlStringifier();

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "PARSEDATETIME('" + DATE_TYPE_FORMATTER.format(time) + "', 'yyyy-MM-dd HH:mm:ss')";
    }
}
