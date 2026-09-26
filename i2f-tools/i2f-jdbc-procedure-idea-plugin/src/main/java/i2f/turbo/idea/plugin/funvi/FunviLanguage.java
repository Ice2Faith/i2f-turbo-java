package i2f.turbo.idea.plugin.funvi;

import com.intellij.lang.Language;

public class FunviLanguage extends Language {
    public static final FunviLanguage INSTANCE = new FunviLanguage();

    protected FunviLanguage() {
        super(FunviConsts.LANGUAGE_ID);
    }

}
