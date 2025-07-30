package i2f.database.metadata.reverse.ddl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.std.StdType;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/30 10:49
 */
public class StdDdlDatabaseReverseEngineer extends DefaultDdlDatabaseReverseEngineer {
    public static final StdDdlDatabaseReverseEngineer INSTANCE = new StdDdlDatabaseReverseEngineer();

    @Override
    public String generate(TableMeta meta) {
        StringBuilder builder = new StringBuilder();
        builder.append("-- ").append(meta.getDatabase()).append(".").append(meta.getName()).append(" ").append(meta.getComment()).append("\n");

        builder.append("drop table if exists ").append(meta.getName()).append(";").append("\n");
        builder.append("\n");

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

                builder.append(column.getName()).append(" ");
                StdType stdType = column.getRawStdType();
                builder.append(stdType.text());
                if (stdType.precision() && stdType.scale()) {
                    builder.append("(").append(column.getPrecision()).append(", ").append(column.getScale()).append(")");
                } else if (stdType.precision()) {
                    builder.append("(").append(column.getPrecision()).append(")");
                }

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


        return builder.toString();
    }
}
