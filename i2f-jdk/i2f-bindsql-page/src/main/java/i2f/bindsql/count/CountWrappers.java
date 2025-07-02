package i2f.bindsql.count;

import i2f.bindsql.count.impl.SqlCountWrapper;
import i2f.database.type.DatabaseType;

import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/4/28 15:22
 * @desc
 */
public class CountWrappers {

    public static ICountWrapper wrapper(DatabaseType type) {
        ServiceLoader<ICountWrapperProvider> list = ServiceLoader.load(ICountWrapperProvider.class);
        for (ICountWrapperProvider item : list) {
            if (item == null) {
                continue;
            }
            if (item.support(type)) {
                ICountWrapper countWrapper = item.getCountWrapper();
                if (countWrapper != null) {
                    return countWrapper;
                }
            }
        }
        return SqlCountWrapper.INSTANCE;
    }

}
