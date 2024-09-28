package i2f.i18n;

import i2f.i18n.provider.I18nProvider;
import i2f.i18n.provider.impl.DefaultI18nProvider;
import i2f.i18n.provider.impl.MergedI18nProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * i18n工具
 * 根据getSmartI18nProvider决定实际使用的provider
 * 优先级为：系统属性指定的SPI中的provider>所有的SPI中的provider>默认的provider
 * 所以，在没有SPI配置的情况下
 * 使用的就是默认的provider
 * 所以
 * get获取值的查找顺序为：传入参数.lang>I18n.getThreadLang()>I18n.DEFAULT_LANG
 * 也就是：传入lang>线程lang>全局默认lang
 * 所以，默认情况下
 * 传入lang>线程lang>default
 * 所以，你可以不传入lang
 * 同时，你可以使用I18n.setThreadLang()来设置线程lang
 * 如果你使用在web项目中，则可以摘过滤器获取客户端的lang来设置线程lang以实现多语言支持
 *
 * @author Ice2Faith
 * @date 2024/9/28 14:18
 */
public class I18n {
    public static final String DEFAULT_SPI_PROVIDER_SYSTEM_PROPERTY = "i18n.default.provider";
    public static final String DEFAULT_LANG = "default";
    public static final I18nProvider DEFAULT_PROVIDER;
    private static final InheritableThreadLocal<String> THREAD_LANG = new InheritableThreadLocal<>();

    static {
        I18nProvider smartProvider = getSmartI18nProvider();
        DEFAULT_PROVIDER = smartProvider;
    }

    public static I18nProvider getSmartI18nProvider() {
        String providerName = System.getProperty(DEFAULT_SPI_PROVIDER_SYSTEM_PROPERTY);
        I18nProvider smartProvider = null;
        ServiceLoader<I18nProvider> providers = ServiceLoader.load(I18nProvider.class);
        for (I18nProvider provider : providers) {
            if (provider == null) {
                continue;
            }
            String name = provider.getClass().getName();
            if (name.equals(providerName)) {
                smartProvider = provider;
                break;
            }
        }
        if (smartProvider == null) {
            List<I18nProvider> list = new ArrayList<>();
            for (I18nProvider provider : providers) {
                if (provider == null) {
                    continue;
                }
                list.add(provider);
            }
            if (!list.isEmpty()) {
                DefaultI18nProvider provider = getDefaultI18nProvider();
                list.add(provider);
                smartProvider = new MergedI18nProvider(list);
            }
        }
        if (smartProvider == null) {
            DefaultI18nProvider provider = getDefaultI18nProvider();
            smartProvider = provider;
        }
        return smartProvider;
    }

    public static DefaultI18nProvider getDefaultI18nProvider() {
        DefaultI18nProvider provider = new DefaultI18nProvider();
        provider.loadLangMap(DEFAULT_LANG);
        provider.loadLangMap(Locale.ENGLISH.getLanguage());
        provider.loadLangMap(Locale.getDefault().getLanguage());
        return provider;
    }

    public static I18nProvider getProvider() {
        return DEFAULT_PROVIDER;
    }

    public static InheritableThreadLocal<String> getThreadLocalLang() {
        return THREAD_LANG;
    }

    public static void removeThreadLang() {
        THREAD_LANG.remove();
    }

    public static String getThreadLang() {
        return THREAD_LANG.get();
    }

    public static void setThreadLang(String lang) {
        THREAD_LANG.set(lang);
    }

    public static String get(String name) {
        return DEFAULT_PROVIDER.get(getThreadLang(), name);
    }

    public static String get(String lang, String name) {
        return DEFAULT_PROVIDER.get(lang, name);
    }

    public static String getOrDefault(String name, String defaultValue) {
        return DEFAULT_PROVIDER.getOrDefault(getThreadLang(), name, defaultValue);
    }

    public static String getOrDefault(String lang, String name, String defaultValue) {
        return DEFAULT_PROVIDER.getOrDefault(lang, name, defaultValue);
    }

    public static String format(String name, Object... args) {
        return DEFAULT_PROVIDER.format(getThreadLang(), name, args);
    }

    public static String format(String lang, String name, Object... args) {
        return DEFAULT_PROVIDER.format(lang, name, args);
    }

    public static <T> T getAs(String name, Function<String, T> mapper, T defaultValue) {
        return DEFAULT_PROVIDER.getAs(getThreadLang(), name, mapper, defaultValue);
    }

    public static <T> T getAs(String lang, String name, Function<String, T> mapper, T defaultValue) {
        return DEFAULT_PROVIDER.getAs(lang, name, mapper, defaultValue);
    }

    public static int getInt(String name, int defaultValue) {
        return DEFAULT_PROVIDER.getInt(getThreadLang(), name, defaultValue);
    }

    public static int getInt(String lang, String name, int defaultValue) {
        return DEFAULT_PROVIDER.getInt(lang, name, defaultValue);
    }

    public static long getLong(String name, long defaultValue) {
        return DEFAULT_PROVIDER.getLong(getThreadLang(), name, defaultValue);
    }

    public static long getLong(String lang, String name, long defaultValue) {
        return DEFAULT_PROVIDER.getLong(lang, name, defaultValue);
    }

    public static double getDouble(String name, double defaultValue) {
        return DEFAULT_PROVIDER.getDouble(getThreadLang(), name, defaultValue);
    }

    public static double getDouble(String lang, String name, double defaultValue) {
        return DEFAULT_PROVIDER.getDouble(lang, name, defaultValue);
    }

    public static float getFloat(String name, float defaultValue) {
        return DEFAULT_PROVIDER.getFloat(getThreadLang(), name, defaultValue);
    }

    public static float getFloat(String lang, String name, float defaultValue) {
        return DEFAULT_PROVIDER.getFloat(lang, name, defaultValue);
    }

    public static boolean getBoolean(String name, boolean defaultValue) {
        return DEFAULT_PROVIDER.getBoolean(getThreadLang(), name, defaultValue);
    }

    public static boolean getBoolean(String lang, String name, boolean defaultValue) {
        return DEFAULT_PROVIDER.getBoolean(lang, name, defaultValue);
    }
}
