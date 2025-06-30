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
public class MysqlDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final MysqlDatabaseObject2SqlStringifier INSTANCE = new MysqlDatabaseObject2SqlStringifier();

    @Override
    public boolean support(DatabaseType databaseType) {
        return Arrays.asList(
                DatabaseType.MYSQL,
                DatabaseType.MARIADB,
                DatabaseType.OSCAR,
                DatabaseType.GBASE
        ).contains(databaseType);
    }

    @Override
    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        /*language=sql*/
        return "STR_TO_DATE('" + DATE_TYPE_FORMATTER.format(time) + "', '%Y-%m-%d %H:%i:%s')";
    }
}
