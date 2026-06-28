package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.turbo.idea.plugin.jdbc.procedure.XProc4jConsts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class JdbcProcedureLineBreakpointType extends XLineBreakpointType<JdbcProcedureBreakpointProperties> {

    @NonNls
    public static final String ID = "xproc4j-line-breakpoint";

    public JdbcProcedureLineBreakpointType() {
        super(ID, XProc4jConsts.NAME + " Line Breakpoints");
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        if (!XmlFileType.INSTANCE.equals(file.getFileType())) {
            return false;
        }
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) {
            return false;
        }
        XmlFile xmlFile = (XmlFile) psiFile;
        XmlTag rootTag = xmlFile.getRootTag();
        if (rootTag == null) {
            return false;
        }
        String name = rootTag.getName();
        if (TagConsts.PROCEDURE.equals(name)) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public JdbcProcedureBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new JdbcProcedureBreakpointProperties();
    }

    @Override
    public String getDisplayText(@NotNull XLineBreakpoint<JdbcProcedureBreakpointProperties> breakpoint) {
        String url = breakpoint.getFileUrl();
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return XProc4jConsts.NAME + " breakpoint at " + fileName + ":" + (breakpoint.getLine() + 1);
    }
}
