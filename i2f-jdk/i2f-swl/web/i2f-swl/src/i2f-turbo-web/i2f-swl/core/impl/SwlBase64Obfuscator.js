import Base64Obfuscator from "../../../i2f-core/codec/Base64Obfuscator";

/**
 * @implements {ISwlObfuscator}
 * @return {SwlBase64Obfuscator}
 */
function SwlBase64Obfuscator() {

}

/**
 *
 * @param data {String}
 * @return {String}
 */
SwlBase64Obfuscator.prototype.encode = function (data) {
    return Base64Obfuscator.encode(data, true)
}
/**
 *
 * @param data {String}
 * @return {String}
 */
SwlBase64Obfuscator.prototype.decode = function (data) {
    return Base64Obfuscator.decode(data)
}

export default SwlBase64Obfuscator
