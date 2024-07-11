import Sm3Util from "../../../../../i2f-core/crypto/digest/Sm3Util";

/**
 * @return {SwlAntherdSm3MessageDigester}
 * @implements {ISwlMessageDigester}
 */
function SwlAntherdSm3MessageDigester() {

}

/**
 * @param data {String}
 * @return {String}
 */
SwlAntherdSm3MessageDigester.prototype.digest = function (data) {
    return Sm3Util.sign(data)
}
/**
 *
 * @param digest {String}
 * @param data {String}
 * @return {boolean}
 */
SwlAntherdSm3MessageDigester.prototype.verify = function (digest, data) {
    let enc = Sha256Util.sign(data);
    return digest == enc
}

export default SwlAntherdSm3MessageDigester
