package i2f.jdbc.procedure.event.impl;

import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Ice2Faith
 * @date 2025/4/17 13:52
 */
@Data
@NoArgsConstructor
public abstract class AbsXProc4jEventHandler implements XProc4jEventHandler {
    protected final Deque<XProc4jEvent> queue = new LinkedBlockingDeque<>();
    protected volatile Thread dispatchThread = null;
    protected volatile ExecutorService dispatchPool = new ForkJoinPool(4);

    protected void startDispatchThread() {
        if (dispatchThread != null) {
            return;
        }
        synchronized (this) {
            if (dispatchThread != null) {
                return;
            }
            dispatchThread = new Thread(() -> {
                eventLoop();
            });
            dispatchThread.setName(XProc4jConsts.NAME + "-event-dispatcher");
            dispatchThread.setDaemon(true);
            dispatchThread.start();
        }
    }

    protected void eventLoop() {
        while (true) {
            try {
                XProc4jEvent event = queue.poll();
                if (event != null) {
                    if (dispatchPool != null) {
                        dispatchPool.submit(() -> dispatch(event));
                    } else {
                        dispatch(event);
                    }
                } else {
                    Thread.sleep(1);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(XProc4jEvent event) {
        dispatch(event);
    }

    @Override
    public void publish(XProc4jEvent event) {
        startDispatchThread();
        queue.push(event);
    }

    protected void dispatch(XProc4jEvent event) {
        Collection<XProc4jEventListener> listeners = getListeners();
        for (XProc4jEventListener listener : listeners) {
            if (listener == null) {
                continue;
            }
            if (!listener.support(event)) {
                continue;
            }
            if (listener.handle(event)) {
                break;
            }
        }
    }
}
