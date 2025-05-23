package i2f.jdbc.procedure.event.impl;

import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.typeof.TypeOf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/4/17 13:52
 */
@Data
@NoArgsConstructor
public class DefaultXProc4jEventHandler implements XProc4jEventHandler {
    protected final AtomicBoolean initialWithContext = new AtomicBoolean(false);
    protected final CopyOnWriteArrayList<XProc4jEventListener> listeners = new CopyOnWriteArrayList<>();
    protected final Deque<XProc4jEvent> queue = new LinkedBlockingDeque<>();
    protected volatile Supplier<INamingContext> namingContextSupplier;
    protected volatile Thread dispatchThread=null;
    protected volatile ExecutorService dispatchPool= new ForkJoinPool(4);


    public DefaultXProc4jEventHandler(Supplier<INamingContext> namingContextSupplier, XProc4jEventListener... listeners) {
        this.namingContextSupplier = namingContextSupplier;
        for (XProc4jEventListener listener : listeners) {
            listen(listener);
        }
    }

    public List<XProc4jEventListener> getListeners() {
        if (namingContextSupplier != null) {
            INamingContext namingContext = namingContextSupplier.get();
            if (namingContext != null) {
                if (!initialWithContext.getAndSet(true)) {
                    Map<String, XProc4jEventListener> beans = namingContext.getBeansMap(XProc4jEventListener.class);
                    for (Map.Entry<String, XProc4jEventListener> entry : beans.entrySet()) {
                        listen(entry.getValue());
                    }
                }
            }
        }
        return this.listeners;
    }

    @Override
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

    @Override
    public void remove(XProc4jEventListener listener) {
        listeners.remove(listener);
    }

    @Override
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

    protected void startDispatchThread(){
        if(dispatchThread!=null){
            return;
        }
        synchronized (this) {
            if(dispatchThread!=null){
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
                    if(dispatchPool!=null){
                        dispatchPool.submit(()->dispatch(event));
                    }else {
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
        List<XProc4jEventListener> listeners = getListeners();
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
