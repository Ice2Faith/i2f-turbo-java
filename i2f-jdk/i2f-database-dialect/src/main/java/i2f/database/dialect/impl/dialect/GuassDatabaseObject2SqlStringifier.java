package i2f.database.dialect.impl.dialect;


import i2f.convert.obj.ObjectConvertor;
import i2f.database.dialect.impl.AbsDatabaseObject2SqlStringifier;
import i2f.database.type.DatabaseType;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class GuassDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final GuassDatabaseObject2SqlStringifier INSTANCE = new GuassDatabaseObject2SqlStringifier();

    @Override
    public boolean support(DatabaseType databaseType) {
        return DatabaseType.GAUSS == databaseType;
    }

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "'" + DATE_TYPE_FORMATTER.format(time) + "'::timestamp";
    }
}
