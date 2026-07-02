package i2f.jdbc.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/14 11:04
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {
    protected List<QueryColumn> columns;
    protected List<Map<String, Object>> rows;

    public int rowCount() {
        return rows.size();
    }

    public int colCount() {
        return columns.size();
    }

    public String getColumnName(int colIndex) {
        return columns.get(colIndex).getName();
    }

    public Object get(int rowIndex, int colIndex) {
        return rows.get(rowIndex).get(columns.get(colIndex).getName());
    }

    public Object get(int rowIndex, String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            QueryColumn col = columns.get(i);
            if (col.getName().equals(columnName)) {
                return rows.get(rowIndex).get(columns.get(i).getName());
            }
        }
        return null;
    }

    public Object getIgnoreCase(int rowIndex, String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            QueryColumn col = columns.get(i);
            if (col.getName().equalsIgnoreCase(columnName)) {
                return rows.get(rowIndex).get(columns.get(i).getName());
            }
        }
        return null;
    }

}
