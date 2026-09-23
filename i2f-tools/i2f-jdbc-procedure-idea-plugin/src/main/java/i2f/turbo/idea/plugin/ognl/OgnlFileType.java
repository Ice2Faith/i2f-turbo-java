package i2f.turbo.idea.plugin.ognl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class OgnlFileType extends LanguageFileType {

    public static final OgnlFileType INSTANCE = new OgnlFileType();

    protected OgnlFileType() {
        super(OgnlLanguage.INSTANCE);
    }

    @NonNls
    @NotNull
    @Override
    public String getName() {
        return OgnlConsts.LANGUAGE_ID;
    }

    @NotNull
    @Override
    public String getDescription() {
        return OgnlConsts.FILE_DESCRIPTION;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return OgnlConsts.FILE_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return OgnlConsts.ICON;
    }
}
