package i2f.springboot.dynamic.datasource.wrapper;

import i2f.springboot.dynamic.datasource.core.DataSourcePostWrapper;
import io.seata.rm.datasource.DataSourceProxy;

import javax.sql.DataSource;

/**
 * @author Ice2Faith
 * @date 2026/10/12 10:17
 * @desc
 */
public class SeataDataSourcePostWrapper implements DataSourcePostWrapper {
    @Override
    public DataSource wrap(String datasourceId, DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }
}
