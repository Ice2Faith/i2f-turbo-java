package i2f.extension.canal.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/11/23 15:44
 */
@Data
@NoArgsConstructor
public class CanalMeta {
    protected String host = "127.0.0.1";
    protected int port = 11111;
    protected String destination = "example";
    protected String username;
    protected String password;

    public CanalMeta(String host, int port, String destination) {
        this.host = host;
        this.port = port;
        this.destination = destination;
    }

    public CanalMeta(String host, int port, String destination, String username, String password) {
        this.host = host;
        this.port = port;
        this.destination = destination;
        this.username = username;
        this.password = password;
    }
}
