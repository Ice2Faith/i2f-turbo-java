/**
 * @return {ISwlSymmetricEncryptor}
 * @interface ISwlSymmetricEncryptor
 */
function ISwlSymmetricEncryptor() {

}

/**
 * @return {String}
 */
ISwlSymmetricEncryptor.prototype.generateKey = function () {

}
/**
 * @return {String}
 */
ISwlSymmetricEncryptor.prototype.getKey = function () {

}
/**
 *
 * @param key {String}
 * @return {void}
 */
ISwlSymmetricEncryptor.prototype.setKey = function (key) {

}
/**
 *
 * @param data {String}
 * @return {String}
 */
ISwlSymmetricEncryptor.prototype.encrypt = function (data) {

},
    /**
     *
     * @param data {String}
     * @return {String}
     */
    ISwlSymmetricEncryptor.prototype.decrypt = function (data) {

    }

export default ISwlSymmetricEncryptor
