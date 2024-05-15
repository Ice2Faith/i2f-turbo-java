package i2f.natives.windows.test;

import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.winapp.WinAppCallbacker;
import i2f.natives.windows.consts.winapp.WinAppResizeMode;
import i2f.natives.windows.consts.window.WinSendMessageMsg;
import i2f.natives.windows.consts.window.WinShowWindowCmdShow;
import i2f.natives.windows.types.winapp.Win32AppInstancePtr;
import i2f.natives.windows.types.window.Hwnd;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/5/15 21:22
 * @desc
 */
public class TestWinApp {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(1);
        new Thread(()->{
            WinApi.winAppCreateWin32App("TestWin32App",
                    "Win32应用程序JAVA示例",
                    null,
                    WinShowWindowCmdShow.SW_SHOW,
                    true,
                    WinAppResizeMode.RESIZE_MODE_ADAPT,
                    new WinAppCallbacker() {
                        @Override
                        public long handle(Win32AppInstancePtr pInstance, Hwnd hwnd, long message, long wParam, long lParam) {
                            if(message== WinSendMessageMsg.WM_MOUSEMOVE){
                                int x=WinApi.getXLParam(lParam);
                                int y=WinApi.getYLParam(lParam);
                                System.out.println("java:"+message+","+wParam+","+lParam+","+x+","+y);
                            }
                            return 0;
                        }
                    });
            latch.countDown();
        }).start();

        latch.await();
    }
}
