package i2f.console.color;

import i2f.console.color.impl.ConsoleColor;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Locale;

/**
 * @author Ice2Faith
 * @date 2024/7/24 9:02
 * @desc
 */
public class ConsoleOutput {
    public static final String ENCODE_START = "\u001b[";
    public static final String ENCODE_END = "m";
    public static final String ENCODE_JOIN = ";";
    public static final String OPERATING_SYSTEM_NAME;
    public static final String RESET;

    private static Enabled enabled = Enabled.DETECT;
    private static Boolean consoleAvailable;
    private static Boolean ansiCapable;


    static {
        enabled = Enabled.DETECT;
        OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        RESET = "0" + ENCODE_JOIN + ConsoleColor.DEFAULT;
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String argument : arguments) {
            if (argument == null || argument.isEmpty()) {
                continue;
            }
            if (!argument.startsWith("-javaagent:")) {
                continue;
            }
            if (argument.contains("idea_rt.jar")) {
                enabled = Enabled.ALWAYS;
                break;
            }
            if (argument.contains("JetBrains")
                    && argument.contains("debugger-agent.jar")) {
                enabled = Enabled.ALWAYS;
                break;
            }
        }
    }

    public static enum Enabled {
        DETECT,
        ALWAYS,
        NEVER;

        private Enabled() {
        }
    }

    public ConsoleOutput() {
    }

    public static void setEnabled(Enabled enabled) {
        if (enabled == null) {
            enabled = Enabled.DETECT;
        }
        ConsoleOutput.enabled = enabled;
    }

    public static void setConsoleAvailable(Boolean consoleAvailable) {
        ConsoleOutput.consoleAvailable = consoleAvailable;
    }

    static Enabled getEnabled() {
        return enabled;
    }

    public static String encode(ConsoleElement element) {
        if (!isEnabled()) {
            return "";
        }
        return ENCODE_START + element + ENCODE_END;
    }

    public static String toString(Object... elements) {
        StringBuilder builder = new StringBuilder();
        if (isEnabled()) {
            buildEnabled(builder, elements);
        } else {
            buildDisabled(builder, elements);
        }

        return builder.toString();
    }

    public static String toAnsiString(String in, ConsoleElement element) {
        return toString(new Object[]{element, in});
    }

    public static void buildEnabled(StringBuilder builder, Object[] elements) {
        boolean writingAnsi = false;
        boolean containsEncoding = false;

        for (int i = 0; i < elements.length; i++) {
            Object element = elements[i];
            if (element instanceof ConsoleElement) {
                containsEncoding = true;
                if (!writingAnsi) {
                    builder.append(ENCODE_START);
                    writingAnsi = true;
                } else {
                    builder.append(ENCODE_JOIN);
                }
            } else if (writingAnsi) {
                builder.append(ENCODE_END);
                writingAnsi = false;
            }

            builder.append(element);
        }

        if (containsEncoding) {
            builder.append(writingAnsi ? ENCODE_JOIN : ENCODE_START);
            builder.append(RESET);
            builder.append(ENCODE_END);
        }

    }

    public static void buildDisabled(StringBuilder builder, Object[] elements) {
        for (int i = 0; i < elements.length; i++) {
            Object element = elements[i];
            if (element == null) {
                continue;
            }
            if (element instanceof ConsoleElement) {
                continue;
            }
            builder.append(element);

        }
    }

    public static boolean isEnabled() {
        if (enabled == ConsoleOutput.Enabled.DETECT) {
            if (ansiCapable == null) {
                ansiCapable = detectIfAnsiCapable();
            }
            return ansiCapable;
        }
        return enabled == ConsoleOutput.Enabled.ALWAYS;
    }

    public static boolean detectIfAnsiCapable() {
        try {
            if (Boolean.FALSE.equals(consoleAvailable)) {
                return false;
            }
            if (consoleAvailable == null && System.console() == null) {
                return false;
            }
            return !OPERATING_SYSTEM_NAME.contains("win");
        } catch (Throwable var1) {
            return false;
        }
    }


}

