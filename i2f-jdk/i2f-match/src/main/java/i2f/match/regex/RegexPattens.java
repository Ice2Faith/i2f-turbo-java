package i2f.match.regex;

/**
 * @author Ice2Faith
 * @date 2024/4/29 16:32
 * @desc
 */
public class RegexPattens {

    /**
     * match for :
     * integer number, such as
     * 12
     * 1
     * +80
     * -60
     * 0
     */
    public static final String INTEGER_NUMBER_REGEX = "[+|-]?(0|[1-9][0-9]*)";

    /**
     * match for :
     * double number, such as
     * 12
     * 12.105
     * -1.5
     * 0.3
     */
    public static final String DOUBLE_NUMBER_REGEX = INTEGER_NUMBER_REGEX + "(\\.[0-9]+)?";

    /**
     * match for :
     * standard var defined name for c/c++/java
     * such as:
     * a
     * _
     * _ac
     * a_c
     * a52
     * a_52
     */
    public static final String DECLARE_NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9_]*";

    /**
     * match for :
     * standard full name for java
     * such:
     * a
     * a.b
     * a.b.c
     */
    public static final String DECLARE_FULL_NAME_REGEX = DECLARE_NAME_REGEX + "(" + DECLARE_NAME_REGEX + ")*";

    /**
     * match for :
     * standard C/C++ or java defined a String value,such as
     * "a"
     * "a "
     * "a\"b"
     * "a\'b "
     */
    public static final String QUOTE_STRING_REGEX = "\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"";

    /**
     * match for :
     * standard js or other single quote defined a String value,such as
     * 'a'
     * 'a '
     * 'a\'b'
     * 'a\"b '
     */
    public static final String SINGLE_QUOTE_STRING_REGEX = "\'([^\'\\\\]*(\\\\.[^\'\\\\]*)*)\'";

    /**
     * match for :
     * standard single line comment block, such as
     * // xxx
     */
    public static final String SINGLE_LINE_COMMENT_REGEX = "\\/\\/[^\\n]*";

    /**
     * match for :
     * standard multi line comment block, such as
     * \/* xxx *\/
     */
    public static final String MULTI_LINE_COMMENT_REGEX = "\\/\\*([^*]|(\\*+([^*/])))*\\*+\\/";


    /**
     * match for :
     * standard single/multi line comment block, such as
     * // xxx
     * \/* xxx *\/
     */
    public static final String COMMON_COMMENT_REGEX = SINGLE_LINE_COMMENT_REGEX + "|" + MULTI_LINE_COMMENT_REGEX;

    /**
     * match for :
     * sharp style single line comment block, such as
     * # xxx
     */
    public static final String SINGLE_LINE_COMMENT_SHARP_REGEX = "#[^\\n]*";

    /**
     * match for :
     * sql style single line comment block, such as
     * -- xxx
     */
    public static final String SINGLE_LINE_COMMENT_SQL_REGEX = "--[ |\\t]+[^\\n]*";

    /**
     * match for :
     * xml style multi line comment block, such as
     * <!-- xxx -->
     */
    public static final String XML_COMMENT_REGEX = "\\<\\!\\-\\-[^\\-\\-\\>]*\\-\\-\\>";


    /**
     * match for:
     * double " string, such as
     * "user"
     * "user say \"a's name is b\" "
     */
    public static final String SIMPLE_DOUBLE_QUOTE_STRING_REGEX = "\"((\\\\\")+|[^\"])*\"";

    /**
     * match for:
     * single ' string,such as
     * 'user'
     * 'user say \'a "s" name is b\' '
     */
    public static final String SIMPLE_SINGLE_QUOTE_STRING_REGEX = "'((\\\\')+|[^'])*'";

    /**
     * match for :
     * {} bracket, such as
     * {a+b\}-c}
     */
    public static final String BIG_BRACKET_REGEX = "\\{((\\\\})+|[^}])*\\}";

    /**
     * match for :
     * [] bracket, such as
     * [a+b\]-c]
     */
    public static final String MID_BRACKET_REGEX = "\\[((\\\\])+|[^\\]])*\\]";

    /**
     * match for :
     * [] bracket, such as
     * [a+b\]-c]
     */
    public static final String LIT_BRACKET_REGEX = "\\(((\\\\\\))+|[^\\)])*\\)";

    /**
     * match for :
     * indexed format placeholder, such as
     * {}
     * {:yyyy-MM}
     * {0}
     * {-1}
     * {2:yyyy-MM-dd HH:mm:ss}
     * {3: \{yyyyMMdd\}-HHmmss.SSS}
     * { 4 :%02d}
     * { 5 t:yyyy-MM-dd HH:mm:ss}
     */
    public static final String INDEXED_FORMAT_PLACEHOLDER_REGEX = "([^\\\\]|^)\\{(\\s*[+|-]?\\d+\\s*)?(\\s*[a-zA-Z]?:(((\\\\}|\\{)+|[^}])+)?)?\\}";

}
