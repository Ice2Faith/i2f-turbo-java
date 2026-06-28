package i2f.turbo.idea.plugin.funic.lang.debugger;

import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class FunicBreakpointProperties extends XBreakpointProperties<FunicBreakpointProperties> {

    @Nullable
    @Override
    public FunicBreakpointProperties getState() {
        return this;
    }

    @Override
    public void loadState(FunicBreakpointProperties state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
