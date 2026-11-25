package i2f.jdbc.procedure.node.impl.tinyscript;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/11/25 9:42
 * @desc
 */
public class ExecMethodProviderHelper {

    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Method>> executorMethods = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Method>> execContextMethods = new ConcurrentHashMap<>();

    public static CopyOnWriteArrayList<Method> getExecutorMethods(String naming) {
        if (executorMethods.isEmpty()) {
            synchronized (executorMethods) {
                Method[] list = ExecutorMethodProvider.class.getMethods();
                for (Method item : list) {
                    if (Object.class.equals(item.getDeclaringClass())) {
                        continue;
                    }
                    executorMethods.computeIfAbsent(item.getName(), (k) -> new CopyOnWriteArrayList<>())
                            .add(item);
                }
            }
        }
        return executorMethods.get(naming);
    }

    public static CopyOnWriteArrayList<Method> getExecContextMethods(String naming) {
        if (execContextMethods.isEmpty()) {
            synchronized (execContextMethods) {
                Method[] list = ExecContextMethodProvider.class.getMethods();
                for (Method item : list) {
                    if (Object.class.equals(item.getDeclaringClass())) {
                        continue;
                    }
                    execContextMethods.computeIfAbsent(item.getName(), (k) -> new CopyOnWriteArrayList<>())
                            .add(item);
                }
            }
        }
        return execContextMethods.get(naming);
    }

}
