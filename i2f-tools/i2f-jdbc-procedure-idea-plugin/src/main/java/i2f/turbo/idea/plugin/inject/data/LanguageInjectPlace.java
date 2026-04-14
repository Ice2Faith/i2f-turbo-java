package i2f.turbo.idea.plugin.inject.data;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:12
 * @desc
 */
public class LanguageInjectPlace {
    protected String language;
    protected String prefixTemplate;
    protected String suffixTemplate;

    public String getLanguage() {
        return language;
    }

    public LanguageInjectPlace language(String language) {
        this.language = language;
        return this;
    }

    public String getPrefixTemplate() {
        return prefixTemplate;
    }

    public LanguageInjectPlace prefixTemplate(String prefixTemplate) {
        this.prefixTemplate = prefixTemplate;
        return this;
    }

    public String getSuffixTemplate() {
        return suffixTemplate;
    }

    public LanguageInjectPlace suffixTemplate(String suffixTemplate) {
        this.suffixTemplate = suffixTemplate;
        return this;
    }
}
