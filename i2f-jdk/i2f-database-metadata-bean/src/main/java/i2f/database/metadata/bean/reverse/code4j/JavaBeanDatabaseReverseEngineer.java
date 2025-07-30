package i2f.database.metadata.bean.reverse.code4j;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.std.DatabaseReverseEngineer;
import i2f.text.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/7/30 16:16
 */
public class JavaBeanDatabaseReverseEngineer implements DatabaseReverseEngineer {
    public static final JavaBeanDatabaseReverseEngineer INSTANCE = new JavaBeanDatabaseReverseEngineer();

    @Override
    public String generate(TableMeta meta) {
        meta.fillColumnIndexMeta();

        StringBuilder builder = new StringBuilder();

        builder.append("import lombok.Data;").append("\n");
        builder.append("import lombok.NoArgsConstructor;").append("\n");

        builder.append("\n");
        builder.append("import java.sql.JDBCType;").append("\n");
        builder.append("import java.util.Date;").append("\n");

        builder.append("\n");
        builder.append("@Data").append("\n");
        builder.append("@NoArgsConstructor").append("\n");
        builder.append("@Catalog(\"").append(meta.getCatalog() == null ? "" : meta.getCatalog()).append("\")").append("\n");
        builder.append("@Schema(\"").append(meta.getSchema() == null ? "" : meta.getSchema()).append("\")").append("\n");
        builder.append("@Database(\"").append(meta.getDatabase() == null ? "" : meta.getDatabase()).append("\")").append("\n");
        builder.append("@Comment(\"").append(meta.getComment() == null ? "" : meta.getComment()).append("\")").append("\n");
        builder.append("@TableType(\"").append(meta.getType() == null ? "" : meta.getType()).append("\")").append("\n");
        builder.append("@Table(\"").append(meta.getName() == null ? "" : meta.getName()).append("\")").append("\n");
        builder.append("public class ").append(StringUtils.toPascal(meta.getName())).append(" {").append("\n");


        List<ColumnMeta> columns = meta.getColumns();
        if (columns != null) {
            for (ColumnMeta item : columns) {
                if (item == null) {
                    continue;
                }
                builder.append("\n");
                if (item.isIndexKey()) {
                    for (Map.Entry<String, Integer> entry : item.getIndexKeyList()) {
                        builder.append("\t@Index(value = \"").append(entry.getKey()).append("\"").append(", order = ").append(entry.getValue()).append(")").append("\n");

                    }
                }
                if (item.isUniqueKey()) {
                    for (Map.Entry<String, Integer> entry : item.getUniqueKeyList()) {
                        builder.append("\t@Unique(value = \"").append(entry.getKey()).append("\"").append(", order = ").append(entry.getValue()).append(")").append("\n");

                    }
                }
                if (item.isPrimaryKey()) {
                    builder.append("\t@Primary(value = \"").append(item.getPrimaryKeyName()).append("\"").append(", order = ").append(item.getPrimaryKeyOrder()).append(")").append("\n");
                }
                if (item.getDefaultValue() != null && !item.getDefaultValue().isEmpty()) {
                    builder.append("\t@Default(\"").append(item.getComment()).append("\")").append("\n");
                }
                if (item.isAutoIncrement()) {
                    builder.append("@AutoIncrement").append("\n");
                }
                if (!item.isNullable()) {
                    builder.append("@NotNull").append("\n");
                }
                builder.append("\t@Comment(\"").append(item.getComment()).append("\")").append("\n");
                builder.append("\t@DataType(value = \"").append(item.getType()).append("\"").append(", precision = ").append(item.getPrecision()).append(", scale = ").append(item.getScale()).append(")").append("\n");
                builder.append("\t@JdbcType(\"").append(item.getJdbcType()).append("\")").append("\n");
                builder.append("\t@Column(\"").append(item.getName()).append("\")").append("\n");
                builder.append("\tprotected ").append(item.getJavaType()).append(StringUtils.toCamel(item.getName())).append("\n");
            }

        }

        builder.append("}").append("\n");
        return builder.toString();
    }
}
