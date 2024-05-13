package i2f.natives.windows.easyx;

import i2f.convert.Converters;
import i2f.graphics.d2.shape.Rectangle;
import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.WinBitBltRop;
import i2f.natives.windows.consts.WinGdiColor;
import i2f.natives.windows.easyx.consts.EasyXInitGraphFlag;
import i2f.natives.windows.easyx.types.ImageBufferPtr;
import i2f.natives.windows.easyx.types.ImagePtr;
import i2f.natives.windows.easyx.types.MouseMsg;
import i2f.natives.windows.types.Hdc;
import i2f.natives.windows.types.Hwnd;
import i2f.natives.windows.types.LogFont;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/12 21:39
 * @desc
 */
public class EasyXApi {
    public static String hello() {
        return NativesEasyX.hello();
    }

    public static Hwnd initGraph(int width, int height, int flag) {
        long ret = NativesEasyX.initGraph(width, height, flag);
        return new Hwnd(ret);
    }

    public static Hwnd initGraph(int width, int height) {
        long ret = NativesEasyX.initGraph(width, height, EasyXInitGraphFlag.DEFAULT);
        return new Hwnd(ret);
    }

    public static void closeGraph() {
        NativesEasyX.closeGraph();
    }


    public static void clearDevice() {
        NativesEasyX.clearDevice();
    }

    public static void beginBatchDraw() {
        NativesEasyX.beginBatchDraw();
    }

    public static void endBatchDraw() {
        NativesEasyX.endBatchDraw();
    }

    public static void flushBatchDraw() {
        NativesEasyX.flushBatchDraw();
    }

    public static void setWorkingImage(ImagePtr pImage) {
        NativesEasyX.setWorkingImage(pImage.value());
    }

    public static ImagePtr loadImage(
            String filePath,
            int width,
            int height,
            boolean resize) {
        long ret = NativesEasyX.loadImage(filePath, width, height, resize);
        return new ImagePtr(ret);
    }

    public static ImagePtr loadImage(
            String filePath) {
        long ret = NativesEasyX.loadImage(filePath, 0, 0, false);
        return new ImagePtr(ret);
    }

    public static void freeImage(ImagePtr pImage) {
        NativesEasyX.freeImage(pImage.value());
    }

    public static ImagePtr createImage(int width, int height) {
        long ret = NativesEasyX.createImage(width, height);
        return new ImagePtr(ret);
    }

    public static void saveImage(ImagePtr pImage, String filePath) {
        NativesEasyX.saveImage(pImage.value(), filePath);
    }

    public static void saveImage(String filePath) {
        NativesEasyX.saveImage(0, filePath);
    }

    public static void putImage(
            int dstX,
            int dstY,
            ImagePtr pImage,
            long dwRop) {
        NativesEasyX.putImage(dstX, dstY, pImage.value(), dwRop);
    }

    public static void putImage(
            int dstX,
            int dstY,
            ImagePtr pImage) {
        NativesEasyX.putImage(dstX, dstY, pImage.value(), WinBitBltRop.SRCCOPY);
    }

    public static void setBkMode(int mode) {
        NativesEasyX.setBkMode(mode); // WinSetBkModeMode
    }

    public static void setBkColor(int color) {
        NativesEasyX.setBkColor(color);
    }

    public static void setColor(int color) {
        NativesEasyX.setColor(color);
    }

    public static void setFillColor(int color) {
        NativesEasyX.setFillColor(color);
    }

    public static void setLineColor(int color) {
        NativesEasyX.setLineColor(color);
    }

    public static void circle(int x, int y, int radius) {
        NativesEasyX.circle(x, y, radius);
    }

    public static void fillCircle(int x, int y, int radius) {
        NativesEasyX.fillCircle(x, y, radius);
    }

    public static void clearCircle(int x, int y, int radius) {
        NativesEasyX.clearCircle(x, y, radius);
    }

    public static void solidCircle(int x, int y, int radius) {
        NativesEasyX.solidCircle(x, y, radius);
    }

    public static void rectangle(int left, int top, int right, int bottom) {
        NativesEasyX.rectangle(left, top, right, bottom);
    }

    public static void fillRectangle(int left, int top, int right, int bottom) {
        NativesEasyX.fillRectangle(left, top, right, bottom);
    }

    public static void clearRectangle(int left, int top, int right, int bottom) {
        NativesEasyX.clearRectangle(left, top, right, bottom);
    }

