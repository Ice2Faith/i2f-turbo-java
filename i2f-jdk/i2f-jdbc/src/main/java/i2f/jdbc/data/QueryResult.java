package i2f.jdbc.data;

import i2f.reflect.ReflectResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public <T> List<T> getAsBeans(Class<T> clazz) {
        return getAsBeans(clazz, null);
    }

    public <T> List<T> getAsBeans(Class<T> clazz, Function<String, String> columnNameMapper) {
        List<T> ret = new LinkedList<>();
        for (Map<String, Object> row : rows) {
            Map<String, Object> map = row;
            if (columnNameMapper != null) {
                map = new LinkedHashMap<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    map.put(columnNameMapper.apply(entry.getKey()), entry.getValue());
                }
            }
            try {
                T bean = ReflectResolver.getInstance(clazz);
                ReflectResolver.map2bean(map, bean);
                ret.add(bean);
            } catch (Exception e) {

            }
        }
        return ret;
    }

}
