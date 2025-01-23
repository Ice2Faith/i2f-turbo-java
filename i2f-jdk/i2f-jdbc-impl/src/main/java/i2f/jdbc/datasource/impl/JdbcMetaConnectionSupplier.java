package i2f.jdbc.datasource.impl;

import i2f.jdbc.JdbcResolver;
import i2f.jdbc.datasource.ConnectionSupplier;
import i2f.jdbc.meta.JdbcMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2025/1/23 15:37
 */
@Data
@NoArgsConstructor
public class JdbcMetaConnectionSupplier implements ConnectionSupplier {
    protected JdbcMeta meta;

    public JdbcMetaConnectionSupplier(JdbcMeta meta) {
        this.meta = meta;
    }

    @Override
    public Connection get() throws SQLException {
        return JdbcResolver.getConnection(this.meta);
    }
}
