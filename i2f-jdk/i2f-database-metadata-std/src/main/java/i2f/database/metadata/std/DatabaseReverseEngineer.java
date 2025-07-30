package i2f.database.metadata.std;

import i2f.database.metadata.data.TableMeta;

/**
 * @author Ice2Faith
 * @date 2025/7/30 9:29
 */
public interface DatabaseReverseEngineer {
    String generate(TableMeta meta);

    default String generate(Iterable<TableMeta> list) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (TableMeta item : list) {
            if (item == null) {
                continue;
            }
            if (!isFirst) {
                builder.append("\n");
            }
            String sql = generate(item);
            builder.append(sql).append("\n;");
            isFirst = false;
        }
        return builder.toString();
    }
}
