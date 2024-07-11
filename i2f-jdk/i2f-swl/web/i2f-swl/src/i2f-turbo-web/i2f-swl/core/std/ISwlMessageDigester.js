/**
 * @return {ISwlMessageDigester}
 * @interface ISwlMessageDigester
 */
function ISwlMessageDigester() {

}

/**
 * @param data {String}
 * @return {String}
 */
ISwlMessageDigester.prototype.digest = function (data) {

}
/**
 *
 * @param digest {String}
 * @param data {String}
 * @return {boolean}
 */
ISwlMessageDigester.prototype.verify = function (digest, data) {

}

export default ISwlMessageDigester
