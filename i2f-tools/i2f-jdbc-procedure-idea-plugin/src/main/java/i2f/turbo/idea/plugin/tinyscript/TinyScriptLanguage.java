package i2f.turbo.idea.plugin.tinyscript;

import com.intellij.lang.Language;

public class TinyScriptLanguage extends Language {
    public static final TinyScriptLanguage INSTANCE = new TinyScriptLanguage();
    protected TinyScriptLanguage() {
        super(TinyScriptConsts.LANGUAGE_ID);
    }

}
