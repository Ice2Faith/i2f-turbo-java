package i2f.jdbc.procedure.event.impl;

import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.typeof.TypeOf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/4/17 13:52
 */
@Data
@NoArgsConstructor
public class DefaultXProc4jEventHandler extends AbsXProc4jEventHandler {
    protected final CopyOnWriteArrayList<XProc4jEventListener> listeners = new CopyOnWriteArrayList<>();

    public DefaultXProc4jEventHandler(XProc4jEventListener... listeners) {
        for (XProc4jEventListener listener : listeners) {
            listen(listener);
        }
    }

    @Override
    public List<XProc4jEventListener> getListeners() {
        return this.listeners;
    }

    public void listen(XProc4jEventListener listener) {
        listen(listener, -1);
    }

    public synchronized void listen(XProc4jEventListener listener, int index) {
        if (listener == null) {
            return;
        }
        if (index < 0 || index >= listeners.size()) {
            listeners.addIfAbsent(listener);
        } else {
            listeners.add(index, listener);
        }
    }

    public void remove(XProc4jEventListener listener) {
        listeners.remove(listener);
    }

    public void remove(Class<XProc4jEventListener> type) {
        if (listeners == null) {
            return;
        }
        listeners.removeIf((next) -> {
            if (TypeOf.instanceOf(next, type)) {
                return true;
            }
            return false;
        });
    }

    public void remove(int index) {
        listeners.remove(index);
    }

}
