package i2f.turbo.idea.plugin.ognl;

import com.intellij.lang.Language;

public class OgnlLanguage extends Language {
    public static final OgnlLanguage INSTANCE = new OgnlLanguage();

    protected OgnlLanguage() {
        super(OgnlConsts.LANGUAGE_ID);
    }

}
