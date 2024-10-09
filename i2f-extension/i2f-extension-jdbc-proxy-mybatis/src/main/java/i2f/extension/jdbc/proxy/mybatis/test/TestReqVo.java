package i2f.extension.jdbc.proxy.mybatis.test;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/10/8 16:45
 */
@Data
@NoArgsConstructor
public class TestReqVo {
    private String tableName;
    private String idColumn;
    private String codeColumn;
    private String nameColumn;
}
