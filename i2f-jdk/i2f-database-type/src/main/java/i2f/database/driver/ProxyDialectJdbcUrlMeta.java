package i2f.database.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/5 21:02
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyDialectJdbcUrlMeta {
    protected String dialectType;
    protected String realJdbcUrl;
}
