import SwlRsaAsymmetricEncryptorSupplier from "../impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../impl/SwlBase64Obfuscator";
import MemMapExpireCache from "../../../i2f-core/cache/impl/MemMapExpireCache";
import SwlTransferConfig from "./SwlTransferConfig";
import Random from "../../../i2f-core/util/Random";
import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";
import SwlExchanger from "@/i2f-turbo-web/i2f-swl/core/core/exchanger/SwlExchanger";

/**
 * @return SwlTransferRefactor
 * @constructor {SwlTransferRefactor}
 * @extends {SwlExchanger}
 */
function SwlTransferRefactor() {
    SwlExchanger.call(this)
    /**
     *
     * @type {ISwlAsymmetricEncryptorSupplier}
     */
    this.asymmetricEncryptorSupplier = new SwlRsaAsymmetricEncryptorSupplier()
    /**
     *
     * @type {ISwlSymmetricEncryptorSupplier}
     */
    this.symmetricEncryptorSupplier = new SwlAesSymmetricEncryptorSupplier()
    /**
     *
     * @type {ISwlMessageDigester}
     */
    this.messageDigester = new SwlSha256MessageDigester()
    /**
     *
     * @type {ISwlObfuscator}
     */
    this.obfuscator = new SwlBase64Obfuscator()
    /**
     *
     * @type {IExpireCache}
     */
    this.cache = new MemMapExpireCache();
    /**
     *
     * @type {SwlTransferConfig}
     */
    this.config = new SwlTransferConfig()
    /**
     *
     * @type Random
     */
    this.random = new Random()
    /**
     *
     * @type {boolean}
     */
    this.refreshing = false
    /**
     *
     * @type {number}
     */
    this.schedulePool = null
}

// 继承
SwlTransferRefactor.prototype = Object.create(SwlExchanger.prototype)
SwlTransferRefactor.prototype.constructor = SwlTransferRefactor


SwlTransferRefactor.SELF_KEY_PAIR_CURRENT_KEY = function () {
    return "swl:key:self:current"

};
SwlTransferRefactor.SELF_KEY_PAIR_HISTORY_KEY_PREFIX = function () {
    return "swl:key:self:history:"
};
SwlTransferRefactor.OTHER_KEY_PUBLIC_KEY_PREFIX = function () {
    return "swl:key:other:keys:"
};
SwlTransferRefactor.OTHER_KEY_PUBLIC_DEFAULT = function () {
    return "swl:key:other:default"
};
SwlTransferRefactor.KEYPAIR_SEPARATOR = function () {
    return "\n==========\n"
};

/**
 * @return {void}
 */
SwlTransferRefactor.prototype.initialRefreshingThread = function () {
    if (this.refreshing) {
        return
    }
    this.refreshing = true
    let timeout = this.config.selfKeyExpireSeconds - 10;
    if (timeout <= 0) {
        timeout = this.config.selfKeyExpireSeconds;
    }
    let _this = this
    this.schedulePool = setInterval(() => {
        _this.resetSelfKeyPair()
    }, timeout)

    window.onclose = function () {
        if (_this.schedulePool) {
            clearInterval(_this.schedulePool)
            _this.schedulePool = null
        }
    }
}

/**
 *
 * @param key {String}
 * @return {String}
 */
SwlTransferRefactor.prototype.cacheKey = function (key) {
    let cacheKeyPrefix = this.config.cacheKeyPrefix;
    if (cacheKeyPrefix == null || cacheKeyPrefix === "") {
        return key;
    }
    return cacheKeyPrefix + ":" + key;
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlTransferRefactor.prototype.getSelfKeyPair = function () {
    let obj = this.cache.get(this.cacheKey(SwlTransferRefactor.SELF_KEY_PAIR_CURRENT_KEY()));
    if (obj == null) {
        return this.resetSelfKeyPair();
    }
    let key = SwlTransferRefactor.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + obj;
    obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return this.resetSelfKeyPair();
    }

    let arr = obj.split(SwlTransferRefactor.KEYPAIR_SEPARATOR(), 2);
    let ret = new AsymKeyPair();
    ret.setPublicKey(this.obfuscateDecode(arr[0]));
    ret.setPrivateKey(this.obfuscateDecode(arr[1]));
    return ret;
}

/**
 * @return {String}
 */
SwlTransferRefactor.prototype.getSelfPublicKey = function () {
    let keyPair = this.getSelfKeyPair();
    return keyPair.getPublicKey();
}

/**
 *
 * @param publicKey String
 * @return {String}
 */
SwlTransferRefactor.prototype.calcKeySign = function (publicKey) {
    return this.messageDigester.digest(publicKey);
}

/**
 *
 * @param selfAsymSign {String}
 * @param selfKeyPair {AsymKeyPair}
 * @return {void}
 */
