package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class JdbcProcedureBreakpointProperties extends XBreakpointProperties<JdbcProcedureBreakpointProperties> {

    @Nullable
    @Override
    public JdbcProcedureBreakpointProperties getState() {
        return this;
    }

    @Override
    public void loadState(JdbcProcedureBreakpointProperties state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
