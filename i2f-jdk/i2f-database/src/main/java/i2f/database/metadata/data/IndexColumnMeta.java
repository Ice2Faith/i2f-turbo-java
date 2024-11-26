package i2f.database.metadata.data;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:45
 * @desc
 */
@Data
public class IndexColumnMeta {
    protected int index;
    protected String name;
    protected boolean isDesc;
    protected String type;

}
