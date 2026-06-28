package i2f.turbo.idea.plugin.funic;

import com.intellij.lang.Language;

public class FunicLanguage extends Language {
    public static final FunicLanguage INSTANCE = new FunicLanguage();

    protected FunicLanguage() {
        super(FunicConsts.LANGUAGE_ID);
    }

}
