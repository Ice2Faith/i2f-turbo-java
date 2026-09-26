package i2f.turbo.idea.plugin.funvi.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import i2f.turbo.idea.plugin.funvi.FunviConsts;
import i2f.turbo.idea.plugin.funvi.FunviFileType;
import i2f.turbo.idea.plugin.funvi.FunviLanguage;
import org.jetbrains.annotations.NotNull;

public class FunviFile extends PsiFileBase {
    protected FunviFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FunviLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FunviFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return FunviConsts.FILE_DESCRIPTION;
    }
}
