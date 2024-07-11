import Sm2Util from "../../../../../i2f-core/crypto/asymmetric/Sm2Util";
import AsymKeyPair from "../../../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @param publicKey {String} base64
 * @param privateKey {String} base64
 * @return {SwlAntherdSm2AsymmetricEncryptor}
 * @implements {ISwlAsymmetricEncryptor}
 */
function SwlAntherdSm2AsymmetricEncryptor(publicKey = null, privateKey = null) {
    /**
     * @type {String} hex
     */
    this._publicKey = publicKey
    /**
     * @type {String} hex
     */
    this._privateKey = privateKey

}


/**
 * @return {AsymKeyPair}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.generateKeyPair = function () {
    return Sm2Util.genKeyPair()
}
/**
 * @return {AsymKeyPair}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.getKeyPair = function () {
    return new AsymKeyPair(this._publicKey, this._privateKey)
}
/**
 * @param key : {AsymKeyPair}
 * @return {void}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.setKeyPair = function (key) {
    this._publicKey = key.getPublicKey()
    this._privateKey = key.getPrivateKey()
}
/**
 * @return {String}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.getPublicKey = function () {
    return this._publicKey
}
/**
 * @param publicKey {String}
 * @return {void}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.setPublicKey = function (publicKey) {
    this._publicKey = publicKey
}
/**
 * @return {String}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.getPrivateKey = function () {
    return this._privateKey
}
/**
 *
 * @param privateKey {String}
 * @return {void}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.setPrivateKey = function (privateKey) {
    this._privateKey = privateKey
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.encrypt = function (data) {
    return Sm2Util.publicKeyEncrypt(this._publicKey, data)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.decrypt = function (data) {
    return Sm2Util.privateKeyDecrypt(this._privateKey, data)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.sign = function (data) {
    return Sm2Util.makeSign(this._privateKey, data)
}
/**
 *
 * @param sign {String}
 * @param data {String}
 * @return {boolean}
 */
SwlAntherdSm2AsymmetricEncryptor.prototype.verify = function (sign, data) {
    return Sm2Util.verifySign(this._publicKey, sign, data)
}

export default SwlAntherdSm2AsymmetricEncryptor
