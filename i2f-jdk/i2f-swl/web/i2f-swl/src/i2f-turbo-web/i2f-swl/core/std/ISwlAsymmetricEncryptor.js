/**
 * @return {ISwlAsymmetricEncryptor}
 * @interface ISwlAsymmetricEncryptor
 */
function ISwlAsymmetricEncryptor() {

}

/**
 * @return {AsymKeyPair}
 */
ISwlAsymmetricEncryptor.prototype.generateKeyPair = function () {

}
/**
 * @return {AsymKeyPair}
 */
ISwlAsymmetricEncryptor.prototype.getKeyPair = function () {

}
/**
 * @param key {AsymKeyPair}
 * @return {void}
 */
ISwlAsymmetricEncryptor.prototype.setKeyPair = function (key) {

}
/**
 * @return {String}
 */
ISwlAsymmetricEncryptor.prototype.getPublicKey = function () {

}
/**
 * @param publicKey {String}
 * @return {void}
 */
ISwlAsymmetricEncryptor.prototype.setPublicKey = function (publicKey) {

}
/**
 * @return {String}
 */
ISwlAsymmetricEncryptor.prototype.getPrivateKey = function () {

}
/**
 *
 * @param privateKey {String}
 * @return {void}
 */
ISwlAsymmetricEncryptor.prototype.setPrivateKey = function (privateKey) {

}
/**
 *
 * @param data {String}
 * @return {String}
 */
ISwlAsymmetricEncryptor.prototype.encrypt = function (data) {

}
/**
 *
 * @param data {String}
 * @return {String}
 */
ISwlAsymmetricEncryptor.prototype.decrypt = function (data) {

}
/**
 *
 * @param data {String}
 * @return {String}
 */
ISwlAsymmetricEncryptor.prototype.sign = function (data) {

}
/**
 *
 * @param sign {String}
 * @param data {String}
 * @return {boolean}
 */
ISwlAsymmetricEncryptor.prototype.verify = function (sign, data) {

}

export default ISwlAsymmetricEncryptor
