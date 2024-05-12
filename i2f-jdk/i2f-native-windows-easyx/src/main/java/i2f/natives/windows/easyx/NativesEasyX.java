package i2f.natives.windows.easyx;

import i2f.natives.core.NativeUtil;

/**
 * @author Ice2Faith
 * @date 2024/5/12 21:39
 * @desc
 */
public class NativesEasyX {
    static {
        NativeUtil.loadClasspathLib("lib/NativesEasyX");
    }

    public static native String hello();

    public static native long initGraph(int width, int height, int flag);

    public static native void closeGraph();

    public static native void clearDevice();

    public static native void beginBatchDraw();

    public static native void endBatchDraw();

    public static native void flushBatchDraw();

    public static native void setWorkingImage(long pImage);

    public static native long loadImage(
            String filePath,
            int width,
            int height,
            boolean resize);

    public static native void freeImage(long pImage);

    public static native long createImage(int width, int height);

    public static native void saveImage(long pImage, String filePath);

    public static native void putImage(
            int dstX,
            int dstY,
            long pImage,
            long dwRop);

    public static native void setBkMode(int mode);

    public static native void setBkColor(int color);

    public static native void setColor(int color);

    public static native void setFillColor(int color);

    public static native void setLineColor(int color);

    public static native void circle(int x,int y,int radius);

    public static native void fillCircle(int x,int y,int radius);

    public static native void clearCircle(int x,int y,int radius);

    public static native void solidCircle(int x,int y,int radius);

    public static native void rectangle(int left,int top,int right,int bottom);

    public static native void fillRectangle(int left,int top,int right,int bottom);

    public static native void clearRectangle(int left,int top,int right,int bottom);

    public static native void solidRectangle(int left,int top,int right,int bottom);

    public static native void setTextColor(int color);

    public static native void outText(String str);

    public static native void outTextXy(int x,int y,String str);


}
