package i2f.turbo.idea.plugin.tinyscript.lang.debugger;

import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class TinyScriptBreakpointProperties extends XBreakpointProperties<TinyScriptBreakpointProperties> {

    @Nullable
    @Override
    public TinyScriptBreakpointProperties getState() {
        return this;
    }

    @Override
    public void loadState(TinyScriptBreakpointProperties state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
