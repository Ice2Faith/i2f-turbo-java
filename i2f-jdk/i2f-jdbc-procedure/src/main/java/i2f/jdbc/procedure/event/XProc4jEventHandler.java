package i2f.jdbc.procedure.event;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2025/5/6 14:26
 */
public interface XProc4jEventHandler {
    Collection<XProc4jEventListener> getListeners();

    void send(XProc4jEvent event);

    void publish(XProc4jEvent event);
}
