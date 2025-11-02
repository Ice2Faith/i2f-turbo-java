package i2f.springboot.ops.datasource.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:51
 * @desc
 */
@Data
@NoArgsConstructor
public class DatasourceOperateDto {
    protected String datasource;
    protected String sql;
    protected Integer maxCount;
}
