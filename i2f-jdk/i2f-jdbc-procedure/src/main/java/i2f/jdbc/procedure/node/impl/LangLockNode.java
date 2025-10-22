package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLockNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_LOCK;
    public static ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        String lockKey = String.valueOf(value);
        Lock lock = lockMap.computeIfAbsent(lockKey, k -> new ReentrantLock());
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            executor.visitSet(context, result, lock);
        }
        lock.lock();
        try {
            executor.execAsProcedure(node, context, false, false);
        } finally {
            lock.unlock();
        }
    }
}
