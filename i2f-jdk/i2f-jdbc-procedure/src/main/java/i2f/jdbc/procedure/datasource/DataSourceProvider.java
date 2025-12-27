package i2f.jdbc.procedure.datasource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/30 14:36
 */
@FunctionalInterface
public interface DataSourceProvider {
    Map<String, DataSource> getDataSources();

    default int getOrder() {
        return 0;
    }
}
