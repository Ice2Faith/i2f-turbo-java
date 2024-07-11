import Sha256Util from "../../../i2f-core/crypto/digest/Sha256Util";

/**
 * @return {SwlSha256MessageDigester}
 * @implements {ISwlMessageDigester}
 */
function SwlSha256MessageDigester() {

}

/**
 * @param data {String}
 * @return {String}
 */
SwlSha256MessageDigester.prototype.digest = function (data) {
    return Sha256Util.sign(data)
}
/**
 *
 * @param digest {String}
 * @param data {String}
 * @return {boolean}
 */
SwlSha256MessageDigester.prototype.verify = function (digest, data) {
    let enc = Sha256Util.sign(data);
    return digest == enc
}

export default SwlSha256MessageDigester
