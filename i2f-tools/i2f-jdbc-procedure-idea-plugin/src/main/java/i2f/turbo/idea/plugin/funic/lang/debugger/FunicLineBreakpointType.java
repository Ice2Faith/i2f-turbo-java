package i2f.turbo.idea.plugin.funic.lang.debugger;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import i2f.turbo.idea.plugin.funic.FunicConsts;
import i2f.turbo.idea.plugin.funic.FunicFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class FunicLineBreakpointType extends XLineBreakpointType<FunicBreakpointProperties> {

    @NonNls
    public static final String ID = "funic-line-breakpoint";

    public FunicLineBreakpointType() {
        super(ID, FunicConsts.LANGUAGE_ID + " Line Breakpoints");
    }

    @Override
    public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
        return FunicFileType.INSTANCE.equals(file.getFileType());
    }

    @Nullable
    @Override
    public FunicBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
        return new FunicBreakpointProperties();
    }

    @Override
    public String getDisplayText(@NotNull XLineBreakpoint<FunicBreakpointProperties> breakpoint) {
        String url = breakpoint.getFileUrl();
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return FunicConsts.LANGUAGE_ID + " breakpoint at " + fileName + ":" + (breakpoint.getLine() + 1);
    }
}
