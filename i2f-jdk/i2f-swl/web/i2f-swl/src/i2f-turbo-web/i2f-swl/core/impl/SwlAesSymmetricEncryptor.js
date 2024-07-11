import AesUtil from "../../../i2f-core/crypto/symmetric/AesUtil";
import Base64Util from "@/i2f-turbo-web/i2f-core/codec/Base64Util";

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
    let ret= AesUtil.genKey()
    return Base64Util.encrypt(ret)
}
/**
 * @override
 * @return {String}
 */
SwlAesSymmetricEncryptor.prototype.getKey = function () {
    let ret= this._key
    return Base64Util.encrypt(ret)
}
/**
 *
 * @param key {String}
 * @return {void}
 */
SwlAesSymmetricEncryptor.prototype.setKey = function (key) {
    key=Base64Util.decrypt(key)
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
