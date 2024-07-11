import RsaUtil from "../../lib/crypto/asymmetric/RsaUtil";
import AsymKeyPair from "@/i2f-swl/lib/crypto/asymmetric/AsymKeyPair";

/**
 * @param publicKey {String} base64
 * @param privateKey {String} base64
 * @return {SwlRsaAsymmetricEncryptor}
 * @implements {ISwlAsymmetricEncryptor}
 */
function SwlRsaAsymmetricEncryptor(publicKey = null, privateKey = null) {
    return {
        /**
         * @type {String} base64
         */
        _publicKey: null,
        /**
         * @type {String} base64
         */
        _privateKey: null,
        /**
         * @return {AsymKeyPair}
         */
        generateKeyPair() {
            return RsaUtil.genKeyPair()
        },
        /**
         * @return {AsymKeyPair}
         */
        getKeyPair() {
            return AsymKeyPair(this._publicKey, this._privateKey)
        },
        /**
         * @param key : {AsymKeyPair}
         * @return {void}
         */
        setKeyPair(key) {
            this._publicKey = key.getPublicKey()
            this._privateKey = key.getPrivateKey()
        },
        /**
         * @return {String}
         */
        getPublicKey() {
            return this._publicKey
        },
        /**
         * @param publicKey {String}
         * @return {void}
         */
        setPublicKey(publicKey) {
            this._publicKey = publicKey
        },
        /**
         * @return {String}
         */
        getPrivateKey() {
            return this._privateKey
        },
        /**
         *
         * @param privateKey {String}
         * @return {void}
         */
        setPrivateKey(privateKey) {
            this._privateKey = privateKey
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        encrypt(data) {
            return RsaUtil.publicKeyEncrypt(this._publicKey, data)
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        decrypt(data) {
            return RsaUtil.privateKeyDecrypt(this._privateKey, data)
        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        sign(data) {
            return RsaUtil.privateKeyEncrypt(this._privateKey, data)
        },
        /**
         *
         * @param sign {String}
         * @param data {String}
         * @return {boolean}
         */
        verify(sign, data) {
            let enc = RsaUtil.publicKeyDecrypt(this._publicKey, sign)
            return enc == data
        }
    }
}

export default SwlRsaAsymmetricEncryptor
