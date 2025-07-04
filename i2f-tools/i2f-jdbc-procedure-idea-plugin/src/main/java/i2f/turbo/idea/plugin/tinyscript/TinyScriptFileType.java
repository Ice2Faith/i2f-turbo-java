package i2f.turbo.idea.plugin.tinyscript;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TinyScriptFileType extends LanguageFileType {

    public static final TinyScriptFileType INSTANCE = new TinyScriptFileType();

    protected TinyScriptFileType() {
        super(TinyScriptLanguage.INSTANCE);
    }

    @NonNls
    @NotNull
    @Override
    public String getName() {
        return TinyScriptConsts.LANGUAGE_ID;
    }

    @NotNull
    @Override
    public String getDescription() {
        return TinyScriptConsts.FILE_DESCRIPTION;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return TinyScriptConsts.FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return TinyScriptConsts.ICON;
    }
}
