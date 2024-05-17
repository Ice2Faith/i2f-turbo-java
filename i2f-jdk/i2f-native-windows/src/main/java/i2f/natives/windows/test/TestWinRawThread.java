package i2f.natives.windows.test;

import i2f.natives.windows.WinApi;
import i2f.natives.windows.types.Handle;

/**
 * @author Ice2Faith
 * @date 2024/5/17 10:38
 * @desc
 */
public class TestWinRawThread {
    public static void main(String[] args) {
        Handle hThread1 = WinApi.createThread(() -> {
            System.out.println("thread-1");
        });
        Handle hThread2 = WinApi.createThread(() -> {
            System.out.println("thread-2");
        });
        WinApi.waitForMultipleObjects(new Handle[]{hThread1, hThread2});

        System.out.println("main");

    }
}
