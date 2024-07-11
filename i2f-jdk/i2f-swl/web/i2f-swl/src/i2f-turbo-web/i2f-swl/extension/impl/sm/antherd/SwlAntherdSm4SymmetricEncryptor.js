import Sm4Util from "../../../../../i2f-core/crypto/symmetric/Sm4Util";

/**
 * @param key {String}
 * @reutrn {SwlAntherdSm4SymmetricEncryptor}
 * @implements {ISwlSymmetricEncryptor}
 */
function SwlAntherdSm4SymmetricEncryptor(key = null) {
    /**
     * @type {String}
     */
    this._key = key
}


/**
 * @override
 * return {String}
 */
SwlAntherdSm4SymmetricEncryptor.prototype.generateKey = function () {
    return Sm4Util.genKey()
}
/**
 * @override
 * @return {String}
 */
SwlAntherdSm4SymmetricEncryptor.prototype.getKey = function () {
    return this._key
}
/**
 *
 * @param key {String}
 * @return {void}
 */
SwlAntherdSm4SymmetricEncryptor.prototype.setKey = function (key) {
    this._key = key
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm4SymmetricEncryptor.prototype.encrypt = function (data) {
    return Sm4Util.encrypt(data, this._key)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm4SymmetricEncryptor.prototype.decrypt = function (data) {
    return Sm4Util.decrypt(data, this._key)
}

export default SwlAntherdSm4SymmetricEncryptor
