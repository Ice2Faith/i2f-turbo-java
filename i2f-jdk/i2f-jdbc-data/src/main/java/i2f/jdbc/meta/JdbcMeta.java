package i2f.jdbc.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2025/1/23 15:31
 */
@Data
@NoArgsConstructor
public class JdbcMeta {
    protected String driver;
    protected String url;
    protected String username;
    protected String password;
    protected Properties properties;

    public JdbcMeta(String driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public JdbcMeta(String driver, String url, String username) {
        this.driver = driver;
        this.url = url;
        this.username = username;
    }

    public JdbcMeta(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public JdbcMeta(String driver, String url, String username, String password, Properties properties) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.properties = properties;
    }
}
