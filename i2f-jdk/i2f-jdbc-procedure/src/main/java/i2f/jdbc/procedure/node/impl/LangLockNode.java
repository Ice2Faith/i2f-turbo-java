package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.lock.ILock;
import i2f.lock.ILockProvider;
import i2f.lock.impl.JdkCacheLockProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangLockNode extends AbstractExecutorNode implements ILockProvider {
    public static final String TAG_NAME = TagConsts.LANG_LOCK;
    public ILockProvider defaultProvider = new JdkCacheLockProvider();

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue(AttrConsts.VALUE, FeatureConsts.STRING, node, context);
        String type = (String) executor.attrValue(AttrConsts.TYPE, FeatureConsts.STRING, node, context);
        boolean enable = true;
        String test = node.getTagAttrMap().get(AttrConsts.TEST);
        if (test != null) {
            enable = executor.toBoolean(executor.attrValue(AttrConsts.TEST, FeatureConsts.EVAL, node, context));
        }
        if (!enable) {
            executor.execAsProcedure(node, context, false, false);
            return;
        }
        String lockKey = String.valueOf(value);
        ConcurrentHashMap<String, ILockProvider> lockProviders = executor.getLockProviders();
        ILockProvider provider = lockProviders.get(type);
        if (provider == null) {
            for (Map.Entry<String, ILockProvider> entry : lockProviders.entrySet()) {
                provider = entry.getValue();
                break;
            }
        }
        if (provider == null) {
            provider = defaultProvider;
        }
        ILock lock = provider.getLock(lockKey);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            executor.visitSet(context, result, lock);
        }
        try {
            lock.lock();
            try {
                executor.execAsProcedure(node, context, false, false);
            } finally {
                lock.unlock();
            }
        } catch (Throwable e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

    @Override
    public String name() {
        return defaultProvider.name();
    }

    @Override
    public ILock getLock(String key) {
        return defaultProvider.getLock(key);
    }
}
