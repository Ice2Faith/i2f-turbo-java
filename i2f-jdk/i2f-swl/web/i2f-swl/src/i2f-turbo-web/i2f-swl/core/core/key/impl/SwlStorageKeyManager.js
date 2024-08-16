import SwlKeyManager from "../SwlKeyManager";
import SwlBase64Obfuscator from "../../../impl/SwlBase64Obfuscator";
import AsymKeyPair from "../../../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @param {localStorage|sessionStorage}
 * @return {SwlStorageKeyManager}
 * @implements {SwlKeyManager}
 * @constructor {SwlStorageKeyManager}
 */
function SwlStorageKeyManager(cachePrefix = SwlStorageKeyManager.DEFAULT_KEY_PREFIX(),
                              storage=localStorage) {
    this.cachePrefix = cachePrefix

    this.storage=storage

    this.obfuscator = new SwlBase64Obfuscator()

    this.selfCache=new Map()

    this.otherCache=new Map()

    this.selfDefaultCache=null

    this.otherDefaultCache=null
}

// 继承
SwlStorageKeyManager.prototype = Object.create(SwlKeyManager.prototype)
SwlStorageKeyManager.prototype.constructor = SwlStorageKeyManager


/**
 *
 * @return {string}
 */
SwlStorageKeyManager.DEFAULT_KEY_PREFIX = function () {
    return "swl:key:"
}

/**
 *
 * @return {string}
 */
SwlStorageKeyManager.SELF_SUFFIX = function () {
    return "self:keys:"
}

/**
 *
 * @return {string}
 */
SwlStorageKeyManager.OTHER_SUFFIX = function () {
    return "client:keys:"
}

/**
 *
 * @return {string}
 */
SwlStorageKeyManager.DEFAULT_SELF_NAME = function () {
    return "self:default"
}

/**
 *
 * @return {string}
 */
SwlStorageKeyManager.DEFAULT_OTHER_NAME = function () {
    return "other:default"
}

/**
 *
 * @return {string}
 */
SwlStorageKeyManager.KEY_PAIR_SEPARATOR = function () {
    return "\n====\n"
}

/**
 *
 * @param asymSign {String}
 * @param self {boolean}
 * @return {String}
 */
SwlStorageKeyManager.prototype.getSignName=function(asymSign, self) {
    if (self) {
        return SwlStorageKeyManager.SELF_SUFFIX() + asymSign;
    }
    return SwlStorageKeyManager.OTHER_SUFFIX() + asymSign;
}

/**
 *
 * @param key {String}
 * @return {String}
 */
SwlStorageKeyManager.prototype.cacheKey=function(key) {
    if (!this.cachePrefix || this.cachePrefix=='') {
        return key;
    }
    return this.cachePrefix + ":" + key;
}

/**
 *
 * @param data {String}
 * @return {String|null}
 */
SwlStorageKeyManager.prototype.obfuscateEncode=function(data) {
    if (!data) {
        return null;
    }
    if (this.obfuscator) {
        return data;
    }
    return this.obfuscator.encode(data);
}

/**
 *
 * @param data {String}
 * @return {String|null}
 */
SwlStorageKeyManager.prototype.obfuscateDecode=function(data) {
    if (!data) {
        return null;
    }
    if (!this.obfuscator) {
        return data;
    }
    return this.obfuscator.decode(data);
}

/**
 *
 * @param keyPair {AsymKeyPair}
 * @return {string}
 */
SwlStorageKeyManager.prototype.serializeKeyPair=function(keyPair) {
    let builder = '';
    builder+=(!keyPair.getPublicKey() ? "" : this.obfuscateEncode(keyPair.getPublicKey()));
    builder+=(SwlStorageKeyManager.KEY_PAIR_SEPARATOR());
    builder+=(!keyPair.getPrivateKey() ? "" : this.obfuscateEncode(keyPair.getPrivateKey()));
    return builder;
}

/**
 * @param str {String}
 * @return {AsymKeyPair}
 */
SwlStorageKeyManager.prototype.deserializeKeyPair=function(str) {
    let arr = str.split(SwlStorageKeyManager.KEY_PAIR_SEPARATOR(), 2);
    let publicKey = arr[0];
    if (publicKey=='') {
        publicKey = null;
    }
    let privateKey = null;
    if (arr.length > 1) {
        privateKey = arr[1];
    }
    if (privateKey && privateKey=='') {
        privateKey = null;
    }
    return new AsymKeyPair(
        this.obfuscateDecode(publicKey),
        this.obfuscateDecode(privateKey)
    );
}

