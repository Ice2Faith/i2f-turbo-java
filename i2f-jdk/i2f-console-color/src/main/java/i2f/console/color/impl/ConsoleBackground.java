package i2f.console.color.impl;

import i2f.console.color.ConsoleElement;

/**
 * @author Ice2Faith
 * @date 2024/7/24 9:00
 * @desc
 */
public enum ConsoleBackground implements ConsoleElement {
    DEFAULT("49"),
    BLACK("40"),
    RED("41"),
    GREEN("42"),
    YELLOW("43"),
    BLUE("44"),
    MAGENTA("45"),
    CYAN("46"),
    WHITE("47"),
    BRIGHT_BLACK("100"),
    BRIGHT_RED("101"),
    BRIGHT_GREEN("102"),
    BRIGHT_YELLOW("103"),
    BRIGHT_BLUE("104"),
    BRIGHT_MAGENTA("105"),
    BRIGHT_CYAN("106"),
    BRIGHT_WHITE("107");

    private String code;

    private ConsoleBackground(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }
}
