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
        meta.fillColumnIndexMeta();

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
        builder.append("-- ").append(tableName(meta.getDatabase())).append(".").append(tableName(meta.getName())).append(" ").append(meta.getComment()).append("\n");
    }

    public void generateAfter(TableMeta meta, StringBuilder builder) {

    }

    public void generateCreateTable(TableMeta meta, StringBuilder builder) {
        builder.append(keyword("create table ")).append(tableName(meta.getName())).append("\n")
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


                builder.append(columnName(column.getName())).append(" ").append(keyword(convertColumnType(column)));

                if (column.isAutoIncrement()) {
                    builder.append(" ").append(keyword("auto_increment"));
                }
                if (!isEmpty(column.getDefaultValue())) {
                    builder.append(" ").append(keyword("default ")).append(decorateSqlString(column.getDefaultValue()));
                }
                if (!column.isNullable()) {
                    builder.append(" ").append(keyword("not null"));
                }
                if (!isEmpty(column.getComment())) {
                    builder.append(" ").append(keyword("comment ")).append(decorateSqlString(column.getComment()));
                }
                builder.append("\n");
                isFirst = false;
            }
        }
        IndexMeta primary = meta.getPrimary();
        if (primary != null) {
            builder.append("\n");
            builder.append("\t").append(keyword(",primary key ("));
            generateIndexColumns(primary, builder);
            builder.append(")").append("\n");
        }
        builder.append(")");
        if (!isEmpty(meta.getComment())) {
            builder.append(keyword(" comment ")).append(decorateSqlString(meta.getComment()));
        }
        builder.append(";").append("\n");
    }

    public void generateDropTable(TableMeta meta, StringBuilder builder) {
        builder.append(keyword("drop table if exists ")).append(tableName(meta.getName())).append(";").append("\n");
    }

    public void generateIndexes(TableMeta meta, StringBuilder builder) {
        List<IndexMeta> indexes = meta.getIndexes();
        if (indexes != null) {
            builder.append("\n");
            for (IndexMeta index : indexes) {
                builder.append(keyword("create index ")).append(tableName(index.getName())).append("\n")
                        .append("\t").append(keyword("on ")).append(tableName(meta.getName())).append("(");
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
                builder.append(keyword("create unique index ")).append(tableName(index.getName())).append("\n")
                        .append("\t").append(keyword("on ")).append(tableName(meta.getName())).append("(");
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
            builder.append(columnName(item.getName()));
            if (item.isDesc()) {
                builder.append(keyword(" desc"));
            }
            isFirst = false;
        }
    }

    public String convertColumnType(ColumnMeta column) {
        return column.getColumnType();
    }

    public String keyword(String str) {
        if (str == null) {
            return str;
        }
        return str;
    }

    public String tableName(String str) {
        if (str == null) {
            return str;
        }
        return str;
    }

    public String columnName(String str) {
        if (str == null) {
            return str;
        }
        return str;
    }

    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public String decorateSqlString(String str) {
        if (str == null) {
            return keyword("null");
        }
        return "'" + str.replace("'", "''") + "'";
    }

}
