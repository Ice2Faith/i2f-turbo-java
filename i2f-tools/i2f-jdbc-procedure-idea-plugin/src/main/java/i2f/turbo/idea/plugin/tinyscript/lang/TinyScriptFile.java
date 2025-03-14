package i2f.turbo.idea.plugin.tinyscript.lang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptFileType;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import org.jetbrains.annotations.NotNull;

public class TinyScriptFile extends PsiFileBase {
    protected TinyScriptFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TinyScriptLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public  FileType getFileType() {
        return TinyScriptFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return TinyScriptConsts.FILE_DESCRIPTION;
    }
}
