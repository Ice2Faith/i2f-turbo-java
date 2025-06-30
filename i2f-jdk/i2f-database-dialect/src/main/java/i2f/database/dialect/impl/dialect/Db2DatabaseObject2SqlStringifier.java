package i2f.database.dialect.impl.dialect;


import i2f.convert.obj.ObjectConvertor;
import i2f.database.dialect.impl.AbsDatabaseObject2SqlStringifier;
import i2f.database.type.DatabaseType;

import java.time.LocalDateTime;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class Db2DatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final Db2DatabaseObject2SqlStringifier INSTANCE = new Db2DatabaseObject2SqlStringifier();

    @Override
    public boolean support(DatabaseType databaseType) {
        return DatabaseType.DB2 == databaseType;
    }

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "TIMESTAMP('" + DATE_TYPE_FORMATTER.format(time) + "')";
    }
}
