package i2f.bindsql.stringify.impl.dialect;

import i2f.bindsql.stringify.impl.BasicBindSqlStringifier;
import i2f.convert.obj.ObjectConvertor;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2025/3/20 15:08
 */
public class PhoenixBindSqlStringifier extends BasicBindSqlStringifier {
    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "to_date('" + DATE_TYPE_FORMATTER.format(time) + "')";
    }
}
