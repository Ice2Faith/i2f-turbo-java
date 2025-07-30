package i2f.database.metadata.reverse.ddl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.oracle.OracleType;
import i2f.database.metadata.std.StdType;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/30 11:10
 */
public class OracleDdlDatabaseReverseEngineer extends DefaultDdlDatabaseReverseEngineer {
    public static final OracleDdlDatabaseReverseEngineer INSTANCE = new OracleDdlDatabaseReverseEngineer();

    public static final OracleDdlDatabaseReverseEngineer CONVERT = new OracleDdlDatabaseReverseEngineer() {
        @Override
        public String convertColumnType(ColumnMeta column) {
            StdType stdType = column.getRawStdType();
            OracleType type = OracleType.ofStd(stdType);
            if (stdType == StdType.BIGINT || stdType == StdType.LONG) {
                return type.text() + "(32,0)";
            } else if (stdType == StdType.INT || stdType == StdType.INTEGER) {
                return type.text() + "(15,0)";
            } else if (stdType == StdType.SMALLINT || stdType == StdType.MEDIUMINT) {
                return type.text() + "(8,0)";
            } else if (stdType == StdType.TINYINT) {
                return type.text() + "(3,0)";
            }
            if (stdType == StdType.DECIMAL || stdType == StdType.DOUBLE
                    || stdType == StdType.REAL || stdType == StdType.NUMERIC) {
                return type.text() + "(32,8)";
            }
            if (type.precision() && type.scale()) {
                return type.text() + "(" + column.getPrecision() + ", " + column.getScale() + ")";
            } else if (type.precision()) {
                return type.text() + "(" + column.getPrecision() + ")";
            }
            return type.text();
        }

        @Override
        public String keyword(String str) {
            return str == null ? null : str.toUpperCase();
        }

        @Override
        public String tableName(String str) {
            return str == null ? null : str.toUpperCase();
        }

        @Override
        public String columnName(String str) {
            return str == null ? null : str.toUpperCase();
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
    public void generateDropTable(TableMeta meta, StringBuilder builder) {
        builder.append(keyword("begin;")).append("\n")
                .append("\t").append(keyword("drop table ")).append(tableName(meta.getName())).append(";").append("\n")
                .append(keyword("exception when others then ")).append("\n")
                .append("\t").append(keyword("null;")).append("\n")
                .append(keyword("end;")).append("\n");
    }

    @Override
    public void generateAfter(TableMeta meta, StringBuilder builder) {
        List<ColumnMeta> columns = meta.getColumns();
        if (columns != null) {
            for (ColumnMeta item : columns) {
                if (item == null) {
                    continue;
                }
                if (item.isAutoIncrement() || item.isPrimaryKey()) {
                    builder.append("\n");
                    builder.append(keyword("create sequence ")).append(tableName("SEQ_" + meta.getName() + "_" + item.getName())).append("\n")
                            .append(keyword("increment by 1")).append("\n")
                            .append(keyword("minvalue 1 maxvalue 9999999999999999999999999999")).append("\n")
                            .append(keyword("start with 100 nocycle")).append("\n")
                            .append(keyword("cache 20")).append("\n")
                            .append(keyword("noorder")).append(";").append("\n");
                }
            }
        }

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


}
