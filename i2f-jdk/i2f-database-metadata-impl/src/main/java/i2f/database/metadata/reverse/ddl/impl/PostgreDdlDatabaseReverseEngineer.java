package i2f.database.metadata.reverse.ddl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.postgresql.PostgreSqlType;
import i2f.database.metadata.std.StdType;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/30 11:10
 */
public class PostgreDdlDatabaseReverseEngineer extends DefaultDdlDatabaseReverseEngineer {
    public static final PostgreDdlDatabaseReverseEngineer INSTANCE = new PostgreDdlDatabaseReverseEngineer();

    public static final PostgreDdlDatabaseReverseEngineer CONVERT = new PostgreDdlDatabaseReverseEngineer() {
        @Override
        public String convertColumnType(ColumnMeta column) {
            if (column.isAutoIncrement()) {
                StdType stdType = column.getRawStdType();
                if (stdType == StdType.INT || stdType == StdType.INTEGER) {
                    return PostgreSqlType.SERIAL.text();
                } else if (stdType == StdType.SMALLINT || stdType == StdType.TINYINT) {
                    return PostgreSqlType.SMALLSERIAL.text();
                } else {
                    return PostgreSqlType.BIGSERIAL.text();
                }
            } else {
                StdType stdType = column.getRawStdType();
                PostgreSqlType type = PostgreSqlType.ofStd(stdType);
                if (type.precision() && type.scale()) {
                    return type.text() + "(" + column.getPrecision() + ", " + column.getScale() + ")";
                } else if (type.precision()) {
                    return type.text() + "(" + column.getPrecision() + ")";
                }
                return type.text();
            }
        }

        @Override
        public String keyword(String str) {
            return str == null ? null : str.toLowerCase();
        }

        @Override
        public String tableName(String str) {
            return str == null ? null : str.toLowerCase();
        }

        @Override
        public String columnName(String str) {
            return str == null ? null : str.toLowerCase();
        }
    };

    @Override
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

                if (!isEmpty(column.getDefaultValue())) {
                    builder.append(" ").append(keyword("default ")).append(decorateSqlString(column.getDefaultValue()));
                }
                if (!column.isNullable()) {
                    builder.append(" ").append(keyword("not null"));
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
        builder.append(";").append("\n");
    }

    @Override
    public void generateAfter(TableMeta meta, StringBuilder builder) {
        List<ColumnMeta> columns = meta.getColumns();

        if (!isEmpty(meta.getComment())) {
            builder.append("\n");
            builder.append(keyword("comment on table ")).append(tableName(meta.getName()))
                    .append(keyword(" is ")).append(decorateSqlString(meta.getComment())).append(";").append("\n");
        }

        if (columns != null) {
            builder.append("\n");
            for (ColumnMeta item : columns) {
                if (item == null) {
                    continue;
                }
                if (!isEmpty(item.getComment())) {
                    builder.append(keyword("comment on column ")).append(tableName(meta.getName())).append(".").append(columnName(item.getName()))
                            .append(keyword(" is ")).append(decorateSqlString(item.getComment())).append(";").append("\n");
                }
            }
        }
    }

    @Override
    public String convertColumnType(ColumnMeta column) {
        if (column.isAutoIncrement()) {
            StdType stdType = column.getRawStdType();
            if (stdType == StdType.INT || stdType == StdType.INTEGER) {
                return PostgreSqlType.SERIAL.text();
            } else if (stdType == StdType.SMALLINT || stdType == StdType.TINYINT) {
                return PostgreSqlType.SMALLSERIAL.text();
            } else {
                return PostgreSqlType.BIGSERIAL.text();
            }
        } else {
            return column.getColumnType();
        }
    }

}
