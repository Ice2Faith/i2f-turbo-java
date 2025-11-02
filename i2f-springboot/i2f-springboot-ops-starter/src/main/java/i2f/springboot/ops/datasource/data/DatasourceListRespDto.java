package i2f.springboot.ops.datasource.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:46
 * @desc
 */
@Data
@NoArgsConstructor
public class DatasourceListRespDto {
    protected String defaultName;
    protected List<String> list;
}
