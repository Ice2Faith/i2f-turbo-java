package i2f.swl.core.key.impl;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.key.SwlKeyManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/8/8 21:15
 * @desc
 */
public class SwlMemKeyManager implements SwlKeyManager {
    protected ConcurrentHashMap<String, AsymKeyPair> selfCache = new ConcurrentHashMap<>(1024);
    protected ConcurrentHashMap<String, String> otherCache = new ConcurrentHashMap<>(1024);
    protected AtomicReference<String> selfDefaultCache = new AtomicReference<>();
    protected AtomicReference<String> otherDefaultCache = new AtomicReference<>();


    @Override
    public AsymKeyPair getSelfKeyPair() {
        String selfAsymSign = getDefaultSelfAsymSign();
        return getSelfKeyPair(selfAsymSign);
    }

    @Override
    public String getDefaultSelfAsymSign() {
        return selfDefaultCache.get();
    }

    @Override
    public void setDefaultSelfAsymSign(String selfAsymSign) {
        selfDefaultCache.set(selfAsymSign);
    }

    @Override
    public AsymKeyPair getSelfKeyPair(String selfAsymSign) {
        if (selfAsymSign == null) {
            return null;
        }
        AsymKeyPair ret = selfCache.get(selfAsymSign);
        return ret == null ? null : ret.copy();
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair) {
        if (selfAsymSign == null || keyPair == null) {
            return;
        }
        selfCache.put(selfAsymSign, keyPair.copy());
    }

    @Override
    public String getOtherPublicKey() {
        String otherAsymSign = getDefaultOtherAsymSign();
        return getOtherPublicKey(otherAsymSign);
    }

    @Override
    public String getDefaultOtherAsymSign() {
        return otherDefaultCache.get();
    }

    @Override
    public void setDefaultOtherAsymSign(String otherAsymSign) {
        otherDefaultCache.set(otherAsymSign);
    }

    @Override
    public String getOtherPublicKey(String otherAsymSign) {
        return otherCache.get(otherAsymSign);
    }

    @Override
    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        otherCache.put(otherAsymSign, publicKey);
    }
}
