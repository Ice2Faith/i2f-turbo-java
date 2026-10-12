package i2f.springboot.dynamic.datasource.core;

import javax.sql.DataSource;

/**
 * @author Ice2Faith
 * @date 2026/10/12 10:14
 * @desc
 */
public interface DataSourcePostWrapper {
    DataSource wrap(String datasourceId, DataSource dataSource);
}
