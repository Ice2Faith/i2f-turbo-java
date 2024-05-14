package i2f.natives.windows.easyx.test;

import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.gdi.WinSetBkModeMode;
import i2f.natives.windows.easyx.EasyXApi;
import i2f.natives.windows.types.window.Hwnd;

import java.util.Random;

/**
 * @author Ice2Faith
 * @date 2024/5/12 21:57
 * @desc
 */
public class TestEasyXApi {
    public static void main(String[] args) throws InterruptedException {
        int width=720;
        int height=480;

        Hwnd hwnd = EasyXApi.initGraph(width, height);
        Thread.sleep(1000);

        EasyXApi.setBkMode(WinSetBkModeMode.TRANSPARENT);
        EasyXApi.setBkColor(WinApi.rgbOf(255,255,255));
        EasyXApi.clearDevice();

        EasyXApi.setColor(WinApi.rgbOf(255,0,0));
        EasyXApi.setFillColor(WinApi.rgbOf(0,255,0));
        EasyXApi.setLineColor(WinApi.rgbOf(0,0,255));
        EasyXApi.setTextColor(WinApi.rgbOf(0,255,255));

        EasyXApi.circle(10,10,10);
        Thread.sleep(1000);

        EasyXApi.fillCircle(20,20,10);
        Thread.sleep(1000);

        EasyXApi.clearCircle(30,30,10);
        Thread.sleep(1000);

        EasyXApi.solidCircle(40,40,10);
        Thread.sleep(1000);

        EasyXApi.outTextXy(100,100,"Java中用JNI使用EasyX");
        Thread.sleep(1000);

        EasyXApi.outTextXy(100,200,"这是一些简单的使用实例");
        Thread.sleep(1000);

        EasyXApi.outTextXy(100,300,"即将为你展示一些随机行为");
        Thread.sleep(1000);

        Random rand=new Random();
        for (int i = 0; i < 50; i++) {
            int x=rand.nextInt(width);
            int y=rand.nextInt(height);
            WinApi.sleep(30);
            EasyXApi.setFillColor(WinApi.rgbOf(rand.nextInt(105)+150, rand.nextInt(105)+150,rand.nextInt(105)+150));
            EasyXApi.solidRectangle(x,y,x+rand.nextInt(width/2),y+rand.nextInt(height/2));
        }

        EasyXApi.outTextXy(100,250,"这就结束了，按任意键退出");
        WinApi.getCh();
        EasyXApi.closeGraph();

    }
}