/**
 * @return {AsymKeyPair}
 */
SwlStorageKeyManager.prototype.getDefaultSelfKeyPair = function () {
    let selfAsymSign = this.getDefaultSelfAsymSign();
    return this.getSelfKeyPair(selfAsymSign);
}

/**
 * @return {String}
 */
SwlStorageKeyManager.prototype.getDefaultSelfAsymSign = function () {
    let str = this.selfDefaultCache;
    if (str) {
        return str;
    }
    let selfAsymSign = this.storage.getItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_SELF_NAME()));
    if (selfAsymSign) {
        this.selfDefaultCache=selfAsymSign;
    }
    return selfAsymSign;
}

/**
 *
 * @param selfAsymSign {String}
 * @return {void}
 */
SwlStorageKeyManager.prototype.setDefaultSelfAsymSign = function (selfAsymSign) {
    this.selfDefaultCache=selfAsymSign;
    if (!selfAsymSign) {
        this.storage.removeItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_SELF_NAME()));
        return;
    }
    this.storage.setItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_SELF_NAME()), selfAsymSign);

}

/**
 *
 * @param selfAsymSign {String}
 * @return {AsymKeyPair}
 */
SwlStorageKeyManager.prototype.getSelfKeyPair = function (selfAsymSign) {
    if (!selfAsymSign) {
        return null;
    }
    let keyPair = this.selfCache[selfAsymSign];
    if (keyPair) {
        return keyPair;
    }
    let ret = null;

    let str = this.storage.getItem(this.cacheKey(this.getSignName(selfAsymSign, true)));
    if (str) {
        ret = this.deserializeKeyPair(str);
    }

    if (ret != null) {
        this.selfCache[selfAsymSign]=ret.copy();
    }
    return ret;
}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @return {void}
 */
SwlStorageKeyManager.prototype.setSelfKeyPair = function (selfAsymSign, keyPair) {
    if (!selfAsymSign || !keyPair) {
        return;
    }
    this.selfCache[selfAsymSign]=keyPair.copy();
    let str = this.serializeKeyPair(keyPair);
    this.storage.setItem(this.cacheKey(this.getSignName(selfAsymSign, true)), str);
}

/**
 * @return {String}
 */
SwlStorageKeyManager.prototype.getDefaultOtherPublicKey = function () {
    let otherAsymSign = this.getDefaultOtherAsymSign();
    return this.getOtherPublicKey(otherAsymSign);
}

/**
 * @return {String}
 */
SwlStorageKeyManager.prototype.getDefaultOtherAsymSign = function () {
    let str = this.otherDefaultCache;
    if (str) {
        return str;
    }

    let otherAsymSign = this.storage.getItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_OTHER_NAME()));
    if (otherAsymSign) {
        this.otherDefaultCache=otherAsymSign;
    }
    return otherAsymSign;
}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlStorageKeyManager.prototype.setDefaultOtherAsymSign = function (otherAsymSign) {
    this.otherDefaultCache=otherAsymSign;
    if (!otherAsymSign) {
        this.storage.removeItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_OTHER_NAME()));
        return;
    }
    this.storage.setItem(this.cacheKey(SwlStorageKeyManager.DEFAULT_OTHER_NAME()), otherAsymSign);

}

/**
 *
 * @param otherAsymSign {String}
 * @return {String}
 */
SwlStorageKeyManager.prototype.getOtherPublicKey = function (otherAsymSign) {
    if (!otherAsymSign) {
        return null;
    }
    let publicKey = this.otherCache[otherAsymSign];
    if (publicKey) {
        return publicKey;
    }
    let ret = null;

    let str = this.storage.getItem(this.cacheKey(this.getSignName(otherAsymSign, false)));
    if (str) {
        let keyPair = this.deserializeKeyPair(str);
        ret = keyPair.getPublicKey();
    }

    if (ret) {
        this.otherCache[otherAsymSign]=ret;
    }
    return ret;
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlStorageKeyManager.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    if (!otherAsymSign || !publicKey) {
        return;
    }
    this.otherCache[otherAsymSign]=publicKey;
    let keyPair = new AsymKeyPair(publicKey, null);
    let str = this.serializeKeyPair(keyPair);
    this.storage.setItem(this.cacheKey(this.getSignName(otherAsymSign, false)), str);
}

export default SwlStorageKeyManager
