package i2f.springboot.ops.datasource.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/3 11:49
 */
@Data
@NoArgsConstructor
public class DatasourceSelectDto {
    protected String datasource;

    protected Boolean useCustomDatasource;
    protected String driver;
    protected String url;
    protected String username;
    protected String password;
}
