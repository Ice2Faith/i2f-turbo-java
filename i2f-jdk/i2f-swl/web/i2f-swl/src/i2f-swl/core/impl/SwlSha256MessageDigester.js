import Sha256Util from "../../lib/crypto/digest/Sha256Util";

/**
 * @return {SwlSha256MessageDigester}
 * @implements {ISwlMessageDigester}
 */
function SwlSha256MessageDigester() {
    return {
        /**
         * @param data {String}
         * @return {String}
         */
        digest(data) {
            return Sha256Util.sign(data)
        },
        /**
         *
         * @param digest {String}
         * @param data {String}
         * @return {boolean}
         */
        verify(digest, data) {
            let enc = Sha256Util.sign(data);
            return digest == enc
        }
    }
}

export default ISwlMessageDigester
