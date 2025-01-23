package i2f.jdbc.datasource.impl;

import i2f.jdbc.datasource.ConnectionSupplier;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/1/23 15:41
 */
@Data
@NoArgsConstructor
public class DatasourceConnectionSupplier implements ConnectionSupplier {
    protected DataSource dataSource;

    public DatasourceConnectionSupplier(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() throws SQLException {
        return this.dataSource.getConnection();
    }
}
