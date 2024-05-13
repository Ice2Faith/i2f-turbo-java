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

    public static native void circle(int x, int y, int radius);

    public static native void fillCircle(int x, int y, int radius);

    public static native void clearCircle(int x, int y, int radius);

    public static native void solidCircle(int x, int y, int radius);

    public static native void rectangle(int left, int top, int right, int bottom);

    public static native void fillRectangle(int left, int top, int right, int bottom);

    public static native void clearRectangle(int left, int top, int right, int bottom);

    public static native void solidRectangle(int left, int top, int right, int bottom);

    public static native void setTextColor(int color);

    public static native void outText(String str);

    public static native void outTextXy(int x, int y, String str);

    public static native int hslToRgb(float h, float s, float l);

    public static native int hsvToRgb(float h, float s, float v);

    public static native int bgr(int color);

    public static native int getRValue(int color);

    public static native int getGValue(int color);

    public static native int getBValue(int color);

    public static native int rgbToGray(int color);

    public static native float[] rgbToHsl(int color);

    public static native float[] rgbToHsv(int color);

    public static native int getBkColor();

    public static native int getBkMode();

    public static native int getFillColor();

    public static native int getLineColor();

    public static native int getPolyFillMode();

    public static native void setPolyFillMode(int mode);

    public static native int getRop2();

    public static native void setRop2(int mode);

    public static native void clearClipRgn();

    public static native float[] getAspectRatio();

    public static native void setAspectRatio(float xasp, float yasp);

    public static native void graphDefaults();

    public static native void setOrigin(int x, int y);

    public static native int drawText(String text, int left, int top, int right, int bottom, int uFormat);

    public static native int getTextColor();

    public static native int textHeight(String text);

    public static native int textWidth(String text);

    public static native long getImageBuffer(long pImage);

    public static native void setImageBufferValue(long pBuffer, int index, int value);

    public static native int getImageBufferValue(long pBuffer, int index);

    public static native long getImageHDC(long pImage);

    public static native long getWorkingImage();

    public static native int getImageHeight(long pImage);

    public static native int getImageWidth(long pImage);

    public static native void resize(long pImage, int width, int height);

    public static native void rotateImage(
            long pDstImage,
            long pSrcImage,
            double radian,
            int bkColor,
            boolean autosize,
            boolean highQuality);

    public static native void flushMouseMsgBuffer();

    public static native boolean mouseHit();

    public static native String getMouseMsg();

    public static native String getEasyXVer();

    public static native long getHWnd();

    public static native String inputBox(
            String prompt,
            String title,
            String defaultText,
            int width,
            int height,
            boolean onlyOk
    );

    public static native int getColor();

    public static native int getWidth();

    public static native int getHeight();

    public static native int getMaxX();

    public static native int getMaxY();

    public static native void setTextStyleLogFont(
            int lfHeight,
            int lfWidth,
            int lfEscapement,
            int lfOrientation,
            int lfWeight,
            boolean lfItalic,
            boolean lfUnderline,
            boolean lfStrikeOut,
            int lfCharSet,
            int lfOutPrecision,
            int lfClipPrecision,
            int lfQuality,
            int lfPitchAndFamily,
            String lfFaceName
    );

    public static native void setTextStyle3(
            int lfHeight,
            int lfWidth,
            String lfFaceName,
            int lfEscapement,
            int lfOrientation,
            int lfWeight,
            boolean lfItalic,
            boolean lfUnderline,
            boolean lfStrikeOut,
            int lfCharSet,
            int lfOutPrecision,
            int lfClipPrecision,
            int lfQuality,
            int lfPitchAndFamily
    );

    public static native void setTextStyle2(
            int lfHeight,
            int lfWidth,
            String lfFaceName,
            int lfEscapement,
            int lfOrientation,
            int lfWeight,
            boolean lfItalic,
            boolean lfUnderline,
            boolean lfStrikeOut
    );

    public static native void setTextStyle(
            int lfHeight,
            int lfWidth,
            String lfFaceName
    );

    public static native String getTxtStyle();

    public static native void bar(int left, int top, int right, int bottom);

    public static native void bar3d(int left, int top, int right, int bottom, int depth, int topFlag);

    public static native void drawPoly(int[] points);

    public static native void fillPoly(int[] points);

    public static native void setWriteMode(int mode);

    public static native String getFillStyle();

    public static native void setFillStyle(
            int style,
            int hatch,
            long pImage);

    public static native void setFillStylePattern8x8(byte[] pattern8x8);

    public static native String getLineStyle();

    public static native void setLineStyle(int style, int thickness, int[] userType);

    public static native void arc(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian);

    public static native void ellipse(int left, int top, int right, int bottom);

    public static native void fillEllipse(int left, int top, int right, int bottom);

    public static native void clearEllipse(int left, int top, int right, int bottom);

    public static native void solidEllipse(int left, int top, int right, int bottom);

    public static native void pie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian);

    public static native void fillPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian);

    public static native void clearPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian);

    public static native void solidPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian);

    public static native void polygon(int[] points);

    public static native void fillPolygon(int[] points);

    public static native void clearPolygon(int[] points);

    public static native void solidPolygon(int[] points);

    public static native void roundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight);

    public static native void fillRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight);

    public static native void clearRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight);

    public static native void solidRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight);

    public static native void floodFill(
            int x,
            int y,
            int color,
            int fillType);

    public static native int getPixel(int x, int y);

    public static native void putPixel(int x, int y, int color);

    public static native int getX();

    public static native int getY();

    public static native void line(int x1, int y1, int x2, int y2);

    public static native void lineRel(int dx, int dy);

    public static native void lineTo(int x, int y);

    public static native void moveRel(int dx, int dy);

    public static native void moveTo(int x, int y);

    public static native void polyBezier(int[] points);

    public static native void polyLine(int[] points);
}
