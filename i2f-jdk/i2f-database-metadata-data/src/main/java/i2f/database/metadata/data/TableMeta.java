package i2f.database.metadata.data;

import lombok.Data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:29
 * @desc
 */
@Data
public class TableMeta {
    protected String catalog;
    protected String schema;

    protected String database;

    protected String name;
    protected String comment;
    protected String type;

    protected List<ColumnMeta> columns;

    protected IndexMeta primary;

    protected List<IndexMeta> uniqueIndexes;

    protected List<IndexMeta> indexes;

}
