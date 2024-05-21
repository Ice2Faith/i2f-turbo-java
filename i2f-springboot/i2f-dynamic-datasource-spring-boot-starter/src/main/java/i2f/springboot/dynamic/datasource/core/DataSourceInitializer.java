package i2f.springboot.dynamic.datasource.core;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/20 16:57
 * @desc
 */
public interface DataSourceInitializer {
    DataSource initial(String dataSourceId, Map<String, Object> dataSourceConfig);
}
