import AesUtil from "../../../i2f-core/crypto/symmetric/AesUtil";

/**
 * @param key {String}
 * @reutrn {SwlAesSymmetricEncryptor}
 * @implements {ISwlSymmetricEncryptor}
 */
function SwlAesSymmetricEncryptor(key = null) {
    /**
     * @type {String}
     */
    this._key = key
}


/**
 * @override
 * return {String}
 */
SwlAesSymmetricEncryptor.prototype.generateKey = function () {
    return AesUtil.genKey()
}
/**
 * @override
 * @return {String}
 */
SwlAesSymmetricEncryptor.prototype.getKey = function () {
    return this._key
}
/**
 *
 * @param key {String}
 * @return {void}
 */
SwlAesSymmetricEncryptor.prototype.setKey = function (key) {
    this._key = key
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAesSymmetricEncryptor.prototype.encrypt = function (data) {
    return AesUtil.encrypt(data, this._key)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAesSymmetricEncryptor.prototype.decrypt = function (data) {
    return AesUtil.decrypt(data, this._key)
}

export default SwlAesSymmetricEncryptor
