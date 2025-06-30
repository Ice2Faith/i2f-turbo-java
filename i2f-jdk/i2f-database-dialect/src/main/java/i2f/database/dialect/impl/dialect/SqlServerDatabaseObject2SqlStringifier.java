package i2f.database.dialect.impl.dialect;


import i2f.convert.obj.ObjectConvertor;
import i2f.database.dialect.impl.AbsDatabaseObject2SqlStringifier;
import i2f.database.type.DatabaseType;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class SqlServerDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final SqlServerDatabaseObject2SqlStringifier INSTANCE = new SqlServerDatabaseObject2SqlStringifier();

    @Override
    public boolean support(DatabaseType databaseType) {
        return Arrays.asList(
                DatabaseType.SQL_SERVER,
                DatabaseType.SQL_SERVER2005
        ).contains(databaseType);
    }

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "CONVERT(datetime, '" + DATE_TYPE_FORMATTER.format(time) + "', 120)";
    }
}
