package i2f.springboot.ops.datasource.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/12/23 22:25
 * @desc
 */
@Data
@NoArgsConstructor
public class DatasourceItemDto {
    protected String driver;
    protected String url;
    protected String username;
    protected String password;
}
