import SwlKeyExchanger from "@/i2f-turbo-web/i2f-swl/core/core/key/SwlKeyExchanger";
import SwlMemTtlKeyManager from "@/i2f-turbo-web/i2f-swl/core/core/key/ttl/impl/SwlMemTtlKeyManager";

/**
 * @param keyManager {SwlTtlKeyManager}
 * @return {SwlTtlKeyExchanger}
 * @constructor {SwlTtlKeyExchanger}
 * @extends {SwlKeyExchanger}
 */
function SwlTtlKeyExchanger(keyManager = new SwlMemTtlKeyManager()) {
    SwlKeyExchanger.call(this)
    /**
     *
     * @type {int}
     */
    this.selfKeyExpireSeconds = 24 * 60 * 60
    /**
     *
     * @type {int}
     */
    this.selfKeyMaxCount = 3
    /**
     *
     * @type {int}
     */
    this.otherKeyExpireSeconds = 24 * 60 * 60

    this._refreshing = false

    this._schedulePool = null
}

// 继承
SwlTtlKeyExchanger.prototype = Object.create(SwlKeyExchanger.prototype)
SwlTtlKeyExchanger.prototype.constructor = SwlTtlKeyExchanger


/**
 * @return {void}
 */
SwlTtlKeyExchanger.prototype.initialRefreshingThread = function () {
    if (this._refreshing) {
        return
    }
    let timeout = this.selfKeyExpireSeconds - 10;
    if (timeout <= 0) {
        timeout = this.selfKeyExpireSeconds;
    }

    const _this = this
    this._schedulePool = setInterval(() => {
        _this.resetSelfKeyPair()
    }, timeout * 1000)
}

/**
 *
 * @return {SwlTtlKeyManager}
 */
SwlTtlKeyExchanger.prototype.getTtlKeyManager = function () {
    return this.keyManager
}

/**
 *
 * @param selfAsymSign {String}
 * @param selfKeyPair {AsymKeyPair}
 * @return {void}
 */
SwlTtlKeyExchanger.prototype.setSelfKeyPair = function (selfAsymSign, selfKeyPair) {
    let manager = this.getTtlKeyManager();
    if (manager.preferSetAndTtl()) {
        manager.setSelfKeyPairWithTtl(selfAsymSign, selfKeyPair, this.selfKeyMaxCount * this.selfKeyExpireSeconds);
    } else {
        manager.setSelfKeyPair(selfAsymSign, selfKeyPair);
        manager.setSelfTtl(selfAsymSign, this.selfKeyMaxCount * this.selfKeyExpireSeconds);
    }
    manager.setDefaultSelfAsymSign(selfAsymSign);
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlTtlKeyExchanger.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    let manager = this.getTtlKeyManager();
    if (manager.preferSetAndTtl()) {
        manager.setOtherPublicKeyWithTtl(otherAsymSign, publicKey, this.otherKeyExpireSeconds);
    } else {
        manager.setOtherPublicKey(otherAsymSign, publicKey);
        manager.setOtherTtl(otherAsymSign, this.otherKeyExpireSeconds);
    }
    manager.setDefaultOtherAsymSign(otherAsymSign);
}
/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlTtlKeyExchanger.prototype.refreshOtherPublicKeyExpire = function (otherAsymSign) {
    let manager = this.getTtlKeyManager();
    manager.setOtherTtl(otherAsymSign, this.otherKeyExpireSeconds);
}

export default SwlTtlKeyExchanger