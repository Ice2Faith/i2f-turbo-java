package i2f.natives.windows.easyx;

import i2f.convert.Converters;
import i2f.graphics.d2.Point;
import i2f.graphics.d2.Size;
import i2f.graphics.d2.shape.Rectangle;
import i2f.natives.windows.WinApi;
import i2f.natives.windows.consts.*;
import i2f.natives.windows.easyx.consts.EasyXInitGraphFlag;
import i2f.natives.windows.easyx.types.*;
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

    public static void bar(int left, int top, int right, int bottom) {
        NativesEasyX.bar(left, top, right, bottom);
    }

    public static void bar(Rectangle rect) {
        NativesEasyX.bar((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static void bar3d(int left, int top, int right, int bottom, int depth, int topFlag) {
        NativesEasyX.bar3d(left, top, right, bottom, depth, topFlag);
    }

    public static void bar3d(Rectangle rect, int depth, int topFlag) {
        NativesEasyX.bar3d((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), depth, topFlag);
    }

    public static void drawPoly(int[] points) {
        NativesEasyX.drawPoly(points);
    }

    public static void drawPoly(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.drawPoly(arr);
    }

    public static void fillPoly(int[] points) {
        NativesEasyX.fillPoly(points);
    }

    public static void fillPoly(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.fillPoly(arr);
    }

    public static int[] points2flat(Point[] points) {
        int[] arr = new int[points.length * 2];
        for (int i = 0; i < points.length; i++) {
            arr[i * 2] = (int) points[i].x;
            arr[i * 2 + 1] = (int) points[i].y;
        }
        return arr;
    }

    public static void setWriteMode(int mode) {
        NativesEasyX.setWriteMode(mode);
    }

    public static FillStyle parseFillStyle(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = WinApi.getJniStringMap(str);
        FillStyle ret = new FillStyle();
        ret.style = Converters.parseInt(map.get("style"), 0);
        ret.hatch = Converters.parseInt(map.get("hatch"), 0);
        ret.ppattern = new ImagePtr(Converters.parseLong(map.get("ppattern"), 0));
        return ret;
    }

    public static FillStyle getFillStyle() {
        String str = NativesEasyX.getFillStyle();
        return parseFillStyle(str);
    }

    public static void setFillStyle(
            int style,
            int hatch,
            ImagePtr pImage) {
        NativesEasyX.setFillStyle(style, hatch, pImage.value());
    }

    public static void setFillStyle(
            int style,
            int hatch) {
        NativesEasyX.setFillStyle(style, hatch, 0);
    }

    public static void setFillStyle(
            int style) {
        NativesEasyX.setFillStyle(style, WinGdiHatchStyle.HS_HORIZONTAL, 0);
    }

    public static void setFillStyle() {
        NativesEasyX.setFillStyle(WinGdiBrushStyle.BS_SOLID, WinGdiHatchStyle.HS_HORIZONTAL, 0);
    }

    public static void setFillStylePattern8x8(byte[] pattern8x8) {
        NativesEasyX.setFillStylePattern8x8(pattern8x8);
    }

    public static LineStyle parseLineStyle(String str) {
        if (str == null) {
            return null;
        }
        Map<String, String> map = WinApi.getJniStringMap(str);
        LineStyle ret = new LineStyle();
        ret.style = Converters.parseInt(map.get("style"), 0);
        ret.thickness = Converters.parseInt(map.get("thickness"), 0);
        String userType = map.get("userType");
        if (userType != null) {
            String[] arr = userType.split(",");
            ret.userType = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                ret.userType[i] = Converters.parseInt(arr[i], 0);
            }
        }
        return ret;
    }

    public static LineStyle getLineStyle() {
        String str = NativesEasyX.getLineStyle();
        return parseLineStyle(str);
    }

    public static void setLineStyle(int style, int thickness, int[] userType) {
        NativesEasyX.setLineStyle(style, thickness, userType);
    }

    public static void setLineStyle(int style, int thickness) {
        NativesEasyX.setLineStyle(style, thickness, null);
    }

    public static void setLineStyle(int style) {
        NativesEasyX.setLineStyle(style, 1, null);
    }

    public static void setLineStyle() {
        NativesEasyX.setLineStyle(WinGdiPenStyle.PS_SOLID, 1, null);
    }

    public static void arc(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian) {
        NativesEasyX.arc(left, top, right, bottom, startRadian, endRadian);
    }

    public static void arc(
            Rectangle rect,
            double startRadian,
            double endRadian) {
        NativesEasyX.arc((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), startRadian, endRadian);
    }

    public static void ellipse(int left, int top, int right, int bottom) {
        NativesEasyX.ellipse(left, top, right, bottom);
    }

    public static void ellipse(Rectangle rect) {
        NativesEasyX.ellipse((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static void fillEllipse(int left, int top, int right, int bottom) {
        NativesEasyX.fillEllipse(left, top, right, bottom);
    }

    public static void fillEllipse(Rectangle rect) {
        NativesEasyX.fillEllipse((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static void clearEllipse(int left, int top, int right, int bottom) {
        NativesEasyX.clearEllipse(left, top, right, bottom);
    }

    public static void clearEllipse(Rectangle rect) {
        NativesEasyX.clearEllipse((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static void solidEllipse(int left, int top, int right, int bottom) {
        NativesEasyX.solidEllipse(left, top, right, bottom);
    }

    public static void solidEllipse(Rectangle rect) {
        NativesEasyX.solidEllipse((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom());
    }

    public static void pie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian) {
        NativesEasyX.pie(left, top, right, bottom, startRadian, endRadian);
    }

    public static void pie(
            Rectangle rect,
            double startRadian,
            double endRadian) {
        NativesEasyX.pie((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), startRadian, endRadian);
    }

    public static void fillPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian) {
        NativesEasyX.fillPie(left, top, right, bottom, startRadian, endRadian);
    }

    public static void fillPie(
            Rectangle rect,
            double startRadian,
            double endRadian) {
        NativesEasyX.fillPie((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), startRadian, endRadian);
    }

    public static void clearPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian) {
        NativesEasyX.clearPie(left, top, right, bottom, startRadian, endRadian);
    }

    public static void clearPie(
            Rectangle rect,
            double startRadian,
            double endRadian) {
        NativesEasyX.clearPie((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), startRadian, endRadian);
    }

    public static void solidPie(
            int left,
            int top,
            int right,
            int bottom,
            double startRadian,
            double endRadian) {
        NativesEasyX.solidPie(left, top, right, bottom, startRadian, endRadian);
    }

    public static void solidPie(
            Rectangle rect,
            double startRadian,
            double endRadian) {
        NativesEasyX.solidPie((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), startRadian, endRadian);
    }

    public static void polygon(int[] points) {
        NativesEasyX.polygon(points);
    }

    public static void polygon(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.polygon(arr);
    }

    public static void fillPolygon(int[] points) {
        NativesEasyX.fillPolygon(points);
    }

    public static void fillPolygon(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.fillPolygon(arr);
    }

    public static void clearPolygon(int[] points) {
        NativesEasyX.clearPolygon(points);
    }

    public static void clearPolygon(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.clearPolygon(arr);
    }

    public static void solidPolygon(int[] points) {
        NativesEasyX.solidPolygon(points);
    }

    public static void solidPolygon(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.solidPolygon(arr);
    }

    public static void roundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.roundRect(left, top, right, bottom, ellipseWidth, ellipseHeight);
    }

    public static void roundRect(
            Rectangle rect,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.roundRect((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), ellipseWidth, ellipseHeight);
    }

    public static void fillRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.fillRoundRect(left, top, right, bottom, ellipseWidth, ellipseHeight);
    }

    public static void fillRoundRect(
            Rectangle rect,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.fillRoundRect((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), ellipseWidth, ellipseHeight);
    }

    public static void clearRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.clearRoundRect(left, top, right, bottom, ellipseWidth, ellipseHeight);
    }

    public static void clearRoundRect(
            Rectangle rect,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.clearRoundRect((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), ellipseWidth, ellipseHeight);
    }

    public static void solidRoundRect(
            int left,
            int top,
            int right,
            int bottom,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.solidRoundRect(left, top, right, bottom, ellipseWidth, ellipseHeight);
    }

    public static void solidRoundRect(
            Rectangle rect,
            double ellipseWidth,
            double ellipseHeight) {
        NativesEasyX.solidRoundRect((int) rect.left(), (int) rect.top(), (int) rect.right(), (int) rect.bottom(), ellipseWidth, ellipseHeight);
    }

    public static void floodFill(
            int x,
            int y,
            int color,
            int fillType) {
        NativesEasyX.floodFill(x, y, color, fillType);
    }

    public static void floodFill(
            Point p,
            int color,
            int fillType) {
        NativesEasyX.floodFill((int) p.x, (int) p.y, color, fillType);
    }

    public static void floodFill(
            int x,
            int y,
            int color) {
        NativesEasyX.floodFill(x, y, color, WinGdiFloodFillType.FLOODFILLBORDER);
    }

    public static void floodFill(
            Point p,
            int color) {
        NativesEasyX.floodFill((int) p.x, (int) p.y, color, WinGdiFloodFillType.FLOODFILLBORDER);
    }

    public static int getPixel(int x, int y) {
        return NativesEasyX.getPixel(x, y);
    }

    public static int getPixel(Point p) {
        return NativesEasyX.getPixel((int) p.x, (int) p.y);
    }

    public static void putPixel(int x, int y, int color) {
        NativesEasyX.putPixel(x, y, color);
    }

    public static void putPixel(Point p, int color) {
        NativesEasyX.putPixel((int) p.x, (int) p.y, color);
    }

    public static int getX() {
        return NativesEasyX.getX();
    }

    public static int getY() {
        return NativesEasyX.getY();
    }

    public static Point getPoint() {
        return new Point(getX(), getY());
    }

    public static void line(int x1, int y1, int x2, int y2) {
        NativesEasyX.line(x1, y1, x2, y2);
    }

    public static void line(Point p1, Point p2) {
        NativesEasyX.line((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
    }

    public static void lineRel(int dx, int dy) {
        NativesEasyX.lineRel(dx, dy);
    }

    public static void lineRel(Size s) {
        NativesEasyX.lineRel((int) s.dx, (int) s.dy);
    }

    public static void lineTo(int x, int y) {
        NativesEasyX.lineTo(x, y);
    }

    public static void lineTo(Point p) {
        NativesEasyX.lineTo((int) p.x, (int) p.y);
    }

    public static void moveRel(int dx, int dy) {
        NativesEasyX.moveRel(dx, dy);
    }

    public static void moveRel(Size s) {
        NativesEasyX.moveRel((int) s.dx, (int) s.dy);
    }

    public static void moveTo(int x, int y) {
        NativesEasyX.moveTo(x, y);
    }

    public static void moveTo(Point p) {
        NativesEasyX.moveTo((int) p.x, (int) p.y);
    }

    public static void polyBezier(int[] points) {
        NativesEasyX.polyBezier(points);
    }

    public static void polyBezier(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.polyBezier(arr);
    }

    public static void polyLine(int[] points) {
        NativesEasyX.polyLine(points);
    }

    public static void polyLine(Point[] points) {
        int[] arr = points2flat(points);
        NativesEasyX.polyLine(arr);
    }
}