SwlTransferRefactor.prototype.setSelfKeyPair = function (selfAsymSign, selfKeyPair) {
    let key = SwlTransferRefactor.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + selfAsymSign;
    let keyPair = new AsymKeyPair(
        this.obfuscateEncode(selfKeyPair.getPublicKey()),
        this.obfuscateEncode(selfKeyPair.getPrivateKey())
    );
    let text = keyPair.getPublicKey() + SwlTransferRefactor.KEYPAIR_SEPARATOR() + keyPair.getPrivateKey();
    this.cache.setWith(this.cacheKey(key), text, this.config.selfKeyMaxCount * this.config.selfKeyExpireSeconds);
    this.cache.set(this.cacheKey(SwlTransferRefactor.SELF_KEY_PAIR_CURRENT_KEY()), selfAsymSign);
}

/**
 *
 * @param selfAsymSign {String}
 * @return {String|null}
 */
SwlTransferRefactor.prototype.getSelfPrivateKey = function (selfAsymSign) {
    let key = SwlTransferRefactor.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + selfAsymSign;
    let obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return null;
    }
    let arr = obj.split(SwlTransferRefactor.KEYPAIR_SEPARATOR(), 2);
    return this.obfuscateDecode(arr[1]);
}

/**
 *
 * @param otherAsymSign {String}
 * @return {String|null}
 */
SwlTransferRefactor.prototype.getOtherPublicKey = function (otherAsymSign) {
    let key = SwlTransferRefactor.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    let obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return null;
    }
    return this.obfuscateDecode(obj);
}

/**
 *
 * @return {String|null}
 */
SwlTransferRefactor.prototype.getOtherPublicKeyDefault = function () {
    let obj = this.cache.get(this.cacheKey(SwlTransferRefactor.OTHER_KEY_PUBLIC_DEFAULT()));
    if (obj == null) {
        return null;
    }
    let otherAsymSign = obj;
    return this.getOtherPublicKey(otherAsymSign);
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlTransferRefactor.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    let key = SwlTransferRefactor.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    this.cache.setWith(this.cacheKey(key), this.obfuscateEncode(publicKey), this.config.otherKeyExpireSeconds);
    this.cache.set(this.cacheKey(SwlTransferRefactor.OTHER_KEY_PUBLIC_DEFAULT()), otherAsymSign);
}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlTransferRefactor.prototype.refreshOtherPublicKeyExpire = function (otherAsymSign) {
    let key = SwlTransferRefactor.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    this.cache.expire(this.cacheKey(key), this.config.otherKeyExpireSeconds);
}


/**
 *
 * @param otherPublicKey {String}
 * @return {void}
 */
SwlTransferRefactor.prototype.acceptOtherPublicKey = function (otherPublicKey) {
    let otherAsymSign = this.messageDigester.digest(otherPublicKey);
    this.setOtherPublicKey(otherAsymSign, otherPublicKey);
}

/**
 *
 * @return {String}
 */
SwlTransferRefactor.prototype.getSelfSwapKey = function () {
    let selfPublicKey = this.getSelfPublicKey();
    return this.obfuscateEncode(selfPublicKey);
}

/**
 *
 * @param otherSwapKey {String}
 * @return {void}
 */
SwlTransferRefactor.prototype.acceptOtherSwapKey = function (otherSwapKey) {
    let otherPublicKey = this.obfuscateDecode(otherSwapKey);
    this.acceptOtherPublicKey(otherPublicKey);
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlTransferRefactor.prototype.resetSelfKeyPair = function () {
    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    let asymKeyPair = asymmetricEncryptor.generateKeyPair();
    let selfAsymSign = this.messageDigester.digest(asymKeyPair.getPublicKey());
    this.setSelfKeyPair(selfAsymSign, asymKeyPair);
    return asymKeyPair;
}

/**
 *
 * @param remotePublicKey {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransferRefactor.prototype.send = function (remotePublicKey, parts, attaches = null) {
    let keyPair = this.getSelfKeyPair();
    return this.sendByRaw(remotePublicKey, this.calcKeySign(remotePublicKey),
        keyPair.getPrivateKey(), this.calcKeySign(keyPair.getPublicKey()),
        parts, attaches);
}

/**
 *
 * @param clientId {String}
 * @param request {SwlData}
 * @return {SwlData}
 */
SwlTransferRefactor.prototype.receive = function (clientId, request) {
    let selfAsymSign = request.header.remoteAsymSign;
    let otherAsymSign = request.header.localAsymSign;
    let otherPublicKey = this.getOtherPublicKey(otherAsymSign);
    let selfPrivateKey = this.getSelfPrivateKey(selfAsymSign);
    return this.receiveByRaw(clientId, request, otherPublicKey, selfPrivateKey);
}

/**
 *
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransferRefactor.prototype.sendDefault = function (parts, attaches = null) {
    let otherPublicKey = this.getOtherPublicKeyDefault();
    return this.send(otherPublicKey, parts, attaches);
}

/**
 *
 * @param remoteAsymSign {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransferRefactor.prototype.response = function (remoteAsymSign, parts, attaches = null) {
    let otherPublicKey = this.getOtherPublicKey(remoteAsymSign);
    return this.send(otherPublicKey, parts, attaches);
}

export default SwlTransferRefactor
