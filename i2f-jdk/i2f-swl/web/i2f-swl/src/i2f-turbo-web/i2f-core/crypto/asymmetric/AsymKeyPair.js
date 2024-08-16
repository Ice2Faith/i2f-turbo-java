/**
 * @constructor
 * @param publicKey {String}
 * @param privateKey {String}
 * @return {AsymKeyPair}
 */
function AsymKeyPair(publicKey = null, privateKey = null) {
    /**
     * @type String
     */
    this._publicKey = publicKey
    /**
     * @type String
     */
    this._privateKey = privateKey

}


/**
 * @return String
 */
AsymKeyPair.prototype.getPublicKey = function () {
    return this._publicKey
}
/**
 * @param publicKey String
 * @return void
 */
AsymKeyPair.prototype.setPublicKey = function (publicKey) {
    this._publicKey = publicKey
}
/**
 * @return String
 */
AsymKeyPair.prototype.getPrivateKey = function () {
    return this._privateKey
}
/**
 * @param privateKey String
 * @return void
 */
AsymKeyPair.prototype.setPrivateKey = function (privateKey) {
    this._privateKey = privateKey
}

/**
 *
 * @param pair {AsymKeyPair}
 * @return {AsymKeyPair}
 */
AsymKeyPair.copy=function(pair){
    return new AsymKeyPair(pair.getPublicKey(),pair.getPrivateKey())
}

/**
 *
 * @return {AsymKeyPair}
 */
AsymKeyPair.prototype.copy=function(){
    return AsymKeyPair.copy(this)
}

export default AsymKeyPair
