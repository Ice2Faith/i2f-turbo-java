package i2f.turbo.idea.plugin.funic;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FunicFileType extends LanguageFileType {

    public static final FunicFileType INSTANCE = new FunicFileType();

    protected FunicFileType() {
        super(FunicLanguage.INSTANCE);
    }

    @NonNls
    @NotNull
    @Override
    public String getName() {
        return FunicConsts.LANGUAGE_ID;
    }

    @NotNull
    @Override
    public String getDescription() {
        return FunicConsts.FILE_DESCRIPTION;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return FunicConsts.FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return FunicConsts.ICON;
    }
}
