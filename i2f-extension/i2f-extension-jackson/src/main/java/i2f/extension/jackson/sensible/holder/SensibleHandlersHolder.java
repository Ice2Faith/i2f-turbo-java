package i2f.extension.jackson.sensible.holder;

import i2f.extension.jackson.sensible.handler.ISensibleHandler;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/6/26 22:11
 * @desc
 */
public class SensibleHandlersHolder {

    public static CopyOnWriteArrayList<ISensibleHandler> GLOBAL_HANDLERS = new CopyOnWriteArrayList<>();
    public static ThreadLocal<Collection<ISensibleHandler>> THREAD_HANDLERS = new ThreadLocal<>();

    public static Collection<ISensibleHandler> getContextHandlers() {
        Collection<ISensibleHandler> handlers = THREAD_HANDLERS.get();
        if (handlers != null) {
            return handlers;
        }
        return GLOBAL_HANDLERS;
    }

}
