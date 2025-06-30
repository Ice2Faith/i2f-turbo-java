package i2f.database.dialect.impl.dialect;

import i2f.database.dialect.impl.AbsDatabaseObject2SqlStringifier;
import i2f.database.type.DatabaseType;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public class DefaultDatabaseObject2SqlStringifier extends AbsDatabaseObject2SqlStringifier {
    public static final DefaultDatabaseObject2SqlStringifier INSTANCE = new DefaultDatabaseObject2SqlStringifier();

    @Override
    public boolean support(DatabaseType databaseType) {
        return true;
    }


}
