package i2f.natives.windows.easyx;

import i2f.natives.windows.consts.WinBitBltRop;
import i2f.natives.windows.easyx.consts.EasyXInitGraphFlag;
import i2f.natives.windows.easyx.types.ImagePtr;
import i2f.natives.windows.types.Hwnd;

/**
 * @author Ice2Faith
 * @date 2024/5/12 21:39
 * @desc
 */
public class EasyXApi {
    public static String hello(){
        return NativesEasyX.hello();
    }

    public static Hwnd initGraph(int width, int height, int flag){
        long ret = NativesEasyX.initGraph(width, height, flag);
        return new Hwnd(ret);
    }

    public static Hwnd initGraph(int width, int height){
        long ret = NativesEasyX.initGraph(width, height, EasyXInitGraphFlag.DEFAULT);
        return new Hwnd(ret);
    }

    public static void closeGraph(){
        NativesEasyX.closeGraph();
    }


    public static void clearDevice(){
        NativesEasyX.clearDevice();
    }

    public static void beginBatchDraw(){
        NativesEasyX.beginBatchDraw();
    }

    public static void endBatchDraw(){
        NativesEasyX.endBatchDraw();
    }

    public static void flushBatchDraw(){
        NativesEasyX.flushBatchDraw();
    }

    public static void setWorkingImage(ImagePtr pImage){
        NativesEasyX.setWorkingImage(pImage.value());
    }

    public static ImagePtr loadImage(
            String filePath,
            int width,
            int height,
            boolean resize){
        long ret = NativesEasyX.loadImage(filePath, width, height, resize);
        return new ImagePtr(ret);
    }

    public static ImagePtr loadImage(
            String filePath){
        long ret = NativesEasyX.loadImage(filePath, 0, 0, false);
        return new ImagePtr(ret);
    }

    public static void freeImage(ImagePtr pImage){
        NativesEasyX.freeImage(pImage.value());
    }

    public static ImagePtr createImage(int width,int height){
        long ret = NativesEasyX.createImage(width, height);
        return new ImagePtr(ret);
    }

    public static void saveImage(ImagePtr pImage,String filePath){
        NativesEasyX.saveImage(pImage.value(),filePath);
    }

    public static void saveImage(String filePath){
        NativesEasyX.saveImage(0,filePath);
    }

    public static void putImage(
            int dstX,
            int dstY,
            ImagePtr pImage,
            long dwRop){
        NativesEasyX.putImage(dstX,dstY,pImage.value(),dwRop);
    }

    public static void putImage(
            int dstX,
            int dstY,
            ImagePtr pImage){
        NativesEasyX.putImage(dstX,dstY,pImage.value(), WinBitBltRop.SRCCOPY);
    }

    public static void setBkMode(int mode){
        NativesEasyX.setBkMode(mode); // WinSetBkModeMode
    }

    public static void setBkColor(int color){
        NativesEasyX.setBkColor(color);
    }

    public static void setColor(int color){
        NativesEasyX.setColor(color);
    }

    public static void setFillColor(int color){
        NativesEasyX.setFillColor(color);
    }

    public static void setLineColor(int color){
        NativesEasyX.setLineColor(color);
    }

    public static void circle(int x,int y,int radius){
        NativesEasyX.circle(x,y,radius);
    }

    public static void fillCircle(int x,int y,int radius){
        NativesEasyX.fillCircle(x, y, radius);
    }

    public static void clearCircle(int x,int y,int radius){
        NativesEasyX.clearCircle(x, y, radius);
    }

    public static void solidCircle(int x,int y,int radius){
        NativesEasyX.solidCircle(x, y, radius);
    }

    public static void rectangle(int left,int top,int right,int bottom){
        NativesEasyX.rectangle(left, top, right, bottom);
    }

    public static void fillRectangle(int left,int top,int right,int bottom){
        NativesEasyX.fillRectangle(left, top, right, bottom);
    }

    public static void clearRectangle(int left,int top,int right,int bottom){
        NativesEasyX.clearRectangle(left, top, right, bottom);
    }

    public static void solidRectangle(int left,int top,int right,int bottom){
        NativesEasyX.solidRectangle(left, top, right, bottom);
    }


    public static void setTextColor(int color){
NativesEasyX.setTextColor(color);
    }

    public static void outText(String str){
        NativesEasyX.outText(str);
    }

    public static void outTextXy(int x,int y,String str){
        NativesEasyX.outTextXy(x,y,str);
    }


}
