package i2f.jdbc.procedure.event.impl;

import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/4/17 13:52
 */
@Data
@NoArgsConstructor
public class ContextXProc4jEventHandler extends AbsXProc4jEventHandler {

    protected volatile INamingContext context;

    public ContextXProc4jEventHandler(INamingContext context) {
        this.context = context;
    }

    @Override
    public Collection<XProc4jEventListener> getListeners() {
        List<XProc4jEventListener> beans = context.getBeans(XProc4jEventListener.class);
        return beans;
    }

}
