/**
 * @return {SwlKeyManager}
 * @interface {SwlKeyManager}
 */
function SwlKeyManager() {

}


/**
 * @return {AsymKeyPair}
 */
SwlKeyManager.prototype.getDefaultSelfKeyPair = function () {

}

/**
 * @return {String}
 */
SwlKeyManager.prototype.getDefaultSelfAsymSign = function () {

}

/**
 *
 * @param selfAsymSign {String}
 * @return {void}
 */
SwlKeyManager.prototype.setDefaultSelfAsymSign = function (selfAsymSign) {

}

/**
 *
 * @param selfAsymSign {String}
 * @return {AsymKeyPair}
 */
SwlKeyManager.prototype.getSelfKeyPair = function (selfAsymSign) {

}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @return {void}
 */
SwlKeyManager.prototype.setSelfKeyPair = function (selfAsymSign, keyPair) {

}

/**
 * @return {String}
 */
SwlKeyManager.prototype.getDefaultOtherPublicKey = function () {

}

/**
 * @return {String}
 */
SwlKeyManager.prototype.getDefaultOtherAsymSign = function () {

}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlKeyManager.prototype.setDefaultOtherAsymSign = function (otherAsymSign) {

}

/**
 *
 * @param otherAsymSign {String}
 * @return {String}
 */
SwlKeyManager.prototype.getOtherPublicKey = function (otherAsymSign) {

}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlKeyManager.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {

}

export default SwlKeyManager
