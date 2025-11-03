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
public class DatasourceRunnerDto extends DatasourceSelectDto {
    protected String sql;

    protected Boolean autoCommit;
    protected String delimiter;
    protected Boolean sendFullScript;
    protected Boolean stopOnError;
}
