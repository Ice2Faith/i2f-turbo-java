package i2f.robot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/5/7 9:31
 * @desc
 */
public class RobotUtil {
    public static GraphicsDevice defaultGraphicsDevice() {
        return GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
    }

    public static GraphicsDevice[] getGraphicsDevices() {
        return GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getScreenDevices();
    }

    private static Robot DEFAULT_ROBOT;

    public static synchronized Robot defaultRobot() {
        if (DEFAULT_ROBOT == null) {
            try {
                DEFAULT_ROBOT = new Robot(defaultGraphicsDevice());
            } catch (Exception e) {
                throw new UnsupportedOperationException(e.getMessage(), e);
            }
        }
        return DEFAULT_ROBOT;
    }

    public static Color getPixelColor(int x, int y) {
        return defaultRobot().getPixelColor(x, y);
    }

    public static Rectangle getBounds() {
        return defaultGraphicsDevice().getDefaultConfiguration().getBounds();
    }

    public static BufferedImage screenCapture() {
        return defaultRobot().createScreenCapture(getBounds());
    }

    public static void screenSave(File file) throws IOException {
        BufferedImage img = screenCapture();
        String type = "png";
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            String suffix = name.substring(idx + 1).toLowerCase();
            if (Arrays.asList("jpg", "jpeg", "png", "bmp").contains(suffix)) {
                type = suffix;
            }
        }
        ImageIO.write(img, type, file);
    }

    public static void keyClickWindows() {
        keyClick(KeyEvent.VK_WINDOWS);
    }

    public static void keyClick(int key) {
        keyClick(defaultRobot(), key);
    }

    public static void keyClick(Robot robot, int key) {
        robot.keyPress(key);
        robot.delay(60);
        robot.keyRelease(key);
    }

    public static void mouseClickLeft() {
        mouseClick(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void mouseClickRight() {
        mouseClick(InputEvent.BUTTON3_DOWN_MASK);
    }

    public static void mouseClickMiddle() {
        mouseClick(InputEvent.BUTTON2_DOWN_MASK);
    }

    public static void mouseClick(int button) {
        mouseClick(defaultRobot(), button);
    }

    public static void mouseClick(Robot robot, int button) {
        robot.mousePress(button);
        robot.delay(60);
        robot.mouseRelease(button);
    }

    public static void mouseDrag(int beginX, int beginY, int endX, int endY) {
        mouseDrag(defaultRobot(), beginX, beginY, endX, endY);
    }

    public static void mouseDrag(Robot robot, int beginX, int beginY, int endX, int endY) {
        robot.mouseMove(beginX, beginY);
        robot.delay(60);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(60);
        int len = (int) Math.sqrt(Math.pow(endX - beginX, 2.0) + Math.pow(endY - beginY, 2.0));
        for (int i = 0; i < len; i++) {
            double rate = i * 1.0 / len;
            double dx = beginX + (endX - beginX) * rate;
            double dy = beginY + (endY - beginY) * rate;
            robot.delay(10);
            robot.mouseMove((int) dx, (int) dy);
        }
        robot.delay(60);
        robot.mouseMove(endX, endY);
        robot.delay(60);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void keyPressCtrlC() {
        keyPressCombine(KeyEvent.VK_CONTROL, KeyEvent.VK_C);
    }

    public static void keyPressCtrlV() {
        keyPressCombine(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
    }

    public static void keyPressCtrlX() {
        keyPressCombine(KeyEvent.VK_CONTROL, KeyEvent.VK_X);
    }

    public static void keyPressCtrlShiftEsc() {
        keyPressCombine(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ESCAPE);
    }

    public static void keyPressCtrlAltDelete() {
        keyPressCombine(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_DELETE);
    }

    public static void keyPressCombine(int... keys) {
        keyPressCombine(defaultRobot(), keys);
    }

    public static void keyPressCombine(Robot robot, int... keys) {
        for (int i = 0; i < keys.length; i++) {
            robot.keyPress(keys[i]);
            robot.delay(60);
        }

        for (int i = keys.length - 1; i >= 0; i--) {
            robot.delay(60);
            robot.keyRelease(keys[i]);
        }
    }


}
