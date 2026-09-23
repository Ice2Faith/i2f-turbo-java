package i2f.turbo.idea.plugin.ognl.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import i2f.turbo.idea.plugin.ognl.OgnlConsts;
import i2f.turbo.idea.plugin.ognl.OgnlFileType;
import i2f.turbo.idea.plugin.ognl.OgnlLanguage;
import org.jetbrains.annotations.NotNull;

public class OgnlFile extends PsiFileBase {
    protected OgnlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, OgnlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return OgnlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return OgnlConsts.FILE_DESCRIPTION;
    }
}
