package i2f.springboot.jdbc.bql.components;

import i2f.jdbc.std.context.JdbcInvokeContextProvider;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Ice2Faith
 * @date 2024/6/7 14:56
 * @desc
 */
public class SpringDatasourceJdbcInvokeContextProvider implements JdbcInvokeContextProvider<DataSource> {
    protected DataSource dataSource;

    public SpringDatasourceJdbcInvokeContextProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource beginContext() {
        return dataSource;
    }

    @Override
    public Connection getConnection(DataSource context) {
        return DataSourceUtils.getConnection(context);
    }

    @Override
    public void endContext(DataSource context, Connection conn) {
        DataSourceUtils.releaseConnection(conn, context);
    }
}
