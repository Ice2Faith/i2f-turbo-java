package i2f.turbo.idea.plugin.tinyscript.lang.debugger;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class TinyScriptLineBreakpointType extends XLineBreakpointType<TinyScriptBreakpointProperties> {

    @NonNls
    public static final String ID = "tinyscript-line-breakpoint";

    public TinyScriptLineBreakpointType() {
        super(ID, TinyScriptConsts.LANGUAGE_ID + " Line Breakpoints");
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        return TinyScriptFileType.INSTANCE.equals(file.getFileType());
    }

    @Nullable
    @Override
    public TinyScriptBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new TinyScriptBreakpointProperties();
    }

    @Override
    public String getDisplayText(@NotNull XLineBreakpoint<TinyScriptBreakpointProperties> breakpoint) {
        String url = breakpoint.getFileUrl();
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return TinyScriptConsts.LANGUAGE_ID + " breakpoint at " + fileName + ":" + (breakpoint.getLine() + 1);
    }
}
