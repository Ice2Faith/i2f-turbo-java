package i2f.springboot.ops.datasource.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/28 11:17
 */
public interface DatasourceCollector {
    Map<String, DataSource> collect();
}
