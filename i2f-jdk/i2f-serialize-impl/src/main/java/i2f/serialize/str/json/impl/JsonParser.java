package i2f.serialize.str.json.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Single-pass recursive descent JSON parser, JDK8 only, no third-party dependencies.
 *
 * <p>Lenient syntax: {@code //} line comments, {@code /* *}{@code /} block comments,
 * trailing commas in objects/arrays, single-quoted strings and unquoted object keys
 * (Java/JS identifier syntax) are accepted.</p>
 *
 * <p>Retains object key order (LinkedHashMap). Numbers map to Integer or Long for
 * integral values, Double when a fraction/exponent is present, and fall back to
 * BigInteger / BigDecimal when the value does not fit.</p>
 */
public final class JsonParser {

    private static final char[] TRUE = "true".toCharArray();
    private static final char[] FALSE = "false".toCharArray();
    private static final char[] NULL = "null".toCharArray();

    private final char[] src;
    private final int end;
    private int pos;

    private JsonParser(char[] src, int off, int len) {
        this.src = src;
        this.pos = off;
        this.end = off + len;
    }

    public static Object parse(String json) {
        if (json == null) {
            return null;
        }
        return parse(json.toCharArray(), 0, json.length());
    }

    public static Object parse(char[] json, int off, int len) {
        JsonParser parser = new JsonParser(json, off, len);
        parser.skipWhitespaceAndComments();
        Object value = parser.readValue();
        parser.skipWhitespaceAndComments();
        if (parser.pos != parser.end) {
            throw parser.error("unexpected trailing characters");
        }
        return value;
    }

    private Object readValue() {
        if (pos >= end) {
            throw error("unexpected end of input");
        }
        char c = src[pos];
        switch (c) {
            case '{':
                return readObject();
            case '[':
                return readArray();
            case '"':
            case '\'':
                return readString(c);
            case 't':
                expectLiteral(TRUE);
                return Boolean.TRUE;
            case 'f':
                expectLiteral(FALSE);
                return Boolean.FALSE;
            case 'n':
                expectLiteral(NULL);
                return null;
            default:
                if (c == '-' || (c >= '0' && c <= '9')) {
                    return readNumber();
                }
                throw error("unexpected character '" + c + "'");
        }
    }

    private Map<String, Object> readObject() {
        pos++;
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        skipWhitespaceAndComments();
        if (pos < end && src[pos] == '}') {
            pos++;
            return map;
        }
        while (true) {
            skipWhitespaceAndComments();
            if (pos >= end) {
                throw error("expected object key");
            }
            String key = readKey();
            skipWhitespaceAndComments();
            if (pos >= end || src[pos] != ':') {
                throw error("expected ':' after object key");
            }
            pos++;
            skipWhitespaceAndComments();
            map.put(key, readValue());
            skipWhitespaceAndComments();
            if (pos >= end) {
                throw error("unterminated object");
            }
            char c = src[pos++];
            if (c == '}') {
                return map;
            }
            if (c != ',') {
                throw error("expected ',' or '}' in object");
            }
            skipWhitespaceAndComments();
            if (pos < end && src[pos] == '}') {
                pos++;
                return map;
            }
        }
    }

    private List<Object> readArray() {
        pos++;
        List<Object> list = new ArrayList<Object>();
        skipWhitespaceAndComments();
        if (pos < end && src[pos] == ']') {
            pos++;
            return list;
        }
        while (true) {
            skipWhitespaceAndComments();
            list.add(readValue());
            skipWhitespaceAndComments();
            if (pos >= end) {
                throw error("unterminated array");
            }
            char c = src[pos++];
            if (c == ']') {
                return list;
            }
            if (c != ',') {
                throw error("expected ',' or ']' in array");
            }
            skipWhitespaceAndComments();
            if (pos < end && src[pos] == ']') {
                pos++;
                return list;
            }
        }
    }

    private String readKey() {
        if (src[pos] == '"' || src[pos] == '\'') {
            return readString(src[pos]);
        }
        int start = pos;
        int cp = codePointAt(pos);
        if (!Character.isJavaIdentifierStart(cp)) {
            throw error("expected object key");
        }
        pos += cp > Character.MAX_VALUE ? 2 : 1;
        while (pos < end) {
            cp = codePointAt(pos);
            if (!Character.isJavaIdentifierPart(cp)) {
                break;
            }
            pos += cp > Character.MAX_VALUE ? 2 : 1;
        }
        return new String(src, start, pos - start);
    }

    private int codePointAt(int at) {
        char c = src[at];
        if (Character.isHighSurrogate(c) && at + 1 < end && Character.isLowSurrogate(src[at + 1])) {
            return Character.toCodePoint(c, src[at + 1]);
        }
        return c;
    }

    private String readString(char quote) {
        pos++;
        int start = pos;
        while (pos < end) {
            char c = src[pos];
            if (c == quote) {
                String value = new String(src, start, pos - start);
                pos++;
                return value;
            }
            if (c == '\\') {
                break;
            }
            pos++;
        }
        if (pos >= end) {
            throw error("unterminated string");
        }

        StringBuilder sb = new StringBuilder(pos - start + 16);
        sb.append(src, start, pos - start);
        while (pos < end) {
            char c = src[pos++];
            if (c == quote) {
                return sb.toString();
            }
            if (c != '\\') {
                sb.append(c);
                continue;
            }
            if (pos >= end) {
                throw error("unterminated escape sequence");
            }
            char esc = src[pos++];
            switch (esc) {
                case '"':
                case '\'':
                    sb.append(esc);
                    break;
                case '\\':
                    sb.append('\\');
                    break;
                case '/':
                    sb.append('/');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'u':
                    if (pos + 4 > end) {
                        throw error("truncated unicode escape");
                    }
                    sb.append((char) readHex4());
                    break;
                default:
                    throw error("invalid escape '\\" + esc + "'");
            }
        }
        throw error("unterminated string");
    }

    private int readHex4() {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            char c = src[pos++];
            int digit;
            if (c >= '0' && c <= '9') {
                digit = c - '0';
            } else if (c >= 'a' && c <= 'f') {
                digit = c - 'a' + 10;
            } else if (c >= 'A' && c <= 'F') {
                digit = c - 'A' + 10;
            } else {
                throw error("invalid hex digit '" + c + "' in unicode escape");
            }
            value = (value << 4) | digit;
        }
        return value;
    }

    private Object readNumber() {
        int start = pos;
        boolean negative = false;
        if (src[pos] == '-') {
            negative = true;
            pos++;
        }
        int digitsStart = pos;
        while (pos < end && src[pos] >= '0' && src[pos] <= '9') {
            pos++;
        }
        if (pos == digitsStart) {
            throw error("expected digit");
        }
        boolean decimal = false;
        if (pos < end && src[pos] == '.') {
            decimal = true;
            pos++;
            int fracStart = pos;
            while (pos < end && src[pos] >= '0' && src[pos] <= '9') {
                pos++;
            }
            if (pos == fracStart) {
                throw error("expected digit after decimal point");
            }
        }
        if (pos < end && (src[pos] == 'e' || src[pos] == 'E')) {
            decimal = true;
            pos++;
            if (pos < end && (src[pos] == '+' || src[pos] == '-')) {
                pos++;
            }
            int expStart = pos;
            while (pos < end && src[pos] >= '0' && src[pos] <= '9') {
                pos++;
            }
            if (pos == expStart) {
                throw error("expected digit in exponent");
            }
        }

        int len = pos - start;
        if (decimal) {
            double value = Double.parseDouble(new String(src, start, len));
            if (Double.isInfinite(value)) {
                return new BigDecimal(new String(src, start, len));
            }
            return Double.valueOf(value);
        }

        long value = 0;
        boolean overflow = false;
        for (int i = digitsStart; i < pos; i++) {
            int digit = src[i] - '0';
            if (value < (Long.MIN_VALUE + digit) / 10) {
                overflow = true;
                break;
            }
            value = value * 10 - digit;
        }
        if (overflow) {
            return new BigInteger(new String(src, start, len));
        }
        if (!negative) {
            if (value == Long.MIN_VALUE) {
                return new BigInteger(new String(src, start, len));
            }
            value = -value;
        }
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
            return Integer.valueOf((int) value);
        }
        return Long.valueOf(value);
    }

    private void expectLiteral(char[] literal) {
        if (pos + literal.length > end) {
            throw error("unexpected end of input");
        }
        for (int i = 0; i < literal.length; i++) {
            if (src[pos + i] != literal[i]) {
                throw error("invalid literal");
            }
        }
        pos += literal.length;
    }

    private void skipWhitespaceAndComments() {
        while (pos < end) {
            char c = src[pos];
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                pos++;
                continue;
            }
            if (c != '/' || pos + 1 >= end) {
                return;
            }
            char next = src[pos + 1];
            if (next == '/') {
                pos += 2;
                while (pos < end && src[pos] != '\n') {
                    pos++;
                }
            } else if (next == '*') {
                int close = pos + 2;
                while (close + 1 < end && !(src[close] == '*' && src[close + 1] == '/')) {
                    close++;
                }
                if (close + 1 >= end) {
                    pos = close;
                    throw error("unterminated block comment");
                }
                pos = close + 2;
            } else {
                return;
            }
        }
    }

    private IllegalArgumentException error(String message) {
        int line = 1;
        int column = 1;
        for (int i = 0; i < pos && i < end; i++) {
            if (src[i] == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return new IllegalArgumentException("JSON parse error at line " + line + ", column " + column + ": " + message);
    }
}
