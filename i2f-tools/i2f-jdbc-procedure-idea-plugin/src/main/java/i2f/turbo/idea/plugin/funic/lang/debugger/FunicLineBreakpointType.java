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
 * @desc Funic 脚本行断点类型，允许在 .fic 文件中设置断点。
 *       当调试的 Java 进程调用 FunicDebugReporter.report(fileName, lineNumber, variableMap)
 *       并且 fileName + lineNumber 与本断点匹配时，调试器将暂停执行。
 */
public class FunicLineBreakpointType extends XLineBreakpointType<FunicBreakpointProperties> {

    @NonNls
    public static final String ID = "funic-line-breakpoint";

    public FunicLineBreakpointType() {
        super(ID, FunicConsts.LANGUAGE_ID + " Line Breakpoints");
    }

    /**
     * 只有 .fic 文件才允许打断点
     */
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
