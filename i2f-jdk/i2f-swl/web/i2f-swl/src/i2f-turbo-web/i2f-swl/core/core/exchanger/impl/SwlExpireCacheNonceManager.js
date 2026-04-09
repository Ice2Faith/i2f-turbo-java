import SwlNonceManager from "../SwlNonceManager";
import LocalStorageExpireCache from "../../../../../i2f-core/cache/impl/LocalStorageExpireCache";

/**
 * @return {SwlExpireCacheNonceManager}
 * @implements {SwlNonceManager}
 * @constructor {SwlExpireCacheNonceManager}
 */
function SwlExpireCacheNonceManager() {
    /**
     *
     * @type {IExpireCache}
     */
    this.cache = new LocalStorageExpireCache();
    /**
     *
     * @type {string}
     */
    this.cacheKeyPrefix=null;
}

// 继承
SwlExpireCacheNonceManager.prototype = Object.create(SwlNonceManager.prototype)
SwlExpireCacheNonceManager.prototype.constructor = SwlExpireCacheNonceManager

SwlExpireCacheNonceManager.NONCE_PREFIX_KEY=function(){
    return "swl:nonce:";
}

/**
 *
 * @param key {String}
 * @return {String}
 */
SwlExpireCacheNonceManager.prototype.cacheKey=function(key) {
    let cacheKeyPrefix = this.cacheKeyPrefix;
    if (cacheKeyPrefix == null || cacheKeyPrefix.length==0) {
        return key;
    }
    return cacheKeyPrefix + ":" + key;
}

/**
 *
 * @param nonce {String}
 * @return {String}
 */
SwlExpireCacheNonceManager.prototype.nonceKey=function(nonce) {
    return SwlExpireCacheNonceManager.NONCE_PREFIX_KEY() + nonce;
}

/**
 * @param nonce {String}
 * @return {boolean}
 */
SwlExpireCacheNonceManager.prototype.contains = function (nonce) {
    return this.cache.exists(this.cacheKey(this.nonceKey(nonce)));
}
/**
 * @param nonce {String}
 * @param timeoutSeconds {long}
 * @return {void}
 */
SwlExpireCacheNonceManager.prototype.set = function (nonce,timeoutSeconds) {
    this.cache.setWith(this.cacheKey(this.nonceKey(nonce)),nonce,timeoutSeconds);
}

export default SwlExpireCacheNonceManager
