import SwlNonceManager from "../SwlNonceManager";

/**
 * @return {SwlEmptyNonceManager}
 * @implements {SwlNonceManager}
 * @constructor {SwlEmptyNonceManager}
 */
function SwlEmptyNonceManager() {

}

// 继承
SwlEmptyNonceManager.prototype = Object.create(SwlNonceManager.prototype)
SwlEmptyNonceManager.prototype.constructor = SwlEmptyNonceManager

/**
 * @param nonce {String}
 * @return {boolean}
 */
SwlEmptyNonceManager.prototype.contains = function (nonce) {
    return false
}
/**
 * @param nonce {SwlCert}
 * @param timeoutSeconds {long}
 * @return {void}
 */
SwlEmptyNonceManager.prototype.set = function (nonce,timeoutSeconds) {

}

export default SwlEmptyNonceManager
