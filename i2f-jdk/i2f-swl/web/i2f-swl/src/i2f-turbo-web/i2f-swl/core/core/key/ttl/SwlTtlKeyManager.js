/**
 * @return {SwlTtlKeyManager}
 * @interface {SwlTtlKeyManager}
 * @extends {SwlKeyManager}
 */
function SwlTtlKeyManager() {

}


/**
 * @return {boolean}
 */
SwlTtlKeyManager.prototype.preferSetAndTtl = function () {

}

/**
 *
 * @param selfAsymSign {String}
 * @param keyPair {AsymKeyPair}
 * @param ttlSeconds {long}
 * @return {void}
 */
SwlTtlKeyManager.prototype.setSelfKeyPairWithTtl = function (selfAsymSign,keyPair,ttlSeconds) {

}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @param ttlSeconds {long}
 * @return {void}
 */
SwlTtlKeyManager.prototype.setOtherPublicKeyWithTtl = function (otherAsymSign,publicKey,ttlSeconds) {

}

/**
 *
 * @param selfAsymSign {String}
 * @param ttlSeconds {long}
 */
SwlTtlKeyManager.prototype.setSelfTtl = function (selfAsymSign,ttlSeconds) {

}

/**
 *
 * @param otherAsymSign {String}
 * @param ttlSeconds {long}
 */
SwlTtlKeyManager.prototype.setOtherTtl = function (otherAsymSign,ttlSeconds) {

}

export default SwlTtlKeyManager
