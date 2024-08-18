import SwlTtlKeyManager from "../SwlTtlKeyManager";
import SystemClock from "../../../../../../i2f-core/clock/SystemClock";

/**
 *
 * @param key {any}
 * @param value {any}
 * @constructor {SimpleEntry}
 * @return {SimpleEntry}
 */
function SimpleEntry(key=null,value=null){
    this.key=key
    this.value=value
}

/**
 * @return {SwlMemTtlKeyManager}
 * @constructor {SwlMemTtlKeyManager}
 * @implements {SwlTtlKeyManager}
 */
function SwlMemTtlKeyManager(){
    /**
     *
     * @type {Map<String,SimpleEntry<AsymKeyPair, long>>}
     */
    this.selfCache=new Map()

    /**
     *
     * @type {Map<String, SimpleEntry<String,long>>}
     */
    this.otherCache=new Map()

    this.selfDefaultCache=null

    this.otherDefaultCache=null
}

// 继承
SwlMemTtlKeyManager.prototype = Object.create(SwlTtlKeyManager.prototype)
SwlMemTtlKeyManager.prototype.constructor = SwlMemTtlKeyManager


/**
 * @return {AsymKeyPair}
 */
SwlMemTtlKeyManager.prototype.getDefaultSelfKeyPair = function () {
    let selfAsymSign = this.getDefaultSelfAsymSign();
    return this.getSelfKeyPair(selfAsymSign);
}

/**
 * @return {String}
 */
SwlMemTtlKeyManager.prototype.getDefaultSelfAsymSign = function () {
    return this.selfDefaultCache;
}

/**
 *
 * @param selfAsymSign {String}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setDefaultSelfAsymSign = function (selfAsymSign) {
    this.selfDefaultCache=selfAsymSign;
}

/**
 *
 * @param selfAsymSign {String}
 * @return {AsymKeyPair}
 */
SwlMemTtlKeyManager.prototype.getSelfKeyPair = function (selfAsymSign) {
    if (!selfAsymSign) {
        return null;
    }
    let entry = this.selfCache[selfAsymSign];
    if(entry){
        if(SystemClock.currentTimeMillis()>entry.value && entry.value>=0){
            this.selfCache.delete(selfAsymSign)
            return null
        }
        return entry.key.copy()
    }
    return null
}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setSelfKeyPair = function (selfAsymSign, keyPair) {
    if (!selfAsymSign  || !keyPair) {
        return;
    }
    this.selfCache[selfAsymSign]=new SimpleEntry(keyPair.copy(), -1);
}

/**
 * @return {String}
 */
SwlMemTtlKeyManager.prototype.getDefaultOtherPublicKey = function () {
    let otherAsymSign = this.getDefaultOtherAsymSign();
    return this.getOtherPublicKey(otherAsymSign);
}

/**
 * @return {String}
 */
SwlMemTtlKeyManager.prototype.getDefaultOtherAsymSign = function () {
    return this.otherDefaultCache
}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setDefaultOtherAsymSign = function (otherAsymSign) {
    this.otherDefaultCache=otherAsymSign;
}

/**
 *
 * @param otherAsymSign {String}
 * @return {String}
 */
SwlMemTtlKeyManager.prototype.getOtherPublicKey = function (otherAsymSign) {
    if (!otherAsymSign) {
        return null;
    }
    let entry = this.otherCache[otherAsymSign];
    if (entry) {
        if (SystemClock.currentTimeMillis() > entry.value && entry.value >= 0) {
            this.otherCache.delete(otherAsymSign);
            return null;
        }
        return entry.key;
    }
    return null
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    if (!otherAsymSign || !publicKey) {
        return;
    }
    this.otherCache[otherAsymSign]=new SimpleEntry(publicKey, -1);
}


/**
 * @return {boolean}
 */
SwlMemTtlKeyManager.prototype.preferSetAndTtl = function () {
    return true
}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @param ttlSeconds {long}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setSelfKeyPairWithTtl = function (selfAsymSign,keyPair,ttlSeconds) {
    if (!selfAsymSign || !keyPair) {
        return;
    }
    let expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
    this.selfCache[selfAsymSign]=new SimpleEntry(keyPair.copy(), expireTs);
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @param ttlSeconds {long}
 * @return {void}
 */
SwlMemTtlKeyManager.prototype.setOtherPublicKeyWithTtl = function (otherAsymSign,publicKey,ttlSeconds) {
    if (!otherAsymSign || !publicKey) {
        return;
    }
    let expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
    this.otherCache[otherAsymSign]=new SimpleEntry(publicKey, expireTs);
}

/**
 *
 * @param selfAsymSign {String}
 * @param ttlSeconds {long}
 */
SwlMemTtlKeyManager.prototype.setSelfTtl = function (selfAsymSign,ttlSeconds) {
    let entry = this.selfCache[selfAsymSign];
    let expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
    if (entry != null) {
        entry.value=expireTs;
    }
}

/**
 *
 * @param otherAsymSign {String}
 * @param ttlSeconds {long}
 */
SwlMemTtlKeyManager.prototype.setOtherTtl = function (otherAsymSign,ttlSeconds) {
    let entry = this.otherCache[otherAsymSign];
    let expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
    if (entry != null) {
        entry.value=expireTs;
    }
}

export default SwlMemTtlKeyManager