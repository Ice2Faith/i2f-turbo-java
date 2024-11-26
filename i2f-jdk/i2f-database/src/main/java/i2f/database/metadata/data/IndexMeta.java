package i2f.database.metadata.data;

import lombok.Data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:44
 * @desc
 */
@Data
public class IndexMeta {
    protected boolean isUnique;
    protected String name;
    protected List<IndexColumnMeta> columns;

}