    public static void solidRectangle(int left, int top, int right, int bottom) {
        NativesEasyX.solidRectangle(left, top, right, bottom);
    }


    public static void setTextColor(int color) {
        NativesEasyX.setTextColor(color);
    }

    public static void outText(String str) {
        NativesEasyX.outText(str);
    }

    public static void outTextXy(int x, int y, String str) {
        NativesEasyX.outTextXy(x, y, str);
    }

    public static int hslToRgb(float h, float s, float l) {
        return NativesEasyX.hslToRgb(h, s, l);
    }

    public static int hsvToRgb(float h, float s, float v) {
        return NativesEasyX.hsvToRgb(h, s, v);
    }

    public static int bgr(int color) {
        return NativesEasyX.bgr(color);
    }

    public static int getRValue(int color) {
        return NativesEasyX.getRValue(color);
    }

    public static int getGValue(int color) {
        return NativesEasyX.getGValue(color);
    }

    public static int getBValue(int color) {
        return NativesEasyX.getBValue(color);
    }

    public static int rgbToGray(int color) {
        return NativesEasyX.rgbToGray(color);
    }

    public static float[] rgbToHsl(int color) {
        return NativesEasyX.rgbToHsl(color);
    }

    public static float[] rgbToHsv(int color) {
        return NativesEasyX.rgbToHsv(color);
    }

    public static int getBkColor() {
        return NativesEasyX.getBkColor();
    }

    public static int getBkMode() {
        return NativesEasyX.getBkMode();
    }

    public static int getFillColor() {
        return NativesEasyX.getFillColor();
    }

    public static int getLineColor() {
        return NativesEasyX.getLineColor();
    }

    public static int getPolyFillMode() {
        return NativesEasyX.getPolyFillMode();
    }

    public static void setPolyFillMode(int mode) {
        NativesEasyX.setPolyFillMode(mode);
    }

    public static int getRop2() {
        return NativesEasyX.getRop2();
    }

    public static void setRop2(int mode) {
        NativesEasyX.setRop2(mode);
    }

    public static void clearClipRgn() {
        NativesEasyX.clearClipRgn();
    }

    public static float[] getAspectRatio() {
        return NativesEasyX.getAspectRatio();
    }

    public static void setAspectRatio(float xasp, float yasp) {
        NativesEasyX.setAspectRatio(xasp, yasp);
    }

    public static void graphDefaults() {
        NativesEasyX.graphDefaults();
    }

    public static void setOrigin(int x, int y) {
        NativesEasyX.setOrigin(x, y);
    }

    public static int drawText(String text, int left, int top, int right, int bottom, int uFormat) {
        return NativesEasyX.drawText(text, left, top, right, bottom, uFormat);
    }

    public static int drawText(String text, Rectangle rect, int uFormat) {
        return NativesEasyX.drawText(text, (int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), uFormat);
    }

    public static int getTextColor() {
        return NativesEasyX.getTextColor();
    }

    public static int textHeight(String text) {
        return NativesEasyX.textHeight(text);
    }

    public static int textWidth(String text) {
        return NativesEasyX.textWidth(text);
    }

    public static ImageBufferPtr getImageBuffer(ImagePtr pImage) {
        long ret = NativesEasyX.getImageBuffer(pImage.value());
        return new ImageBufferPtr(ret);
    }

    public static void setImageBufferValue(ImageBufferPtr pBuffer, int index, int value) {
        NativesEasyX.setImageBufferValue(pBuffer.value(), index, value);
    }

    public static int getImageBufferValue(ImageBufferPtr pBuffer, int index) {
        return NativesEasyX.getImageBufferValue(pBuffer.value(), index);
    }

    public static Hdc getImageHDC(ImagePtr pImage) {
        long ret = NativesEasyX.getImageHDC(pImage.value());
        return new Hdc(ret);
    }

    public static ImagePtr getWorkingImage() {
        long ret = NativesEasyX.getWorkingImage();
        return new ImagePtr(ret);
    }

    public static int getImageHeight(ImagePtr pImage) {
        return NativesEasyX.getImageHeight(pImage.value());
    }

    public static int getImageWidth(ImagePtr pImage) {
        return NativesEasyX.getImageWidth(pImage.value());
    }

    public static void resize(ImagePtr pImage, int width, int height) {
        NativesEasyX.resize(pImage.value(), width, height);
    }

