package i2f.extension.browser.selenium;

import i2f.io.stream.StreamUtil;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.std.consts.StdConst;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
    // https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/?form=MA13LH#downloads
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
            try {
                WebDriver driver = new EdgeDriver(options);
                return driver;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (e instanceof SessionNotCreatedException) {
                    /**
                     * try download driver
                     */
                    downloadEdgeDriver((SessionNotCreatedException) e);
                    System.out.println("retry start driver session ...");
                    WebDriver driver = new EdgeDriver(options);
                    return driver;
                }
                throw e;
            }
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
            try {
                WebDriver driver = new ChromeDriver(options);
                return driver;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (e instanceof SessionNotCreatedException) {
                    /**
                     * try download driver
                     */
                    downloadChromeDriver((SessionNotCreatedException) e);
                    System.out.println("retry start driver session ...");
                    WebDriver driver = new ChromeDriver(options);
                    return driver;
                }
                throw e;
            }
        }

    }

    public static void downloadChromeDriver(SessionNotCreatedException e) {
        try {
            String text = e.getMessage();
            List<RegexMatchItem> list = RegexUtil.regexFinds(text, "(?i)current\\s+browser\\s+version\\s+is\\s+([0-9\\.]+)\\s+");
            if (list.isEmpty()) {
                return;
            }
            RegexMatchItem item = list.get(0);
            text = item.getMatchStr();
            list = RegexUtil.regexFinds(text, "[0-9\\.]+");
            if (list.isEmpty()) {
                return;
            }
            item = list.get(0);
            String version = item.getMatchStr();
            System.out.println("chrome version: " + version);

            System.out.println("chrome driver page: https://googlechromelabs.github.io/chrome-for-testing/");
            String downloadUrl = String.format("https://storage.googleapis.com/chrome-for-testing-public/%s/win64/chromedriver-win64.zip", version);
            System.out.println("chrome driver download url: " + downloadUrl);

            URL url = new URL(downloadUrl);
            File downloadFile = new File(StdConst.RUNTIME_TMP_DIR + "/" + "chrome-driver-" + version + ".zip");
            if (!downloadFile.exists()) {
                downloadFile = new File(downloadFile.getAbsolutePath());
                if (!downloadFile.getParentFile().exists()) {
                    downloadFile.getParentFile().mkdirs();
                }
                System.out.println("chrome driver download to: " + downloadFile.getAbsolutePath());

                System.out.println("chrome driver downloading ...");
                InputStream is = url.openStream();
                OutputStream os = new FileOutputStream(downloadFile);
                StreamUtil.streamCopy(is, os, true);
                System.out.println("chrome driver download finish.");
            } else {
                System.out.println("chrome driver has download at: " + downloadFile.getAbsolutePath());
            }

            System.out.println("extract driver ...");

            try (ZipFile zipFile = new ZipFile(downloadFile)) {
                Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                while (enumeration.hasMoreElements()) {
                    ZipEntry entry = enumeration.nextElement();
                    if (entry == null || entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    if (name.endsWith("chromedriver.exe")) {
                        InputStream zis = zipFile.getInputStream(entry);
                        File driverDir = new File(DEFAULT_DRIVERS_PATH);
                        if (!driverDir.exists()) {
                            driverDir.mkdirs();
                        }
                        File saveFile = new File(driverDir, CHROME_DRIVER_NAME + ".exe");
                        StreamUtil.writeBytes(zis, saveFile);
                        System.out.println("extract driver finish, save to : " + saveFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception ex) {

        }

    }

    public static void downloadEdgeDriver(SessionNotCreatedException e) {
        try {
            String text = e.getMessage();
            List<RegexMatchItem> list = RegexUtil.regexFinds(text, "(?i)current\\s+browser\\s+version\\s+is\\s+([0-9\\.]+)\\s+");
            if (list.isEmpty()) {
                return;
            }
            RegexMatchItem item = list.get(0);
            text = item.getMatchStr();
            list = RegexUtil.regexFinds(text, "[0-9\\.]+");
            if (list.isEmpty()) {
                return;
            }
            item = list.get(0);
            String version = item.getMatchStr();
            System.out.println("edge version: " + version);

            System.out.println("edge driver page: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/?form=MA13LH#downloads");
            String downloadUrl = String.format("https://msedgedriver.microsoft.com/%s/edgedriver_win64.zip", version);
            System.out.println("edge driver download url: " + downloadUrl);

            URL url = new URL(downloadUrl);
            File downloadFile = new File(StdConst.RUNTIME_TMP_DIR + "/" + "edge-driver-" + version + ".zip");
            if (!downloadFile.exists()) {
                downloadFile = new File(downloadFile.getAbsolutePath());
                if (!downloadFile.getParentFile().exists()) {
                    downloadFile.getParentFile().mkdirs();
                }
                System.out.println("edge driver download to: " + downloadFile.getAbsolutePath());

                System.out.println("edge driver downloading ...");
                InputStream is = url.openStream();
                OutputStream os = new FileOutputStream(downloadFile);
                StreamUtil.streamCopy(is, os, true);
                System.out.println("edge driver download finish.");
            } else {
                System.out.println("edge driver has download at: " + downloadFile.getAbsolutePath());
            }

            System.out.println("extract driver ...");

            try (ZipFile zipFile = new ZipFile(downloadFile)) {
                Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                while (enumeration.hasMoreElements()) {
                    ZipEntry entry = enumeration.nextElement();
                    if (entry == null || entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    if (name.endsWith("msedgedriver.exe")) {
                        InputStream zis = zipFile.getInputStream(entry);
                        File driverDir = new File(DEFAULT_DRIVERS_PATH);
                        if (!driverDir.exists()) {
                            driverDir.mkdirs();
                        }
                        File saveFile = new File(driverDir, EDGE_DRIVER_NAME + ".exe");
                        StreamUtil.writeBytes(zis, saveFile);
                        System.out.println("extract driver finish, save to : " + saveFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception ex) {

        }

    }
}
