package i2f.match.regex;

/**
 * @author Ice2Faith
 * @date 2024/4/29 16:32
 * @desc
 */
public class RegexPattens {
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

}