    public static void rotateImage(
            ImagePtr pDstImage,
            ImagePtr pSrcImage,
            double radian,
            int bkColor,
            boolean autosize,
            boolean highQuality) {
        NativesEasyX.rotateImage(pDstImage.value(), pSrcImage.value(),
                radian, bkColor, autosize, highQuality);
    }

    public static void rotateImage(
            ImagePtr pDstImage,
            ImagePtr pSrcImage,
            double radian) {
        NativesEasyX.rotateImage(pDstImage.value(), pSrcImage.value(),
                radian, WinGdiColor.BLACK, false, true);
    }

    public static void flushMouseMsgBuffer() {
        NativesEasyX.flushMouseMsgBuffer();
    }

    public static boolean mouseHit() {
        return NativesEasyX.mouseHit();
    }

    public static MouseMsg parseMouseMsg(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = WinApi.getJniStringMap(str);
        MouseMsg ret = new MouseMsg();
        ret.uMsg = Converters.parseInt(map.get("uMsg"), 0);
        ret.mkCtrl = Converters.parseInt(map.get("mkCtrl"), 0) == 1;
        ret.mkShift = Converters.parseInt(map.get("mkShift"), 0) == 1;
        ret.mkLButton = Converters.parseInt(map.get("mkLButton"), 0) == 1;
        ret.mkMButton = Converters.parseInt(map.get("mkMButton"), 0) == 1;
        ret.mkRButton = Converters.parseInt(map.get("mkRButton"), 0) == 1;
        ret.x = Converters.parseInt(map.get("x"), 0);
        ret.y = Converters.parseInt(map.get("y"), 0);
        ret.wheel = Converters.parseInt(map.get("wheel"), 0);
        return ret;
    }

    public static MouseMsg getMouseMsg() {
        String str = NativesEasyX.getMouseMsg();
        return parseMouseMsg(str);
    }

    public static String getEasyXVer() {
        return NativesEasyX.getEasyXVer();
    }

    public static Hwnd getHWnd() {
        long ret = NativesEasyX.getHWnd();
        return new Hwnd(ret);
    }

    public static String inputBox(
            String prompt,
            String title,
            String defaultText,
            int width,
            int height,
            boolean onlyOk
    ) {
        return NativesEasyX.inputBox(prompt, title, defaultText, width, height, onlyOk);
    }

    public static String inputBox(
            String prompt,
            String title,
            String defaultText
    ) {
        return NativesEasyX.inputBox(prompt, title, defaultText, 0, 0, true);
    }

    public static String inputBox(
            String prompt,
            String title
    ) {
        return NativesEasyX.inputBox(prompt, title, null, 0, 0, true);
    }

    public static String inputBox(
            String prompt
    ) {
        return NativesEasyX.inputBox(prompt, null, null, 0, 0, true);
    }

    public static String inputBox() {
        return NativesEasyX.inputBox(null, null, null, 0, 0, true);
    }

    public static int getColor() {
        return NativesEasyX.getColor();
    }

    public static int getWidth() {
        return NativesEasyX.getWidth();
    }

    public static int getHeight() {
        return NativesEasyX.getHeight();
    }

    public static int getMaxX() {
        return NativesEasyX.getMaxX();
    }

    public static int getMaxY() {
        return NativesEasyX.getMaxY();
    }

    public static void setTextStyleLogFont(
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
    ) {
        NativesEasyX.setTextStyleLogFont(lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily, lfFaceName);
    }

    public static void setTextStyle(
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
    ) {
        NativesEasyX.setTextStyle3(lfHeight, lfWidth, lfFaceName, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily);
    }

    public static void setTextStyle(
            int lfHeight,
            int lfWidth,
            String lfFaceName,
            int lfEscapement,
            int lfOrientation,
            int lfWeight,
            boolean lfItalic,
            boolean lfUnderline,
            boolean lfStrikeOut
    ) {
        NativesEasyX.setTextStyle2(lfHeight, lfWidth, lfFaceName, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut);
    }

    public static void setTextStyle(
            int lfHeight,
            int lfWidth,
            String lfFaceName
    ) {
        NativesEasyX.setTextStyle(lfHeight, lfWidth, lfFaceName);
    }

    public static LogFont getTxtStyle() {
        String str = NativesEasyX.getTxtStyle();
        return WinApi.parseLogFont(str);
    }
}
