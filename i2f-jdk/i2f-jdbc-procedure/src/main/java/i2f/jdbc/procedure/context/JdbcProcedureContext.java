package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:50
 * @desc
 */
@Data
public class JdbcProcedureContext extends CacheObjectRefresherSupplier<Map<String, ProcedureMeta>, ConcurrentHashMap<String, ProcedureMeta>> {
    protected volatile JdbcProcedureNodeMapSupplier nodeSupplier;
    protected volatile JdbcProcedureJavaCallerMapSupplier javaCallerSupplier;
    protected final CopyOnWriteArrayList<Consumer<JdbcProcedureContext>> refreshListeners=new CopyOnWriteArrayList<>();

    public JdbcProcedureContext() {
        super(new ConcurrentHashMap<>(), "procedure-context-refresher");
    }

    public JdbcProcedureContext(Map<String, ProcedureMeta> map) {
        this();
        registry(map);
    }

    public JdbcProcedureContext(JdbcProcedureNodeMapSupplier nodeSupplier, JdbcProcedureJavaCallerMapSupplier javaCallerSupplier) {
        this();
        this.nodeSupplier = nodeSupplier;
        this.javaCallerSupplier = javaCallerSupplier;
    }

    public void listener(Consumer<JdbcProcedureContext> listener){
        this.refreshListeners.add(listener);
    }

    public void registry(Map<String, ProcedureMeta> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, ProcedureMeta> entry : map.entrySet()) {
            registry(entry.getKey(), entry.getValue());
        }
    }

    public void registry(Collection<ProcedureMeta> list) {
        if (list == null) {
            return;
        }
        for (ProcedureMeta meta : list) {
            registry(meta);
        }
    }

    public void registry(ProcedureMeta... args) {
        registry(Arrays.asList(args));
    }

    public void registry(ProcedureMeta meta) {
        registry(null, meta);
    }

    public void registry(String name, ProcedureMeta meta) {
        if (meta == null) {
            return;
        }
        if (name == null) {
            name = meta.getName();
        }
        if (name == null || name.isEmpty()) {
            return;
        }
        cache.put(name, meta);
    }

    public void registry(String key, XmlNode value) {
        ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
        registry(meta);
    }

    public void registry(String key, JdbcProcedureJavaCaller value) {
        ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
        registry(meta);
    }

    @Override
    public boolean isMissingCache() {
        return cache == null || cache.isEmpty();
    }

    @Override
    public Map<String, ProcedureMeta> wrapGet(ConcurrentHashMap<String, ProcedureMeta> ret) {
        return new HashMap<>(getCache());
    }


    @Override
    public void refresh() {
        Map<String, ProcedureMeta> metaMap = new HashMap<>();
        if (nodeSupplier != null) {
            Map<String, XmlNode> nodeMap = nodeSupplier.getNodeMap();
            if (nodeMap != null) {
                for (Map.Entry<String, XmlNode> entry : nodeMap.entrySet()) {
                    String key = entry.getKey();
                    XmlNode value = entry.getValue();
                    ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
                    if (meta != null) {
                        metaMap.put(meta.getName(), meta);
                    }
                }
            }
        }
        if (javaCallerSupplier != null) {
            Map<String, JdbcProcedureJavaCaller> javaCallerMap = javaCallerSupplier.getJavaCallerMap();
            if (javaCallerMap != null) {
                for (Map.Entry<String, JdbcProcedureJavaCaller> entry : javaCallerMap.entrySet()) {
                    String key = entry.getKey();
                    JdbcProcedureJavaCaller value = entry.getValue();
                    ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
                    if (meta != null) {
                        metaMap.put(meta.getName(), meta);
                    }
                }
            }
        }

        cache.putAll(metaMap);

        for (Consumer<JdbcProcedureContext> listener : refreshListeners) {
            if(listener==null){
                continue;
            }
            listener.accept(this);
        }
    }

    public void reportGrammar(JdbcProcedureExecutor executor,Consumer<String> warnPoster){
        Map<String,ProcedureMeta> map=new HashMap<>(cache);
        List<ExecutorNode> nodes = executor.getNodes();
        for (Map.Entry<String, ProcedureMeta> entry : map.entrySet()) {
            ProcedureMeta meta = entry.getValue();
            if(meta.getType()== ProcedureMeta.Type.XML){
                XmlNode node = (XmlNode) meta.getTarget();
                for (ExecutorNode executorNode : nodes) {
                    if(executorNode.support(node)){
                        try {
                            executorNode.reportGrammar(node,(msg)->{
                                warnPoster.accept("xproc4j report xml grammar, at "+node.getLocationFile()+":"+node.getLocationLineNumber()+" error: "+msg);
                            });
                        } catch (Throwable e) {
                            warnPoster.accept("xproc4j report xml grammar, at "+node.getLocationFile()+":"+node.getLocationLineNumber()+" error: "+e.getMessage());
                        }
                        break;
                    }
                }
            }
        }
    }
}
