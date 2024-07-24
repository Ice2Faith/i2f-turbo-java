package i2f.console.color.impl;

import i2f.console.color.ConsoleElement;

/**
 * @author Ice2Faith
 * @date 2024/7/24 9:01
 * @desc
 */
public enum ConsoleStyle implements ConsoleElement {
    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4");

    private String code;

    private ConsoleStyle(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }
}
