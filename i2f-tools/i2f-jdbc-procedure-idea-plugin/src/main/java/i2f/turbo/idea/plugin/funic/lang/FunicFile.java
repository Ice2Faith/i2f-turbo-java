package i2f.turbo.idea.plugin.funic.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import i2f.turbo.idea.plugin.funic.FunicConsts;
import i2f.turbo.idea.plugin.funic.FunicFileType;
import i2f.turbo.idea.plugin.funic.FunicLanguage;
import org.jetbrains.annotations.NotNull;

public class FunicFile extends PsiFileBase {
    protected FunicFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FunicLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FunicFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return FunicConsts.FILE_DESCRIPTION;
    }
}
