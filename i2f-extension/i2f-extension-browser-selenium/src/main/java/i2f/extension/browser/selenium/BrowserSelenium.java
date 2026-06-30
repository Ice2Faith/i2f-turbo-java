package i2f.extension.browser.selenium;

import i2f.std.consts.StdConst;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/6/5 8:41
 */
public class BrowserSelenium {

    // https://www.runoob.com/selenium/selenium-install.html
    // https://www.selenium.dev/documentation/webdriver/getting_started/install_library/
    // chromedriver 下载地址，其中 145.0.7632.117 替换为自己的chrome的版本即可下载
    // https://googlechromelabs.github.io/chrome-for-testing/
    // https://storage.googleapis.com/chrome-for-testing-public/145.0.7632.117/win64/chromedriver-win64.zip
    // msedgedriver 下载地址，其中 151.0.4119.0 替换为自己的chrome的版本即可下载
    // https://msedgedriver.microsoft.com/catalog/index.html?form=MA13LH
    // https://msedgedriver.microsoft.com/151.0.4119.0/edgedriver_win64.zip
    public static enum WebDriverType {
        EDGE,
        CHROME
    }

    public static final String EDGE_DRIVER_NAME = "msedgedriver";
    public static final String CHROME_DRIVER_NAME = "chromedriver";
    public static final String DEFAULT_DRIVERS_PATH = "./" + StdConst.RUNTIME_PERSIST_DIR + "/webdriver";
    private static final AtomicBoolean hasReleased = new AtomicBoolean(false);

    public static void releaseDrivers() {
        if (hasReleased.getAndSet(true)) {
            return;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String[] driverNames = {EDGE_DRIVER_NAME, CHROME_DRIVER_NAME};
        String[] driverSuffixes = {"", ".exe"};
        byte[] buff = new byte[4096];
        int len = 0;
        File driverDir = new File(DEFAULT_DRIVERS_PATH);
        if (!driverDir.exists()) {
            driverDir.mkdirs();
        }
        for (String driverName : driverNames) {
            for (String driverSuffix : driverSuffixes) {
                String resourcePath = "assets/webdriver/" + driverName + driverSuffix;
                File saveFile = new File(driverDir, driverName + driverSuffix);
                if (saveFile.exists()) {
                    continue;
                }
                try (InputStream is = loader.getResourceAsStream(resourcePath)) {
                    if (is != null) {
                        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                            while ((len = is.read(buff)) > 0) {
                                fos.write(buff, 0, len);
                            }
                            fos.flush();
                        }
                    }
                } catch (Exception e) {

                }
            }

        }
    }

    public static WebDriver getWebDriver(WebDriverType type, boolean withUi, String driverPath) {
        if (driverPath == null) {
            driverPath = DEFAULT_DRIVERS_PATH;
        }
        File driverDir = new File(driverPath);
        if (!DEFAULT_DRIVERS_PATH.equals(driverPath)) {
            if (!driverDir.exists()) {
                driverDir = new File(DEFAULT_DRIVERS_PATH);
            }
        }
        if (!driverDir.exists()) {
            driverDir.mkdirs();
        }
        releaseDrivers();
        Set<WebDriverType> existsDriverTypes = new LinkedHashSet<>();
        if (driverDir.isDirectory()) {
            for (File item : driverDir.listFiles()) {
                String name = item.getName();
                String shortName = name;
                String suffix = "";
                int idx = name.lastIndexOf(".");
                if (idx >= 0) {
                    suffix = name.substring(idx);
                    shortName = name.substring(0, idx);
                }
                if (EDGE_DRIVER_NAME.equalsIgnoreCase(shortName)) {
                    //   System.setProperty("webdriver.edge.driver", "G:\\edgedriver_win64\\msedgedriver.exe");
                    System.setProperty("webdriver.edge.driver", item.getAbsolutePath());
                    existsDriverTypes.add(WebDriverType.EDGE);
                }
                if (CHROME_DRIVER_NAME.equalsIgnoreCase(shortName)) {
                    //System.setProperty("webdriver.chrome.driver", "G:\\chromedriver-win64\\chromedriver.exe");
                    System.setProperty("webdriver.chrome.driver", item.getAbsolutePath());
                    existsDriverTypes.add(WebDriverType.CHROME);
                }
            }
        }

        if (type == null) {
            for (WebDriverType item : existsDriverTypes) {
                type = item;
                break;
            }
        }

        if (type == WebDriverType.EDGE) {
            // 配置无头模式
            EdgeOptions options = new EdgeOptions();
            if (!withUi) {
                options.addArguments("--headless"); // 无头模式
            }
//        options.addArguments("--window-size=1920,1080"); // 设置窗口大小（可选）
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu"); // 禁用 GPU 加速（解决某些系统报错）
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new ArrayList<>(Arrays.asList("enable-automation")));
            options.setExperimentalOption("useAutomationExtension", false);

            // 初始化 WebDriver
            WebDriver driver = new EdgeDriver(options);
            return driver;
        } else {
            // 配置无头模式
            ChromeOptions options = new ChromeOptions();
            if (!withUi) {
                options.addArguments("--headless"); // 无头模式
            }
//        options.addArguments("--window-size=1920,1080"); // 设置窗口大小（可选）
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu"); // 禁用 GPU 加速（解决某些系统报错）
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new ArrayList<>(Arrays.asList("enable-automation")));
            options.setExperimentalOption("useAutomationExtension", false);

            // 初始化 WebDriver
            WebDriver driver = new ChromeDriver(options);
            return driver;
        }

    }


}
