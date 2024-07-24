package i2f.console.color.test;

import i2f.console.color.ConsoleOutput;
import i2f.console.color.impl.ConsoleColor;

/**
 * @author Ice2Faith
 * @date 2024/7/24 9:18
 * @desc
 */
public class TestConsoleColor {
    public static void main(String[] args) {
//        ConsoleOutput.setEnabled(ConsoleOutput.Enabled.ALWAYS);
        String str =
                ConsoleOutput.toAnsiString("01-01 12:13:14.123", ConsoleColor.BRIGHT_WHITE)
                        + " " + ConsoleOutput.toAnsiString("[INFO ]", ConsoleColor.BRIGHT_BLACK)
                        + " " + ConsoleOutput.toAnsiString("[System.out.println]", ConsoleColor.CYAN)
                        + " " + ConsoleOutput.toAnsiString("[main-1    ]", ConsoleColor.MAGENTA)
                        + " - "
                        + "msg";
        System.out.println(str);
    }
}
