package i2f.i18n.parser.impl;

import i2f.i18n.data.I18nItem;
import i2f.i18n.parser.I18nParser;

import java.io.*;
import java.util.*;

/**
 * properties 中加载i18n配置
 * 支持使用\t\r\n的转义
 *
 * @author Ice2Faith
 * @date 2024/9/28 14:35
 */
public class PropertiesI18nParser implements I18nParser {
    protected String lang;
    protected Properties properties;

    public PropertiesI18nParser(String lang, Properties properties) {
        this.lang = lang;
        this.properties = properties;
    }

    public PropertiesI18nParser(String lang, InputStream is) throws IOException {
        this.lang = lang;
        this.properties = new Properties();
        try {
            properties.load(is);
        } finally {
            is.close();
        }
    }

    public PropertiesI18nParser(String lang, Reader reader) throws IOException {
        this.lang = lang;
        this.properties = new Properties();
        try {
            properties.load(reader);
        } finally {
            reader.close();
        }
    }

    public PropertiesI18nParser(String lang, String propertiesString) {
        ByteArrayInputStream bis = new ByteArrayInputStream(propertiesString.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        this.lang = lang;
        this.properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {

        } finally {
            try {
                reader.close();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public Collection<I18nItem> parse() {
        List<I18nItem> ret = new ArrayList<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement();
            if (key == null) {
                continue;
            }
            String name = String.valueOf(key);
            String value = properties.getProperty(name);
            if (name != null) {
                name = name.trim();
            }
            if (value != null) {
                value = I18nParser.unescape(value);
                value = value.trim();
            }
            ret.add(new I18nItem(lang, name, value));
        }
        return ret;
    }
}
