package i2f.jdbc.procedure.event;

/**
 * @author Ice2Faith
 * @date 2025/5/6 14:26
 */
public interface XProc4jEventHandler {
    void listen(XProc4jEventListener listener);

    void remove(XProc4jEventListener listener);

    void remove(Class<XProc4jEventListener> type);

    void send(XProc4jEvent event);

    void publish(XProc4jEvent event);
}
