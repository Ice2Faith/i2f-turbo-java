package i2f.turbo.idea.plugin.funvi;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FunviFileType extends LanguageFileType {

    public static final FunviFileType INSTANCE = new FunviFileType();

    protected FunviFileType() {
        super(FunviLanguage.INSTANCE);
    }

    @NonNls
    @NotNull
    @Override
    public String getName() {
        return FunviConsts.LANGUAGE_ID;
    }

    @NotNull
    @Override
    public String getDescription() {
        return FunviConsts.FILE_DESCRIPTION;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return FunviConsts.FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return FunviConsts.ICON;
    }
}
