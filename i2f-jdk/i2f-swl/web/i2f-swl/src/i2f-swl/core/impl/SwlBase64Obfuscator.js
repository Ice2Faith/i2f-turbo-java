import Base64Obfuscator from "../../lib/Base64Obfuscator";

/**
 * @implements {ISwlObfuscator}
 * @return {SwlBase64Obfuscator}
 */
function SwlBase64Obfuscator() {
    return {
        /**
         *
         * @param data {String}
         * @return {String}
         */
        encode(data) {
            return Base64Obfuscator.encode(data, true)
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        decode(data) {
            return Base64Obfuscator.decode(data)
        }
    }
}

export default SwlBase64Obfuscator
