package i2f.database.metadata.data;

import lombok.Data;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public TableMeta fillColumnIndexMeta() {
        for (ColumnMeta column : columns) {
            if (primary != null) {
                for (IndexColumnMeta primaryColumn : primary.getColumns()) {
                    if (Objects.equals(column.getName(), primaryColumn.getName())) {
                        column.setPrimaryKey(true);
                        column.setPrimaryKeyName(primary.getName());
                        column.setPrimaryKeyOrder(primaryColumn.getIndex());
                        break;
                    }
                }
            }
            if (uniqueIndexes != null) {
                for (IndexMeta uniqueIndex : uniqueIndexes) {
                    for (IndexColumnMeta indexColumn : uniqueIndex.getColumns()) {
                        if (Objects.equals(column.getName(), indexColumn.getName())) {
                            column.setUniqueKey(true);
                            if (column.uniqueKeyList == null) {
                                column.uniqueKeyList = new ArrayList<>();
                            }
                            column.uniqueKeyList.add(new AbstractMap.SimpleEntry<>(uniqueIndex.getName(), indexColumn.getIndex()));
                        }
                    }
                }
            }
            if (indexes != null) {
                for (IndexMeta uniqueIndex : indexes) {
                    for (IndexColumnMeta indexColumn : uniqueIndex.getColumns()) {
                        if (Objects.equals(column.getName(), indexColumn.getName())) {
                            column.setIndexKey(true);
                            if (column.indexKeyList == null) {
                                column.indexKeyList = new ArrayList<>();
                            }
                            column.indexKeyList.add(new AbstractMap.SimpleEntry<>(uniqueIndex.getName(), indexColumn.getIndex()));
                        }
                    }
                }
            }
        }
        if (columns != null) {
            for (ColumnMeta column : columns) {
                if (column == null) {
                    continue;
                }
                column.fillRawTypes();
            }
        }
        return this;
    }
}
