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

    public Object get(int rowIndex, int colIndex) {
        return rows.get(rowIndex).get(columns.get(colIndex).getName());
    }


}
