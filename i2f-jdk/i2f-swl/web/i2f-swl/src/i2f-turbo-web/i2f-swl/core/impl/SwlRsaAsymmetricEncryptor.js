import RsaUtil from "../../../i2f-core/crypto/asymmetric/RsaUtil";
import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @param publicKey {String} base64
 * @param privateKey {String} base64
 * @return {SwlRsaAsymmetricEncryptor}
 * @implements {ISwlAsymmetricEncryptor}
 */
function SwlRsaAsymmetricEncryptor(publicKey = null, privateKey = null) {
    /**
     * @type {String} base64
     */
    this._publicKey = publicKey
    /**
     * @type {String} base64
     */
    this._privateKey = privateKey

}


/**
 * @return {AsymKeyPair}
 */
SwlRsaAsymmetricEncryptor.prototype.generateKeyPair = function () {
    return RsaUtil.genKeyPair()
}
/**
 * @return {AsymKeyPair}
 */
SwlRsaAsymmetricEncryptor.prototype.getKeyPair = function () {
    return new AsymKeyPair(this._publicKey, this._privateKey)
}
/**
 * @param key : {AsymKeyPair}
 * @return {void}
 */
SwlRsaAsymmetricEncryptor.prototype.setKeyPair = function (key) {
    this._publicKey = key.getPublicKey()
    this._privateKey = key.getPrivateKey()
}
/**
 * @return {String}
 */
SwlRsaAsymmetricEncryptor.prototype.getPublicKey = function () {
    return this._publicKey
}
/**
 * @param publicKey {String}
 * @return {void}
 */
SwlRsaAsymmetricEncryptor.prototype.setPublicKey = function (publicKey) {
    this._publicKey = publicKey
}
/**
 * @return {String}
 */
SwlRsaAsymmetricEncryptor.prototype.getPrivateKey = function () {
    return this._privateKey
}
/**
 *
 * @param privateKey {String}
 * @return {void}
 */
SwlRsaAsymmetricEncryptor.prototype.setPrivateKey = function (privateKey) {
    this._privateKey = privateKey
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlRsaAsymmetricEncryptor.prototype.encrypt = function (data) {
    return RsaUtil.publicKeyEncrypt(this._publicKey, data)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlRsaAsymmetricEncryptor.prototype.decrypt = function (data) {
    return RsaUtil.privateKeyDecrypt(this._privateKey, data)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlRsaAsymmetricEncryptor.prototype.sign = function (data) {
    return RsaUtil.privateKeyEncrypt(this._privateKey, data)
}
/**
 *
 * @param sign {String}
 * @param data {String}
 * @return {boolean}
 */
SwlRsaAsymmetricEncryptor.prototype.verify = function (sign, data) {
    let enc = RsaUtil.publicKeyDecrypt(this._publicKey, sign)
    return enc == data
}

export default SwlRsaAsymmetricEncryptor
