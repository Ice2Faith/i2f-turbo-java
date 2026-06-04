package i2f.springboot.ops.openai.tool.impl.sql.impl;

import i2f.springboot.ops.openai.tool.impl.sql.OpsSqlValidator;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.analyze.Analyze;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.show.ShowIndexStatement;
import net.sf.jsqlparser.statement.show.ShowTablesStatement;

/**
 * @author Ice2Faith
 * @date 2026/6/4 10:52
 * @desc
 */
public class OpsSqlParserAstSqlValidator implements OpsSqlValidator {
    public static final OpsSqlParserAstSqlValidator INSTANCE = new OpsSqlParserAstSqlValidator();

    @Override
    public void validateQuery(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("sql cannot be blank");
        }
        validateQuerySql(sql);
    }

    public static String validateQuerySql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }


        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new IllegalArgumentException("sql validator cannot recognize this grammar! maybe contains keywords or current validator not support, but you may still attempt to run it manually.");
        }

        // 白名单校验：仅允许 SELECT, EXPLAIN, ANALYZE, SHOW, DESCRIBE
        boolean isAllowed = statement instanceof Select
                || statement instanceof ExplainStatement
                || statement instanceof Analyze
                || statement instanceof ShowStatement
                || statement instanceof DescribeStatement
                || statement instanceof ShowColumnsStatement
                || statement instanceof ShowTablesStatement
                || statement instanceof ShowIndexStatement;

        if (!isAllowed) {
            throw new IllegalArgumentException("only support [select,explain,analyze,show,describe] sql statement");
        }

        return sql;
    }
}
