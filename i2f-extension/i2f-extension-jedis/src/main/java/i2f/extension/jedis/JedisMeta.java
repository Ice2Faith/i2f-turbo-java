package i2f.extension.jedis;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/8/28 14:21
 */
@Data
@NoArgsConstructor
public class JedisMeta {
    protected String host = "127.0.0.1";
    protected int port = 6379;
    protected String password;
    protected int database = 0;

    protected int timeout = 3000;
    protected boolean ssl = false;

    public JedisMeta(String host, int database) {
        this.host = host;
        this.database = database;
    }

    public JedisMeta(String host, String password, int database) {
        this.host = host;
        this.password = password;
        this.database = database;
    }

    public JedisMeta(String host, int port, String password, int database) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
    }
}
