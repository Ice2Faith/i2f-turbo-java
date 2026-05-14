package i2f.mixin.impl;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/5/14 8:59
 * @desc
 */
public interface CommandLineMixins {

    default void exec(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void exec(String... commandArray) {
        try {
            Runtime.getRuntime().exec(commandArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void system(String command) {
        exec(command);
    }

    default void system(String... commandArray) {
        exec(commandArray);
    }

}
