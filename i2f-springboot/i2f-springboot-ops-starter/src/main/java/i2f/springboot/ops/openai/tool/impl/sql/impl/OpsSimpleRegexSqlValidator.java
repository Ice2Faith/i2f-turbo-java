package i2f.springboot.ops.openai.tool.impl.sql.impl;

import i2f.match.regex.RegexPattens;
import i2f.match.regex.RegexUtil;
import i2f.springboot.ops.openai.tool.impl.sql.OpsSqlValidator;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2026/6/4 10:51
 * @desc
 */
public class OpsSimpleRegexSqlValidator implements OpsSqlValidator {

    public static final OpsSimpleRegexSqlValidator INSTANCE = new OpsSimpleRegexSqlValidator();

    @Override
    public void validateQuery(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("sql cannot be blank");
        }
        assertQuerySql(sql);
    }

    public static String assertCommonSql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        // 移除注释
        sql = sql.replaceAll(RegexPattens.MULTI_LINE_COMMENT_REGEX, "");
        sql = sql.replaceAll(RegexPattens.SINGLE_LINE_COMMENT_SQL_REGEX, "");
        // 移除转义 \\
        sql = sql.replaceAll("[\\\\]{2}", "");
        // 移除转义的单引号 \'
        sql = sql.replaceAll("\\\\'", "");
        // 移除字符串常量 'xxx'
        sql = sql.replaceAll("'[^']*'", "");
        // 移除转义的单引号 \"
        sql = sql.replaceAll("\\\\\"", "");
        // 移除字符串标识符 "xxx"
        sql = sql.replaceAll("\"[^\"]*\"", "");
        // 移除转义的单引号 \`
        sql = sql.replaceAll("\\\\`", "");
        // 移除字符串标识符 `xxx`
        sql = sql.replaceAll("`[^`]*`", "");

        // 现在剩下的就是可分解的字符串了，不存在转义，理论上应该都是一系列的标识符构成

        // 多条语句的情况
        if (sql.contains(";")) {
            throw new IllegalArgumentException("sql cannot contains \";\"");
        }

        // 因为移除了字符串常量，此时还包含单引号，那就是语句有问题
        if (sql.contains("'")) {
            throw new IllegalArgumentException("sql string \"'\" not enclosed");
        }

        return sql;
    }

    public static String assertQuerySql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        sql = assertCommonSql(sql);

        sql = sql.toLowerCase();

        String[] badKeywords = {
                "update", "delete", "insert", "truncate", "merge", "upsert",
                "create", "drop", "replace", "alter", "rename",
                "grant", "revoke",
                "comment",
                "execute",
                "call",
                "exec",
                "copy", "load", "save",
                "duplicate", "infile", "outfile", "dumpfile",
                "conflict", "returning", "prepare",
                "dblink_exec", "parse", "getxml", "request", "create_policy",
        };

        for (String item : badKeywords) {
            if (!RegexUtil.regexFinds(sql, "(?s)(^|\\s+)" + item + "($|\\s+)").isEmpty()) {
                throw new IllegalArgumentException("query sql cannot contains : " + item);
            }
            // \b 正则中的单词定界标志，用于单词边界，零宽断言，所在位置需要时分割单词的字符，也就是非单词，可以是空白符或者符号
            if (!RegexUtil.regexFinds(sql, "(?s)(^|\\b)" + item + "($|\\b)").isEmpty()) {
                throw new IllegalArgumentException("query sql cannot contains : " + item);
            }
        }

        String[] mustIncludeKeywords = {
                "select", "show", "describe", "explain", "analyze"
        };

        boolean include = false;
        for (String item : mustIncludeKeywords) {
            if (!RegexUtil.regexFinds(sql, "(?s)(^|\\s+)" + item + "($|\\s+)").isEmpty()) {
                include = true;
                break;
            }
        }

        if (!include) {
            throw new IllegalArgumentException("sql must include query keywords: " + Arrays.toString(mustIncludeKeywords));
        }

        sql = sql.trim();

        String[] startKeywords = {
                "select", "show", "describe", "explain", "analyze", "with"
        };

        boolean start = false;
        for (String item : startKeywords) {
            if (sql.startsWith(item)) {
                start = true;
                break;
            }
        }

        if (!start) {
            throw new IllegalArgumentException("sql must start with query keywords: " + Arrays.toString(startKeywords));
        }

        return sql;
    }
}
