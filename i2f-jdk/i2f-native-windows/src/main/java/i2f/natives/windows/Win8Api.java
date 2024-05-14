package i2f.natives.windows;

import i2f.graphics.d2.Point;
import i2f.natives.windows.types.window.HMonitor;


/**
 * @author Ice2Faith
 * @date 2024/5/10 8:50
 * @desc
 */
public class Win8Api {
    public static String hello() {
        return NativesWindows.hello();
    }

    public static Point getDpiForMonitor(HMonitor hMonitor, int dpiType) {
        int[] ret = NativesWindows8.getDpiForMonitor(hMonitor.value(), dpiType);
        if (ret.length == 0) {
            return null;
        }
        return new Point(ret[0], ret[1]);
    }

    public static int getScaleFactorForMonitor(HMonitor hMonitor) {
        return NativesWindows8.getScaleFactorForMonitor(hMonitor.value());
    }

}
