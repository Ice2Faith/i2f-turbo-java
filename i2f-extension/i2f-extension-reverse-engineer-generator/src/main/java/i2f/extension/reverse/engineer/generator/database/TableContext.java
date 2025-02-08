package i2f.extension.reverse.engineer.generator.database;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author ltb
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
public class TableContext {
    private String database;
    private String name;
    private String comment;
    private PrimaryContext primaryKey;

    private List<ColumnContext> columns;

    private TableMeta meta;

    public static TableContext parse(TableMeta meta) {
        TableContext ret = new TableContext();
        ret.database = meta.getDatabase();
        ret.name = meta.getName();
        ret.comment = meta.getComment();

        ret.primaryKey = new PrimaryContext();
        IndexMeta primary = meta.getPrimary();
        if (primary != null) {
            List<IndexColumnMeta> cols = primary.getColumns();
            if (!cols.isEmpty()) {
                IndexColumnMeta idxCol = cols.get(0);
                ret.primaryKey.setName(idxCol.getName());
            }
        }
        for (ColumnMeta column : meta.getColumns()) {
            if (Objects.equals(column.getName(), ret.primaryKey.getName())) {
                ret.primaryKey.setJavaType(column.getJavaType());
            }
        }

        ret.columns = ColumnContext.parse(meta.getColumns());

        ret.meta = meta;
        return ret;
    }
}
