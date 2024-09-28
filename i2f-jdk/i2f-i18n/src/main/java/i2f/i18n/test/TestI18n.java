package i2f.i18n.test;

import i2f.i18n.I18n;
import i2f.i18n.provider.impl.DefaultI18nProvider;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Ice2Faith
 * @date 2024/9/28 16:44
 */
public class TestI18n {
    public static void main(String[] args) {
        if(I18n.DEFAULT_PROVIDER instanceof DefaultI18nProvider){
            DefaultI18nProvider defaultProvider = (DefaultI18nProvider) I18n.DEFAULT_PROVIDER;
            defaultProvider.getFileNames()
                    .addAll(Arrays.asList(
                            "file:i2f-jdk/i2f-i18n/src/main/java/i2f/i18n/i18n.xml",
                            "file:i2f-jdk/i2f-i18n/src/main/java/i2f/i18n/i18n-default.xml",
                            "file:i2f-jdk/i2f-i18n/src/main/java/i2f/i18n/i18n-zh-CN.properties",
                            "file:i2f-jdk/i2f-i18n/src/main/java/i2f/i18n/i18n-zh-CN.xml"
                    ));
            defaultProvider.loadLangMap(I18n.DEFAULT_LANG);
            defaultProvider.loadLangMap("zh-CN");
        }
        System.out.println(Locale.getDefault().getLanguage());
        String version = I18n.get("app.version");
        System.out.println(version);
        I18n.setThreadLang("zh");
        String banner = I18n.get("app.banner");
        System.out.println("###" + banner + "###");
    }
}
