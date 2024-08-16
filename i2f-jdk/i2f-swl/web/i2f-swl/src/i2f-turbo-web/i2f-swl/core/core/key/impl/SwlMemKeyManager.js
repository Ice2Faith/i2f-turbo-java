import SwlKeyManager from "../SwlKeyManager";
import SwlBase64Obfuscator from "../../../impl/SwlBase64Obfuscator";
import AsymKeyPair from "../../../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @return {SwlMemKeyManager}
 * @implements {SwlKeyManager}
 * @constructor {SwlMemKeyManager}
 */
function SwlMemKeyManager() {

    this.selfCache=new Map()

    this.otherCache=new Map()

    this.selfDefaultCache=null

    this.otherDefaultCache=null
}

// 继承
SwlMemKeyManager.prototype = Object.create(SwlKeyManager.prototype)
SwlMemKeyManager.prototype.constructor = SwlMemKeyManager


/**
 * @return {AsymKeyPair}
 */
SwlMemKeyManager.prototype.getDefaultSelfKeyPair = function () {
    let selfAsymSign = this.getDefaultSelfAsymSign();
    return this.getSelfKeyPair(selfAsymSign);
}

/**
 * @return {String}
 */
SwlMemKeyManager.prototype.getDefaultSelfAsymSign = function () {
    return this.selfDefaultCache;
}

/**
 *
 * @param selfAsymSign {String}
 * @return {void}
 */
SwlMemKeyManager.prototype.setDefaultSelfAsymSign = function (selfAsymSign) {
    this.selfDefaultCache=selfAsymSign;
}

/**
 *
 * @param selfAsymSign {String}
 * @return {AsymKeyPair}
 */
SwlMemKeyManager.prototype.getSelfKeyPair = function (selfAsymSign) {
    if (!selfAsymSign) {
        return null;
    }
    let ret = this.selfCache[selfAsymSign];
    return ret?ret.copy():null
}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @return {void}
 */
SwlMemKeyManager.prototype.setSelfKeyPair = function (selfAsymSign, keyPair) {
    if (!selfAsymSign || !keyPair) {
        return;
    }
    this.selfCache[selfAsymSign]=keyPair.copy();
}

/**
 * @return {String}
 */
SwlMemKeyManager.prototype.getDefaultOtherPublicKey = function () {
    let otherAsymSign = this.getDefaultOtherAsymSign();
    return this.getOtherPublicKey(otherAsymSign);
}

/**
 * @return {String}
 */
SwlMemKeyManager.prototype.getDefaultOtherAsymSign = function () {
    return  this.otherDefaultCache;
}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlMemKeyManager.prototype.setDefaultOtherAsymSign = function (otherAsymSign) {
    this.otherDefaultCache=otherAsymSign;
}

/**
 *
 * @param otherAsymSign {String}
 * @return {String}
 */
SwlMemKeyManager.prototype.getOtherPublicKey = function (otherAsymSign) {
    if (!otherAsymSign) {
        return null;
    }
    return this.otherCache[otherAsymSign];

}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlMemKeyManager.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    if (!otherAsymSign || !publicKey) {
        return;
    }
    this.otherCache[otherAsymSign]=publicKey;
}

export default SwlMemKeyManager
