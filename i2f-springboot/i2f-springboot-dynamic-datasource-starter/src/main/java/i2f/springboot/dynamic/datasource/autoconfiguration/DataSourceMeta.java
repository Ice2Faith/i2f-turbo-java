package i2f.springboot.dynamic.datasource.autoconfiguration;

import lombok.Data;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/22 20:46
 * @desc
 */
@Data
public class DataSourceMeta {
    private String driver;
    private String url;
    private String username;
    private String password;

    private String type;
    private String group;

    public static DataSourceMeta of(Map<String, ?> map) {
        DataSourceMeta ret = new DataSourceMeta();
        Object driver = map.get("driver");
        if (driver == null) {
            driver = map.get("driver-class-name");
        }
        if (driver != null) {
            ret.setDriver(String.valueOf(driver));
        }
        Object url = map.get("url");
        if (url == null) {
            url = map.get("jdbc-url");
        }
        if (url != null) {
            ret.setUrl(String.valueOf(url));
        }
        Object username = map.get("username");
        if (username != null) {
            ret.setUsername(String.valueOf(username));
        }
        Object password = map.get("password");
        if (password != null) {
            ret.setPassword(String.valueOf(password));
        }
        Object type = map.get("type");
        if (type != null) {
            ret.setType(String.valueOf(type));
        }
        Object group = map.get("group");
        if (group != null) {
            ret.setGroup(String.valueOf(group));
        }
        return ret;
    }
}
