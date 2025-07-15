package i2f.extension.sqlparser;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/7/7 10:32
 */
public class SqlParserUtil {

    /**
     * 转换SQL为count-sql
     * 优先以sqlparser方式，直接将查询的列变为count(1)，去除order by 语句，这样可以提升count查询的效率
     * 否则的话，直接包装为子查询进行count(1)，这样虽然通用，但是查询效率一般不太好
     * -------------------------------------------------------
     * sqlparser 默认支持 ? 占位符，因此就算传入带有 ? 占位符的SQL也是可以的
     * 但是，默认不支持 Mybatis 风格的 #{} 占位符，所以这种情况就会有问题，触发降级处理策略
     *
     * @param sql
     * @return
     */
    public static String wrapAsCountSql(String sql) {
        try {
            CCJSqlParser parser = new CCJSqlParser(sql);
            parser.withAllowComplexParsing(false)
                    .withUnsupportedStatements(false);
            parser.setErrorRecovery(false);
            Statement statement = parser.Statement();
            if (statement instanceof PlainSelect) {
                PlainSelect select = (PlainSelect) statement;
                List<SelectItem<?>> selectItems = new ArrayList<>();
                selectItems.add(new SelectItem<>(new Column("count(1)"), new Alias("cnt")));
                select.setSelectItems(selectItems);
                select.setOrderByElements(new ArrayList<>());
                return select.toString();
            }
        } catch (ParseException e) {

        }
        return " select count(1) cnt from (\n" +
                sql +
                "\n ) tmp_count";
    }
}
