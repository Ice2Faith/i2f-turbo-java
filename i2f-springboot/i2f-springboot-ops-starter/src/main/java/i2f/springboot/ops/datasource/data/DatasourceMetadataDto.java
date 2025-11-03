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
public class DatasourceMetadataDto extends DatasourceSelectDto {
    protected String database;
    protected String table;
    protected String ddlType;
}
