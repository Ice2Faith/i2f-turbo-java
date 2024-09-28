package i2f.i18n.test;

import i2f.i18n.I18n;

/**
 * @author Ice2Faith
 * @date 2024/9/28 16:44
 */
public class TestI18n {
    public static void main(String[] args) {
//        I18n.setThreadLang("zh-CN");
        System.out.println(I18n.DEFAULT_LANG);
        String version = I18n.get("app.version");
        System.out.println(version);
        String banner = I18n.get("app.banner");
        System.out.println("###" + banner + "###");
    }
}
