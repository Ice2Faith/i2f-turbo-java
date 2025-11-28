package i2f.springboot.ops.elasticsearch.data;

import i2f.extension.elasticsearch.EsMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/27 11:16
 */
@Data
@NoArgsConstructor
public class ElasticSearchOperateDto {
    protected boolean useCustomMeta = false;
    protected EsMeta meta;

    protected String pattern;

    protected String indexName;
    protected String id;
    protected String jsonDsl;

    protected Integer maxCount;

    protected String mappingJson;
    protected String settingJson;
}
