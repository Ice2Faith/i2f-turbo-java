package i2f.springboot.dynamic.datasource.core;

import i2f.springboot.dynamic.datasource.autoconfiguration.DataSourceMeta;

import javax.sql.DataSource;

/**
 * @author Ice2Faith
 * @date 2024/5/20 16:57
 * @desc
 */
public interface DataSourceInitializer {
    boolean accept(String dataSourceId, DataSourceMeta dataSourceMeta);

    DataSource initial(String dataSourceId, DataSourceMeta dataSourceMeta);
}
