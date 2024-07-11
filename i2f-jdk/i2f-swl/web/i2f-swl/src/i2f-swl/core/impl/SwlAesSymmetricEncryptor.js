import AesUtil from "../../lib/crypto/symmetric/AesUtil";

/**
 * @param key {String}
 * @reutrn {SwlAesSymmetricEncryptor}
 * @implements {ISwlSymmetricEncryptor}
 */
function SwlAesSymmetricEncryptor(key = null) {
    return {
        /**
         * @type {String}
         */
        _key: key,
        /**
         * @override
         * return {String}
         */
        generateKey() {
            return AesUtil.genKey()
        },
        /**
         * @override
         * @return {String}
         */
        getKey() {
            return this._key
        },
        /**
         *
         * @param key {String}
         * @return {void}
         */
        setKey(key) {
            this._key = key
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        encrypt(data) {
            return AesUtil.encrypt(data, this._key)
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        decrypt(data) {
            return AesUtil.decrypt(data, this._key)
        }
    }
}

export default SwlAesSymmetricEncryptor
