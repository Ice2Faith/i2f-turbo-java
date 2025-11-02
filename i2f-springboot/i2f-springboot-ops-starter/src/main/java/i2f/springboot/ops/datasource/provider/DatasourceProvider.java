package i2f.springboot.ops.datasource.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/1 21:45
 * @desc
 */
public interface DatasourceProvider {
    Map<String, DataSource> getDatasourceMap();

    String getDefaultDataSourceName();

    default DataSource getDefaultDataSource() {
        return getDatasourceMap().get(getDefaultDataSourceName());
    }

    default DataSource getDatasource(String dataSourceName) {
        return getDatasourceMap().get(dataSourceName);
    }
}
