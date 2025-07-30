package i2f.database.metadata.reverse.ddl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.reverse.ddl.DdlDatabaseReverseEngineer;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/30 9:41
 */
public class DefaultDdlDatabaseReverseEngineer implements DdlDatabaseReverseEngineer {
    public static final DefaultDdlDatabaseReverseEngineer INSTANCE = new DefaultDdlDatabaseReverseEngineer();

    @Override
    public String generate(TableMeta meta) {
        StringBuilder builder = new StringBuilder();
        generateBefore(meta, builder);

        generateDropTable(meta, builder);
        builder.append("\n");

        generateCreateTable(meta, builder);

        generateUniqueIndexes(meta, builder);

        generateIndexes(meta, builder);

        generateAfter(meta, builder);
        return builder.toString();
    }

    public void generateBefore(TableMeta meta, StringBuilder builder) {
        builder.append("-- ").append(meta.getDatabase()).append(".").append(meta.getName()).append(" ").append(meta.getComment()).append("\n");
    }

    public void generateAfter(TableMeta meta, StringBuilder builder) {

    }

    public void generateCreateTable(TableMeta meta, StringBuilder builder) {
        builder.append("create table ").append(meta.getName()).append("\n")
                .append("(").append("\n");
        List<ColumnMeta> columns = meta.getColumns();
        if (columns != null) {
            boolean isFirst = true;
            for (ColumnMeta column : columns) {
                if (column == null) {
                    continue;
                }
                builder.append("\t");
                if (!isFirst) {
                    builder.append(",");
                }

                builder.append(column.getName()).append(" ").append(column.getColumnType());
                if (column.isAutoIncrement()) {
                    builder.append(" ").append("auto_increment");
                }
                if (!isEmpty(column.getDefaultValue())) {
                    builder.append(" ").append("default ").append(decorateSqlString(column.getDefaultValue()));
                }
                if (!column.isNullable()) {
                    builder.append(" ").append("not null");
                }
                if (!isEmpty(column.getComment())) {
                    builder.append(" ").append("comment ").append(decorateSqlString(column.getComment()));
                }
                builder.append("\n");
                isFirst = false;
            }
        }
        IndexMeta primary = meta.getPrimary();
        if (primary != null) {
            builder.append("\n");
            builder.append("\t").append(",primary key (");
            generateIndexColumns(primary, builder);
            builder.append(")").append("\n");
        }
        builder.append(")");
        if (!isEmpty(meta.getComment())) {
            builder.append(" comment ").append(decorateSqlString(meta.getComment()));
        }
        builder.append(";").append("\n");
    }

    public void generateDropTable(TableMeta meta, StringBuilder builder) {
        builder.append("drop table if exists ").append(meta.getName()).append(";").append("\n");
    }

    public void generateIndexes(TableMeta meta, StringBuilder builder) {
        List<IndexMeta> indexes = meta.getIndexes();
        if (indexes != null) {
            builder.append("\n");
            for (IndexMeta index : indexes) {
                builder.append("create index ").append(index.getName()).append("\n")
                        .append("\t").append("on ").append(meta.getName()).append("(");
                generateIndexColumns(index, builder);
                builder.append(");").append("\n");
            }
        }
    }

    public void generateUniqueIndexes(TableMeta meta, StringBuilder builder) {
        List<IndexMeta> uniqueIndexes = meta.getUniqueIndexes();
        if (uniqueIndexes != null) {
            builder.append("\n");
            for (IndexMeta index : uniqueIndexes) {
                builder.append("create unique index ").append(index.getName()).append("\n")
                        .append("\t").append("on ").append(meta.getName()).append("(");
                generateIndexColumns(index, builder);
                builder.append(");").append("\n");
            }
        }
    }

    public void generateIndexColumns(IndexMeta primary, StringBuilder builder) {
        List<IndexColumnMeta> indexColumns = primary.getColumns();
        boolean isFirst = true;
        for (IndexColumnMeta item : indexColumns) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(item.getName());
            if (item.isDesc()) {
                builder.append(" desc");
            }
            isFirst = false;
        }
    }

    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public String decorateSqlString(String str) {
        if (str == null) {
            return "null";
        }
        return "'" + str.replace("'", "''") + "'";
    }

}
