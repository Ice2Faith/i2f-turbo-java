package i2f.jdbc.procedure.event;

/**
 * @author Ice2Faith
 * @date 2025/4/17 13:53
 */
public interface XProc4jEventListener {
    boolean support(XProc4jEvent event);

    boolean handle(XProc4jEvent event);

}
